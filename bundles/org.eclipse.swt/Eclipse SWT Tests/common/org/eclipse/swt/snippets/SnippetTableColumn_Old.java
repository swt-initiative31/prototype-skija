
/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Table example snippet: insert a table column (at an index)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetTableColumn_Old {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 106");
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		final Table_Old table = new Table_Old(shell, SWT.BORDER | SWT.MULTI);
		table.setHeaderVisible(true);
		for (int i = 0; i < 4; i++) {
			TableColumn_Old column = new TableColumn_Old(table, SWT.NONE);
			column.setText("Column " + i);
			System.out.println(column.getWidth());
		}
		final TableColumn_Old[] columns = table.getColumns();
		for (int i = 0; i < 12; i++) {
			TableItem_Old item = new TableItem_Old(table, SWT.NONE);
			for (int j = 0; j < columns.length; j++) {
				item.setText(j, "Item " + i);
			}
		}
		for (TableColumn_Old col : columns) {
			col.pack();
			System.out.println(col.getWidth());
		}
		Button button = new Button(shell, SWT.PUSH);
		final int index = 1;
		button.setText("Insert Column " + index + "a");
		button.addListener(SWT.Selection, e -> {
			TableColumn_Old column = new TableColumn_Old(table, SWT.NONE,
					index);
			column.setText("Column " + index + "a");
			TableItem_Old[] items = table.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].setText(index, "Item " + i + "a");
			}
			column.pack();
		});

		table.setHeaderVisible(false);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
