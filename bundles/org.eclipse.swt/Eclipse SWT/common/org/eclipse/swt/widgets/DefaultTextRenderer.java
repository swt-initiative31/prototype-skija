package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

public class DefaultTextRenderer extends TextRenderer {

	static final String COLOR_FOREGROUND = "text.foreground"; //$NON-NLS-1$
	static final String COLOR_BACKGROUND = "text.background"; //$NON-NLS-1$
	static final String COLOR_BORDER = "text.border"; //$NON-NLS-1$
	static final String COLOR_BACKGROUND_READONLY = "text.background.readonly"; //$NON-NLS-1$
	static final String COLOR_SELECTION_BACKGROUND = "text.selection.background"; //$NON-NLS-1$
	static final String COLOR_SELECTION_FOREGROUND = "text.selection.foreground"; //$NON-NLS-1$

	private static final int HORIZONTAL_MARGIN = 2;

	public DefaultTextRenderer(Text text, TextModel model) {
		super(text, model);
	}

	@Override
	protected Point computeDefaultSize() {
		Point size = computeTextSize();
		final int borderSize = text.getBorderWidth();
		size.x += 2 * (borderSize + HORIZONTAL_MARGIN);
		size.y += 2 * borderSize;
		return size;
	}

	@Override
	protected Point computeTextSize() {
		return Drawing.measure(text, gc -> {
			gc.setFont(text.getFont());
			int style = text.getStyle();
			int width = 0;
			int height;
			if ((style & SWT.SINGLE) != 0) {
				String str = model.getLines()[0];
				Point size = gc.textExtent(str);
				if (str.length() > 0) {
					width = (int)Math.ceil(size.x);
				}
				height = (int)Math.ceil(size.y);
			} else {
				Point size = null;
				for (String line : model.getLines()) {
					size = gc.textExtent(line);
					width = Math.max(width, size.x);
				}
				height = size.y * model.getLineCount();
				final ScrollBar horizontalBar = text.getHorizontalBar();
				if (horizontalBar != null) {
					height += horizontalBar.getSize().y;
				}
				final ScrollBar verticalBar = text.getVerticalBar();
				if (verticalBar != null) {
					width += verticalBar.getSize().x;
				}
			}
			return new Point(width, height);
		});
	}

	@Override
	protected int getBorderWidth() {
		int style = text.getStyle();
		if ((style & SWT.BORDER) != 0) {
			return 2;
		}
		return 0;
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final boolean enabled = text.isEnabled();
		if (!enabled) {
			gc.setForeground(getColor(COLOR_DISABLED));
		}

		final int style = text.getStyle();
		final boolean editable = text.getEditable();
		if (!enabled || !editable) {
			gc.setBackground(getColor(COLOR_BACKGROUND_READONLY));
		}

		final Rectangle visibleArea = getVisibleArea();
		final Rectangle clientArea = text.getClientArea();

		drawBackground(gc, clientArea, style, editable && enabled);
		drawText(visibleArea, clientArea, style, gc);
		if (text.isFocusControl()) {
			drawSelection(gc, visibleArea);
			drawCaret(gc, visibleArea);
		}
	}

	@Override
	public Color getDefaultBackground() {
		return getColor(COLOR_BACKGROUND);
	}

	@Override
	public Color getDefaultForeground() {
		return getColor(COLOR_FOREGROUND);
	}

	@Override
	protected Rectangle getVisibleArea() {
		Rectangle clientArea = text.getClientArea();

		ScrollBar horizontalBar = text.getHorizontalBar();
		ScrollBar verticalBar = text.getVerticalBar();

		int hOffset = (horizontalBar != null) ? horizontalBar.getSelection() : 0;
		int vOffset = (verticalBar != null) ? verticalBar.getSelection() : 0;

		clientArea.x += hOffset;
		clientArea.y += vOffset;

		return clientArea;
	}

