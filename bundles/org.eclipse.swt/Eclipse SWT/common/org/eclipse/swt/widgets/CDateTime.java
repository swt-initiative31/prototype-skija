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

import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

public class CDateTime extends CustomComposite {

	private static final class ArrowButtons extends CustomComposite {

		private final Button arrowUp, arrowDown;

		public ArrowButtons(CDateTime parent2, int style) {
			super(parent2, style);
			int arrowStyle = SWT.ARROW;
			if ((style & SWT.FLAT) != 0)
				arrowStyle |= SWT.FLAT;

			setLayout(new GridLayout(1, false));
			setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true));

			arrowUp = createArrowButton(arrowStyle | SWT.UP,
					SelectionListener.widgetSelectedAdapter(this::increment));
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
			CDateTime cdatetime = (CDateTime) ((Button) e.widget).getParent().getParent();
			
			// TODO check selected item
			cdatetime.dateTime = cdatetime.dateTime.plusDays(1);
			cdatetime.updateText();
			
		}

		private void decrement(SelectionEvent e) {
			CDateTime cdatetime = (CDateTime) ((Button) e.widget).getParent().getParent();
			
			// TODO check selected item
			cdatetime.dateTime = cdatetime.dateTime.minusDays(1);
			cdatetime.updateText();
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

	private Text text;
	private ArrowButtons arrows;
	private int increment = 1;
	LocalDateTime dateTime = LocalDateTime.now();



/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CDateTime (Composite parent, int style) {
	super (parent, checkStyle (style));

	GridLayout layout = new GridLayout(2, false); // 2 columns, no equal width
	layout.marginWidth = 0;
	layout.marginHeight = 0;
	setLayout(layout);

	
	updateText();
	
	createButtons();
}

void updateText() {
	
	String dateTimeText = null;
	// using String
	if ((this.style & SWT.TIME) != 0) {
		dateTimeText = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	} else  {
		dateTimeText = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	setTextWidget(dateTimeText);
	
}

private void setTextWidget(String dateTimeText) {
	
	if(text == null) {
	
		text = new Text(this, style);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setCaret(null);
	
		// remove native text listeners. They interfere with the selection handling of CDateTime...
		removeTextListeners(SWT.MouseDown, SWT.MouseUp, SWT.MouseMove | SWT.Selection);
		
		text.addListener(SWT.MouseDown, this::onTextMouseDown);
		
		
	}
	text.setText(dateTimeText);
	
	text.redraw();
	
}

private void removeTextListeners(int ... listenerTypes ) {
	
	for(int type : listenerTypes) {
		var listeners = text.getListeners(type);
		
		for( var l : listeners) {
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
	text.redraw();
	arrows.redraw();
}

@Override
public void redraw(int x, int y, int width, int height, boolean all) {
	super.redraw(x, y, width, height, true);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	addTypedListener(listener, SWT.Modify);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #removeVerifyListener
 */
void addVerifyListener (VerifyListener listener) {
	addTypedListener(listener, SWT.Verify);
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void copy () {
	checkWidget ();
	text.copy();
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	text.cut();
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget(enabled);
	arrows.setEnabled(enabled);
	text.setEnabled(enabled);
}






/**
 * Returns the <em>selection</em>, which is the receiver's position.
 *
 * @return the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
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
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public String getText () {
	checkWidget ();
	return text.getText();
}



@Override
void releaseHandle () {
	super.releaseHandle();
	if (arrows != null) arrows.releaseHandle();
	if (text != null) text.releaseHandle();
	arrows = null;
	text = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #addVerifyListener
 */
void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
}


private void onTextMouseDown(Event event) {

	Text t = (Text) event.widget;

	var location = t.getTextLocation(event.x, event.y);
	
	// TODO distinguish between day month year

	if (location.column < 2) {
		text.setSelection(0, 2);
	} else if (location.column > 2 && location.column < 5) {
		text.setSelection(3, 5);
	} else if (location.column > 6) {
		text.setSelection(6, 10);
	}

}

@Override
protected ControlRenderer getRenderer() {
	// TODO Auto-generated method stub
	return null;
}
}