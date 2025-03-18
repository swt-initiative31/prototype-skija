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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class LabelRenderer extends ControlRenderer {

	public abstract Point computeDefaultSize();

	/** a string inserted in the middle of text that has been shortened */
	private static final String ELLIPSIS = "..."; //$NON-NLS-1$ // could use
	// the ellipsis glyph on
	// some platforms "\u2026"

	private final Label label;
	protected final LabelState state;


	// The tooltip is used for two purposes - the application can set
	// a tooltip or the tooltip can be used to display the full text when the
	// the text has been truncated due to the label being too short.
	// The appToolTip stores the tooltip set by the application.
	// Control.tooltiptext
	// contains whatever tooltip is currently being displayed.
	private String toolTipText;

	protected LabelRenderer(Label label) {
		super(label);
		this.label = label;
		state = label.getControlState();
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	protected void setDisplayedToolTip(String text) {
		label.setDisplayedToolTip(text);
	}

	public void dispose() {
		toolTipText = null;
	}

	/**
	 * Shorten the given text <code>t</code> so that its length doesn't exceed
	 * the given width. The default implementation replaces characters in the
	 * center of the original string with an ellipsis ("..."). Override if you
	 * need a different strategy.
	 *
	 * @param gc    the gc to use for text measurement
	 * @param t     the text to shorten
	 * @param width the width to shorten the text to, in points
	 * @return the shortened text
	 */
	protected String shortenText(GC gc, String t, int width, int drawFlags) {
		if (t == null) {
			return null;
		}
		int w = gc.textExtent(ELLIPSIS, drawFlags).x;
		if (width <= w) {
			return t;
		}
		int l = t.length();
		int max = l / 2;
		int min = 0;
		int mid = (max + min) / 2 - 1;
		if (mid <= 0) {
			return t;
		}
		TextLayout layout = new TextLayout(label.getDisplay());
		layout.setText(t);
		mid = validateOffset(layout, mid);
		while (min < mid && mid < max) {
			String s1 = t.substring(0, mid);
			String s2 = t.substring(validateOffset(layout, l - mid), l);
			int l1 = gc.textExtent(s1, drawFlags).x;
			int l2 = gc.textExtent(s2, drawFlags).x;
			if (l1 + w + l2 > width) {
				max = mid;
				mid = validateOffset(layout, (max + min) / 2);
			} else if (l1 + w + l2 < width) {
				min = mid;
				mid = validateOffset(layout, (max + min) / 2);
			} else {
				min = max;
			}
		}
		String result = mid == 0
				? t
				: t.substring(0, mid) + ELLIPSIS
				  + t.substring(validateOffset(layout, l - mid), l);
		layout.dispose();
		return result;
	}

	public Color getBackgroundOrDefault() {
		final Color background = state.getBackground();
		return background != null ? background : new Color(240, 240, 240);
	}

	public Color getForegroundOrDefault() {
		final Color foreground = state.getForeground();
		return foreground != null ? foreground : new Color(0, 0, 0);
	}

	private int validateOffset(TextLayout layout, int offset) {
		int nextOffset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
		if (nextOffset != offset) {
			return layout.getPreviousOffset(nextOffset, SWT.MOVEMENT_CLUSTER);
		}
		return offset;
	}
}
