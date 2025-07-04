/*******************************************************************************
 * Copyright (c) 2025 ETAS GmbH and others, all rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ETAS GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class PathDrawingSnippet {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
		shell.setText("Path Drawing Example");
		shell.setSize(600, 500);

        shell.addListener(SWT.Paint, event -> {
			GC gc = Drawing.createGraphicsContext(event.gc, shell);
            // Test setAlpha and getAlpha
            int oldAlpha = gc.getAlpha();
			gc.setAlpha(50);

            // 1. Star shape with lines
            Path star = new Path(display);
            star.moveTo(100, 40);
            star.lineTo(120, 120);
            star.lineTo(40, 70);
            star.lineTo(160, 70);
            star.lineTo(80, 120);
            star.close();

            gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
            gc.setLineWidth(4);
            gc.drawPath(star);

            gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
            gc.fillPath(star);

            star.dispose();

            // 2. Cubic Bezier curve
            Path cubic = new Path(display);
            cubic.moveTo(250, 60);
            cubic.cubicTo(300, 10, 400, 110, 450, 60);

            gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
            gc.setLineWidth(3);
            gc.drawPath(cubic);

            cubic.dispose();

            // 3. Quadratic Bezier curve
            Path quad = new Path(display);
            quad.moveTo(250, 120);
            quad.quadTo(350, 200, 450, 120);

            gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_RED));
            gc.setLineWidth(3);
            gc.drawPath(quad);

            quad.dispose();

			// 4.donut shape (two subpaths)
            Path donut = new Path(display);
            donut.addArc(100, 200, 200, 200, 0, 360); // Outer circle
            donut.addArc(150, 250, 100, 100, 0, 360); // Inner circle (hole)
            donut.close();

            gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
            gc.fillPath(donut);

            gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
            gc.setLineWidth(2);
            gc.drawPath(donut);

            donut.dispose();

            // 5. Open path with multiple segments
            Path open = new Path(display);
            open.moveTo(350, 300);
            open.lineTo(400, 350);
            open.cubicTo(420, 370, 480, 330, 500, 350);
            open.quadTo(520, 370, 550, 300);

            gc.setForeground(display.getSystemColor(SWT.COLOR_MAGENTA));
            gc.setLineWidth(2);
            gc.drawPath(open);
			gc.commit();
            open.dispose();
            // Restore alpha for any further drawing
			gc.setAlpha(oldAlpha);
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}