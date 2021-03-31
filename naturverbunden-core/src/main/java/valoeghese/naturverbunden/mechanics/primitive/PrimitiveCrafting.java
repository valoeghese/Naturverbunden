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

package valoeghese.naturverbunden.mechanics.primitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.item.Item;

public class PrimitiveCrafting {
	public static boolean match(List<Item> ingredients) {
		matcher: for (List<Item> recipe : RECIPES.keySet()) {
			List<Item> check = new ArrayList<>(recipe);

			for (Item item : ingredients) {
				if (!check.remove(item)) {
					continue matcher;
				}
			}

			if (check.isEmpty()) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	public static Item get(List<Item> ingredients) {
		matcher: for (Map.Entry<List<Item>, Item> recipe : RECIPES.entrySet()) {
			List<Item> check = new ArrayList<>(recipe.getKey());

			for (Item item : ingredients) {
				if (!check.remove(item)) {
					continue matcher;
				}
			}

			if (check.isEmpty()) {
				return recipe.getValue();
			}
		}

		return null;
	}

	public static void addRecipe(List<Item> ingredients, Item result) {
		RECIPES.put(ingredients, result);
	}

	private static final Map<List<Item>, Item> RECIPES = new HashMap<>();
}
