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

public final class Climates {
	public Climates(VanillaTerrainTypes types) {
		// Lower values = higher temperature, lower humidity
		// Sorted temperature * 5 + humidity
		TROPICAL_RAINFOREST = new Climate(types.terrainJungle, types.terrainJungleMtns, types.terrainJungleWithHills, types.terrainJungleWithBamboo); // tropical rainforest
		TROPICAL_MONSOON = new Climate(types.terrainJungleEdge); // tropical monsoon. think: florida. I'd add bayou to this but the grass colour doesn't blend nice
		TROPICAL_WET_AND_DRY = new Climate(types.terrainSavannah, types.terrainSavannah, types.terrainSavannahHills, types.terrainSavannahTerrace); // Savannahs

		TROPICAL_DESERT = new Climate(types.terrainTropicalDesert); // Hot Deserts, like sahara
		TROPICAL_SEMI_ARID = new Climate(types.terrainPlains); // Steppe
		MEDITERRANEAN = new Climate(types.terrainPlains, types.terrainBayou, types.terrainRollingHills, types.terrainDeciduousForest); // Mediterranean climate. Spain, Italy, West coast of US
		OCEANIC = new Climate(types.terrainDeciduousForest, types.terrainBayou, types.terrainRoofedForest, types.terrainTaigaGiant); // Think: New Zealand, England, Ireland
		HUMID_SUBTROPICAL = new Climate(types.terrainRoofedForest, types.terrainBambooJungle); // Think: southeast china, florida

		COLD_DESERT = TROPICAL_DESERT; // Deserts further from equator due to rain shadows etc
		COLD_SEMI_ARID = TROPICAL_SEMI_ARID; // Similar to above but steppes. Eurasian Steppe, and parts of North America.
		HOT_SUMMER_CONTINENTAL = new Climate(types.terrainBirchForest, types.terrainPlains); // Would be meadows and such. Think: central-north east america
		HUMID_CONTINENTAL = new Climate(types.terrainDeciduousForest); // Forests. Mixed Deciduous Forests. Lots of them. A ton of them.

		SUBARCTIC_DRY = new Climate(types.terrainPlains);
		HEMIBOREAL = new Climate(types.terrainTaiga, types.terrainBirchForest);
		SUBARCTIC_HUMID = new Climate(types.terrainTaiga, types.terrainTaiga, types.terrainTaiga, types.terrainTaigaGiant);

		ARCTIC_ICE_CAP = new Climate(types.oceanFrozen.shallow);
		ARCTIC_TUNDRA = new Climate(types.terrainSnowyTundra, types.terrainSnowyTundra, types.terrainSnowyTundra, types.terrainSnowySpikes);

		CLIMATES = new Climate[]
			{
				TROPICAL_WET_AND_DRY, TROPICAL_WET_AND_DRY, TROPICAL_MONSOON, TROPICAL_RAINFOREST, TROPICAL_RAINFOREST,
				TROPICAL_DESERT, TROPICAL_SEMI_ARID, MEDITERRANEAN, OCEANIC, HUMID_SUBTROPICAL,
				COLD_DESERT, COLD_SEMI_ARID, HOT_SUMMER_CONTINENTAL, OCEANIC, HUMID_CONTINENTAL,
				SUBARCTIC_DRY, SUBARCTIC_DRY, HEMIBOREAL, SUBARCTIC_HUMID, SUBARCTIC_HUMID,
				ARCTIC_ICE_CAP, ARCTIC_ICE_CAP, ARCTIC_TUNDRA, ARCTIC_TUNDRA, ARCTIC_TUNDRA
			};
	}
	
	// Equatorial
	public final Climate TROPICAL_WET_AND_DRY; // As, Aw
	public final Climate TROPICAL_MONSOON; // Am
	public final Climate TROPICAL_RAINFOREST; // Af
	
	// Subtropical
	public final Climate TROPICAL_DESERT; // BWh
	public final Climate TROPICAL_SEMI_ARID; // BSh
	public final Climate MEDITERRANEAN; // Csa
	public final Climate OCEANIC; // C[wf][bc]
	public final Climate HUMID_SUBTROPICAL; //C[wf]a
	
	// Temperate
	public final Climate COLD_DESERT; // BWk
	public final Climate COLD_SEMI_ARID; // BSk
	public final Climate HOT_SUMMER_CONTINENTAL; // Dfa
	public final Climate HUMID_CONTINENTAL; // D type a

	// Boreal
	public final Climate SUBARCTIC_DRY; // Dw, Ds
	public final Climate HEMIBOREAL; // D type b
	public final Climate SUBARCTIC_HUMID; // Df

	// Polar
	public final Climate ARCTIC_ICE_CAP; // EF
	public final Climate ARCTIC_TUNDRA; // ET

	private final Climate[] CLIMATES;

	public Climate sample(int temperature, double humidityRaw) {
		return CLIMATES[5 * temperature + getHumidity(humidityRaw)];
	}

	/**
	 * For climate:
	 * Noise Distributions in 5 sections, 3 sig figs.
	 * Retrieved via calculating noise values numerous times at random positions and dividing into 5 sections
	 * Section 1: [-1, -0.415]
	 * Section 2: [-0.415, -0.139]
	 * Section 3: [-0.139, 0.139]
	 * Section 4: [0.139, 0.415]
	 * Section 5: [0.415, 1]
	 * @return the "section number" minus one (i.e. an int in the range [0,4] inclusive). 
	 */
	public static int getHumidity(double raw) {
		if (raw < -0.415) {
			return 0;
		} else if (raw < -0.139) {
			return 1;
		} else if (raw < 0.139) {
			return 2;
		} else if (raw < 0.415) {
			return 3;
		} else {
			return 4;
		}
	}
}
