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
import org.eclipse.swt.internal.win32.RECT;

/**
 * Temporary API.
 */
public abstract class CustomControl extends NativeBasedCustomControl {

	protected abstract Point computeDefaultSize();

	private boolean visible = true;
	private boolean enabled = true;
	private int x;
	private int y;
	private int width;
	private int height;

	protected CustomControl(Composite parent, int style) {
		super(parent, style);
		parent.addCustomChild(this);
	}

	@Override
	void destroyWidget() {
		parent.removeCustomChild(this);
	}

	@Override
	void reskinWidget() {
	}

	@Override
	void createHandle() {
	}

	@Override
	void register() {
	}

	@Override
	void subclass() {
	}

	@Override
	void setDefaultFont() {
	}

	@Override
	void checkGesture() {
	}

	@Override
	public boolean getEnabled() {
		checkWidget();
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		checkWidget();
		if (enabled == this.enabled) {
			return;
		}

		this.enabled = enabled;
		if (parent.isEnabled()) {
			redraw();
		}
		// todo fix focus if invisible
	}

	@Override
	public Point getSize() {
		return new Point(width, height);
	}

	@Override
	public void setSize(int width, int height) {
		checkWidget();
		if (width == this.width && height == this.height) {
			return;
		}

		this.width = width;
		this.height = height;
		redraw();
	}

	@Override
	public void setSize(Point size) {
		checkWidget ();
		if (size == null) error (SWT.ERROR_NULL_ARGUMENT);

		setSize(size.x, size.y);
	}

	@Override
	public Point getLocation() {
		checkWidget();
		return new Point(x, y);
	}

	@Override
	public void setLocation(int x, int y) {
		checkWidget();
		if (x == this.x && y == this.y) {
			return;
		}

		this.x = x;
		this.y = y;
		super.setLocation(x, y);
		redraw();
	}

	@Override
	public void setLocation(Point location) {
		if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
		setLocation(location.x, location.y);
	}

	@Override
	public Rectangle getBounds() {
		checkWidget();
		return new Rectangle(x, y, width, height);
	}

	@Override
	public void setBounds(Rectangle rect) {
		if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
		setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		checkWidget();
		if (x == this.x
		    && y == this.y
		    && width == this.width
		    && height == this.height) {
			return;
		}

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		redraw();
	}

	@Override
	public void redraw() {
		if (!isVisible()) {
			return;
		}

		final Rectangle bounds = getBounds();
		if (bounds.width < 1 || bounds.height < 1) {
			return;
		}
		parent.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
	}

	@Override
	void redrawInPixels(RECT rect, boolean all) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isVisible() {
		checkWidget();
		return getVisible() && parent.isVisible();
	}

	@Override
	public boolean getVisible() {
		checkWidget();
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		checkWidget();
		if (visible == this.visible) {
			return;
		}

		this.visible = visible;
		redraw();
		// todo fix focus if invisible
	}

	@Override
	boolean isShowing() {
		if (!isVisible()) {
			return false;
		}
		Control control = this;
		while (control != null) {
			Point size = control.getSize();
			if (size.x == 0 || size.y == 0) {
				return false;
			}
			control = control.parent;
		}
		return true;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		Point defaultSize = computeDefaultSize();
		int width = wHint == SWT.DEFAULT ? defaultSize.x : wHint;
		int height = hHint == SWT.DEFAULT ? defaultSize.y : hHint;
		return new Point(width, height);
	}

	@Override
	boolean hasFocus() {
		final Composite nativeParent = getNativeParent();
		return nativeParent.hasFocus()
		       && nativeParent.getCustomBridge().isFocusControl(this);
	}

	@Override
	public boolean forceFocus() {
		checkWidget();
		if (display.focusEvent == SWT.FocusOut) {
			return false;
		}
		Decorations shell = menuShell();
		shell.setSavedFocus(this);
		if (!isEnabled() || !isVisible() || !isActive()) {
			return false;
		}
		if (display.getActiveShell() != shell && !Display.isActivateShellOnForceFocus()) {
			return false;
		}
		if (isFocusControl()) {
			return true;
		}
		shell.setSavedFocus(null);

		getNativeParent().getCustomBridge().setFocus(this);

		if (isDisposed()) {
			return false;
		}
		shell.setSavedFocus(this);
		final boolean focusControl = isFocusControl();
		if (focusControl) {
			redraw();
		}
		return focusControl;
	}

	private Composite getNativeParent() {
		return parent;
	}
}
