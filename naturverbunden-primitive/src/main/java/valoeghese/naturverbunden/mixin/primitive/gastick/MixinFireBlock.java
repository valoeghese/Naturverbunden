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

package valoeghese.naturverbunden.mixin.primitive.gastick;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import valoeghese.naturverbunden.block.primitive.CombustibleGasBlock;

@Mixin(FireBlock.class)
public class MixinFireBlock {
	@Inject(at = @At("HEAD"), method = "getBurnChance(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)I", cancellable = true)
	private void getBurnChance(WorldView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		if (world.getBlockState(pos).getBlock() instanceof CombustibleGasBlock) {
			info.setReturnValue(2);
		}
	}
}
