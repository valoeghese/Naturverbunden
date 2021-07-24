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

package schluessel_impl.obj;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.block.AbstractBlock.OffsetType;
import net.minecraft.util.Identifier;
import schluessel.block.BlockModel;
import schluessel.block.Layer;
import schluessel.core.SchluesselMod;
import schluessel_impl.ImplSchluesselARRP;

public class ImplBlockModel implements BlockModel {
	public ImplBlockModel() {
	}

	StateFunction state = null;
	ModelFunction blockModel = null;
	Boolean opaque = null; // This can also be determined by the material. If both are set, this wins.
	Layer renderLayer = Layer.DEFAULT;
	AdditionalModelFunction additionalModels;
	OffsetType offsetType;

	@Override
	public BlockModel blockState(StateFunction blockStateCreator) {
		this.state = blockStateCreator;
		return this;
	}

	@Override
	public BlockModel blockModels(ModelFunction modelCreator) {
		this.blockModel = modelCreator;
		return this;
	}

	@Override
	public BlockModel additionalModels(AdditionalModelFunction additionalModels) {
		this.additionalModels = additionalModels;
		return this;
	}

	@Override
	public BlockModel opaque(boolean opaque) {
		this.opaque = opaque;
		return this;
	}

	@Override
	public BlockModel renderOn(Layer layer) {
		this.renderLayer = layer;
		return this;
	}

	@Override
	public BlockModel offsetType(OffsetType type) {
		this.offsetType = type;
		return this;
	}

	@Override
	public BlockModel immutable() {
		return new Immutable(this);
	}

	public OffsetType getOffsetType() {
		return this.offsetType;
	}

	public Layer getRenderLayer() {
		return this.renderLayer;
	}

	public void createModelFor(SchluesselMod context, ImplSchluesselBlock block, String id) {
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
				ImplSchluesselARRP.RESOURCE_PACK.addModel(model.getValue(), model.getKey());
			}

			modelLocations = models.stream().map(entry -> new JBlockModel(entry.getKey())).collect(Collectors.toList());
		}

		// Add additional model locations
		modelLocations.addAll(this.additionalModels.createModels(identifierFunction));

		if (this.state != null) {
			// "ID Location"
			Identifier idWithModid = context.identifierOf(id);
			ImplSchluesselARRP.RESOURCE_PACK.addBlockState(this.state.createModel(idWithModid, modelLocations.toArray(JBlockModel[]::new)), idWithModid);
		}
	}

	// Immutable Model Class

	private static final class Immutable extends ImplBlockModel {
		public Immutable(ImplBlockModel parent) {
			this.opaque = parent.opaque;
			this.state = parent.state;
			this.blockModel = parent.blockModel;
			this.renderLayer = parent.renderLayer;
			this.additionalModels = parent.additionalModels;
			this.offsetType = parent.offsetType;
		}

		@Override
		public BlockModel opaque(boolean opaque) {
			throw new UnsupportedOperationException("Cannot set opaque property on an immutable model!");
		}

		@Override
		public BlockModel blockModels(ModelFunction modelCreator) {
			throw new UnsupportedOperationException("Cannot set blockModel property on an immutable model!");
		}

		@Override
		public BlockModel blockState(StateFunction blockStateCreator) {
			throw new UnsupportedOperationException("Cannot set blockState property on an immutable model!");
		}

		@Override
		public BlockModel renderOn(Layer layer) {
			throw new UnsupportedOperationException("Cannot set render layer property on an immutable model!");
		}

		@Override
		public BlockModel additionalModels(AdditionalModelFunction additionalModels) {
			throw new UnsupportedOperationException("Cannot set the additional models on an immutable model!");
		}

		@Override
		public BlockModel offsetType(OffsetType type) {
			throw new UnsupportedOperationException("Cannot set the offset type on an immutable model!");
		}
	}
}
