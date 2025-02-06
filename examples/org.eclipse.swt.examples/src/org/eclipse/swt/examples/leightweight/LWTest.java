package org.eclipse.swt.examples.leightweight;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * @author Thomas Singer
 */
public class LWTest {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.NO_BACKGROUND);
		final LWBridge bridge = new LWBridge(shell);
		final LWContainer container = bridge.getContainer();
//		container.setLayoutManager(new LWRowLayout());
		container.setLayoutManager(new LWColumnLayout(LWColumnLayout.Alignment.BEGIN)
				                           .setMargin(10, 5)
				                           .setSpacing(10));
		final LWRadioButton.DefaultGroup group = new LWRadioButton.DefaultGroup();
		final LWRadioButton radioButton1 = new LWRadioButton("Hello", group, container);
		logSelection(radioButton1);
		final LWRadioButton radioButton2 = new LWRadioButton("World!!!!", group, container);
		logSelection(radioButton2);
		final LWRadioButton radioButton3 = new LWRadioButton("SWT rocks", group, container);
		radioButton3.setSelected();
		logSelection(radioButton3);

		final LWButton addButton = new LWButton("Add", container);
		addButton.setLayoutData(LWColumnLayout.Alignment.FILL);
		addButton.addListener(type -> {
					if (type == LWAbstractButton.EVENT_SELECTED) {
						System.out.println("Add selected");
					}
				});
		bridge.layout();

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void logSelection(LWRadioButton radioButton) {
		radioButton.addListener(type -> {
			if (type == LWAbstractButton.EVENT_SELECTED && radioButton.isSelected()) {
				System.out.println(radioButton.getText() + " selected");
			}
		});
	}
}
