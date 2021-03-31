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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import valoeghese.naturverbunden.mechanics.primitive.PrimitiveCrafting;

public class ItemBlockEntity extends BlockEntity {
	public ItemBlockEntity(BlockPos pos, BlockState state) {
		super(PrimitiveContent.ITEM_BLOCK_ENTITY, pos, state);
		this.items = DefaultedList.ofSize(3, ItemStack.EMPTY);
	}

	private final DefaultedList<ItemStack> items;
	private int craftProgress = 0;

	public DefaultedList<ItemStack> getItems() {
		return this.items;
	}

	/**
	 * @return A value between 0 and 4 as the progress in crafting.
	 */
	public int getCraftProgress() {
		return this.craftProgress;
	}

	public void setItemWorldgen(Item item) {
		this.items.set(0, item.getDefaultStack());
		this.markDirty();
	}

	public boolean addItem(ItemStack item) {
		for(int i = 0; i < this.items.size(); ++i) {
			ItemStack itemStack = this.items.get(i);

			if (itemStack.isEmpty()) {
				this.items.set(i, item);
				this.craftProgress = 0;
				this.updateListeners();
				return true;
			}
		}

		return false;
	}

	public Optional<ItemStack> removeItem(World world, BlockPos pos) {
		for(int i = this.items.size() - 1; i >= 0; --i) {
			ItemStack itemStack = this.items.get(i);

			if (!itemStack.isEmpty()) {
				this.items.set(i, ItemStack.EMPTY);
				this.craftProgress = 0;
				this.updateListeners();
				boolean empty = true;

				// Check if more items
				for (--i; i >= 0; --i) {
					if (!this.items.get(i).isEmpty()) {
						empty = false;
						break;
					}
				}

				if (empty) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}

				return Optional.of(itemStack);
			}
		}

		return Optional.empty();
	}

	public boolean removeItem(World world, BlockPos pos, ItemStack stack) {
		if (stack.getCount() == stack.getMaxCount()) {
			return false;
		}

		Item item = stack.getItem();
		boolean empty = true;

		for(int i = this.items.size() - 1; i >= 0; --i) {
			ItemStack itemStack = this.items.get(i);

			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() == item) {
					this.items.set(i, ItemStack.EMPTY);
					stack.increment(1);
					this.craftProgress = 0;
					this.updateListeners();

					// Check if more items
					if (empty) {
						for (--i; i >= 0; --i) {
							if (!this.items.get(i).isEmpty()) {
								empty = false;
								break;
							}
						}
					}

					if (empty) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}

					return true;
				}

				empty = false;
			}
		}

		return false;
	}

	/**
	 * @return A list of the items in this block entity.
	 */
	private List<Item> getContents() {
		return this.items.stream()
				.filter(stack -> !stack.isEmpty())
				.map(ItemStack::getItem)
				.collect(Collectors.toList());
	}

	public void hit() {
		if (this.craftProgress > 0 || PrimitiveCrafting.match(this.getContents())) {
			if (++this.craftProgress > 4) {
				Item result = PrimitiveCrafting.get(this.getContents());

				this.items.clear();
				this.items.set(0, result.getDefaultStack());
			}

			this.updateListeners();
		}
	}

	private void updateListeners() {
		this.markDirty();
		this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
	}

	public void clear() {
		this.items.clear();
	}

	// NBT

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		this.items.clear();
		Inventories.readNbt(tag, this.items);
		this.craftProgress = tag.getInt("craftProgress");
	}


	private NbtCompound saveInitialChunkData(NbtCompound tag) {
		super.writeNbt(tag);
		Inventories.writeNbt(tag, this.items, true);
		tag.putInt("craftProgress", this.craftProgress);
		return tag;
	}

	// Backend NBT Hell

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		this.saveInitialChunkData(tag);
		return tag;
	}

	@Nullable
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, Registry.BLOCK_ENTITY_TYPE.getRawId(this.getType()), this.toInitialChunkDataNbt());
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return this.saveInitialChunkData(new NbtCompound());
	}
}
