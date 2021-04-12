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

	public static boolean isPowerOfTwo(int i) {
		return (i & (i - 1)) == 0;
	}

	public static int log2p2(int powerOfTwo) {
		// TODO try get an O(1) algorithm. Perhaps abusing the existance of the not operator.
		int res = 0;
		while ((powerOfTwo >>= 1) > 0) ++res;
		return res;
	}
}
