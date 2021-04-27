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

import java.util.Random;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import valoeghese.naturverbunden.util.terrain.Noise;

/**
 * Terrain type that generates height with a simple octave simplex noise algorithm.
 */
@Deprecated
public class SimpleSimplexTerrainType extends TerrainType {
	public SimpleSimplexTerrainType(RegistryKey<Biome> biome, Random seed, int octaves, double baseHeight, double frequency, double amplitude) {
		super(biome, null);

		this.noise = new Noise(seed, octaves);
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.baseHeight = baseHeight;
	}

	private final Noise noise;
	private final double frequency;
	private final double amplitude;
	private final double baseHeight;

	@Override
	public double getHeight(int x, int z) {
		return this.baseHeight + this.amplitude * this.noise.sample(x * this.frequency, z * this.frequency);
	}

}
