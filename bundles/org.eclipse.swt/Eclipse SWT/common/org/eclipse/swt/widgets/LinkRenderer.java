/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LinkRenderer extends ControlRenderer {

	public abstract void onMouseUp(int x, int y, Consumer<String> consumer);

	public abstract boolean onMouseOver(int x, int y);

	protected final Map<String, List<TextSegment>> parsedText = new HashMap<>();
	private final Link link;

	private Image backgroundImage;
	private Color background;
	private Color linkColor;

	protected LinkRenderer(Link link) {
		super(link);
		this.link = link;
	}

	public String getParsedText() {
		StringBuilder sb = new StringBuilder();
		String[] lines = getText().split("\n");
		for (String line : lines) {
			List<TextSegment> segments = parseTextSegments(line);
			for (TextSegment segment : segments) {
				sb.append(segment.text);
			}
		}
		return sb.toString();
	}

	public void setText(String text) {
		parsedText.clear();
		String[] lines = text.split("\n");
		for (String line : lines) {
			parsedText.put(line, parseTextSegments(line));
		}
	}

	protected final String getText() {
		return link.getText();
	}

	protected final int getAlign() {
		return link.getAlignment();
	}

	protected final int getLeftMargin() {
		return link.getLeftMargin();
	}

	protected final int getTopMargin() {
		return link.getTopMargin();
	}

	protected final int getRightMargin() {
		return link.getRightMargin();
	}

	protected final int getBottomMargin() {
		return link.getBottomMargin();
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image image) {
		backgroundImage = image;
	}

	protected Color getForeground() {
		return link.getForeground();
	}

	protected Color getBackground() {
		return background;
	}

	public void setBackground(Color color) {
		background = color;
		backgroundImage = null;
	}

	protected Color getLinkForeground() {
		return linkColor;
	}

	public void setLinkForeground(Color color) {
		this.linkColor = color;
	}

	public void dispose() {
		backgroundImage = null;
	}

	protected java.util.List<TextSegment> parseTextSegments(String input) {
		List<TextSegment> segments = new ArrayList<>();
		Pattern pattern = Pattern.compile("(.*?)<a(?: href=\"(.*?)\")?>(.*?)</a>([\\s.,]*)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);

		int lastEnd = 0;

		while (matcher.find()) {
			// Extract normal text before <a> tag
			String normalText = matcher.group(1);
			if (!normalText.isEmpty()) {
				segments.add(new TextSegment(normalText, false, null, null));
			}

			// Extract href attribute (if present) and linked text inside <a> tag
			String href = matcher.group(2); // href="..." value (can be null)
			String linkText = matcher.group(3); // The actual clickable text
			segments.add(new TextSegment(linkText, true, href, null));

			// Capture trailing spaces and punctuation (important for handling ", "
			// correctly)
			String trailingText = matcher.group(4);
			if (!trailingText.isEmpty()) {
				segments.add(new TextSegment(trailingText, false, null, null));
			}

			lastEnd = matcher.end();
		}

		// Add any remaining text after the last <a> tag
		if (lastEnd < input.length()) {
			String remainingText = input.substring(lastEnd);
			segments.add(new TextSegment(remainingText, false, null, null));
		}

		return segments;
	}

	protected static class TextSegment {
		String text, linkData;
		boolean isLink;
		Rectangle rect;

		TextSegment(String text, boolean isLink, String linkData, Rectangle rect) {
			this.text = text;
			this.isLink = isLink;
			this.linkData = linkData;
			this.rect = rect;
		}
	}
}
