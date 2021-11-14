package schluessel.item;

/**
 * A record representing a creative tab in game.
 */
public record CreativeTab(int id, String name) {
	public static final CreativeTab BUILDING_BLOCKS = new CreativeTab(0, "buildingBlocks");
	public static final CreativeTab DECORATIONS = new CreativeTab(1, "decorations");
	public static final CreativeTab REDSTONE = new CreativeTab(2, "redstone");
	public static final CreativeTab TRANSPORTATION = new CreativeTab(3, "transportation");
	public static final CreativeTab HOTBARS = new CreativeTab(4, "hotbar");
	public static final CreativeTab SEARCH = new CreativeTab(5, "search");
	public static final CreativeTab MISCELLANEOUS = new CreativeTab(6, "misc");
	public static final CreativeTab FOOD = new CreativeTab(7, "food");
	public static final CreativeTab TOOLS = new CreativeTab(8, "tools");
	public static final CreativeTab COMBAT = new CreativeTab(9, "combat");
	public static final CreativeTab BREWING = new CreativeTab(10, "brewing");
	public static final CreativeTab HOTBAR = new CreativeTab(11, "hotbar");

	//public boolean containsItem TODO (delegate#tabContains(ILschluessel/item/ItemType;)Z)
}
