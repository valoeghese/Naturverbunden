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

import java.util.function.LongFunction;

import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.SmoothLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import valoeghese.naturverbunden.worldgen.terrain.layer.DenoteBeachesLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.DiagonalShapeEdgeLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.HillsInformationLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.InformationLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.IslandLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.LandOceanLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.NaiveMoreLandLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.ShapeEdgeLayer;
import valoeghese.naturverbunden.worldgen.terrain.layer.TerrainInfoSampler;

public class Layers {
	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long seed, ParentedLayer layer, LayerFactory<T> parent, int count, LongFunction<C> cp) {
		LayerFactory<T> layerFactory = parent;

		for(int i = 0; i < count; ++i) {
			layerFactory = layer.create(cp.apply(seed + (long)i), layerFactory);
		}

		return layerFactory;
	}

	private static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LongFunction<C> cp) {
		// Continental Shape
		// This is mostly random tbh I am just spamming interesting modifications
		LayerFactory<T> layer = LandOceanLayer.INSTANCE.create(cp.apply(1L));
		layer = ScaleLayer.FUZZY.create(cp.apply(2000L), layer);
		layer = ShapeEdgeLayer.INSTANCE.create(cp.apply(2L), layer);
		layer = NaiveMoreLandLayer.INSTANCE.create(cp.apply(50L), layer);
		layer = ScaleLayer.NORMAL.create(cp.apply(1000L), layer);

		layer = ShapeEdgeLayer.INSTANCE.create(cp.apply(3L), layer);
		layer = NaiveMoreLandLayer.INSTANCE.create(cp.apply(51L), layer);
		layer = DiagonalShapeEdgeLayer.INSTANCE.create(cp.apply(4L), layer);

		// Information
		layer = stack(1000L, ScaleLayer.NORMAL, layer, 2, cp);
		layer = InformationLayer.TWO_BIT.create(cp.apply(100L), layer); // 2 bit

		layer = ScaleLayer.NORMAL.create(cp.apply(1000L), layer);
		layer = HillsInformationLayer.INSTANCE.create(cp.apply(101L), layer); // 1 bit
		layer = ScaleLayer.NORMAL.create(cp.apply(1001L), layer);
		layer = InformationLayer.ONE_BIT.create(cp.apply(102L), layer); // 2 bit

		// Scale

		for (int i = 0; i < 5; ++i) {
			layer = ScaleLayer.NORMAL.create(cp.apply(1000L + (long)i), layer);

			if (i == 0) {
				layer = IslandLayer.INSTANCE.create(cp.apply(69L), layer);
			} if (i == 1) {
				layer = DenoteBeachesLayer.LARGE.create(cp.apply(5L), layer);
			} else if (i == 2) {
				layer = DenoteBeachesLayer.SMALL.create(cp.apply(5L), layer);
			}
		}

		layer = SmoothLayer.INSTANCE.create(cp.apply(1000L), layer);

		return layer;
	}

	public static TerrainInfoSampler build(long seed) {
		LayerFactory<FleißigArea> layerFactory = build(salt -> new CachingLayerContext(512, seed, salt));
		return new TerrainInfoSampler(layerFactory);
	}

}
