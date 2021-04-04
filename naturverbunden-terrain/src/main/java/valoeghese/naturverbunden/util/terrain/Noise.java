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

import java.util.Random;
import java.util.function.Function;

/**
 * Base class for octave noise samplers.
 */
public class Noise {
	public Noise(Random random, int octaves) {
		this(random, octaves, OpenSimplexGenerator::new);
	}

	public Noise(Random random, int octaves, Function<Random, OpenSimplexGenerator> constructor) {
		this.samplers = new OpenSimplexGenerator[octaves];
		this.clamp = 1D / (1D - (1D / Math.pow(2, octaves))); // I came up with this algorithm like a year ago I can't remember how but it probably works

		for (int i = 0; i < octaves; ++i) {
			this.samplers[i] = constructor.apply(random);
		}
	}

	private final OpenSimplexGenerator[] samplers;
	private double clamp;

	public double sample(double x, double y) {
		double amplFreq = 0.5D;
		double result = 0;
		for (OpenSimplexGenerator sampler : this.samplers) {
			result += (amplFreq * sampler.sample(x / amplFreq, y / amplFreq));

			amplFreq *= 0.5D;
		}

		return result * this.clamp;
	}
}
