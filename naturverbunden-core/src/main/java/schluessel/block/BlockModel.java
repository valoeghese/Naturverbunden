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
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.block.AbstractBlock.OffsetType;
import net.minecraft.util.Identifier;
import schluessel_impl.obj.ImplBlockModel;

/**
 * Class that represents the visual elements of a block.
 */
public interface BlockModel {
	/**
	 * Sets the function to create the blockstate of this block model.
	 * @param blockStateCreator the block state model creator.
	 * @return this
	 */
	BlockModel blockState(StateFunction blockStateCreator);

	/**
	 * Sets the block model creation function of the block model
	 * @param modelCreator the model creation function
	 * @return this
	 */
	BlockModel blockModels(ModelFunction modelCreator);

	/**
	 * Sets the function to provide additional models that aren't created in this block model.
	 * @param additionalModels the additional model creation function
	 * @return this
	 */
	BlockModel additionalModels(AdditionalModelFunction additionalModels);

	/**
	 * Whether the block model should be opaque
	 * @param opaque whether the block model is opaque
	 * @return this
	 */
	BlockModel opaque(boolean opaque);

	/**
	 * Sets the layer on which this block is rendered.
	 * @param layer the layer on which to render the block
	 * @return this
	 */
	BlockModel renderOn(Layer layer);

	/**
	 * Sets the offset type of the block model.
	 * @param type the offset type.
	 * @return this
	 */
	BlockModel offsetType(OffsetType type);

	/**
	 * Creates an immutable block model from the settings.
	 * @return a new block model, immutable, with the properties on the current model.
	 */
	BlockModel immutable();

	// States
	StateFunction SIMPLE_STATE = (id, models) -> JState.state(JState.variant().put("", models[0]));

	StateFunction TALL_CROSS_STATE = (id, models) -> JState.state(JState.variant().put("half=lower", models[0]).put("half=upper", models[1]));

	StateFunction SLAB_STATE = (id, models) -> JState.state(JState.variant()
			.put("type=bottom", models[0])
			.put("type=top", models[1])
			.put("type=double", models[2]));

	// Block Model Functions

	ModelFunction CUBE_ALL_MODEL = ids -> {
		Identifier id = ids.apply("", "block");
		return ImmutableMap.of(id, JModel.model().parent("block/cube_all").textures(JModel.textures().var("all", id.toString())));
	};

	ModelFunction CROSS_MODEL = ids -> {
		Identifier id = ids.apply("", "block");
		return ImmutableMap.of(id, JModel.model().parent("block/cross").textures(JModel.textures().var("cross", id.toString())));
	};

	ModelFunction TALL_CROSS_MODEL = ids -> {
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

	static ModelFunction slabModel(String planksId) {
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

	BlockModel NONE = BlockModel.create().immutable();

	BlockModel SIMPLE_BLOCKSTATE_ONLY = BlockModel.create()
			.blockState(SIMPLE_STATE)
			.immutable();

	BlockModel CUTOUT_SIMPLE_BLOCKSTATE = BlockModel.create()
			.blockState(SIMPLE_STATE)
			.opaque(false)
			.renderOn(Layer.CUTOUT)
			.immutable();

	BlockModel SIMPLE_CUBE_ALL = BlockModel.create()
			.blockState(SIMPLE_STATE)
			.blockModels(CUBE_ALL_MODEL)
			.immutable();

	BlockModel CUTOUT_CUBE_ALL = BlockModel.create()
			.blockState(SIMPLE_STATE)
			.blockModels(CUBE_ALL_MODEL)
			.renderOn(Layer.CUTOUT)
			.opaque(false)
			.immutable();

	BlockModel CROSS = BlockModel.create()
			.blockState(SIMPLE_STATE)
			.blockModels(CROSS_MODEL)
			.opaque(false)
			.renderOn(Layer.CUTOUT)
			.immutable();

	BlockModel TALL_PLANT = BlockModel.create()
			.blockState(TALL_CROSS_STATE)
			.blockModels(TALL_CROSS_MODEL)
			.opaque(false)
			.renderOn(Layer.CUTOUT)
			.immutable();

	static BlockModel slab(String baseId) {
		return BlockModel.create()
				.blockState(SLAB_STATE)
				.blockModels(slabModel(baseId))
				.additionalModels(ids -> Arrays.asList(new JBlockModel(ids.apply("block/" + baseId))));
	}

	// Functions

	@FunctionalInterface
	interface IdentifierFunction extends BiFunction<String, String, Identifier> {
		/**
		 * Retrieves an Identifier with either: the mod id and the "subPath" (if category is null), or: the mod id, the category, the id, and the subpath
		 */
		Identifier apply(String subPath, @Nullable String category);

		default Identifier apply(String path) {
			return this.apply(path, null);
		}
	}

	@FunctionalInterface
	interface StateFunction {
		JState createModel(Identifier id, JBlockModel[] models);
	}

	@FunctionalInterface
	interface ModelFunction {
		Map<Identifier, JModel> createModels(IdentifierFunction ids);
	}

	@FunctionalInterface
	public interface AdditionalModelFunction {
		List<JBlockModel> createModels(IdentifierFunction ids);
	}

	/**
	 * Create a new mutable block model instance.
	 * @return the created instance.
	 */
	static BlockModel create() {
		return new ImplBlockModel();
	}
}
