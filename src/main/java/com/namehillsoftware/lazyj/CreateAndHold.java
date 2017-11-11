package com.namehillsoftware.lazyj;

public interface CreateAndHold<T> {
	boolean isCreated();
	T getObject();
}
