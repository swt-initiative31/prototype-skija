package org.eclipse.swt.examples.skija;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SharedGCTest {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		new Label(shell, SWT.NONE)
				.setText("Label");

		createButton(shell, "Button 1");
		createButton(shell, "Button 2");

		createCheckbox(shell, "Check 1");
		createCheckbox(shell, "Check 2");

		createRadioButton(shell, "Radio 1");
		createRadioButton(shell, "Radio 2");

		final Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		size.x = Math.max(size.x, 400);
		shell.setSize(size);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static void createButton(Shell shell, String text) {
		final Button button = new Button(shell, SWT.PUSH);
		button.setText(text);
		button.addListener(SWT.Selection, e -> System.out.println(text));
	}

	private static void createCheckbox(Shell shell, String text) {
		final Button button = new Button(shell, SWT.CHECK);
		button.setText(text);
		button.addListener(SWT.Selection, e -> System.out.println(text));
	}

	private static void createRadioButton(Shell shell, String text) {
		final Button button = new Button(shell, SWT.RADIO);
		button.setText(text);
		button.addListener(SWT.Selection, e -> System.out.println(text));
	}
}
