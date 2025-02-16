package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public interface IScrollable extends IControl {

	Rectangle computeTrim(int x, int y, int width, int height);

	Rectangle getClientArea();

	ScrollBar getHorizontalBar();

	int getScrollbarsMode();

	void setScrollbarsMode(int mode);

	ScrollBar getVerticalBar();

	@Override
	Scrollable getWrapper();

}