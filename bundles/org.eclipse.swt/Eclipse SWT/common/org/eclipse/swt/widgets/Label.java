/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     Paul Pazderski - Bug 205199: setImage(null) on Label overrides text
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * A Label which supports aligned text and/or an image and different border
 * styles.
 * <p>
 * If there is not enough space a Label uses the following strategy to fit the
 * information into the available space:
 *
 * <pre>
 * 		ignores the indent in left align mode
 * 		ignores the image and the gap
 * 		shortens the text by replacing the center portion of the label with an ellipsis
 * 		shortens the text by removing the center portion of the label
 * </pre>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>LEFT, RIGHT, CENTER, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b>
 * <dd>(NONE)</dd>
 * </dl>
 *
 * <p>
 * This class may be subclassed for the purpose of overriding the default string
 * shortening algorithm that is implemented in method
 * <code>shortenText()</code>.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      CustomControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @see Label#shortenText(GC, String, int)
 */
public class Label extends CustomControl {

	private static final Color DISABLED_COLOR = new Color(160, 160, 160);
	private static final Color SHADOW_IN_COLOR1 = new Color(160, 160, 160);
	private static final Color SHADOW_IN_COLOR2 = new Color(255, 255, 255);
	private static final Color SHADOW_OUT_COLOR1 = new Color(227, 227, 227);
	private static final Color SHADOW_OUT_COLOR2 = new Color(160, 160, 160);

	/** Gap between icon and text */
	private static final int GAP = 5;
	/** Left and right margins */
	private static final int DEFAULT_MARGIN = 3;
	/** a string inserted in the middle of text that has been shortened */
	private static final String ELLIPSIS = "..."; //$NON-NLS-1$ // could use
													// the ellipsis glyph on
													// some platforms "\u2026"
	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align = SWT.LEFT;
	private int leftMargin = DEFAULT_MARGIN;
	private int topMargin = DEFAULT_MARGIN;
	private int rightMargin = DEFAULT_MARGIN;
	private int bottomMargin = DEFAULT_MARGIN;
	/** the current text */
	private String text;
	/** the current icon */
	private Image image;
	// The tooltip is used for two purposes - the application can set
	// a tooltip or the tooltip can be used to display the full text when the
	// the text has been truncated due to the label being too short.
	// The appToolTip stores the tooltip set by the application.
	// Control.tooltiptext
	// contains whatever tooltip is currently being displayed.
	private String appToolTipText;
	private boolean ignoreDispose;

	private Image backgroundImage;
	private Color[] gradientColors;
	private int[] gradientPercents;
	private boolean gradientVertical;
	private Color background;

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
	                                      | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 *
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see SWT#SHADOW_IN
	 * @see SWT#SHADOW_OUT
	 * @see SWT#SHADOW_NONE
	 * @see #getStyle()
	 */
	public Label(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.style |= SWT.DOUBLE_BUFFERED;
		if ((style & (SWT.CENTER | SWT.RIGHT)) == 0) {
			style |= SWT.LEFT;
		}
		if ((style & SWT.CENTER) != 0) {
			align = SWT.CENTER;
		} else if ((style & SWT.RIGHT) != 0) {
			align = SWT.RIGHT;
		} else if ((style & SWT.LEFT) != 0) {
			align = SWT.LEFT;
		}

		final Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.Traverse -> {
				if (event.detail == SWT.TRAVERSE_MNEMONIC) {
					onMnemonic(event);
				}
			}
			case SWT.Dispose -> onDispose(event);
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.Dispose, listener);

