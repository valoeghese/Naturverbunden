package schluessel_impl;

import net.fabricmc.api.ModInitializer;

public class SchluesselFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		SchluesselARRP.setup();
	}
}
