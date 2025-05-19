/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.layout.*;

public class SnippetShellMacDraw {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SnippetShellMacDraw");

		shell.setSize(800, 800);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Canvas c1 = new Canvas(shell, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		c1.setSize(800, 400);
		Canvas c2 = new Canvas(shell, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		c2.setSize(800, 400);

		c1.addListener(SWT.Paint, e -> {
//			e.gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
//			e.gc.fillOval(50, 50, 100, 100);

			var img = display.getSystemImage(SWT.ICON_INFORMATION);

			e.gc.drawImage(img, 0, 0);
		});


		c2.addListener(SWT.Paint, e -> {

//			NSData d = new NSData(c1.view.id);
//
//			long length = d.length();
//
//			byte[] b = new byte[(int) length];
//
//			d.getBytes(b);

			var swtimg = display.getSystemImage(SWT.ICON_INFORMATION);

			System.out.println("draw c2");

			NSImage img = swtimg.handle;


			NSGraphicsContext handle = NSGraphicsContext.currentContext();
			handle.saveGraphicsState();

			NSRect srcRect = new NSRect();
			srcRect.x = 0;
			srcRect.y = 0;
			srcRect.width = swtimg.getBounds().width;
			srcRect.height = swtimg.getBounds().height;
			NSRect destRect = new NSRect();
			destRect.x = 0;
			destRect.y = 0;
			destRect.width = srcRect.width;
			destRect.height = srcRect.height;

			img.drawInRect(destRect, srcRect, OS.NSCompositeSourceOver, 1);

			handle.restoreGraphicsState();


		});

		shell.open();
		while (!shell.isDisposed()) {
			c1.redraw();
			c2.redraw();


			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
