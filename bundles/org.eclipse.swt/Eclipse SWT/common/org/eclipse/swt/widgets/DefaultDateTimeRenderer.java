package org.eclipse.swt.widgets;

import java.time.*;
import java.time.format.*;

//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.*;

public class DefaultDateTimeRenderer extends DateTimeRenderer {

	private static final int DEFAULT_DATETIME_WIDTH = 192;
	private static final int DEFAULT_DATETIME_HEIGHT = 28;
	private static final int DEFAULT_DATE_WIDTH = 102;// 72
	private static final int DEFAULT_DATE_HEIGHT = 28;// 19
	private static final int DEFAULT_TIME_WIDTH = 80;// 51
	private static final int DEFAULT_TIME_HEIGHT = 28;// 19
	private static final int DEFAULT_CALENDER_WIDTH = 300;// 286
	private static final int DEFAULT_CALENDER_HEIGHT = 200;// 183
	private static final int START_DRAWING_AT = 20;
	private static final int BORDER = 10;
	private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
	private static final Color FOREGROUND_COLOR = new Color(0, 0, 0);

	private String dateTimeText;
	private Rectangle dateTimeBounds;
	private Rectangle bar;

	protected DefaultDateTimeRenderer(CDateTime dateTime) {
		super(dateTime);
	}

	@Override
	protected void setDateTimeText(String text) {
		this.dateTimeText = text;
	}

	@Override
	public Point computeDefaultSize() {
		int width = 0;
		int height = 0;
		if (isDateTime()) {
			width = DEFAULT_DATETIME_WIDTH;
			height = DEFAULT_DATETIME_HEIGHT;
		} else if (isDate()) {
			width = DEFAULT_DATE_WIDTH;
			height = DEFAULT_DATE_HEIGHT;
		} else if (isTime()) {
			width = DEFAULT_TIME_WIDTH;
			height = DEFAULT_TIME_HEIGHT;
		} else if (isCalender()) {
			width = DEFAULT_CALENDER_WIDTH;
			height = DEFAULT_CALENDER_HEIGHT;
		}
		return new Point(width, height);
	}

	/**
	 * Paints the DateTime with the specified width and height. The method sets the
	 * background and fills the rectangle defined by the DateTime bounds.
	 *
	 * @param gc     the graphics context used for painting.
	 * @param width  the width of the DateTime.
	 * @param height the height of the DateTime.
	 */
	@Override
	protected void paint(GC gc, int widthx, int heighty) {
		int width = 0;
		int height = 0;
		Point extent = gc.textExtent(dateTimeText);
		width = extent.x;
		height = extent.y;

		bar = new Rectangle(START_DRAWING_AT, START_DRAWING_AT, START_DRAWING_AT + extent.x,
				START_DRAWING_AT + extent.y);
//		gc.setForeground(FOREGROUND_COLOR);
//		gc.setLineWidth(2);
		gc.drawRectangle(bar.x - BORDER / 2, bar.y - BORDER / 2, bar.width - BORDER, bar.height - BORDER);
		gc.drawText(dateTimeText, START_DRAWING_AT, START_DRAWING_AT, true);
//		gc.setBackground(BACKGROUND_COLOR);
//		gc.fillRectangle(dateTimeBounds);
	}

	/**
	 * Sets the bounds of the Sash.
	 *
	 * @param lastX  the x-coordinate of the Sash.
	 * @param lastY  the y-coordinate of the Sash.
	 * @param width  the width of the Sash.
	 * @param height the height of the Sash.
	 */
	@Override
	protected void setDateTimeBounds(int lastX, int lastY, int width, int height) {
		dateTimeBounds = new Rectangle(lastX, lastY, width, height);
	}

	/**
	 * Gets the bounds of the Sash.
	 *
	 * @return a Rectangle representing the bounds of the Sash.
	 */
	@Override
	public Rectangle getDateTimeBounds() {
		return dateTimeBounds;
	}

	@Override
	public Color getDefaultBackground() {
		return getColor(COLOR_BACKGROUND);
	}

	@Override
	public Color getDefaultForeground() {
		return getColor(COLOR_FOREGROUND);
	}
}
