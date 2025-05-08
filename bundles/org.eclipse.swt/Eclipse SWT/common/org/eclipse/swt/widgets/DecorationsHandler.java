package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * This is a proof of concept for context menus and it could also be used as
 * template for popups.
 */
class DecorationsHandler implements Listener {

	private static final int MENU_HEIGHT = 40;
	private static final int HEADER_HEIGHT = 40;
	private static final int MENU_MARGIN_LEFT = 0;
	private static final int GAP = 5;

	private Color bgDecorationFocus;
	private Color fgDecoration;

	private Decorations decorations;
	private boolean mouseDownLeft = false;
	private boolean mouseDownRight = false;
	private boolean mouseDownBottom = false;
	private boolean mouseDownTop = false;
	private Point lastTopPosition = null;
	private boolean withFocus = true; // change the color if the shell loses
	// focus. Not yet implemented
	private Rectangle minimize = null;
	private Rectangle maximize = null;
	private Rectangle close = null;
	private Rectangle header = null;
	private Rectangle menuBar = null;
	private Color bgDecorationNoFocus;
	private Map<Rectangle, MenuItem> menuItems = new HashMap<>();
	private Rectangle highlightedMenuItem = null;

	public DecorationsHandler(Decorations c) {
		this.decorations = c;

		registerListeners(c);
	}

	private void registerListeners(Decorations dec) {

		dec.addListener(SWT.OPEN, this);
		dec.addListener(SWT.Paint, this);
		dec.addListener(SWT.Resize, this);
		dec.addListener(SWT.MouseMove, this);
		dec.addListener(SWT.MouseDown, this);
		dec.addListener(SWT.PaintItem, this);
		dec.addListener(SWT.MouseUp, this);

	}

	@Override
	public void handleEvent(Event e) {

		switch (e.type) {

		case SWT.Paint:
			drawDecoration(e);
			break;

		case SWT.Resize:
			// TODO positions of icons menu etc should be done in
			// setPostitions not in draw!!
			setPositions(e);
			break;

		case SWT.OPEN:
			setPositions(e);
			decorations.redraw();
			break;
		case SWT.MouseMove:
			handleMouseMove(e);
			break;
		case SWT.MouseDown:
			handleMouseDown(e);
			break;
		case SWT.MouseUp:
			handleMouseUp(e);
			break;
		case SWT.MouseEnter:
			handleMouseEnter(e);
			break;
		case SWT.MouseExit:
			handleMouseExit(e);
			break;
		}

	}

	private void setPositions(Event e) {

		if (!showsDecoration())
			return;

		Rectangle b = decorations.getBounds();
		header = new Rectangle(0, 0, b.width, HEADER_HEIGHT);

		minimize = new Rectangle(b.width - 110, 5, 30, 30);
		maximize = new Rectangle(b.width - 75, 5, 30, 30);
		close = new Rectangle(b.width - 40, 5, 30, 30);

		if (!showsMenuBar())
			return;

		menuBar = new Rectangle(0, HEADER_HEIGHT + 1, b.width, MENU_HEIGHT);

		menuItems.clear();

		Drawing.executeOnGC(decorations, gr -> {

			int width = MENU_MARGIN_LEFT;
			for (var i : decorations.getMenuBar().getItems()) {

				int prevWidth = width;

				String txt = replaceMnemonics(i.getText());

				var textExtent = gr.textExtent(txt);
				width += textExtent.x + 30;

				var r = new Rectangle(prevWidth + 5, HEADER_HEIGHT + 5, width - prevWidth - 10, HEADER_HEIGHT - 10);
				menuItems.put(r, i);
			}
			return null;

		});

	}

	private void handleMouseExit(Event e) {
		highlightedMenuItem = null;
		decorations.setCursor(null);
		decorations.redraw();
	}

	private void handleMouseEnter(Event e) {

	}

