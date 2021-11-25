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

import java.util.Random;
import java.util.function.Function;

import valoeghese.naturverbunden.terrain.core.noise.INoise;
import valoeghese.naturverbunden.terrain.core.noise.RidgedSimplexGenerator;

public class TerrainGenerator {
	public TerrainGenerator(long seed) {
		Random random = new Random(seed);
		this.noiseSupplier = n -> n.apply(random); // I would avoid this lambda mess but it's run a single time: startup. So I think it's fine.
		this.baseHeightGenerator = this.noiseSupplier.apply(RidgedSimplexGenerator::new);
	}

	private final Function<Function<Random, INoise>, INoise> noiseSupplier;
	private final INoise baseHeightGenerator;

	public double baseHeight(double x, double z) {
		return BASE_AVERAGE_HEIGHT * BASE_HEIGHT_AMPLITUDE * this.baseHeightGenerator.sample(x * BASE_HEIGHT_FREQUENCY, z * BASE_HEIGHT_FREQUENCY);
	}

	public void pregen() {
		// TODO main pregen phase.
	}

	public int height(int x, int z) {
		return (int)this.baseHeight(x, z);
	}

	private static final double BASE_HEIGHT_FREQUENCY = 1.0 / 1024.0;
	private static final double BASE_HEIGHT_AMPLITUDE = 60.0;
	private static final double BASE_AVERAGE_HEIGHT = 63.0;
}
