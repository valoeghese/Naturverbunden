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

package valoeghese.naturverbunden.mixin.terrain;

import java.util.Properties;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.base.MoreObjects;

import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import valoeghese.naturverbunden.worldgen.terrain.TerrainChunkGenerator;

/**
 * @reason Mess with default generator type.
 * TODO server stuff for fromProperties
 */
@Mixin(GeneratorOptions.class)
public abstract class MixinGeneratorOptions {
	@Inject(at = @At("HEAD"), method = "fromProperties", cancellable = true)
	private static void onFromProperties(DynamicRegistryManager dynamicRegistryManager, Properties properties, CallbackInfoReturnable<GeneratorOptions> info) {
		Object levelType = properties.get("level-type");

		// Ecotones has a null check so probably a good idea for me to as well
		if (levelType != null) {
			if (levelType.toString().trim().toLowerCase().equals("nvb")) {
				// seed shenanigans
				String levelSeed = (String) MoreObjects.firstNonNull(properties.get("level-seed"), "");

				long actualSeed = -1;

				if (!levelSeed.isEmpty()) {
					try {
						long parsed = Long.parseLong(levelSeed);

						if (parsed != 0L) {
							actualSeed = parsed;
						}
					} catch (NumberFormatException nfe) {
						actualSeed = levelSeed.hashCode();
					}
				} else {
					actualSeed = new Random().nextLong();
				}

				// Create the generator options
				String shouldGenerateStructures = (String)properties.get("generate-structures");
				boolean generateStructures = shouldGenerateStructures == null || Boolean.parseBoolean(shouldGenerateStructures);

				Registry<DimensionType> dimReg = dynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY);
				Registry<Biome> biomeReg = dynamicRegistryManager.get(Registry.BIOME_KEY);
				Registry<ChunkGeneratorSettings> settingsReg = dynamicRegistryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
				SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(dimReg, biomeReg, settingsReg, actualSeed);

				// return our chunk generator
				info.setReturnValue(new GeneratorOptions(
						actualSeed,
						generateStructures,
						false,
						GeneratorOptions.getRegistryWithReplacedOverworldGenerator(
								dimReg,
								dimensionOptions,
								TerrainChunkGenerator.create(biomeReg, settingsReg, actualSeed))));
			}
		}
	}
}
