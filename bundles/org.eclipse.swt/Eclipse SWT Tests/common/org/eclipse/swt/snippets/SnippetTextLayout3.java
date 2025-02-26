package org.eclipse.swt.snippets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.List;

public class SnippetTextLayout3 {


    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("SnippetTextLayout3");

	Font font1 = new Font(display, "Tahoma", 14, SWT.BOLD);
	Font font2 = new Font(display, "MS Mincho", 20, SWT.ITALIC);
	Font font3 = new Font(display, "Arabic Transparent", 18, SWT.NORMAL);

	Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	Color green = display.getSystemColor(SWT.COLOR_GREEN);
	Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
	Color gray = display.getSystemColor(SWT.COLOR_GRAY);

	final var layout = new TextLayout(display);
	TextStyle style1 = new TextStyle(font1, yellow, blue);
	TextStyle style2 = new TextStyle(font2, green, null);
	TextStyle style3 = new TextStyle(font3, blue, gray);

	layout.setText(
		"English  ABACDEF   ASDFAS\n\u65E5aaaaaaaaaaa\u672C\u8A9E\u0627\u0644\n\u0639\u0631\u0628\u064A\u0651\u0629");
	layout.setStyle(style1, 0, 6);
	layout.setStyle(style2, 8, 10);
	layout.setStyle(style3, 13, 21);

	var boundRec = layout.getBounds(layout.getText().length() - 1, layout.getText().length());

	var bound = layout.getBounds();

	java.util.List<Rectangle> lineBounds = new ArrayList<>();

	for (int r = 0; r < layout.getLineCount(); r++)
	    lineBounds.add(layout.getLineBounds(r));


	shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	shell.addListener(SWT.Paint, event -> {

	    layout.draw(event.gc, 0, 0);

	    event.gc.setForeground(event.display.getSystemColor(SWT.COLOR_CYAN));
	    event.gc.drawRectangle(boundRec);

	    event.gc.setForeground(event.display.getSystemColor(SWT.COLOR_GREEN));
	    event.gc.drawRectangle(bound);

	    event.gc.setForeground(event.display.getSystemColor(SWT.COLOR_RED));

	    for( var b : lineBounds)
		event.gc.drawRectangle(b);

	});
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch())
		display.sleep();
	}
	font1.dispose();
	font2.dispose();
	font3.dispose();
	layout.dispose();
	display.dispose();
    }

}
