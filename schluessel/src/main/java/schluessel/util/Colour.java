package schluessel.util;

public enum Colour {
	BLACK('0', MapColour.COLOUR_BLACK),
	DARK_BLUE('1', MapColour.COLOUR_BLUE),
	DARK_GREEN('2', MapColour.COLOUR_GREEN),
	DARK_AQUA('3', MapColour.COLOUR_CYAN),
	DARK_RED('4', MapColour.NETHER),
	DARK_PURPLE('5', MapColour.COLOUR_PURPLE),
	GOLD('6', MapColour.COLOUR_ORANGE),
	GRAY('7', MapColour.COLOUR_LIGHT_GRAY),
	DARK_GRAY('8', MapColour.COLOUR_GRAY),
	BLUE('9', MapColour.COLOUR_LIGHT_BLUE),
	GREEN('a', MapColour.COLOUR_LIGHT_GREEN),
	AQUA('b', MapColour.DIAMOND),
	RED('c', MapColour.CRIMSON_NYLIUM),
	LIGHT_PURPLE('d', MapColour.COLOUR_MAGENTA),
	YELLOW('e', MapColour.COLOUR_YELLOW),
	WHITE('f', MapColour.COLOUR_WHITE);

	private Colour(char formatting, MapColour mapColour) {
		this.formatting = formatting;
		this.mapColour = mapColour;
	}

	public MapColour mapColour() {
		return this.mapColour;
	}

	private final char formatting;
	private final MapColour mapColour;

	@Override
	public String toString() {
		return "\u00A7" + this.formatting;
	}
}
