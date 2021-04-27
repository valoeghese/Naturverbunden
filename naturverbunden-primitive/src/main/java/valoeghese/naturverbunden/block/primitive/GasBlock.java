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
import java.util.function.IntFunction;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// Perhaps stop scheduling tick when fully stable and then wait for neighbouring block updates to start scheduling ticks again
public class GasBlock extends AirBlock implements IntFunction<StatusEffectInstance> {
	public GasBlock(Settings settings, boolean rises, IntFunction<StatusEffectInstance> effect) {
		super(settings);
		this.effect = effect;
		this.sinkDirection = rises ? Direction.UP : Direction.DOWN;
		this.setDefaultState(this.getDefaultState().with(CONCENTRATION, MAX_CONCENTRATION));
	}

	private final Direction sinkDirection;
	private final IntFunction<StatusEffectInstance> effect;

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(CONCENTRATION);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.getBlockTickScheduler().schedule(pos, this, 50);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int level = state.get(CONCENTRATION);

		// Level to not replace
		int ignoreLevel = 0;

		// Sinking
		if (pos.getY() > world.getBottomY()) {
			BlockPos sinkPos = pos.offset(this.sinkDirection);
			BlockState existing = world.getBlockState(sinkPos);

			if (existing.isAir()) {
				if (!(existing.getBlock() instanceof GasBlock) || existing.get(CONCENTRATION) < level) {
					world.setBlockState(sinkPos, state); // set sinking position to this concentration
					world.setBlockState(pos, Blocks.AIR.getDefaultState()); // set this to air
					world.getBlockTickScheduler().schedule(pos, this, 50);
					return; // don't diffuse
				}
			}
		}

		// Diffusing
		if (--level > 0) { // If the decremented level is greater than 0.
			if (ignoreLevel == 0) {
				// Don't replace things that are already at this level.
				ignoreLevel = level;
			}

			// Change to the state we want to set
			state = state.with(CONCENTRATION, level);

			// Loop around surrounding blocks.
			for (Direction direction : Direction.values()) {
				BlockPos pos2 = pos.offset(direction); // great variable name I know

				if (world.isInBuildLimit(pos2)) {
					BlockState existing = world.getBlockState(pos2);

					if (existing.isAir()) {
						Block existingBlock = existing.getBlock();

						boolean isGas = existingBlock instanceof GasBlock;

						// If too high a concentration to overtake
						if (isGas) {
							if (existing.get(CONCENTRATION) >= ignoreLevel) {
								continue;
							}
						}

						// If already belongs to a sinking gas block of type: this
						BlockPos posUp = pos2.offset(this.sinkDirection.getOpposite());

						if (direction != this.sinkDirection && world.isInBuildLimit(posUp) && world.getBlockState(posUp).getBlock() == this) {
							continue;
						}

						world.setBlockState(pos2, state);
					}
				}
			}

			world.getBlockTickScheduler().schedule(pos, this, 50);
		}
	}

	@Override
	public StatusEffectInstance apply(int strength) {
		return this.effect.apply(strength);
	}

	public static final int MAX_CONCENTRATION = 16;
	public static final IntProperty CONCENTRATION = IntProperty.of("concentration", 1, MAX_CONCENTRATION);

	/**
	 * Hax to specify death while having null = nothing.
	 */
	public static final StatusEffectInstance INSTANT_DEATH = new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE);
}
