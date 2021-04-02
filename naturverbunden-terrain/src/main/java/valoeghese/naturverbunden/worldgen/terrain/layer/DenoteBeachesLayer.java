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

import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainCategory;

public enum DenoteBeachesLayer implements CrossSamplingLayer {
	INSTANCE;

	@Override
	public int sample(LayerRandomnessSource rand, int n, int e, int s, int w, int centre) {
		int toc = centre >> INFO_BITS; // type of centre
		int ton = n >> INFO_BITS; // ton of bricks
		int toe = e >> INFO_BITS; // my big toe
		int tos = s >> INFO_BITS; // terms of service
		int tow = w >> INFO_BITS; // what happens if you leave your car on private property

		if (toc != 0 || 0 == ton || 0 == toe || 0 == tos || 0 == tow) {
			return (TerrainCategory.EDGE.ordinal() << INFO_BITS) | (centre & INFO_BITS);
		}

		return centre;
	}
	
	// Assuming 4 bits of information after the initial type data
	private static int INFO_BITS = 4;
}
