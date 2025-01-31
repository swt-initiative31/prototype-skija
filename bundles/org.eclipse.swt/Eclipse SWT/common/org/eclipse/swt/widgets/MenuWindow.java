package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class MenuWindow extends Shell implements Listener {

	int width = 100;
	int height = 100;

	Decorations parent;

	public MenuWindow(Decorations c, MenuItem[] items, Point p) {
		super(c.getShell());

		parent = c;

		var pl = c.getShell().getLocation();

		setLocation(new Point(pl.x + p.x, pl.y + p.x + height));
		setSize(width, height);

		display.addFilter(SWT.MouseDown, this);

	}

	@Override
	public void handleEvent(Event event) {

		System.out.println("Handle MouseDown: " + event);
		if (isDisposed()) {
			if (display != null)
				display.removeListener(SWT.MouseDown, this);
			System.out.println("Remove listener");
		}

		if (!isDisposed())
			close();

		if (display != null)
			display.removeFilter(SWT.MouseDown, this);
		parent.getShell().setCurrent();
		parent.getShell().redraw();

	}

	@Override
	public void triggerEvent(Event e) {
		drawMenuSelection(e);
	}

	private void drawMenuSelection(Event e) {

	}

}
