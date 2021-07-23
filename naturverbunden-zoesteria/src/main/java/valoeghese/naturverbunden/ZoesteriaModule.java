package valoeghese.naturverbunden;

import net.fabricmc.api.ModInitializer;
import valoeghese.naturverbunden.block.zoesteria.ZoesteriaBlocks;
import valoeghese.naturverbunden.worldgen.zoesteria.ZoesteriaWorldgen;

public class ZoesteriaModule implements ModInitializer {

	@Override
	public void onInitialize() {
		Naturverbunden.LOGGER.info("[Primitive] Initializing");
		ZoesteriaBlocks.forceRegister();
		ZoesteriaWorldgen.initialiseWorldGen();
	}

}
