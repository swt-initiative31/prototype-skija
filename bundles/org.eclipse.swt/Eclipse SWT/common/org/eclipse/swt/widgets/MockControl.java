package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class MockControl extends Control implements ICustomWidget {

	public MockControl(Composite c, int style) {
		super(c, style);
	}

	@Override
	public void process(Event e) {

		switch (e.type) {
		case SWT.Paint:
			onPaint(e);
			break;
		}

	}

	private void onPaint(Event e) {

		var b = getBounds();

		e.gc.setBackground(getBackground());
		e.gc.fillRectangle(new Rectangle(0, 0, b.width, b.height));

	}

}
