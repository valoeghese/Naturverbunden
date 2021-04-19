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

package valoeghese.naturverbunden.core;

public final class NVBMathUtils {
	private NVBMathUtils() {
	}

	/**
	 * @return whether the input number is a power of two.
	 */
	public static boolean isPowerOfTwo(int i) {
		return (i & (i - 1)) == 0;
	}

	/**
	 * @return the base2 log of the given power of two.
	 */
	public static int log2p2(int powerOfTwo) {
		// TODO try get an O(1) algorithm. Perhaps abusing the existance of the unary not operator.
		int res = 0;
		while ((powerOfTwo >>= 1) > 0) ++res;
		return res;
	}

	/**
	 * Triangle wave with period 2 and amplitude 0.5 in the range [0,1].
	 */
	public static double tri(double d) {
		return Math.abs((d % 2) - 1);
	}
}

