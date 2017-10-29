package com.namehillsoftware.lazyj;

public abstract class AbstractSynchronousLazy<T> implements CreateAndHoldObject<T> {

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
				object = initialize();
			} catch (Exception e) {
				exception = new RuntimeException(e);
			}
		}

		if (exception != null)
			throw exception;

		return object;
	}

	protected abstract T initialize() throws Exception;
}
