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

package schluessel.block;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import schluessel.core.SchluesselMod;
import schluessel_impl.SchluesselARRP;

/**
 * @reason collect a lot of things in one place and keep them nice and java.
 */
public class Model {
	public Model() {
	}

	StateFunction state = null;
	ModelFunction blockModel = null;
	Boolean opaque = null; // This can also be determined by the material. If both are set, this wins.
	Layer renderLayer = Layer.DEFAULT;
	AdditionalModelFunction additionalModels;

	public Model blockState(StateFunction blockStateCreator) {
		this.state = blockStateCreator;
		return this;
	}

	public Model blockModels(ModelFunction modelCreator) {
		this.blockModel = modelCreator;
		return this;
	}

	public Model additionalModels(AdditionalModelFunction additionalModels) {
		this.additionalModels = additionalModels;
		return this;
	}

	public Model opaque(boolean opaque) {
		this.opaque = opaque;
		return this;
	}

	public Model renderOn(Layer layer) {
		this.renderLayer = layer;
		return this;
	}

	public Model immutable() {
		return new Immutable(this);
	}

	void createFor(SchluesselMod context, Block block, String id) {
		IdentifierFunction identifierFunction = (subPath, section) -> context.identifierOf(
				(section == null ? "" : section + "/" + id)
				+ (subPath.isEmpty() ? "" : ("_" + subPath)));

		List<JBlockModel> modelLocations;

		if (this.blockModel == null) {
			modelLocations = Lists.newArrayList(new JBlockModel(context.identifierOf("block/" + id)));
		} else {
			// Create the JBlockModels from the entry set, providing a function to get id path things
			Set<Map.Entry<Identifier, JModel>> models = this.blockModel.createModels(identifierFunction).entrySet();

			for (Map.Entry<Identifier, JModel> model : models) {
				SchluesselARRP.RESOURCE_PACK.addModel(model.getValue(), model.getKey());
			}

			modelLocations = models.stream().map(entry -> new JBlockModel(entry.getKey())).collect(Collectors.toList());
		}

		// Add additional model locations
		modelLocations.addAll(this.additionalModels.createModels(identifierFunction));

		if (this.state != null) {
			// "ID Location"
			Identifier idWithModid = context.identifierOf(id);
			SchluesselARRP.RESOURCE_PACK.addBlockState(this.state.createModel(idWithModid, modelLocations.toArray(JBlockModel[]::new)), idWithModid);
		}
	}

	// States
	private static final StateFunction SIMPLE_STATE = (id, models) -> JState.state(JState.variant().put("", models[0]));

	private static final StateFunction TALL_CROSS_STATE = (id, models) -> JState.state(JState.variant().put("half=lower", models[0]).put("half=upper", models[1]));

	private static final StateFunction SLAB_STATE = (id, models) -> JState.state(JState.variant()
			.put("type=bottom", models[0])
			.put("type=top", models[1])
			.put("type=double", models[2]));

	// Block Model Functions
	private static final ModelFunction CUBE_ALL_MODEL = ids -> {
		Identifier id = ids.apply("", "block");
		return ImmutableMap.of(id, JModel.model().parent("block/cube_all").textures(JModel.textures().var("all", id.toString())));
	};

	private static final ModelFunction CROSS_MODEL = ids -> {
		Identifier id = ids.apply("", "block");
		return ImmutableMap.of(id, JModel.model().parent("block/cross").textures(JModel.textures().var("cross", id.toString())));
	};

	private static final ModelFunction TALL_CROSS_MODEL = ids -> {
		Identifier bottomModelId = ids.apply("", "block");
		Identifier topModelId = ids.apply("top", "block");
		return ImmutableMap.of(
				// bottom model
				bottomModelId,
				JModel.model()
						.parent("block/cross")
						.textures(JModel.textures().var("cross", bottomModelId.toString())),
				// top model
				topModelId,
				JModel.model()
						.parent("block/cross")
						.textures(JModel.textures().var("cross", topModelId.toString())));
	};

