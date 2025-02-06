package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWButtonUI extends LWAbstractButtonUI {

	private static final int LEFT_MARGIN = 2;
	private static final int RIGHT_MARGIN = 2;
	private static final int TOP_MARGIN = 0;
	private static final int BOTTOM_MARGIN = 0;
	private static final int SPACING = 4;
	private static final int BOX_SIZE = 12;
	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
	                                      | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	private final LWButton button;

	public LWButtonUI(LWButton button) {
		this.button = button;
	}

	public Point getPreferredSize(IMeasureContext mc) {
		final String text = button.getText();
		final Image image = button.getImage();
		final int style = button.getStyle();

		if (isArrowButton()) {
			int borderWidth = hasBorder() ? 8 : 0;
			int width = 14 + borderWidth;
			int height = width;

			return new Point(width, height);
		}

		int textWidth = 0;
		int textHeight = 0;
		int boxSpace = 0;
		if ((style & (SWT.PUSH | SWT.TOGGLE)) == 0) {
			boxSpace = BOX_SIZE + SPACING;
		}
		if (text != null && !text.isEmpty()) {
			Point textExtent = mc.textExtent(text, DRAW_FLAGS);
			textWidth = textExtent.x + 1;
			textHeight = textExtent.y;
		}
		int imageSpace = 0;
		int imageHeight = 0;
		if (image != null) {
			Rectangle imgB = image.getBounds();
			imageHeight = imgB.height;
			imageSpace = imgB.width;
			if (text != null && !text.isEmpty()) {
				imageSpace += SPACING;
			}
		}

		int width = LEFT_MARGIN + boxSpace + imageSpace + textWidth + 1
		            + RIGHT_MARGIN;
		int height = TOP_MARGIN
		             + Math.max(boxSpace, Math.max(textHeight, imageHeight))
		             + BOTTOM_MARGIN;

		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
			width += 12;
			height += 10;
		}
		return new Point(width, height);
	}

	public void paint(GC gc, IColorProvider colorProvider) {
		final int width = button.getWidth();
		final int height = button.getHeight();
		final String text = button.getText();
		final int style = button.getStyle();
		final Image image = button.getImage();

		boolean isRightAligned = (style & SWT.RIGHT) != 0;
		boolean isCentered = (style & SWT.CENTER) != 0;
		boolean isPushOrToggleButton = (style & (SWT.PUSH | SWT.TOGGLE)) != 0;
		int initialAntiAlias = gc.getAntialias();

		int boxSpace = 0;
		// Draw check box / radio box / push button border
		if (isPushOrToggleButton) {
			drawPushButton(gc, 0, 0, width - 1, height - 1, colorProvider);
		} else {
			boxSpace = BOX_SIZE + SPACING;
			int boxLeftOffset = LEFT_MARGIN;
			int boxTopOffset = (height - 1 - BOX_SIZE) / 2;
		}

		gc.setAntialias(initialAntiAlias);
		gc.setAdvanced(false);

		// Calculate area for button content (image + text)
		int horizontalSpaceForContent = width - RIGHT_MARGIN - LEFT_MARGIN - boxSpace;
		int textWidth = 0;
		int textHeight = 0;
		if (text != null && !text.isEmpty()) {
			Point textExtent = gc.textExtent(text, DRAW_FLAGS);
			textWidth = textExtent.x;
			textHeight = textExtent.y;
		}
		int imageSpace = 0;
		int imageWidth = 0;
		int imageHeight = 0;
		if (image != null) {
			Rectangle imgB = image.getBounds();
			imageWidth = imgB.width;
			imageHeight = imgB.height;
			imageSpace = imageWidth;
			if (text != null && !text.isEmpty()) {
				imageSpace += SPACING;
			}
		}
		Rectangle contentArea = new Rectangle(LEFT_MARGIN + boxSpace,
		                                      TOP_MARGIN, imageSpace + textWidth,
		                                      height - TOP_MARGIN - BOTTOM_MARGIN);
		if (isRightAligned) {
			contentArea.x += horizontalSpaceForContent - contentArea.width;
		} else if (isCentered) {
			contentArea.x += (horizontalSpaceForContent - contentArea.width)
			                 / 2;
		}

		// Draw image
		if (image != null) {
			int imageTopOffset = (height - imageHeight) / 2;
			int imageLeftOffset = contentArea.x;
			if (button.isEnabled()) {
				if (button.isPressed()) {
					imageTopOffset++;
					imageLeftOffset++;
				}

				gc.drawImage(image, imageLeftOffset, imageTopOffset);
			}
			else {
				gc.drawImage(button.getDisabledImage(), imageLeftOffset, imageTopOffset);
			}
		}

		// Draw text
		if (text != null && !text.isEmpty()) {
			int textTopOffset = (height - 1 - textHeight) / 2;
			int textLeftOffset = contentArea.x + imageSpace;
			if (button.isEnabled()) {
				gc.setForeground(colorProvider.getColor(KEY_TEXT));

				if (button.isPressed()) {
					textTopOffset++;
					textLeftOffset++;
				}
			} else {
				gc.setForeground(colorProvider.getColor(KEY_DISABLE));
			}
			gc.drawText(text, textLeftOffset, textTopOffset, DRAW_FLAGS);
		}
		if (button.hasFocus()) {
			if (((style & SWT.RADIO) | (style & SWT.CHECK)) != 0) {
				int textTopOffset = (height - 1 - textHeight) / 2;
				int textLeftOffset = contentArea.x + imageSpace;
				gc.drawFocus(textLeftOffset - 2, textTopOffset, textWidth + 4,
				             textHeight);
			} else {
				gc.drawFocus(3, 3, width - 7, height - 7);
			}
		}

		if (isArrowButton()) {
			Color bg2 = gc.getBackground();

			gc.setBackground(colorProvider.getColor(KEY_ARROW));

			int centerHeight = height / 2;
			int centerWidth = width / 2;
			if (hasBorder()) {
				// border ruins center position...
				centerHeight -= 2;
				centerWidth -= 2;
			}

			if (button.isEnabled() && button.isPressed()) {
				centerHeight++;
				centerWidth++;
			}

			// TODO: in the next version use a bezier path

			int[] curve = null;

			if ((style & SWT.DOWN) != 0) {
				curve = new int[]{centerWidth, centerHeight + 5,
				                  centerWidth - 5, centerHeight - 5, centerWidth + 5,
				                  centerHeight - 5};

			} else if ((style & SWT.LEFT) != 0) {
				curve = new int[]{centerWidth - 5, centerHeight,
				                  centerWidth + 5, centerHeight + 5, centerWidth + 5,
				                  centerHeight - 5};

			} else if ((style & SWT.RIGHT) != 0) {
				curve = new int[]{centerWidth + 5, centerHeight,
				                  centerWidth - 5, centerHeight - 5, centerWidth - 5,
				                  centerHeight + 5};

			} else {
				curve = new int[]{centerWidth, centerHeight - 5,
				                  centerWidth - 5, centerHeight + 5, centerWidth + 5,
				                  centerHeight + 5};
			}

			gc.fillPolygon(curve);
			gc.setBackground(bg2);
		}
	}

	private boolean hasBorder() {
		return (button.getStyle() & SWT.BORDER) != 0;
	}

	private void drawPushButton(GC gc, int x, int y, int w, int h, IColorProvider colorProvider) {
		final int style = button.getStyle();
		if (button.isEnabled()) {
			if ((style & SWT.TOGGLE) != 0 && button.isSelected()) {
				gc.setBackground(colorProvider.getColor(KEY_TOGGLE));
			} else if (button.isHovered()) {
				gc.setBackground(colorProvider.getColor(KEY_HOVER));
			} else {
				gc.setBackground(colorProvider.getColor(KEY_BUTTON));
			}
			gc.fillRoundRectangle(x, y, w, h, 6, 6);
		}

		if (button.isEnabled()) {
			if ((style & SWT.TOGGLE) != 0 && button.isSelected() || button.isHovered()) {
				gc.setForeground(colorProvider.getColor(KEY_SELECTION));
			} else {
				gc.setForeground(colorProvider.getColor(KEY_OUTLINE));
			}
		} else {
			gc.setForeground(colorProvider.getColor(KEY_DISABLE));
		}

		// if the button has focus, the border also changes the color
		Color fg = gc.getForeground();
		if (button.hasFocus()) {
			gc.setForeground(colorProvider.getColor(KEY_SELECTION));
		}
		gc.drawRoundRectangle(x, y, w - 1, h - 1, 6, 6);
		gc.setForeground(fg);
	}

	private boolean isArrowButton() {
		return (button.getStyle() & SWT.ARROW) != 0;
	}
}
