package com.namehillsoftware.lazyj;

public interface HoldObject<T> {
	boolean isInitialized();
	T getObject();
}
