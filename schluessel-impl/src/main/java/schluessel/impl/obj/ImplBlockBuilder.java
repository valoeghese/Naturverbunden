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

package schluessel.impl.obj;

import java.util.function.Function;

import net.devtech.arrp.json.models.JModel;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.resources.ResourceLocation;
import schluessel.block.Block;
import schluessel.block.BlockBuilder;
import schluessel.block.BlockMaterial;
import schluessel.block.BlockMechanics;
import schluessel.block.BlockModel;
import schluessel.item.ItemSettings;

public class ImplBlockBuilder implements BlockBuilder {
	private ImplBlockModel model = null;
	private ItemSettings itemSettings = new ImplItemSettings();
	private ImplBlockMaterial material;
	private boolean defaultLootTable = true;
	private Function<ResourceLocation, JModel> itemModel = PARENTED;
	private BlockMechanics mechanics = DEFAULT_MECHANICS;

	@Override
	public BlockBuilder material(BlockMaterial material) throws IllegalStateException {
		ImplBlockMaterial impl = (ImplBlockMaterial) material;

		if (impl.material == null || impl.materialColour == null) {
			throw new IllegalStateException("Material and Material colour must both be non-null.");
		}

		this.material = impl;
		return this;
	}

	@Override
	public BlockBuilder model(BlockModel model) {
		this.model = (ImplBlockModel) model;
		return this;
	}

	@Override
	public BlockBuilder itemSettings(ItemSettings settings) {
		this.itemSettings = settings;
		return this;
	}

	@Override
	public BlockBuilder customLootTable() {
		this.defaultLootTable = false;
		return this;
	}

	// TODO make a method for other java loot tables.

	/**
	 * Replace the default item model. Function takes the item id (NOT the model id) and returns the item JModel.
	 */
	@Override
	public BlockBuilder itemModel(Function<ResourceLocation, JModel> modelCreator) {
		this.itemModel = modelCreator;
		return this;
	}

	@Override
	public Block build() {
		if (this.model == null) {
			throw new IllegalStateException("Required Property: Model");
		}

		if (this.material == null) {
			throw new IllegalStateException("Required Property: Material");
		}

		FabricBlockSettings settings = FabricBlockSettings.copyOf(AbstractBlock.Settings.of(material.material, material.materialColour))
				.sounds(material.sounds)
				.luminance(material.luminosity)
				.slipperiness(material.slipperiness)
				.strength(material.hardness, material.resistance)
				.collidable(material.collidable);

		if (this.material.ticksRandomly) {
			settings.ticksRandomly();
		}

		if (this.material.dropsNothing) {
			settings.dropsNothing();
		}

		if (this.material.toolRequired) {
			settings.requiresTool();
		}

		if (this.material.toolType != null) {
			settings.breakByTool(this.material.toolType, this.material.miningLevel);
		}

		// Model gets priority over the material since material can only gain this property by inheriting in a "copy" call
		if (this.model.opaque != null) {
			if (!this.model.opaque) {
				settings.nonOpaque();
			}
		} else if (this.material.opaque != null) {
			if (!this.material.opaque) {
				settings.nonOpaque();
			}
		}

		Block result = (Block) new ImplSchluesselBlock(settings, this.mechanics, this.model,
				this.defaultLootTable, this.material, this.itemModel, this.itemSettings);

		return result;
	}

	private static final Function<ResourceLocation, JModel> PARENTED = id -> JModel.model().parent(new ResourceLocation(id.getNamespace(), "block/" + id.getPath()).toString());
	private static final BlockMechanics DEFAULT_MECHANICS = new BlockMechanics();
}
