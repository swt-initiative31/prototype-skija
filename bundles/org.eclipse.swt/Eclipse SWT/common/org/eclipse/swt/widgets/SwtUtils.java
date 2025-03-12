package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class SwtUtils {

    private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

    abstract static class Position {

	Object item;
	Rectangle position;
	boolean shortenNecessary;

    }

    static class ImagePosition extends Position {

    }

    static class TextPosition extends Position {

	boolean[] shortenedWidth;
	int[] linePositions;
	int firstHiddenLine;

    }

    public static TextPosition computePosition(GC gc, Point size, int align, String text, int[] margins) {

	final int width = size.x;
	final int height = size.y;

	int leftMargin = 0;
	int rightMargin = 0;
	int topMargin = 0;
	int bottomMargin = 0;

	if (margins != null)
	    if (margins.length == 4) {
		leftMargin = margins[0];
		rightMargin = margins[1];
		topMargin = margins[2];
		bottomMargin = margins[3];
	    } else if (margins.length == 1) {
		leftMargin = margins[0];
		rightMargin = margins[0];
		topMargin = margins[0];
		bottomMargin = margins[0];
	    } else if (margins.length == 2) {
		leftMargin = margins[0];
		rightMargin = margins[0];
		topMargin = margins[1];
		bottomMargin = margins[1];
	    }


	boolean shortenText = false;
	String t = text;
	int availableWidth = Math.max(0, width - (leftMargin + rightMargin));
	int availableHeight = Math.max(0, height - (topMargin + bottomMargin));
	String[] lines = text == null ? null : splitString(text);

	int maxTextExtentWidth = 0;
	int heightTextExtent = 0;
	Point[] extents = getLineSizes(gc, lines);

	boolean[] exceedsWidth = new boolean[extents.length];


	for (int i = 0; i < extents.length; i++) {
	    exceedsWidth[i] = extents[i].x > width;
	    maxTextExtentWidth = Math.max(maxTextExtentWidth, extents[i].x);
	}


	// determine horizontal position
	int x = leftMargin;
	if ((align & SWT.CENTER) != 0) {
	    x = (width - maxTextExtentWidth) / 2;
	}
	if ((align & SWT.RIGHT) != 0) {
	    x = width - rightMargin - maxTextExtentWidth;
	}

	return new TextPosition();

    }

    private static Point[] getLineSizes(GC gc, String[] lines) {

	Point[] res = new Point[lines.length];

	for (int i = 0; i < lines.length; i++) {
	    res[i] = gc.textExtent(lines[i], DRAW_FLAGS);
	}

	return res;
    }

    private static String[] splitString(String text) {
	String[] lines = new String[1];
	int start = 0, pos;
	do {
	    pos = text.indexOf('\n', start);
	    if (pos == -1) {
		lines[lines.length - 1] = text.substring(start);
	    } else {
		boolean crlf = (pos > 0) && (text.charAt(pos - 1) == '\r');
		lines[lines.length - 1] = text.substring(start, pos - (crlf ? 1 : 0));
		start = pos + 1;
		String[] newLines = new String[lines.length + 1];
		System.arraycopy(lines, 0, newLines, 0, lines.length);
		lines = newLines;
	    }
	} while (pos != -1);
	return lines;
    }

}