		initAccessible();
	}

	/**
	 * Check the style bits to ensure that no invalid styles are applied.
	 */
	private static int checkStyle(int style) {
		if ((style & SWT.BORDER) != 0) {
			style |= SWT.SHADOW_IN;
		}
		int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE
				| SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		style = style & mask;
		style |= SWT.NO_FOCUS | SWT.DOUBLE_BUFFERED;
		return style;
	}

	@Override
	boolean isTabItem() {
		return false;
	}

	@Override
	protected Point computeDefaultSize() {
		int lineWidth = 0;
		int lineHeight = 0;

		int leftMargin = this.leftMargin;
		int imageWidth = 0;
		int imageHeight = 0;
		int gap = 0;
		int topMargin = this.topMargin;

		if (text != null && !text.isEmpty()) {
			Point textExtent = Drawing.getTextExtent(this, text, DRAW_FLAGS);
			lineWidth = textExtent.x;
			lineHeight = textExtent.y;
			if (image != null) {
				gap = Label.GAP;
			}
		}
		if (image != null) {
			Rectangle imgB = image.getBounds();
			imageWidth = imgB.width;
			imageHeight = imgB.height;
		}

		int width = leftMargin + imageWidth + gap + lineWidth
				+ this.rightMargin;
		int height = topMargin + Math.max(lineHeight, imageHeight)
				+ this.bottomMargin;

		return new Point(width, height);
	}

	/**
	 * Draw a rectangle in the given colors.
	 */
	private void drawBevelRect(GC gc, int x, int y, int w, int h,
			Color topleft, Color bottomright) {
		gc.setForeground(bottomright);
		gc.drawLine(x + w, y, x + w, y + h);
		gc.drawLine(x, y + h, x + w, y + h);

		gc.setForeground(topleft);
		gc.drawLine(x, y, x + w - 1, y);
		gc.drawLine(x, y, x, y + h - 1);
	}

	/*
	 * Return the lowercase of the first non-'&' character following an '&'
	 * character in the given string. If there are no '&' characters in the
	 * given string, return '\0'.
	 */
	char _findMnemonic(String string) {
		if (string == null) {
			return '\0';
		}

		int index = 0;
		int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&') {
				index++;
			}
			if (++index >= length) {
				return '\0';
			}
			if (string.charAt(index) != '&') {
				return Character.toLowerCase(string.charAt(index));
			}
			index++;
		} while (index < length);
		return '\0';
	}

	/**
	 * Returns the horizontal alignment. The alignment style (LEFT, CENTER or
	 * RIGHT) is returned.
	 *
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int getAlignment() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return align;
	}

	/**
	 * Return the Label's bottom margin.
	 *
	 * @return the bottom margin of the label
	 *
	 * @since 3.6
	 */
	public int getBottomMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return bottomMargin;
	}

	/**
	 * Return the Label's image or <code>null</code>.
	 *
	 * @return the image of the label or null
	 */
	public Image getImage() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return image;
	}

	/**
	 * Return the Label's left margin.
	 *
	 * @return the left margin of the label
	 *
	 * @since 3.6
	 */
	public int getLeftMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return leftMargin;
	}

	/**
	 * Return the Label's right margin.
	 *
	 * @return the right margin of the label
	 *
	 * @since 3.6
	 */
	public int getRightMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return rightMargin;
	}

	/**
	 * Compute the minimum size.
	 */
	private Point getTotalSize(GC gc, Image image, String text) {
		Point size = new Point(0, 0);

		if (image != null) {
			Rectangle r = image.getBounds();
			size.x += r.width;
			size.y += r.height;
		}

		if (text != null && text.length() > 0) {
			Point e = gc.textExtent(text, DRAW_FLAGS);
			size.x += e.x;
			size.y = Math.max(size.y, e.y);
			if (image != null) {
				size.x += GAP;
			}
		} else {
			size.y = Math.max(size.y, gc.getFontMetrics().getHeight());
		}

		return size;
	}

	@Override
	public int getStyle() {
		int style = super.getStyle();
		switch (align) {
		case SWT.RIGHT -> style |= SWT.RIGHT;
		case SWT.CENTER -> style |= SWT.CENTER;
		case SWT.LEFT -> style |= SWT.LEFT;
		}
		return style;
	}

	/**
	 * Return the Label's text.
	 *
	 * @return the text of the label or null
	 */
	public String getText() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return text != null ? text : "";
	}

	@Override
	public String getToolTipText() {
		checkWidget();
		return appToolTipText;
	}

	/**
	 * Return the Label's top margin.
	 *
	 * @return the top margin of the label
	 *
	 * @since 3.6
	 */
	public int getTopMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return topMargin;
	}

	private void initAccessible() {
		Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = getText();
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}

			@Override
			public void getKeyboardShortcut(AccessibleEvent e) {
				char mnemonic = _findMnemonic(Label.this.text);
				if (mnemonic != '\0') {
					e.result = "Alt+" + mnemonic; //$NON-NLS-1$
				}
			}
		});

		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				e.childID = ACC.CHILDID_SELF;
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle rect = getDisplay().map(getParent(), null,
						getBounds());
				e.x = rect.x;
				e.y = rect.y;
				e.width = rect.width;
				e.height = rect.height;
			}

			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_LABEL;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_READONLY;
			}
		});
	}

	private void onDispose(Event event) {
		/* make this handler run after other dispose listeners */
		if (ignoreDispose) {
			ignoreDispose = false;
			return;
		}
		ignoreDispose = true;
		notifyListeners(event.type, event);
		event.type = SWT.NONE;

		gradientColors = null;
		gradientPercents = null;
		backgroundImage = null;
		text = null;
		image = null;
		appToolTipText = null;
	}

	private void onMnemonic(Event event) {
		char mnemonic = _findMnemonic(text);
		if (mnemonic == '\0') {
			return;
		}
		if (Character.toLowerCase(event.character) != mnemonic) {
			return;
		}
		Composite control = this.getParent();
		while (control != null) {
			Control[] children = control.getChildren();
			int index = 0;
			while (index < children.length) {
				if (children[index] == this) {
					break;
				}
				index++;
			}
			index++;
			if (index < children.length) {
				if (children[index].setFocus()) {
					event.doit = true;
					event.detail = SWT.TRAVERSE_NONE;
				}
			}
			control = control.getParent();
		}
	}

	private void onPaint(Event event) {
		Drawing.drawWithGC(this, event.gc, this::doPaint);
	}

	private void doPaint(GC gc) {
		if ((text == null || text.isEmpty()) && image == null) {
			return;
		}

		Point size = getSize();
		final int width = size.x;
		final int height = size.y;

		boolean shortenText = false;
		String t = text;
		Image img = image;
		int availableWidth = Math.max(0,
				width - (leftMargin + rightMargin));
		Point extent = getTotalSize(gc, img, t);
		if (extent.x > availableWidth) {
			img = null;
			extent = getTotalSize(gc, img, t);
			if (extent.x > availableWidth) {
				shortenText = true;
			}
		}

		String[] lines = text == null ? null : splitString(text);

		// shorten the text
		if (shortenText) {
			extent.x = 0;
			for (int i = 0; i < lines.length; i++) {
				Point e = gc.textExtent(lines[i], DRAW_FLAGS);
				if (e.x > availableWidth) {
					lines[i] = shortenText(gc, lines[i], availableWidth);
					extent.x = Math.max(extent.x,
							getTotalSize(gc, null, lines[i]).x);
				} else {
					extent.x = Math.max(extent.x, e.x);
				}
			}
			if (appToolTipText == null) {
				super.setToolTipText(text);
			}
		} else {
			super.setToolTipText(appToolTipText);
		}

		// determine horizontal position
		int x = leftMargin;
		if (align == SWT.CENTER) {
			x = (width - extent.x) / 2;
		}
		if (align == SWT.RIGHT) {
			x = width - rightMargin - extent.x;
		}

		// draw a background image behind the text
		try {
			if (backgroundImage != null) {
				// draw a background image behind the text
				Rectangle imageRect = backgroundImage.getBounds();
				// tile image to fill space
				gc.setBackground(getBackground());
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
			} else if (gradientColors != null) {
				// draw a gradient behind the text
				final Color oldBackground = gc.getBackground();
				if (gradientColors.length == 1) {
					if (gradientColors[0] != null) {
						gc.setBackground(gradientColors[0]);
					}
					gc.fillRectangle(0, 0, width, height);
				} else {
					final Color oldForeground = gc.getForeground();
					Color lastColor = gradientColors[0];
					if (lastColor == null) {
						lastColor = oldBackground;
					}
					int pos = 0;
					for (int i = 0; i < gradientPercents.length; ++i) {
						gc.setForeground(lastColor);
						lastColor = gradientColors[i + 1];
						if (lastColor == null) {
							lastColor = oldBackground;
						}
						gc.setBackground(lastColor);
						if (gradientVertical) {
							final int gradientHeight = (gradientPercents[i]
									* height / 100) - pos;
							gc.fillGradientRectangle(0, pos, width,
									gradientHeight, true);
							pos += gradientHeight;
						} else {
							final int gradientWidth = (gradientPercents[i]
									* width / 100) - pos;
							gc.fillGradientRectangle(pos, 0, gradientWidth,
									height, false);
							pos += gradientWidth;
						}
					}
					if (gradientVertical && pos < height) {
						gc.setBackground(getBackground());
						gc.fillRectangle(0, pos, width, height - pos);
					}
					if (!gradientVertical && pos < width) {
						gc.setBackground(getBackground());
						gc.fillRectangle(pos, 0, width - pos, height);
					}
					gc.setForeground(oldForeground);
				}
				gc.setBackground(oldBackground);
			} else {
				if (background != null && background.getAlpha() > 0) {
					gc.setBackground(getBackground());
					gc.fillRectangle(0, 0, width, height);
				}
			}
		} catch (SWTException e) {
			if ((getStyle() & SWT.DOUBLE_BUFFERED) == 0) {
				gc.setBackground(getBackground());
				gc.fillRectangle(0, 0, width, height);
			}
		}

		// draw border
		int style = getStyle();
		if ((style & SWT.SHADOW_IN) != 0 || (style & SWT.SHADOW_OUT) != 0) {
			paintBorder(gc, width, height);
		}

		/*
		 * Compute text height and image height. If image height is more than
		 * the text height, draw image starting from top margin. Else draw text
		 * starting from top margin.
		 */
		Rectangle imageRect = null;
		int lineHeight = 0, textHeight = 0, imageHeight = 0;

		if (img != null) {
			imageRect = img.getBounds();
			imageHeight = imageRect.height;
		}
		if (lines != null) {
			lineHeight = gc.getFontMetrics().getHeight();
			textHeight = lines.length * lineHeight;
		}

		int imageY, midPoint;
		if (imageHeight > textHeight) {
			if (topMargin == DEFAULT_MARGIN && bottomMargin == DEFAULT_MARGIN) {
				imageY = 0;
			} else {
				imageY = topMargin;
			}
			midPoint = imageY + imageHeight / 2;
		} else {
			int lineY;
			if (topMargin == DEFAULT_MARGIN && bottomMargin == DEFAULT_MARGIN) {
				lineY = (textHeight - imageHeight) / 2;
			} else {
				lineY = topMargin;
			}
			midPoint = lineY + textHeight / 2;
			imageY = midPoint - imageHeight / 2;
		}

		// draw the image
		if (img != null) {
			gc.drawImage(img, 0, 0, imageRect.width, imageHeight, x, imageY,
					imageRect.width, imageHeight);
			x += imageRect.width + GAP;
			extent.x -= imageRect.width + GAP;
		}

		// draw the text
		// we draw the label at the top.
		int lineY = topMargin;

		if (textHeight < imageHeight) {
			lineY = (imageHeight - textHeight) / 2;
		}

		if (lines != null) {
			gc.setForeground(isEnabled() ? getForeground() : DISABLED_COLOR);
			for (String line : lines) {
				int lineX = x;
				if (lines.length > 1) {
					if (align == SWT.CENTER) {
						int lineWidth = gc.textExtent(line, DRAW_FLAGS).x;
						lineX = x + Math.max(0, (extent.x - lineWidth) / 2);
					}
					if (align == SWT.RIGHT) {
						int lineWidth = gc.textExtent(line, DRAW_FLAGS).x;
						lineX = Math.max(x,
								width - rightMargin - lineWidth);
					}
				}
				gc.drawText(line, lineX, lineY, DRAW_FLAGS);
				lineY += lineHeight;
			}
		}
	}

	/**
	 * Paint the Label's border.
	 */
	private void paintBorder(GC gc, int width, int height) {
		Display disp = getDisplay();

		Color c1 = null;
		Color c2 = null;

		int style = getStyle();
		if ((style & SWT.SHADOW_IN) != 0) {
			c1 = SHADOW_IN_COLOR1;
			c2 = SHADOW_IN_COLOR2;
		}
		if ((style & SWT.SHADOW_OUT) != 0) {
			c1 = SHADOW_OUT_COLOR1;
			c2 = SHADOW_OUT_COLOR2;
		}

		if (c1 != null && c2 != null) {
			gc.setLineWidth(1);
			drawBevelRect(gc, 0, 0, width - 1, height - 1, c1, c2);
		}
	}

	/**
	 * Set the horizontal alignment of the Label. Use the values LEFT, CENTER
	 * and RIGHT to align image and text within the available space.
	 *
	 * @param align
	 *            the alignment style of LEFT, RIGHT or CENTER
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the value of align is not
	 *                one of SWT.LEFT, SWT.RIGHT or SWT.CENTER</li>
	 *                </ul>
	 */
	public void setAlignment(int align) {
		checkWidget();
		if (align != SWT.LEFT && align != SWT.RIGHT && align != SWT.CENTER) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.align != align) {
			this.align = align;
			redraw();
		}
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		// Are these settings the same as before?
		if (backgroundImage == null && gradientColors == null
				&& gradientPercents == null) {
			if (color == null) {
				if (background == null) {
					return;
				}
			} else {
				if (color.equals(background)) {
					return;
				}
			}
		}
		background = color;
		backgroundImage = null;
		gradientColors = null;
		gradientPercents = null;
		redraw();
	}

	/**
	 * Specify a gradient of colours to be drawn in the background of the Label.
	 * <p>
	 * For example, to draw a gradient that varies from dark blue to blue and
	 * then to white and stays white for the right half of the label, use the
	 * following call to setBackground:
	 * </p>
	 *
	 * <pre>
	 * Label.setBackground(
	 * 		new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE),
	 * 				display.getSystemColor(SWT.COLOR_BLUE),
	 * 				display.getSystemColor(SWT.COLOR_WHITE),
	 * 				display.getSystemColor(SWT.COLOR_WHITE)},
	 * 		new int[]{25, 50, 100});
	 * </pre>
	 *
	 * @param colors
	 *            an array of Color that specifies the colors to appear in the
	 *            gradient in order of appearance from left to right; The value
	 *            <code>null</code> clears the background gradient; the value
	 *            <code>null</code> can be used inside the array of Color to
	 *            specify the background color.
	 * @param percents
	 *            an array of integers between 0 and 100 specifying the percent
	 *            of the width of the widget at which the color should change;
	 *            the size of the percents array must be one less than the size
	 *            of the colors array.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the values of colors and
	 *                percents are not consistent</li>
	 *                </ul>
	 */
	public void setBackground(Color[] colors, int[] percents) {
		setBackground(colors, percents, false);
	}

	/**
	 * Specify a gradient of colours to be drawn in the background of the Label.
	 * <p>
	 * For example, to draw a gradient that varies from dark blue to white in
	 * the vertical, direction use the following call to setBackground:
	 * </p>
	 *
	 * <pre>
	 * Label.setBackground(
	 * 		new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE),
	 * 				display.getSystemColor(SWT.COLOR_WHITE)},
	 * 		new int[]{100}, true);
	 * </pre>
	 *
	 * @param colors
	 *            an array of Color that specifies the colors to appear in the
	 *            gradient in order of appearance from left/top to right/bottom;
	 *            The value <code>null</code> clears the background gradient;
	 *            the value <code>null</code> can be used inside the array of
	 *            Color to specify the background color.
	 * @param percents
	 *            an array of integers between 0 and 100 specifying the percent
	 *            of the width/height of the widget at which the color should
	 *            change; the size of the percents array must be one less than
	 *            the size of the colors array.
	 * @param vertical
	 *            indicate the direction of the gradient. True is vertical and
	 *            false is horizontal.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the values of colors and
	 *                percents are not consistent</li>
	 *                </ul>
	 *
	 * @since 3.0
	 */
	public void setBackground(Color[] colors, int[] percents,
			boolean vertical) {
		checkWidget();
		if (colors != null) {
			if (percents == null || percents.length != colors.length - 1) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (getDisplay().getDepth() < 15) {
				// Don't use gradients on low color displays
				colors = new Color[]{colors[colors.length - 1]};
				percents = new int[]{};
			}
			for (int i = 0; i < percents.length; i++) {
				if (percents[i] < 0 || percents[i] > 100) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (i > 0 && percents[i] < percents[i - 1]) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
			}
		}

		// Are these settings the same as before?
		final Color background = getBackground();
		if (backgroundImage == null) {
			if ((gradientColors != null) && (colors != null)
					&& (gradientColors.length == colors.length)) {
				boolean same = false;
				for (int i = 0; i < gradientColors.length; i++) {
					same = (gradientColors[i] == colors[i])
							|| ((gradientColors[i] == null)
									&& (colors[i] == background))
							|| ((gradientColors[i] == background)
									&& (colors[i] == null));
					if (!same) {
						break;
					}
				}
				if (same) {
					for (int i = 0; i < gradientPercents.length; i++) {
						same = gradientPercents[i] == percents[i];
						if (!same) {
							break;
						}
					}
				}
				if (same && this.gradientVertical == vertical)
					return;
			}
		} else {
			backgroundImage = null;
		}
		// Store the new settings
		if (colors == null) {
			gradientColors = null;
			gradientPercents = null;
			gradientVertical = false;
		} else {
			gradientColors = new Color[colors.length];
			for (int i = 0; i < colors.length; ++i)
				gradientColors[i] = (colors[i] != null)
						? colors[i]
						: background;
			gradientPercents = new int[percents.length];
			for (int i = 0; i < percents.length; ++i) {
				gradientPercents[i] = percents[i];
			}
			gradientVertical = vertical;
		}
		// Refresh with the new settings
		redraw();
	}

	/**
	 * Set the image to be drawn in the background of the label.
	 *
	 * @param image
	 *            the image to be drawn in the background
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setBackgroundImage(Image image) {
		checkWidget();
		if (image == backgroundImage) {
			return;
		}
		if (image != null) {
			gradientColors = null;
			gradientPercents = null;
		}
		backgroundImage = image;
		redraw();

	}

	/**
	 * Set the label's bottom margin, in points.
	 *
	 * @param bottomMargin
	 *            the bottom margin of the label, which must be equal to or
	 *            greater than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setBottomMargin(int bottomMargin) {
		checkWidget();
		if (this.bottomMargin == bottomMargin || bottomMargin < 0) {
			return;
		}
		this.bottomMargin = bottomMargin;
		redraw();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		redraw();
	}

	/**
	 * Set the label's Image. The value <code>null</code> clears it.
	 *
	 * @param image
	 *            the image to be displayed in the label or null
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setImage(Image image) {
		checkWidget();
		if (image != this.image) {
			this.image = image;
			redraw();
		}
	}

	/**
	 * Set the label's horizontal left margin, in points.
	 *
	 * @param leftMargin
	 *            the left margin of the label, which must be equal to or
	 *            greater than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setLeftMargin(int leftMargin) {
		checkWidget();
		if (this.leftMargin == leftMargin || leftMargin < 0) {
			return;
		}
		this.leftMargin = leftMargin;
		redraw();
	}

	/**
	 * Set the label's margins, in points.
	 *
	 * @param leftMargin
	 *            the left margin.
	 * @param topMargin
	 *            the top margin.
	 * @param rightMargin
	 *            the right margin.
	 * @param bottomMargin
	 *            the bottom margin.
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setMargins(int leftMargin, int topMargin, int rightMargin,
			int bottomMargin) {
		checkWidget();
		this.leftMargin = Math.max(0, leftMargin);
		this.topMargin = Math.max(0, topMargin);
		this.rightMargin = Math.max(0, rightMargin);
		this.bottomMargin = Math.max(0, bottomMargin);
		redraw();
	}

	/**
	 * Set the label's right margin, in points.
	 *
	 * @param rightMargin
	 *            the right margin of the label, which must be equal to or
	 *            greater than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setRightMargin(int rightMargin) {
		checkWidget();
		if (this.rightMargin == rightMargin || rightMargin < 0) {
			return;
		}
		this.rightMargin = rightMargin;
		redraw();
	}

	/**
	 * Set the label's text. The value <code>null</code> clears it.
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, focus is assigned to the control that follows the label. On
	 * most platforms, the mnemonic appears underlined but may be emphasised in
	 * a platform specific manner. The mnemonic indicator character '&amp;' can
	 * be escaped by doubling it in the string, causing a single '&amp;' to be
	 * displayed.
	 * </p>
	 * <p>
	 * Note: If control characters like '\n', '\t' etc. are used in the string,
	 * then the behavior is platform dependent.
	 * </p>
	 *
	 * @param text
	 *            the text to be displayed in the label or null
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setText(String text) {
		checkWidget();
		if (text == null) {
			text = ""; //$NON-NLS-1$
		}
		if (!text.equals(this.text)) {
			this.text = text;
			redraw();
		}
	}

	@Override
	public void setToolTipText(String string) {
		super.setToolTipText(string);
		appToolTipText = super.getToolTipText();
	}

	/**
	 * Set the label's top margin, in points.
	 *
	 * @param topMargin
	 *            the top margin of the label, which must be equal to or greater
	 *            than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setTopMargin(int topMargin) {
		checkWidget();
		if (this.topMargin == topMargin || topMargin < 0) {
			return;
		}
		this.topMargin = topMargin;
		redraw();
	}

	/**
	 * Shorten the given text <code>t</code> so that its length doesn't exceed
	 * the given width. The default implementation replaces characters in the
	 * center of the original string with an ellipsis ("..."). Override if you
	 * need a different strategy.
	 *
	 * @param gc
	 *            the gc to use for text measurement
	 * @param t
	 *            the text to shorten
	 * @param width
	 *            the width to shorten the text to, in points
	 * @return the shortened text
	 */
	protected String shortenText(GC gc, String t, int width) {
		if (t == null) {
			return null;
		}
		int w = gc.textExtent(ELLIPSIS, DRAW_FLAGS).x;
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
		TextLayout layout = new TextLayout(getDisplay());
		layout.setText(t);
		mid = validateOffset(layout, mid);
		while (min < mid && mid < max) {
			String s1 = t.substring(0, mid);
			String s2 = t.substring(validateOffset(layout, l - mid), l);
			int l1 = gc.textExtent(s1, DRAW_FLAGS).x;
			int l2 = gc.textExtent(s2, DRAW_FLAGS).x;
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

	int validateOffset(TextLayout layout, int offset) {
		int nextOffset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
		if (nextOffset != offset) {
			return layout.getPreviousOffset(nextOffset, SWT.MOVEMENT_CLUSTER);
		}
		return offset;
	}

	private String[] splitString(String text) {
		String[] lines = new String[1];
		int start = 0, pos;
		do {
			pos = text.indexOf('\n', start);
			if (pos == -1) {
				lines[lines.length - 1] = text.substring(start);
			} else {
				boolean crlf = (pos > 0) && (text.charAt(pos - 1) == '\r');
				lines[lines.length - 1] = text.substring(start,
						pos - (crlf ? 1 : 0));
				start = pos + 1;
				String[] newLines = new String[lines.length + 1];
				System.arraycopy(lines, 0, newLines, 0, lines.length);
				lines = newLines;
			}
		} while (pos != -1);
		return lines;
	}
}
