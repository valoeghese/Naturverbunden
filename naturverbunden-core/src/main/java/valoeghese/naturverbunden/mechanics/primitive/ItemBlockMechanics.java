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

package valoeghese.naturverbunden.mechanics.primitive;

import java.util.function.Consumer;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import valoeghese.naturverbunden.common.primitive.ItemBlockEntity;
import valoeghese.naturverbunden.common.primitive.PrimitiveContent;

public class ItemBlockMechanics {
	public static void placeItem(ItemUsageContext context, Consumer<ActionResult> sreturn) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos().offset(context.getSide());
		ItemStack stack = context.getStack();

		// If it isn't empty nor it has enchantments and it's not food (unless you're sneaking) and the position valid
		if (!stack.isEmpty() && !stack.hasEnchantments() && (!stack.isFood() || context.getPlayer().isSneaking()) && !world.isOutOfHeightLimit(pos)) {
			if (world.isAir(pos)) {
				BlockState state = PrimitiveContent.ITEM_BLOCK.getDefaultState();

				// Set the block in the world
				world.setBlockState(pos, state);
				ItemBlockEntity entity = (ItemBlockEntity) world.getBlockEntity(pos);
				entity.addItem(new ItemStack(stack.getItem(), 1));

				// Decrement stack
				stack.decrement(1);

				sreturn.accept(ActionResult.SUCCESS);
			}
		}
	}
}
