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
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

abstract class ControlCommon extends Widget {

	protected abstract boolean isLightWeight();

	protected abstract Font getFont();

	protected abstract Color getBackground();

	protected abstract Color getForeground();

	protected abstract int getBorderWidth();

	public static Control getNativeControl(Control control) {
		for (; control != null; control = control.parent) {
			if (!control.isLightWeight()) {
				return control;
			}
		}
		return null;
	}

	public static Control getNativeControlAndOffset(Control control, Point offset) {
		while (control != null && control.isLightWeight()) {
			final Point location = control.getLocation();
			offset.x += location.x;
			offset.y += location.y;
			control = control.getParent();
		}
		return control;
	}

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

	protected boolean setParent(Composite parent) {
		checkWidget();
		if (parent == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (parent.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (this.parent == parent) return true;

		this.parent.removeChild((Control)this);
		this.parent = parent;
		parent.addChild((Control)this);
		return true;
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

	protected void _updateLayout(boolean all) {
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
		if (!isLightWeight()) {
			return;
		}
		redraw();
		if (move) {
			moved();
		}
		if (resize) {
			resized();
		}
	}

	protected Point toDisplay(int x, int y) {
		assertIsLightWeight();
		ControlCommon c = this;
		do {
			final Point location = c.getLocation();
			x += location.x;
			y += location.y;
			c = c.parent;
		} while (c.isLightWeight());
		return c.toDisplay(x, y);
	}

	/**
	 * Returns a point which is the result of converting the
	 * argument, which is specified in coordinates relative to
	 * the receiver, to display relative coordinates.
	 * <p>
	 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
	 * {@link Display#map(Control, Control, Rectangle)}.
	 * </p>
	 *
	 * @param point the point to be translated (must not be null)
	 * @return the translated coordinates
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public Point toDisplay(Point point) {
		checkWidget();
		if (point == null) error(SWT.ERROR_NULL_ARGUMENT);
		return toDisplay(point.x, point.y);
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		int border = getBorderWidth();
		width += border * 2;
		height += border * 2;
		return new Point(width, height);
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

		final boolean parentVisible = parent.isVisible();
		if (parentVisible && isLightWeight() && !visible) {
			redraw();
		}

		this.visible = visible;

		if (parentVisible) {
			updateNativeVisibility();
			if (isLightWeight() && visible) {
				redraw();
			}
		}
		if (!visible) {
			sendEvent(SWT.Hide);
		}
	}

	protected void updateNativeVisibility() {
	}

	boolean isShowing() {
		if (!isVisible()) return false;
		ControlCommon control = this;
		while (control != null) {
			Point size = control.getSize();
			if (size.x == 0 || size.y == 0) {
				return false;
			}
			control = control.parent;
		}
		return true;
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

		final Point offset = new Point(0, 0);
		final Composite parent = (Composite) getNativeControlAndOffset((Control) this, offset);
		parent.redraw(bounds.x + offset.x, bounds.y + offset.y, bounds.width, bounds.height, false);
	}

	protected void paintAndSendEvent(Event paintEvent) {
		initializeGC(paintEvent.gc);
		paintControl(paintEvent);

		if (hooks(SWT.Paint)) {
			initializeGC(paintEvent.gc);
			sendEvent(SWT.Paint, paintEvent);
		}
	}

	protected void paintControl(Event event) {
	}

	private void initializeGC(GC gc) {
		gc.setFont(getFont());
		gc.setBackground(getBackground());
		gc.setForeground(getForeground());
	}

	protected final ColorProvider getColorProvider() {
		return display.getColorProvider();
	}
}
