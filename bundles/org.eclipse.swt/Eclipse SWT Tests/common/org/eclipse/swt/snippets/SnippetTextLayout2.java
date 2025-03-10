package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetTextLayout2 {

	final static String longString = "The preferred size of a widget is the minimum size needed to show its content. In the case of a Composite, the preferred size is the smallest rectangle that contains all of its children. If children have been positioned by the application, the Composite computes its own preferred size based on the size and position of the children. If a Composite is using a layout class to position its children, it asks the Layout to compute the size of its clientArea, and then it adds in the trim to determine its preferred size.";
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet 197");
		final TextLayout layout = new TextLayout(display);
		layout.setText(longString);
		Listener listener = event -> {
			switch (event.type) {
				case SWT.Paint :
					layout.draw(event.gc, 10, 10);
					break;
				case SWT.Resize :
					layout.setWidth(shell.getSize().x - 20);
					break;
			}
		};
		shell.addListener(SWT.Paint, listener);
		shell.addListener(SWT.Resize, listener);
		shell.setSize(300, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		layout.dispose();
		display.dispose();
	}

}
