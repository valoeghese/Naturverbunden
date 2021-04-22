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

import java.util.function.Function;

import com.mojang.datafixers.types.Type;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import valoeghese.naturverbunden.Naturverbunden;

public class NVBBlockUtils {
	protected static <T extends Block> BlockResult<T> register(String id, AbstractBlock.Settings settings, Function<AbstractBlock.Settings, T> blockifier) {
		Identifier identifier = Naturverbunden.id(id);
		return new BlockResult<>(identifier, Registry.register(Registry.BLOCK, identifier, blockifier.apply(settings)));
	}

	protected static <T extends Block> T registerBlockItem(BlockResult<T> base, Item.Settings settings) {
		Registry.register(Registry.ITEM, base.id, new BlockItem(base.block, settings));
		return base.get();
	}

	protected static <T extends BlockEntity> BlockEntityType<T> create(String id, FabricBlockEntityTypeBuilder<T> builder) {
		Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id);
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, Naturverbunden.id(id), builder.build(type));
	}

	protected static Boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	public static class BlockResult<T extends Block> {
		private BlockResult(Identifier id, T block) {
			this.id = id;
			this.block = block;
		}

		private final Identifier id;
		private final T block;

		public T get() {
			return this.block;
		}
	}
}
