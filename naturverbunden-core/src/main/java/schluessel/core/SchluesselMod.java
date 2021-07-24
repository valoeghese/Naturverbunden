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

package schluessel.core;

import net.minecraft.util.Identifier;

/**
 * Interface which specifies a schluessel mod.
 */
public interface SchluesselMod {
	/**
	 * @return the mod id of the mod.
	 */
	String getModid();
	/**
	 * Runs when the game starts.
	 */
	void onGameStart();

	/**
	 * Used by the implementation. Gives an Identifier which specifies the modid and given id.
	 */
	default Identifier identifierOf(String id) {
		return new Identifier(getModid(), id);
	}
}
