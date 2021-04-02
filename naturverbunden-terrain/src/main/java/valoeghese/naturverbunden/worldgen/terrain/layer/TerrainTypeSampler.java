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

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainCategory;

public class TerrainTypeSampler {
	private final CachingLayerSampler sampler;

	public TerrainTypeSampler(LayerFactory<CachingLayerSampler> layerFactory) {
		this.sampler = (CachingLayerSampler)layerFactory.make();
	}

	public TerrainCategory sample(Registry<Biome> biomeRegistry, int x, int z) {
		int i = this.sampler.sample(x, z);
		TerrainCategory[] values = TerrainCategory.values();

		if (i >= values.length) {
			throw new IllegalStateException("Invalid terrain category id emitted by layers: " + i);
		} else {
			return values[i];
		}
	}
}
