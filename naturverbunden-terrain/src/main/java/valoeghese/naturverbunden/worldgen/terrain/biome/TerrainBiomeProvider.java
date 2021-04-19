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

package valoeghese.naturverbunden.worldgen.terrain.biome;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import valoeghese.naturverbunden.core.NVBMathUtils;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.util.terrain.cache.DoubleGridOperator;
import valoeghese.naturverbunden.util.terrain.cache.GridOperator;
import valoeghese.naturverbunden.util.terrain.cache.LossyCache;
import valoeghese.naturverbunden.util.terrain.cache.LossyDoubleCache;
import valoeghese.naturverbunden.worldgen.terrain.layer.TerrainInfoSampler;
import valoeghese.naturverbunden.worldgen.terrain.layer.util.Layers;
import valoeghese.naturverbunden.worldgen.terrain.type.MountainEdgeTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainCategory;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public class TerrainBiomeProvider extends BiomeSource {
	public static final Codec<TerrainBiomeProvider> CODEC = RecordCodecBuilder.create(instance ->
	instance.group(
			RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(s -> s.biomeRegistry),
			Codec.LONG.fieldOf("seed").stable().forGetter(s -> s.seed))
	.apply(instance, instance.stable(TerrainBiomeProvider::new)));

	public TerrainBiomeProvider(Registry<Biome> biomes, long seed) {
		super(biomes.stream().map(b -> () -> b));

		this.biomeRegistry = biomes;
		this.seed = seed;

		Random gr = new Random(seed);
		this.humidityNoise = new Noise(gr, 1);
		this.mountainChain = new Noise(gr, 1);
		this.mountainChainStretch = new Noise(gr, 1);
		this.tempOffset = (gr.nextDouble() - 0.5) * 6; // -3 to 3
		this.infoSampler = Layers.build(seed);

		this.terrainTypeSampler = new LossyCache<>(512, this::getTerrainType);

		// Terrain Types
		gr.setSeed(seed + 1);

		this.terrain = new VanillaTerrainTypes(gr);
		this.climates = new Climates(this.terrain);

		gr.setSeed(seed + 2);
		RiverSampler rivers = new RiverSampler(gr);
		this.rivers = new LossyDoubleCache(512, (x, z) -> {
			double base = rivers.sample(x, z);

			// Copied From Terrain Sample (below)
			final double chainCutoff = 0.17;
			final double chainNormaliser = 1 / chainCutoff;

			double chainSample = this.getChainSample(x, z);
			double mountainChain = chainCutoff - Math.abs(chainSample);
			// normalised between 0 and 1
			mountainChain = chainNormaliser * Math.max(0.0, mountainChain);
			// =====
			return Math.max(base - 3 * mountainChain, 0.0);
		});
		this.rawMountains = new LossyDoubleCache(512, this::getChainSample);
	}

	private final long seed;
	private final DoubleGridOperator rawMountains;
	private final DoubleGridOperator rivers;
	private final Noise humidityNoise;
	private final Noise mountainChain;
	private final Noise mountainChainStretch;
	private final double tempOffset;
	private final TerrainInfoSampler infoSampler;
	private final GridOperator<TerrainType> terrainTypeSampler;

	private final Registry<Biome> biomeRegistry;

	private final VanillaTerrainTypes terrain;
	private final Climates climates;

	// Terrain

	public TerrainType sampleTerrainType(int x, int z) {
		return this.terrainTypeSampler.get(x, z);
	}

	private double getChainSample(int x, int z) {
		final double stretchFrequency = 1.0 / 820.0;
		final double chainFrequency = 1.0 / 4500.0;

		// Chain Sample. Used for mountain chains and fake orthographic lift humidity modification
		double chainStretch = this.mountainChainStretch.sample(x * stretchFrequency, z * stretchFrequency);

		double chainSample = 0;

		if (chainStretch >= 0) {
			chainSample = this.mountainChain.sample(x * chainFrequency * (1.0 - 0.33 * chainStretch), z * chainFrequency);
		} else {
			chainSample = this.mountainChain.sample(x * chainFrequency, z * chainFrequency * (1.0 + 0.33 * chainStretch));
		}

		return chainSample;
	}

	private TerrainType getTerrainType(int x, int z) {
		// Don't touch mountains here without mirroring your changes in the river sampler
		final double humidityFrequency = 1.0 / 920.0;
		final double chainCutoff = 0.17;
		final double chainNormaliser = 1 / chainCutoff;

		double chainSample = this.rawMountains.get(x, z);

		// mountain terrain strength.
		double mountainChain = chainCutoff - Math.abs(chainSample);
		boolean applyLiftToHumidity = mountainChain > -chainCutoff;
		// normalised between 0 and 1
		mountainChain = chainNormaliser * Math.max(0.0, mountainChain);
		TerrainInfoSampler.Info terrainInfo = this.infoSampler.sample(x >> 2, z >> 2);

		if (mountainChain > 0.6 && terrainInfo.category == TerrainCategory.LAND) {
			return this.terrain.terrainMountains;
		} else { // Using an else block for readability
			// Humidity from -1 to 1. Initial: -0.9 to 0.9
			double humidity = this.humidityNoise.sample(x * humidityFrequency, z * humidityFrequency) * 0.9; // cannot get more extreme climate values without additional stuff below

			// Fake Orthographic Lift and Rain Shadow
			if (applyLiftToHumidity) {
				humidity += 0.35 * (chainSample > 0 ? 1 : -1); // +0.35 and -0.35. Total of 0.7 change.
			}

			// 
			// This temperature ranges from 0-4, and represents HOW COLD SOMETHING IS
			// IF YOU ARE READING THIS PLEASE NOTE THAT HIGHER VALUES ARE COLDER TEMPERATURES

			int temperature = calculateTemperature(z + 80 * MathHelper.sin(0.01f * x));

			// Humidity Increases and Decreases due to high and low pressure from circulation cells.
			// We simplify the code and thus it's like there are more circulation cells here than on earth
			// But it's fiiiiiiiine
			double humidityIncrease = calculateHumidityIncrease(z);
			humidity += humidityIncrease;

			if (terrainInfo.category == TerrainCategory.OCEAN) {
				final int deepCheckDist = 8;
				boolean i5 = this.infoSampler.sample((x >> 2) + deepCheckDist, (z >> 2) + deepCheckDist).category == TerrainCategory.OCEAN;
				boolean _09 = this.infoSampler.sample((x >> 2) + deepCheckDist, (z >> 2) - deepCheckDist).category == TerrainCategory.OCEAN;
				boolean vc = this.infoSampler.sample((x >> 2) - deepCheckDist, (z >> 2) + deepCheckDist).category == TerrainCategory.OCEAN;
				boolean b = this.infoSampler.sample((x >> 2) - deepCheckDist, (z >> 2) - deepCheckDist).category == TerrainCategory.OCEAN;

				return i5 && _09 && vc && b ? this.terrain.terrainDeepOcean : this.terrain.terrainOcean;
			} else if (terrainInfo.category == TerrainCategory.SMALL_BEACH) {
				return temperature == 3 ? this.terrain.terrainBeachFrozen : this.terrain.terrainBeach;
			} else if (terrainInfo.category == TerrainCategory.LARGE_BEACH) {
				humidity += 0.1;
				mountainChain -= 0.5;
			}

			TerrainType primaryTerrain = this.climates.sample(temperature, humidity, terrainInfo);

			if (primaryTerrain == null) {
				throw new IllegalStateException("WTF 2 electric boogaloo. Humidity " + humidity + ", MountainChain " + mountainChain + ", InfoBits " + terrainInfo.info + " TerrainCategory " + terrainInfo.category.name());
			}

			if (mountainChain > 0) {
				// Because mountainChain edge goes from 0 to 0.5, multiply by 2.
				return new MountainEdgeTerrainType(primaryTerrain, this.terrain.terrainMountains, mountainChain, terrainInfo.isLargeHills(), temperature < 2 && humidity < 0.4);
			} else {
				return primaryTerrain;
			}
		}
	}

	public double sampleRiver(int x, int z) {
		return this.rivers.get(x, z);
	}

	// https://www.desmos.com/calculator/1pprr73tpn

	private int calculateTemperature(double z) {
		z = Math.abs(this.tempOffset + z / TEMPERATURE_CELL_SIZE);
		z = Math.min(z, 4) + 0.5;
		return (int) z;
	}

	private double calculateHumidityIncrease(double z) {
		z = Math.abs(this.tempOffset + z / TEMPERATURE_CELL_SIZE);
		z = Math.min(z, 5);
		z = NVBMathUtils.tri(z) - 0.5;
		return 0.8 * z;
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		TerrainType type = this.sampleTerrainType(biomeX << 2, biomeZ << 2);
		double rivers = this.sampleRiver(biomeX << 2, biomeZ << 2);

		if (type.getCategory() != Biome.Category.OCEAN && rivers > 0.7) {
			// TODO put river type as a parameter of the gen type
			return this.biomeRegistry.get(type.getCategory() == Biome.Category.ICY ? BiomeKeys.FROZEN_RIVER : BiomeKeys.RIVER);
		}

		return this.biomeRegistry.get(type.getBiome());
	}

	// Boring Stuff

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new TerrainBiomeProvider(this.biomeRegistry, seed);
	}

	// Temperature Scale
	private static final double TEMPERATURE_CELL_SIZE = 1.0 / 600.0;
}
