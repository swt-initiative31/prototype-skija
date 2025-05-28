package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Thomas Singer
 */
abstract class CompositeCommon extends Scrollable {

abstract Composite findDeferredControl();

private final List<Control> children = new ArrayList<>();

Layout layout;
private CompositeLightWeightChildHandling lightWeightChildHandling;

protected CompositeCommon() {
}

protected CompositeCommon(Composite parent, int style) {
	super(parent, style);
}

protected final List<Control> this_children() {
	return Collections.unmodifiableList(children);
}

protected final boolean isLightWeightChildHandling() {
	return lightWeightChildHandling != null;
}

void addChild(Control control) {
	if (!isLightWeight()) {
		if (control.isLightWeight() && lightWeightChildHandling == null) {
			lightWeightChildHandling = new CompositeLightWeightChildHandling((Composite) this);
		}
	}
	children.add(control);
}

void removeChild(Control control) {
	if (!(control instanceof Shell) && !children.remove(control))
		error(SWT.ERROR_UNSPECIFIED);
}

void moveChildAbove(Control child1, Control child2) {
	if (child1 == null) error(SWT.ERROR_NULL_ARGUMENT);

	final int index1 = children.indexOf(child1);
	if (index1 < 0) error(SWT.ERROR_UNSPECIFIED);

	final int newIndex;
	if (child2 != null) {
		newIndex = children.indexOf(child2);
	} else {
		newIndex = 0;
	}

	if (index1 <= newIndex) {
		return;
	}

	children.remove(index1);
	children.add(newIndex, child1);
}

@Override
public Point computeSize(int wHint, int hHint, boolean changed) {
	if (isLightWeight()) {
		display.runSkin();
		Point size;
		if (layout != null) {
			if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
				changed |= (state & LAYOUT_CHANGED) != 0;
				state &= ~LAYOUT_CHANGED;
				size = layout.computeSize((Composite) this, wHint, hHint, changed);
			} else {
				size = new Point(wHint, hHint);
			}
		} else {
			size = minimumSize(wHint, hHint, changed);
			if (size.x == 0) {
				size.x = DEFAULT_WIDTH;
			}
			if (size.y == 0) {
				size.y = DEFAULT_HEIGHT;
			}
		}
		if (wHint != SWT.DEFAULT) {
			size.x = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			size.y = hHint;
		}
		Rectangle trim = computeTrim(0, 0, size.x, size.y);
		return new Point(trim.width, trim.height);
	}

	return super.computeSize(wHint, hHint, changed);
}

Point minimumSize(int wHint, int hHint, boolean changed) {
	Rectangle clientArea = getClientArea();
	int width = 0, height = 0;
	for (Control child : this_children()) {
		Rectangle rect = child.getBounds();
		width = Math.max(width, rect.x - clientArea.x + rect.width);
		height = Math.max(height, rect.y - clientArea.y + rect.height);
	}
	return new Point(width, height);
}

@Override
protected void resized() {
	super.resized();
	if (isDisposed()) return;
	if (layout != null) {
		markLayout(false, false);
		_updateLayout(false);
	}
}

@Override
protected void _updateLayout(boolean all) {
	Composite parent = findDeferredControl();
	if (parent != null) {
		parent.state |= LAYOUT_CHILD;
		return;
	}
	if ((state & LAYOUT_NEEDED) != 0) {
		boolean changed = (state & LAYOUT_CHANGED) != 0;
		state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
		display.runSkin();
		layout.layout((Composite) this, changed);
	}
	if (all) {
		state &= ~LAYOUT_CHILD;
		for (Control child : this_children()) {
			child._updateLayout(all);
		}
	}
}

@Override
protected void getCurrentTabStopControls(List<Control> controls) {
	super.getCurrentTabStopControls(controls);

	final List<Control> children = this_children();
	for (Control child : children) {
		if (!child.getVisible() || !child.getEnabled()) {
			continue;
		}

		child.getCurrentTabStopControls(controls);
	}
}

@Override
protected void hideNativeChilds() {
	for (Control child : this_children()) {
		if (!child.getVisible()) {
			continue;
		}

		if (!child.isLightWeight()) {
			child.hideNativeControl();
			continue;
		}

		if (child instanceof CompositeCommon composite) {
			composite.hideNativeChilds();
		}
	}
}

@Override
protected void paintAndSendEvent(Event paintEvent) {
	final Rectangle clipping = new Rectangle(paintEvent.x, paintEvent.y, paintEvent.width, paintEvent.height);

	super.paintAndSendEvent(paintEvent);

	if (!isLightWeight() && !isLightWeightChildHandling()) {
		return;
	}

	paintEvent.gc.setPreventDispose(true);
	try {
		drawChildren(clipping, paintEvent);
	} finally {
		paintEvent.gc.setPreventDispose(false);
	}
}

@Override
protected void paintControl(Event event) {
	if ((style & (SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND | SWT.TRANSPARENT)) == 0) {
		event.gc.setBackground(getBackground());
		event.gc.fillRectangle(event.x, event.y, event.width, event.height);
	}
}

private void drawChildren(Rectangle clipping, Event e) {
	final List<Control> children = new ArrayList<>(this_children());
	Collections.reverse(children);
	for (Control child : children) {
		if (!child.isLightWeight()) {
			continue;
		}

		if (!child.getVisible()) {
			continue;
		}

		final Rectangle b = child.getBounds();
		if (b.width < 1 || b.height < 1) {
			continue;
		}

		if (!clipping.intersects(b)) {
			continue;
		}

		e.gc.setForeground(child.getForeground());
		e.gc.setBackground(child.getBackground());
		e.widget = child;
		e.gc.translate(b.x, b.y);
		try {
			final Rectangle subClipping = b.intersection(clipping);
			subClipping.x -= b.x;
			subClipping.y -= b.y;
			e.setBounds(subClipping);
			e.gc.setClipping(subClipping);
			child.paintAndSendEvent(e);
		}
		finally {
			e.gc.translate(-b.x, -b.y);
		}
	}
}
}
