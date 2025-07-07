/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetDateTime {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Custom DateTime Widget Demo");
        shell.setSize(500, 200);

        GridLayout layout = new GridLayout(1, false);
        shell.setLayout(layout);

        // Use your custom DateTime widget
        DateTime time = new DateTime(shell, SWT.TIME); // One of these 3 line are allowed at a time.
        DateTime time2 = new DateTime(shell, SWT.DATE); // One of these 3 line are allowed at a time.
		DateTime time3 = new DateTime(shell, SWT.DATE | SWT.TIME); // One of these 3 line are allowed at a time.

        // Once Junits are written, the below commented section can be removed.
		/*
		 * System.out.println("time.getDateTime() : " + time.getDateTime());
		 * System.out.println("time.getYear() : " + time.getYear());
		 * System.out.println("time.getMonth() : " + time.getMonth());
		 * System.out.println("time.getDayOfYear() : " + time.getDayOfYear());
		 * System.out.println("time.getDay() : " + time.getDay());
		 * System.out.println("time.getDayOfWeek() : " + time.getDayOfWeek());
		 * System.out.println("time.getHour() : " + time.getHours());
		 * System.out.println("time.getMinute() : " + time.getMinutes());
		 * System.out.println("time.getSecond() : " + time.getSeconds());
		 * System.out.println("time.getCustomShortTimeFormat() : " +
		 * time.getCustomShortTimeFormat());
		 * System.out.println("time.getCustomMediumTimeFormat() : " +
		 * time.getCustomMediumTimeFormat());
		 * System.out.println("time.getCustomLongTimeFormat() : " +
		 * time.getCustomLongTimeFormat());
		 * System.out.println("time.getCustomShortDateFormat() : " +
		 * time.getCustomShortDateFormat());
		 * System.out.println("time.getCustomMediumDateFormat() : " +
		 * time.getCustomMediumDateFormat());
		 * System.out.println("time.getCustomLongDateFormat() : " +
		 * time.getCustomLongDateFormat());
		 *
		 * System.out.println("time.getYear() : " + time.getYear()); time.setYear(2050);
		 * System.out.println("time.getYear() : " + time.getYear());
		 *
		 * System.out.println("time.getMonth() : " + time.getMonth()); time.setMonth(9);
		 * System.out.println("time.getMonth() : " + time.getMonth());
		 *
		 * System.out.println("time.getDay() : " + time.getDay());
		 * time.setDayOfMonth(29); System.out.println("time.getDay() : " +
		 * time.getDay());
		 *
		 * System.out.println("time.getHours() : " + time.getHours()); time.setHours(9);
		 * System.out.println("time.getHours() : " + time.getHours());
		 *
		 * System.out.println("time.getMinutes() : " + time.getMinutes());
		 * time.setMinutes(5); System.out.println("time.getMinutes() : " +
		 * time.getMinutes());
		 *
		 * System.out.println("time.getSeconds() : " + time.getSeconds());
		 * time.setSeconds(3); System.out.println("time.getSeconds() : " +
		 * time.getSeconds());
		 *
		 * System.out.println("time.getTime() : " + time.getTime()); time.setTime(5, 45,
		 * 0); System.out.println("time.getTime() : " + time.getTime());
		 *
		 * System.out.println("time.getDate() : " + time.getDate()); time.setDate(2075,
		 * 11, 30); System.out.println("time.getDate() : " + time.getDate());
		 */

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
