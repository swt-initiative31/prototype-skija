package org.eclipse.test;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class TracingSuite extends org.junit.runner.Runner {
@Override
public Description getDescription() {
	return null;
}

@Override
public void run(RunNotifier runNotifier) {
}

public @interface TracingOptions {
	int stackDumpTimeoutSeconds();
}
}
