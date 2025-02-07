package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 *  This is a proof of concept for context menus and it could also be used as template for popups.
 */
public class MenuWindow extends Shell implements Listener, DisposeListener {

    public final static int RIGHT_MARGIN = 50;
    public final static int LEFT_MARGIN = 30;
    public final static int TOP_MARGIN = 5;
    public final static int BOTTOM_MARGIN = 5;
    private static final int IMG_LEFT_MARGIN = 5;

    Font menuWindowFont;

    private Point size;
    int[] lineHeights = null;
    Rectangle[] itemRect = null;
    int mouseOverItem = -1;

    Control parent;
    private MenuItem[] items;

    private int openedChildMenuIndex = -1;
    private MenuWindow openedChildMenu = null;

    public MenuWindow(Control c, MenuItem[] items, Point p, GC gc) {
	super(c.getShell(), SWT.DIALOG_TRIM | SWT.ON_TOP | SWT.NO_MOVE | SWT.NO_TRIM);

	parent = c;
	this.items = items;
	if (items == null || items.length == 0)
	    return;

	computeSize(gc);

	setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

	var monitorPosition = c.toDisplay(p);

	setLocation(monitorPosition);

	setSize(size);
	display.addFilter(SWT.MouseDown, this);

	this.addListener(SWT.MouseMove, this);
	this.addListener(SWT.Paint, this);

    }

    private Point calculateRelativeShellPosition(Control parent2) {

	if (parent2 instanceof Shell)
	    return parent2.getLocation();

	var p = calculateRelativeShellPosition(parent2.getParent());

	return new Point(p.x + parent2.getLocation().x, p.y + parent2.getLocation().y);


    }

    @Override
    public void open() {

	if (this.items == null || items.length == 0)
	    return;

	super.open();

    }

    private void computeSize(GC none) {

	Drawing.executeOnGC(this, gr -> {

	    var font = getDisplay().getSystemFont();
	    var fd = font.getFontData()[0];
	    FontData d = new FontData(fd.getName(), 6, fd.getStyle());
	    menuWindowFont = new Font(getDisplay(), d);
	    gr.setFont(menuWindowFont);

	    Rectangle b = getBounds();
	    Rectangle ca = getClientArea();

	    int width = LEFT_MARGIN + RIGHT_MARGIN;

	    lineHeights = new int[items.length];
	    itemRect = new Rectangle[items.length];

	    int k = 0;
	    int height = b.height - ca.height;
	    int initialHeight = height;
	    for (var i : items) {

		height++; // border

		if (i.style == SWT.SEPARATOR) {

		    itemRect[k] = new Rectangle(1, height - initialHeight, 0, 1);
		    height += 1;

		    k++;
		    continue;
		}

		Point te = gr.textExtent(replaceMnemonics(i.getText()));
		itemRect[k] = new Rectangle(1, height - initialHeight, 0, te.y + TOP_MARGIN + BOTTOM_MARGIN);
		height += itemRect[k].height + 1;

		width = Math.max(width, te.x + LEFT_MARGIN + RIGHT_MARGIN + 2);

		System.out.println(k + "-th iteration, width: " + width);

		k++;

	    }

	    k = 0;
	    for (var r : itemRect) {

		itemRect[k] = new Rectangle(r.x, r.y, width - 2, r.height);
		k++;
	    }

	    size = new Point(width, height);

	    return null;

	});
    }

