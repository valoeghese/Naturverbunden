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

package valoeghese.naturverbunden.worldgen.terrain;

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
		final double cutoff = 0.08;
		final double normaliser = 1 / cutoff;

		double scalex = rx * 0.002;
		double scalez = rz * 0.002;

		double x = scalex + 0.5 * this.offsetX.sample(scalex * 5, scalez * 5);
		double z = scalez + 0.5 * this.offsetZ.sample(scalex * 5, scalez * 5);

		double worley = Voronoi.sampleWorley(x, z, this.voronoiSeed);
		worley = cutoff - worley;

		if (worley > 0) {
			return normaliser * worley;
		} else {
			return 0;
		}
	}
}
