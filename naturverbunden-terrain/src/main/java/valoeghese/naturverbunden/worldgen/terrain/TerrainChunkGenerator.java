/*
 * Naturverbunden
 * Copyright (C) 2021 Valoeghese
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package valoeghese.naturverbunden.worldgen.terrain;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.NoiseCaveSampler;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import valoeghese.naturverbunden.util.terrain.Vec2d;
import valoeghese.naturverbunden.util.terrain.Voronoi;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider;
import valoeghese.naturverbunden.worldgen.terrain.layer.util.FleißigArea;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

// TODO rivers carve through tall terrain
public class TerrainChunkGenerator extends ChunkGenerator {
	public TerrainChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings) {
		super(biomeSource, biomeSource, settings.get().getStructuresConfig(), seed);

		this.seed = seed;
		this.voronoiSeed = Voronoi.seedFromLong(seed);
		this.settings = settings.get();
		this.surfaceDepthNoise = new OctaveSimplexNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));

		if (biomeSource instanceof TerrainBiomeProvider) {
			this.terrainHeightSampler = new FleißigArea(1024, this::calculateTerrainHeight);
		} else {
			throw new IllegalStateException("biome provider of a TerrainChunkGenerator must be a TerrainBiomeProvider");
		}

		this.noiseCaves = new NoiseCaveSampler(new ChunkRandom(seed - 1), this.settings.getGenerationShapeConfig().getMinimumY());
	}

	private final long seed;
	private final int voronoiSeed;
	private final ChunkGeneratorSettings settings;
	private final NoiseSampler surfaceDepthNoise;
	private final LayerSampler terrainHeightSampler;
	private final NoiseCaveSampler noiseCaves;

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new TerrainChunkGenerator(this.populationSource.withSeed(seed), seed, () -> this.settings);
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		ChunkRandom rand = new ChunkRandom();
		rand.setTerrainSeed(chunkX, chunkZ);

		int startX = chunkPos.getStartX();
		int startZ = chunkPos.getStartZ();
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for(int xo = 0; xo < 16; ++xo) {
			for(int zo = 0; zo < 16; ++zo) {
				int x = startX + xo;
				int z = startZ + zo;
				int height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, xo, zo) + 1;
				double noise = this.surfaceDepthNoise.sample((double)x * 0.0625D, (double)z * 0.0625D, 0.0625D, (double)xo * 0.0625D) * 15.0D;
				int minSurfaceLevel = this.settings.getMinSurfaceLevel();
				region.getBiome(mutable.set(startX + xo, height, startZ + zo)).buildSurface(rand, chunk, x, z, height, noise, STONE, WATER, this.getSeaLevel(), minSurfaceLevel, region.getSeed());
			}
		}

		//this.buildBedrock(chunk, chunkRandom);
	}

	private double getApparentRockDensity(int height, int y) {
		final double magicDensityConstant = 12.75/3.0;
		final double distrustLevel = 4.0;
		return magicDensityConstant * (height - y) - distrustLevel; // gradient -magicDensityConstant, offset magicDensityConstant * height - distrustLevel.
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, StructureAccessor accessor, Chunk chunk) {
		ChunkPos pos = chunk.getPos();
		int seaLevel = this.getSeaLevel();
		BlockPos.Mutable setPos = new BlockPos.Mutable();
		Heightmap oceanFloor = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap surface = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
		final int startX = pos.getStartX();
		final int startZ = pos.getStartZ();

		// @Reason: CC Compat
		int[] heights = new int[17 * 17];
		int[] trueHeights = new int[16 * 16];

		for (int x = 0; x < 17; ++x) {
			int totalX = startX + x;

			for (int z = 0; z < 17; ++z) {
				int totalZ = startZ + z;
				int sample = this.terrainHeightSampler.sample(totalX, totalZ);

				if (z < 16 && x < 16) {
					trueHeights[(x * 16) + z] = sample;
				}

				heights[(x * 17) + z] = Math.min(chunk.getTopY() - 1, sample);
			}
		}

		/*Perlerper cavess = new Perlerper(this.getWorldHeight(), startX, chunk.getBottomY(), startZ, (x, y, z) -> {
			final double extraDistrustLevel = 20.0; // Perlerp is not trustworthy

			int xpos = x << 2; // 0 to 16 bc raw val is 0-4 (range:5)
			int zpos = z << 2;
			int ypos = (y << 3) - 64;
			int height = heights[(xpos * 17) + zpos];
			double extraDistrust = (1.0 / 20.0) * (height + ypos);

			return MathHelper.clamp(extraDistrust, 0.0, 1.0) * extraDistrustLevel + this.noiseCaves.sample(startX + xpos, ypos, startZ + zpos, this.getApparentRockDensity(height, ypos));
		});*/

		for (int x = 0; x < 16; ++x) {
			int totalX = startX + x;
			setPos.setX(x);

			for (int z = 0; z < 16; ++z) {
				int totalZ = startZ + z;
				setPos.setZ(z);

				int height = heights[(x * 17) + z];
				int grimstoneHeight = MathHelper.floor(3 * MathHelper.sin(totalX * 0.01f) + 3 * MathHelper.sin(totalZ * 0.01f));
				BlockState state;

				for (int y = chunk.getBottomY(); y < height; ++y) {
					try {
						state = y < grimstoneHeight ? GRIMSTONE : STONE;

						//if (y > -64 && (y < height - 1 || height > seaLevel + 1) && cavess.sample(x, y, z) < 0.0) {
						//	state = CAVE_AIR;
						//}

						setPos.setY(y);
						chunk.setBlockState(setPos, state, false);
					} catch (RuntimeException e) {
						System.out.println("e" + this.getWorldHeight());
						System.out.println("r" + this.getMinimumY());
						System.out.println(chunk.getBottomY());
						throw e;
					}
				}

				oceanFloor.trackUpdate(x, height - 1, z, STONE);
				surface.trackUpdate(x, height - 1, z, STONE);

				int trueHeight = trueHeights[(x * 16) + z];

				if (trueHeight < seaLevel && height < chunk.getTopY()) { // Second Check @Reason: CC Compat
					int cap = Math.min(seaLevel, chunk.getTopY()); // @Reason CC Compat

					for (int y = height; y < cap; ++y) {
						setPos.setY(y);
						chunk.setBlockState(setPos, WATER, false);
					}
				}

				oceanFloor.trackUpdate(x, seaLevel - 1, z, WATER); // for oceanFloor probably not necessary
				surface.trackUpdate(x, seaLevel - 1, z, WATER);
			}
		}

		return CompletableFuture.completedFuture(chunk);
	}

	private int calculateTerrainHeight(int x, int z) {
		double height = 0.0;
		double totalWeight = 0.0;
		final double maxSquareRadius = 9.0; // 3.0 * 3.0;

		// Sample Relevant Voronoi in 5x5 area around the player for smoothing
		// This is not optimised

		int calcX = (x >> 4);
		int calcZ = (z >> 4);
		Vec2d pos = new Vec2d((double) x * 0.0625, (double) z * 0.0625);

		int sampleX = 0;
		int sampleZ = 0;

		// 9x9 sample because that is the possible range of the circle
		// can't wait for some mathematical oversight to break my code somewhere amirite
		for (int xo = -4; xo <= 4; ++xo) {
			sampleX = xo + calcX;

			for (int zo = -4; zo <= 4; ++zo) {
				sampleZ = zo + calcZ;

				Vec2d voronoi = Voronoi.sampleVoronoiGrid(sampleX, sampleZ, this.voronoiSeed);
				double sqrDist = voronoi.squaredDist(pos);
				double weight = maxSquareRadius - sqrDist;

				// this is kept square-weighted because sqrt is a trash not pog not based operation and is slower than the hare from aesop's fables
				if (weight > 0) {
					TerrainType type = ((TerrainBiomeProvider) this.biomeSource).sampleTerrainType(MathHelper.floor(voronoi.getX() * 16.0), MathHelper.floor(voronoi.getY() * 16.0));

					totalWeight += weight;
					height += weight * type.getHeight(x, z);
				}
			}
		}

		// Complete the average
		height = height / totalWeight;
		double riverGen = ((TerrainBiomeProvider) this.biomeSource).sampleRiver(x, z);
		riverGen = Math.max(riverGen, 0.0);
		double mtnChainForRivers = Math.min(1.0, (2.5 * ((TerrainBiomeProvider) this.biomeSource).getMtnChainVal(x, z)));

		final double river = RIVER_HEIGHT * (1.0 - mtnChainForRivers) + height * mtnChainForRivers;
		return (int) (MathHelper.lerp(riverGen, height, river));
	}

	@Override
	public int getHeight(int x, int z, Type heightmap, HeightLimitView world) {
		// Lazy Implementation
		int height = this.terrainHeightSampler.sample(x, z);
		int seaLevel = this.getSeaLevel();

		if (height < seaLevel && heightmap.getBlockPredicate().test(WATER)) {
			return seaLevel - 1;
		}

		return height;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		// TODO should I implement cave stuff here too
		BlockState[] states = new BlockState[world.getHeight()];
		int height = this.terrainHeightSampler.sample(x, z);

		int i = 0;
		int y;

		for (y = world.getBottomY(); y < height; ++y) {
			states[i++] = STONE;
		}

		int seaLevel = this.getSeaLevel();

		while (y++ < seaLevel) {
			states[i++] = WATER;
		}

		while (i < states.length) {
			states[i++] = AIR;
		}

		return new VerticalBlockSample(world.getBottomY(), states);
	}

	@Override
	public void populateEntities(ChunkRegion region) {
		ChunkPos chunkPos = region.getCenterPos();
		Biome biome = region.getBiome(chunkPos.getStartPos());
		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setPopulationSeed(region.getSeed(), chunkPos.getStartX(), chunkPos.getStartZ());
		SpawnHelper.populateEntities(region, biome, chunkPos, chunkRandom);
	}

	@Override
	public int getWorldHeight() {
		return this.settings.getGenerationShapeConfig().getHeight();
	}

	@Override
	public int getSeaLevel() {
		return this.settings.getSeaLevel();
	}

	@Override
	public int getMinimumY() {
		return this.settings.getGenerationShapeConfig().getMinimumY();
	}

	public static TerrainChunkGenerator create(Registry<Biome> biomeReg, Registry<ChunkGeneratorSettings> settingsReg, long seed) {
		return new TerrainChunkGenerator(new TerrainBiomeProvider(biomeReg, seed), seed, () -> settingsReg.getOrThrow(ChunkGeneratorSettings.OVERWORLD));
	}

	public static final Codec<TerrainChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
	instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(chunkGenerator -> chunkGenerator.populationSource),
			Codec.LONG.fieldOf("seed").stable().forGetter(chunkGenerator -> chunkGenerator.seed),
			ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(chunkGenerator -> () -> chunkGenerator.settings))
	.apply(instance, TerrainChunkGenerator::new));

	public static final BlockState GRIMSTONE = Blocks.DEEPSLATE.getDefaultState();
	public static final BlockState STONE = Blocks.STONE.getDefaultState();
	public static final BlockState AIR = Blocks.AIR.getDefaultState();
	public static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	public static final BlockState WATER = Blocks.WATER.getDefaultState();
	public static final double RIVER_HEIGHT = 61.0;
}
