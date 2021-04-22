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

import java.util.function.Consumer;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.BlockPlacerType;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import valoeghese.naturverbunden.Naturverbunden;

public class NVBWorldgenUtils {
	protected static <P extends BlockPlacer> BlockPlacerType<P> register(String id, Codec<P> codec) {
		return Registry.register(Registry.BLOCK_PLACER_TYPE, Naturverbunden.id(id), new BlockPlacerType<>(codec));
	}

	protected static Biome register(RegistryKey<Biome> id, Biome biome) {
		return Registry.register(BuiltinRegistries.BIOME, id.getValue(), biome);
	}

	protected static RegistryKey<ConfiguredFeature<?, ?>> key(String name) {
		return RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, Naturverbunden.id(name));
	}

	protected static SpawnSettings spawnSettings(Consumer<SpawnSettings.Builder> mobs, boolean players) {
		SpawnSettings.Builder builder = new SpawnSettings.Builder();
		mobs.accept(builder);
		
		if (players) {
			builder.playerSpawnFriendly();
		}

		return builder.build();
	}

	protected static GenerationSettings generationSettings(Consumer<GenerationSettings.Builder> gen) {
		GenerationSettings.Builder builder = new GenerationSettings.Builder();
		gen.accept(builder);
		return builder.build();
	}

	protected static RegistryKey<Biome> bkey(String name) {
		return RegistryKey.of(Registry.BIOME_KEY, Naturverbunden.id(name));
	}

	protected static int getSkyColor(float temperature) {
		float f = temperature / 3.0F;
		f = MathHelper.clamp(f, -1.0F, 1.0F);
		return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
	}

	protected static ConfiguredFeature<?, ?> createFrequentPatch(RegistryKey<ConfiguredFeature<?, ?>> key, int frequency, BlockState state, BlockPlacer placer) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.getValue(), Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(state), placer).tries(32).cannotProject().build())
				.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(frequency));
	}
}
