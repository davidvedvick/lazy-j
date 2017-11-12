package com.namehillsoftware.lazyj;

/**
 * A class that implements this interface guarantees to create an
 * object and hold it.
 *
 * Implementations may have different levels of thread-safety,
 * or levels of synchronization between threads
 * @param <T> The type of object to create
 */
public interface CreateAndHold<T> {
	/**
	 * Check if object of type T is created
	 * @return True if the object is created
	 */
	boolean isCreated();

	/**
	 * Returns the object, creating it first if necessary.
	 * @return An object of type @param <T>
	 */
	T getObject();
}
