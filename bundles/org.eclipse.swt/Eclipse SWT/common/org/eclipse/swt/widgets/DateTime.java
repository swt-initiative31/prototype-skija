/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
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

import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

public class DateTime extends Composite {
	static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	static final int MAX_YEAR = 9999;
	private static int INCREMENT = 1;
	private static boolean dayUpdate, monthUpdate, yearUpdate, hourUpdate, minuteUpdate, secondUpdate;

	private Text text;
	private ArrowButtons arrows;
	private int year;
	private int day;
	private int month;
	private int hours;
	private int minutes;
	private int seconds;

	private void init() {
		LocalDateTime dateTime = LocalDateTime.now();
		year = dateTime.getYear();
		day = dateTime.getDayOfMonth();
		month = dateTime.getMonthValue() - 1;
		hours = dateTime.getHour();
		minutes = dateTime.getMinute();
		seconds = dateTime.getSecond();

	}

	private static final class ArrowButtons extends Composite {
		private final Button arrowUp, arrowDown;

		public ArrowButtons(DateTime parent2, int style) {
			super(parent2, style);
			int arrowStyle = SWT.ARROW;
			if ((style & SWT.FLAT) != 0)
				arrowStyle |= SWT.FLAT;

			setLayout(new GridLayout(1, false));
			setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true));

			arrowUp = createArrowButton(arrowStyle | SWT.UP, SelectionListener.widgetSelectedAdapter(this::increment));
			arrowDown = createArrowButton(arrowStyle | SWT.DOWN,
					SelectionListener.widgetSelectedAdapter(this::decrement));
		}

		private Button createArrowButton(int style, SelectionListener selectionListener) {
			Button b = new Button(this, style);
			b.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			b.addSelectionListener(selectionListener);
			return b;
		}

		private void increment(SelectionEvent e) {
			DateTime dt = (DateTime) ((Button) e.widget).getParent().getParent();

			var dateTime = dt.getLocalDateTime();

			// TODO - add any checks when calender is implemented if required
			if (dayUpdate) {
				dateTime = dateTime.plusDays(INCREMENT);
			} else if (monthUpdate) {
				dateTime = dateTime.plusMonths(INCREMENT);
			} else if (yearUpdate) {
				dateTime = dateTime.plusYears(INCREMENT);
			} else if (hourUpdate) {
				dateTime = dateTime.plusHours(INCREMENT);
			} else if (minuteUpdate) {
				dateTime = dateTime.plusMinutes(INCREMENT);
			} else if (secondUpdate) {
				dateTime = dateTime.plusSeconds(INCREMENT);
			}
			dt.setDateTime(dateTime);
			dt.updateText();
		}

		private void decrement(SelectionEvent e) {
			DateTime dt = (DateTime) ((Button) e.widget).getParent().getParent();

			var dateTime = LocalDateTime.of(dt.getYear(), dt.getMonth() + 1, dt.getDay(), dt.getHours(),
					dt.getMinutes());

			// TODO - add any checks when calender is implemented if required
			if (dayUpdate) {
				dateTime = dateTime.minusDays(1);
			} else if (monthUpdate) {
				dateTime = dateTime.minusMonths(INCREMENT);
			} else if (yearUpdate) {
				dateTime = dateTime.minusYears(INCREMENT);
			} else if (hourUpdate) {
				dateTime = dateTime.minusHours(INCREMENT);
			} else if (minuteUpdate) {
				dateTime = dateTime.minusMinutes(INCREMENT);
			} else if (secondUpdate) {
				dateTime = dateTime.minusSeconds(INCREMENT);
			}
			dt.setDateTime(dateTime);
			dt.updateText();
		}

		@Override
		public void setEnabled(boolean enabled) {
			arrowUp.setEnabled(enabled);
			arrowDown.setEnabled(enabled);
		}

		@Override
		void releaseHandle() {
			arrowUp.releaseHandle();
			arrowDown.releaseHandle();
		}
	}

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#READ_ONLY
	 * @see SWT#WRAP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public DateTime(Composite parent, int style) {
		super(parent, checkStyle(style));

		init();
		
		GridLayout layout = new GridLayout(2, false); // 2 columns, no equal width
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		setLayout(layout);
		dayUpdate = true;

		updateText();
		if ((this.style & SWT.TIME) != 0 && !((this.style & SWT.DATE) != 0)) {
			dayUpdate = false;
			hourUpdate = true;
		}
		createButtons();
