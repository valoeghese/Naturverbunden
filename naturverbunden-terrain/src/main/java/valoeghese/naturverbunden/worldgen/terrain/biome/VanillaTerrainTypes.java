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

package valoeghese.naturverbunden.worldgen.terrain.biome;

import java.util.Random;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import valoeghese.naturverbunden.util.terrain.INoise;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.util.terrain.OpenSimplexGenerator;
import valoeghese.naturverbunden.util.terrain.RidgedSimplexGenerator;
import valoeghese.naturverbunden.worldgen.terrain.type.FlatTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.MountainsTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.PrimaryTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.SimpleSimplexTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerracedTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

/*
 *  UNDERGOING MAJOR REVISIONS
 *  
 *  SHAPES TO KEEP (remember to rescale):
 *  - Tropical Desert
 *  - Taiga
 *  - Jungle Mountains and regular flat Jungle
 *  - Terraced Savannah
 *  - Flat terrains
 *  - Other savannah ones are fine but I think normal savannah needs to be flatter
 *  
 *  SHAPES TO REWRITE FULLY
 *  - Plains / Forest types
 *  
 *  OTHER SHAPES WILL BE REWRITTEN ANYWAY, THOUGH I MAY REFERENCE THIS.
 *
 *  BIOMES TO ADD
 *  - Mesa
 *  
 *  SHAPES TO ADD AS METHODS (aside from porting a bunch of stuff I already had)
 *  - Cliffs (dual noise generator additional-type cliff generators)
 *  
 *  POSSIBLE THINGS I MIGHT DO
 *  - Make a modifier to raise/lower terrain in large-beach-edge-terrain so we can have cliffs
 *  - Though, because of the smooth large-scale blending, I might want to add some more methods to control terrain height directly at x/z sample-time.
 */

