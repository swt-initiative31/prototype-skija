/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 483540
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are controls that allow the user
 * to choose an item from a list of items, or optionally
 * enter a new value by typing it into an editable text
 * field. Often, <code>Combo</code>s are used in the same place
 * where a single selection <code>List</code> widget could
 * be used but space is limited. A <code>Combo</code> takes
 * less space than a <code>List</code> widget and shows
 * similar information.
 * <p>
 * Note: Since <code>Combo</code>s can contain both a list
 * and an editable text field, it is possible to confuse methods
 * which access one versus the other (compare for example,
 * <code>clearSelection()</code> and <code>deselectAll()</code>).
 * The API documentation is careful to indicate either "the
 * receiver's list" or the "the receiver's text field" to
 * distinguish between the two cases.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN, READ_ONLY, SIMPLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Selection, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see NativeList
 * @see <a href="http://www.eclipse.org/swt/snippets/#combo">Combo snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
public abstract class NativeCombo extends NativeComposite implements ICombo {
	String text;
	int textLimit = Combo.LIMIT;
	boolean receivingFocus;
	boolean ignoreSetObject, ignoreSelection;
	NSRange selectionRange;
	boolean listVisible;

	static final int VISIBLE_COUNT = 5;

========
public class Combo extends Composite {

	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public static final int LIMIT;

	/*
	 * These values can be different on different platforms.
	 * Therefore they are not initialized in the declaration
	 * to stop the compiler from inlining.
	 */
	static {
		LIMIT = 0x7FFFFFFF;
	}

