package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;

public class CSimpleTextModel {
	static final String DELIMITER = "\n";

	private String text = "";

	private int selectionStart, selectionEnd;
	private int caretOffset;

	private final List<ITextModelChangedListener> modelChangedListeners = new ArrayList<>();

	private String[] textLines;

	String getText() {
		return text;
	}

	String getText(int start, int end) {
		if (start >= end) {
			return "";
		}

		int textLength = getText().length();

		return getText().substring(Math.min(Math.max(start, 0), textLength), Math.min(Math.max(0, end), textLength));
	}

	void replaceSelectedTextWith(String string) {
		int start = getSelectionStart();
		replaceWith(string, start, getSelectionEnd());
		setCaretOffset(start + string.length());
		clearSelection();
		if (string.length() > 0) {
			sendTextModified();
		}
	}


	private void replaceWith(String string, int start, int end) {
		if (start > end) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		StringBuilder sb = new StringBuilder(text.substring(0, start));
		sb.append(string);
		sb.append(text.substring(end));

		text = sb.toString();
		caretOffset = start + string.length();
		sendTextModified();
		clearSelection();
	}

	void removeCharacterBeforeCaret() {
		if (isTextSelected()) {
			replaceSelectedTextWith("");
		} else if (getCaretOffset() > 0) {
			replaceWith("", getCaretOffset() - 1, getCaretOffset());
		}
	}

	void removeCharacterAfterCaret() {
		if (isTextSelected()) {
			replaceSelectedTextWith("");
		} else if (getCaretOffset() < text.length()) {
			replaceWith("", getCaretOffset(), getCaretOffset() + 1);
		}
	}

	int getOffset(TextLocation location) {
		String[] lines = getLines();
		int offset = 0;
		for (int i = 0; i < location.line; i++) {
			offset += lines[i].length() + 1; // add 1 for line break
		}
		offset += Math.min(location.column, lines[location.line].length());
		return offset;
	}

	void setText(String text) {
		if (text == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.text = text;
		caretOffset = 0;
		clearSelection();
		sendTextModified();
	}

	void append(String string) {
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		text = text + string;
		caretOffset = getText().length();
		if (string.length() != 0) {
			sendTextModified();
		}
	}

	String[] getLines() {
		if (textLines == null) {
			textLines = getLinesOf(text);
		}
		return textLines;
	}

	int getLineCount() {
		return getLines().length;
	}

	TextLocation getLocation(int offset) {
		if (offset < 0 || offset > text.length()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

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
		int count = (int) string.chars().filter(c -> c == DELIMITER.toCharArray()[0]).count();
		String[] lines = new String[count + 1];

		int offset = string.indexOf(DELIMITER);
		int i = 0;
		while (offset >= 0) {
			lines[i] = string.substring(0, offset);
			string = string.substring(offset + 1);
			offset = string.indexOf(DELIMITER);
			i++;
		}
		lines[i] = string;

		return lines;
	}

	void insert(String string) {
		if (isTextSelected()) {
			replaceSelectedTextWith(string);
		} else {
			insert(string, getCaretOffset());
		}
		if (string.length() > 0) {
			sendTextModified();
		}
	}

	private void insert(String string, int offset) {
		if (text == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		StringBuilder sb = new StringBuilder(text.substring(0, offset));
		sb.append(string);
		sb.append(text.substring(offset));

		text = sb.toString();
		setCaretOffset(getCaretOffset() + string.length());
		sendTextModified();
	}

	private void setCaretOffset(int offset) {
		caretOffset = offset;
	}

	int getCaretOffset() {
		return caretOffset;
	}

	private void moveCaretTo(int newOffset, boolean changeSelection) {
		if (newOffset < 0 || newOffset > getText().length()) {
			return;
		}

		if (changeSelection) {
			if (caretOffset == selectionEnd) {
				selectionEnd = newOffset;
			} else if (!isTextSelected()) {
				selectionStart = caretOffset;
				selectionEnd = newOffset;
			}
		} else {
			clearSelection();
		}
		caretOffset = newOffset;
		sendSelectionChanged();
	}

	void clearSelection() {
		selectionStart = selectionEnd = -1;
		sendSelectionChanged();
	}

	boolean isTextSelected() {
		if (selectionStart >= 0 && selectionEnd >= 0 && selectionStart != selectionEnd) {
			return true;
		}
		return false;
	}

	int getSelectionStart() {
		return Math.min(selectionStart, selectionEnd);
	}

	int getSelectionEnd() {
		return Math.max(selectionStart, selectionEnd);
	}

	private int keepOffsetInTextBounds(int offset) {
		return Math.max(0, Math.min(offset, getText().length()));
	}

	void setSelection(int start) {
		selectionStart = selectionEnd = caretOffset = keepOffsetInTextBounds(start);
		sendSelectionChanged();
	}

	void setSelection(int start, int end) {
		selectionStart = keepOffsetInTextBounds(start);
		selectionEnd = caretOffset = keepOffsetInTextBounds(end);
		sendSelectionChanged();
	}

	void setSectionStart(TextLocation location) {
		setSelection(getOffset(location));
	}

	void setSelectionEnd(TextLocation location) {
		caretOffset = selectionEnd = getOffset(location);
		sendSelectionChanged();
	}

	void selectAll() {
		setSelection(0, getText().length());
	}

	int getSelectionCount() {
		return getSelectionEnd() - getSelectionStart();
	}

	TextLocation getCaretLocation() {
		return getLocation(getCaretOffset());
	}

	void moveCaretLeft(boolean updateSelection) {
		moveCaretTo(getCaretOffset() - 1, updateSelection);
	}

	void moveCaretRight(boolean updateSelection) {
		moveCaretTo(getCaretOffset() + 1, updateSelection);
	}

	void moveCaretUp(boolean updateSelection) {
		TextLocation caretLocation = getLocation(getCaretOffset());
		if (caretLocation.line <= 0) {
			return;
		}
		caretLocation.line--;
		moveCaretTo(getOffset(caretLocation), updateSelection);
	}

	void moveCaretDown(boolean updateSelection) {
		TextLocation caretLocation = getLocation(getCaretOffset());
		if (caretLocation.line >= getLineCount() - 1) {
			return;
		}
		caretLocation.line++;
		moveCaretTo(getOffset(caretLocation), updateSelection);
	}

	void addModelChangedListner(ITextModelChangedListener listener) {
		modelChangedListeners.add(listener);
	}

	void removeModelChangedListner(ITextModelChangedListener listener) {
		modelChangedListeners.remove(listener);
	}

	private void sendTextModified() {
		textLines = null;
		for (ITextModelChangedListener listener : modelChangedListeners) {
			listener.textModified();
		}
	}

	private void sendSelectionChanged() {
		for (ITextModelChangedListener listener : modelChangedListeners) {
			listener.selectionChanged();
		}
	}

	void insert(char character) {
		char[] chars = { normalize(character) };
		String text = new String(chars);
		if (isTextSelected()) {
			replaceWith(text, getSelectionStart(), getSelectionEnd());
		} else {
			insert(text);
		}
		clearSelection();
	}

	private char normalize(char character) {
		if (character == SWT.CR) {
			return DELIMITER.charAt(0);
		}
		return character;
	}

	String getSelectedText() {
		if (!isTextSelected()) {
			return "";
		}
		return getText(getSelectionStart(), getSelectionEnd());
	}

	public void setTextChars(char[] cs) {
		setText(new String(cs));
	}
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

interface ITextModelChangedListener {

	void textModified();

	void selectionChanged();
}
