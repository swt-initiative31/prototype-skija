package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class DateTimeDemoApp {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Custom DateTime Widget Demo");
        shell.setSize(500, 200);
//        shell.setLayout(new FillLayout());
        GridLayout layout = new GridLayout(1, false);
//        layout.marginWidth = 20;
//        layout.marginHeight = 20;
        shell.setLayout(layout);
        // Use your custom widget
        //DateTime time = new DateTime(shell, SWT.NONE);
        //CDateTime time = new CDateTime(shell, SWT.NONE);
        //CDateTime time = new CDateTime(shell, SWT.BORDER | SWT.TIME | SWT.DATE);
//      CDateTime time = new CDateTime(shell, SWT.TIME);
//      CDateTime time = new CDateTime(shell, SWT.DATE);
        CDateTime time = new CDateTime(shell, SWT.TIME | SWT.DATE);
        System.out.println("time.getDateTime() : " + time.getDateTime());
        System.out.println("time.getYear() : " + time.getYear());
        System.out.println("time.getMonth() : " + time.getMonth());
        System.out.println("time.getDayOfYear() : " + time.getDayOfYear());
        System.out.println("time.getDayOfMonth() : " + time.getDayOfMonth());
        System.out.println("time.getDayOfWeek() : " + time.getDayOfWeek());
        System.out.println("time.getHour() : " + time.getHour());
        System.out.println("time.getMinute() : " + time.getMinute());
        System.out.println("time.getSecond() : " + time.getSecond());
        System.out.println("time.getCustomShortTimeFormat() : " + time.getCustomShortTimeFormat());
        System.out.println("time.getCustomMediumTimeFormat() : " + time.getCustomMediumTimeFormat());
        System.out.println("time.getCustomShortDateFormat() : " + time.getCustomShortDateFormat());
        System.out.println("time.getCustomMediumDateFormat() : " + time.getCustomMediumDateFormat());
        System.out.println("time.getCustomLongDateFormat() : " + time.getCustomLongDateFormat());

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
