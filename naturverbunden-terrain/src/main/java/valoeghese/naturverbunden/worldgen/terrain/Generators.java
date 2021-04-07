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

package valoeghese.naturverbunden.worldgen.terrain;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.Target;
import valoeghese.naturverbunden.worldgen.Generator;

/**
 * Naturverbunden Feature Generators.
 */
public class Generators {
	public static final Generator<OreFeatureConfig> ORE = new Generator<OreFeatureConfig>() {
		@Override
		public boolean generate(WorldAccess world, BlockPos origin, Random rand, OreFeatureConfig config) {
			// Related to this https://www.desmos.com/calculator/bq6kptsyno
			// Only can really fit the ores by size>=4

			BlockPos.Mutable pos = new BlockPos.Mutable();
			final int minAmount = config.size / 2;
			final int startAmount = rand.nextInt(config.size - minAmount) + minAmount;
			int amount = startAmount;
			int rad = Math.min(3, config.size / 4);
			int startX = origin.getX();
			int startY = origin.getY();
			int startZ = origin.getZ();

			int squaresApprox = (int) ((4.0 / 3.0) * Math.PI * (rad * rad * rad));

			start: for (int xo = -rad; xo < rad; ++xo) {
				pos.setX(xo + startX);

				for (int zo = -rad; zo < rad; ++zo) {
					pos.setZ(zo + startZ);

					for (int yo = -rad; yo < rad; ++yo) {
						pos.setY(yo + startY);

						if (xo * xo + zo * zo + yo * yo <= rad * rad) {
							if (!world.isOutOfHeightLimit(pos)) {
								if (rand.nextInt(squaresApprox) < amount) {
									try {
										BlockState currentState = world.getBlockState(pos);
										
										for (Target target : config.targets) {
											if (target.target.test(currentState, rand)) {
												this.setBlockState(world, pos, target.state);
												amount--;
												break;
											}
										}
									} catch (Exception e) {
										System.out.println(pos);
										throw new RuntimeException(e);
									}
								}
							}

							if (amount < 1) {
								break start;
							}

							if (--squaresApprox < 1) {
								break start;
							}
						}
					}
				}
			}

			return amount < startAmount;
		}
	};
}