    @Override
    public void handleEvent(Event e) {

	if (e.type == SWT.Paint) {
	    drawMenuSelection(e);
	    return;

	}
	if (e.type == SWT.MouseMove) {

	    for (var r : itemRect) {
		if (r.contains(e.x, e.y)) {
		    handleItemOver(r, e);
		}
	    }
	    return;
	}

	if (e.type == SWT.MouseDown) {
	    System.out.println("Handle MouseDown: " + e);
	    System.out.println("Disposed: " + isDisposed() + "  " + this.getClass().getName());
	    if (isDisposed()) {
		if (display != null) {
		    display.removeListener(SWT.MouseDown, this);
		    display.removeFilter(SWT.MouseDown, this);
		}
		System.out.println("Remove listener");
	    } else {

		System.out.println(
			"Bounds: " + getBounds() + "  " + e.x + "/" + e.y + "  " + e.widget.getClass().getName());

		if (e.widget != null && e.widget.equals(this)) {
		    System.out.println("triggerevent and close");

		    for (var r : itemRect) {
			if (r.contains(e.x, e.y)) {
			    var ind = getIndex(r);
			    if (ind >= 0 && ind < items.length) {
				var i = items[ind];
				i.sendSelectionEvent(SWT.Selection);

			    }
			}
		    }

		    close();
		    parent.setFocus();
		    e.doit = false;

		} else {
		    System.out.println("Event not triggered, not right widget...");
		    close();
		    e.doit = false;
		    parent.setFocus();

		}

//				if (getBounds().contains(e.x, e.y)) {
//				} else {
//				}

	    }

	    return;

	}

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
    public void close() {
	if (display != null) {
	    display.removeFilter(SWT.MouseDown, this);
	    display.removeListener(SWT.MouseDown, this);
	}
	super.close();
    }

    @Override
    public void dispose() {
	if (display != null) {
	    display.removeFilter(SWT.MouseDown, this);
	    display.removeListener(SWT.MouseDown, this);
	}

	if (menuWindowFont != null && !menuWindowFont.isDisposed())
	    menuWindowFont.dispose();
	if (!isDisposed())
	    super.dispose();
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
	    var pos = new Point(itemRect[index].x + itemRect[index].width + 2, itemRect[index].y);
	    openedChildMenuIndex = index;
	    openedChildMenu = new MenuWindow(this, its, pos, e.gc);
	    openedChildMenu.addDisposeListener(this);
	    openedChildMenu.open();
	}
	redraw();

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

	    e.gc.setFont(menuWindowFont);

	    Color gray = getDisplay().getSystemColor(SWT.COLOR_GRAY);
	    Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
	    Color selectedBg = getDisplay().getSystemColor(SWT.COLOR_YELLOW);
	    Color defaultBg = e.gc.getBackground();
	    Color defaultFg = e.gc.getForeground();
	    Color disabledText = getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);

	    e.gc.setForeground(gray);
	    e.gc.drawRectangle(0, 0, size.x - 1, size.y - 1);

	    e.gc.setFont(menuWindowFont);

	    int k = 0;

	    for (var i : items) {

		var r = itemRect[k];

		if (i.style == SWT.SEPARATOR) {

		    e.gc.setForeground(gray);
		    e.gc.drawRectangle(new Rectangle(r.x + 4, r.y, r.width - 8, r.height));
		    e.gc.setForeground(black);

		    k++;
		    continue;
		}

		if (mouseOverItem == k) {
		    e.gc.setBackground(selectedBg);
		    e.gc.fillRectangle(r);

		}

		if (i.getImage() != null) {
		    Image img = i.getImage();
		    e.gc.drawImage(img, IMG_LEFT_MARGIN, r.y + r.height / 2 - img.getBounds().height / 2);
		}

		String txt = replaceMnemonics(i.getText());

		// TODO enable and disable is not possible with the classical MenuItem. Override
		// of the classical OS-dependent MenuItem is necessary
		if (i.isEnabled())
		    e.gc.setForeground(black);
		else
		    e.gc.setForeground(disabledText);

		e.gc.drawText(txt, LEFT_MARGIN + 1, TOP_MARGIN + r.y);
		e.gc.setForeground(gray);

		if ((i.getStyle() & SWT.CASCADE) != 0) {

		    Point rightMiddle = new Point(r.x + r.width, r.y + r.height / 2);

		    e.gc.drawLine(rightMiddle.x - 20, rightMiddle.y, rightMiddle.x - 10, rightMiddle.y);

		}
		e.gc.setForeground(defaultFg);
		e.gc.setBackground(defaultBg);
		k++;
	    }
	    e.gc.setForeground(black);

	}

    }

    private String replaceMnemonics(String text) {
	int mnemonicIndex = text.lastIndexOf('&');
	if (mnemonicIndex != -1) {
	    text = text.replaceAll("&", "");
	    // TODO Underline the mnemonic key
	}
	return text;
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