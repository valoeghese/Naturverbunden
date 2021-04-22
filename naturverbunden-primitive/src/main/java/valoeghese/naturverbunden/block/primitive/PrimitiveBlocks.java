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

package valoeghese.naturverbunden.block.primitive;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import valoeghese.naturverbunden.core.NVBBlockUtils;

public class PrimitiveBlocks extends NVBBlockUtils {
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

	public static final Block ITEM_BLOCK = register("item_block", AbstractBlock.Settings.of(Material.DECORATION)
			.strength(-1.0f, 0.4f)
			.noCollision()
			.nonOpaque()
			.solidBlock(NVBBlockUtils::never)
			.suffocates(NVBBlockUtils::never)
			.blockVision(NVBBlockUtils::never)
			.sounds(BlockSoundGroup.STONE), ItemBlock::new);

	public static final Block HYDROGEN_SULFIDE = register("hydrogen_sulfide", createGasSettings(), settings -> new GasBlock(settings, c -> c >= GasBlock.MAX_CONCENTRATION - 1 ? GasBlock.INSTANT_DEATH : new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 4, 0, false, false)));

	public static final Block BROWN_STONE = register("brown_stone", createRockSettings(MapColor.BROWN), Block::new);
	public static final Block SYNECHOCOCCUS_COVERED_ROCK = register("synechococcus_covered_rock", createRockSettings(MapColor.TERRACOTTA_YELLOW), Block::new);
	public static final Block CAROTENOID_COVERED_ROCK = register("carotenoid_covered_rock", createRockSettings(MapColor.TERRACOTTA_ORANGE), Block::new);
	public static final Block DEINOCOCCUS_COVERED_ROCK = register("deinococcus_covered_rock", createRockSettings(MapColor.TERRACOTTA_RED), Block::new);

	public static final BlockEntityType<ItemBlockEntity> ITEM_BLOCK_ENTITY = create("item_block", FabricBlockEntityTypeBuilder.create(ItemBlockEntity::new, ITEM_BLOCK));

	public static final Block forceRegister() {
		return ITEM_BLOCK;
	}
}
