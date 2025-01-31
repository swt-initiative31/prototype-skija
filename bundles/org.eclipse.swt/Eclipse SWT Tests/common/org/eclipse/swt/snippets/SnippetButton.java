package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetButton {

	public static void main(String[] args) {
		Display display = new Display();
		Image image = display.getSystemImage(SWT.ICON_QUESTION);
		Shell shell = new Shell(display);
		shell.setText("SnippetButton");
//		shell.setLayout(new GridLayout());
		var button = new Button(shell, SWT.PUSH);

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

		var label = new Label(shell, SWT.BORDER);
		label.setText("Label Text");
		label.setImage(image);
		label.setSize(100, 100);
		label.setLocation(100, 100);

		var text = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		text.setText("This is a text....");

		text.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		text.setSize(300, 300);

		text.setLocation(400, 400);


		shell.setSize(300, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
