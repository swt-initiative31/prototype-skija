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

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface objects that represent
 * a range of positive, numeric values.
 * <p>
 * At any given moment, a given slider will have a single 'selection' that is
 * considered to be its value, which is constrained to be within the range of
 * values the slider represents (that is, between its <em>minimum</em> and
 * <em>maximum</em> values).
 * </p>
 * <p>
 * Typically, sliders will be made up of five areas:
 * </p>
 * <ol>
 * <li>an arrow button for decrementing the value</li>
 * <li>a page decrement area for decrementing the value by a larger amount</li>
 * <li>a <em>thumb</em> for modifying the value by mouse dragging</li>
 * <li>a page increment area for incrementing the value by a larger amount</li>
 * <li>an arrow button for incrementing the value</li>
 * </ol>
 * <p>
 * Based on their style, sliders are either <code>HORIZONTAL</code> (which have
 * a left facing button for decrementing the value and a right facing button for
 * incrementing it) or <code>VERTICAL</code> (which have an upward facing button
 * for decrementing the value and a downward facing buttons for incrementing
 * it).
 * </p>
 * <p>
 * On some platforms, the size of the slider's thumb can be varied relative to
 * the magnitude of the range of values it represents (that is, relative to the
 * difference between its maximum and minimum values). Typically, this is used
 * to indicate some proportional value such as the ratio of the visible area of
 * a document to the total amount of space that it would take to display it. SWT
 * supports setting the thumb size even if the underlying platform does not, but
 * in this case the appearance of the slider will not change.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see ScrollBar
 * @see <a href="http://www.eclipse.org/swt/snippets/#slider">Slider
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Slider extends CustomControl {
	private final boolean horizontal;
	private final int orientation;

	private SliderRenderer renderer;

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
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Slider(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.style |= SWT.DOUBLE_BUFFERED;

		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = rendererFactory.createSliderRenderer(this);

		Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.KeyDown -> onKeyDown(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseMove -> onMouseMove(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.MouseHorizontalWheel -> onMouseHorizontalWheel(event);
			case SWT.MouseVerticalWheel -> onMouseVerticalWheel(event);
			case SWT.Resize -> redraw();
			case SWT.MouseEnter -> onMouseEnter();
			case SWT.MouseExit -> onMouseExit();
			}
		};

		addListener(SWT.KeyDown, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseHorizontalWheel, listener);
		addListener(SWT.MouseVerticalWheel, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.MouseEnter, listener);
		addListener(SWT.MouseExit, listener);

		horizontal = (style & SWT.VERTICAL) == 0;
		orientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
		super.style |= horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
	}

	private static int checkStyle(int style) {
		// Do not propagate this flags to the super class
		style &= ~SWT.HORIZONTAL;
		style &= ~SWT.VERTICAL;

		if ((style & SWT.LEFT_TO_RIGHT) != 0) {
			style &= ~SWT.RIGHT_TO_LEFT;
		} else if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			style &= ~SWT.LEFT_TO_RIGHT;
		} else {
			style |= SWT.LEFT_TO_RIGHT;
			style &= ~SWT.RIGHT_TO_LEFT;
		}

		return style;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public boolean isVertical() {
		return !horizontal;
	}

	@Override
	public int getOrientation() {
		return orientation;
	}

	private void onMouseEnter() {
		renderer.setDrawTrack(true);
		redraw();
	}

	private void onMouseExit() {
		renderer.setDrawTrack(false);
		redraw();
	}

	private void onMouseVerticalWheel(Event event) {
		renderer.onMouseVerticalWheel(event);
	}

	private void onMouseHorizontalWheel(Event event) {
		renderer.onMouseHorizontalWheel(event);
	}

	private void onKeyDown(Event event) {
		renderer.onKeyDown(event);
	}

	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}

		Rectangle drawingArea = getBounds();
		Drawing.drawWithGC(this, event.gc, gc -> renderer.paint(gc, drawingArea.width, drawingArea.height));
	}

	private void onMouseDown(Event event) {
		if (event.button != 1)
			return;

		renderer.onMouseDown(event);
	}


	private void onMouseUp(Event event) {
		renderer.onMouseUp(event);
	}

	private void onMouseMove(Event event) {
		renderer.onMouseMove(event);
	}

	@Override
	protected Point computeDefaultSize() {
		return renderer.computeDefaultSize();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the user changes the receiver's value, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the user changes the receiver's
	 * value. <code>widgetDefaultSelected</code> is not called.
	 * </p>
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
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the user changes the receiver's value.
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
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed.
	 *
	 * @return the increment
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getIncrement() {
		return renderer.getIncrement();
	}

	/**
	 * Returns the maximum value which the receiver will allow.
	 *
	 * @return the maximum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getMaximum() {
		return renderer.getMaximum();
	}

	/**
	 * Returns the minimum value which the receiver will allow.
	 *
	 * @return the minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getMinimum() {
		checkWidget();
		return renderer.getMinimum();
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * page increment/decrement areas are selected.
	 *
	 * @return the page increment
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getPageIncrement() {
		checkWidget();
		return renderer.getPageIncrement();
	}

	/**
	 * Returns the 'selection', which is the receiver's value.
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
		return renderer.getSelection();
	}

	/**
	 * Returns the receiver's thumb value.
	 *
	 * @return the thumb value
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getThumb() {
		checkWidget();
		return renderer.getThumb();
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed to the argument, which must be at
	 * least one.
	 *
	 * @param increment the new increment (must be greater than zero)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setIncrement(int increment) {
		checkWidget();
		renderer.setIncrement(increment);
	}

	/**
	 * Sets the maximum. If this value is negative or less than or equal to the
	 * minimum, the value is ignored. If necessary, first the thumb and then the
	 * selection are adjusted to fit within the new range.
	 *
	 * @param max the new maximum, which must be greater than the current minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMaximum(int max) {
		checkWidget();
		renderer.setMaximum(max);
	}

	/**
	 * Sets the minimum value. If this value is negative or greater than or equal to
	 * the maximum, the value is ignored. If necessary, first the thumb and then the
	 * selection are adjusted to fit within the new range.
	 *
	 * @param minimum the new minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMinimum(int minimum) {
		checkWidget();
		renderer.setMinimum(minimum);
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the page
	 * increment/decrement areas are selected to the argument, which must be at
	 * least one.
	 *
	 * @param pageIncrement the page increment (must be greater than zero)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setPageIncrement(int pageIncrement) {
		checkWidget();
		renderer.setPageIncrement(pageIncrement);
	}

	/**
	 * Sets the 'selection', which is the receiver's value, to the argument which
	 * must be greater than or equal to zero.
	 *
	 * @param selection the new selection (must be zero or greater)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setSelection(int selection) {
		checkWidget();
		if (renderer.setSelection(selection)) {
			sendEvent(SWT.Selection, new Event());
		}
	}

	/**
	 * Sets the thumb value. The thumb value should be used to represent the size of
	 * the visual portion of the current range. This value is usually the same as
	 * the page increment value.
	 * <p>
	 * This new value will be ignored if it is less than one, and will be clamped if
	 * it exceeds the receiver's current range.
	 * </p>
	 *
	 * @param thumb the new thumb value, which must be at least one and not larger
	 *              than the size of the current range
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setThumb(int thumb) {
		checkWidget();
		renderer.setThumb(thumb);
	}

	/**
	 * Sets the receiver's selection, minimum value, maximum value, thumb, increment
	 * and page increment all at once.
	 * <p>
	 * Note: This is similar to setting the values individually using the
	 * appropriate methods, but may be implemented in a more efficient fashion on
	 * some platforms.
	 * </p>
	 *
	 * @param selection     the new selection value
	 * @param minimum       the new minimum value
	 * @param maximum       the new maximum value
	 * @param thumb         the new thumb value
	 * @param increment     the new increment value
	 * @param pageIncrement the new pageIncrement value
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
		checkWidget();
		renderer.setValues(selection, minimum, maximum, thumb, increment, pageIncrement);
	}
}