//		addListeners();
	}

	/*
	 * private void addListeners() { Listener listener = event -> { switch
	 * (event.type) { case SWT.MouseDown, SWT.MouseDoubleClick, SWT.MouseUp ->
	 * onTextMouseDown(event); // case SWT.MouseUp -> onMouseUp(event); // case
	 * SWT.MouseMove -> onMouseMove(event); // case SWT.FocusIn -> onFocusIn(event);
	 * // case SWT.FocusOut -> onFocusOut(event); // case SWT.MouseWheel ->
	 * onMouseWheel(event); // case SWT.Paint -> onPaint(event); case SWT.Resize ->
	 * onResize(); // case SWT.Dispose -> releaseHandle(); } };
	 * 
	 * addListener(SWT.MouseDown, listener); addListener(SWT.MouseDoubleClick,
	 * listener); addListener(SWT.MouseUp, listener); // addListener(SWT.MouseMove,
	 * listener); // addListener(SWT.FocusIn, listener); //
	 * addListener(SWT.FocusOut, listener); // addListener(SWT.MouseWheel,
	 * listener); // addListener(SWT.Paint, listener); addListener(SWT.Resize,
	 * listener); // addListener(SWT.Dispose, listener);
	 * 
	 * // final RendererFactory rendererFactory =
	 * parent.getDisplay().getRendererFactory(); // renderer =
	 * rendererFactory.createDateTimeRenderer(this); //
	 * renderer.setDateTimeText(dateTimeText); // renderer.setAlign(align); }
	 */
	void updateText() {
		String dateTimeText = null;
		// using String
		if (((this.style & SWT.TIME) != 0) && ((this.style & SWT.DATE) != 0)) {
			dateTimeText = getLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		} else if ((this.style & SWT.TIME) != 0) {
//			if ((this.style & SWT.SHORT) != 0)
//				dateTimeText = getCustomShortTimeFormat();
//			else if ((this.style & SWT.MEDIUM) != 0)
//				dateTimeText = getCustomMediumTimeFormat();
//			else if ((this.style & SWT.LONG) != 0)
//				dateTimeText = getCustomLongTimeFormat();
			dateTimeText = getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//		} else if ((this.style & SWT.DATE) != 0) { // ideally this is correct one
			// this is fallback for SWT.CALENDAR till it is implemented
		} else if ((this.style & SWT.DATE) != 0 || (this.style & SWT.CALENDAR) != 0) {
//			if ((this.style & SWT.SHORT) != 0)
//				dateTimeText = getCustomShortDateFormat();
//			else if ((this.style & SWT.MEDIUM) != 0)
//				dateTimeText = getCustomMediumDateFormat();
//			else if ((this.style & SWT.LONG) != 0)
//				dateTimeText = getCustomLongDateFormat();
			dateTimeText = getLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		} else if ((this.style & SWT.CALENDAR) != 0) {
			// TODO - Calender implementation yet to be done
		}
		if (dateTimeText != null)
			setTextWidget(dateTimeText);
	}

	private void onResize() {
		redraw();
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

	private void setTextWidget(String dateTimeText) {
		if (text == null) {
			text = new Text(this, style);
			text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			text.setCaret(null);

			// remove native text listeners. They interfere with the selection handling of
			// CDateTime...
			removeTextListeners(SWT.MouseDown, SWT.MouseUp, SWT.MouseMove | SWT.Selection);

			text.addListener(SWT.MouseDown, this::onTextMouseDown);
		}
		text.setText(dateTimeText);

		text.redraw();
	}

	private void removeTextListeners(int... listenerTypes) {
		for (int type : listenerTypes) {
			var listeners = text.getListeners(type);

			for (var l : listeners) {
				text.removeListener(type, l);
			}
		}
	}

	private void ignoreLineBreaks(KeyEvent e) {
		e.doit = e.keyCode != 13 // Enter
				&& e.keyCode != SWT.KEYPAD_CR;
	}

	private void createButtons() {
		arrows = new ArrowButtons(this, style);
	}

	@Override
	public void redraw() {
		super.redraw();
		if (text != null)
			text.redraw();
		arrows.redraw();
	}

	@Override
	public void redraw(int x, int y, int width, int height, boolean all) {
		super.redraw(x, y, width, height, true);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the receiver's text is modified, by sending it one of the messages defined in
	 * the <code>ModifyListener</code> interface.
	 *
	 * @param listener the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see ModifyListener
	 * @see #removeModifyListener
	 */
	public void addModifyListener(ModifyListener listener) {
		addTypedListener(listener, SWT.Modify);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the control is selected by the user, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is not called for texts.
	 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed
	 * in a single-line text.
	 * </p>
	 *
	 * @param listener the listener which should be notified when the control is
	 *                 selected by the user
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the receiver's text is verified, by sending it one of the messages defined in
	 * the <code>VerifyListener</code> interface.
	 *
	 * @param listener the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see VerifyListener
	 * @see #removeVerifyListener
	 */
	void addVerifyListener(VerifyListener listener) {
		addTypedListener(listener, SWT.Verify);
	}

	static int checkStyle(int style) {
		style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);

		// Add SWT.DATE if no format is specified
		if ((style & (SWT.DATE | SWT.TIME | SWT.CALENDAR)) == 0) {
			style |= SWT.DATE;
		}

		// Add SWT.MEDIUM if no format is specified
		if ((style & (SWT.SHORT | SWT.MEDIUM | SWT.LONG)) == 0) {
			style |= SWT.MEDIUM;
		}
		return style;
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass())
			error(SWT.ERROR_INVALID_SUBCLASS);
	}

	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void copy() {
		checkWidget();
		text.copy();
	}

	/**
	 * Cuts the selected text.
	 * <p>
	 * The current selection is first copied to the clipboard and then deleted from
	 * the widget.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void cut() {
		checkWidget();
		if ((style & SWT.READ_ONLY) != 0)
			return;
		text.cut();
	}

	@Override
	void enableWidget(boolean enabled) {
		super.enableWidget(enabled);
		arrows.setEnabled(enabled);
		if (text != null)
			text.setEnabled(enabled);
	}

	/**
	 * Returns the <em>selection</em>, which is the receiver's position.
	 *
	 * @return the selection
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getSelection() {
		checkWidget();
		int val = Integer.MIN_VALUE;

		try {
			val = Integer.parseInt(
					text.getText().replace("" + DecimalFormatSymbols.getInstance().getDecimalSeparator(), ""));
		} catch (NumberFormatException e) {
		}
		return val;
	}

	/**
	 * Returns a string containing a copy of the contents of the receiver's text
	 * field, or an empty string if there are no contents.
	 *
	 * @return the receiver's text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.4
	 */
	public String getText() {
		checkWidget();
		return text.getText();
	}

	@Override
	void releaseHandle() {
		super.releaseHandle();
		if (arrows != null)
			arrows.releaseHandle();
		if (text != null)
			text.releaseHandle();
		arrows = null;
		text = null;
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the receiver's text is modified.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Modify, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the control is selected by the user.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the control is verified.
	 *
	 * @param listener the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see VerifyListener
	 * @see #addVerifyListener
	 */
	void removeVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Verify, listener);
	}

	public String getDateTime() {
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).toString();
	}

	private void setDateTime(LocalDateTime t) {

		year = t.getYear();
		month = t.getMonthValue() - 1;
		day = t.getDayOfMonth();
		hours = t.getHour();
		minutes = t.getMinute();
		seconds = t.getSecond();

	}

	private LocalDateTime getLocalDateTime() {
		return LocalDateTime.of(year, month + 1, day, hours, minutes,seconds);
	}

	private boolean datetimeIsValid() {

		try {
			getLocalDateTime();
		} catch (DateTimeException e) {
			return false;
		}
		return true;

	}

	public String getDate() {
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
	}

	public String getTime() {
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString();
	}

	public int getYear() {
		return getLocalDateTime().getYear();
	}

	public int getMonth() {
		// To maintain consistency with the old DateTime widget(0-11).
		return getLocalDateTime().getMonthValue() - 1;
	}

	public int getDayOfYear() {
		return getLocalDateTime().getDayOfYear();
	}

	public int getDay() {
		return getLocalDateTime().getDayOfMonth();
	}

	public String getDayOfWeek() {
		return getLocalDateTime().getDayOfWeek().toString();
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public String getCustomShortTimeFormat() { // actual getCustomShortTimeFormat
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
	}

	public String getCustomMediumTimeFormat() {
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	public String getCustomLongTimeFormat() {
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	public String getCustomShortDateFormat() {
//		return dateTime.getMonth().toString() + "," + dateTime.getYear();
//		return dateTime.format(DateTimeFormatter.ofPattern("MMMM,yyyy")).toUpperCase();
// 		Recheck the above implementations once SWT.SHORT/SWT.LONG/SWT.MEDIUM are functional and can be removed.
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("MMMM, yyyy"));
	}

	public String getCustomMediumDateFormat() { // actual getCustomShortDateFormat
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	public String getCustomLongDateFormat() {
//		return dateTime.getDay() + "," + dateTime.getMonth().toString() + "," + dateTime.getYear();
//		return dateTime.format(DateTimeFormatter.ofPattern("dd,MMMM,yyyy")).toUpperCase();
// 		Recheck the above implementations once SWT.SHORT/SWT.LONG/SWT.MEDIUM are functional and can be removed.
		return getLocalDateTime().format(DateTimeFormatter.ofPattern("dd, MMMM, yyyy"));
//		return dateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
	}

	public void setYear(int year) {
		if (year < MIN_YEAR || year > MAX_YEAR)
			return;

		int prevYear = this.year;
		this.year = year;

		if (!datetimeIsValid()) {
			this.year = prevYear;
		}
	}

	public void setMonth(int month) {

		int prevMonth = this.month;
		this.month = month;

		if (!datetimeIsValid()) {
			this.month = prevMonth;
		}
	}

	/**
	 * Sets the day of month, validating leap years and month lengths.
	 *
	 * @param day the day of the month
	 * @throws IllegalArgumentException if the day is invalid for the current
	 *                                  month/year
	 */
	public void setDay(int day) {

		int prevDay = this.day;
		this.day = day;

		if (!datetimeIsValid()) {
			this.day = prevDay;
		}

	}

	public void setHours(int hours) {

		int prevHour = this.hours;
		this.hours = hours;

		if (!datetimeIsValid()) {
			this.hours = prevHour;
		}

	}

	public void setMinutes(int minutes) {
		int prevM = this.minutes;
		this.minutes = minutes;

		if (!datetimeIsValid()) {
			this.minutes = prevM;
		}
	}

	public void setSeconds(int seconds) {
		int prevS = this.seconds;
		this.seconds = seconds;

		if (!datetimeIsValid()) {
			this.seconds = prevS;
		}
	}

	public void setTime(int hours, int minutes, int seconds) {

		int prevH = this.hours;
		int prevS = this.seconds;
		int prevM = this.minutes;

		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;

		if (!datetimeIsValid()) {
			this.hours = prevH;
			this.seconds = prevS;
			this.minutes = prevM;
		}

	}

	public void setDate(int year, int month, int day) {
		if (year < MIN_YEAR || year > MAX_YEAR)
			return;

		int prevY = this.year;
		int prevM = this.month;
		int prevD = this.day;

		this.year = year;
		this.month = month;
		this.day = day;

		if (!datetimeIsValid()) {
			this.year = prevY;
			this.month = prevM;
			this.day = prevD;
		}

	}

	private void onTextMouseDown(Event event) {
		dayUpdate = monthUpdate = yearUpdate = false;
		hourUpdate = minuteUpdate = secondUpdate = false;

		Text t = (Text) event.widget;
		String textValue = t.getText();
		int column = t.getTextLocation(event.x, event.y).column;

		int length = textValue.length();

		if (length == 19 && textValue.charAt(2) == '-' && textValue.charAt(13) == ':') {
			// Format: "25-07-2025 13:48:18"
			if (column < 2) {
				t.setSelection(0, 2);
				dayUpdate = true;
			} else if (column < 5) {
				t.setSelection(3, 5);
				monthUpdate = true;
			} else if (column < 10) {
				t.setSelection(6, 10);
				yearUpdate = true;
			} else if (column < 13) {
				t.setSelection(11, 13);
				hourUpdate = true;
			} else if (column < 16) {
				t.setSelection(14, 16);
				minuteUpdate = true;
			} else {
				t.setSelection(17, 19);
				secondUpdate = true;
			}
		} else if (length == 8 && textValue.charAt(2) == ':') {
			// Format: "13:48:18"
			if (column < 2) {
				t.setSelection(0, 2);
				hourUpdate = true;
			} else if (column < 5) {
				t.setSelection(3, 5);
				minuteUpdate = true;
			} else {
				t.setSelection(6, 8);
				secondUpdate = true;
			}
		} else if (length == 10 && textValue.charAt(2) == '-') {
			// Format: "25-07-2025"
			if (column < 2) {
				t.setSelection(0, 2);
				dayUpdate = true;
			} else if (column < 5) {
				t.setSelection(3, 5);
				monthUpdate = true;
			} else {
				t.setSelection(6, 10);
				yearUpdate = true;
			}
		}
		t.setFocus();
	}


}