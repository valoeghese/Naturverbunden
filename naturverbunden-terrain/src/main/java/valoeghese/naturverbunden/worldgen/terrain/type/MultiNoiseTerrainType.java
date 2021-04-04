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
public class MultiNoiseTerrainType extends TerrainType {
	public MultiNoiseTerrainType(RegistryKey<Biome> biome, double baseHeight) {
		super(biome, null);

		this.baseHeight = baseHeight;
	}

	public MultiNoiseTerrainType addNoise(Noise noise, double frequency, double amplitude) {
		this.noises.add(new ConfiguredNoise(noise, frequency, amplitude));
		return this;
	}

	public MultiNoiseTerrainType addNoise(Noise noise, double frequency, double amplitudeHigh, double amplitudeLow) {
		this.noises.add(new ConfiguredComplexNoise(noise, frequency, amplitudeHigh, amplitudeLow));
		return this;
	}

	private final List<ConfiguredNoise> noises = new ArrayList<>();
	private final double baseHeight;

	@Override
	public double getHeight(int x, int z) {
		double result = this.baseHeight;

		for (ConfiguredNoise noise : this.noises) {
			result += noise.sample(x, z);
		}

		return result;
	}

	private static class ConfiguredNoise {
		public ConfiguredNoise(Noise noise, double frequency, double amplitude) {
			this.noise = noise;
			this.frequency = frequency;
			this.amplitude = amplitude;
		}

		final Noise noise;
		final double frequency;
		private final double amplitude;
		
		public double sample(double x, double z) {
			return this.noise.sample(x * this.frequency, z * this.frequency) * this.amplitude;
		}
	}
	
	private static class ConfiguredComplexNoise extends ConfiguredNoise {
		public ConfiguredComplexNoise(Noise noise, double frequency, double amplitudeHigh, double amplitudeLow) {
			super(noise, frequency, 0.0);
			this.amplitudeHigh = amplitudeHigh;
			this.amplitudeLow = amplitudeLow;
		}

		private final double amplitudeHigh;
		private final double amplitudeLow;
		
		@Override
		public double sample(double x, double z) {
			double preliminary = this.noise.sample(x * this.frequency, z * this.frequency);
			return preliminary * (preliminary < 0 ? this.amplitudeLow : this.amplitudeHigh);
		}
	}
}
