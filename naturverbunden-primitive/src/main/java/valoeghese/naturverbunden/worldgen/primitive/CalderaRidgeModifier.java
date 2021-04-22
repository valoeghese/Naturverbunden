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

package valoeghese.naturverbunden.worldgen.primitive;

import valoeghese.naturverbunden.util.terrain.Voronoi;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider.Properties;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider.TerrainTypeModifier;
import valoeghese.naturverbunden.worldgen.terrain.layer.TerrainInfoSampler.Info;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public class CalderaRidgeModifier implements TerrainTypeModifier {
	CalderaRidgeModifier(long seed) {
		this.seed = Voronoi.seedFromLong(seed - 10);
//		this.terrainHotSprings 
	}
	
	private final int seed;
	private final TerrainType terrainHotSprings;

	@Override
	public TerrainType apply(TerrainType original, TerrainBiomeProvider source, int x, int z, Properties properties, Info info) {
		double voronoi = Voronoi.sampleD1Worley(x * FREQUENCY, z * FREQUENCY, this.seed);
	}

	private static final double FREQUENCY = 1 / 4200.0;
}
