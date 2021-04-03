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

package valoeghese.naturverbunden.mixin.terrain;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import valoeghese.naturverbunden.worldgen.terrain.TerrainChunkGenerator;

/**
 * @reason Mess with default generator type.
 */
@Mixin(GeneratorOptions.class)
public abstract class MixinGeneratorOptions {
	@Overwrite
	public static GeneratorOptions getDefaultOptions(Registry<DimensionType> dimReg, Registry<Biome> biomeReg, Registry<ChunkGeneratorSettings> settingsReg) {
		long seed = (new Random()).nextLong();
		return new GeneratorOptions(seed, true, false,
				GeneratorOptions.getRegistryWithReplacedOverworldGenerator(dimReg,
						DimensionType.createDefaultDimensionOptions(dimReg, biomeReg, settingsReg, seed),
						TerrainChunkGenerator.create(biomeReg, settingsReg, seed)));
	}
}
