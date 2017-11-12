package com.namehillsoftware.lazyj;

import java.util.concurrent.Callable;

/**
 * A default implementation of `AbstractSynchronousLazy<T>`
 * @param <T> The type to create
 *
 * Usage:
 *
 * ```java
 * private final Lazy<Object> lazyObject = new Lazy<>(Object::new);
 * ```
 */
public final class Lazy<T> extends AbstractSynchronousLazy<T> {

	private final Callable<T> initialization;

	public Lazy(Callable<T> initialization) {
		this.initialization = initialization;
	}

	@Override
	protected final T create() throws Exception {
		return initialization.call();
	}
}
