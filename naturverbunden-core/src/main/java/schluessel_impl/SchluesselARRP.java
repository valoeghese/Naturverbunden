package schluessel_impl;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;

/**
 * Schl�ssel API's little <i>key</i> to ARRP
 */
public class SchluesselARRP {
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("schluessel");

	public static void setup() {
		RRPCallback.BEFORE_VANILLA.register(r->r.add(RESOURCE_PACK));
	}
}
