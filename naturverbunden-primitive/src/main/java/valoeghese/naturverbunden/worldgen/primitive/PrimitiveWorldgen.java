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
import valoeghese.naturverbunden.core.NVBWorldgenUtils;

@SuppressWarnings("deprecation")
public class PrimitiveWorldgen extends NVBWorldgenUtils {
	public static final BlockPlacerType<ItemBlockPlacer> ITEM_BLOCK_PLACER = register("item_block_placer", ItemBlockPlacer.CODEC);

	// Forest Ground Sticks
	// TODO make this a tree decorator
	private static final RegistryKey<ConfiguredFeature<?, ?>> K_FOREST_GROUND_STICKS = key("forest_ground_sticks");
	public static final ConfiguredFeature<?, ?> FOREST_GROUND_STICKS = createFrequentPatch(K_FOREST_GROUND_STICKS, 6, PrimitiveBlocks.ITEM_BLOCK.getDefaultState(), new ItemBlockPlacer(Items.STICK)); 

	private static final RegistryKey<ConfiguredFeature<?, ?>> K_SPARSE_GROUND_STICKS = key("sparse_ground_sticks");
	public static final ConfiguredFeature<?, ?> SPARSE_GROUND_STICKS = createFrequentPatch(K_SPARSE_GROUND_STICKS, 2, PrimitiveBlocks.ITEM_BLOCK.getDefaultState(), new ItemBlockPlacer(Items.STICK)); 

	public static void initWorldgen() {
		BiomeModifications.addFeature(BiomeSelectors.categories(Category.FOREST, Category.JUNGLE, Category.TAIGA), GenerationStep.Feature.VEGETAL_DECORATION, K_FOREST_GROUND_STICKS);
		BiomeModifications.addFeature(BiomeSelectors.categories(Category.EXTREME_HILLS, Category.PLAINS, Category.SWAMP, Category.RIVER, Category.SAVANNA), GenerationStep.Feature.VEGETAL_DECORATION, K_SPARSE_GROUND_STICKS);
	}
}
