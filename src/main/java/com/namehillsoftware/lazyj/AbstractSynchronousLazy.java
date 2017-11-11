package com.namehillsoftware.lazyj;

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
			} catch (Exception e) {
				exception = new RuntimeException(e);
			}
		}

		if (exception != null)
			throw exception;

		return object;
	}

	protected abstract T create() throws Exception;
}
