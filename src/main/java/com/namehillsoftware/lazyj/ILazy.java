package com.namehillsoftware.lazyj;

public interface ILazy<T> {
	boolean isInitialized();
	T getObject();
}
