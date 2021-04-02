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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.util.terrain.RidgedSimplexGenerator;

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
		this.mountainChainNoise = new Noise(gr, 3, RidgedSimplexGenerator::new);
		this.tempOffset = (gr.nextDouble() - 0.5) * 20; // -10 to 10
	}

	private final long seed;
	private final Noise humidityNoise;
	private final Noise mountainChainNoise;
	private final double tempOffset;

	private final Registry<Biome> biomeRegistry;

	// Terrain

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		// TODO Auto-generated method stub
		return null;
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
}
