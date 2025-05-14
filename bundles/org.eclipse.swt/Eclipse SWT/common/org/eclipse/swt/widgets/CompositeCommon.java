package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Thomas Singer
 */
abstract class CompositeCommon extends Scrollable {

private final List<Control> children = new ArrayList<>();

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
	if (control.isCustomControl() && customChildHandling == null) {
		customChildHandling = new CompositeCustomChildHandling((Composite) this);
	}
	children.add(control);
}

void removeChild(Control control) {
	if (!children.remove(control)) error(SWT.ERROR_UNSPECIFIED);
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
