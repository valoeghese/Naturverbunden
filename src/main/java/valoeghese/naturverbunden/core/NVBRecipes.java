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

package valoeghese.naturverbunden.core;

import java.util.Arrays;

import net.minecraft.item.Items;
import valoeghese.naturverbunden.mechanics.primitive.PrimitiveCrafting;

public class NVBRecipes {
	public static void initialise() {
		// "Primitive" recipes
		PrimitiveCrafting.addRecipe(Arrays.asList(Items.STICK, Items.STICK, Items.FLINT), Items.DIAMOND);
	}
}
