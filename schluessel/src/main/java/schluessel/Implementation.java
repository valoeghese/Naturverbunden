package schluessel;

import schluessel.block.Block;
import schluessel.block.BlockBuilder;
import schluessel.block.BlockMaterial.Builder;
import schluessel.block.BlockModel;
import schluessel.item.ItemSettings;

/**
 * A registerable implementation of Schluessel.
 * @author Valoeghese
 */
public abstract class Implementation {
	public static Implementation INSTANCE;

	protected Implementation() {
		INSTANCE = this;
	}

	/**
	 * Used by the API for providing a block builder in {@linkplain BlockBuilder#create()}
	 * @return a new instance of {@linkplain BlockBuilder}.
	 */
	abstract public BlockBuilder newBlockBuilder();

	abstract public Builder newBlockMaterialBuilder();
	abstract public Builder newBlockMaterialBuilder(Block existing);
	
	abstract public BlockModel newBlockModel();

	abstract public ItemSettings newItemSettings();
}
