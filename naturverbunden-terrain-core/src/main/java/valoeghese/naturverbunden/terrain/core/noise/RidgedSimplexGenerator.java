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

package valoeghese.naturverbunden.terrain.core.noise;

import java.util.Random;

public class RidgedSimplexGenerator extends OpenSimplexGenerator {
	public RidgedSimplexGenerator(Random rand) {
		super(rand);
	}

	@Override
	public double sample(double x) {
		return 1 - Math.abs(super.sample(x)) * 2;
	}

	@Override
	public double sample(double x, double y) {
		return 1 - Math.abs(super.sample(x, y)) * 2;
	}
}