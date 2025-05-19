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
	if (control.isLightWeight() && lightWeightChildHandling == null) {
		lightWeightChildHandling = new CompositeLightWeightChildHandling((Composite) this);
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
