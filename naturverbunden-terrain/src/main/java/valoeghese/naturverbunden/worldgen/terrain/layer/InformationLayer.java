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

import net.minecraft.world.biome.layer.type.IdentitySamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum InformationLayer implements IdentitySamplingLayer {
	ONE_BIT(1),
	TWO_BIT(2);

	private InformationLayer(int bits) {
		this.bits = bits;
	}

	private final int bits;

	@Override
	public int sample(LayerRandomnessSource context, int value) {
		value <<= this.bits; // shift the existing data over to make space
		value |= context.nextInt(1 + (0b11 >> (2 - this.bits))); // Add relevant bits pseudorandomly in the new space
		return value; // return the resultant value containing the old and new information
	}
}
