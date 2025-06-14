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

class DefaultSliderRenderer extends SliderRenderer {
	private static final int PREFERRED_WIDTH = 170;
	private static final int PREFERRED_HEIGHT = 18;

	static final String COLOR_TRACK_BACKGROUND = "slider.track.background"; //$NON-NLS-1$
	static final String COLOR_TRACK_BORDER = "slider.track.border"; //$NON-NLS-1$
	static final String COLOR_THUMB_BACKGROUND = "slider.thumb.background"; //$NON-NLS-1$
	static final String COLOR_THUMB_BORDER = "slider.thumb.border"; //$NON-NLS-1$
	static final String COLOR_THUMB_HOVER = "slider.thumb.hover"; //$NON-NLS-1$

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
		if (range == 0) {
			range = 1;
		}

		// Fill background
		gc.setBackground(slider.getBackground());
		gc.fillRectangle(0, 0, width, height);

		gc.setForeground(slider.isEnabled() ? slider.getForeground() : getColor(COLOR_DISABLED));

		if (slider.isVertical()) {
			int trackX = (width - 7) / 2;
			int trackY = 2;
			int trackWidth = 7;
			int trackHeight = height - 4;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbHeight = Math.max(10, (thumb * (height - 4)) / range);
			int thumbWidth = trackWidth;

			int adjustedRange = range - thumb;
			if (adjustedRange == 0) {
				adjustedRange = 1;
			}
			int thumbY = trackY + ((height - thumbHeight - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(trackX, thumbY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, 0, height);
		} else {
			int trackX = 2;
			int trackY = (height - 7) / 2;
			int trackWidth = width - 4;
			int trackHeight = 7;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbWidth = Math.max(10, (thumb * (width - 4)) / range);
			int thumbHeight = trackHeight;

			int adjustedRange = range - thumb;
			if (adjustedRange == 0) {
				adjustedRange = 1;
			}
			int thumbX = trackX + ((width - thumbWidth - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(thumbX, trackY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, width, 0);
		}

		gc.setForeground(getColor(COLOR_TRACK_BORDER));
		// Draw the track
		if (drawTrack) {
			gc.setBackground(getColor(COLOR_TRACK_BACKGROUND));
			drawRoundRectWithBorder(gc, trackRectangle);
		}

		// Draw the thumb
		gc.setBackground(getColor(thumbHovered || isDragging ? COLOR_THUMB_HOVER : COLOR_THUMB_BACKGROUND));
		gc.setForeground(getColor(COLOR_THUMB_BORDER));
		drawRoundRectWithBorder(gc, thumbRectangle);
	}

	private void drawRoundRectWithBorder(GC gc, Rectangle rect) {
		final int arcSize = 10;
		gc.fillRoundRectangle(rect.x, rect.y, rect.width + 1, rect.height + 1, arcSize, arcSize);
		if (slider.isEnabled()) {
			gc.drawRoundRectangle(rect.x, rect.y, rect.width, rect.height, arcSize, arcSize);
		}
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
	public void setThumbHovered(boolean thumbHovered) {
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
