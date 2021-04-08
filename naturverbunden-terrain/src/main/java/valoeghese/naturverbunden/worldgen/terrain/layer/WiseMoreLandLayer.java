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
 * Remove too much ocean layer 2 electric boogaloo
 */
public enum WiseMoreLandLayer implements DiagonalCrossSamplingLayer {
	INSTANCE;

	@Override
	public int sample(LayerRandomnessSource random, int sw, int se, int nw, int ne, int centre) {
		int c = centre;
		int e1 = ne;
		int e2 = se;
		int e3 = sw;
		int e4 = nw;

		if (c == 0 && 0 == e1 && 0 == e2 && 0 == e3 && 0 == e4 && random.nextInt(3) == 0) {
			return 1;
		}

		return centre;
	}
}
