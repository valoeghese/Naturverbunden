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

package valoeghese.naturverbunden.mixin.terrain;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import valoeghese.naturverbunden.worldgen.terrain.TerrainChunkGenerator;

@Mixin(OreFeature.class)
public class MixinOreFeature {
	@Inject(at = @At("HEAD"), method = "generate", cancellable = true)
	private void generate(FeatureContext<OreFeatureConfig> context, CallbackInfoReturnable<Boolean> info) {
		if (context.getGenerator() instanceof TerrainChunkGenerator) {
			BlockPos origin = context.getOrigin();
			OreFeatureConfig config = context.getConfig();
			BlockPos.Mutable pos = new BlockPos.Mutable();
			WorldAccess world = context.getWorld();
			Random random = context.getRandom();

			// I literally did all this, https://www.desmos.com/calculator/tiqi230gm8, just to realise it was related to the area of a circle smh
			// Overengineering go brrr
			// Now just using the volume of a sphere floored
			// Related to this https://www.desmos.com/calculator/bw03axt5l0
			// Only can really fit the ores by size=4

			final int minAmount = config.size / 2;
			int amount = random.nextInt(config.size - minAmount) + minAmount;
			int rad = 

			int squaresApprox = (int) ((4.0 / 3.0) * Math.PI * (rad * rad * rad));

			for (int xo = -4; xo < config.size; ++xo) {
				for (int zo = -config.size; zo < config.size; ++zo) {
					for (int yo = -config.size; yo < config.size; ++yo) {
					}
				}
			}

			info.setReturnValue(true);
		}
	}
}
