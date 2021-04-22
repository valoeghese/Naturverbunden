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

import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.fabricmc.fabric.api.util.BooleanFunction;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class GasBlock extends AirBlock implements BooleanFunction<StatusEffectInstance> {
	public GasBlock(Settings settings, @Nullable Supplier<StatusEffectInstance> effectStrong, Supplier<StatusEffectInstance> effectWeak) {
		super(settings);
		this.effectStrong = effectStrong;
		this.effectWeak = effectWeak;
		this.setDefaultState(this.getDefaultState().with(CONCENTRATION, MAX_CONCENTRATION));
	}

	@Nullable
	private final Supplier<StatusEffectInstance> effectStrong;
	private final Supplier<StatusEffectInstance> effectWeak;

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(CONCENTRATION);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.getBlockTickScheduler().schedule(pos, this, 20 * 2);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int level = state.get(CONCENTRATION);

		if (--level > 0) { // If the decremented level is greater than 0.
			// Change to the state we want to set
			state = state.with(CONCENTRATION, level);

			// Loop around surrounding blocks.
			for (Direction direction : Direction.values()) {
				BlockPos pos2 = pos.offset(direction); // great variable name I know

				if (world.isInBuildLimit(pos2)) {
					BlockState existing = world.getBlockState(pos2);

					if (existing.isAir()) {
						Block existingBlock = existing.getBlock();
						
						boolean isThis = existingBlock == this;
						
						if (isThis) {
							if (existing.get(CONCENTRATION) >= level) {
								continue;
							}
						}

						if (!(existingBlock instanceof GasBlock) || isThis) { // if the same block or a non-gas air block.
							world.setBlockState(pos2, state);
						}
					}
				}
			}

			world.getBlockTickScheduler().schedule(pos, this, 20 * 2);
		}
	}

	@Override
	public StatusEffectInstance apply(boolean strong) {
		return strong ? (this.effectStrong == null ? null : this.effectStrong.get()) : this.effectWeak.get();
	}

	public static final int MAX_CONCENTRATION = 5;
	public static final IntProperty CONCENTRATION = IntProperty.of("concentration", 1, MAX_CONCENTRATION);
}
