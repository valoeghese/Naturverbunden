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

import net.minecraft.world.biome.BiomeKeys;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.util.terrain.RidgedSimplexGenerator;
import valoeghese.naturverbunden.worldgen.terrain.type.MountainsTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.MultiNoiseTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.SimpleSimplexTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public class TerrainTypes {
	public TerrainTypes(Random seed) {
		this.terrainMountains = new MountainsTerrainType(seed);

		// The equivalent of future "Jungle"
		this.terrainJungle = new SimpleSimplexTerrainType(BiomeKeys.JUNGLE, seed, 1, 70.0, 1.0 / 100.0, 10.0);
		// Rainforest will be more mountainous
		// The equivalent of future "Savanna"
		this.terrainSavannahHills = new MultiNoiseTerrainType(BiomeKeys.SAVANNA, 84.0)
				.addNoise(new Noise(seed, 2, RidgedSimplexGenerator::new), 1.0 / 240.0, 22.0)
				.addNoise(new Noise(seed, 1), 1.0 / 90.0, 12.0);

		// The equivalent of future "Rolling Plains"
		this.terrainRollingHills = new MultiNoiseTerrainType(BiomeKeys.PLAINS, 80.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 290.0, 30.0, 12.0)
				.addNoise(new Noise(seed, 2), 1.0 / 90.0, 25.0, 8.0);

		this.terrainSnowyTundra = new SimpleSimplexTerrainType(BiomeKeys.SNOWY_TUNDRA, seed, 2, 68.0, 1.0 / 75.0, 8.0);
		// Also snow mountain short peak areas
	}

	// Special
	final TerrainType terrainMountains;

	// Ocean

	// Equator
	final TerrainType terrainJungle;
	
	// Subequator
	final TerrainType terrainSavannahHills;
	
	// Temperate
	final TerrainType terrainRollingHills;
	
	// Ice Cap
	final TerrainType terrainSnowyTundra;
}
