package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWCheckboxUI extends LWAbstractButtonUI {

	private static final int LEFT_MARGIN = 2;
	private static final int RIGHT_MARGIN = 2;
	private static final int TOP_MARGIN = 0;
	private static final int BOTTOM_MARGIN = 0;
	private static final int SPACING = 4;
	private static final int BOX_SIZE = 12;
	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
	                                      | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	private final LWCheckbox button;

	public LWCheckboxUI(LWCheckbox button) {
		this.button = button;
	}

	public Point getPreferredSize(IMeasureContext mc) {
		final String text = button.getText();

		int textWidth = 0;
		int textHeight = 0;
		int boxSpace = BOX_SIZE + SPACING;
		if (text != null && !text.isEmpty()) {
			Point textExtent = mc.textExtent(text, DRAW_FLAGS);
			textWidth = textExtent.x + 1;
			textHeight = textExtent.y;
		}
		int imageSpace = 0;
		int imageHeight = 0;

		int width = LEFT_MARGIN + boxSpace + imageSpace + textWidth + 1 + RIGHT_MARGIN;
		int height = TOP_MARGIN
		             + Math.max(boxSpace, Math.max(textHeight, imageHeight))
		             + BOTTOM_MARGIN;
		return new Point(width, height);
	}

	public void paint(GC gc, IColorProvider colorProvider) {
		final int width = button.getWidth();
		final int height = button.getHeight();
		final String text = button.getText();

		boolean isRightAligned = false;
		boolean isCentered = false;
		boolean isPushOrToggleButton = false;
		int initialAntiAlias = gc.getAntialias();

		int boxSpace = BOX_SIZE + SPACING;
		int boxLeftOffset = LEFT_MARGIN;
		int boxTopOffset = (height - 1 - BOX_SIZE) / 2;
		drawCheckbox(gc, boxLeftOffset, boxTopOffset, colorProvider);

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
		Rectangle contentArea = new Rectangle(LEFT_MARGIN + boxSpace,
		                                      TOP_MARGIN, imageSpace + textWidth,
		                                      height - TOP_MARGIN - BOTTOM_MARGIN);

		// Draw text
		if (text != null && !text.isEmpty()) {
			if (button.isEnabled()) {
				gc.setForeground(colorProvider.getColor(KEY_TEXT));
			} else {
				gc.setForeground(colorProvider.getColor(KEY_DISABLE));
			}
			int textTopOffset = (height - 1 - textHeight) / 2;
			int textLeftOffset = contentArea.x + imageSpace;
			gc.drawText(text, textLeftOffset, textTopOffset, DRAW_FLAGS);
		}
		if (button.hasFocus()) {
			int textTopOffset = (height - 1 - textHeight) / 2;
			int textLeftOffset = contentArea.x + imageSpace;
			gc.drawFocus(textLeftOffset - 2, textTopOffset, textWidth + 4, textHeight);
		}
	}

	private void drawCheckbox(GC gc, int x, int y, IColorProvider colorProvider) {
		if (button.isSelected()) {
			if (button.isGrayed()) {
				gc.setBackground(colorProvider.getColor(KEY_TRISTATE));
			} else {
				gc.setBackground(colorProvider.getColor(KEY_SELECTION));
			}
			int partialBoxBorder = 2;
			gc.fillRoundRectangle(x + partialBoxBorder, y + partialBoxBorder,
			                      BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1,
			                      BOX_SIZE / 4 - partialBoxBorder / 2,
			                      BOX_SIZE / 4 - partialBoxBorder / 2);

		}
		if (button.isHovered()) {
			gc.setBackground(colorProvider.getColor(KEY_HOVER));
			int partialBoxBorder = button.isSelected() ? 4 : 0;
			gc.fillRoundRectangle(x + partialBoxBorder, y + partialBoxBorder,
			                      BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1,
			                      BOX_SIZE / 4 - partialBoxBorder / 2,
			                      BOX_SIZE / 4 - partialBoxBorder / 2);
		}
		gc.drawRoundRectangle(x, y, BOX_SIZE, BOX_SIZE, 4, 4);
	}
}
