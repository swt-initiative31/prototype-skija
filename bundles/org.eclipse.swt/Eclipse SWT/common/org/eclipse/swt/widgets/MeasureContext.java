package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
final class MeasureContext implements IMeasureContext {
	private final Control control;

	private GC gc;

	public MeasureContext(Control control) {
		this.control = control;
	}

	@Override
	public Point textExtent(String text, int drawFlags) {
		if (gc == null) {
			gc = GCFactory.createGraphicsContext(control);
			gc.setFont(control.getFont());
		}

		return gc.textExtent(text, drawFlags);
	}

	public void dispose() {
		if (gc != null) {
			gc.dispose();
			gc = null;
		}
	}
}
