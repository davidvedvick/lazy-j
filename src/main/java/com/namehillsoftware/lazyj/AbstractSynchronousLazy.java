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

	private final Object syncObject = new Object();

	private boolean isCreated;

	private T object;

	private RuntimeException exception;

	public boolean isCreated() {
		synchronized (syncObject) {
			return isCreated;
		}
	}

	/**
	 * The creation function. This function will only be called once.
	 * If the implementation gives an error, that error will be retained
	 * and rethrown.
	 * @return An object of type @param <T>
	 * @throws Throwable
	 */
	protected abstract T create() throws Throwable;

	public final T getObject() {
		if (isCreated) return getValue();

		synchronized (syncObject) {
			if (isCreated) return getValue();

			try {
				return object = create();
			} catch (RuntimeException e) {
				throw exception = e;
			} catch (Throwable t) {
				throw exception = new RuntimeException(t);
			} finally {
				isCreated = true;
			}
		}
	}

	private T getValue() {
		if (exception == null)
			return object;

		throw exception;
	}
}
