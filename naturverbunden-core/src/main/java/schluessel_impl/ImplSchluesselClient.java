package schluessel_impl;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import schluessel.block.Layer;
import schluessel_impl.obj.ImplSchluesselBlock;

public class ImplSchluesselClient {
	public static void setRenderLayer(ImplSchluesselBlock block, Layer layer) {
		BlockRenderLayerMap.INSTANCE.putBlock(block, getVanillaRenderLayer(layer));
	}

	/**
	 * @return the vanilla version of the given render layer.
	 */
	private static RenderLayer getVanillaRenderLayer(Layer layer) {
		return switch (layer) {
			case CUTOUT -> RenderLayer.getCutoutMipped();
			case TRANSLUCENT -> RenderLayer.getTranslucent();
			default -> RenderLayer.getSolid();
		};
	}
}
