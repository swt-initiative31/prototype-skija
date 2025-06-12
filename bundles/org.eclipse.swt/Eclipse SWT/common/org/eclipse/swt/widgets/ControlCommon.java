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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

abstract class ControlCommon extends Widget {

	protected abstract boolean isLightWeight();

	public static String toDebugName(Control control) {
		return control != null ? control.toDebugName() : "null";
	}

	Composite parent;

	// custom control properties
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean enabled = true;
	private boolean visible = true;

	protected ControlCommon() {
	}

	protected ControlCommon(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
	}

	protected String toDebugName() {
		return Integer.toString(System.identityHashCode(this), Character.MAX_RADIX) + "$" + getClass().getName();
	}

	protected final void assertIsLightWeight() {
		if (!isLightWeight()) error(SWT.ERROR_UNSPECIFIED);
	}

	protected final void assertIsNative() {
		if (isLightWeight()) error(SWT.ERROR_UNSPECIFIED);
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public void setLocation(Point location) {
		checkWidget();
		if (location == null) error(SWT.ERROR_NULL_ARGUMENT);

		setLocation(location.x, location.y);
	}

	public void setLocation(int x, int y) {
		checkWidget();
		if (x == this.x && y == this.y) return;

		this.x = x;
		this.y = y;
		redraw();
		moved();
	}

	protected void moved() {
		sendEvent(SWT.Move);
	}

	public Point getSize() {
		return new Point(width, height);
	}

	public void setSize(Point size) {
		checkWidget();
		if (size == null) error(SWT.ERROR_NULL_ARGUMENT);

		setSize(size.x, size.y);
	}

	public void setSize(int width, int height) {
		checkWidget();
		if (width == this.width && height == this.height) return;

		this.width = width;
		this.height = height;
		redraw();
		resized();
	}

	protected void resized() {
		sendEvent(SWT.Resize);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void setBounds(Rectangle rect) {
		checkWidget();
		if (rect == null) error(SWT.ERROR_NULL_ARGUMENT);

		setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	public void setBounds(int x, int y, int width, int height) {
		checkWidget();
		final boolean move = x != this.x || y != this.y;
		final boolean resize = width != this.width || height != this.height;
		if (!move && !resize) return;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		redraw();
		if (move) {
			moved();
		}
		if (resize) {
			resized();
		}
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		checkWidget();
		if (enabled == this.enabled) return;

		this.enabled = enabled;
		if (parent.isEnabled()) {
			redraw();
		}
	}

	public boolean getVisible() {
		return visible;
	}

	public boolean isVisible() {
		return visible && parent.isVisible();
	}

	public void setVisible(boolean visible) {
		checkWidget();
		if (visible == this.visible) return;

		if (visible) {
			sendEvent(SWT.Show);
			if (isDisposed()) return;
		} else {
			//TODO focus
		}

		this.visible = visible;

		if (!visible) {
			sendEvent(SWT.Hide);
		}
	}

	public void redraw() {
		final Point size = getSize();
		redraw(0, 0, size.x, size.y);
	}

	protected void redraw(int x, int y, int width, int height) {
		checkWidget();
		if (!isVisible()) {
			return;
		}

		final Point size = getSize();
		final Rectangle bounds = new Rectangle(0, 0, size.x, size.y);
		bounds.intersect(new Rectangle(x, y, width, height));
		if (bounds.width < 1 || bounds.height < 1) {
			return;
		}

		final Point location = getLocation();
		bounds.x += location.x;
		bounds.y += location.y;
		if (parent.isLightWeight()) {
			parent.redraw(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		else {
			parent.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
		}
	}

	protected final ColorProvider getColorProvider() {
		return display.getColorProvider();
	}
}
