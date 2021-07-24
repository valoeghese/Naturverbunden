package schluessel_impl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockPos;
import schluessel.util.Position;

public class ImplSchluesselFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		ImplSchluesselARRP.setup();

		// TODO deep scan mod jars? or specify mod.properties. If deep scan, then mod workspace stuff can be done via scanning the sources and loading thus. Need to use separate classloader for that as well to not screw up sh1t. Wait... isn't there a method to load a class without initialiseing it
	}

	// TODO should Position be bound to these classes instead? i.e. use an interface instead of a record, and bind it to BlockPos, Vec3d, etc
	public static Position blockposToPosition(BlockPos pos) {
		return new Position(pos.getX(), pos.getY(), pos.getZ());
	}
}
