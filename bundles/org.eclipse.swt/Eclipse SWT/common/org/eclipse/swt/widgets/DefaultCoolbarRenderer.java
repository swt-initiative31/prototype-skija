/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa (Advantest)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefaultCoolbarRenderer extends CoolbarRenderer {

	private CoolBar coolbar;
	Display display = Display.getCurrent();
	Color COLOR_WIDGET_NORMAL_SHADOW = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color COLOR_WIDGET_HIGHLIGHT_SHADOW = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);

	protected DefaultCoolbarRenderer(CoolBar control) {
		super(control);
		this.coolbar = control;
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		CoolItem[][] items = coolbar.items;
		if (items.length == 0) {
			return;
		}
		int style = coolbar.getStyle();
		boolean vertical = (style & SWT.VERTICAL) != 0;
		boolean flat = (style & SWT.FLAT) != 0;
		int stopX = width;
		Rectangle rect;
		Rectangle clipping = new Rectangle(0, 0, width, height);
		for (int row = 0; row < items.length; row++) {
			Rectangle bounds = new Rectangle(0, 0, 0, 0);
			for (int i = 0; i < items[row].length; i++) {
				bounds = items[row][i].internalGetBounds();
				rect = coolbar.fixRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
				if (!clipping.intersects(rect)) {
					continue;
				}
				boolean nativeGripper = false;

				/* Draw gripper. */
				if (!coolbar.isLocked) {
					rect = coolbar.fixRectangle(bounds.x, bounds.y, CoolItem.MINIMUM_WIDTH, bounds.height);
					if (flat) {
						int grabberTrim = 2;
						int grabberHeight = bounds.height - (2 * grabberTrim) - 1;
						gc.setForeground(COLOR_WIDGET_NORMAL_SHADOW);
						rect = coolbar.fixRectangle(bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim, 2,
								grabberHeight);
						gc.drawRectangle(rect);
						gc.setForeground(COLOR_WIDGET_HIGHLIGHT_SHADOW);
						rect = coolbar.fixRectangle(bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim + 1,
								bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim + grabberHeight - 1);
						gc.drawLine(rect.x, rect.y, rect.width, rect.height);
						rect = coolbar.fixRectangle(bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim,
								bounds.x + CoolItem.MARGIN_WIDTH + 1, bounds.y + grabberTrim);
						gc.drawLine(rect.x, rect.y, rect.width, rect.height);
					} else {
						nativeGripper = drawGripper(gc, rect.x, rect.y, rect.width, rect.height, vertical);
					}
				}

				/* Draw separator. */
				if (!flat && !nativeGripper && i != 0) {
					gc.setForeground(COLOR_WIDGET_NORMAL_SHADOW);
					rect = coolbar.fixRectangle(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
					gc.drawLine(rect.x, rect.y, rect.width, rect.height);
					gc.setForeground(COLOR_WIDGET_HIGHLIGHT_SHADOW);
					rect = coolbar.fixRectangle(bounds.x + 1, bounds.y, bounds.x + 1, bounds.y + bounds.height - 1);
					gc.drawLine(rect.x, rect.y, rect.width, rect.height);
				}
			}
			if (!flat && row + 1 < items.length) {
				/* Draw row separator. */
				int separatorY = bounds.y + bounds.height;
				gc.setForeground(COLOR_WIDGET_NORMAL_SHADOW);
				rect = coolbar.fixRectangle(0, separatorY, stopX, separatorY);
				gc.drawLine(rect.x, rect.y, rect.width, rect.height);
				gc.setForeground(COLOR_WIDGET_HIGHLIGHT_SHADOW);
				rect = coolbar.fixRectangle(0, separatorY + 1, stopX, separatorY + 1);
				gc.drawLine(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	boolean drawGripper(GC gc, int x, int y, int width, int height, boolean vertical) {
		int dotSpacing = 3;
		int dotLength = 1;
		if (vertical) {
			int centerY = y + height / 2;
			for (int i = x + 2; i < x + width - 2; i += dotSpacing) {
				gc.setForeground(COLOR_WIDGET_NORMAL_SHADOW);
				gc.drawLine(i, centerY, i + dotLength, centerY);
				gc.setForeground(COLOR_WIDGET_HIGHLIGHT_SHADOW);
				gc.drawLine(i, centerY + 1, i + dotLength, centerY + 1);
			}
		} else {
			int centerX = x + width / 2;
			for (int i = y + 2; i < y + height - 2; i += dotSpacing) {
				gc.setForeground(COLOR_WIDGET_NORMAL_SHADOW);
				gc.drawLine(centerX, i, centerX, i + dotLength);
				gc.setForeground(COLOR_WIDGET_HIGHLIGHT_SHADOW);
				gc.drawLine(centerX + 1, i, centerX + 1, i + dotLength);
			}
		}
		return true;
	}
}
