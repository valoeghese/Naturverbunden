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

package valoeghese.naturverbunden.worldgen.primitive;

import java.util.Random;

import net.minecraft.world.biome.BiomeKeys;
import valoeghese.naturverbunden.util.terrain.Noise;
import valoeghese.naturverbunden.util.terrain.OpenSimplexGenerator;
import valoeghese.naturverbunden.util.terrain.RidgedSimplexGenerator;
import valoeghese.naturverbunden.util.terrain.Voronoi;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider.Properties;
import valoeghese.naturverbunden.worldgen.terrain.biome.TerrainBiomeProvider.TerrainTypeModifier;
import valoeghese.naturverbunden.worldgen.terrain.layer.TerrainInfoSampler.Info;
import valoeghese.naturverbunden.worldgen.terrain.type.EdgeTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.MountainsTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.MultiNoiseTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.SimpleSimplexTerrainType;
import valoeghese.naturverbunden.worldgen.terrain.type.TerrainType;

public class CalderaRidgeModifier implements TerrainTypeModifier {
	CalderaRidgeModifier(long seed) {
		this.seed = Voronoi.seedFromLong(seed - 10);

		Random rand = new Random(seed);
		this.terrainHotSpringsRidge = new MultiNoiseTerrainType(BiomeKeys.MOUNTAINS, 130.0)
				.addNoise(new Noise(rand, 2, RidgedSimplexGenerator::new), MountainsTerrainType.FREQUENCY * 0.5, 25.0);

		this.terrainHotSprings = new HotspringsTerrainType(rand);
	}
	
	private final int seed;
	private final TerrainType terrainHotSpringsRidge;
	private final TerrainType terrainHotSprings;

	@Override
	public TerrainType apply(TerrainType original, TerrainBiomeProvider source, int x, int z, Properties properties, Info info) {
		double voronoi = Voronoi.sampleD1Worley(x * FREQUENCY, z * FREQUENCY, this.seed);

		if (voronoi < 0.1) {
			return this.terrainHotSprings;
		} else if (voronoi < 0.15) {
			voronoi = (1.0 / 0.05) * (voronoi - 0.1);
			return new EdgeTerrainType(this.terrainHotSpringsRidge, original, voronoi, voronoi > 0.8 ? original : this.terrainHotSpringsRidge);
		} else {
			return original;
		}
	}

	private static final double FREQUENCY = 1 / 4200.0;
	
	private static class HotspringsTerrainType extends SimpleSimplexTerrainType {
		HotspringsTerrainType(Random rand) {
			super(PrimitiveWorldgen.HOT_SPRINGS_KEY, rand, 2, 106.0, 1.0 / 100.0, 6.0);
			this.lakes = new OpenSimplexGenerator(rand);
		}

		private final OpenSimplexGenerator lakes;

		@Override
		public double getHeight(int x, int z) {
			double height = super.getHeight(x, z);
			double lakeNoise = lakes.sample(x * LAKES_FREQUENCY, z * LAKES_FREQUENCY);

			if (lakeNoise > 0.3) {
				lakeNoise = (lakeNoise - 0.3) * (1.0 / (0.866 - 0.3));
				height -= lakeNoise * 12.0;
			}

			return height;
		}

		private static final double LAKES_FREQUENCY = 1.0 / 60.0;
	}
}
