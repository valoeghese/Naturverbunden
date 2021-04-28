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

package valoeghese.naturverbunden.debug.terrain;

// TODO make a way to expose this to datapack devs when I make terrain types data driven
public class IngameDebugSettings {
	private static final boolean DEBUG_SHAPE = false; // If this is enabled, lots of worldgen shape modifiers will be disabled (some will be reenabled if DEBUG_BIOMES is also on)
	private static final boolean DEBUG_BIOMES = true; // If this is enabled, single biome world except for modifiers such as mountains

	public static final boolean debugShape() {
		return DEBUG_SHAPE;
	}

	public static final boolean debugBiomes() {
		return DEBUG_BIOMES;
	}
}
