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

import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.worldgen.terrain.layer.TerrainInfoSampler;
import valoeghese.naturverbunden.worldgen.terrain.layer.util.Layers;
import valoeghese.naturverbunden.worldgen.terrain.type.MountainsTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.SimpleSimplexTerrainType;
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
		this.tempOffset = (gr.nextDouble() - 0.5) * 6.66; // -3.33 to 3.33
		this.infoSampler = Layers.build(seed);

		// Terrain Types
		gr.setSeed(seed + 1);
		this.terrainMountains = new MountainsTerrainType(gr);
		this.terrainRiver = new SimpleSimplexTerrainType(BiomeKeys.RIVER, gr, 1, 58.0, 0.2, 1.0);
		this.terrainRiverFrozen = new SimpleSimplexTerrainType(BiomeKeys.FROZEN_RIVER, gr, 1, 58.0, 0.2, 1.0);
	}

	private final long seed;
	private final Noise humidityNoise;
	private final Noise mountainChain;
	private final Noise mountainChainStretch;
	private final double tempOffset;
	private final TerrainInfoSampler infoSampler;

	private final Registry<Biome> biomeRegistry;

	private final TerrainType terrainMountains;
	private final TerrainType terrainRiver;
	private final TerrainType terrainRiverFrozen;

	// Terrain

	public TerrainType getTerrainType(int x, int z) {
		final double humidityFrequency = 1.0 /600.0;
		final double chainFrequency = 1.0 / 1200.0;
		final double chainCutoff = 0.2;
		final double chainNormaliser = 1 / chainCutoff;

		// Chain Sample. Used for mountain chains and fake orthographic lift humidity modification
		double chainSample = this.mountainChain.sample(x * chainFrequency, z);
		// mountain terrain strength.
		double mountainChain = chainCutoff - Math.abs(chainSample);
		boolean applyLiftToHumidity = mountainChain > -chainCutoff;
		// normalised between 0 and 1
		mountainChain = chainNormaliser * Math.max(0.0, mountainChain);

		if (mountainChain > 0.5) {
			return this.terrainMountains;
		} else { // Using an else block for readability
			// Humidity from -1 to 1
			double humidity = this.humidityNoise.sample(x * humidityFrequency, z * humidityFrequency);

			// Fake Orthographic Lift and Rain Shadow
			if (applyLiftToHumidity) {
				humidity += 0.3 * (chainSample > 0 ? 1 : -1);
			}

			// https://www.desmos.com/calculator/iab0cvwydf
			// This temperature ranges from 0-3, and represents HOW COLD SOMETHING IS
			// IF YOU ARE READING THIS PLEASE NOTE THAT HIGHER VALUES ARE COLDER TEMPERATURES
			// 0 = Equatorial (Mostly rainforest, No Deserts) - This humidity is handled below
			// 1 = Subtropical (Mostly Deserts, Medditeranean, Savannah)
			// 2 = Temperate (More cool stuff, boreal, deciduous, temperate rainforest, grasslands, moors)
			// 3 = Polar (Snow stuff)
			int temperature = calculateTemperature(z + 80 * MathHelper.sin(0.01f * x));
			
			// Equatorial Humidity Increase due to rising air in hadley cells. Value from 0 to 0.5
			double humidityIncrease = Math.abs(2 * (z * TEMPERATURE_SCALE + this.tempOffset));
			humidityIncrease = Math.max(0.0, 1.0 - (humidityIncrease * humidityIncrease));
			humidity += 0.5 * humidityIncrease; // 0-1 -> 0-0.5 and append

			TerrainInfoSampler.Info terrainInfo = this.infoSampler.sample(x >> 2, z >> 2);

			if (mountainChain < 0.05 && terrainInfo.category == TerrainCategory.RIVER) {
				return temperature == 3 ? this.terrainRiverFrozen : this.terrainRiver;
			}

			
		}
	}

	private int calculateTemperature(double z) {
		double rawVal = Math.abs(z * TEMPERATURE_SCALE + this.tempOffset) + 0.5;
		return Math.min(3, MathHelper.floor(rawVal));
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return this.biomeRegistry.get(this.getTerrainType(biomeX << 2, biomeZ << 2).getBiome());
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

	private static final double TEMPERATURE_SCALE = 1.0 / 450.0;
}
