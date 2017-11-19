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

```java
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
```

There's some nice things here; it uses Java's built in synchronized methods to
do a double-checked lock for object initialization. It doesn't have all the
niceties of Microsoft's library (such as different degrees of thread-safety),
but it gets the job done nicely while being simple enough to understand at a
glance.

## Usage

Usage is also fairly simple. To install via gradle add this to your `build.gradle`:

```
dependencies {
	compile 'com.namehillsoftware:lazy-j:0.9.0'
}
```

To instantiate a new object do something like below:

```java
class MyClass {
    .
    .
    .

    public static CreateAndHold<MyCrazySingletonConfig> myCrazySingletonConfig = new AbstractSynchronousLazy<MyCrazySingletonConfig>() {
		@Override
		protected MyCrazySingletonConfig create() {
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
