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

import javax.annotation.Nullable;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

/**
 * Represents a terrain type.
 */
public abstract class TerrainType {
	protected TerrainType(RegistryKey<Biome> biome, @Nullable Biome.Category category) {
		this(biome, category, 1.0);
	}

	protected TerrainType(RegistryKey<Biome> biome, @Nullable Biome.Category category, double shapeWeight) {
		this.biome = biome;
		this.shapeWeight = shapeWeight;

		if (category == null) {
			Biome builtin = BuiltinRegistries.BIOME.get(biome);

			if (builtin == null) {
				throw new IllegalStateException("Tried to auto-provide category from the builtin registry, as specified by the terrain type. Found no matching entry for " + biome.getValue() + "!");
			}

			this.category = builtin.getCategory();
		} else {
			this.category = category;
		}
	}

	protected RegistryKey<Biome> biome;
	protected Biome.Category category;
	private final double shapeWeight;
	public TerrainType largeHills = this;
	public TerrainType smallHills = this;

	public final RegistryKey<Biome> getBiome() {
		return this.biome;
	}

	public final Biome.Category getCategory() {
		return this.category;
	}

	public final double getShapeWeight() {
		return this.shapeWeight;
	}

	/**
	 * Weight on fading out rivers. 0.0 expresses intent for terrain with rivers, whereas 1.0 expresses intent to have no rivers.
	 */
	public double riverFadeModifier(int x, int z) {
		return 0.0;
	}

	public abstract double getHeight(int x, int z);
}
