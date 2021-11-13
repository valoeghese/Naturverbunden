package schluessel.impl.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import schluessel.block.Layer;
import schluessel.impl.ClientConversions;
import schluessel.impl.obj.ImplSchluesselBlock;

public class ImplementedClientConversions extends ClientConversions {
	@Override
	public void setRenderLayer(ImplSchluesselBlock block, Layer layer) {
		BlockRenderLayerMap.INSTANCE.putBlock(block, getVanillaRenderLayer(layer));
	}

	/**
	 * @return the vanilla version of the given render layer.
	 */
	private static RenderType getVanillaRenderLayer(Layer layer) {
		return switch (layer) {
		case CUTOUT -> RenderType.cutoutMipped();
		case TRANSLUCENT -> RenderType.translucent();
		default -> RenderType.solid();
		};
	}
}
