package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class TreeColumnsHandler {

	private Tree Tree;
	private Rectangle columnsArea;
	private Point computedSize = null;
	private int columnResizePossible = -1;
	private int columnResizeActive = -1;

	TreeColumnsHandler(Tree Tree) {
		this.Tree = Tree;
	}

	void calculateColumnsPositions() {
		boolean drawColumns = Tree.columnsExist();

		int TreeColumnsHeight = 0;
		int width = 0;
		this.columnsArea = new Rectangle(0, 0, 0, 0);

		int horizontalShift = 0;
		if (Tree.getHorizontalBar() != null) {
			horizontalShift = Tree.getHorizontalBar().getSelection();
		}

		if (drawColumns) {
			for (TreeColumn c : Tree.getColumns()) {
				width += c.getWidth();
				TreeColumnsHeight = Math.max(c.getHeight(), TreeColumnsHeight);
			}

			this.columnsArea = new Rectangle(-horizontalShift, 0, width, TreeColumnsHeight);
		}

		this.computedSize = new Point(width, TreeColumnsHeight);

		if (Tree.getHeaderVisible()) {
			this.computedSize.y = Math.max(1, this.computedSize.y);
		} else {
			this.columnsArea.height = 0;
			this.computedSize.y = 0;
		}
	}

	public void paint(GC gc) {
		if (!Tree.getHeaderVisible())
			return;

		var ca = Tree.getClientArea();

		if (Tree.FILL_AREAS) {
			var prev = gc.getBackground();
			gc.setBackground(Tree.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			gc.fillRectangle(columnsArea);
			gc.setBackground(prev);
		}

		var fgBef = gc.getForeground();

		for (var c : Tree.getColumns()) {
			if (!c.getBounds().intersects(ca)) {
				continue;
			}

			c.paint(gc);
		}

		gc.setForeground(fgBef);
	}

	public Point getSize() {
		if (this.computedSize == null || !Tree.USE_CACHES) {
			calculateColumnsPositions();
		}

		return this.computedSize;
	}

	public Rectangle getColumnsBounds() {
		if (!Tree.USE_CACHES || columnsArea == null) {
			calculateColumnsPositions();
		}

		return columnsArea;
	}

	public void handleMouseMove(Event event) {
		if (columnsArea == null)
			return;

		if (!columnsArea.contains(event.x, event.y)) {
			if (Tree.mouseHoverElement instanceof TreeColumn ti) {
				Tree.mouseHoverElement = null;
				ti.redraw();
			}
			Tree.setCursor(null);
			return;
		}

		if (this.columnResizeActive != -1) {
			var c = Tree.getColumn(this.columnResizeActive);
			int x = c.getBounds().x;
			c.setWidth(event.x - x);
			Tree.redraw();
		}

		// TODO highlight columns if mouse over...

		int i = mouseIsOnColumnSide(event.x, event.y);
		if (i >= 0) {
			System.out.println("highlight Side and change cursor: " + i);
			Tree.setCursor(Tree.getDisplay().getSystemCursor(SWT.CURSOR_SIZEWE));
			this.columnResizePossible = i;
		} else {
			Tree.setCursor(null);
			this.columnResizePossible = -1;
		}

	}

	private int mouseIsOnColumnSide(int x, int y) {
		final TreeColumn[] columns = Tree.getColumns();
		if (columns != null) {
			for (TreeColumn c : columns) {
				if (Math.abs(c.getBounds().x + c.getBounds().width - x) < 5) {
					return Tree.indexOf(c);
				}
			}
		}

		return -1;
	}

	public void handleMouseDown(Event event) {
		if (columnsArea == null)
			return;
		if (!columnsArea.contains(event.x, event.y))
			return;

		if (this.columnResizePossible != -1 && event.button == 1) {
			this.columnResizeActive = this.columnResizePossible;
		}
	}

	public void handleMouseUp(Event e) {
		this.columnResizeActive = -1;
	}

	public void clearCache() {
		columnsArea = null;
		computedSize = null;
	}
}
