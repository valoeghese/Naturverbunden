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

package valoeghese.naturverbunden.worldgen.primitive;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.item.Items;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.placer.BlockPlacerType;
import valoeghese.naturverbunden.block.primitive.PrimitiveBlocks;
import valoeghese.naturverbunden.core.NVBFeatureUtils;

public class PrimitiveWorldgen extends NVBFeatureUtils {
	public static final BlockPlacerType<ItemBlockPlacer> ITEM_BLOCK_PLACER = register("item_block_placer", ItemBlockPlacer.CODEC);

	// Forest Ground Sticks
	// TODO make this a tree decorator
	private static final RegistryKey<ConfiguredFeature<?, ?>> K_FOREST_GROUND_STICKS = key("forest_ground_sticks");
	public static final ConfiguredFeature<?, ?> FOREST_GROUND_STICKS = createFrequentPatch(K_FOREST_GROUND_STICKS, 6, PrimitiveBlocks.ITEM_BLOCK.getDefaultState(), new ItemBlockPlacer(Items.STICK)); 

	private static final RegistryKey<ConfiguredFeature<?, ?>> K_SPARSE_GROUND_STICKS = key("sparse_ground_sticks");
	public static final ConfiguredFeature<?, ?> SPARSE_GROUND_STICKS = createFrequentPatch(K_SPARSE_GROUND_STICKS, 2, PrimitiveBlocks.ITEM_BLOCK.getDefaultState(), new ItemBlockPlacer(Items.STICK)); 

	public static final ConfiguredFeature<?, ?> forceRegister() {
		return FOREST_GROUND_STICKS;
	}

	@SuppressWarnings("deprecation")
	public static void initialiseWorldGen() {
		BiomeModifications.addFeature(BiomeSelectors.categories(Category.FOREST, Category.JUNGLE, Category.TAIGA), GenerationStep.Feature.VEGETAL_DECORATION, K_FOREST_GROUND_STICKS);
		BiomeModifications.addFeature(BiomeSelectors.categories(Category.EXTREME_HILLS, Category.PLAINS, Category.SWAMP, Category.RIVER, Category.SAVANNA), GenerationStep.Feature.VEGETAL_DECORATION, K_SPARSE_GROUND_STICKS);
	}
}
