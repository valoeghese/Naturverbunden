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

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.util.terrain.RidgedSimplexGenerator;
import valoeghese.naturverbunden.worldgen.terrain.type.FlatTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.MountainsTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.MultiNoiseTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.SimpleSimplexTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerracedTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public class TerrainTypes {
	public TerrainTypes(Random seed) {
		this.terrainBeach = new FlatTerrainType(BiomeKeys.BEACH, 64.0, Biome.Category.BEACH);
		this.terrainBeachFrozen = new FlatTerrainType(BiomeKeys.SNOWY_BEACH, 64.0, Biome.Category.BEACH);

		this.terrainDeciduousForest = new SimpleSimplexTerrainType(BiomeKeys.FOREST, seed, 3, 80.0, 1.0 / 140.0, 12.0);

		this.terrainJungle = new SimpleSimplexTerrainType(BiomeKeys.JUNGLE, seed, 1, 70.0, 1.0 / 100.0, 10.0);
		this.terrainJungleEdge = new SimpleSimplexTerrainType(BiomeKeys.JUNGLE_EDGE, seed, 2, 72.0, 1.0 / 120.0, 15.0);

		// Rainforest will be more mountainous

		this.terrainMountains = new MountainsTerrainType(seed);

		this.terrainOcean = new SimpleSimplexTerrainType(BiomeKeys.OCEAN, seed, 2, 50.0, 1.0 / 80.0, 12.0);

		this.terrainPlains = new MultiNoiseTerrainType(BiomeKeys.PLAINS, 76.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 450.0, 15.0, 8.0)
				.addNoise(new Noise(seed, 1), 1.0 / 90.0, 12.5, 3.0, -0.2);

		this.terrainRollingHills = new MultiNoiseTerrainType(BiomeKeys.PLAINS, 76.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 290.0, 30.0, 12.0)
				.addNoise(new Noise(seed, 2), 1.0 / 90.0, 25.0, 8.0);

		this.terrainSavannah = new MultiNoiseTerrainType(BiomeKeys.SAVANNA, 77.0)
				.addNoise(new Noise(seed, 2, RidgedSimplexGenerator::new), 1.0 / 520.0, 22.0)
				.addNoise(new Noise(seed, 1), 1.0 / 90.0, 6.0);

		this.terrainSavannahHills = new MultiNoiseTerrainType(BiomeKeys.SAVANNA, 84.0)
				.addNoise(new Noise(seed, 2, RidgedSimplexGenerator::new), 1.0 / 240.0, 22.0)
				.addNoise(new Noise(seed, 1), 1.0 / 90.0, 12.0);

		this.terrainSavannahTerrace = new TerracedTerrainType(BiomeKeys.SHATTERED_SAVANNA, seed, 4, 1.0 / 270.0, 72.0, 12.0, new Noise(seed, 2));

		this.terrainScrubland = this.terrainSavannah; // temp

		this.terrainSnowPlateau = new SimpleSimplexTerrainType(BiomeKeys.SNOWY_MOUNTAINS, seed, 2, 98.0, 1.0 / 80.0, 12.0);
		this.terrainSnowySpikes = new SimpleSimplexTerrainType(BiomeKeys.ICE_SPIKES, seed, 2, 68.0, 1.0 / 75.0, 8.0);
		this.terrainSnowyTundra = new SimpleSimplexTerrainType(BiomeKeys.SNOWY_TUNDRA, seed, 2, 68.0, 1.0 / 75.0, 8.0);

		this.terrainTropicalDesert = new MultiNoiseTerrainType(BiomeKeys.DESERT, 78.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 410.0, 30.0, 12.0)
				.addNoise(new Noise(seed, 2), 1.0 / 60.0, 2.5)
				.addNoise(new Noise(seed, 1), 1.0 / 150.0, 22.0, 0.0, -0.4); // idk might remove this last one

	}

	// Special
	final TerrainType terrainMountains;
	final TerrainType terrainBeach;
	final TerrainType terrainBeachFrozen;

	// Ocean
	final TerrainType terrainOcean;

	// Hottish Wettish
	final TerrainType terrainJungle;
	final TerrainType terrainJungleEdge;

	// Hottish Dryish
	final TerrainType terrainSavannah;
	final TerrainType terrainSavannahHills;
	final TerrainType terrainSavannahTerrace;
	final TerrainType terrainScrubland;
	final TerrainType terrainTropicalDesert;

	// Temperatish Dryish
	final TerrainType terrainDeciduousForest;
	final TerrainType terrainRollingHills;
	final TerrainType terrainPlains;

	// Temperatish Wettish
	// Ice Cap
	final TerrainType terrainSnowPlateau;
	final TerrainType terrainSnowySpikes;
	final TerrainType terrainSnowyTundra;
}
