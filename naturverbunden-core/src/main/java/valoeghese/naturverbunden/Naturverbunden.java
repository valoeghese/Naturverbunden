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

package valoeghese.naturverbunden;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import valoeghese.naturverbunden.common.primitive.PrimitiveContent;
import valoeghese.naturverbunden.core.NVBRecipes;
import valoeghese.naturverbunden.worldgen.primitive.PrimitiveWorldgen;

public class Naturverbunden implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Naturverbunden");

	@Override
	public void onInitialize() {
		PrimitiveContent.forceRegister();
		NVBRecipes.initialise();
		PrimitiveWorldgen.forceRegister();
		PrimitiveWorldgen.initialiseWorldGen();
	}

	public static Identifier id(String id) {
		return new Identifier("nvb", id);
	}
}
