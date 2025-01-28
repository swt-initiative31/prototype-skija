package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetComposite {

	public static void main(String[] args) {
		Display display = new Display();
		Image image = display.getSystemImage(SWT.ICON_QUESTION);
		Shell shell = new Shell(display);
		shell.setText("SnippetButton");

		Composite c = new Composite(shell, SWT.FILL | SWT.BORDER);

		c.setLocation(50, 50);
		c.setSize(800, 800);

//		shell.setLayout(new GridLayout());

		var button = new Button(c, SWT.PUSH);

		// button.addSelectionListener(widgetSelectedAdapter(e ->
		// System.out.println("Received evt: " + e )));
		// button.addSelectionListener(widgetSelectedAdapter(__ ->
		// System.out.println("Another click")));
		button.addListener(SWT.Selection, event -> System.out.println("Click!!!"));

		// When the shell is active and the user presses ENTER, the button is
		// pressed
		// shell.setDefaultButton(button);

		button.setImage(image);
		button.setText("Button hola");
		button.setSize(133, 150);
		button.setLocation(300, 300);

		var label = new Label(c, SWT.BORDER | SWT.LEFT | SWT.SHADOW_IN);
		label.setText("New Label Text");
		label.setImage(image);
		label.setSize(100, 100);
		label.setLocation(0, 0);

		var text = new Text(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		text.setText("This is a text....");

		text.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		text.setSize(300, 300);

		text.setLocation(400, 400);

		var old = new Label_Old(c, SWT.BORDER);

		old.setText("Old_Label");
		old.setLocation(100, 100);

		old.pack();

		shell.setSize(300, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
