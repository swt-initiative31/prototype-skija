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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class DefaultScaleRenderer implements ScaleRenderer {

	static final String KEY_HANDLE_IDLE = "scale.handle.background"; //$NON-NLS-1$
	static final String KEY_HANDLE_HOVER = "scale.handle.background.hover"; //$NON-NLS-1$
	static final String KEY_HANDLE_DRAG = "scale.handle.background.drag"; //$NON-NLS-1$
	static final String KEY_HANDLE_OUTLINE = "scale.handle.outline"; //$NON-NLS-1$
	static final String KEY_DISABLED = "scale.disabled"; //$NON-NLS-1$
	static final String KEY_NOTCH = "scale.notch.foreground"; //$NON-NLS-1$

	private final Scale scale;

	/**
	 * Pixel per Unit. Ratio of how many pixels are used to display one unit of the
	 * scale.
	 */
	private double ppu;

	private Rectangle handleBounds;
	private Rectangle bar;

	DefaultScaleRenderer(Scale scale) {
		this.scale = scale;
	}

	@Override
	public void render(GC gc, Point size) {
		int value = scale.getSelection();
		int min = scale.getMinimum();
		int max = scale.getMaximum();
		int units = Math.max(1, max - min);
		int effectiveValue = Math.min(max, Math.max(min, value));

		int firstNotch;
		int lastNotch;

		if (scale.isVertical()) {
			bar = new Rectangle(19, 8, 3, size.y - 16);
			firstNotch = bar.y + 5;
			lastNotch = bar.y + bar.height - 5;
		} else {
			bar = new Rectangle(8, 19, size.x - 16, 3);
			firstNotch = bar.x + 5;
			lastNotch = bar.x + bar.width - 5;
		}

		final ColorProvider colorProvider = scale.getColorProvider();
		gc.setForeground(colorProvider.getColor(KEY_HANDLE_OUTLINE));
		gc.drawRectangle(bar);

		// prepare for line drawing
		gc.setForeground(colorProvider.getColor(KEY_NOTCH));
		gc.setLineWidth(1);

		// draw first and last notch
		drawNotch(gc, firstNotch, 4);
		drawNotch(gc, lastNotch, 4);

		// draw center notches
		int unitPerPage = scale.getPageIncrement();
		double totalPixel = lastNotch - firstNotch;
		ppu = totalPixel / units;
		drawCenterNotches(gc, firstNotch, lastNotch, units, unitPerPage);

		drawHandle(gc, effectiveValue, colorProvider);
	}

	private void drawCenterNotches(GC gc, int firstNotchPos, int lastNotchPos, int units, int unitPerPage) {
		if (isRTL() && scale.isHorizontal()) {
			for (int i = unitPerPage; i < units; i += unitPerPage) {
				int position = lastNotchPos - (int) (i * ppu);
				drawNotch(gc, position, 3);
			}
		} else { // SWT.LEFT_TO_RIGHT or SWT.VERTICAL
			for (int i = unitPerPage; i < units; i += unitPerPage) {
				int position = firstNotchPos + (int) (i * ppu);
				drawNotch(gc, position, 3);
			}
		}
	}

	private void drawHandle(GC gc, int value, ColorProvider colorProvider) {
		final String colorKey;
		if (scale.isEnabled()) {
			colorKey = switch (scale.getHandleState()) {
				case IDLE -> KEY_HANDLE_IDLE;
				case HOVER -> KEY_HANDLE_HOVER;
				case DRAG -> KEY_HANDLE_DRAG;
			};
		} else {
			colorKey = KEY_DISABLED;
		}
		Color handleColor = colorProvider.getColor(colorKey);
		gc.setBackground(handleColor);
		handleBounds = calculateHandleBounds(value);
		gc.fillRectangle(handleBounds);
	}

	private Rectangle calculateHandleBounds(int value) {
		int pixelValue = (int) (ppu * value);
		if (scale.isVertical()) {
			return new Rectangle(bar.x - 9, bar.y + pixelValue, 21, 10);
		} else if (isRTL()) {
			return new Rectangle(bar.x + bar.width - pixelValue - 10, bar.y - 9, 10, 21);
		} else {
			return new Rectangle(bar.x + pixelValue, bar.y - 9, 10, 21);
		}
	}

	@Override
	public int handlePosToValue(Point pos) {
		if (scale.isVertical()) {
			return (int) Math.round((pos.y - bar.y) / ppu);
		} else {
			return (int) Math.round((pos.x - bar.x) / ppu);
		}
	}

	private boolean isRTL() {
		return scale.getOrientation() == SWT.RIGHT_TO_LEFT;
	}

	private void drawNotch(GC gc, int pos, int size) {
		if (scale.isVertical()) {
			gc.drawLine(bar.x - 10 - size, pos, bar.x - 10, pos);
			gc.drawLine(bar.x + 14, pos, bar.x + 14 + size, pos);
		} else {
			gc.drawLine(pos, bar.y - 10 - size, pos, bar.y - 10);
			gc.drawLine(pos, bar.y + 14, pos, bar.y + 14 + size);
		}
	}

	/*
	 * If in RTL mode, the points of events are relative to the top right corner of
	 * the widget. Since everything else is still relative to the top left, we
	 * mirror vertically.
	 */
	private Point mirrorVertically(Point p) {
		return new Point(scale.getBounds().width - p.x, p.y);
	}

	@Override
	public boolean isWithinHandle(Point position) {
		if (isRTL()) {
			position = mirrorVertically(position);
		}
		return handleBounds.contains(position);
	}

	@Override
	public boolean isAfterHandle(Point position) {
		if (scale.isVertical()) {
			return position.y > handleBounds.y + handleBounds.height;
		} else if (isRTL()) {
			position = mirrorVertically(position);
			return position.x < handleBounds.x;
		} else {
			return position.x > handleBounds.x + handleBounds.width;
		}
	}

	@Override
	public boolean isBeforeHandle(Point position) {
		if (scale.isVertical()) {
			return position.y < handleBounds.y;
		} else if (isRTL()) {
			position = mirrorVertically(position);
			return position.x > handleBounds.x + handleBounds.width;
		} else {
			return position.x < handleBounds.x;
		}
	}
}
