package schluessel.core;

import net.minecraft.util.Identifier;

public interface SchluesselMod {
	String getModid();

	default Identifier identifierOf(String id) {
		return new Identifier(getModid(), id);
	}
}
