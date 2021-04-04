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

package valoeghese.naturverbunden.util.terrain;

import net.minecraft.util.math.MathHelper;

/**
 * Voronoi calculations taken from older code I've written. Changed to use doubles instead of floats.
 */
public final class Voronoi {
	public static Vec2d sampleVoronoiGrid(int x, int y, int seed) {
		double vx = x + randomdouble(x, y, seed);
		double vy = y + randomdouble(x, y, seed + 1);
		return new Vec2d(vx, vy);
	}

	public static Vec2d sampleEvenVoronoi(double x, double y, int seed) {
		final int baseX = MathHelper.floor(x);
		final int baseY = MathHelper.floor(y);
		double rx = 0;
		double ry = 0;
		double rdist = 1000;

		for (int xo = -1; xo <= 1; ++xo) {
			int gridX = baseX + xo;

			for (int yo = -1; yo <= 1; ++yo) {
				int gridY = baseY + yo;

				// ensure more evenly distributed
				double vx = gridX + (randomdouble(gridX, gridY, seed) + 0.5) * 0.5;
				double vy = gridY + (randomdouble(gridX, gridY, seed + 1) + 0.5) * 0.5;
				double vdist = squaredDist(x, y, vx, vy);

				if (vdist < rdist) {
					rx = vx;
					ry = vy;
					rdist = vdist;
				}
			}
		}

		return new Vec2d(rx, ry);
	}

	public static Vec2d sampleVoronoi(double x, double y, int seed) {
		final int baseX = MathHelper.floor(x);
		final int baseY = MathHelper.floor(y);
		double rx = 0;
		double ry = 0;
		double rdist = 1000;

		for (int xo = -1; xo <= 1; ++xo) {
			int gridX = baseX + xo;

			for (int yo = -1; yo <= 1; ++yo) {
				int gridY = baseY + yo;

				// ensure more evenly distributed
				double vx = gridX + randomdouble(gridX, gridY, seed);
				double vy = gridY + randomdouble(gridX, gridY, seed + 1);
				double vdist = squaredDist(x, y, vx, vy);

				if (vdist < rdist) {
					rx = vx;
					ry = vy;
					rdist = vdist;
				}
			}
		}

		return new Vec2d(rx, ry);
	}

	public static Vec2d sampleManhattanVoronoi(double x, double y, int seed) {
		final int baseX = MathHelper.floor(x);
		final int baseY = MathHelper.floor(y);
		double rx = 0;
		double ry = 0;
		double rdist = 1000;

		for (int xo = -1; xo <= 1; ++xo) {
			int gridX = baseX + xo;

			for (int yo = -1; yo <= 1; ++yo) {
				int gridY = baseY + yo;

				// ensure more evenly distributed
				double vx = gridX + randomdouble(gridX, gridY, seed);
				double vy = gridY + randomdouble(gridX, gridY, seed + 1);
				double vdist = manhattanDist(x, y, vx, vy);

				if (vdist < rdist) {
					rx = vx;
					ry = vy;
					rdist = vdist;
				}
			}
		}

		return new Vec2d(rx, ry);
	}

	public static int random(int x, int y, int seed, int mask) {
		// seed *= seed * 6364136223846793005L + 1442695040888963407L are constants in biome layers
		// this seems to work fine. I have had with some variations of this times where seems to be always one value but hooopefully this is fine
		seed *= 375462423 * seed + 672456235;
		seed += x;
		seed *= 375462423 * seed + 672456235;
		seed += y;

		return seed & mask;
	}

	private static double squaredDist(double x0, double y0, double x1, double y1) {
		double dx = Math.abs(x1 - x0);
		double dy = Math.abs(y1 - y0);
		return dx * dx + dy * dy;
	}

	private static double manhattanDist(double x0, double y0, double x1, double y1) {
		double dx = Math.abs(x1 - x0);
		double dy = Math.abs(y1 - y0);
		return dx + dy;
	}

	public static double randomdouble(int x, int y, int seed) {
		return (double) random(x, y, seed, 0xFFFF) / (double) 0xFFFF;
	}
}
