/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

abstract class NativeBasedCustomScrollable extends Scrollable {

	protected abstract ControlRenderer getRenderer();

	private Color backgroundColor;
	private Color foregroundColor;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private boolean visible = true;
	private boolean enabled = true;

	public NativeBasedCustomScrollable(Composite parent, int style) {
		super(parent, style);
		// this is for custom drawn widgets necessary to update the scrolling with thumb
		// and with the arrows in the scrollbar widget. Else the position won't be
		// updated.
		state = state | CANVAS;
	}
	
	public boolean isVisible () {
		return getVisible () && parent.isVisible ();
	}
	
	public boolean getVisible() {
		return visible ;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isEnabled () {
		checkWidget ();
		return getEnabled () && parent.isEnabled ();
	}
	
	public boolean getEnabled () {
		return enabled;
	}
	
	public void setEnabled ( boolean enable ) {
		this.enabled = enable;
	}
	
	boolean isNativeScrollable(){
		return false;
	}
	
	void createHandle () {
		parent.addWidget(this);
	}

	@Override
	public final Color getBackground() {
		return backgroundColor != null ? backgroundColor : getRenderer().getDefaultBackground();
	}

	@Override
	public final void setBackground(Color color) {
		backgroundColor = color;
		super.setBackground(color);
	}

	@Override
	public final Color getForeground() {
		return foregroundColor != null ? foregroundColor : getRenderer().getDefaultForeground();
	}

	@Override
	public final void setForeground(Color color) {
		foregroundColor = color;
		super.setForeground(color);
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
		checkWidget();
		if (rect.x == this.x && rect.y == this.y && rect.width == this.width && rect.height == this.height) {
			return;
		}

		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
		super.setBounds(rect);
		redraw();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
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
	
}
