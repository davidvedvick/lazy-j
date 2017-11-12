package com.namehillsoftware.lazyj;

/**
 * An implementation of `CreateAndHold` which initializes after a
 * double-checked lock.
 *
 * Example implementation:
 *
 * ```java
 * public static CreateAndHold<MyCrazySingletonConfig> myCrazySingletonConfig = new AbstractSynchronousLazy<MyCrazySingletonConfig>() {
 *     .@Override
 *     protected MyCrazySingletonConfig create() {
 *         final MyCrazySingletonConfig newConfig = .....
 *
 *         return newConfig;
 *     }
 * };
 * ```
 *
 * @param <T> The type to create
 */
public abstract class AbstractSynchronousLazy<T> implements CreateAndHold<T> {

	private T object;

	private RuntimeException exception;

	public boolean isCreated() {
		return object != null || exception != null;
	}

	public final T getObject() {
		return isCreated() ? object : getValueSynchronized();
	}

	private synchronized T getValueSynchronized() {
		if (!isCreated()) {
			try {
				object = create();
			} catch (RuntimeException e) {
				exception = e;
			} catch (Throwable t) {
				exception = new RuntimeException(t);
			}
		}

		if (exception != null)
			throw exception;

		return object;
	}

	/**
	 * The creation function. This function will only be called once.
	 * If the implementation gives an error, that error will be retained
	 * and rethrown.
	 * @return An object of type @param <T>
	 * @throws Throwable
	 */
	protected abstract T create() throws Throwable;
}
