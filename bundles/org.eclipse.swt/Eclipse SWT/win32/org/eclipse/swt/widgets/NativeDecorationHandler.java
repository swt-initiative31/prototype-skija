package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.win32.*;

public class NativeDecorationHandler {

	public static void setMinimized(Control c, boolean minimized) {

		c.checkWidget();
		Display.lpStartupInfo = null;
		_setMinimized(c, minimized);

	}

	private static void _setMinimized(Control c, boolean minimized) {

		int swFlags = minimized ? OS.SW_SHOWMINNOACTIVE : OS.SW_RESTORE;
		if (!OS.IsWindowVisible(c.handle))
			return;
		if (minimized == OS.IsIconic(c.handle))
			return;
		int flags = swFlags;
		if (flags == OS.SW_SHOWMINNOACTIVE && c.handle == OS.GetActiveWindow()) {
			flags = OS.SW_MINIMIZE;
		}
		OS.ShowWindow(c.handle, flags);
		OS.UpdateWindow(c.handle);

	}

}
