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

package valoeghese.naturverbunden.worldgen.terrain.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import valoeghese.naturverbunden.util.terrain.Noise;

/**
 * Terrain type that generates height with mutliple provided noise algorithms.
 */
public class PrimaryTerrainType extends TerrainType {
	public PrimaryTerrainType(RegistryKey<Biome> biome, Random rand, double baseHeight) {
		super(biome, null);

		this.rand = new Random(rand.nextLong());
		this.baseHeight = baseHeight;
	}

	private final Random rand;
	private final double baseHeight;
	private final List<HeightModifier> generators = new ArrayList<>();

	public void switchBiome(RegistryKey<Biome> nextBiome) {
		this.biome = nextBiome;
		Biome builtin = BuiltinRegistries.BIOME.get(biome);

		if (builtin == null) {
			throw new IllegalStateException("Tried to auto-provide category from the builtin registry, as specified by the terrain type. Found no matching entry for " + biome.getValue() + "!");
		}

		this.category = builtin.getCategory();
	}

	public PrimaryTerrainType addGenerator(HeightModifier modifier) {
		this.generators.add(modifier);
		return this;
	}

	// @WillProbablyDeprecateSoon
	public PrimaryTerrainType addNoise(Noise noise, double frequency, double amplitude) {
		this.generators.add(
				(x, z) -> noise.sample(x * frequency, z * frequency) * amplitude);
		return this;
	}

	// @WillProbablyDeprecateSoon
	public PrimaryTerrainType addNoise(Noise noise, double frequency, double amplitudeHigh, double amplitudeLow) {
		this.generators.add((x, z) -> {
			double preliminary = noise.sample(x * frequency, z * frequency);
			return preliminary * (preliminary < 0 ? amplitudeLow : amplitudeHigh);
		});
		return this;
	}

	// @WillProbablyDeprecateSoon
	public PrimaryTerrainType addNoise(Noise noise, double frequency, double amplitudeHigh, double amplitudeLow, double offset) {
		this.generators.add((x, z) -> {
			double preliminary = noise.sample(x * frequency, z * frequency) + offset;
			return preliminary * (preliminary < 0 ? amplitudeLow : amplitudeHigh);
		});
		return this;
	}

	public Random getRandom() {
		return this.rand;
	}

	public int nextInt(int bound) {
		return this.rand.nextInt(bound);
	}

	@Override
	public double getHeight(int x, int z) {
		double result = this.baseHeight;

		for (HeightModifier noise : this.generators) {
			result += noise.sample(x, z);
		}

		return result;
	}
}
