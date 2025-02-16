package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public interface IComposite extends IScrollable {

	@Deprecated
	void changed(NativeControl[] changed);

	void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY);

	int getBackgroundMode();

	Control[] getChildren();

	Layout getLayout();

	Control[] getTabList();

	boolean getLayoutDeferred();

	boolean isLayoutDeferred();

	void layout();

	void layout(boolean changed);

	void layout(boolean changed, boolean all);

	void layout(Control[] changed);

	void layout(Control[] changed, int flags);

	void setBackgroundMode(int mode);

	void setLayout(Layout layout);

	void setLayoutDeferred(boolean defer);

	void setTabList(Control[] tabList);

	@Override
	Composite getWrapper();

}