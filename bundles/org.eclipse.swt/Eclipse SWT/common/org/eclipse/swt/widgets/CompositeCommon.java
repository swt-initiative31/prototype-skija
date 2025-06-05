/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Drawing;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class CompositeCommon extends Scrollable {

	abstract Composite findDeferredControl();

	private final List<Control> children = new ArrayList<>();

	Layout layout;
	private Listener lightWeightChildHandlingListener;
	private Control mouseOverControl;

	protected CompositeCommon() {
	}

	protected CompositeCommon(Composite parent, int style) {
		super(parent, style);
	}

	protected final List<Control> this_children() {
		return Collections.unmodifiableList(children);
	}

	protected final boolean isLightWeightChildHandling() {
		return lightWeightChildHandlingListener != null;
	}

	void addChild(Control control) {
		if (!isLightWeight() && control.isLightWeight() && lightWeightChildHandlingListener == null) {
			lightWeightChildHandlingListener = e -> {
				switch (e.type) {
					case SWT.MouseMove -> onMouseMove(e);
					case SWT.MouseEnter -> onMouseEnter(e);
					case SWT.MouseExit -> onMouseExit(e);
					case SWT.MouseDown,
						 SWT.MouseUp -> onMouseDownOrUp(e);
					case SWT.MouseWheel -> onMouseDownOrUp(e);
					case SWT.MenuDetect -> onMenuDetect(e);
				}
			};
			addListener(SWT.MouseMove, lightWeightChildHandlingListener);
			addListener(SWT.MouseEnter, lightWeightChildHandlingListener);
			addListener(SWT.MouseExit, lightWeightChildHandlingListener);
			addListener(SWT.MouseDown, lightWeightChildHandlingListener);
			addListener(SWT.MouseUp, lightWeightChildHandlingListener);
			addListener(SWT.MouseWheel, lightWeightChildHandlingListener);
			addListener(SWT.MenuDetect, lightWeightChildHandlingListener);
		}
		children.add(control);
	}

	void removeChild(Control control) {
		if (!(control instanceof Shell) && !children.remove(control)) {
			error(SWT.ERROR_UNSPECIFIED);
		}
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
	protected void updateNativeVisibility() {
		if (!isLightWeight()) {
			super.updateNativeVisibility();
			return;
		}

		for (Control child : this_children()) {
			if (!child.getVisible()) {
				continue;
			}

			child.updateNativeVisibility();
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

	private void onMouseMove(Event e) {
		final Point location = new Point(e.x, e.y);
		final Control mouseControl = getChildAt(location);
		int type = e.type;
		if (mouseControl != mouseOverControl) {
			if (mouseOverControl != null) {
				sendMouseEvent(SWT.MouseExit, location, mouseOverControl, e);
			}
			mouseOverControl = mouseControl;
			type = SWT.MouseEnter;
		}
		if (mouseControl != null) {
			sendMouseEvent(type, location, mouseControl, e);
		}
	}

	private void onMouseEnter(Event e) {
		final Point location = new Point(e.x, e.y);
		mouseOverControl = getChildAt(location);
		if (mouseOverControl != null) {
			sendMouseEvent(e.type, location, mouseOverControl, e);
		}
	}

	private void onMouseExit(Event e) {
		if (mouseOverControl != null) {
			sendMouseEvent(e.type, new Point(e.x, e.y), mouseOverControl, e);
			mouseOverControl = null;
		}
	}

	private void onMouseDownOrUp(Event e) {
		final Point location = new Point(e.x, e.y);
		final Control mouseControl = getChildAt(location);
		if (mouseControl != mouseOverControl) {
			if (mouseOverControl != null) {
				sendMouseEvent(SWT.MouseExit, location, mouseOverControl, e);
			}
			mouseOverControl = mouseControl;
			if (mouseControl != null) {
				sendMouseEvent(SWT.MouseEnter, location, mouseControl, e);
			}
		}
		if (mouseControl != null) {
			sendMouseEvent(e.type, location, mouseControl, e);
		}
	}

	private void onMenuDetect(Event e) {
		if (e.detail == SWT.MENU_MOUSE) {
			final int screenX = e.x;
			final int screenY = e.y;
			final Point location = toControl(screenX, screenY);
			final Control mouseControl = getChildAt(location);
			if (mouseControl != mouseOverControl) {
				e.x = location.x;
				e.y = location.y;
				if (mouseOverControl != null) {
					sendMouseEvent(SWT.MouseExit, location, mouseOverControl, e);
				}
				mouseOverControl = mouseControl;
				if (mouseControl != null) {
					sendMouseEvent(SWT.MouseEnter, location, mouseControl, e);
				}
			}
			if (mouseControl != null) {
				sendMouseEvent(e.type, new Point(screenX, screenY), mouseControl, e);
			}
		} else {
			final Control focusControl = e.display.getFocusControl();
			if (focusControl != null && focusControl.isLightWeight()) {
				final Control nativeParent = ControlCommon.getNativeControl(focusControl);
				if (nativeParent == this) {
					sendMouseEvent(e.type, null, focusControl, e);
				}
			}
		}
	}

	private Control getChildAt(Point location) {
		return getChildAt(location, this);
	}

	private Control getChildAt(Point location, CompositeCommon composite) {
		for (Control child : composite.this_children()) {
			if (!child.isLightWeight()) {
				continue;
			}

			if (!child.isVisible()) {
				continue;
			}

			final Rectangle bounds = child.getBounds();
			if (bounds.contains(location.x, location.y)) {
				location.x -= bounds.x;
				location.y -= bounds.y;
				if (child instanceof Composite childComposite) {
					final Control grandChild = getChildAt(location, childComposite);
					if (grandChild != null) {
						return grandChild;
					}
				}
				return child;
			}
		}
		return null;
	}

	private static void sendMouseEvent(int type, Point location, Control control, Event original) {
		final Event event = new Event();
		event.display = original.display;
		event.widget = control;
		event.type = type;
		event.x = location != null ? location.x : original.x;
		event.y = location != null ? location.y : original.y;
		event.button = original.button;
		event.count = original.count;
		event.doit = original.doit;
		event.stateMask = original.stateMask;
		event.time = original.time;
		control.sendEvent(event);
	}
}
