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

import static valoeghese.naturverbunden.worldgen.terrain.biome.VanillaTerrainTypes.addCliffs;
import static valoeghese.naturverbunden.worldgen.terrain.biome.VanillaTerrainTypes.addFrequentCliffs;
import static valoeghese.naturverbunden.worldgen.terrain.biome.VanillaTerrainTypes.addLowHills;
import static valoeghese.naturverbunden.worldgen.terrain.biome.VanillaTerrainTypes.addSuperFrequentCliffs;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.world.biome.BiomeKeys;
import valoeghese.naturverbunden.worldgen.terrain.type.PrimaryTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public class DebugTerrainTypes {
	public DebugTerrainTypes(Random rand) {
		this.rand = rand;

		this.flatHills1 = debug($ -> addLowHills($, 1));
		this.flatHills3 = debug($ -> addLowHills($, 3));
		this.smallHills = debug(VanillaTerrainTypes::addSmallHills);
		this.largeHills = debug(VanillaTerrainTypes::addLargeHills);
		this.grasslandRidges = debug(VanillaTerrainTypes::addGrasslandRidges);
		this.taigaRidges = debug(VanillaTerrainTypes::addTaigaRidges);
		this.mountainRidges = debug(VanillaTerrainTypes::addMountainRidges);
		this.cliffs5_0 = debug($ -> addCliffs($, 5.0, 0.0));
		this.fcliffs5_0 = debug($ -> addFrequentCliffs($, 5.0, 0.0));
		this.sfcliffs5_0 = debug($ -> addSuperFrequentCliffs($, 5.0, 0.0));
	}

	private final Random rand;
	final TerrainType flatHills1;
	final TerrainType flatHills3;
	final TerrainType smallHills;
	final TerrainType largeHills;
	final TerrainType grasslandRidges;
	final TerrainType taigaRidges; // rolling hills used this too
	final TerrainType mountainRidges;
	final TerrainType cliffs5_0;
	final TerrainType fcliffs5_0;
	final TerrainType sfcliffs5_0;
	
	private final TerrainType debug(Consumer<PrimaryTerrainType> initialiser) {
		PrimaryTerrainType result = new PrimaryTerrainType(BiomeKeys.PLAINS, this.rand, 90.0);
		initialiser.accept(result);
		return result;
	}
}
