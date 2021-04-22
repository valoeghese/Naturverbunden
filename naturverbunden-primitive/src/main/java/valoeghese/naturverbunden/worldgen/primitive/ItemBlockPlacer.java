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

import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.BlockPlacerType;
import valoeghese.naturverbunden.Naturverbunden;
import valoeghese.naturverbunden.block.primitive.ItemBlockEntity;
import valoeghese.naturverbunden.core.NVBWorldgenUtils;

public class ItemBlockPlacer extends BlockPlacer {
	public ItemBlockPlacer(Item item) {
		this.item = item;
	}

	private Item item;

	@Override
	public void generate(WorldAccess world, BlockPos pos, BlockState state, Random random) {
		world.setBlockState(pos, state, 2);
		ItemBlockEntity entity = (ItemBlockEntity) world.getBlockEntity(pos);

		if (entity == null) {
			Naturverbunden.LOGGER.warn("THERES A DAMN NULL ITEM ENTITY AT " + pos);
		} else {
			entity.setItemWorldgen(this.item);
		}
	}

	@Override
	protected BlockPlacerType<?> getType() {
		return PrimitiveWorldgen.ITEM_BLOCK_PLACER;
	}

	public static final Codec<ItemBlockPlacer> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(Identifier.CODEC.fieldOf("item")
				.forGetter(placer -> Registry.ITEM.getId(placer.item)))
				.apply(instance, id -> new ItemBlockPlacer(Registry.ITEM.get(id)));
	});
}
