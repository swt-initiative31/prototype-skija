package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class TreeItemRenderer {

	/** Gap between icon and text */
	private static final int GAP = 3;
	/** Left and right margins */
	private static final int DEFAULT_MARGIN = 3;
	/** Left and right margins */
	private static final int DEFAULT_MARGIN_UP_DOWN = 2;
	/** a string inserted in the middle of text that has been shortened */

	private static final int leftMargin = 3;
	private static final int rightMargin = DEFAULT_MARGIN;
	private static final int topMargin = DEFAULT_MARGIN_UP_DOWN;
	private static final int bottomMargin = DEFAULT_MARGIN_UP_DOWN;
	private static final int INDENT_WIDTH = 10;

	private final TreeItem item;
	private boolean selected;
	private boolean hovered;
	Rectangle checkboxBounds;

	private final Map<Integer, Point> computedCellSizes = new HashMap<>();
	private final Map<Integer, Rectangle> internalComputedCellTextBounds = new HashMap<>();
	private Point computedSize;
	Rectangle arrowBounds;

	public TreeItemRenderer(TreeItem tableItem) {
		this.item = tableItem;
	}

	void doPaint(GC gc) {
		Rectangle b = item.getBounds();

		Color bgBefore = gc.getBackground();

		System.out.println(getParent().selectedTreeItems);
		if (getParent().selectedTreeItems.contains(item)) {
			this.selected = true;
			System.out.println("selected: " + item.getText());

			gc.setBackground(Tree.SELECTION_COLOR);
			gc.fillRectangle(b);
			gc.drawRectangle(new Rectangle(b.x, b.y, b.width - 1, b.height - 1));
			gc.drawRectangle(b);
		} else if (getParent().mouseHoverElement == item) {
			this.hovered = true;
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
			gc.fillRectangle(b);
		} else {
			this.selected = false;
			this.hovered = false;
		}

		drawArrowArea(gc);
		drawCheckbox(gc);

		Tree parent = getParent();
		if (parent.columnsExist()) {
			final boolean paintItemEvent = parent.hooks(SWT.PaintItem);
			for (int i = 0; i < parent.getColumnCount(); i++) {
				drawItemCell(gc, i, paintItemEvent);
			}
		} else {
			drawItem(gc);
		}

		gc.setBackground(bgBefore);
	}

	private void drawArrowArea(GC gc) {

		var itemBounds = item.getFullBounds();

		this.arrowBounds = new Rectangle(itemBounds.x + 5, itemBounds.y + 3, 20, 20);

		var prev = gc.getBackground();
		try {

			gc.setBackground(item.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			gc.fillRectangle(this.arrowBounds);

			if (item.getItemCount() == 0)
				return;

			if (item.getExpanded()) {

				drawArrow(gc, this.arrowBounds, SWT.DOWN);

			} else {
				drawArrow(gc, this.arrowBounds, SWT.RIGHT);
			}
		} finally {
			gc.setBackground(prev);
		}

	}

	private void drawArrow(GC gc, Rectangle arrowBounds2, int style) {

		gc.setBackground(item.getDisplay().getSystemColor(SWT.COLOR_RED));

		int width = arrowBounds2.width;
		int height = arrowBounds2.height;

		int centerHeight = height / 2;
		int centerWidth = width / 2;

		// TODO: in the next version use a bezier path

		int[] curve;
		if ((style & SWT.DOWN) != 0) {
			curve = new int[] { centerWidth, centerHeight + 5, centerWidth - 5, centerHeight - 5, centerWidth + 5,
					centerHeight - 5 };

		} else if ((style & SWT.LEFT) != 0) {
			curve = new int[] { centerWidth - 5, centerHeight, centerWidth + 5, centerHeight + 5, centerWidth + 5,
					centerHeight - 5 };

		} else if ((style & SWT.RIGHT) != 0) {
			curve = new int[] { centerWidth + 5, centerHeight, centerWidth - 5, centerHeight - 5, centerWidth - 5,
					centerHeight + 5 };

		} else {
			curve = new int[] { centerWidth, centerHeight - 5, centerWidth - 5, centerHeight + 5, centerWidth + 5,
					centerHeight + 5 };
		}

		curve = translateCurve(arrowBounds2.x, arrowBounds2.y, curve);

		gc.fillPolygon(curve);
	}


	private int[] translateCurve(int x, int y, int[] curve) {

		int[] newCurve = new int[curve.length];

		for (int i = 0; i < curve.length / 2; i++) {
			int px = curve[2 * i];
			int py = curve[2 * i + 1];

			newCurve[2 * i] = px + x;
			newCurve[2 * i + 1] = py + y;

		}

		return newCurve;
	}

	private Tree getParent() {
		return item.getParent();
	}

	private void drawCheckbox(GC gc) {
		if ((getParent().getStyle() & SWT.CHECK) == 0)
			return;

		var itemBounds = item.getFullBounds();

		this.checkboxBounds = new Rectangle(arrowBounds.x + arrowBounds.width + 5, itemBounds.y + 3, 20, 20);

		gc.drawRectangle(this.checkboxBounds);
		if (item.getChecked()) {
			gc.drawLine(this.checkboxBounds.x, this.checkboxBounds.y, this.checkboxBounds.x + this.checkboxBounds.width,
					this.checkboxBounds.y + this.checkboxBounds.height);
			gc.drawLine(this.checkboxBounds.x + this.checkboxBounds.width, this.checkboxBounds.y, this.checkboxBounds.x,
					this.checkboxBounds.y + this.checkboxBounds.height);
		}
	}

	private void drawItemCell(GC gc, int columnIndex, boolean paintItemEvent) {
		var b = getBounds(columnIndex);
		gc.setClipping(b);

		final Tree parent = getParent();
		if (paintItemEvent) {
			Event event = new Event();
			event.item = item;
			event.index = columnIndex;
			event.gc = gc;
			event.x = b.x;
			event.y = b.y;
			parent.sendEvent(SWT.PaintItem, event);
			return;
		}

		var prevBG = gc.getBackground();
		var bgCell = item.getBackground(columnIndex);

		if (bgCell != null && !this.selected && !this.hovered) {
			gc.setBackground(bgCell);
			gc.fillRectangle(b);
		}

		int currentWidthPosition = b.x + leftMargin;

		int xPosition = currentWidthPosition;
		int yPosition = b.y + topMargin;

		var image = item.getImage(columnIndex);
		if (image != null) {
			if (Tree.FILL_IMAGE_AREAS) {
				var pBG = gc.getBackground();

				gc.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
				var imgB = image.getBounds();
				var rec = new Rectangle(imgB.x + xPosition, imgB.y + yPosition, imgB.width, imgB.height);
				gc.fillRectangle(rec);

				gc.setBackground(pBG);
			}

			if (Tree.DRAW_IMAGES) {
				gc.drawImage(image, xPosition, yPosition);
			}
			currentWidthPosition += image.getBounds().width + GAP;
		}

		var prevFG = gc.getForeground();
		var fgCol = item.getForeground(columnIndex);
		if (fgCol != null && !this.selected && !this.hovered) {
			gc.setForeground(fgCol);
		}

		xPosition = currentWidthPosition;
		yPosition = b.y + topMargin;

		if (Tree.FILL_TEXT_AREAS) {
			var pBG = gc.getBackground();
			gc.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			var textSize = parent.computeTextExtent(item.getText(columnIndex));
			var rec = new Rectangle(xPosition, yPosition, textSize.x, textSize.y);
			gc.fillRectangle(rec);
			gc.setBackground(pBG);
		}

		if (Tree.DRAW_TEXTS) {
			gc.drawText(item.getText(columnIndex), currentWidthPosition, b.y + topMargin);
		}

		gc.setForeground(prevFG);
		gc.setBackground(prevBG);
		gc.setClipping((Rectangle) null);
	}

	private Rectangle getBounds(int columnIndex) {
		return item.getBounds(columnIndex);
	}

	private void drawItem(GC gc) {
		var b = item.getBounds();

		var prevBG = gc.getBackground();
		var bgCol = item.getBackground();
		if (bgCol != null && !this.selected && !this.hovered) {
			gc.setBackground(bgCol);
			gc.fillRectangle(b);
		}

		int xPosition = b.x + leftMargin;

		xPosition += INDENT_WIDTH * item.getIndent();

		int yPosition = b.y + topMargin;

		var image = item.getImage();
		if (image != null) {
			if (Tree.FILL_IMAGE_AREAS) {
				var pBG = gc.getBackground();
				gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_RED));
				var imgB = item.getImage().getBounds();
				var rec = new Rectangle(imgB.x + xPosition, imgB.y + yPosition, imgB.width, imgB.height);
				gc.fillRectangle(rec);
				gc.setBackground(pBG);
			}

			if (Tree.DRAW_IMAGES) {
				gc.drawImage(image, xPosition, yPosition);
			}
			xPosition += image.getBounds().width + GAP;
		}

		var prevFG = gc.getForeground();
		var fgCol = item.getForeground();
		if (fgCol != null && !this.selected && !this.hovered) {
			gc.setForeground(fgCol);
		}

		yPosition = b.y + topMargin;

		if (Tree.FILL_TEXT_AREAS) {
			var pBG = gc.getBackground();
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			var textSize = getParent().computeTextExtent(item.getText());
			var rec = new Rectangle(xPosition, yPosition, textSize.x, textSize.y);
			gc.fillRectangle(rec);
			gc.setBackground(pBG);
		}

		if (Tree.DRAW_TEXTS) {
			gc.drawText(item.getText(), xPosition, b.y + topMargin);
		}

		gc.setForeground(prevFG);
		gc.setBackground(prevBG);
	}

	public Point computeCellSize(int colIndex) {
		final Point cellSize = computedCellSizes.get(colIndex);
		if (cellSize != null) {
			return cellSize;
		}

		var image = item.getImage(colIndex);

		int height = topMargin + bottomMargin;
		int width = leftMargin + rightMargin;

		if (image != null) {
			final Rectangle bounds = image.getBounds();
			height += bounds.height;
			width += bounds.width;
		}

		var text = item.getText(colIndex);
		if (text != null) {
			var size = getParent().computeTextExtent(text);

			var rec = new Rectangle(width, height, size.x, size.y);
			internalComputedCellTextBounds.put(colIndex, rec);

			width += size.x;
			height += size.y;
		} else {
			internalComputedCellTextBounds.put(colIndex, new Rectangle(width, height, 0, 0));
		}

		if (image != null && text != null) {
			width += GAP;
		}

		var p = new Point(width, height);

		computedCellSizes.put(colIndex, p);

		return p;
	}

	/**
	 *
	 * TODO: The text extent calls cause da drastic performance decrease with skija.
	 * This should be improved: no height calculations, just use the default height
	 * from textMetrics. And check for multiple lines of text.
	 *
	 */
	Point computeSize(boolean changed) {
		if (!changed && this.computedSize != null) {
			return computedSize;
		}

		int lineHeight = 0;
		int imageHeight = 0;

		int width = leftMargin + rightMargin;

		// guess the line height for the text. Currently only support for one line

		lineHeight = guessItemHeight(getParent());

//		if (text != null && !text.isEmpty()) {
//			Point textExtent = computeTextExtent();
//			lineHeight = textExtent.y;
//			width += textExtent.x;
//		}
//
//		if (getParent().getColumnCount() > 0) {
//			for (int c = 0; c < getParent().getColumnCount(); c++) {
//				Point textExtent = computeTextExtent(c);
//				lineHeight = Math.max(lineHeight, textExtent.y);
//			}
//		}

		if (item.images != null) {
			for (var i : item.images) {
				if (i == null) {
					continue;
				}
				Rectangle imageBounds = i.getBounds();
				imageHeight = Math.max(imageBounds.height, imageHeight);
//				width += imageBounds.width;
//
//				if (text != null)
//					width += GAP;

			}
		} else if (item.image != null) {
			Rectangle imageBounds = item.image.getBounds();
			imageHeight = Math.max(imageBounds.height, imageHeight);
			width += imageBounds.width;
			if (item.text != null) {
				width += GAP;
			}
		}

		int height = topMargin + Math.max(lineHeight, imageHeight) + bottomMargin;

		if (getParent().getColumnCount() > 0) {
			width = getParent().getTotalColumnWidth();
		} else {
			Point textExtent = getParent().computeTextExtent(item.getText());
			lineHeight = textExtent.y;
			width += textExtent.x;
			width += INDENT_WIDTH * item.getIndent();
		}

		this.computedSize = new Point(width, height);

		return this.computedSize;
	}

	public void clearCache() {
		this.computedCellSizes.clear();
		this.internalComputedCellTextBounds.clear();
		computedSize = null;
	}

	public static int guessItemHeight(Tree table) {
		int textHeight = Tree.guessTextHeight(table);
		return textHeight + topMargin + bottomMargin;
	}

	public Rectangle getTextBounds(int index) {

		if (internalComputedCellTextBounds.get(index) == null)
			computeCellSize(index);

		var internal = internalComputedCellTextBounds.get(index);

		var outer = getBounds(index);

		return new Rectangle(outer.x + internal.x, outer.y + internal.y, internal.width, internal.height);

	}
}
