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
 * 				Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class DefaultSliderRenderer extends SliderRenderer {
	private static final int PREFERRED_WIDTH = 170;
	private static final int PREFERRED_HEIGHT = 42;

	private static final Color DRAG_COLOR = new Color(204, 204, 204);
	private static final Color LINE_COLOR = new Color(160, 160, 160);
	private static final Color TRACK_COLOR = new Color(211, 211, 211, 100);
	private static final Color THUMB_HOVER_COLOR = new Color(135, 206, 235);

	private Rectangle trackRectangle;
	private boolean isDragging;
	private Rectangle thumbRectangle;
	private boolean drawTrack;

	private boolean thumbHovered;
	private int dragOffset;
	private int drawWidth;
	private int drawHeight;
	private int thumbPosition;

	protected DefaultSliderRenderer(Slider slider) {
		super(slider);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		int value = getSelection();
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();
		int range = max - min;

		this.drawWidth = width;
		this.drawHeight = height;

		// Fill background
		if (slider.getBackground() != null) {
			Color background = slider.getBackground();
			gc.setBackground(background);
			gc.fillRectangle(0, 0, width, height);
		}
		gc.setForeground(LINE_COLOR);

		int trackX, trackY, trackWidth, trackHeight;

		if (slider.isVertical()) {
			trackX = (width - 7) / 2;
			trackY = 2;
			trackWidth = 7;
			trackHeight = height - 4;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbHeight = Math.max(10, (thumb * (height - 4)) / range);
			int thumbWidth = 7;

			int adjustedRange = range - thumb;
			int thumbY = trackY + ((height - thumbHeight - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(trackX, thumbY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, 0, height);
		} else {
			trackX = 2;
			trackY = (height - 7) / 2;
			trackWidth = width - 4;
			trackHeight = 7;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbWidth = Math.max(10, (thumb * (width - 4)) / range);
			int thumbHeight = 7;

			int adjustedRange = range - thumb;
			int thumbX = trackX + ((width - thumbWidth - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(thumbX, trackY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, width, 0);
		}

		// Draw the track
		if (drawTrack) {
			gc.setBackground(TRACK_COLOR);
			gc.drawRoundRectangle(trackRectangle.x, trackRectangle.y, trackRectangle.width, trackRectangle.height, 10,
					10);
			gc.fillRoundRectangle(trackRectangle.x, trackRectangle.y, trackRectangle.width, trackRectangle.height, 10,
					10);
		}

		// Draw the thumb
		if (thumbHovered || isDragging) {
			gc.setForeground(LINE_COLOR);
			gc.setBackground(THUMB_HOVER_COLOR);
		} else {
			gc.setForeground(LINE_COLOR);
			gc.setBackground(DRAG_COLOR);
		}
		gc.drawRoundRectangle(thumbRectangle.x, thumbRectangle.y, thumbRectangle.width, thumbRectangle.height, 10, 10);
		gc.fillRoundRectangle(thumbRectangle.x, thumbRectangle.y, thumbRectangle.width, thumbRectangle.height, 10, 10);

	}

	@Override
	public void onMouseDown(Event event) {
		if (event.button != 1)
			return;

		// Drag of the thumb.
		if (thumbRectangle != null && thumbRectangle.contains(event.x, event.y)) {
			isDragging = true;
			dragOffset = (slider.isHorizontal()) ? event.x - thumbRectangle.x : event.y - thumbRectangle.y;
			return;
		}

		// Click on the track. i.e page increment/decrement
		if (trackRectangle != null && trackRectangle.contains(event.x, event.y)) {
			int pageIncrement = getPageIncrement();
			int oldSelection = getSelection();
			int newSelection;

			if (slider.isHorizontal()) {
				if (event.x < thumbRectangle.x) {
					newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
				} else {
					newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
				}
			} else {
				if (event.y < thumbRectangle.y) {
					newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
				} else {
					newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
				}
			}

			slider.setSelection(newSelection);
			slider.redraw();
		}
	}

	@Override
	public void onMouseUp(Event event) {
		if (isDragging) {
			isDragging = false;
			updateValueFromThumbPosition();
			slider.redraw();
		}
	}

	private void updateValueFromThumbPosition() {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();
		int range = max - min;

		if (slider.isHorizontal()) {
			int trackWidth = this.drawWidth - 4 - thumbRectangle.width;
			int relativeX = Math.min(trackWidth, Math.max(0, thumbPosition - 2));
			int newValue = min + (relativeX * (range - thumb)) / trackWidth;

			slider.setSelection(newValue);
		} else {
			int trackHeight = this.drawHeight - 4 - thumbRectangle.height;
			int relativeY = Math.min(trackHeight, Math.max(0, thumbPosition - 2));
			int newValue = min + (relativeY * (range - thumb)) / trackHeight;

			slider.setSelection(newValue);
		}
	}

	@Override
	public void onMouseMove(Event event) {
		if (thumbRectangle == null) {
			return;
		}

		boolean isThumbHovered = thumbRectangle.contains(event.x, event.y);
		if (isThumbHovered != thumbHovered) {
			thumbHovered = isThumbHovered;
			slider.redraw();
		}

		if (isDragging) {
			int newPos;
			int min = getMinimum();
			int max = getMaximum();
			int thumb = getThumb();
			int range = max - min;
			int newValue;

			if (slider.isHorizontal()) {
				newPos = event.x - dragOffset;

				int minX = 2;
				int maxX = drawWidth - thumbRectangle.width - 2;
				newPos = Math.max(minX, Math.min(newPos, maxX));

				thumbRectangle.x = newPos;
				thumbPosition = newPos;

				int trackWidth = drawWidth - 4 - thumbRectangle.width;
				int relativeX = thumbPosition - 2;
				newValue = min + Math.round((relativeX * (float) (range - thumb)) / trackWidth);
			} else {
				newPos = event.y - dragOffset;

				int minY = 2;
				int maxY = drawHeight - thumbRectangle.height - 2;
				newPos = Math.max(minY, Math.min(newPos, maxY));

				thumbRectangle.y = newPos;
				thumbPosition = newPos;

				int trackHeight = drawHeight - 4 - thumbRectangle.height;
				int relativeY = thumbPosition - 2;
				newValue = min + Math.round((relativeY * (float) (range - thumb)) / trackHeight);
			}

			newValue = Math.max(min, Math.min(newValue, max - thumb));

			if (newValue != getSelection()) {
				slider.setSelection(newValue);
			}
			slider.redraw();
		}
	}

	@Override
	public void onKeyDown(Event event) {
		KeyEvent keyEvent = new KeyEvent(event);
		switch (keyEvent.keyCode) {
		case SWT.ARROW_DOWN, SWT.ARROW_LEFT -> increment(-1);
		case SWT.ARROW_RIGHT, SWT.ARROW_UP -> increment(1);
		case SWT.PAGE_DOWN -> pageIncrement(-1);
		case SWT.PAGE_UP -> pageIncrement(1);
		case SWT.HOME -> slider.setSelection(getMinimum());
		case SWT.END -> slider.setSelection(getMaximum());
		}
	}

	@Override
	public void onMouseVerticalWheel(Event event) {
		if (event.count > 0) {
			increment(1);
		} else if (event.count < 0) {
			increment(-1);
		}
	}

	@Override
	public void onMouseHorizontalWheel(Event event) {
		if (event.count < 0) {
			increment(1);
		} else if (event.count > 0) {
			increment(-1);
		}
	}

	private void increment(int count) {
		int delta = getIncrement() * count;
		int newValue = SliderRenderer.minMax(getMinimum(), getSelection() + delta, getMaximum());
		slider.setSelection(newValue);
		slider.redraw();
	}

	private void pageIncrement(int count) {
		int delta = getPageIncrement() * count;
		int newValue = SliderRenderer.minMax(getMinimum(), getSelection() + delta, getMaximum());
		slider.setSelection(newValue);
		slider.redraw();
	}

	@Override
	public Point computeDefaultSize() {
		int width;
		int height;
		if (slider.isVertical()) {
			width = PREFERRED_HEIGHT;
			height = PREFERRED_WIDTH;
		} else {
			width = PREFERRED_WIDTH;
			height = PREFERRED_HEIGHT;
		}

		return new Point(width, height);
	}

	@Override
	public void setDrawTrack(boolean drawTrack) {
		this.drawTrack = drawTrack;
	}
}
