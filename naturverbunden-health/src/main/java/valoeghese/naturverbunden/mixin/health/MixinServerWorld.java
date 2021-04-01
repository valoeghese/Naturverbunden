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

package valoeghese.naturverbunden.mixin.health;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import valoeghese.naturverbunden.HealthModule;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
	@Inject(at = @At("HEAD"), method = "wakeSleepingPlayers")
	private void onWakeSleepingPlayers(CallbackInfo info) {
		@SuppressWarnings("resource")
		World world = (World) (Object) this;

		if (world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) {
			world.getPlayers().stream().filter(LivingEntity::isSleeping).forEach(player -> {
				if (player.getHealth() < player.getMaxHealth() && player.getHungerManager().getFoodLevel() > 0) {
					player.heal(player.getMaxHealth() / 2);
					player.addExhaustion(30.0f);
					HealthModule.getStats((ServerPlayerEntity) player).resetHealPoints();
				}
			});
		}
	}
}
