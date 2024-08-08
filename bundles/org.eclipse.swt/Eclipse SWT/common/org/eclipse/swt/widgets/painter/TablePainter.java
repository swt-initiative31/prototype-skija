package org.eclipse.swt.widgets.painter;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class TablePainter  implements IPainter {

	private Table table;

	public TablePainter(Table table) {
		this.table = table;
	}

	@Override
	public void onPaint(PaintEvent event) {

		GC gc = event.gc;

		gc.setBackground(table.getBackground());

		gc.fillRectangle(table.getBounds());

		Rectangle r = table.getBounds();

		gc.drawRectangle(new Rectangle(r.x+1, r.y +1, r.width -2  , r.height-2));


	}




}