	private void handleMouseUp(Event e) {

		if (mouseDownLeft || mouseDownBottom || mouseDownRight || mouseDownTop)
			decorations.setCursor(null);

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
		} else if (onBottom(e)) {
			mouseDownBottom = true;
		} else if (onTop(e)) {

			if (minimize.contains(e.x, e.y)) {
				if (decorations instanceof Shell s) {
					s.setMinimized(true);
				}
			} else if (maximize.contains(e.x, e.y)) {
				if (decorations instanceof Shell s) {
					s.setMaximized(!s.getMaximized());
				}

			} else if (close.contains(e.x, e.y)) {
				if (decorations instanceof Shell s) {
					s.close();
				}
			} else {
				mouseDownTop = true;
				lastTopPosition = new Point(e.x, e.y);
			}
		} else if (onMenu(e)) {

			handleMenuMouseDown(e);

		}
	}

	private void handleMenuMouseDown(Event e) {

		for (var rec : menuItems.keySet()) {

			if (rec.contains(e.x, e.y)) {

				MenuItem item = menuItems.get(rec);
				executeMenuItem(e, rec, item);
				return;

			}

		}

	}

	private void executeMenuItem(Event e, Rectangle rec, MenuItem item) {

		if ((item.getStyle() & SWT.PUSH) != 0 || (item.getStyle() & SWT.TOGGLE) != 0) {
			item.sendSelectionEvent(SWT.Selection);

			if ((item.getStyle() & SWT.TOGGLE) != 0) {
				item.setSelection(!item.getSelection());
			}

		}

		if ((item.getStyle() & SWT.CASCADE) != 0) {
			new MenuWindow(decorations, item.getMenu().getItems(), new Point(rec.x, rec.y)).open();
		}
	}

	private boolean onMenu(Event e) {
		return menuBar != null && menuBar.contains(e.x, e.y);
	}

	private boolean onTop(Event e) {
		return e.y < 40;
	}

	private boolean onBottom(Event e) {
		var b = decorations.getBounds();

		return e.y > b.height - 5;
	}

	private boolean onRight(Event e) {
		var b = decorations.getBounds();
		return (e.x > b.width - 3 && e.y > 40);
	}

	private boolean onLeft(Event e) {
		return (e.x < 3 && e.y > 40);
	}

	private void handleMouseMove(Event e) {

		System.out.println("MouseMove");

		if (decorations.isDisposed() || decorations.display == null) {
			return;
		}

		if (!showsDecoration() && !showsMenuBar())
			return;

		if (mouseDownLeft) {

			var b = decorations.getBounds();
			decorations.setBounds(b.x + e.x, b.y, b.width - e.x, b.height);

		} else if (mouseDownRight) {
			var b = decorations.getBounds();
			decorations.setSize(e.x, b.height);
		} else if (mouseDownBottom) {
			var b = decorations.getBounds();
			decorations.setSize(b.width, e.y);
		} else if (mouseDownTop) {
			var l = decorations.getLocation();
			decorations.setLocation(l.x + e.x - lastTopPosition.x, l.y + e.y - lastTopPosition.y);
		}

		if (onLeft(e) || onRight(e)) {
			decorations.setCursor(decorations.getDisplay().getSystemCursor(SWT.CURSOR_SIZEW));
		} else if (onBottom(e)) {
			decorations.setCursor(decorations.getDisplay().getSystemCursor(SWT.CURSOR_SIZES));
		} else {
			decorations.setCursor(null);
		}

		if (onMenu(e)) {
			highlightMenuItem(e);
		} else {

			if (highlightedMenuItem != null) {
				highlightedMenuItem = null;
//		decorations.redraw();
			}
		}

	}

	private void highlightMenuItem(Event e) {

		if (highlightedMenuItem != null && highlightedMenuItem.contains(e.x, e.y + 8))
			return;

		for (var r : menuItems.keySet()) {
			if (r.contains(e.x, e.y + 8)) {
				highlightedMenuItem = r;
				decorations.redraw();
				return;
			}
		}

		if (highlightedMenuItem != null) {
			highlightedMenuItem = null;
			decorations.redraw();
		}

	}

	void drawDecoration(Event e) {

		if (!showsDecoration())
			return;

		GC gr = e.gc;

		Rectangle b = decorations.getBounds();

		if (b.width == 0 || b.height == 0)
			return;

		var bbg = gr.getBackground();
		var bfg = gr.getForeground();

		var bg = getBgDecorationColor();
		var fg = decorations.getDisplay().getSystemColor(SWT.COLOR_BLACK);

		gr.setBackground(bg);
		gr.setForeground(fg);

		if (header == null)
			return;

		gr.fillRectangle(header);
		// draw border
		gr.fillRectangle(new Rectangle(0, 0, 2, b.height));
		gr.fillRectangle(new Rectangle(0, b.height - 2, b.width, 2));
		gr.fillRectangle(new Rectangle(b.width - 2, 0, 2, b.height));
		gr.drawRectangle(new Rectangle(header.x, header.y, header.width - 1, header.height - 1));

		// end draw border

		var image = decorations.getImage();

		var images = decorations.getImages();

		int currentXPosition = 4;
		int currentYPosition = 8;

		for (var im : images) {
			if (images[0] != null) {
				gr.drawImage(images[0], currentXPosition, currentYPosition);

				currentXPosition += images[0].getBounds().width + GAP;

			}
		}

		gr.drawText(decorations.getText(), currentXPosition, currentYPosition);
		drawIcons(b, gr);
		drawMenu(b, gr);

		gr.setForeground(getFgColor());
		gr.drawRectangle(header.x, header.y, header.width - 1, b.height - 1);

		gr.setBackground(bbg);
		gr.setForeground(bfg);

	}

	private Color getFgColor() {
		return new Color(decorations.getDisplay(), new RGB(200, 200, 200));
	}

	private Color getBgDecorationColor() {

		if (withFocus) {

			if (bgDecorationFocus == null)
				bgDecorationFocus = new Color(decorations.getDisplay(), new RGB(233, 233, 225));

			return bgDecorationFocus;
		}

		if (bgDecorationNoFocus == null)
			bgDecorationNoFocus = new Color(decorations.getDisplay(), new RGB(233, 233, 225));

		return bgDecorationNoFocus;

	}

	private void drawMenu(Rectangle b, GC gr) {

		if (decorations.getMenuBar() != null) {

			Menu mb = decorations.getMenuBar();

			Color bg = getMenuBarColor();
			gr.setBackground(bg);
			menuBar = new Rectangle(0, 41, b.width, 40);
			gr.fillRectangle(menuBar);

			for (var en : menuItems.entrySet()) {

				var r = en.getKey();
				var i = en.getValue();


				if (highlightedMenuItem != null && highlightedMenuItem.equals(r)) {

					var col = decorations.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);

					Color beg = gr.getBackground();

					gr.setBackground(col);

					gr.fillRectangle(r);

					gr.drawText(replaceMnemonics(i.getText()), r.x + 8, r.y);
					gr.setBackground(beg);

				} else {
					if (i.getText() != null)
						gr.drawText(replaceMnemonics(i.getText()), r.x + 8, r.y);

				}

			}

			gr.drawLine(menuBar.x, menuBar.y + menuBar.height - 2, menuBar.x + menuBar.width,
					menuBar.y + menuBar.height - 2);

		}

	}

	private String replaceMnemonics(String text) {
		if (text == null)
			return "";
		int mnemonicIndex = text.lastIndexOf('&');
		if (mnemonicIndex != -1) {
			text = text.replaceAll("&", "");
			// TODO Underline the mnemonic key
		}
		return text;
	}

	private Color getMenuBarColor() {

		return decorations.getDisplay().getSystemColor(SWT.COLOR_WHITE);

	}

	private void drawIcons(Rectangle b, GC gr) {

		var bg = decorations.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		gr.setBackground(bg);

		var fg = decorations.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		gr.setForeground(fg);

		gr.fillRectangle(minimize);
		gr.fillRectangle(maximize);
		gr.fillRectangle(close);

		gr.drawRectangle(minimize);
		gr.drawRectangle(new Rectangle(minimize.x + 4, minimize.y + 20, minimize.width - 8, minimize.height - 24));

		gr.drawRectangle(maximize);
		gr.drawRectangle(new Rectangle(maximize.x + 4, maximize.y + 4, maximize.width - 8, maximize.height - 8));

		gr.drawRectangle(close);
		gr.drawRectangle(new Rectangle(close.x + 4, close.y + 4, close.width - 8, close.height - 8));
		gr.drawLine(close.x + 4, close.y + 4, close.x + close.width - 4, close.y + close.height - 4);
		gr.drawLine(close.x + 4, close.y + (close.height - 4), close.x + close.width - 4, close.y + 4);

	}

	public boolean showsDecoration() {

		if (decorations instanceof MenuWindow)
			return false;

		return true;
	}

	public boolean showsMenuBar() {

		if (decorations instanceof MenuWindow)
			return false;

		return decorations.getMenuBar() != null;
	}

	public void recalculateMenu() {

		setPositions(null);

	}

}
