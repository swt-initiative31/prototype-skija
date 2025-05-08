package org.eclipse.test;

import org.eclipse.swt.tests.junit.SwtTestUtil;
import org.eclipse.swt.widgets.Shell;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.function.Supplier;

public class Screenshots {

public static TestWatcher onFailure(Supplier<Shell> o) {
	return null;
}

public static void takeScreenshot(Class swtTestUtilClass, String s) {
}

public static class ScreenshotOnFailure implements org.junit.rules.TestRule {
	public ScreenshotOnFailure(Supplier<Shell> shell) {
	}

	@Override
	public Statement apply(Statement statement, Description description) {
		return null;
	}

	public void dispose() {
	}
}
}
