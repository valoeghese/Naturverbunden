/*
 * Naturverbunden
 * Copyright (C) 2021, 2022 Valoeghese
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

package valoeghese.naturverbunden.terrain.core;

import net.minecraft.world.phys.Vec2;

/**
 * Stolen and adapted from 2fc0f18.
 * @author Valoeghese
 */
public class MathsUtils {
	private MathsUtils() {
		// NO-OP
	}

	public static float lerp(float min, float max, float progress) {
		return min + progress * (max - min);
	}

	public static double lerp(double min, double max, double progress) {
		return min + progress * (max - min);
	}

	public static float clampMap(float value, float min, float max, float newmin, float newmax) {
		value -= min;
		value /= (max - min);
		value = newmin + value * (newmax - newmin);

		if (value > newmax) {
			return newmax;
		} else if (value < newmin) {
			return newmin;
		} else {
			return value;
		}
	}

	public static int clamp(int i, int low, int high) {
		return i < low ? low : (i > high ? high : i);
	}

	public static float clamp(float f, float low, float high) {
		return f < low ? low : (f > high ? high : f);
	}

	public static double clamp(double d, double low, double high) {
		return d < low ? low : (d > high ? high : d);
	}

	public static int sign(double d) {
		if (d == 0) {
			return 0;
		}

		return d > 0 ? 1 : -1;
	}

	public static int floor(float f) {
		int i = (int) f;
		return f < i ? i - 1 : i;
	}

	public static int manhattan(int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		return dx + dy;
	}

	public static float squaredDist(float x0, float y0, float x1, float y1) {
		float dx = Math.abs(x1 - x0);
		float dy = Math.abs(y1 - y0);
		return dx * dx + dy * dy;
	}

	public static int floor(double f) {
		return (int) Math.floor(f);
	}

	public static boolean withinBounds(double val, double bound0, double bound1, double falloff) {
		if (bound0 > bound1) {
			return withinBounds(val, bound1, bound0, falloff);
		}

		bound0 -= falloff;
		bound1 += falloff;
		System.out.println("bound0 " + bound0 + " bound1 " + bound1);

		return bound0 <= val && val <= bound1;
	}

	public static double distanceLineBetween(Vec2 start, Vec2 end, int x, int z) {
		return distanceLineBetween(start.x, start.y, end.x, end.y, x, z);
	}

	// Stolen from Khaki
	public static double distanceLineBetween(double startX, double startZ, double endX, double endZ, int x, int z) {
		double dx = endX - startX;
		double dz = endZ - startZ;

		// try fix bugs by swappings all x and z and doing it backwards
		if (Math.abs(dz) > Math.abs(dx)) {
			// cache old vals
			double oldDX = dx;
			double oldSX = startX;
			//double oldEX = endX; unused
			int oldX = x;

			// swap
			dx = dz;
			startX = startZ;
			//endX = endZ;
			x = z;

			dz = oldDX;
			startZ = oldSX;
			//endZ = oldEX;
			z = oldX;
		}

		double m = dz / dx;
		double targetZ = m * x + startZ - m * startX;
		return Math.abs(z - targetZ);
	}
}
