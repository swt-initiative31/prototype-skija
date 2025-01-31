package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class DecorationsHandler implements Listener {

	private Decorations c;
	boolean mouseDownLeft = false;
	boolean mouseDownRight = false;
	boolean mouseDownBottom = false;
	private boolean mouseDownTop = false;
	private Point lastTopPosition = null;

	Rectangle minimize = null;
	Rectangle maximize = null;
	Rectangle close = null;
	Rectangle header = null;
	Rectangle menuBar = null;

	public DecorationsHandler(Decorations c) {
		this.c = c;
	}

	@Override
	public void handleEvent(Event e) {

		switch (e.type) {
			case SWT.MouseMove :
				handleMouseMove(e);
				break;
			case SWT.MouseDown :
				handleMouseDown(e);
				break;
			case SWT.MouseUp :
				handleMouseUp(e);
				break;
			case SWT.MouseEnter :
				handleMouseEnter(e);
				break;
			case SWT.MouseExit :
				handleMouseExit(e);
				break;
		}

	}

	private void handleMouseExit(Event e) {

	}

	private void handleMouseEnter(Event e) {

	}

	private void handleMouseUp(Event e) {

		mouseDownLeft = false;
		mouseDownRight = false;
		mouseDownBottom = false;
		mouseDownTop = false;
		lastTopPosition = null;

	}

	private void handleMouseDown(Event e) {

		if (onLeft(e)) {
			mouseDownLeft = true;
		} else if (onRight(e)) {
			mouseDownRight = true;
		} else if (onBotton(e)) {
			mouseDownBottom = true;
		} else if (onTop(e)) {

			if (minimize.contains(e.x, e.y)) {
				if (c instanceof Shell s) {
					s.setMinimized(true);
				}
			} else if (maximize.contains(e.x, e.y)) {
				if (c instanceof Shell s) {
					s.setMaximized(true);
				}

			} else if (close.contains(e.x, e.y)) {
				if (c instanceof Shell s) {
					s.close();
				}
			} else {
				mouseDownTop = true;
				lastTopPosition = new Point(e.x, e.y);
			}
		} else if (onMenu(e)) {

			var mb = c.getMenuBar();

			MenuItem[] items = mb.getItems();
			if (items != null && items.length > 0) {

				new MenuWindow(c, items, new Point(e.x, e.y)).open();

			}

		}
	}

	private boolean onMenu(Event e) {
		return menuBar != null && menuBar.contains(new Point(e.x, e.y));
	}

	private boolean onTop(Event e) {
		return e.y < 40;
	}

	private boolean onBotton(Event e) {
		var b = c.getBounds();

		return e.y > b.height - 5;
	}

	private boolean onRight(Event e) {
		var b = c.getBounds();
		return (e.x > b.width - 5 && e.y > 40);
	}

	private boolean onLeft(Event e) {
		return (e.x < 5 && e.y > 40);
	}

	private void handleMouseMove(Event e) {

		if (mouseDownLeft) {

			var b = c.getBounds();

			c.setSize(b.width - e.x, b.height);
			c.setLocation(b.x + e.x, b.y);

		} else if (mouseDownRight) {
			var b = c.getBounds();
			c.setSize(e.x, b.height);
		} else if (mouseDownBottom) {
			var b = c.getBounds();
			c.setSize(b.width, e.y);
		} else if (mouseDownTop) {

			var l = c.getLocation();
			c.setLocation(l.x + e.xDirection, l.y + e.yDirection);

			c.setLocation(l.x + e.x - lastTopPosition.x,
					l.y + e.y - lastTopPosition.y);

		}

		Rectangle b = c.getBounds();

		if (e.y < 40) {
			System.out.println("onDecoration...");
		}

		if (onLeft(e) || onRight(e)) {
			System.out.println("onBorder");
			c.setCursor(c.getDisplay().getSystemCursor(SWT.CURSOR_SIZEW));
		} else if (onBotton(e)) {
			System.out.println("onBorder");
			c.setCursor(c.getDisplay().getSystemCursor(SWT.CURSOR_SIZES));
		} else {
			c.setCursor(null);
		}

	}

	void drawDecoration(Event e) {

		GC gr = e.gc;

		Rectangle b = c.getBounds();

		var bbg = gr.getBackground();
		var bfg = gr.getForeground();

		var bg = c.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		var fg = c.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		gr.setBackground(bg);
		gr.setForeground(fg);

		header = new Rectangle(0, 0, b.width, 40);

		gr.fillRectangle(header);
		gr.fillRectangle(new Rectangle(0, 0, 2, b.height));
		gr.fillRectangle(new Rectangle(0, b.height - 2, b.width, 2));
		gr.fillRectangle(new Rectangle(b.width - 2, 0, 2, b.height));
		gr.drawText(c.getName(), 4, 8);

		drawIcons(b, gr);

		drawMenu(b, gr);

		gr.setBackground(bbg);
		gr.setForeground(bfg);

		if (menuBar != null)
			gr.setTranslate(0, 80);
		else
			gr.setTranslate(0, 40);

	}

	private void drawMenu(Rectangle b, GC gr) {

		if (c.getMenuBar() != null) {

			Menu mb = c.getMenuBar();

			// System.out.println("MenuBar: " + mb);

			var bg = c.getDisplay().getSystemColor(SWT.COLOR_GREEN);
			gr.setBackground(bg);
			menuBar = new Rectangle(0, 41, b.width, 40);
			gr.fillRectangle(menuBar);
			gr.drawText(mb.getNameText(), 5, 45);



		}

	}

	private void drawIcons(Rectangle b, GC gr) {

		var bg = c.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		gr.setBackground(bg);

		var fg = c.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		gr.setForeground(fg);

		minimize = new Rectangle(b.width - 110, 5, 30, 30);
		maximize = new Rectangle(b.width - 75, 5, 30, 30);
		close = new Rectangle(b.width - 40, 5, 30, 30);

		gr.fillRectangle(minimize);
		gr.fillRectangle(maximize);
		gr.fillRectangle(close);

		gr.drawRectangle(minimize);
		gr.drawRectangle(new Rectangle(minimize.x + 4, minimize.y + 20,
				minimize.width - 8, minimize.height - 24));

		gr.drawRectangle(maximize);
		gr.drawRectangle(new Rectangle(maximize.x + 4, maximize.y + 4,
				maximize.width - 8, maximize.height - 8));

		gr.drawRectangle(close);
		gr.drawRectangle(new Rectangle(close.x + 4, close.y + 4,
				close.width - 8, close.height - 8));
		gr.drawLine(close.x + 4, close.y + 4, close.x + close.width - 4,
				close.y + close.height - 4);
		gr.drawLine(close.x + 4, close.y + (close.height - 4),
				close.x + close.width - 4, close.y + 4);

	}

}

