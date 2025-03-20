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

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefaultLinkRenderer extends LinkRenderer {

	private final Set<TextSegment> links = new HashSet<>();

	public DefaultLinkRenderer(Link link) {
		super(link);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		if (link.getText().isEmpty() || parsedText.isEmpty()) {
			return;
		}

		// Fill background
		gc.fillRectangle(0, 0, width, height);

		drawBackground(gc, width, height);

		Color linkColor = link.getLinkForeground();

		links.clear();

		int x = link.getLeftMargin();
		int lineY = link.getTopMargin();

		for (List<TextSegment> segments : parsedText) {
			int lineX = x;
			if (link.getAlignment() == SWT.CENTER) {
				int lineWidth = getLineExtent(gc, segments).x;
				if (width > lineWidth) {
					lineX = Math.max(x, (width - lineWidth) / 2);
				}
			}
			if (link.getAlignment() == SWT.RIGHT) {
				int lineWidth = getLineExtent(gc, segments).x;
				lineX = Math.max(x, 0 + width - link.getRightMargin() - lineWidth);
			}

			Point baseExtent = gc.textExtent("a", DRAW_FLAGS);

			for (TextSegment segment : segments) {
				Point extent = gc.textExtent(segment.text, DRAW_FLAGS);
				int noOfTrailSpaces = countTrailingSpaces(segment.text);
				if (noOfTrailSpaces > 0) {
					extent.x = extent.x + noOfTrailSpaces * baseExtent.x;
				}

				if (isEnabled()) {
					gc.setForeground(segment.isLink() ? linkColor : link.getForeground());
				} else {
					gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				}
				gc.drawText(segment.text, lineX, lineY, DRAW_FLAGS);

				if (segment.isLink()) {
					int underlineY = lineY + extent.y - 2;
					gc.drawLine(lineX, underlineY, lineX + extent.x, underlineY);
					// remember bounds of links
					segment.rect = new Rectangle(lineX, lineY, extent.x, extent.y);
					links.add(segment);
				}

				lineX += extent.x;
			}
			lineY += gc.getFontMetrics().getHeight();
		}
	}

	private void drawBackground(GC gc, int width, int height) {
		// draw a background image behind the text
		Color background = link.getBackground();
		Image backgroundImage = link.getBackgroundImage();
		try {
			if (backgroundImage != null) {
				// draw a background image behind the text
				Rectangle imageRect = backgroundImage.getBounds();
				// tile image to fill space
				gc.setBackground(background);
				gc.fillRectangle(0, 0, width, height);
				int xPos = 0;
				while (xPos < width) {
					int yPos = 0;
					while (yPos < height) {
						gc.drawImage(backgroundImage, xPos, yPos);
						yPos += imageRect.height;
					}
					xPos += imageRect.width;
				}
			} else {
				if (background != null && background.getAlpha() > 0) {
					gc.setBackground(background);
					gc.fillRectangle(0, 0, width, height);
				}
			}
		} catch (SWTException e) {
			if ((getStyle() & SWT.DOUBLE_BUFFERED) == 0) {
				gc.setBackground(background);
				gc.fillRectangle(0, 0, width, height);
			}
		}
	}

	private Point getLineExtent(GC gc, List<TextSegment> segments) {
		StringBuilder sb = new StringBuilder();
		for (TextSegment textSegment : segments) {
			sb.append(textSegment.text);
		}
		return gc.textExtent(sb.toString(), DRAW_FLAGS);
	}

	private int countTrailingSpaces(String text) {
		int count = 0;
		for (int i = text.length() - 1; i >= 0 && text.charAt(i) == ' '; i--) {
			count++;
		}
		return count;
	}

	@Override
	public Set<TextSegment> getLinks() {
		return links;
	}

	@Override
	public Point computeDefaultSize() {
		int lineWidth = 0;
		int lineHeight = 0;

		int leftMargin = link.getLeftMargin();
		int topMargin = link.getTopMargin();

		String displayText = getLinkDisplayText();
		if (!displayText.isEmpty()) {
			String[] lines = displayText.split("\n", -1);
			for (String line : lines) {
				Point textExtent = Drawing.executeOnGC(link, gc -> {
					gc.setFont(link.getFont());
					return gc.textExtent(line, DRAW_FLAGS);
				});
				lineWidth = Math.max(textExtent.x, lineWidth);
				lineHeight = lineHeight + textExtent.y;
			}
		}

		int width = leftMargin + lineWidth + link.getRightMargin();
		int height = topMargin + lineHeight + link.getBottomMargin();
		return new Point(width, height);
	}

}
