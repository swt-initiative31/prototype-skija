package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class MenuWindow extends Shell implements Listener, DisposeListener {

	private Point size;
	int[] lineHeights = null;
	Rectangle[] itemRect = null;
	int mouseOverItem = -1;

	Decorations parent;
	private MenuItem[] items;

	private int openedChildMenuIndex = -1;
	private MenuWindow openedChildMenu = null;

	public MenuWindow(Decorations c, MenuItem[] items, Point p, GC gc) {
		super(c.getShell(),
				SWT.DIALOG_TRIM | SWT.ON_TOP);

		parent = c;

		this.items = items;

		computeSize(gc);

		// Point location = parent.toControl(p);

		Point pl = parent.getLocation();

		setLocation(pl.x + p.x, pl.y + p.y);

		setSize(size);
		display.addFilter(SWT.MouseDown, this);

	}

	private void computeSize(GC gc) {

		if (gc == null)
			gc = GCFactory.getGraphicsContext(this);

		Rectangle b = getBounds();
		Rectangle ca = getClientArea();

		int width = 0;

		lineHeights = new int[items.length];
		itemRect = new Rectangle[items.length];

		int k = 0;
		int height = b.height - ca.height;
		int initialHeight = height;
		for (var i : items) {

			height++; // border
			Point te = gc.textExtent(i.getText());
			itemRect[k] = new Rectangle(1, height - initialHeight, 0, te.y);
			height += te.y;

			width = Math.max(width, te.x + 2);

			System.out.println(k + "-th iteration, width: " + width);

			k++;

		}

		k = 0;
		for (var r : itemRect) {

			itemRect[k] = new Rectangle(r.x, r.y, width - 2, r.height);
			k++;
		}

		size = new Point(width, height);
	}

	@Override
	public void handleEvent(Event event) {

		if (event.type == SWT.MouseDown) {
			if (isDisposed()) {
				if (display != null)
					display.removeListener(SWT.MouseDown, this);
				System.out.println("Remove listener");
			} else {

				if (getBounds().contains(event.x, event.y)) {
					System.out.println("triggerevent and close");
					close();
					parent.setFocus();
					event.doit = false;
					if (parent instanceof Shell s)
						s.setCurrent();
				} else {
					close();
					event.doit = false;
					parent.setFocus();
					if (parent instanceof Shell s)
						s.setCurrent();
				}

			}

		}

		System.out.println("Handle MouseDown: " + event);

		// if (!isDisposed())
		// close();
		//
		// if (display != null)
		// display.removeFilter(SWT.MouseDown, this);

		// if (parent instanceof MenuWindow) {
		//
		// } else {
		// parent.getShell().setCurrent();
		// parent.getShell().redraw();
		// }

	}

	@Override
	public void triggerEvent(Event e) {

		if (e.type == SWT.Resize) {
			super.triggerEvent(e);
		}

		if (e.type == SWT.Paint) {

			var pe = prepareSurface(e);

			if (pe == null)
				return;

			drawMenuSelection(pe);

			finishDrawing(pe);

		}
		if (e.type == SWT.MouseMove) {

			for (var r : itemRect) {
				if (r.contains(e.x, e.y)) {
					handleItemOver(r, e);
				}
			}

		}

	}

	private void handleItemOver(Rectangle r, Event e) {

		int index = getIndex(r);

		if (index == -1)
			return;

		setMouseOver(index, e);

	}

	private void setMouseOver(int index, Event e) {

		if (index == mouseOverItem) {
			return;
		}

		if (openedChildMenuIndex == index) {
			return;
		}

		if (openedChildMenu != null) {
			openedChildMenu.close();
			openedChildMenuIndex = -1;
		}

		mouseOverItem = index;
		var it = items[index];

		if ((it.getStyle() & SWT.CASCADE) != 0) {

			var its = it.getMenu().getItems();
			var pos = new Point(itemRect[index].x + itemRect[index].width + 2,
					itemRect[index].y);
			openedChildMenuIndex = index;
			openedChildMenu = new MenuWindow(this, its, pos, e.gc);
			openedChildMenu.addDisposeListener(this);
			openedChildMenu.open();
		}

	}

	private int getIndex(Rectangle r) {

		int k = 0;
		for (var n : itemRect) {
			if (Objects.equals(n, r))
				return k;

			k++;
		}

		return -1;
	}

	private void drawMenuSelection(Event e) {

		if (e.type == SWT.Paint) {

			if (e.gc == null) {
				return;
			}

			if (e.gc instanceof SkijaGC) {

				e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
				e.gc.drawRectangle(0, 0, size.x - 1, size.y - 1);

				int k = 0;

				for (var i : items) {

					var r = itemRect[k];

					e.gc.drawText(i.getText(), 1, r.y);
					e.gc.drawLine(0, r.height + r.y + 1, size.x,
							r.height + r.y + 1);

					k++;
				}

			}
		}

	}

	@Override
	public void widgetDisposed(DisposeEvent e) {
		// this handler is for the child windows...
		if (e.widget == openedChildMenu) {
			openedChildMenuIndex = -1;
			openedChildMenu = null;
		}

	}

}
