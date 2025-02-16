package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public interface ICanvas extends IComposite {

	void drawBackground(GC gc, int x, int y, int width, int height);

	Caret getCaret();

	IME getIME();

	void scroll(int destX, int destY, int x, int y, int width, int height, boolean all);

	void setCaret(Caret caret);

	void setIME(IME ime);

	@Override
	Canvas getWrapper();

}