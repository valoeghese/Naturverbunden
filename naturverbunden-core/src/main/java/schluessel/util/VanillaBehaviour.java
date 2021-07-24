package schluessel.util;

@FunctionalInterface
public interface VanillaBehaviour<T> {
	/**
	 * Runs the vanilla behaviour.
	 * @return the result of the vanilla behaviour.
	 */
	T run();
}
