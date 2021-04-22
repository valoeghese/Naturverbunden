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

public class EdgeTerrainType extends ParentedTerrainType {
	/**
	 * @param bias how much the terrain is biased towards terrainB over terrainA.
	 */
	public EdgeTerrainType(TerrainType terrainA, TerrainType terrainB, double bias, TerrainType parent) {
		super(parent);

		this.terrainA = terrainA;
		this.terrainB = terrainB;
		this.bias = bias;
	}

	private final double bias;
	private final TerrainType terrainA, terrainB;

	@Override
	public double getHeight(int x, int z) {
		return this.bias * this.terrainB.getHeight(x, z) + (1.0 - this.bias) * this.terrainA.getHeight(x, z);
	}
}

abstract class ParentedTerrainType extends TerrainType {
	protected ParentedTerrainType(TerrainType parent) {
		super(parent.getBiome(), parent.getCategory());
	}
}
