package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetTable {

	 public static void main(String[] args) {
	        // Display und Shell erstellen
	        Display display = new Display();
	        Shell shell = new Shell(display);
	        shell.setText("SWT Table Example");
	        shell.setSize(800, 300);
	        shell.setLayout(new FillLayout());

	        // Tabelle erstellen
	        Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	        table.setHeaderVisible(true);
	        table.setLinesVisible(true);


	        table.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

	        // Spalten erstellen
	        for (int i = 0; i < 5; i++) {
	            TableColumn column = new TableColumn(table, SWT.NONE);
	            column.setText("Column " + (i + 1));
	            column.setWidth(120);
	        }

	        // Zeilen und Zellen erstellen
	        for (int i = 0; i < 5; i++) {
	            TableItem item = new TableItem(table, SWT.NONE);
	            for (int j = 0; j < 5; j++) {
	                item.setText(j, "Item " + (i + 1) + "," + (j + 1));
	            }
	        }

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
