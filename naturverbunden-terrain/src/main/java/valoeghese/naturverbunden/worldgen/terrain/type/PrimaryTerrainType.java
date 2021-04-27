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

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import valoeghese.naturverbunden.util.terrain.Noise;

/**
 * Terrain type that generates height with mutliple provided noise algorithms.
 */
public class PrimaryTerrainType extends TerrainType {
	public PrimaryTerrainType(RegistryKey<Biome> biome, double baseHeight) {
		super(biome, null);

		this.baseHeight = baseHeight;
	}

	public PrimaryTerrainType addNoise(Noise noise, double frequency, double amplitude) {
		this.noises.add(
				(x, z) -> noise.sample(x * frequency, z * frequency) * amplitude);
		return this;
	}

	public PrimaryTerrainType addNoise(Noise noise, double frequency, double amplitudeHigh, double amplitudeLow) {
		this.noises.add((x, z) -> {
			double preliminary = noise.sample(x * frequency, z * frequency);
			return preliminary * (preliminary < 0 ? amplitudeLow : amplitudeHigh);
		});
		return this;
	}

	public PrimaryTerrainType addNoise(Noise noise, double frequency, double amplitudeHigh, double amplitudeLow, double offset) {
		this.noises.add((x, z) -> {
			double preliminary = noise.sample(x * frequency, z * frequency) + offset;
			return preliminary * (preliminary < 0 ? amplitudeLow : amplitudeHigh);
		});
		return this;
	}

	private final List<HeightModifier> noises = new ArrayList<>();
	private final double baseHeight;

	@Override
	public double getHeight(int x, int z) {
		double result = this.baseHeight;

		for (HeightModifier noise : this.noises) {
			result += noise.sample(x, z);
		}

		return result;
	}
}
