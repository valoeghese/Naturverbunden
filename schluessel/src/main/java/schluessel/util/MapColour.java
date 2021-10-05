package schluessel.util;

public enum MapColour {
	/**
	 * The map colour associated with no colour.
	 */
	NONE(0, 0),
	GRASS(1, 8368696),
	SAND(2, 16247203),
	COLOUR_WHITE(3, 13092807), // WOOL in vanilla.
	FIRE(4, 16711680),
	ICE(5, 10526975),
	METAL(6, 10987431),
	PLANT(7, 31744),
	SNOW(8, 16777215),
	CLAY(9, 10791096),
	DIRT(10, 9923917),
	STONE(11, 7368816),
	WATER(12, 4210943),
	WOOD(13, 9402184),
	QUARTZ(14, 16776437),
	COLOUR_ORANGE(15, 14188339),
	COLOUR_MAGENTA(16, 11685080),
	COLOUR_LIGHT_BLUE(17, 6724056),
	COLOUR_YELLOW(18, 15066419),
	COLOUR_LIGHT_GREEN(19, 8375321),
	COLOUR_PINK(20, 15892389),
	COLOUR_GRAY(21, 5000268),
	COLOUR_LIGHT_GRAY(22, 10066329),
	COLOUR_CYAN(23, 5013401),
	COLOUR_PURPLE(24, 8339378),
	COLOUR_BLUE(25, 3361970),
	COLOUR_BROWN(26, 6704179),
	COLOUR_GREEN(27, 6717235),
	COLOUR_RED(28, 10040115),
	COLOUR_BLACK(29, 1644825),
	GOLD(30, 16445005),
	DIAMOND(31, 6085589),
	LAPIS(32, 4882687),
	EMERALD(33, 55610),
	PODZOL(34, 8476209),
	NETHER(35, 7340544),
	TERRACOTTA_WHITE(36, 13742497),
	TERRACOTTA_ORANGE(37, 10441252),
	TERRACOTTA_MAGENTA(38, 9787244),
	TERRACOTTA_LIGHT_BLUE(39, 7367818),
	TERRACOTTA_YELLOW(40, 12223780),
	TERRACOTTA_LIGHT_GREEN(41, 6780213),
	TERRACOTTA_PINK(42, 10505550),
	TERRACOTTA_GRAY(43, 3746083),
	TERRACOTTA_LIGHT_GRAY(44, 8874850),
	TERRACOTTA_CYAN(45, 5725276),
	TERRACOTTA_PURPLE(46, 8014168),
	TERRACOTTA_BLUE(47, 4996700),
	TERRACOTTA_BROWN(48, 4993571),
	TERRACOTTA_GREEN(49, 5001770),
	TERRACOTTA_RED(50, 9321518),
	TERRACOTTA_BLACK(51, 2430480),
	CRIMSON_NYLIUM(52, 12398641),
	CRIMSON_STEM(53, 9715553),
	CRIMSON_HYPHAE(54, 6035741),
	WARPED_NYLIUM(55, 1474182),
	WARPED_STEM(56, 3837580),
	WARPED_HYPHAE(57, 5647422),
	WARPED_WART_BLOCK(58, 1356933),
	DEEPSLATE(59, 6579300),
	RAW_IRON(60, 14200723),
	GLOW_LICHEN(61, 8365974);

	private MapColour(int id, int colour) {
		this.id = id;
		this.col = colour;
	}

	public final int col;
	public final int id;

	private static final MapColour[] BY_ID = new MapColour[64];

	/**
	 * Gives the Map Colour for the following id.
	 * @param id the numerical id of the map colour.
	 * @return the given map colour if one is associated with the given id. If the numerical id is valid, but has no associated colour value, returns {@linkplain MapColour#NONE}.
	 * @throws ArrayIndexOutOfBoundsException if the id of the map colour is invalid.
	 */
	public static MapColour byId(int id) {
		return BY_ID[id];
	}

	static {
		System.arraycopy(values(), 0, BY_ID, 0, values().length);

		// Fulfil method promise.
		BY_ID[62] = NONE;
		BY_ID[63] = NONE;
		BY_ID[64] = NONE;
	}
}
