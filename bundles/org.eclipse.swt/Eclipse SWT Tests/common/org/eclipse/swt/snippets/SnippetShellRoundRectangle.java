package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetShellRoundRectangle {

	public static void main(String[] args) {

		SWT.NATIVE_DECORATIONS = false;

		int width = 300;
		int height = 200;

		Display display = new Display();

		Region r = new Region(display);

		Shell shell = new Shell(display);

		shell.setSize(width, height);

		r.add(new Rectangle(0, 0, width, height));

		removeCorners(r, 20);

		shell.setRegion(r);

		shell.setText("Snippet 117");
		shell.setLayout(new FillLayout());
		final Text t = new Text(shell, SWT.BORDER | SWT.MULTI);
		t.setText("here is some text to be selected");
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem editItem = new MenuItem(bar, SWT.CASCADE);
		editItem.setText("Edit");
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		editItem.setMenu(submenu);

		{
			MenuItem item = new MenuItem(submenu, SWT.PUSH);
			item.addListener(SWT.Selection, e -> t.selectAll());
			item.setText("Select &All\tCtrl+A");
			item.setAccelerator(SWT.MOD1 + 'A');
		}

		{
			MenuItem item = new MenuItem(submenu, SWT.PUSH);
			item.addListener(SWT.Selection, e -> t.selectAll());
			item.setText("Item 2");
			item.setAccelerator(SWT.MOD1 + 'A');
		}

		{
			MenuItem item = new MenuItem(submenu, SWT.PUSH);
			item.addListener(SWT.Selection, e -> t.selectAll());
			item.setText("Item 3");
			item.setAccelerator(SWT.MOD1 + 'A');
		}

		{
			MenuItem item2 = new MenuItem(submenu, SWT.CASCADE);
			item2.setText("Cascade Item 1 ");
			Menu ssubMenu = new Menu(submenu);
			item2.setMenu(ssubMenu);
			MenuItem subItem1 = new MenuItem(ssubMenu, SWT.PUSH);
			subItem1.setText("Subitem 1");
			MenuItem subItem2 = new MenuItem(ssubMenu, SWT.PUSH);
			subItem2.setText("Subitem 2");
		}

		{
			MenuItem item2 = new MenuItem(submenu, SWT.CASCADE);
			item2.setText("Cascade Item 2 ");
			Menu ssubMenu = new Menu(submenu);
			item2.setMenu(ssubMenu);
			MenuItem subItem1 = new MenuItem(ssubMenu, SWT.PUSH);
			subItem1.setText("Subitem 1");
			MenuItem subItem2 = new MenuItem(ssubMenu, SWT.PUSH);
			subItem2.setText("Subitem 2");
		}

		// Note that as long as your application has not overridden
		// the global accelerators for copy, paste, and cut
		// (CTRL+C or CTRL+INSERT, CTRL+V or SHIFT+INSERT, and CTRL+X or
		// SHIFT+DELETE)
		// these behaviours are already available by default.
		// If your application overrides these accelerators,
		// you will need to call Text.copy(), Text.paste() and Text.cut()
		// from the Selection callback for the accelerator when the
		// text widget has focus.


		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void removeCorners(Region r, int radius) {

		int qrad = radius * radius;
		var b = r.getBounds();

		// TODO improve concept by only move along quarter circle and remove
		// lines.

		for (int x = 0; x < radius; x++) {
			for (int y = 0; y < radius; y++) {
				if (x * x + y * y > qrad) {
					r.subtract(radius - x, radius - y, 1, 1);
					r.subtract(b.width - (radius - x), radius - y, 1, 1);
					r.subtract(radius - x, b.height - (radius - y), 1, 1);
					r.subtract(b.width - (radius - x), b.height - (radius - y),
							1, 1);
				}
			}
		}

	}

}