	private final ICombo wrappedCombo;

>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
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
 * @see SWT#DROP_DOWN
 * @see SWT#READ_ONLY
 * @see SWT#SIMPLE
 * @see NativeWidget#checkSubclass
 * @see NativeWidget#getStyle
 */
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
protected NativeCombo (NativeComposite parent, int style) {
	super (parent, checkStyle (style));
========
public Combo (Composite parent, int style) {
	this.wrappedCombo = WidgetFactory.createCombo(this, parent, style);
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
}

/**
 * Adds the argument to the end of the receiver's list.
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String,int)
 */
@Override
public void add (String string) {
	wrappedCombo.add(string);
}

/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String)
 */
@Override
public void add (String string, int index) {
	wrappedCombo.add(string, index);
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
@Override
public void addModifyListener (ModifyListener listener) {
	wrappedCombo.addModifyListener(listener);
}

/**
 * Adds a segment listener.
 * <p>
 * A <code>SegmentEvent</code> is sent whenever text content is being modified or
 * a segment listener is added or removed. You can
 * customize the appearance of text by indicating certain characters to be inserted
 * at certain text offsets. This may be used for bidi purposes, e.g. when
 * adjacent segments of right-to-left text should not be reordered relative to
 * each other.
 * E.g., multiple Java string literals in a right-to-left language
 * should generally remain in logical order to each other, that is, the
 * way they are stored.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows.
 * <code>SegmentEvent</code>s won't be sent on GTK and Cocoa.
 * </p>
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
 * @see SegmentEvent
 * @see SegmentListener
 * @see #removeSegmentListener
 *
 * @since 3.103
 */
@Override
public void addSegmentListener (SegmentListener listener) {
	wrappedCombo.addSegmentListener(listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the combo's list selection.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
 * </p>
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
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
@Override
public void addSelectionListener(SelectionListener listener) {
	wrappedCombo.addSelectionListener(listener);
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
 *
 * @since 3.1
 */
@Override
public void addVerifyListener (VerifyListener listener) {
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
	addTypedListener(listener, SWT.Verify);
}

@Override
boolean becomeFirstResponder (long id, long sel) {
	receivingFocus = true;
	boolean result = super.becomeFirstResponder (id, sel);
	receivingFocus = false;
	return result;
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a combo box that has a border using Windows style
	* bits.  All combo boxes draw their own border and
	* do not use the standard Windows border styles.
	* Therefore, no matter what style bits are specified,
	* clear the BORDER bits so that the SWT style will
	* match the Windows widget.
	*
	* The Windows behavior is currently implemented on
	* all platforms.
	*/
	style &= ~SWT.BORDER;

	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style = checkBits (style, SWT.DROP_DOWN, SWT.SIMPLE, 0, 0, 0, 0);
	if ((style & SWT.SIMPLE) != 0) return style & ~SWT.READ_ONLY;
	return style;
}

@Override
public void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
========
	wrappedCombo.addVerifyListener(listener);
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
}

/**
 * Sets the selection in the receiver's text field to an empty
 * selection starting just before the first character. If the
 * text field is editable, this has the effect of placing the
 * i-beam at the start of the text.
 * <p>
 * Note: To clear the selected items in the receiver's list,
 * use <code>deselectAll()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #deselectAll
 */
@Override
public void clearSelection () {
	wrappedCombo.clearSelection();
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
 *
 * @since 2.1
 */
@Override
public void copy () {
	wrappedCombo.copy();
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
 *
 * @since 2.1
 */
@Override
public void cut () {
	wrappedCombo.cut();
}

/**
 * Deselects the item at the given zero-relative index in the receiver's
 * list.  If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void deselect (int index) {
	wrappedCombo.deselect(index);
}

/**
 * Deselects all selected items in the receiver's list.
 * <p>
 * Note: To clear the selection in the receiver's text field,
 * use <code>clearSelection()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #clearSelection
 */
@Override
public void deselectAll () {
	wrappedCombo.deselectAll();
}

/**
 * Returns a point describing the location of the caret relative
 * to the receiver.
 *
 * @return a point, the location of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public Point getCaretLocation () {
	return wrappedCombo.getCaretLocation();
}

/**
 * Returns the character position of the caret.
 * <p>
 * Indexing is zero based.
 * </p>
 *
 * @return the position of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
@Override
public int getCaretPosition() {
	checkWidget();
	return selectionRange != null ? (int)selectionRange.location : 0;
}

/**
 * Returns a point describing the location of the caret relative
 * to the receiver.
 *
 * @return a point, the location of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
@Override
public Point getCaretLocation() {
	checkWidget();
	NSTextView widget = null;
	if (this.hasFocus()) {
		widget = new NSTextView(view.window().fieldEditor(true, view));
	}
	if (widget == null) return new Point (0, 0);
	NSLayoutManager layoutManager = widget.layoutManager();
	NSTextContainer container = widget.textContainer();
	NSRange range = widget.selectedRange();
	long [] rectCount = new long [1];
	long pArray = layoutManager.rectArrayForCharacterRange(range, range, container, rectCount);
	NSRect rect = new NSRect();
	if (rectCount[0] > 0) OS.memmove(rect, pArray, NSRect.sizeof);
	NSPoint pt = new NSPoint();
	pt.x = (int)rect.x;
	pt.y = (int)rect.y;
	pt = widget.convertPoint_toView_(pt, view);
	return new Point((int)pt.x, (int)pt.y);
}

int getCharCount() {
	NSString str;
	if ((style & SWT.READ_ONLY) != 0) {
		str = ((NSPopUpButton)view).titleOfSelectedItem();
	} else {
		str = new NSCell(((NSComboBox)view).cell()).title();
	}
	if (str == null) return 0;
	return (int)str.length();
========
public int getCaretPosition () {
	return wrappedCombo.getCaretPosition();
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver's list. Throws an exception if the index is out
 * of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public String getItem (int index) {
	return wrappedCombo.getItem(index);
}

/**
 * Returns the number of items contained in the receiver's list.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public int getItemCount () {
	return wrappedCombo.getItemCount();
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's list.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public int getItemHeight () {
	return wrappedCombo.getItemHeight();
}

/**
 * Returns a (possibly empty) array of <code>String</code>s which are
 * the items in the receiver's list.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public String [] getItems () {
	return wrappedCombo.getItems();
}

/**
 * Returns <code>true</code> if the receiver's list is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's list's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
@Override
public boolean getListVisible () {
	return wrappedCombo.getListVisible();
}

/**
 * Marks the receiver's list as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setListVisible (boolean visible) {
	wrappedCombo.setListVisible(visible);
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1.2
 */
@Override
public int getOrientation () {
	return wrappedCombo.getOrientation();
}

/**
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selection
 * in the receiver's text field, and whose y coordinate is the
 * character position representing the end of the selection.
 * An "empty" selection is indicated by the x and y coordinates
 * having the same value.
 * <p>
 * Indexing is zero based.  The range of a selection is from
 * 0..N where N is the number of characters in the widget.
 * </p>
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public Point getSelection () {
	return wrappedCombo.getSelection();
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver's list, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public int getSelectionIndex () {
	return wrappedCombo.getSelectionIndex();
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
 */
@Override
public String getText () {
	return wrappedCombo.getText();
}

/**
 * Returns the height of the receivers's text field.
 *
 * @return the text height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public int getTextHeight () {
	return wrappedCombo.getTextHeight();
}

/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Combo.LIMIT</code>.
 *
 * @return the text limit
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
 *
 * @see Combo#LIMIT
========
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
 */
@Override
public int getTextLimit () {
	return wrappedCombo.getTextLimit();
}

/**
 * Gets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the number of items that are visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
@Override
public int getVisibleItemCount () {
	return wrappedCombo.getVisibleItemCount();
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public int indexOf (String string) {
	return wrappedCombo.indexOf(string);
}

/**
 * Searches the receiver's list starting at the given,
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @param start the zero-relative index at which to begin the search
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public int indexOf (String string, int start) {
	return wrappedCombo.indexOf(string, start);
}

/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1
 */
@Override
public void paste () {
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	Point selection = getSelection ();
	int start = selection.x, end = selection.y;
	String text = getText ();
	String leftText = text.substring (0, start);
	String rightText = text.substring (end, text.length ());
	String newText = getClipboardText ();
	if (newText == null) return;
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		newText = verifyText (newText, start, end, null);
		if (newText == null) return;
	}
	if (textLimit != Combo.LIMIT) {
		int charCount = text.length ();
		if (charCount - (end - start) + newText.length() > textLimit) {
			newText = newText.substring(0, textLimit - charCount + (end - start));
		}
	}
	setText (leftText + newText + rightText, false);
	start += newText.length ();
	setSelection (new Point (start, start));
	sendEvent (SWT.Modify);
}

@Override
void register() {
	super.register();
	display.addWidget(((NSControl)view).cell(), this);
}

@Override
void releaseWidget () {
	if (display.currentCombo == this) {
		display.currentCombo = null;
	}
	super.releaseWidget ();
	if ((style & SWT.READ_ONLY) == 0) {
		((NSControl)view).abortEditing();
	}
	text = null;
	selectionRange = null;
========
	wrappedCombo.paste();
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
}

/**
 * Removes the item from the receiver's list at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void remove (int index) {
	wrappedCombo.remove(index);
}

/**
 * Removes the items from the receiver's list which are
 * between the given zero-relative start and end
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void remove (int start, int end) {
	wrappedCombo.remove(start, end);
}

/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument,
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void remove (String string) {
	wrappedCombo.remove(string);
}

/**
 * Removes all of the items from the receiver's list and clear the
 * contents of receiver's text field.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void removeAll () {
	wrappedCombo.removeAll();
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
@Override
public void removeModifyListener (ModifyListener listener) {
	wrappedCombo.removeModifyListener(listener);
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
 * @see SegmentEvent
 * @see SegmentListener
 * @see #addSegmentListener
 *
 * @since 3.103
 */
@Override
public void removeSegmentListener (SegmentListener listener) {
	wrappedCombo.removeSegmentListener(listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
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
@Override
public void removeSelectionListener (SelectionListener listener) {
	wrappedCombo.removeSelectionListener(listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 *
 * @since 3.1
 */
@Override
public void removeVerifyListener (VerifyListener listener) {
	wrappedCombo.removeVerifyListener(listener);
}

/**
 * Selects the item at the given zero-relative index in the receiver's
 * list.  If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void select (int index) {
	wrappedCombo.select(index);
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setItem (int index, String string) {
	wrappedCombo.setItem(index, string);
}

/**
 * Sets the receiver's list to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setItems (String... items) {
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	removeAll();
	if (items.length == 0) return;
	ignoreSelection = true;
	for (int i= 0; i < items.length; i++) {
		NSAttributedString str = createString(items[i]);
		if ((style & SWT.READ_ONLY) != 0) {
			NSMenu nsMenu = ((NSPopUpButton)view).menu();
			NSMenuItem nsItem = (NSMenuItem)new NSMenuItem().alloc();
			NSString empty = NSString.string();
			nsItem.initWithTitle(empty, 0, empty);
			nsItem.setAttributedTitle(str);
			nsMenu.addItem(nsItem);
			nsItem.release();
			//clear the selection
			((NSPopUpButton)view).selectItemAtIndex(-1);
		} else {
			((NSComboBox)view).addItemWithObjectValue(str);
		}
	}
	ignoreSelection = false;
}

/**
 * Marks the receiver's list as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
@Override
public void setListVisible (boolean visible) {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) {
		((NSPopUpButton)view).setPullsDown(visible);
	} else {
	}
========
	wrappedCombo.setItems(items);
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1.2
 */
@Override
public void setOrientation (int orientation) {
	wrappedCombo.setOrientation(orientation);
}

/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection.
 *
 * @param selection a point representing the new selection start and end
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setSelection (Point selection) {
	wrappedCombo.setSelection(selection);
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * This call is ignored when the receiver is read only and
 * the given string is not in the receiver's list.
 * </p>
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to
 * display incorrectly.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setText (String string) {
	wrappedCombo.setText(string);
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Combo.LIMIT)</code>.
 * Specifying a limit value larger than <code>Combo.LIMIT</code> sets the
 * receiver's limit to <code>Combo.LIMIT</code>.
 * </p>
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
 *
 * @see Combo#LIMIT
========
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
 */
@Override
public void setTextLimit (int limit) {
	wrappedCombo.setTextLimit(limit);
}

/**
 * Sets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param count the new number of items to be visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
@Override
public void setVisibleItemCount (int count) {
	wrappedCombo.setVisibleItemCount(count);
}

@Override
<<<<<<<< HEAD:bundles/org.eclipse.swt/Eclipse SWT/cocoa/org/eclipse/swt/widgets/NativeCombo.java
boolean shouldChangeTextInRange_replacementString(long id, long sel, long affectedCharRange, long replacementString) {
	NSRange range = new NSRange();
	OS.memmove(range, affectedCharRange, NSRange.sizeof);
	boolean result = callSuperBoolean(id, sel, range, replacementString);
	if (hooks (SWT.Verify)) {
		String string = new NSString(replacementString).getString();
		NSEvent currentEvent = display.application.currentEvent();
		long type = currentEvent.type();
		if (type != OS.NSKeyDown && type != OS.NSKeyUp) currentEvent = null;
		String newText = verifyText(string, (int)range.location, (int)(range.location+range.length), currentEvent);
		if (newText == null) return false;
		if (!string.equals(newText)) {
			int length = newText.length();
			Point selection = getSelection();
			if (textLimit != Combo.LIMIT) {
				int charCount = getCharCount();
				if (charCount - (selection.y - selection.x) + length > textLimit) {
					length = textLimit - charCount + (selection.y - selection.x);
				}
			}
			char [] buffer = new char [length];
			newText.getChars (0, buffer.length, buffer, 0);
			NSString nsstring = NSString.stringWithCharacters (buffer, buffer.length);
			NSText fieldEditor = ((NSTextField) view).currentEditor ();
			fieldEditor.replaceCharactersInRange (fieldEditor.selectedRange (), nsstring);
			text = fieldEditor.string().getString();
			sendEvent (SWT.Modify);
			result = false;
		}
	}
	if (result) {
		char[] chars = new char[text.length()];
		text.getChars(0, chars.length, chars, 0);
		NSMutableString mutable = (NSMutableString) NSMutableString.stringWithCharacters(chars, chars.length);
		mutable.replaceCharactersInRange(range, new NSString(replacementString));
		text = mutable.getString();
		selectionRange = null;
	}
	return result;
}

@Override
void textViewDidChangeSelection(long id, long sel, long aNotification) {
	NSNotification notification = new NSNotification(aNotification);
	NSText editor = new NSText(notification.object().id);
	selectionRange = editor.selectedRange();
}

@Override
void textDidChange (long id, long sel, long aNotification) {
	super.textDidChange (id, sel, aNotification);
	postEvent (SWT.Modify);
}

@Override
NSRange textView_willChangeSelectionFromCharacterRange_toCharacterRange(long id, long sel, long aTextView, long oldSelectedCharRange, long newSelectedCharRange) {
	/*
	* If the selection is changing as a result of the receiver getting focus
	* then return the receiver's last selection range, otherwise the full
	* text will be automatically selected.
	*/
	if (receivingFocus && selectionRange != null) return selectionRange;

	/* allow the selection change to proceed */
	NSRange result = new NSRange();
	OS.memmove(result, newSelectedCharRange, NSRange.sizeof);
	return result;
}

void updateItems () {
	if ((style & SWT.READ_ONLY) != 0) {
		NSPopUpButton widget = (NSPopUpButton)view;
		int count = (int) widget.numberOfItems();
		for (int i = 0; i < count; i++) {
			NSMenuItem item = new NSMenuItem (widget.itemAtIndex(i));
			NSAttributedString attStr = item.attributedTitle();
			String string = attStr.string().getString();
			item.setAttributedTitle(createString(string));
		}
	} else {
		NSComboBox widget = (NSComboBox)view;
		int count = (int) widget.numberOfItems();
		for (int i = 0; i < count; i++) {
			NSAttributedString attStr = new NSAttributedString (widget.itemObjectValueAtIndex(i));
			String string = attStr.string().getString();
			widget.insertItemWithObjectValue(createString(string), i);
			widget.removeItemAtIndex(i + 1);
		}
		widget.cell().setAttributedStringValue(createString(text));
	}
}

String verifyText (String string, int start, int end, NSEvent keyEvent) {
	Event event = new Event ();
	if (keyEvent != null) setKeyState(event, SWT.MouseDown, keyEvent);
	event.text = string;
	event.start = start;
	event.end = end;
	/*
	 * It is possible (but unlikely), that application
	 * code could have disposed the widget in the verify
	 * event.  If this happens, answer null to cancel
	 * the operation.
	 */
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
========
protected ICombo getWrappedWidget() {
	return wrappedCombo;
>>>>>>>> a771f7b523 (Introduce widget facade and adapt Win32 native widgets accordingly):bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/Combo.java
}

@Override
public abstract Combo getWrapper();

}
