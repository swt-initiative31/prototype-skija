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

/**
 * Temporary API.
 */
public abstract class CustomControl extends NativeBasedCustomControl {

	protected abstract Point computeDefaultSize();

	protected abstract ControlState getControlState();

	protected CustomControl(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public Point getSize() {
		return getControlState().getSize();
	}

	@Override
	public void setSize(int width, int height) {
		checkWidget();
		super.setSize(width, height);
		getControlState().setSize(width, height);
	}

	@Override
	public void setSize(Point size) {
		checkWidget ();
		if (size == null) error (SWT.ERROR_NULL_ARGUMENT);

		setSize(size.x, size.y);
	}

	@Override
	public Point getLocation() {
		return getControlState().getLocation();
	}

	@Override
	public void setLocation(int x, int y) {
		checkWidget();
		super.setLocation(x, y);
		getControlState().setLocation(x, y);
		redraw();
	}

	@Override
	public void setLocation(Point location) {
		if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
		setLocation(location.x, location.y);
	}

	@Override
	public Rectangle getBounds() {
		return getControlState().getBounds();
	}

	@Override
	public void setBounds(Rectangle rect) {
		checkWidget();
		if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
		super.setBounds(rect);
		getControlState().setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		// swap both implementations later
		setBounds(new Rectangle(x, y, width, height));
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getControlState().setEnabled(enabled);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		Point defaultSize = computeDefaultSize();
		int width = wHint == SWT.DEFAULT ? defaultSize.x : wHint;
		int height = hHint == SWT.DEFAULT ? defaultSize.y : hHint;
		return new Point(width, height);
	}
}
