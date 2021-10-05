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

import java.util.function.Function;

import net.devtech.arrp.json.models.JModel;
import net.minecraft.resources.ResourceLocation;
import schluessel.Implementation;
import schluessel.item.ItemSettings;

/**
 * A material oriented builder pipeline for the efficient and easily readable construction of blocks.
 */
public interface BlockBuilder {
	/**
	 * Sets the material of the block.
	 * @param material the material to set.
	 * @return this.
	 * @throws IllegalStateException if the material does not specify material and material colour.
	 */
	BlockBuilder material(BlockMaterial material) throws IllegalStateException;

	/**
	 * Sets the model of the block.
	 * @param model the model to set.
	 * @return this.
	 */
	BlockBuilder model(BlockModel model);

	/**
	 * Sets the item settings of the block item.
	 * @param settings the item settings to use.
	 * @return this
	 */
	BlockBuilder itemSettings(ItemSettings settings);
	
	/**
	 * Tells the block builder not to add the default loot table.
	 * @return this
	 */
	BlockBuilder customLootTable();
	
	/**
	 * Replace the default item model. Function takes the item id (NOT the model id) and returns the item JModel.
	 */
	BlockBuilder itemModel(Function<ResourceLocation, JModel> modelCreator);
	
	/**
	 * Creates a block object for the given settings.
	 * @return the constructed block.
	 */
	Block build();

	/**
	 * Constructs a new block builder usage for making a block.
	 * @return the constructed block builder.
	 */
	public static BlockBuilder create() {
		return Implementation.INSTANCE.newBlockBuilder();
	}
}
