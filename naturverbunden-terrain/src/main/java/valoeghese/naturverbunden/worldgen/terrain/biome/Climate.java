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

package valoeghese.naturverbunden.worldgen.terrain.biome;

import java.util.Arrays;

import valoeghese.naturverbunden.core.NVBMathUtils;
import valoeghese.naturverbunden.worldgen.terrain.layer.TerrainInfoSampler;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public final class Climate {
	Climate(TerrainType terrainType) {
		this.types = new TerrainType[8];
		Arrays.fill(this.types, terrainType);
	}

	Climate(TerrainType... terrainTypes) {
		this.types = new TerrainType[8];
		this.setTypes(terrainTypes);
	}

	public void setTypes(TerrainType... terrainTypes) {
		if (NVBMathUtils.isPowerOfTwo(terrainTypes.length) && terrainTypes.length <= 8) {
			int amountToUse = 8 / terrainTypes.length;

			int start = 0;
			int next = amountToUse;
			for (TerrainType type : terrainTypes) {
				Arrays.fill(this.types, start, next, type);
				start = next;
				next += amountToUse;
			}

		} else throw new IllegalStateException("Array size must be a power of two and less than or equal to 8");
	}

	public final TerrainType[] types;

	public TerrainType get(TerrainInfoSampler.Info info) {
		TerrainType result = this.types[info.getInfo()];

		if (info.isLargeHills()) {
			result = result.largeHills;
		}

		return info.isSmallHills() ? result.smallHills : result;
	}

	/**
	 * Debug
	 */
	public static void main(String[] args) {
		int[] c = new int[] {8463, 69};
		int amountToUse = 8 / c.length;
		int start = 0;
		int next = amountToUse;
		int[] result = new int[8];

		for (int type : c) {
			Arrays.fill(result, start, next, type);
			start = next;
			next += amountToUse;
		}

		for (int i : result) System.out.println(i);
		//Noise n = new Noise(new Random(), 1);
	}
}
