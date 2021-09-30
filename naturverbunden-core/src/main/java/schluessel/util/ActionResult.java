package schluessel.util;

/**
 * A result for an action or an event handler.
 */
public enum ActionResult {
	/**
	 * Indicates the action was performed, and, in cases where this relates to something like a block being right clicked, an animation should play.
	 */
	SUCCESS,
	/**
	 * Indicates the action was performed and no animation should play. This is identical to SUCCESS in most cases.
	 */
	CONSUME,
	/**
	 * Indicates whether the action was performed should be left to further processing, by other mods, vanilla, or ultimately by some base implementation.
	 */
	PASS,
	/**
	 * Indicates further event processing should be skipped, and whether the action was performed should be left to some base implementation, which may in some cases have still have more logic involved and/or delegate handling to other classes.
	 */
	SKIP,
	/**
	 * Indicates the action should not be performed.
	 */
	FAIL
}
