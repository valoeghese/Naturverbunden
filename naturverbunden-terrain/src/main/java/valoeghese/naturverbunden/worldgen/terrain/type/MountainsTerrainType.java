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

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.util.terrain.RidgedSimplexGenerator;

/**
 * Represents mountain terrain.
 */
public class MountainsTerrainType extends TerrainType {
	public MountainsTerrainType(Random seed) {
		super(BiomeKeys.MOUNTAINS, Biome.Category.EXTREME_HILLS);
		this.mountainChainNoise = new Noise(seed, 3, RidgedSimplexGenerator::new);
	}

	private final Noise mountainChainNoise;

	@Override
	public double getHeight(int x, int z) {
		return 200.0 + this.mountainChainNoise.sample(x * FREQUENCY, z * FREQUENCY) * 50.0;
	}

	private static final double FREQUENCY = 1.0 / 150.0;
}
