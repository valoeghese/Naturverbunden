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

package valoeghese.naturverbunden.worldgen.terrain.layer;

import net.minecraft.world.biome.layer.type.DiagonalCrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

/**
 * Increase Edge Curvature layer 2 electric boogaloo.
 * @implNote https://github.com/valoeghese/FractalBiomeGeneration/blob/master/src/tk/valoeghese/world/gen/fractal/FractalShapeEdge.java
 */
public enum DiagonalShapeEdgeLayer implements DiagonalCrossSamplingLayer {
	INSTANCE;

	@Override
	public int sample(LayerRandomnessSource random, int sw, int se, int nw, int ne, int centre) {
		if (centre != 0) {
			if (sw == 0 || se == 0 || nw == 0 || ne == 0) {
				if (random.nextInt(6) == 0) {
					return 0; // Maybe indent the ocean in
				}
			}
			return centre;
		} else {
			int chance = 1; // pick equally among all non-ocean biomes by incrementing this number after each successful non-ocean detection.
			//                 (one non-ocean = certain, two non-oceans = pick first one (100% chance for all)
			//                 1/2 chance to be second one (overall, 50% chance for all).
			//                 And so on.
			int landResult = 1; // temporary initialisation to something else to fix compile errors

			if (sw != 0 && random.nextInt(chance++) == 0) {
				landResult = sw;
			}
			if (se != 0 && random.nextInt(chance++) == 0) {
				landResult = se;
			}
			if (nw != 0 && random.nextInt(chance++) == 0) {
				landResult = nw;
			}
			if (ne != 0 && random.nextInt(chance++) == 0) {
				landResult = ne;
			}

			// chance to be replace ocean with land here is dependent on how much land borders this tile
			// 100% chance if surrounded by land
			return random.nextInt(4) < (chance - 1) ? landResult : 0;
		}
	}
}
