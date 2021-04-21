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

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
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
				.nonOpaque()
				.strength(-1.0f, 69696969.0f)
				.solidBlock(NVBBlockUtils::never)
				.suffocates(NVBBlockUtils::never)
				.blockVision(NVBBlockUtils::never);
	}

	public static final Block ITEM_BLOCK = register("item_block", AbstractBlock.Settings.of(Material.DECORATION)
			.strength(-1.0f, 0.4f)
			.noCollision()
			.nonOpaque()
			.solidBlock(NVBBlockUtils::never)
			.suffocates(NVBBlockUtils::never)
			.blockVision(NVBBlockUtils::never)
			.sounds(BlockSoundGroup.STONE), ItemBlock::new);

	public static final Block HYDROGEN_SULFIDE = register("hydrogen_sulfide", createGasSettings(), settings -> new GasBlock(settings, null, () -> new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 4)));

	public static final BlockEntityType<ItemBlockEntity> ITEM_BLOCK_ENTITY = create("item_block", FabricBlockEntityTypeBuilder.create(ItemBlockEntity::new, ITEM_BLOCK));

	public static final Block forceRegister() {
		return ITEM_BLOCK;
	}
}
