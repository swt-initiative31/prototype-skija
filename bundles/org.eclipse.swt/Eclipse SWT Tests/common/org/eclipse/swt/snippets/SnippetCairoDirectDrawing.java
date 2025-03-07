package org.eclipse.swt.snippets;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

public class SnippetCairoDirectDrawing {

	public static void main(String[] arg) {

		Display d = new Display();
		Shell s = new Shell(d);

		s.open();

		while (!s.isDisposed()) {
			d.readAndDispatch();
			s.redraw();
		}

		d.close();

	}

}
