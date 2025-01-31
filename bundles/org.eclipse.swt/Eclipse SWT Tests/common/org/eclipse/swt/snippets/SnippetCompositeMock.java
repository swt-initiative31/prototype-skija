package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetCompositeMock {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SnippetButton");

		{
			var mock = new MockControl(shell, SWT.PUSH);
			mock.setLocation(0, 0);
			mock.setSize(95, 95);
			mock.setBackground(mock.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
		Composite c1 = new Composite(shell, SWT.FILL | SWT.BORDER);
		Composite c2 = new Composite(c1, SWT.FILL | SWT.BORDER);
		Composite c = shell;

		c1.setLocation(200, 50);
		c1.setSize(800, 800);

		c2.setLocation(200, 40);
		c2.setSize(200, 200);

//		shell.setLayout(new GridLayout());

		var mock = new MockControl(c1, SWT.PUSH);
		mock.setBackground(mock.getDisplay().getSystemColor(SWT.COLOR_GREEN));

		mock.setLocation(50, 50);
		mock.setSize(100, 100);

		mock = new MockControl(c2, SWT.NONE);
		mock.setBackground(mock.getDisplay().getSystemColor(SWT.COLOR_YELLOW));

		mock.setLocation(50, 50);
		mock.setSize(30, 30);

		shell.setSize(300, 500);

		shell.setLocation(300, 100);
		shell.open();
		long currentTime = System.currentTimeMillis();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();

		}
		display.dispose();
	}

}
