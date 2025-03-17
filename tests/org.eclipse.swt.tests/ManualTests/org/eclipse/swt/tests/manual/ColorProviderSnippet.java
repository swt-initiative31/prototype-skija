package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class ColorProviderSnippet {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		final Label label = new Label(shell, SWT.NONE);
		label.setText("Label");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Label shadowOut = new Label(shell, SWT.SHADOW_OUT);
		shadowOut.setText("Shadow out");
		shadowOut.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Label shadowIn = new Label(shell, SWT.SHADOW_IN);
		shadowIn.setText("Shadow in");
		shadowIn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Link link = new Link(shell, SWT.NONE);
		link.setText("Link <a href=\"foo\">target</a>");
		link.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Button pushButton = new Button(shell, SWT.PUSH);
		pushButton.setText("Push");
		pushButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Button checkbox = new Button(shell, SWT.CHECK);
		checkbox.setText("Checkbox");
		checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Button checkboxTristate = new Button(shell, SWT.CHECK);
		checkboxTristate.setSelection(true);
		checkboxTristate.setGrayed(true);
		checkboxTristate.setText("Checkbox tristate");
		checkboxTristate.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Button radio = new Button(shell, SWT.RADIO);
		radio.setText("Radio");
		radio.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Scale scale = new Scale(shell, SWT.HORIZONTAL);
		scale.setSelection(50);
		scale.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		final Text textSingle = new Text(shell, SWT.SINGLE);
		textSingle.setText("Text");
		textSingle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Text textMulti = new Text(shell, SWT.MULTI);
		textMulti.setText("Multiline\ntext");
		textMulti.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final ToolBar toolBar = new ToolBar(shell, SWT.NORMAL);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		new ToolItem(toolBar, SWT.PUSH).setText("Push");
		new ToolItem(toolBar, SWT.CHECK).setText("Check");

		final Slider slider = new Slider(shell, SWT.HORIZONTAL);
		slider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		slider.setValues(50, 0, 100, 5, 1, 5);

		final Button enabledChkBx = new Button(shell, SWT.CHECK);
		enabledChkBx.setText("enabled");
		enabledChkBx.setSelection(true);
		enabledChkBx.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		enabledChkBx.addListener(SWT.Selection, e -> {
			final boolean enabled = enabledChkBx.getSelection();
			label.setEnabled(enabled);
			shadowOut.setEnabled(enabled);
			shadowIn.setEnabled(enabled);
			link.setEnabled(enabled);
			pushButton.setEnabled(enabled);
			checkbox.setEnabled(enabled);
			checkboxTristate.setEnabled(enabled);
			radio.setEnabled(enabled);
			scale.setEnabled(enabled);
			textSingle.setEnabled(enabled);
			textMulti.setEnabled(enabled);
			toolBar.setEnabled(enabled);
			slider.setEnabled(enabled);
		});

		final Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		size.x = Math.max(size.x, 300);
		shell.setSize(size);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
