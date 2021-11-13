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
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import schluessel.block.BlockMaterial;
import schluessel.block.BlockMechanics;
import schluessel.block.BlockModel;
import schluessel.impl.CommonConversions;
import schluessel.impl.ImplSchluesselFabric;
import schluessel.item.ItemSettings;
import schluessel.util.ActionResult;

public class ImplSchluesselBlock extends Block {
	public ImplSchluesselBlock(BlockBehaviour.Properties settings, BlockMechanics mechanics, BlockModel model,
			boolean dlt, BlockMaterial material, Function<ResourceLocation, JModel> itemModel, ItemSettings itemSettings) {
		super(settings);
		this.mechanics = mechanics;
		this.model = model;
		this.defaultLootTable = dlt;
		this.itemModel = itemModel;
		this.material = material;
		this.itemSettings = itemSettings;
		
		this.vanillaOffsetType = switch(((ImplBlockModel)this.model).getRandomOffsetType()) {
		    case ALL			-> OffsetType.XYZ;
		    case HORIZONTAL_ONLY-> OffsetType.XZ;
		    case NONE			-> OffsetType.NONE;
		};
	}

	private final BlockMechanics mechanics;
	private final BlockMaterial material;
	private final BlockModel model;
	private final boolean defaultLootTable;
	private final Function<ResourceLocation, JModel> itemModel;
	private final ItemSettings itemSettings;
	private final OffsetType vanillaOffsetType;

	public BlockModel getModel() {
		return this.model;
	}
	
	public boolean defaultLootTable() {
		return this.defaultLootTable;
	}
	
	public JModel computeItemModel(ResourceLocation id) {
		return this.itemModel.apply(id);
	}

	public BlockMaterial getMaterial() {
		return this.material;
	}
	
	public ItemSettings getItemSettings() {
		return this.itemSettings;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		// if there is ever more logic added to vanilla here, PASS will need to be differentiated to SKIP where SKIP just returns immediately whereas PASS runs vanilla
		return ImplSchluesselFabric.convertAction(this.mechanics.onUse(CommonConversions.blockposToPosition(pos)));
	}

	@Override
	public OffsetType getOffsetType() {
		return this.vanillaOffsetType;
	}

//	@Override
//	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
//		this.mechanics.onCollision((schluessel.block.Block) this, state, world, pos, entity);
//	}

//	@Override
//	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
//		this.mechanics.onNeighbourUpdate((schluessel.block.Block) this, state, world, pos, block, fromPos, notify);
//	}
}