/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public abstract class DateTimeRenderer extends ControlRenderer {

	/**
	 * The DateTime widget associated with this renderer.
	 */
	protected final CDateTime dateTime;
	private String toolTipText;
	private Image backgroundImage;
	private Color[] gradientColors;
	private int[] gradientPercents;
	private boolean pressed;

	protected DateTimeRenderer(CDateTime dateTime) {
		super(dateTime);
		this.dateTime = dateTime;
	}

	/**
	 * Sets the bounds of the DateTime.
	 *
	 * @param x      the x-coordinate of the DateTime.
	 * @param y      the y-coordinate of the DateTime.
	 * @param width  the width of the DateTime.
	 * @param height the height of the DateTime.
	 */
	protected abstract void setDateTimeBounds(int x, int y, int width, int height);

	/**
	 * Gets the bounds of the DateTime.
	 *
	 * @return a {@link Rectangle} representing the bounds of the DateTime.
	 */
	protected abstract Rectangle getDateTimeBounds();

	public void dispose() {
		gradientColors = null;
		gradientPercents = null;
		backgroundImage = null;
		toolTipText = null;
	}

	public abstract Point computeDefaultSize();

	protected boolean isDate() {
		return dateTime.isDate();
	}

	protected boolean isTime() {
		return dateTime.isTime();
	}

	protected boolean isDateTime() {
		return dateTime.isDateTime();
	}

	protected boolean isCalender() {
		return dateTime.isCalender();
	}

	protected final boolean isPressed() {
		return pressed;
	}

	public final void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
//	protected abstract Rectangle getVisibleArea();
//
//	protected abstract Point getLocationByTextLocation(TextLocation textLocation, GC gc);
//
//	protected abstract int getLineHeight(GC gc);

	protected abstract void setDateTimeText(String dateTimeText);
}