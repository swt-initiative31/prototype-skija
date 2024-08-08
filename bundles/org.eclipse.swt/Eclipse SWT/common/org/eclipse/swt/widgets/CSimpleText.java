package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class CSimpleText extends Canvas {

	public static final String DELIMITER = "\n";

	private static final int LINE_HEIGTH = 16;

	private TextContent content;
	private String message;

	private int selectionStart, selectionEnd;
	private int caretOffset;
//	Caret caret;

	public CSimpleText(Composite parent, int style) {
		super(parent, style);
		content = new TextContent();
		message = "";

		caret = new Caret(this, SWT.NONE);

		addDisposeListener(e -> CSimpleText.this.widgetDisposed(e));
		addPaintListener(e -> CSimpleText.this.paintControl(e));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				CSimpleText.this.keyPressed(e);
			}
		});

		ScrollBar horizontalBar = getHorizontalBar();
		if (horizontalBar != null) {
			horizontalBar.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					CSimpleText.this.scrollBarSelectionChanged(e);
				}
			});
		}
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null) {
			verticalBar.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					CSimpleText.this.scrollBarSelectionChanged(e);
				}
			});
		}
	}

	public CSimpleText() {
		// TODO DELETE ME
	}

	private void keyPressed(KeyEvent e) {
		TextLocation caretLocation;

		boolean updateSelection = (e.stateMask & SWT.SHIFT) != 0;

		switch (e.keyCode) {
		case SWT.ARROW_LEFT:
			moveCaretTo(caretOffset - 1, updateSelection);
			break;
		case SWT.ARROW_RIGHT:
			moveCaretTo(caretOffset + 1, updateSelection);
			break;
		case SWT.ARROW_UP:
			caretLocation = content.getLocation(caretOffset);
			if (caretLocation.line <= 0)
				break;
			caretLocation.line--;
			moveCaretTo(content.getOffset(caretLocation), updateSelection);
			break;
		case SWT.ARROW_DOWN:
			caretLocation = content.getLocation(caretOffset);
			if (caretLocation.line > content.getLines().length - 1)
				break;
			caretLocation.line++;
			moveCaretTo(content.getOffset(caretLocation), updateSelection);
			break;
		case SWT.BS:
			content.removeCharacter(caretOffset - 1);
			moveCaretTo(caretOffset - 1, false);
			removeSelection();
			break;
		case SWT.DEL:
			content.removeCharacter(caretOffset);
			removeSelection();
			break;
		default:
			content.append(e.character);
			moveCaretTo(caretOffset + 1, false);
			removeSelection();
		}
		redraw();
	}

	private int removeSelection() {
		return selectionStart = selectionEnd = -1;
	}

	private void moveCaretTo(int newOffset, boolean updateSelection) {
		if (newOffset < 0 || newOffset > content.getText().length())
			return;

		if (updateSelection) {
			if (caretOffset == selectionEnd) {
				selectionEnd = newOffset;
			} else if (selectionStart < 0 && selectionEnd < 0) {
				selectionStart = caretOffset;
				selectionEnd = newOffset;
			}
		} else {
			removeSelection();
		}
		caretOffset = newOffset;
	}

	protected void widgetDisposed(DisposeEvent e) {
		// TODO Auto-generated method stub
	}

	private void paintControl(PaintEvent e) {
		drawBackground(e);
		Rectangle visibleArea = getVisibleArea();
		drawText(e, visibleArea);
		drawSelection(e, visibleArea);
		drawFrame(e);
		drawCaret(e, visibleArea);
	}

	private void drawSelection(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;
		int textLength = content.getText().length();
		int start = Math.min(Math.max(Math.min(selectionStart, selectionEnd), 0), textLength);
		int end = Math.min(Math.max(Math.max(selectionStart, selectionEnd), 0), textLength);

		if (selectionStart >= 0) {
			Point startLocationPixel = getLocationByOffset(start, gc);
			TextLocation startLocation = content.getLocation(start);
			TextLocation endLocation = content.getLocation(end);
			String[] textLines = content.getLines();

			Color oldBackground = gc.getBackground();
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));

			int y = startLocationPixel.y;
			for (int i = startLocation.line; i <= endLocation.line; i++) {
				int x = 0;
				String text = textLines[i];
				if (i == endLocation.line) {
					text = text.substring(0, endLocation.column);
				}
				if (i == startLocation.line) {
					x = startLocationPixel.x;
					text = text.substring(startLocation.column);
				}
				gc.drawText(text, x - visibleArea.x, y - visibleArea.y, false);
				y += LINE_HEIGTH;
			}

			gc.setBackground(oldBackground);

		}
	}

	private void drawCaret(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;

		if (caretOffset >= 0) {
			Point caretLocation = getLocationByOffset(caretOffset, gc);
			int x = e.x + caretLocation.x - visibleArea.x;
			int y = e.y + caretLocation.y - visibleArea.y;
			caret.setBounds(x, y, 1, LINE_HEIGTH);
		}

	}

	private void drawFrame(PaintEvent e) {
		GC gc = e.gc;
		gc.setForeground(getForeground());
		gc.drawRectangle(e.x, e.y, e.width - 1, e.height - 1);
	}

	private void drawText(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;
		gc.drawText(content.getText(), e.x - visibleArea.x, e.y - visibleArea.y, true);
	}

	private void drawBackground(PaintEvent e) {
		GC gc = e.gc;
		gc.setBackground(new Color(255, 255, 255, 128));
		gc.fillRectangle(e.x, e.y, e.width - 1, e.height - 1);
	}

	private Rectangle getVisibleArea() {
		Rectangle clientArea = getClientArea();

		ScrollBar horizontalBar = getHorizontalBar();
		ScrollBar verticalBar = getVerticalBar();

		int hOffset = (horizontalBar != null) ? horizontalBar.getSelection() : 0;
		int vOffset = (verticalBar != null) ? verticalBar.getSelection() : 0;

		clientArea.x += hOffset;
		clientArea.y += vOffset;

		return clientArea;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		redraw();
	}

	public String getText() {
		return content.getText();
	}

	public void setText(String text) {
		content.setText(text);
		redraw();
	}

	public void append(String string) {
		content.append(string);
		redraw();
	}

	public void setSelection(int start) {
		selectionStart = start;
		redraw();
	}

	public void setSelection(int start, int end) {
		selectionStart = start;
		selectionEnd = end;
		caretOffset = end;
		redraw();
	}

	public Point getSelection() {
		return new Point(0, 0);
	}

	public String getSelectionText() {
		return content.getText().substring(selectionStart, selectionEnd);
	}

	public int getTopIndex() {
		if ((style & SWT.SINGLE) != 0)
			return 0;
		Rectangle visibleArea = getVisibleArea();
		return visibleArea.x / LINE_HEIGTH;
	}

	public void setTopIndex(int index) {
		checkWidget();
		if ((style & SWT.SINGLE) != 0)
			return;
		Rectangle visibleArea = getVisibleArea();

		int y = index * LINE_HEIGTH;
		scroll(0, 0, visibleArea.x, y, visibleArea.width, visibleArea.height, true);
	}

	/**
	 * Returns a point describing the location of the caret relative to the
	 * receiver.
	 *
	 * @return a point, the location of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Point getCaretLocation() {
		return getLocationByOffset(caretOffset, new GC(this));
	}

	public Point getLocationByOffset(int offset, GC gc) {
		TextLocation selectionLocation = content.getLocation(offset);

		String beforeSelection = content.getLines()[selectionLocation.line].substring(0, selectionLocation.column);
		int x = gc.textExtent(beforeSelection).x;
		int y = selectionLocation.line * LINE_HEIGTH;
		return new Point(x, y);
	}

	/**
	 * Returns the character position of the caret.
	 * <p>
	 * Indexing is zero based.
	 * </p>
	 *
	 * @return the position of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getCaretPosition() {
		return selectionStart;
	}

	class TextLocation {
		int line;
		int column;

		public TextLocation(int line, int column) {
			this.line = line;
			this.column = column;
		}

		@Override
		public String toString() {
			return "(" + line + ", " + column + ")";
		}
	}

	class TextContent {
		private String text = "";

		public String getText() {
			return text;
		}

		public void removeCharacter(int offset) {
			if (offset > text.length()) return;
			StringBuilder sb = new StringBuilder(text.substring(0, offset));
			if (offset + 1 < text.length()) {
				sb.append(text.substring(offset + 1, text.length()));
			}
			text = sb.toString();

		}

		public void append(char character) {
			StringBuilder sb = new StringBuilder(text);
			sb.insert(caretOffset, character);
			text = sb.toString();
		}

		public int getOffset(TextLocation location) {
			String[] lines = getLines();
			int offset = 0;
			for (int i = 0; i < location.line; i++) {
				offset += lines[i].length() + 1; // add 1 for line break
			}
			offset += Math.min(location.column, lines[location.line].length());
			return offset;
		}

		public void setText(String text) {
			if (text == null)
				SWT.error(SWT.ERROR_NULL_ARGUMENT);
			this.text = text;
		}

		public void append(String string) {
			if (string == null)
				SWT.error(SWT.ERROR_NULL_ARGUMENT);
			text = text + string;
		}

		public String[] getLines() {
			return getLinesOf(text);
		}

		public int getLineCount() {
			return getLines().length;
		}

		public TextLocation getLocation(int offset) {
			if (offset < 0 || offset > text.length())
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);

			int line = 0;
			int column = 0;

			for (int i = 0; i < offset; i++) {
				char c = text.charAt(i);
				if (c == '\n') {
					line++;
					column = 0; // Reset column number after a new line
				} else if (c == '\r') {
					if (i + 1 < text.length() && text.charAt(i + 1) == '\n') {
						i++; // Skip the '\n' in '\r\n' sequence
					}
					line++;
					column = 0; // Reset column number after a new line
				} else {
					column++;
				}
			}
			return new TextLocation(line, column);
		}

		private String[] getLinesOf(String string) {
			return string.split(DELIMITER);
		}
	}

	private void scrollBarSelectionChanged(SelectionEvent e) {
		redraw();
	}
}
