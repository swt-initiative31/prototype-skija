/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class CDateTime extends CustomComposite {
	static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	static final int MAX_YEAR = 9999;
	static final int INCREMENT = 1;
	static final int PAGEINCREMENT = 10;
	static final int LEFT_MARGIN = 20;
	static final int TOP_MARGIN = 20;

	private LocalDateTime dateTime = LocalDateTime.now();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	/*
	 * yyyy - year, MM - Month, MMMM - Month full name, dd - Date, HH - Hours, mm - Minutes, ss - Seconds
	 */
	private Label displayLabel;
//	private Text dateTimeText;
	private String dateTimeText;
	private final DateTimeRenderer renderer;
	private int x;
	private int y;
	private int width;
	private int height;


	public CDateTime(Composite parent, int style) {
		super(parent, checkStyle (style));
		this.style |= SWT.DOUBLE_BUFFERED;
//		createContent();// using label widget

//		Using Text widget
//		dateTimeText = new Text(parent, SWT.NONE);
//		if ((this.style & (SWT.DATE | SWT.TIME)) != 0) {
//			dateTimeText.setText(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//		} else if ((this.style & SWT.DATE) != 0) {
//			  dateTimeText.setText(dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//		} else if ((this.style & SWT.TIME) != 0) {
//			  dateTimeText.setText(dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//		} else if ((this.style & SWT.CALENDAR) != 0) { //
//			  dateTimeText.setText(dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

		// using String
		if ((this.style & SWT.DATE) != 0) {
			dateTimeText = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		} else if ((this.style & SWT.TIME) != 0) {
			dateTimeText = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		} else if ((this.style & (SWT.DATE | SWT.TIME)) != 0) {
			dateTimeText = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} else if ((this.style & SWT.CALENDAR) != 0) {
			// dateTimeText = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			// TODO Deepika
		}
//		createText();
//		createButtons();
//		addListeners();
		Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.Resize -> onResize();
//			case SWT.MouseMove -> onMouseMove(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseUp -> onMouseUp(event);
//			case SWT.MouseWheel -> onMouseWheel(event);
//			case SWT.KeyDown -> keyPressed(event);
			case SWT.Traverse -> onTraverse(event);
			case SWT.FocusIn -> onFocusIn(event);
			case SWT.FocusOut -> onFocusOut(event);
//			case SWT.Gesture -> onGesture(event);
			case SWT.Dispose -> onDispose(event);
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
//		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
//		addListener(SWT.MouseWheel, listener);
//		addListener(SWT.KeyDown, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
//		addListener(SWT.Gesture, listener);
		addListener(SWT.Dispose, listener);

		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = rendererFactory.createDateTimeRenderer(this);
		renderer.setDateTimeText(dateTimeText);
	}

	private void createContent() {
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
//		gd.widthHint = 300; // Optional: Minimum width
		gd.horizontalIndent = 150;
		gd.verticalIndent = 150;
		this.setLayoutData(gd);
		// Date/time label
		displayLabel = new Label(this, SWT.BORDER);
		displayLabel.setText(dateTime.format(formatter));// formatDateTime()
		displayLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		displayLabel.setAlignment(SWT.CENTER);

		// Adjust size of the label
		displayLabel.setSize(135, 20);
	}

	protected Point computeDefaultSize() {
		return renderer.computeDefaultSize();
	}

	@Override
	Point computeSizeInPixels(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = 300, height = 200;
		return new Point(width, height);
	}

	private void onPaint(Event event) {
		Drawing.drawWithGC(this, event.gc, renderer::paint);
	}

	private void onResize() {
		redraw();
	}

	private void onMouseDown(Event event) {
//		if (event.character == SWT.SPACE) {
//			renderer.setPressed(true);
//			redraw();
//		}
		redraw();// TODO
	}

	private void onMouseUp(Event event) {
//		if (event.character == SWT.SPACE) {
//			renderer.setPressed(false);
//			handleSelection();
//			redraw();
//		}
		redraw();// TODO
	}

	@Override
	public void redraw(int x, int y, int width, int height, boolean all) {
		super.redraw(x, y, width, height, true);
	}

	public boolean isDate() {
		return (style & SWT.DATE) != 0;
	}

	public boolean isTime() {
		return (style & SWT.TIME) != 0;
	}

	public boolean isDateTime() {
		return (style & SWT.DATE & SWT.TIME) != 0;
	}

	public boolean isCalender() {
		return (style & SWT.CALENDAR) != 0;
	}

	private void onTraverse(Event event) {
		// TODO
	}

	private void onDispose(Event event) {
		dispose();
	}

	private void onSelection(Event event) {
		redraw();
	}

	private void onFocusIn(Event event) {
		// TODO
		redraw();
	}

	private void onFocusOut(Event event) {
		// TODO
		redraw();
	}

	private String formatDateTime() {
		return dateTime.format(formatter);

	}

	public String getDateTime() {
		return dateTime.format(formatter).toString();
	}

	public void setDateTime(LocalDateTime newTime) {
		this.dateTime = newTime;
		// refreshLabel(); TODO
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass())
			error(SWT.ERROR_INVALID_SUBCLASS);
	}

	private void refreshLabel() {
		displayLabel.setText(formatDateTime());
		layout(); // TODO
	}

	/**
	 * This method returns the `DateTimeRenderer` instance used for rendering the
	 * DateTime.
	 *
	 * @return the `ControlRenderer` instance for the DateTime
	 */
	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}

	@Override
	public void setBounds(Rectangle rect) {
		if (rect == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		checkWidget();
		if (rect.x == this.x && rect.y == this.y && rect.width == this.width && rect.height == this.height) {
			return;
		}

		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
		super.setBounds(rect);
		redraw();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
	}

	private void handleSelection() {
//		if (isDate()) {
//			forceFocus();
//			selectRadio();
//		} else if ((style & SWT.PUSH) == 0 && (style & (SWT.TOGGLE | SWT.CHECK)) != 0) {
//			setSelection(!checked);
//		}
		sendSelectionEvent(SWT.Selection);
	}

	@Override
	public boolean forceFocus() {
		boolean b = super.forceFocus();
//		if (b && isRadioButton()) { // if a radio button gets focus, then all
//									// other radio buttons of the group lose the
//									// selection
//			selectRadio(/* withFocus = */false);
//		}
		return b;
	}

	private Event sendSelectionEvent(Rectangle dateTimeBounds) {
		Event event = new Event();
		event.setBounds(new Rectangle(dateTimeBounds.x, dateTimeBounds.y, dateTimeBounds.width, dateTimeBounds.height));
		sendSelectionEvent(SWT.Selection, event, true);
		return event;
	}

	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	public void addModifyListener(ModifyListener listener) {
		addTypedListener(listener, SWT.Modify);
	}

	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Modify, listener);
	}

	public int getYear() {
		return dateTime.getYear();
	}

	public String getMonth() {
//		return dateTime.getMonth().toString();
//		return dateTime.format(DateTimeFormatter.ofPattern("MMMM")).toUpperCase();
		return dateTime.format(DateTimeFormatter.ofPattern("MMMM"));
	}

	public int getDayOfYear() {
		return dateTime.getDayOfYear();
	}

	public int getDayOfMonth() {
		return dateTime.getDayOfMonth();
	}

	public String getDayOfWeek() {
		return dateTime.getDayOfWeek().toString();
	}

	public int getHour() {
		return dateTime.getHour();
	}

	public int getMinute() {
		return dateTime.getMinute();
	}

	public int getSecond() {
		return dateTime.getSecond();
	}

	public String getCustomShortTimeFormat() { // actual getCustomShortTimeFormat
		return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	public String getCustomMediumTimeFormat() {
		return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	public String getCustomLongTimeFormat() {
		return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	public String getCustomShortDateFormat() {
//		return dateTime.getMonth().toString() + "," + dateTime.getYear();
//		return dateTime.format(DateTimeFormatter.ofPattern("MMMM,yyyy")).toUpperCase();
		return dateTime.format(DateTimeFormatter.ofPattern("MMMM,yyyy"));
	}

	public String getCustomMediumDateFormat() { // actual getCustomShortDateFormat
		return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	public String getCustomLongDateFormat() {
//		return dateTime.getDayOfMonth() + "," + dateTime.getMonth().toString() + "," + dateTime.getYear();
//		return dateTime.format(DateTimeFormatter.ofPattern("dd,MMMM,yyyy")).toUpperCase();
		return dateTime.format(DateTimeFormatter.ofPattern("dd,MMMM,yyyy"));
	}

	static int checkStyle(int style) {
		style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
		style = checkBits(style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
		style = checkBits(style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
		if ((style & SWT.DATE) == 0)
			style &= ~SWT.DROP_DOWN;
		return style;
	}
}