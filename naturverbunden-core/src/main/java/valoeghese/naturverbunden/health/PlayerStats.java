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

package valoeghese.naturverbunden.health;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerStats implements Component {
	public PlayerStats(ServerPlayerEntity player) {
		this.parent = player;
	}

	private float healpoints = 8.0f;
	private long sprintTimeTarget = 0;
	private long attackTimeTarget = 0;
	private boolean sprintUnlock = true;
	private final ServerPlayerEntity parent;

	public ServerPlayerEntity getParent() {
		return this.parent;
	}

	// Checks

	public boolean allowNaturalHeal(long tickTime) {
		return this.sprintUnlock && this.healpoints > 0.0f && tickTime > this.sprintTimeTarget && tickTime > this.attackTimeTarget;
	}

	// Updating Stuff

	public float expendHealPoints(float heal) {
		float result = Math.min(this.healpoints, heal);
		this.healpoints -= result;
		return result;
	}

	public void setUnlockVonSprint(long tickTime, boolean unlock) {
		if (this.sprintUnlock != unlock) {
			if (this.sprintUnlock = unlock) {
				this.sprintTimeTarget = tickTime + 600; // 20ticks * 30seconds
			}
		}
	}

	public void attackedAt(long tickTime) {
		this.attackTimeTarget = tickTime + 600;
	}

	public void resetHealPoints() {
		this.healpoints = 8.0f;
	}

	// Overrides

	@Override
	public void readFromNbt(NbtCompound tag) {
		try {
			this.healpoints = tag.getFloat("healpoints");
			this.sprintUnlock = tag.getBoolean("sprintUnlock");
			this.sprintTimeTarget = tag.getLong("sprintTimeTarget");
			this.attackTimeTarget = tag.getLong("attackTimeTarget");
		} catch (Exception e) {
			System.err.println("The following error may or may not be a problem. If this occurs updating a world to newer versions of the mod, it's probably fine.");
			e.printStackTrace();
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putFloat("healpoints", this.healpoints);
		tag.putBoolean("sprintUnlock", this.sprintUnlock);
		tag.putLong("attackTimeTarget", this.attackTimeTarget);
		tag.putLong("sprintTimeTarget", this.sprintTimeTarget);
	}
}