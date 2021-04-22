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

package valoeghese.naturverbunden;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import valoeghese.naturverbunden.core.NVBToggles;
import valoeghese.naturverbunden.worldgen.terrain.TerrainChunkGenerator;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider;
import valoeghese.naturverbunden.worldgen.terrain.type.MountainEdgeTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.RiverEdgeTerrainType;

public class TerrainModule implements ModInitializer {

	@Override
	public void onInitialize() {
		Naturverbunden.LOGGER.info("[Terrain] Initialising!");
		NVBToggles.cutePuppies = true;

		Registry.register(Registry.CHUNK_GENERATOR, Naturverbunden.id("overworld"), TerrainChunkGenerator.CODEC);
		Registry.register(Registry.BIOME_SOURCE, Naturverbunden.id("overworld"), TerrainBiomeProvider.CODEC);

		// River Edge
		TerrainBiomeProvider.addTerrainTypeModifier(2, (original, source, x, z, properties, info) -> {
			double riverGen = source.sampleRiver(x, z);

			if (riverGen > -0.145) {
				riverGen = (1 / -0.145) * Math.min(riverGen, 0.0); // Normalise 0-1 with clamp and bias.
				riverGen = 1.0 - riverGen;
				riverGen *= riverGen;

				return new RiverEdgeTerrainType(original, original.getCategory() == Biome.Category.DESERT && riverGen > 0.5 ? BiomeKeys.RIVER : original.getBiome(), TerrainChunkGenerator.RIVER_HEIGHT + 6, riverGen);
			}
			
			return original;
		});

		TerrainBiomeProvider.addTerrainTypeModifier(4, (original, source, x, z, properties, info) -> {
			double mountainChain = properties.getMountainChain();

			if (mountainChain > 0) {
				int temperature = properties.getTemperature();
				double humidity = properties.getHumidity();

				// Because mountainChain edge goes from 0 to 0.5, multiply by 2.
				return new MountainEdgeTerrainType(original, source.getVanillaTypes().terrainMountains, mountainChain * (1.0 / 0.6), info.isLargeHills(), (temperature < 2 && humidity < 0.0) || (temperature == 2 && humidity < -0.34));
			} else {
				return original;
			}
		});
	}

}
