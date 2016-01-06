# lazy-j: Lazy Java initialization library

Coming from the C# world, while working on Android projects I've often had the
overwhelming desire to use something similar to the
[Lazy](https://msdn.microsoft.com/en-us/library/dd642331%28v=vs.110%29.aspx)
class that is in the Standard libs for .Net. Using it, you can easily initialize
any object lazily without needing to implement your own double-check locked
lazy initialization code.

So in a hasty moment, I wrote a library called `lazy-j` which supposedly guarantees
your object will lazily be created the first time it is requested, using the
supplied initialization function. It should also be thread-safe. It is also
*EXCEEDINGLY* simple, here's the source:

```
package com.vedsoft.lazyj;

/**
 * Created by david on 11/28/15.
 */
public abstract class Lazy<T> {

	private T object;

	public boolean isInitialized() {
		return object != null;
	}

	public T getObject() {
		return isInitialized() ? object : getValueSynchronized();
	}

	private synchronized T getValueSynchronized() {
		if (!isInitialized())
			object = initialize();

		return object;
	}

	protected abstract T initialize();
}
```

There's some nice things here; it uses Java's built in synchronized methods to
do a double-checked lock for object initialization. It doesn't have all the
niceties of Microsoft's library (such as different degrees of thread-safety),
but it gets the job done nicely while being simple enough to understand at a
glance.

Usage is also fairly simple. To instantiate a new object do something like below:

```
class MyClass {
    .
    .
    .

    public static Lazy<MyCrazySingletonConfig> myCrazySingletonConfig = new Lazy<MyCrazySingletonConfig>() {
        @Override
		protected MyCrazySingletonConfig initialize() {
            final MyCrazySingletonConfig newConfig = .....

            return newConfig;
        }
    };    
}

class SomeOtherClassThatNeedsConfig {

    public void doingThingsWithConfig() {
        final String property = MyClass.myCrazySingletonConfig.getObject().getMyCrazyProperty();
    }
}
```
