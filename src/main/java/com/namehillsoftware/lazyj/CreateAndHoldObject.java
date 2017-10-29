package com.namehillsoftware.lazyj;

public interface CreateAndHoldObject<T> {
	boolean isCreated();
	T getObject();
}
