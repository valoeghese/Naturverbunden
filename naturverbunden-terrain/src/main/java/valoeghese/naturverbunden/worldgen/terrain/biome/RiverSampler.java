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

import java.util.Random;

import valoeghese.naturverbunden.util.terrain.OpenSimplexGenerator;
import valoeghese.naturverbunden.util.terrain.Voronoi;

public class RiverSampler {
	public RiverSampler(Random gr) {
		this.offsetX = new OpenSimplexGenerator(gr);
		this.offsetZ = new OpenSimplexGenerator(gr);
		this.voronoiSeed = gr.nextInt();
	}

	private final OpenSimplexGenerator offsetX;
	private final OpenSimplexGenerator offsetZ;
	private final int voronoiSeed;

	public double sample(int rx, int rz) {
		final double cutoff = 0.08 + 0.012 * this.offsetX.sample(rx * 0.0008 + 1, rz * 0.00081);
		final double normaliser = 1 / cutoff;

		double scalex = rx * SCALE;
		double scalez = rz * SCALE;

		double x = scalex + 0.5 * this.offsetX.sample(scalex * 2, scalez * 2);
		double z = scalez + 0.5 * this.offsetZ.sample(scalex * 2, scalez * 2);

		double worley = Voronoi.sampleD1D2SquaredWorley(x, z, this.voronoiSeed);
		worley = cutoff - worley;

		return normaliser * worley;
	}

	private static final double SCALE = 1.0 / 1666.0;
}
