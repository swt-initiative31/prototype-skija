package org.eclipse.swt.examples.skija;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SharedGCTest {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		createLeafControls(shell);

		final Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		size.x = Math.max(size.x, 500);
		shell.setSize(size);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static void createLeafControls(Composite parent) {
		parent.setLayout(new GridLayout());

		final Label label = new Label(parent, SWT.NONE);
		label.setText("Label");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Text textSingle = new Text(parent, SWT.BORDER | SWT.SINGLE);
		textSingle.setText("bla");
		textSingle.setMessage("Filter");
		textSingle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Text textMulti = new Text(parent, SWT.BORDER | SWT.MULTI);
		textMulti.setText("bla\nblu");
		textMulti.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createButton(parent, "Button 1");
		createButton(parent, "Button 2");

		createCheckbox(parent, "Check 1");
		createCheckbox(parent, "Check 2");

		createRadioButton(parent, "Radio 1");
		createRadioButton(parent, "Radio 2");
	}

	private static void createButton(Composite parent, String text) {
		final Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		button.addListener(SWT.Selection, e -> System.out.println(text));
	}

	private static void createCheckbox(Composite parent, String text) {
		final Button button = new Button(parent, SWT.CHECK);
		button.setText(text);
		button.addListener(SWT.Selection, e -> System.out.println(text));
	}

	private static void createRadioButton(Composite parent, String text) {
		final Button button = new Button(parent, SWT.RADIO);
		button.setText(text);
		button.addListener(SWT.Selection, e -> System.out.println(text));
	}
}
