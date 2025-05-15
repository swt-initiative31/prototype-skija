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
private CompositeCustomChildHandling customChildHandling;

protected CompositeCommon() {
}

protected CompositeCommon(Composite parent, int style) {
	super(parent, style);
}

protected final List<Control> this_children() {
	return Collections.unmodifiableList(children);
}

protected final boolean isCustomChildHandling() {
	return customChildHandling != null;
}

void addChild(Control control) {
	if (isCustomControl()) {
		if (!control.isCustomControl()) error(SWT.ERROR_UNSPECIFIED);
	}
	else {
		if (control.isCustomControl() && customChildHandling == null) {
			customChildHandling = new CompositeCustomChildHandling((Composite) this);
		}
	}
	children.add(control);
}

void removeChild(Control control) {
	if (!children.remove(control)) error(SWT.ERROR_UNSPECIFIED);
}

@Override
public Point computeSize(int wHint, int hHint, boolean changed) {
	if (isCustomControl()) {
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
}
