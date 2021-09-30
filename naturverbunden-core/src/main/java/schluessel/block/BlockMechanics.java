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

package schluessel.block;

import schluessel.util.ActionResult;
import schluessel.util.Position;

/**
 * Class that handles default block mechanics.
 */
public class BlockMechanics {
	/**
	 * Called when the block is right clicked by a player.
	 * @param position the position of the block being right clicked.
	 * @param vanilla the vanilla behaviour.
	 * @return the result of the action
	 */
	public ActionResult onUse(Position position) {
		return ActionResult.PASS;
	}
}
