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
import valoeghese.naturverbunden.util.terrain.OpenSimplexGenerator;

public class TerracedTerrainType extends TerrainType {
	public TerracedTerrainType(RegistryKey<Biome> biome, Random seed, int layers, double frequency, double baseHeight, double separation, Noise additional) {
		super(biome, null);

		this.baseHeight = baseHeight;
		this.layers = layers;
		this.frequency = frequency;
		this.separation = separation;
		this.noise = new OpenSimplexGenerator(seed);
		this.additional = additional;
	}

	private final double baseHeight;
	private final double frequency;
	private final double separation;
	private final int layers;
	private final OpenSimplexGenerator noise;
	private final Noise additional;

	@Override
	public double getHeight(int x, int z) {
		// OLD: \operatorname{floor}\left(k\left|x\right|\right)\left\{-1\le x\le1\right\}
		//double layer = this.separation * Math.floor(this.layers * Math.abs(this.noise.sample(x * this.frequency, z * this.frequency)));
		
		// NEW: \min\left(k,\operatorname{floor}\left(\left|1.5kx\right|\right)\right)\left\{-1\le x\le1\right\}
		double layer = this.separation * Math.min(
				this.layers,
				Math.floor(1.5 * this.layers * Math.abs(this.noise.sample(x * this.frequency, z * this.frequency)))
				);
		double hills = this.separation * 0.4 * this.additional.sample(x * this.frequency * 3, z * this.frequency * 3);
		return hills + this.baseHeight + layer;
	}
}