	@Override
	protected Point getLocationByTextLocation(TextLocation textLocation, GC gc) {
		String completeText = model.getLines()[textLocation.line];
		String beforeSelection = completeText.substring(0, textLocation.column);
		gc.setFont(text.getFont());
		Point completeTextExtent = gc.textExtent(completeText);
		Point textExtent = gc.textExtent(beforeSelection);
		int x;
		Rectangle clientArea = text.getClientArea();
		final int style = text.getStyle();
		if ((style & SWT.CENTER) != 0) {
			x = (clientArea.width - completeTextExtent.x) / 2 - 1 - HORIZONTAL_MARGIN;
		} else if ((style & SWT.RIGHT) != 0) {
			x = clientArea.width - completeTextExtent.x;
		} else { // ((style & SWT.LEFT) != 0)
			x = HORIZONTAL_MARGIN;
		}
		x += textExtent.x;
		int y = textLocation.line * textExtent.y;
		if ((style & SWT.BORDER) != 0) {
			final int borderWidth = text.getBorderWidth();
			x += borderWidth;
			y += borderWidth;
		}
		return new Point(x, y);
	}

	@Override
	protected int getLineHeight(GC gc) {
		String str = model.getLines()[0];
		return gc.textExtent(str).y;
	}

	private void drawBackground(GC gc, Rectangle clientArea, int style, boolean editable) {
		int height = clientArea.height;
		final boolean drawLine = (style & SWT.BORDER) != 0 && editable;
		if (drawLine) {
			height--;
		}
		gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width, height);
		if (drawLine) {
			Color prevBackground = gc.getBackground();
			gc.setBackground(getColor(text.isFocusControl() ? COLOR_SELECTION_BACKGROUND : COLOR_BORDER));
			gc.fillRectangle(clientArea.x, clientArea.y + height, clientArea.width, 1);
			gc.setBackground(prevBackground);
		}
	}

	private void drawText(Rectangle visibleArea, Rectangle clientArea, int style, GC gc) {
		String[] lines = model.getLines();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			drawTextLine(line, i, visibleArea, clientArea, style, gc);
		}
	}

	private void drawTextLine(String text, int lineNumber, Rectangle visibleArea,
							  Rectangle clientArea, int style, GC gc) {
		Point completeTextExtent = gc.textExtent(text);
		int x = HORIZONTAL_MARGIN;
		if ((style & SWT.CENTER) != 0) {
			x = (clientArea.width - completeTextExtent.x) / 2;
		} else if ((style & SWT.RIGHT) != 0) {
			x = clientArea.width - completeTextExtent.x - 1 - HORIZONTAL_MARGIN;
		}
		x -= visibleArea.x;
		int y = lineNumber * completeTextExtent.y - visibleArea.y;
		if ((style & SWT.BORDER) != 0) {
			final int borderWidth = this.text.getBorderWidth();
			x += borderWidth;
			y += borderWidth;
		}
		gc.drawText(text, x, y, true);
	}

	private void drawSelection(GC gc, Rectangle visibleArea) {
		int textLength = model.getText().length();
		int start = Math.min(Math.max(model.getSelectionStart(), 0), textLength);
		int end = Math.min(Math.max(model.getSelectionEnd(), 0), textLength);

		if (model.getSelectionStart() >= 0) {
			TextLocation startLocation = model.getLocation(start);
			TextLocation endLocation = model.getLocation(end);
			String[] textLines = model.getLines();

			Color oldForeground = gc.getForeground();
			Color oldBackground = gc.getBackground();
			gc.setForeground(getColor(COLOR_SELECTION_FOREGROUND));
			gc.setBackground(getColor(COLOR_SELECTION_BACKGROUND));
			for (int i = startLocation.line; i <= endLocation.line; i++) {
				TextLocation location = new TextLocation(i, 0);
				String text = textLines[i];
				if (i == endLocation.line) {
					text = text.substring(0, endLocation.column);
				}
				if (i == startLocation.line) {
					location.column = startLocation.column;
					text = text.substring(startLocation.column);
				}
				Point locationPixel = getLocationByTextLocation(location, gc);
				gc.drawText(text, locationPixel.x - visibleArea.x, locationPixel.y - visibleArea.y, false);
			}
			gc.setForeground(oldForeground);
			gc.setBackground(oldBackground);
		}
	}

	private void drawCaret(GC gc, Rectangle visibleArea) {
		int caretOffset = model.getCaretOffset();
		final TextCaret caret = text.getCaret();
		if (caretOffset >= 0) {
			int offset = model.getCaretOffset();
			TextLocation textLocation = model.getLocation(offset);
			Point caretLocation = getLocationByTextLocation(textLocation, gc);
			int x = caretLocation.x - visibleArea.x;
			int y = caretLocation.y - visibleArea.y;
			caret.setBounds(x, y, 1, getLineHeight(gc));
		}

		caret.paint(gc);
	}
}