public class VanillaTerrainTypes {
	// I can't remember what this number means I think it might have been a world seed for testing at some point?
	// 1580589449715291311
	public VanillaTerrainTypes(Random seed) {
		this.terrainBeach = new FlatTerrainType(BiomeKeys.BEACH, 64.0, Biome.Category.BEACH);
		this.terrainBeachFrozen = new FlatTerrainType(BiomeKeys.SNOWY_BEACH, 64.0, Biome.Category.BEACH);
		this.terrainBeachStone = new FlatTerrainType(BiomeKeys.STONE_SHORE, 64.0, Biome.Category.BEACH);
		this.terrainBayou = new SimpleSimplexTerrainType(BiomeKeys.SWAMP, seed, 2, 61.0, 1.0 / 40.0, 8.0);

		this.terrainDeciduousForest = new SimpleSimplexTerrainType(BiomeKeys.FOREST, seed, 3, 80.0, 1.0 / 70.0, 12.0);
		this.terrainBirchForest = new SimpleSimplexTerrainType(BiomeKeys.BIRCH_FOREST, seed, 3, 80.0, 1.0 / 70.0, 12.0);
		this.terrainRoofedForest = new SimpleSimplexTerrainType(BiomeKeys.DARK_FOREST, seed, 3, 80.0, 1.0 / 70.0, 12.0);

		this.terrainJungle = createJungle(seed);
		this.terrainJungleWithHills = createJungle(seed);
		this.terrainJungleWithBamboo = createJungle(seed);	
		this.terrainJungleHills = new SimpleSimplexTerrainType(BiomeKeys.JUNGLE_HILLS, seed, 1, 90.0, 1.0 / 60.0, 15.0);
		this.terrainJungleMtns = new PrimaryTerrainType(BiomeKeys.MODIFIED_JUNGLE, 97.0)
				.addNoise(new Noise(seed, 2, RidgedSimplexGenerator::new), 1.0 / 380.0, 34.0);
		this.terrainBambooJungle = new SimpleSimplexTerrainType(BiomeKeys.BAMBOO_JUNGLE, seed, 1, 70.0, 1.0 / 50.0, 10.0);
		this.terrainJungleEdge = new SimpleSimplexTerrainType(BiomeKeys.JUNGLE_EDGE, seed, 2, 72.0, 1.0 / 60.0, 15.0);

		this.terrainMountains = new MountainsTerrainType(seed);

		// TODO make this code less cursed
		long seedoc = seed.nextLong();
		Random oceanRand = new Random(seedoc);
		this.oceanWarm = new OceanEntry(BiomeKeys.WARM_OCEAN, BiomeKeys.DEEP_WARM_OCEAN, oceanRand);
		oceanRand.setSeed(seedoc);
		this.oceanLukewarm = new OceanEntry(BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN, oceanRand);
		oceanRand.setSeed(seedoc);
		this.oceanTemperate = new OceanEntry(BiomeKeys.OCEAN, BiomeKeys.DEEP_OCEAN, oceanRand);
		oceanRand.setSeed(seedoc);
		this.oceanCool = new OceanEntry(BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN, oceanRand);
		oceanRand.setSeed(seedoc);
		this.oceanFrozen = new OceanEntry(BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN, oceanRand);

		this.terrainPlains = new PrimaryTerrainType(BiomeKeys.PLAINS, 76.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 225.0, 15.0, 8.0)
				.addNoise(new Noise(seed, 1), 1.0 / 45.0, 12.5, 3.0, -0.2);

		this.terrainRollingHills = new PrimaryTerrainType(BiomeKeys.PLAINS, 76.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 145.0, 30.0, 12.0)
				.addNoise(new Noise(seed, 2), 1.0 / 45.0, 25.0, 8.0);

		this.terrainSavannah = new PrimaryTerrainType(BiomeKeys.SAVANNA, 77.0)
				.addNoise(new Noise(seed, 2, RidgedSimplexGenerator::new), 1.0 / 260.0, 22.0)
				.addNoise(new Noise(seed, 1), 1.0 / 45.0, 6.0);

		this.terrainSavannahPlateau = new SimpleSimplexTerrainType(BiomeKeys.SAVANNA_PLATEAU, seed, 2, 118.0, 1.0 / 41.0, 9.0);

		this.terrainSavannahHills = new PrimaryTerrainType(BiomeKeys.SAVANNA, 84.0)
				.addNoise(new Noise(seed, 2, RidgedSimplexGenerator::new), 1.0 / 120.0, 22.0)
				.addNoise(new Noise(seed, 1), 1.0 / 45.0, 12.0);

		this.terrainSavannahTerrace = new TerracedTerrainType(BiomeKeys.SHATTERED_SAVANNA, seed, 4, 1.0 / 270.0, 72.0, 12.0, new Noise(seed, 2));

		this.terrainSnowPlateau = new SimpleSimplexTerrainType(BiomeKeys.SNOWY_MOUNTAINS, seed, 2, 98.0, 1.0 / 40.0, 12.0);
		this.terrainSnowySpikes = new SimpleSimplexTerrainType(BiomeKeys.ICE_SPIKES, seed, 2, 68.0, 1.0 / 37.5, 8.0);
		this.terrainSnowyTundra = new SimpleSimplexTerrainType(BiomeKeys.SNOWY_TUNDRA, seed, 2, 68.0, 1.0 / 37.5, 8.0);

		this.terrainTaiga = createTaiga(BiomeKeys.TAIGA, seed);
		this.terrainTaigaGiant = createTaiga(BiomeKeys.GIANT_TREE_TAIGA, seed);
		this.terrainTaigaSnowy = createTaiga(BiomeKeys.SNOWY_TAIGA, seed);

		this.terrainTropicalDesert = new PrimaryTerrainType(BiomeKeys.DESERT, seed, 78.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 205.0, 30.0, 12.0)
				.addNoise(new Noise(seed, 2), 1.0 / 30.0, 2.5)
				.addNoise(new Noise(seed, 1), 1.0 / 75.0, 22.0, 0.0, -0.4); // idk might remove this last one

		this.terrainSnowyTundra.largeHills = this.terrainTaigaSnowy;
		this.terrainSnowyTundra.smallHills = this.terrainSnowPlateau;

		this.terrainSavannah.largeHills = this.terrainSavannahPlateau;
		this.terrainJungleWithBamboo.largeHills = this.terrainBambooJungle;
		this.terrainJungleWithHills.largeHills = this.terrainJungleHills;
		this.terrainJungleWithBamboo.smallHills = this.terrainJungleHills;
		this.terrainJungleMtns.smallHills = this.terrainBambooJungle;

		this.oceans = new OceanEntry[] {
				this.oceanWarm,
				this.oceanLukewarm,
				this.oceanTemperate,
				this.oceanCool,
				this.oceanFrozen
		};
	}

