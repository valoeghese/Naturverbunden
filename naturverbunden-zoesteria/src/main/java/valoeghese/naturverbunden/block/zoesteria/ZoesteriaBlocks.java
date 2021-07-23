package valoeghese.naturverbunden.block.zoesteria;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import valoeghese.naturverbunden.core.NVBBlockUtils;
import valoeghese.naturverbunden.item.zoesteria.ZoesteriaItems;

public class ZoesteriaBlocks extends NVBBlockUtils {
	// Settings
	public static final AbstractBlock.Settings createGasSettings() {
		return AbstractBlock.Settings.of(Material.AIR)
				.noCollision()
				.dropsNothing()
				.air();
	}

	private static final AbstractBlock.Settings createRockSettings(MapColor colour) {
		return FabricBlockSettings.copyOf(Blocks.STONE)
				.breakByTool(FabricToolTags.PICKAXES)
				.mapColor(colour);
	}

	// Gases
	public static final Block HYDROGEN_SULFIDE = register("hydrogen_sulfide", createGasSettings(), settings -> new GasBlock(settings, false, c -> {
		if (c >= GasBlock.MAX_CONCENTRATION - 1) {
			return GasBlock.INSTANT_DEATH;
		} else {
			// TODO allow effect stacking
			return new StatusEffectInstance(c > 2*GasBlock.MAX_CONCENTRATION/3 ? StatusEffects.BLINDNESS : StatusEffects.NAUSEA, 20 * 4, 0, false, false);
		}
	})).get();

	// Stones
	public static final Block BROWN_STONE = registerBlockItem(
			register("brown_stone", createRockSettings(MapColor.BROWN), Block::new),
			new Item.Settings().group(ZoesteriaItems.GROUP));
	public static final Block SYNECHOCOCCUS_COVERED_ROCK = registerBlockItem(
			register("synechococcus_covered_rock", createRockSettings(MapColor.TERRACOTTA_YELLOW), Block::new),
			new Item.Settings().group(ZoesteriaItems.GROUP));
	public static final Block CAROTENOID_COVERED_ROCK = registerBlockItem(
			register("carotenoid_covered_rock", createRockSettings(MapColor.TERRACOTTA_ORANGE), Block::new),
			new Item.Settings().group(ZoesteriaItems.GROUP));
	public static final Block DEINOCOCCUS_COVERED_ROCK = registerBlockItem(
			register("deinococcus_covered_rock", createRockSettings(MapColor.TERRACOTTA_RED), Block::new),
			new Item.Settings().group(ZoesteriaItems.GROUP));

	public static final Block forceRegister() {
		return DEINOCOCCUS_COVERED_ROCK;
	}
}
