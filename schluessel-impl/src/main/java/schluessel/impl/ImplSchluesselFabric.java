package schluessel.impl;

import net.fabricmc.api.ModInitializer;
import schluessel.block.Block;
import schluessel.block.BlockBuilder;
import schluessel.block.BlockMaterial.Builder;
import schluessel.block.BlockModel;
import schluessel.item.ItemSettings;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Builder newBlockMaterialBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Builder newBlockMaterialBuilder(Block existing) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockModel newBlockModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemSettings newItemSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blockBaseOnUse(Block block, Position position) {
		// TODO Auto-generated method stub
		
	}
}
