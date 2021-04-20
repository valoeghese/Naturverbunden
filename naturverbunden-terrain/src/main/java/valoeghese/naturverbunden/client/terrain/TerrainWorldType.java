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

package valoeghese.naturverbunden.client.terrain;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import valoeghese.naturverbunden.worldgen.NVBGeneratorType;
import valoeghese.naturverbunden.worldgen.terrain.TerrainChunkGenerator;

public class TerrainWorldType extends NVBGeneratorType {
	public TerrainWorldType() {
		super("nvb");
	}

	@Override
	protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeReg, Registry<ChunkGeneratorSettings> settingsReg, long seed) {
		return TerrainChunkGenerator.create(biomeReg, settingsReg, seed);
	}
}
