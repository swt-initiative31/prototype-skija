package org.eclipse.swt.widgets;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;

public class TableItemEvent extends Event {
	TableItemEvent(TableItem content) {
		super();
		data = content;
		widget = content;

	}
}