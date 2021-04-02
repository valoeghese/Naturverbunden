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

package valoeghese.naturverbunden.worldgen.terrain.layer.util;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerOperator;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.source.SeedMixer;
import net.minecraft.world.gen.SimpleRandom;

public class CachingLayerContext implements LayerSampleContext<FleißigArea> {
	private final Long2IntLinkedOpenHashMap cache;
	private final int cacheCapacity;
	private final PerlinNoiseSampler noiseSampler;
	private final long worldSeed;
	private long localSeed;

	public CachingLayerContext(int cacheCapacity, long seed, long salt) {
		this.worldSeed = addSalt(seed, salt);
		this.noiseSampler = new PerlinNoiseSampler(new SimpleRandom(seed));
		this.cache = new Long2IntLinkedOpenHashMap(16, 0.25F);
		this.cache.defaultReturnValue(Integer.MIN_VALUE);
		this.cacheCapacity = cacheCapacity;
	}

	public FleißigArea createSampler(LayerOperator layerOperator) {
		return new FleißigArea(this.cache, this.cacheCapacity, layerOperator);
	}

	public FleißigArea createSampler(LayerOperator layerOperator, CachingLayerSampler cachingLayerSampler) {
		return new FleißigArea(this.cache, Math.min(512, cachingLayerSampler.getCapacity() * 4), layerOperator);
	}

	public FleißigArea createSampler(LayerOperator layerOperator, CachingLayerSampler cachingLayerSampler, CachingLayerSampler cachingLayerSampler2) {
		return new FleißigArea(this.cache, Math.min(512, Math.max(cachingLayerSampler.getCapacity(), cachingLayerSampler2.getCapacity()) * 4), layerOperator);
	}

	public void initSeed(long x, long y) {
		long l = this.worldSeed;
		l = SeedMixer.mixSeed(l, x);
		l = SeedMixer.mixSeed(l, y);
		l = SeedMixer.mixSeed(l, x);
		l = SeedMixer.mixSeed(l, y);
		this.localSeed = l;
	}

	public int nextInt(int bound) {
		int i = (int)Math.floorMod(this.localSeed >> 24, (long)bound);
		this.localSeed = SeedMixer.mixSeed(this.localSeed, this.worldSeed);
		return i;
	}

	public PerlinNoiseSampler getNoiseSampler() {
		return this.noiseSampler;
	}

	private static long addSalt(long seed, long salt) {
		long l = SeedMixer.mixSeed(salt, salt);
		l = SeedMixer.mixSeed(l, salt);
		l = SeedMixer.mixSeed(l, salt);
		long m = SeedMixer.mixSeed(seed, l);
		m = SeedMixer.mixSeed(m, l);
		m = SeedMixer.mixSeed(m, l);
		return m;
	}
}
