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

/**
 * Enum for a random block offset for block models.
 */
public enum RandomOffsetType {
	/**
	 * Indicates there is to be no random offset.
	 */
	NONE,
	/**
	 * Indicates the random offset is to be in the horizontal direction only.
	 */
	HORIZONTAL_ONLY,
	/**
	 * Indicates the random offset may be in any and all directions.
	 */
	ALL
}
