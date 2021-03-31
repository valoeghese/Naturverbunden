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

package valoeghese.naturverbunden.common.primitive;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class ItemBlock extends BlockWithEntity {
	public ItemBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (hand == Hand.MAIN_HAND) {
			ItemBlockEntity entity = (ItemBlockEntity) world.getBlockEntity(pos);
			ItemStack stack = player.getStackInHand(hand);

			if (stack.isEmpty()) {
				PlayerInventory inventory = player.getInventory();
				Optional<ItemStack> s = entity.removeItem(world, pos);

				if (s.isPresent()) {
					inventory.insertStack(inventory.selectedSlot, s.get());
					return ActionResult.success(world.isClient());
				}
			} else {
				if (!stack.hasEnchantments()) {
					if (entity.addItem(new ItemStack(stack.getItem(), 1))) {
						stack.decrement(1);
						return ActionResult.SUCCESS;
					}
				} else if (player.isSneaking() && entity.removeItem(world, pos, stack)) {
					return ActionResult.success(world.isClient());
				}
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		ItemBlockEntity entity = (ItemBlockEntity) world.getBlockEntity(pos);
		entity.hit();
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		ItemBlockEntity entity = (ItemBlockEntity) world.getBlockEntity(pos);

		for (ItemStack stack : entity.getItems()) {
			if (!stack.isEmpty()) {
				ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
				itemEntity.setPickupDelay(40);

				float f = world.getRandom().nextFloat() * 0.5F;
				float g = world.getRandom().nextFloat() * 6.2831855F;
				itemEntity.setVelocity((double)(-MathHelper.sin(g) * f), 0.2D, (double)(MathHelper.cos(g) * f));

				if (!world.isClient()) {
					world.spawnEntity(itemEntity);
				}
			}
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ItemBlockEntity(pos, state);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos down = pos.down();
		return world.getBlockState(down).isSolidBlock(world, down);
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return type == NavigationType.AIR && !this.collidable ? true : super.canPathfindThrough(state, world, pos, type);
	}

	public static VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16.0, 1.0, 16.0);
}
