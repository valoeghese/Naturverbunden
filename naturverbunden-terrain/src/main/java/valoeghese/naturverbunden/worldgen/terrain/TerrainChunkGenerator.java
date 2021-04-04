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
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import valoeghese.naturverbunden.util.terrain.Vec2d;
import valoeghese.naturverbunden.util.terrain.Voronoi;
import valoeghese.naturverbunden.util.terrain.cache.GridOperator;
import valoeghese.naturverbunden.util.terrain.cache.LossyCache;
import valoeghese.naturverbunden.worldgen.terrain.layer.util.FleißigArea;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public class TerrainChunkGenerator extends ChunkGenerator {
	public TerrainChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings) {
		super(biomeSource, biomeSource, settings.get().getStructuresConfig(), seed);

		this.seed = seed;
		this.voronoiSeed = (int) (seed & 0xFFFFFFFF);
		this.settings = settings.get();
		this.surfaceDepthNoise = new OctaveSimplexNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));

		if (biomeSource instanceof TerrainBiomeProvider) {
			this.terrainTypeSampler = new LossyCache<>(512, ((TerrainBiomeProvider) biomeSource)::getTerrainType);
			this.terrainHeightSampler = new FleißigArea(512, this::calculateTerrainHeight);
		} else {
			throw new IllegalStateException("biome provider of a TerrainChunkGenerator must be a TerrainBiomeProvider");
		}
	}

	private final long seed;
	private final int voronoiSeed;
	private final ChunkGeneratorSettings settings;
	private final NoiseSampler surfaceDepthNoise;
	private final GridOperator<TerrainType> terrainTypeSampler;
	private final LayerSampler terrainHeightSampler;

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
				double e = this.surfaceDepthNoise.sample((double)x * 0.0625D, (double)z * 0.0625D, 0.0625D, (double)xo * 0.0625D) * 15.0D;
				region.getBiome(mutable.set(startX + xo, height, startZ + zo)).buildSurface(rand, chunk, x, z, height, e, STONE, WATER, this.getSeaLevel(), region.getSeed());
			}
		}

		//this.buildBedrock(chunk, chunkRandom);
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

		for (int x = 0; x < 16; ++x) {
			setPos.setX(x);
			int totalX = startX + x;

			for (int z = 0; z < 16; ++z) {
				setPos.setZ(z);

				int height = Math.min(chunk.getTopY() - 1, this.terrainHeightSampler.sample(totalX, startZ + z)); // this.terrainHeightSampler.sample(totalX, startZ + z)

				for (int y = chunk.getBottomY(); y < height; ++y) {
					setPos.setY(y);
					chunk.setBlockState(setPos, STONE, false);
				}

				oceanFloor.trackUpdate(x, height - 1, z, STONE);
				surface.trackUpdate(x, height - 1, z, STONE);

				if (height < seaLevel) {
					for (int y = height; y < seaLevel; ++y) {
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
		double totalHeight = 0.0;
		double totalWeight = 0.0;
		final double maxSquareRadius = 3.0 * 3.0;

		// Sample Relevant Voronoi in 5x5 area around the player for smoothing
		// This is not optimised

		final int calcX = (x >> 4);
		final int calcZ = (z >> 4);
		final Vec2d pos = new Vec2d((double) x * 0.0625, (double) z * 0.0625);

		int sampleX = 0;
		int sampleZ = 0;

		// 5x5 sample because that is the possible range of the circle
		// can't wait for some mathematical oversight to break my code somewhere amirite
		for (int xo = -4; xo <= 4; ++xo) {
			sampleX = xo + calcX;

			for (int zo = -4; zo <= 4; ++zo) {
				sampleZ = zo + calcZ;

				Vec2d voronoi = Voronoi.sampleVoronoiGrid(sampleX, sampleZ, this.voronoiSeed);
				double weight = maxSquareRadius - voronoi.squaredDist(pos);

				// this is kept square-weighted because sqrt is a trash not pog not based operation and is slower than the hare from aesop's fables
				if (weight > 0) {
					totalWeight += weight;
					TerrainType type = this.terrainTypeSampler.get(MathHelper.floor(voronoi.getX() * 16.0), MathHelper.floor(voronoi.getY() * 16.0));
					RegistryKey<Biome> biome = type.getBiome();

					if (biome == BiomeKeys.RIVER || biome == BiomeKeys.FROZEN_RIVER) {
						weight *= 2;
					}

					totalHeight += weight * type.getHeight(x, z);
				}
			}
		}

		return (int) (totalHeight / totalWeight);
	}

	@Override
	public int getHeight(int x, int z, Type heightmap, HeightLimitView world) {
		// Lazy Implementation
		int height = this.terrainHeightSampler.sample(x, z);
		int seaLevel = this.getSeaLevel();

		if (height < seaLevel && heightmap.getBlockPredicate().test(WATER)) {
			return seaLevel;
		}

		return height;
	}

	@Override
	public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
		// TODO Auto-generated method stub
		//super.generateFeatures(region, accessor);
	}
	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
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

	public static final BlockState STONE = Blocks.STONE.getDefaultState();
	public static final BlockState AIR = Blocks.AIR.getDefaultState();
	public static final BlockState WATER = Blocks.WATER.getDefaultState();

}
