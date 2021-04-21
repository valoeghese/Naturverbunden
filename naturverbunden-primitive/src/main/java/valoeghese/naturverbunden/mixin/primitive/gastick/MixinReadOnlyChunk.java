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

import net.minecraft.block.Block;
import net.minecraft.world.chunk.ReadOnlyChunk;
import valoeghese.naturverbunden.mechanics.primitive.GasMechanics;

@Mixin(ReadOnlyChunk.class)
public class MixinReadOnlyChunk {
	// Synthetic Method: Lambda Predicate for Block Tick Scheduler
	@Inject(at = @At("HEAD"), method = "method_12242", cancellable = true)
	private static void onBlockTest(Block block, CallbackInfoReturnable<Boolean> info) {
		GasMechanics.shouldExclude(block, info);
	}
}