
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetRadioButton_Old {

	public static void main(String[] args) {
		// Display und Shell initialisieren
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Radio Buttons Example Old");
		shell.setSize(300, 200);
		shell.setLayout(new GridLayout(1, false));

		// Label hinzufügen
		Label_Old label = new Label_Old(shell, SWT.NONE);
		label.setText("Choose an option:");

		// Radio Button_Olds hinzufügen
		Button_Old radio1 = new Button_Old(shell, SWT.RADIO);
		radio1.setText("Option 1");
		radio1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button_Old radio2 = new Button_Old(shell, SWT.RADIO);
		radio2.setText("Option 2");
		radio2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button_Old radio3 = new Button_Old(shell, SWT.RADIO);
		radio3.setText("Option 3");
		radio3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button_Old push = new Button_Old(shell, SWT.PUSH);
		push.setText("Push");
		push.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// Event-Handling
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button_Old selectedButton_Old = (Button_Old) e.widget;
				System.out.println("Selected: " + selectedButton_Old.getText());
				System.out.println(selectedButton_Old.getSelection());
			}
		};

		radio1.addSelectionListener(selectionAdapter);
		radio2.addSelectionListener(selectionAdapter);
		radio3.addSelectionListener(selectionAdapter);

		push.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Button_Old selectedButton = (Button_Old) e.widget;
				System.out.println("Selected: " + selectedButton.getText());
				System.out.println(selectedButton.getSelection());
			}

		});

		// Shell öffnen
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
