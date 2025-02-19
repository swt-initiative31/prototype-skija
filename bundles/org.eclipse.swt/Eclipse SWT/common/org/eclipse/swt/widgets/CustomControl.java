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
public abstract class CustomControl extends Control implements ICustomWidget {

	private int x;
	private int y;
	private int width;
	private int height;

	protected CustomControl(Composite parent, int style) {
		super(parent, style);
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
		super.setSize(this.width, this.height);
		redraw();
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
		super.setBounds(x, y, width, height);
		redraw();
	}
}
