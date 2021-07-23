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

package valoeghese.naturverbunden.worldgen.zoesteria;

import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import valoeghese.naturverbunden.Naturverbunden;
import valoeghese.naturverbunden.core.NVBWorldgenUtils;

public class ZoesteriaWorldgen extends NVBWorldgenUtils {
	public static final RegistryKey<Biome> HOT_SPRINGS_KEY = bkey("hot_springs");

	private static final SurfaceBuilder<TernarySurfaceConfig> HOT_SPRINGS_SURFACE = Registry.register(Registry.SURFACE_BUILDER, Naturverbunden.id("hot_springs"), new HotSpringsSurfaceBuilder());
	private static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> CALDERA_HOT_SPRINGS = Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, Naturverbunden.id("caldera_hot_springs"), HOT_SPRINGS_SURFACE.withConfig(SurfaceBuilder.GRASS_CONFIG));

	public static void initialiseWorldGen() {
		registerBiomes();
	}

	private static void registerBiomes() {
		register(HOT_SPRINGS_KEY, new Biome.Builder()
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.EXTREME_HILLS)
				.depth(2.0f)
				.scale(0.125f)
				.temperature(0.6f)
				.downfall(0.5f)
				.effects(new BiomeEffects.Builder()
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(getSkyColor(0.6F))
						.moodSound(BiomeMoodSound.CAVE)
						.build())
				.spawnSettings(spawnSettings(DefaultBiomeFeatures::addPlainsMobs, true))
				.generationSettings(generationSettings(builder -> {
					builder.surfaceBuilder(CALDERA_HOT_SPRINGS);
					DefaultBiomeFeatures.addDefaultUndergroundStructures(builder);
					builder.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_MOUNTAIN);

					DefaultBiomeFeatures.addLandCarvers(builder);
					//builder.feature(GenerationStep.Feature.LAKES, ConfiguredFeatures.LAKE_WATER); TODO sulfuric acid lakes which emit H2S
					DefaultBiomeFeatures.addAmethystGeodes(builder);
					DefaultBiomeFeatures.addDungeons(builder);

					DefaultBiomeFeatures.addMineables(builder);
					DefaultBiomeFeatures.addDefaultOres(builder);
					DefaultBiomeFeatures.addDefaultDisks(builder);
					DefaultBiomeFeatures.addEmeraldOre(builder);
					DefaultBiomeFeatures.addInfestedStone(builder);

					DefaultBiomeFeatures.addExtraMountainTrees(builder);
					DefaultBiomeFeatures.addDefaultFlowers(builder);
					DefaultBiomeFeatures.addForestGrass(builder);
					DefaultBiomeFeatures.addDefaultMushrooms(builder);
					DefaultBiomeFeatures.addDefaultVegetation(builder);

					DefaultBiomeFeatures.addFrozenTopLayer(builder);
				}))
				.build());
	}
}
