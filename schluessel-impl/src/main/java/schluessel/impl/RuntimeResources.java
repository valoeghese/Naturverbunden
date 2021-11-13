package schluessel.impl;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;

/**
 * Schlüssel API's little <i>key</i> to ARRP
 */
public class RuntimeResources {
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("schluessel");

	public static void setup() {
		RRPCallback.BEFORE_VANILLA.register(r->r.add(RESOURCE_PACK));
	}
}
