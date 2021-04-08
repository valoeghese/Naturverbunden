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

package valoeghese.naturverbunden.worldgen;

import net.minecraft.util.math.MathHelper;

public class Perlerper {
	public Perlerper(int height, int startX, int minY, int startZ, TripleSampler sampler) {
		final int l2 = (height / 8) + 1;
		this.values = new double[5][l2][5];
		this.miny = minY;

		for (int x = 0; x < 5; ++x) {
			for (int y = 0; y < l2; ++y) {
				for (int z = 0; z < 5; ++z) {
					this.values[x][y][z] = sampler.sample(x, y, z);
				}
			}
		}
	}

	private final double[][][] values;
	private final int miny;

	public double sample(int x, int y, int z) {
		y += this.miny;
		int locX = x >> 2;
				int locY = y >> 3;
		int locZ = z >> 3;

		double p000 = this.values[locX][locY][locZ];
		double p001 = this.values[locX][locY][locZ + 1];
		double p010 = this.values[locX][locY + 1][locZ];
		double p011 = this.values[locX][locY + 1][locZ + 1];

		double p100 = this.values[locX + 1][locY][locZ];
		double p101 = this.values[locX + 1][locY][locZ + 1];
		double p110 = this.values[locX + 1][locY + 1][locZ];
		double p111 = this.values[locX + 1][locY + 1][locZ + 1];

		double deltaX = (x & 0b11) / 4.0;
		double deltaY = (y & 0b111) / 8.0;
		double deltaZ = (z & 0b11) / 4.0;

		return MathHelper.lerp3(deltaX, deltaY, deltaZ, p000, p100, p010, p110, p001, p101, p011, p111);
	}

	@FunctionalInterface
	public interface TripleSampler {
		double sample(int x, int y, int z);
	}
}
