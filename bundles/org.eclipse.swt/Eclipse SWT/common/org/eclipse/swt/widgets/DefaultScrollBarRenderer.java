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
 * 				Raghunandana Murthappa, Thomas Singer (syntevo)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

class DefaultScrollBarRenderer extends ScrollBarRenderer {
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

	public DefaultScrollBarRenderer(CustomScrollBar scrollBar) {
		super(scrollBar);
	}

	@Override
	public void paint(GC gc, int x, int y, int width, int height) {
		int value = scrollBar.getSelection();
		int min = scrollBar.getMinimum();
		int max = scrollBar.getMaximum();
		int thumb = scrollBar.getThumb();
		int range = max - min;

		gc.setBackground(scrollBar.getColor(ControlRenderer.COLOR_BACKGROUND));
		gc.fillRectangle(x, y, width, height);

		gc.setForeground(scrollBar.getColor(scrollBar.getEnabled() ? ControlRenderer.COLOR_FOREGROUND : ControlRenderer.COLOR_DISABLED));

		if (scrollBar.isHorizontal()) {
			int trackX = 2;
			int trackY = (height - 7) / 2;
			int trackWidth = width - 4;
			int trackHeight = 7;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbWidth = Math.max(10, (thumb * (width - 4)) / range);
			int thumbHeight = trackHeight;

			int adjustedRange = range - thumb;
			int thumbX = trackX + ((width - thumbWidth - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(thumbX, trackY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, width, 0);
		} else {
			int trackX = (width - 7) / 2;
			int trackY = 2;
			int trackWidth = 7;
			int trackHeight = height - 4;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbHeight = Math.max(10, (thumb * (height - 4)) / range);
			int thumbWidth = trackWidth;

			int adjustedRange = range - thumb;
			int thumbY = trackY + ((height - thumbHeight - 4) * (value - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(trackX, thumbY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, 0, height);
		}

		gc.setForeground(scrollBar.getColor(COLOR_TRACK_BORDER));
		// Draw the track
		if (drawTrack) {
			gc.setBackground(scrollBar.getColor(COLOR_TRACK_BACKGROUND));
			drawRoundRectWithBorder(gc, trackRectangle);
		}

		// Draw the thumb
		gc.setBackground(scrollBar.getColor(thumbHovered || isDragging ? COLOR_THUMB_HOVER : COLOR_THUMB_BACKGROUND));
		gc.setForeground(scrollBar.getColor(COLOR_THUMB_BORDER));
		drawRoundRectWithBorder(gc, thumbRectangle);
	}

	private void drawRoundRectWithBorder(GC gc, Rectangle rect) {
		final int arcSize = 10;
		gc.fillRoundRectangle(rect.x, rect.y, rect.width + 1, rect.height + 1, arcSize, arcSize);
		if (scrollBar.getEnabled()) {
			gc.drawRoundRectangle(rect.x, rect.y, rect.width, rect.height, arcSize, arcSize);
		}
	}

	@Override
	public Point computeDefaultSize() {
		int width;
		int height;
		if (scrollBar.isHorizontal()) {
			width = PREFERRED_WIDTH;
			height = PREFERRED_HEIGHT;
		} else {
			width = PREFERRED_HEIGHT;
			height = PREFERRED_WIDTH;
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