	// Special
	public final TerrainType terrainMountains;
	public final TerrainType terrainBeach;
	public final TerrainType terrainBeachFrozen;
	public final TerrainType terrainBeachStone;

	// Ocean
	public final OceanEntry oceanWarm;
	public final OceanEntry oceanLukewarm;
	public final OceanEntry oceanTemperate;
	public final OceanEntry oceanCool;
	public final OceanEntry oceanFrozen;

	// Hottish Wettish
	public final TerrainType terrainJungle;
	public final TerrainType terrainJungleWithHills;
	public final TerrainType terrainJungleHills;
	public final TerrainType terrainJungleMtns;
	public final TerrainType terrainJungleWithBamboo;
	public final TerrainType terrainBambooJungle;
	public final TerrainType terrainJungleEdge;

	// Hottish Dryish
	public final TerrainType terrainSavannah;
	public final TerrainType terrainSavannahHills;
	public final TerrainType terrainSavannahTerrace;
	public final TerrainType terrainSavannahPlateau;
	public final TerrainType terrainTropicalDesert;

	// Temperatish Dryish
	public final TerrainType terrainRollingHills;
	public final TerrainType terrainTaiga;
	public final TerrainType terrainTaigaGiant;
	public final TerrainType terrainPlains;

	// Temperatish Wettish
	public final TerrainType terrainBayou;
	public final TerrainType terrainRoofedForest;
	public final TerrainType terrainDeciduousForest;
	public final TerrainType terrainBirchForest;

	// Ice Cap
	public final TerrainType terrainSnowPlateau;
	public final TerrainType terrainSnowySpikes;
	public final TerrainType terrainSnowyTundra;
	public final TerrainType terrainTaigaSnowy;

	private final OceanEntry[] oceans;

	OceanEntry getOcean(int temperature) {
		return oceans[temperature];
	}

	// TODO test each shape function individually in a debug client-side world type of only grass/dirt/stone.
	// This way I can create individual shapes
	// TODO make these functions registered as lambdas so they can be applied from configs and specified therefrom
	// TODO more fucking datagen or scripting. I'd do scripting but is it worth depending on graal?
	// TODO make the terrain types I want to keep moved to such functions
	// TODO the mountain refactors I figured out over the last few days

	/**
	 * Add hills with a period of 70 blocks and height amplitude of +/- 12 (overall variation: 24 blocks).
	 * @param type the type of terrain.
	 * @param detail the level of detail, i.e. number of octaves.
	 */
	private static void addFlatHills(PrimaryTerrainType type, int detail) {
		INoise noise = detail == 1 ? new Noise(type.getRandom(), detail) : new OpenSimplexGenerator(type.getRandom());
		final double freq = 1.0 / 70.0;
		final double ampl = 12.0;
		// relative grad: 0.2
		type.addGenerator((x, z) -> ampl * noise.sample(x * freq, z * freq));
	}

	private static void addSmallHills(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new OpenSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 45.0;
		final double ampl = 12.0;
		// relative grad: 0.2
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}

	private static TerrainType createJungle(Random seed) {
		return new SimpleSimplexTerrainType(BiomeKeys.JUNGLE, seed, 1, 70.0, 1.0 / 50.0, 10.0);
	}

	private static TerrainType createTaiga(RegistryKey<Biome> biome, Random seed) {
		return new PrimaryTerrainType(biome, 76.0)
				.addNoise(new Noise(seed, 1, RidgedSimplexGenerator::new), 1.0 / 195.0, 30.0, 12.0)
				.addNoise(new Noise(seed, 2), 1.0 / 60.0, 25.0, 8.0);
	}

	public static class OceanEntry {
		private OceanEntry(RegistryKey<Biome> shallow, RegistryKey<Biome> deep, Random seed) {
			this.deep = new SimpleSimplexTerrainType(deep, seed, 2, 38.0, 1.0 / 51.0, 15.0);
			this.shallow = new SimpleSimplexTerrainType(shallow, seed, 2, 50.0, 1.0 / 40.0, 12.0);
		}

		public final TerrainType deep;
		public final TerrainType shallow;
	}
}
