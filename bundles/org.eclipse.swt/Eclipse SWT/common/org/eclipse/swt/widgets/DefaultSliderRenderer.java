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

import org.eclipse.swt.graphics.*;

public class DefaultSliderRenderer extends SliderRenderer {
	private static final int PREFERRED_WIDTH = 170;
	private static final int PREFERRED_HEIGHT = 10;

	private static final Color DRAG_COLOR = new Color(204, 204, 204);
	private static final Color LINE_COLOR = new Color(160, 160, 160);
	private static final Color TRACK_COLOR = new Color(211, 211, 211, 100);
	private static final Color THUMB_HOVER_COLOR = new Color(135, 206, 235);

	private boolean drawTrack;

	private Rectangle trackRectangle;
	private Rectangle thumbRectangle;
	private boolean isDragging;
	private boolean thumbHovered;

	protected DefaultSliderRenderer(Slider slider) {
		super(slider);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		int value = slider.getSelection();
		int min = slider.getMinimum();
		int max = slider.getMaximum();
		int thumb = slider.getThumb();
		int range = max - min;

		// Fill background
		Color background = slider.getBackground();
		if (background != null) {
			gc.setBackground(background);
			gc.fillRectangle(0, 0, width, height);
		}
		gc.setBackground(LINE_COLOR);

		if (slider.isVertical()) {
			int trackX = (width - 7) / 2 + 1;
			int trackY = 2;
			int trackWidth = 7;
			int trackHeight = height - 4;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbHeight = Math.max(10, (thumb * (height - 4)) / range);
			int thumbWidth = 7;

			int adjustedRange = range - thumb;
			int thumbY = trackY + ((height - thumbHeight - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(trackX, thumbY, thumbWidth, thumbHeight);
			}
			gc.fillRectangle(0, 0, 1, height);
		} else {
			int trackX = 2;
			int trackY = (height - 7) / 2 + 1;
			int trackWidth = width - 4;
			int trackHeight = 7;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbWidth = Math.max(10, (thumb * (width - 4)) / range);
			int thumbHeight = 7;

			int adjustedRange = range - thumb;
			int thumbX = trackX + ((width - thumbWidth - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(thumbX, trackY, thumbWidth, thumbHeight);
			}
			gc.fillRectangle(0, 0, width, 1);
		}

		// Draw the track
		if (drawTrack) {
			gc.fillRoundRectangle(trackRectangle.x, trackRectangle.y, trackRectangle.width, trackRectangle.height, 10,
					10);
			gc.setBackground(TRACK_COLOR);
			gc.fillRoundRectangle(trackRectangle.x + 1, trackRectangle.y + 1, trackRectangle.width - 2, trackRectangle.height - 2, 10,
					10);
		}

		// Draw the thumb
		gc.setBackground(LINE_COLOR);
		gc.fillRoundRectangle(thumbRectangle.x, thumbRectangle.y, thumbRectangle.width, thumbRectangle.height, 10, 10);
		if (thumbHovered || isDragging) {
			gc.setBackground(THUMB_HOVER_COLOR);
		} else {
			gc.setBackground(DRAG_COLOR);
		}
		gc.fillRoundRectangle(thumbRectangle.x + 1, thumbRectangle.y + 1, thumbRectangle.width - 2, thumbRectangle.height - 2, 8, 8);
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

	@Override
	public Rectangle getThumbRectangle() {
		return thumbRectangle;
	}

	@Override
	public Rectangle getTrackRectangle() {
		return trackRectangle;
	}

	@Override
	public void setDragging(boolean dragging) {
		this.isDragging = dragging;

	}

	@Override
	public void setHovered(boolean thumbHovered) {
		this.thumbHovered = thumbHovered;

	}

	@Override
	public boolean getDragging() {
		return isDragging;
	}

	@Override
	public boolean getHovered() {
		return thumbHovered;
	}
}
