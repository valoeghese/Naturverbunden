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

package valoeghese.naturverbunden.worldgen.terrain.type;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

/**
 * Represents a mixed mountain edge sample at a position.
 */
public class MountainEdgeTerrainType extends ParentedTerrainType {
	public MountainEdgeTerrainType(TerrainType mix, TerrainType mountains, double mountainousness, boolean hills) {
		super(mountainousness > 0.2 ? (hills ? DUMMY_WOODED_MOUNTAINS : mountains) : mix);

		this.mix = mix;
		this.mountains = mountains;
		this.strength = mountainousness;
	}

	private final double strength;
	private final TerrainType mix, mountains;

	@Override
	public double getHeight(int x, int z) {
		return this.strength * this.mountains.getHeight(x, z) + (1.0 - this.strength) * this.mix.getHeight(x, z);
	}

	private static final TerrainType DUMMY_WOODED_MOUNTAINS = new FlatTerrainType(BiomeKeys.WOODED_MOUNTAINS, 0, Biome.Category.EXTREME_HILLS);
}

abstract class ParentedTerrainType extends TerrainType {
	protected ParentedTerrainType(TerrainType parent) {
		super(parent.getBiome(), parent.getCategory());
	}
}

