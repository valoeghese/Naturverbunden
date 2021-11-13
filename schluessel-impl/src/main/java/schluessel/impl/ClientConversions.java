package schluessel.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import schluessel.block.Layer;
import schluessel.impl.client.ImplementedClientConversions;
import schluessel.impl.obj.ImplSchluesselBlock;

public class ClientConversions {
	/**
	 * The client conversions instance, based on the side. Empty functions on server, actual code on client. 
	 */
	public static final ClientConversions INSTANCE = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? new ImplementedClientConversions() : new ClientConversions();

	public void setRenderLayer(ImplSchluesselBlock block, Layer layer) {
		// OVERRIDE-ME
	}
}
