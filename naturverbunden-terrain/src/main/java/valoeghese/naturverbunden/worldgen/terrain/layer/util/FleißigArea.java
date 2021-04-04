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

package valoeghese.naturverbunden.worldgen.terrain.layer.util;

import java.util.Arrays;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.layer.util.LayerOperator;
import net.minecraft.world.biome.layer.util.LayerSampler;

/**
 * Caching Layer Sampler that shortens the number of possible hash values via using an `original & mask` algorithm, and uses an array lookup.
 * Original concept by Gegy, implementation written from scratch by me.
 * @implNote originally a mixin (warning: mojmap): https://github.com/valoeghese/EpicValoMod/blob/master/src/main/java/valoeghese/epic/mixin/MixinLazyArea.java
 */
public class FleißigArea implements LayerSampler {
	public FleißigArea(Long2IntLinkedOpenHashMap map, int size, LayerOperator operator) {
		this.operator = operator;
		int arrSize = 1; // 2^n = 2 * (2^(n-1))
		int nextArrSize;

		while (true) {
			if ((nextArrSize = (arrSize << 1)) > size) {
				break;
			}

			arrSize = nextArrSize;
		}

		if (arrSize > size) {
			throw new RuntimeException("Fast Array Size " + arrSize + " must be smaller or equal to the provided size! (" + size + ")");
		}

		this.mask = arrSize - 1;
		this.positions = new long[arrSize];
		this.values = new int[arrSize];

		// It's never gonna be max value so this is a good "no value" template
		Arrays.fill(this.positions, Long.MAX_VALUE);
	}

	private final LayerOperator operator;

	private int mask;
	private long[] positions;
	private int[] values;

	@Override
	public int sample(int x, int y) {
		try {
			long pos = ChunkPos.toLong(x, y);
			int loc = mix5(x, y) & this.mask;

			if (this.positions[loc] != pos) {
				this.positions[loc] = pos;
				return this.values[loc] = this.operator.apply(x, y);
			} else {
				return this.values[loc];
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("FleißigArea broke! You'll need to restart your game, sadly. If this issue persists, let me (Valoeghese) know!");
			throw new RuntimeException(e);
		}
	}

	private static int mix5(int a, int b) {
		return (((a >> 4) & 1) << 9) |
				(((b >> 4) & 1) << 8) |
				(((a >> 3) & 1) << 7) |
				(((b >> 3) & 1) << 6) |
				(((a >> 2) & 1) << 5) |
				(((b >> 2) & 1) << 4) |
				(((a >> 1) & 1) << 3) |
				(((b >> 1) & 1) << 2) |
				((a & 1) << 1) |
				(b & 1);
	}
}
