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
	 * Indicates whether the action should be left to further processing, ultimately by some base implementation.
	 */
	PASS,
	/**
	 * Indicates the action should not be performed.
	 */
	FAIL
}
