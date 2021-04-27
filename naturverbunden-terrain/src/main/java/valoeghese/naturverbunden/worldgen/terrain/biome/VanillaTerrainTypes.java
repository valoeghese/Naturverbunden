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
	}

	// Special
	public final TerrainType terrainBeach;
	public final TerrainType terrainBeachFrozen;
	public final TerrainType terrainBeachStone;

	private final OceanEntry[] oceans = new OceanEntry[5];

	// Primary types

	public final PrimaryTerrainType terrainMountains;

	OceanEntry getOcean(int temperature) {
		return oceans[temperature];
	}

	// TODO make these functions registered as lambdas so they can be applied from configs and specified therefrom
	// TODO more fucking datagen or scripting. I'd do scripting but is it worth depending on graal?
	// TODO make the terrain types I want to keep moved to such functions
	// TODO the mountain refactors I figured out over the last few days

	/**
	 * Add hills with a period of 70 blocks and height amplitude of +/- 12 (overall variation: 24 blocks).
	 * @param type the type of terrain.
	 * @param detail the level of detail, i.e. number of octaves. 3 or greater is not recommended.
	 */
	public static void addLowHills(PrimaryTerrainType type, int detail) {
		INoise noise = detail > 1 ? new Noise(type.getRandom(), detail) : new OpenSimplexGenerator(type.getRandom());
		final double freq = 1.0 / 70.0;
		final double ampl = 10.0;
		// relative grad: 0.17
		type.addGenerator((x, z) -> ampl * noise.sample(x * freq, z * freq));
	}

	public static void addSmallHills(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new OpenSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 55.0;
		final double ampl = 12.0;
		// relative grad: 0.21
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}

	public static void addLargeHills(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new OpenSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 60.0;
		final double ampl = 22.0;
		// relative grad: 0.37
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}

	public static void addGrasslandRidges(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new RidgedSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 260.0;
		final double ampl = 19.0;
		// relative grad: 0.15r
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}

	public static void addTaigaRidges(PrimaryTerrainType type) {
		OpenSimplexGenerator generator = new RidgedSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 195.0;
		final double ampl = 26.0;
		// relative grad: 0.26r
		type.addGenerator((x, z) -> ampl * generator.sample(x * freq, z * freq));
	}
	
	public static void addMountainRidges(PrimaryTerrainType type) {
		INoise noise = new Noise(type.getRandom(), 3, RidgedSimplexGenerator::new);		
		final double freq = 1.0 / 150.0;
		final double ampl = 43.3;
		// relative grad: 0.58r
		type.addGenerator((x, z) -> ampl * noise.sample(x * freq, z * freq));
	}

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

	public static void addSuperFrequentCliffs(PrimaryTerrainType type, double maxHeight, double minHeight) {
		OpenSimplexGenerator height = new RidgedSimplexGenerator(type.getRandom());		
		OpenSimplexGenerator cutoff = new RidgedSimplexGenerator(type.getRandom());		
		final double freq = 1.0 / 62.0;
		// relative grad: 0.58r
		type.addGenerator((x, z) -> {
			if (cutoff.sample(x * freq, z * freq) > 0) {
				// cliff regions are more restrained, meaning they are more prominent when they happen
				double cliffheight = (maxHeight + 1.5) * height.sample(x * freq, z * freq) - 1.5;

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
