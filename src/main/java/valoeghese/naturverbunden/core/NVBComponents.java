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

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.server.network.ServerPlayerEntity;
import valoeghese.naturverbunden.Naturverbunden;
import valoeghese.naturverbunden.health.PlayerStats;

public class NVBComponents implements EntityComponentInitializer {
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		beBloat(registry);
	}

	private static void beBloat(EntityComponentFactoryRegistry bad) {
		bad.registerFor(ServerPlayerEntity.class, PLAYER_STATS, PlayerStats::new);
	}

	public static PlayerStats getStats(ServerPlayerEntity player) {
		return PLAYER_STATS.get(player);
	}

	public static final ComponentKey<PlayerStats> PLAYER_STATS =
			ComponentRegistry.getOrCreate(Naturverbunden.id("playerstats"), PlayerStats.class);
}
