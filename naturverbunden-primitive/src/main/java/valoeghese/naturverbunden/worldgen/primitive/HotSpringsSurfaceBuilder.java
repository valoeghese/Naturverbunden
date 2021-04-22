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

package valoeghese.naturverbunden.worldgen.primitive;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import valoeghese.naturverbunden.block.primitive.PrimitiveBlocks;

public class HotSpringsSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
	public HotSpringsSurfaceBuilder() {
		super(TernarySurfaceConfig.CODEC);
	}

	@Override
	public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int depth, long seed, TernarySurfaceConfig surfaceConfig) {
		if (height < THRESHOLD_0a || height > THRESHOLD_0b) {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, MODIFIED_SEA_LEVEL, depth, seed, HS_0_CONFIG);
		} else if (height > THRESHOLD_3) {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, MODIFIED_SEA_LEVEL, depth, seed, HS_2_CONFIG);
		} else if (height > THRESHOLD_2) {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, MODIFIED_SEA_LEVEL, depth, seed, HS_3_CONFIG);
		} else {
			SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, MODIFIED_SEA_LEVEL, depth, seed, HS_1_CONFIG);
		}
	}

	public static final int MODIFIED_SEA_LEVEL = 100;
	public static final int THRESHOLD_0a = MODIFIED_SEA_LEVEL - 3;
	public static final int THRESHOLD_0b = MODIFIED_SEA_LEVEL + 3;
	public static final int THRESHOLD_2 = MODIFIED_SEA_LEVEL + 1;
	public static final int THRESHOLD_3 = MODIFIED_SEA_LEVEL + 2;

	private static final TernarySurfaceConfig HS_1_CONFIG = new TernarySurfaceConfig(
			PrimitiveBlocks.SYNECHOCOCCUS_COVERED_ROCK.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState());

	private static final TernarySurfaceConfig HS_2_CONFIG = new TernarySurfaceConfig(
			PrimitiveBlocks.CAROTENOID_COVERED_ROCK.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState());
	
	private static final TernarySurfaceConfig HS_3_CONFIG = new TernarySurfaceConfig(
			PrimitiveBlocks.DEINOCOCCUS_COVERED_ROCK.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState());
	
	private static final TernarySurfaceConfig HS_0_CONFIG = new TernarySurfaceConfig(
			PrimitiveBlocks.BROWN_STONE.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState(),
			PrimitiveBlocks.BROWN_STONE.getDefaultState());
}
