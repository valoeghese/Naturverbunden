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

package valoeghese.naturverbunden.block.primitive;

import java.util.function.IntFunction;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;

public class CombustibleGasBlock extends GasBlock {
	public CombustibleGasBlock(Settings settings, boolean rises, IntFunction<StatusEffectInstance> effect) {
		super(settings, rises, effect);
		FlammableBlockRegistry.getDefaultInstance().add(this, new FlammableBlockRegistry.Entry(2, 1));
	}

}
