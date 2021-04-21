package valoeghese.naturverbunden;

import net.fabricmc.api.ModInitializer;
import valoeghese.naturverbunden.block.primitive.PrimitiveBlocks;
import valoeghese.naturverbunden.mechanics.primitive.GasMechanics;
import valoeghese.naturverbunden.mechanics.primitive.PrimitiveCrafting;
import valoeghese.naturverbunden.worldgen.primitive.PrimitiveWorldgen;

public class PrimitiveModule implements ModInitializer {
	@Override
	public void onInitialize() {
		Naturverbunden.LOGGER.info("[Primitive] Initializing");
		PrimitiveBlocks.forceRegister();
		PrimitiveCrafting.addDefaultRecipes();
		PrimitiveWorldgen.forceRegister();
		PrimitiveWorldgen.initialiseWorldGen();
		GasMechanics.initialise();
	}
}
