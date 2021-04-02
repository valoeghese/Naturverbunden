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

import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.layer.type.MergingLayer;
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.minecraft.world.biome.layer.util.LayerSampler;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainCategory;

public enum MergeRiversLayer implements MergingLayer, IdentityCoordinateTransformer {
	INSTANCE;

	@Override
	public int sample(LayerRandomnessSource context, LayerSampler info, LayerSampler rivers, int x, int z) {
		int value = info.sample(x, z);
		int river = rivers.sample(x, z);
		int type = value >> DenoteBeachesLayer.INFO_BITS;

		if (type > 0) { // TerrainCategory.OCEAN.ordinal()
			if (BuiltinBiomes.fromRawId(river) == BiomeKeys.RIVER) {
				return DenoteBeachesLayer.switchCategory(TerrainCategory.RIVER, value);
			}
		}

		return value;
	}
}
