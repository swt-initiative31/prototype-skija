package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

import java.util.function.Function;

public abstract class ControlRenderer {

	public abstract Point computeDefaultSize();

	protected abstract void paint(GC gc, int width, int height);

	private final CustomControl control;

	protected ControlRenderer(CustomControl control) {
		this.control = control;
	}

	public final void paint(GC gc) {
		final Point size = control.getSize();
		paint(gc, size.x, size.y);
	}

	protected final boolean isEnabled() {
		return control.isEnabled();
	}

	protected final int getStyle() {
		return control.getStyle();
	}

	protected Point getSize() {
		return control.getSize();
	}

	protected final <T> T measure(Function<GC, T> function) {
		return Drawing.measure(control, function);
	}

	protected final Point getTextExtent(String text, int flags) {
		return Drawing.getTextExtent(control, text, flags);
	}
}
