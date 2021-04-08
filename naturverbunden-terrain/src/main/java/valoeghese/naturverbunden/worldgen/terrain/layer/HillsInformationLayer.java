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

public enum HillsInformationLayer implements CrossSamplingLayer {
	INSTANCE;

	@Override
	public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int centre) {
		int similarity = 0;
		int value = centre << 1;

		if (n == centre) {
			similarity++;
		}

		if (e == centre) {
			similarity++;
		}

		if (s == centre) {
			similarity++;
		}

		if (w == centre) {
			similarity++;
		}
		
		if (similarity >= 3 && context.nextInt(3) == 0) {
			value |= 1;
		}
		
		return value;
	}
}