	private static final ModelFunction slabModel(String planksId) {
		return ids -> {
			Identifier lowerSlabIdentifier = ids.apply("", "block");
			Identifier topSlabIdentifier = ids.apply("top", "block");
			Identifier plankModelIdentifier = ids.apply("block/" + planksId);

			return ImmutableMap.of(
					// bottom model
					lowerSlabIdentifier,
					JModel.model()
							.parent("block/slab")
							.textures(JModel.textures()
									.var("bottom", plankModelIdentifier.toString())
									.var("top", plankModelIdentifier.toString())
									.var("side", plankModelIdentifier.toString())
							),
					// top model
					topSlabIdentifier,
					JModel.model()
							.parent("block/slab_top")
							.textures(JModel.textures()
									.var("bottom", plankModelIdentifier.toString())
									.var("top", plankModelIdentifier.toString())
									.var("side", plankModelIdentifier.toString())
							)
			);
		};
	}

	// Models

	public static final Model NONE = new Model().immutable();

	public static final Model SIMPLE_BLOCKSTATE_ONLY = new Model()
			.blockState(SIMPLE_STATE)
			.immutable();

	public static final Model CUTOUT_SIMPLE_BLOCKSTATE = new Model()
			.blockState(SIMPLE_STATE)
			.opaque(false)
			.renderOn(Layer.CUTOUT_MIPPED)
			.immutable();

	public static final Model SIMPLE_CUBE_ALL = new Model()
			.blockState(SIMPLE_STATE)
			.blockModels(CUBE_ALL_MODEL)
			.immutable();

	public static final Model CUTOUT_CUBE_ALL = new Model()
			.blockState(SIMPLE_STATE)
			.blockModels(CUBE_ALL_MODEL)
			.renderOn(Layer.CUTOUT_MIPPED)
			.opaque(false)
			.immutable();

	public static final Model CROSS = new Model()
			.blockState(SIMPLE_STATE)
			.blockModels(CROSS_MODEL)
			.opaque(false)
			.renderOn(Layer.CUTOUT_MIPPED)
			.immutable();

	public static final Model TALL_PLANT = new Model()
			.blockState(TALL_CROSS_STATE)
			.blockModels(TALL_CROSS_MODEL)
			.opaque(false)
			.renderOn(Layer.CUTOUT_MIPPED)
			.immutable();

	public static final Model slab(String baseId) {
		return new Model()
				.blockState(SLAB_STATE)
				.blockModels(slabModel(baseId))
				.additionalModels(ids -> Arrays.asList(new JBlockModel(ids.apply("block/" + baseId))));
	}

	// Functions

	@FunctionalInterface
	public interface IdentifierFunction extends BiFunction<String, String, Identifier> {
		/**
		 * Retrieves an Identifier with either: the mod id and the "subPath" (if category is null), or: the mod id, the category, the id, and the subpath
		 */
		Identifier apply(String subPath, @Nullable String category);

		default Identifier apply(String path) {
			return this.apply(path, null);
		}
	}

	@FunctionalInterface
	public interface StateFunction {
		JState createModel(Identifier id, JBlockModel[] models);
	}

	@FunctionalInterface
	public interface ModelFunction {
		Map<Identifier, JModel> createModels(IdentifierFunction ids);
	}

	@FunctionalInterface
	public interface AdditionalModelFunction {
		List<JBlockModel> createModels(IdentifierFunction ids);
	}

	// Immutable Model Class

	private static final class Immutable extends Model {
		public Immutable(Model parent) {
			this.opaque = parent.opaque;
			this.state = parent.state;
			this.blockModel = parent.blockModel;
			this.renderLayer = parent.renderLayer;
			this.additionalModels = parent.additionalModels;
		}

		@Override
		public Model opaque(boolean opaque) {
			throw new UnsupportedOperationException("Cannot set opaque property on an immutable model!");
		}

		@Override
		public Model blockModels(ModelFunction modelCreator) {
			throw new UnsupportedOperationException("Cannot set blockModel property on an immutable model!");
		}

		@Override
		public Model blockState(StateFunction blockStateCreator) {
			throw new UnsupportedOperationException("Cannot set blockState property on an immutable model!");
		}

		@Override
		public Model renderOn(Layer layer) {
			throw new UnsupportedOperationException("Cannot set render layer property on an immutable model!");
		}

		@Override
		public Model additionalModels(AdditionalModelFunction additionalModels) {
			throw new UnsupportedOperationException("Cannot set the additional models on an immutable model!");
		}
	}
}
