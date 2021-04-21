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

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import valoeghese.naturverbunden.block.primitive.GasBlock;

public class GasMechanics {
	public static void initialise() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if ((server.getTicks() & 0b1111) == 0) {
				for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) { // slightly faster than .forEach()
					if (!player.isCreative()) { // Don't target creative players
						BlockPos headPos = player.getCameraBlockPos(); // Because you don't breathe through your feet
						ServerWorld world = player.getServerWorld();

						if (!world.isOutOfHeightLimit(headPos)) {
							BlockState state = world.getBlockState(headPos);
							Block block = state.getBlock();

							if (block instanceof GasBlock) {
								// Could have written this in one line but that would be messy
								StatusEffectInstance instance = ((GasBlock) block).apply(state.get(GasBlock.CONCENTRATION) == GasBlock.MAX_CONCENTRATION);

								if (instance == null) {
									player.kill();
								} else {
									player.addStatusEffect(instance);
								}
							}
						}
					}
				}
			}
		});
	}

	public static void shouldExclude(Block block, CallbackInfoReturnable<Boolean> info) {
		if (block instanceof GasBlock) {
			info.setReturnValue(false);
		}
	}
}
