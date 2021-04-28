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
import valoeghese.naturverbunden.worldgen.terrain.type.PrimaryTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.SimpleSimplexTerrainType;
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
	public VanillaTerrainTypes(Random seed) {
		// Beaches
		this.terrainBeach = new FlatTerrainType(BiomeKeys.BEACH, 64.0, Biome.Category.BEACH);
		this.terrainBeachFrozen = new FlatTerrainType(BiomeKeys.SNOWY_BEACH, 64.0, Biome.Category.BEACH);
		this.terrainBeachStone = new FlatTerrainType(BiomeKeys.STONE_SHORE, 64.0, Biome.Category.BEACH);

		// Mountains
		this.terrainMountains = new PrimaryTerrainType(BiomeKeys.MOUNTAINS, seed, 200.0);
		addMountainRidges(this.terrainMountains);

		// Forest Types
		Random frand = new Random(seed.nextLong());
		this.terrainTaiga = createMontaneForest(BiomeKeys.TAIGA, frand, false);
		this.terrainTaigaSnowy = createMontaneForest(BiomeKeys.SNOWY_TAIGA, frand, false);
		this.terrainTaigaGiant = createMontaneForest(BiomeKeys.GIANT_TREE_TAIGA, frand, true);
		
		this.terrainDeciduousForest = createLowlandForest(BiomeKeys.FOREST, BiomeKeys.WOODED_HILLS, frand);
		this.terrainDeciduousForestHills = createHillyForest(BiomeKeys.FOREST, frand);
	}

	// Special
	public final TerrainType terrainBeach;
	public final TerrainType terrainBeachFrozen;
	public final TerrainType terrainBeachStone;

	private final OceanEntry[] oceans = new OceanEntry[5];

	// === Primary types ===

	// Mountain Types
	public final PrimaryTerrainType terrainMountains;

	// Forest Types
	public final PrimaryTerrainType terrainTaiga;
	public final PrimaryTerrainType terrainTaigaGiant;
	public final PrimaryTerrainType terrainTaigaSnowy;

	public final PrimaryTerrainType terrainDeciduousForest;
	public final PrimaryTerrainType terrainDeciduousForestHills;

	// Factories

	// montane here just refers to the shape, not the placement.
	private static PrimaryTerrainType createMontaneForest(RegistryKey<Biome> biome, Random rand, boolean smootherHills) {
		PrimaryTerrainType montaneForest = new PrimaryTerrainType(biome, rand, 76.0);
		addTaigaRidges(montaneForest); // +38v, +26u
		
		if (smootherHills) {
			addSeparatedSmoothLargeHills(montaneForest, 2); // +30v, +22u
		} else {
			addSeparatedLargeHills(montaneForest, 2); // +30v, +22u
		}

		addCliffs(montaneForest, 5.0, 0.0); // +5v, +5u
		return montaneForest; // overall variation: 74 blocks, 54 up
	}

	private static PrimaryTerrainType createHillyForest(RegistryKey<Biome> biome, Random rand) {
		PrimaryTerrainType hillyForest = new PrimaryTerrainType(biome, rand, 73.0);
		addLargeHills(hillyForest, 1); // +44v, +22u
		addCliffs(hillyForest, 5.0, 0.0); // +5v, +5u
		return hillyForest;
	}

	private static PrimaryTerrainType createLowlandForest(RegistryKey<Biome> biome, RegistryKey<Biome> hills, Random rand) {
		PrimaryTerrainType lowlandForest = new PrimaryTerrainType(biome, rand, 70.0);
		addLowHills(lowlandForest, 2); // +20v, +10u
		addCliffs(lowlandForest, 5.0, 0.0); // +5v, +5u

		PrimaryTerrainType hillsForest = new PrimaryTerrainType(hills, rand, 85.0);
		addModerateHills(hillsForest); // +24v, +12u
		addFrequentCliffs(hillsForest, 8.0, 0.0); // +8v, +8u
		hillsForest.reduceBlendRadius(0.5);
		hillsForest.setShapeWeight(1.5);

		lowlandForest.largeHills = hillsForest;

		return lowlandForest;
	}

	OceanEntry getOcean(int temperature) {
		return oceans[temperature];
	}

	// TODO make these functions registered as lambdas so they can be applied from configs and specified therefrom
	// TODO more fucking datagen or scripting. I'd do scripting but is it worth depending on graal?
	// TODO make the terrain types I want to keep moved to such functions
	// TODO the mountain refactors I figured out over the last few days

	/**
	 * Add hills with a period of 70 blocks and height amplitude of +/- 10 (overall variation: 20 blocks).
	 * @param type the type of terrain.
	 * @param detail the level of detail, i.e. number of octaves. Anything above 3 is not recommended. Each octave makes it less common for large peaks and dips, in exchange for higher detail.
	 */
	public static void addLowHills(PrimaryTerrainType type, int detail) {
		INoise noise = detail > 1 ? new Noise(type.getRandom(), detail) : new OpenSimplexGenerator(type.getRandom());
		final double freq = 1.0 / 70.0;
		final double ampl = 10.0;
		// relative grad: 0.17
		type.addGenerator((x, z) -> ampl * noise.sample(x * freq, z * freq));
	}

	/**
	 * Add hills with a period of 55 blocks and height amplitude of +/- 12 (overall variation: 24 blocks).
	 * @param type the type of terrain.
	 */
	public static void addModerateHills(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new OpenSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 55.0;
		final double ampl = 12.0;
		// relative grad: 0.21
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}

	/**
	 * Add hills with a period of 60 blocks and height amplitude of +/- 22 (overall variation: 44 blocks).
	 * @param type the type of terrain.
	 * @param detail the level of detail, i.e. number of octaves. Anything above 3 is not recommended. Each octave makes it less common for large peaks and dips, in exchange for higher detail.
	 */
	public static void addLargeHills(PrimaryTerrainType type, int detail) {
		OpenSimplexGenerator generator = new OpenSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 60.0;
		final double ampl = 22.0;
		// relative grad: 0.37
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}

	/**
	 * Add hills with a period of 60 blocks and height amplitude of +22/-8 (overall variation: 30 blocks).
	 * @param type the type of terrain.
	 * @param detail the level of detail, i.e. number of octaves. Anything above 3 is not recommended. Each octave makes it less common for large peaks and dips, in exchange for higher detail.
	 */
	public static void addSeparatedLargeHills(PrimaryTerrainType type, int detail) {
		OpenSimplexGenerator generator = new OpenSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 60.0;
		final double amplUp = 22.0;
		final double amplDown = 8.0;
		// relative grad: 0.37
		type.addGenerator((x, z) -> {
			double norm = generator.sample(x * freq, z * freq);

			if (norm > 0) {
				return amplUp * norm;
			} else {
				return amplDown * norm;
			}
		});
	}

	/**
	 * Add hills with a period of 120 blocks and height amplitude of +22/-8 (overall variation: 30 blocks).
	 * @param type the type of terrain.
	 * @param detail the level of detail, i.e. number of octaves. Anything above 3 is not recommended. Each octave makes it less common for large peaks and dips, in exchange for higher detail.
	 */
	public static void addSeparatedSmoothLargeHills(PrimaryTerrainType type, int detail) {
		OpenSimplexGenerator generator = new OpenSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 120.0;
		final double amplUp = 22.0;
		final double amplDown = 8.0;
		// relative grad: 0.37
		type.addGenerator((x, z) -> {
			double norm = generator.sample(x * freq, z * freq);

			if (norm > 0) {
				return amplUp * norm;
			} else {
				return amplDown * norm;
			}
		});
	}

	/**
	 * Add ridges with a period of 260 blocks and height amplitude of +/- 19 (overall variation: 38 blocks).
	 * @param type the type of terrain.
	 */
	public static void addGrasslandRidges(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new RidgedSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 260.0;
		final double ampl = 19.0;
		// relative grad: 0.15r
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}

	/**
	 * Add ridges with a period of 195 blocks and height amplitude of +26/-12 (overall variation: 38 blocks).
	 * @param type the type of terrain.
	 */
	public static void addTaigaRidges(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new RidgedSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 195.0;
		final double amplUp = 26.0;
		final double amplDown = 12.0;
		// relative grad: 0.26r
		type.addGenerator((x, z) -> {
			double norm = generator.sample(x * freq, z * freq);

			if (norm > 0) {
				return amplUp * norm;
			} else {
				return amplDown * norm;
			}
		});
	}

	/**
	 * Add 3-octave ridges with a period of 150 blocks and height amplitude of +/- 43.3 (overall variation: 86.6 blocks).
	 * @param type the type of terrain.
	 */
	public static void addMountainRidges(PrimaryTerrainType type) {
		INoise noise = new Noise(type.getRandom(), 3, RidgedSimplexGenerator::new);		
		final double freq = 1.0 / 150.0;
		final double ampl = 43.3;
		// relative grad: 0.58r
		type.addGenerator((x, z) -> ampl * noise.sample(x * freq, z * freq));
	}

	/**
	 * Adds sparsely generating cliffs to the terrain.
	 * @param type the terrain type
	 * @param maxHeight the maximum height of cliffs.
	 * @param minHeight the minimum height of cliffs (i.e. the cutoff)
	 */
	public static void addCliffs(PrimaryTerrainType type, double maxHeight, double minHeight) {
		OpenSimplexGenerator height = new RidgedSimplexGenerator(type.getRandom());		
		OpenSimplexGenerator cutoff = new RidgedSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 210.0;
		// relative grad: 0.58r
		type.addGenerator((x, z) -> {
			if (cutoff.sample(x * freq, z * freq) > 0) {
				double cliffheight = maxHeight * height.sample(x * freq, z * freq);

				if (cliffheight > minHeight) {
					return cliffheight;
				}
			}

			return 0.0;
		});
	}

	/**
	 * Adds more frequently generating cliffs to the terrain.
	 * @param type the terrain type
	 * @param maxHeight the maximum height of cliffs.
	 * @param minHeight the minimum height of cliffs (i.e. the cutoff)
	 */
	public static void addFrequentCliffs(PrimaryTerrainType type, double maxHeight, double minHeight) {
		OpenSimplexGenerator height = new RidgedSimplexGenerator(type.getRandom());		
		OpenSimplexGenerator cutoff = new RidgedSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 90.0;
		// relative grad: 0.58r
		type.addGenerator((x, z) -> {
			if (cutoff.sample(x * freq, z * freq) > 0) {
				double cliffheight = maxHeight * height.sample(x * freq, z * freq);

				if (cliffheight > minHeight) {
					return cliffheight;
				}
			}

			return 0.0;
		});
	}

	/**
	 * Adds super frequently generating cliffs to the terrain.
	 * @param type the terrain type
	 * @param maxHeight the maximum height of cliffs.
	 * @param minHeight the minimum height of cliffs.
	 */
	public static void addSuperFrequentCliffs(PrimaryTerrainType type, double maxHeight, double minHeight) {
		OpenSimplexGenerator height = new RidgedSimplexGenerator(type.getRandom());
		OpenSimplexGenerator cutoff = new RidgedSimplexGenerator(type.getRandom());
		// Was a value of 62.0 each but I decided to mess with it a bit
		final double cutfreq = 1.0 / 58.0;
		final double genFreq = 1.0 / 68.0;
		// relative grad: 0.58r
		type.addGenerator((x, z) -> {
			if (cutoff.sample(x * cutfreq, z * cutfreq) > 0) {
				// cliff regions are more restrained, meaning they are more prominent when they happen
				double cliffheight = (maxHeight + 1.0) * height.sample(x * genFreq, z * genFreq) - 1.0;

				if (cliffheight > minHeight) {
					return cliffheight;
				}
			}

			return 0.0;
		});
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
