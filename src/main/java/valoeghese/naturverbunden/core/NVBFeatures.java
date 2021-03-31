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

package valoeghese.naturverbunden.core;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.BlockPlacerType;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import valoeghese.naturverbunden.Naturverbunden;
import valoeghese.naturverbunden.common.primitive.PrimitiveContent;
import valoeghese.naturverbunden.worldgen.primitive.ItemBlockPlacer;

public class NVBFeatures {
	private static <P extends BlockPlacer> BlockPlacerType<P> register(String id, Codec<P> codec) {
		return Registry.register(Registry.BLOCK_PLACER_TYPE, Naturverbunden.id(id), new BlockPlacerType<>(codec));
	}

	private static RegistryKey<ConfiguredFeature<?, ?>> key(String name) {
		return RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, Naturverbunden.id(name));
	}

	private static ConfiguredFeature<?, ?> createFrequentPatch(RegistryKey<ConfiguredFeature<?, ?>> key, int frequency, BlockState state, BlockPlacer placer) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.getValue(), Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(state), placer).tries(32).cannotProject().build())
				.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(frequency));
	}

	public static final BlockPlacerType<ItemBlockPlacer> ITEM_BLOCK_PLACER = register("item_block_placer", ItemBlockPlacer.CODEC);

	// Forest Ground Sticks
	// TODO make this a tree decorator
	private static final RegistryKey<ConfiguredFeature<?, ?>> K_FOREST_GROUND_STICKS = key("forest_ground_sticks");
	public static final ConfiguredFeature<?, ?> FOREST_GROUND_STICKS = createFrequentPatch(K_FOREST_GROUND_STICKS, 6, PrimitiveContent.ITEM_BLOCK.getDefaultState(), new ItemBlockPlacer(Items.STICK)); 

	private static final RegistryKey<ConfiguredFeature<?, ?>> K_SPARSE_GROUND_STICKS = key("sparse_ground_sticks");
	public static final ConfiguredFeature<?, ?> SPARSE_GROUND_STICKS = createFrequentPatch(K_SPARSE_GROUND_STICKS, 2, PrimitiveContent.ITEM_BLOCK.getDefaultState(), new ItemBlockPlacer(Items.STICK)); 

	public static final ConfiguredFeature<?, ?> forceRegister() {
		return FOREST_GROUND_STICKS;
	}

	@SuppressWarnings("deprecation")
	public static void initialiseWorldGen() {
		BiomeModifications.addFeature(BiomeSelectors.categories(Category.FOREST, Category.JUNGLE, Category.TAIGA), GenerationStep.Feature.VEGETAL_DECORATION, K_FOREST_GROUND_STICKS);
		BiomeModifications.addFeature(BiomeSelectors.categories(Category.EXTREME_HILLS, Category.PLAINS, Category.SWAMP, Category.RIVER, Category.SAVANNA), GenerationStep.Feature.VEGETAL_DECORATION, K_SPARSE_GROUND_STICKS);
	}
}
