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

import schluessel.block.Block;
import schluessel.block.event.BlockActivateEvent;
import schluessel.registry.RegistryEvent;

/**
 * Specifies an event listener.
 */
public interface EventListener {
	/**
	 * Block registry event listener.
	 * @param event the event.
	 */
	default void onBlockRegister(RegistryEvent<Block> event) {
	}

	/**
	 * Block right click event listener.
	 * @param event the event.
	 */
	default void onBlockActivate(BlockActivateEvent event) {
	}
}
