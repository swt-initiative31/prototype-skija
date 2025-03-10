package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class BasicLinkRenderer extends LinkRenderer {

	private static final Color DISABLED_COLOR = new Color(160, 160, 160);
	private static final Color LINK_COLOR = new Color(0, 102, 204);

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	private final Set<TextSegment> links = new HashSet<>();

	private TextSegment prevHoverLink;

	public BasicLinkRenderer(Link link) {
		super(link);
	}

	@Override
	public Point computeDefaultSize() {
		final String text = getText();
		int lineWidth = 0;
		int lineHeight = 0;

		int leftMargin = getLeftMargin();
		int topMargin = getTopMargin();

		if (!text.isEmpty()) {
			String[] lines = text.split("\n");
			final Point size = measure(gc -> {
				int width = 0;
				int height = 0;
				for (String line : lines) {
					Point textExtent = getLineExtent(gc, parseTextSegments(line));
					width = Math.max(textExtent.x, width);
					height = textExtent.y;
				}
				return new Point(width, height);
			});
			lineWidth = size.x;
			lineHeight = size.y;
		}

		int width = leftMargin + lineWidth + getRightMargin();

		// Height must be multiple of lines.
		int newlineCount = (int) text.chars().filter(ch -> ch == '\n').count();
		if (text.contains("\n")) {
			lineHeight *= (newlineCount + 1);
		}

		int height = topMargin + lineHeight + getBottomMargin();
		return new Point(width, height);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final String text = getText();
		if (text.isEmpty()) {
			return;
		}

//		gc.setBackground(getBackground());
//		gc.setClipping(new Rectangle(0, 0, rect.width, rect.height));
		gc.setForeground(getForeground());

		drawBackground(gc, width, height);

		Color linkColor = getLinkForeground();

		links.clear();

		int x = getLeftMargin();
		int rightMargin = getRightMargin();
		int lineY = getTopMargin();
		final int align = getAlign();

		for (String line : parsedText.keySet()) {
			List<TextSegment> segments = parsedText.get(line);
			int lineX = x;
			if (align == SWT.CENTER) {
				int lineWidth = getLineExtent(gc, segments).x;
				if (width > lineWidth) {
					lineX = Math.max(x, (width - lineWidth) / 2);
				}
			}
			if (align == SWT.RIGHT) {
				int lineWidth = getLineExtent(gc, segments).x;
				lineX = Math.max(x, width - rightMargin - lineWidth);
			}

			Point baseExtent = gc.textExtent("a", DRAW_FLAGS);

			for (TextSegment segment : segments) {
				Point extent = gc.textExtent(segment.text, DRAW_FLAGS);
				int noOfTrailSpaces = countTrailingSpaces(segment.text);
				if (noOfTrailSpaces > 0) {
					extent.x = extent.x + noOfTrailSpaces * baseExtent.x;
				}

				if (isEnabled()) {
					gc.setForeground(segment.isLink ? linkColor : getForeground());
				} else {
					gc.setForeground(DISABLED_COLOR);
				}
				gc.drawText(segment.text, lineX, lineY, DRAW_FLAGS);

				if (segment.isLink) {
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

	@Override
	public void onMouseUp(int x, int y, Consumer<String> consumer) {
		for (TextSegment link : links) {
			if (link.rect.contains(x, y)) {
				consumer.accept(link.linkData != null ? link.linkData : link.text);
				break;
			}
		}
	}

	@Override
	public boolean onMouseOver(int x, int y) {
		if (prevHoverLink != null && prevHoverLink.rect.contains(x, y)) {
			return true;
		}

		for (TextSegment link : links) {
			if (link.rect.contains(x, y)) {
				prevHoverLink = link;
				return true;
			}
		}
		return false;
	}

	@Override
	protected Color getLinkForeground() {
		final Color linkForeground = super.getLinkForeground();
		return linkForeground != null ? linkForeground : LINK_COLOR;
	}

	private void drawBackground(GC gc, int width, int height) {
		final Color background = getBackground();
		// draw a background image behind the text
		try {
			final Image backgroundImage = getBackgroundImage();
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
					gc.setBackground(getBackground());
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

	private int countTrailingSpaces(String text) {
		int count = 0;
		for (int i = text.length() - 1; i >= 0 && text.charAt(i) == ' '; i--) {
			count++;
		}
		return count;
	}

	private Point getLineExtent(GC gc, List<TextSegment> segments) {
		StringBuilder sb = new StringBuilder();
		for (TextSegment textSegment : segments) {
			sb.append(textSegment.text);
		}
		return gc.textExtent(sb.toString(), DRAW_FLAGS);
	}
}
