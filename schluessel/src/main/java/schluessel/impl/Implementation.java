package schluessel.impl;

import schluessel.block.Block;
import schluessel.block.BlockBuilder;
import schluessel.block.BlockMaterial.Builder;
import schluessel.block.BlockModel;
import schluessel.item.ItemSettings;
import schluessel.util.ActionResult;
import schluessel.util.Position;

/**
 * A registerable implementation of Schluessel containing a set of schluessel methods that need to be accessed on the API which require implementations.
 * @author Valoeghese
 */
public abstract class Implementation {
	// *********************************************
	//    B U I L D E R   C O N S T R U C T I O N
	// *********************************************

	/**
	 * Used by the API for providing a block builder in {@linkplain BlockBuilder#create()}
	 * @return a new instance of {@linkplain BlockBuilder}.
	 */
	abstract public BlockBuilder newBlockBuilder();

	abstract public Builder newBlockMaterialBuilder();
	abstract public Builder newBlockMaterialBuilder(Block existing);
	
	abstract public BlockModel newBlockModel();

	abstract public ItemSettings newItemSettings();
	
	// *********************************************
	//          B L O C K   M A T E R I A L
	// *********************************************
	abstract public ActionResult blockBaseOnUse(Block block, Position position);
	
	// *********************************************
	//        I M P L E M E N T A T I O N
	// *********************************************
	
	public static void setImplementation(Implementation impl) {
		implementation = impl;
	}

	public static Implementation delegate() {
		return implementation;
	}
	
	public static Implementation implementation;
}
