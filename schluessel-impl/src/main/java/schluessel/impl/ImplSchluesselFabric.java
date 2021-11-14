package schluessel.impl;

import net.fabricmc.api.ModInitializer;
import schluessel.block.Block;
import schluessel.block.BlockBuilder;
import schluessel.block.BlockMaterial.Builder;
import schluessel.block.BlockModel;
import schluessel.impl.obj.ImplBlockBuilder;
import schluessel.impl.obj.ImplBlockMaterial;
import schluessel.impl.obj.ImplBlockModel;
import schluessel.impl.obj.ImplItemSettings;
import schluessel.item.ItemSettings;
import schluessel.util.ActionResult;
import schluessel.util.Position;

public class ImplSchluesselFabric extends Implementation implements ModInitializer {
	public ImplSchluesselFabric() {
	}

	@Override
	public void onInitialize() {
		RuntimeResources.setup();

		// TODO deep scan mod jars? or specify mod.properties. If deep scan, then mod workspace stuff can be done via scanning the sources and loading thus. Need to use separate classloader for that as well to not screw up sh1t. Wait... isn't there a method to load a class without initialiseing it
	}

	@Override
	public BlockBuilder newBlockBuilder() {
		return new ImplBlockBuilder();
	}

	@Override
	public Builder newBlockMaterialBuilder() {
		return new ImplBlockMaterial.Builder();
	}

	@Override
	public Builder newBlockMaterialBuilder(Block existing) {
		return new ImplBlockMaterial.Builder((net.minecraft.world.level.block.Block)existing);
	}

	@Override
	public BlockModel newBlockModel() {
		return new ImplBlockModel();
	}

	@Override
	public ItemSettings newItemSettings() {
		return new ImplItemSettings();
	}

	@Override
	public ActionResult blockBaseOnUse(Block block, Position position) {
		// TODO
		return CommonConversions.convertAction(((net.minecraft.world.level.block.Block) block).use(null, null, null, null, null, null));
	}
}
