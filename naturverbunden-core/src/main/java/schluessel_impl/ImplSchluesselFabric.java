package schluessel_impl;

import net.fabricmc.api.ModInitializer;

public class ImplSchluesselFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		ImplSchluesselARRP.setup();
	}
}
