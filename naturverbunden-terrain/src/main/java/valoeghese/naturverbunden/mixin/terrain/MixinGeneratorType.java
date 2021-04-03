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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import valoeghese.naturverbunden.worldgen.terrain.TerrainBiomeProvider;
import valoeghese.naturverbunden.worldgen.terrain.TerrainChunkGenerator;

/**
 * @reason Mess with default generator type.
 */
@Mixin(GeneratorType.class)
public abstract class MixinGeneratorType {
	@Shadow
	protected abstract ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed);

	@Redirect(method = "createDefaultOptions",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/GeneratorType;getChunkGenerator(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/registry/Registry;J)Lnet/minecraft/world/gen/chunk/ChunkGenerator;"))
	private ChunkGenerator redirectGetChunkGenerator(GeneratorType type, Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> settingsRegistry, long seed) {
		if (type == GeneratorType.DEFAULT ) {
			return new TerrainChunkGenerator(new TerrainBiomeProvider(biomeRegistry, seed), seed, () -> {
				return settingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD);
			});
		} else {
			return this.getChunkGenerator(biomeRegistry, settingsRegistry, seed);
		}
	}
}