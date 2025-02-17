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

		final Label label = new Label(shell, SWT.NONE);
		label.setText("Label");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Text textSingle = new Text(shell, SWT.BORDER | SWT.SINGLE);
		textSingle.setText("bla");
		textSingle.setMessage("Filter");
		textSingle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Text textMulti = new Text(shell, SWT.BORDER | SWT.MULTI);
		textMulti.setText("bla\nblu");
		textMulti.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

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
