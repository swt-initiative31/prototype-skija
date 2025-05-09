/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Tree example snippet: create a tree
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetTree {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SnippetTree");
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);
		for (int i = 0; i < 1; i++) {
			TreeItem iItem = new TreeItem(tree, 0);
			iItem.setText("TreeItem (0) -" + i);
			System.out.println("\t" + iItem.getText());
			for (int j = 0; j < 2; j++) {
				TreeItem jItem = new TreeItem(iItem, 0);
				jItem.setText("TreeItem (1) -" + j);
				System.out.println("\t\t" + jItem.getText());
				for (int k = 0; k < 1; k++) {
					TreeItem kItem = new TreeItem(jItem, 0);
					kItem.setText("TreeItem (2) -" + k);
					System.out.println("\t\t\t" + kItem.getText());
					for (int l = 0; l < 1; l++) {
						TreeItem lItem = new TreeItem(kItem, 0);
						lItem.setText("TreeItem (3) -" + l);
						System.out.println("\t\t\t\t" + lItem.getText());
					}
				}
			}
		}
		shell.setSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
