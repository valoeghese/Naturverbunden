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

import net.minecraft.world.biome.layer.util.LayerFactory;
import valoeghese.naturverbunden.worldgen.terrain.layer.util.FleiﬂigArea;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainCategory;

public class TerrainInfoSampler {
	private final FleiﬂigArea sampler;

	public TerrainInfoSampler(LayerFactory<FleiﬂigArea> layerFactory) {
		this.sampler = layerFactory.make();
	}

	public Info sample(int x, int z) {
		int sample = this.sampler.sample(x, z);
		int category = sample >> DenoteBeachesLayer.INFO_BITS;
		TerrainCategory[] values = TerrainCategory.values();

		if (category >= values.length) {
			throw new IllegalStateException("Invalid terrain category id emitted by layers: " + category);
		} else {
			return new Info(values[category], sample & DenoteBeachesLayer.INFO_MASK);
		}
	}

	public static class Info {
		public Info(TerrainCategory category, int info) {
			this.category = category;
			this.info = info;
		}

		public final TerrainCategory category;
		public final int info;
	}
}
