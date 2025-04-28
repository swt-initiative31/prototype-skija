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
import org.eclipse.swt.internal.*;

/**
 * Instances of this class are selectable user interface objects that represent
 * a range of positive, numeric values.
 * <p>
 * At any given moment, a given scroll bar will have a single 'selection' that
 * is considered to be its value, which is constrained to be within the range of
 * values the scroll bar represents (that is, between its <em>minimum</em> and
 * <em>maximum</em> values).
 * </p>
 * <p>
 * Typically, scroll bars will be made up of five areas:
 * </p>
 * <ol>
 * <li>an arrow button for decrementing the value</li>
 * <li>a page decrement area for decrementing the value by a larger amount</li>
 * <li>a <em>thumb</em> for modifying the value by mouse dragging</li>
 * <li>a page increment area for incrementing the value by a larger amount</li>
 * <li>an arrow button for incrementing the value</li>
 * </ol>
 * <p>
 * Based on their style, scroll bars are either <code>HORIZONTAL</code> (which
 * have a left facing button for decrementing the value and a right facing
 * button for incrementing it) or <code>VERTICAL</code> (which have an upward
 * facing button for decrementing the value and a downward facing buttons for
 * incrementing it).
 * </p>
 * <p>
 * On some platforms, the size of the scroll bar's thumb can be varied relative
 * to the magnitude of the range of values it represents (that is, relative to
 * the difference between its maximum and minimum values). Typically, this is
 * used to indicate some proportional value such as the ratio of the visible
 * area of a document to the total amount of space that it would take to display
 * it. SWT supports setting the thumb size even if the underlying platform does
 * not, but in this case the appearance of the scroll bar will not change.
 * </p>
 * <p>
 * Scroll bars are created by specifying either <code>H_SCROLL</code>,
 * <code>V_SCROLL</code> or both when creating a <code>Scrollable</code>. They
 * are accessed from the <code>Scrollable</code> using
 * <code>getHorizontalBar</code> and <code>getVerticalBar</code>.
 * </p>
 * <p>
 * Note: Scroll bars are not Controls. On some platforms, scroll bars that
 * appear as part of some standard controls such as a text or list have no
 * operating system resources and are not children of the control. For this
 * reason, scroll bars are treated specially. To create a control that looks
 * like a scroll bar but has operating system resources, use
 * <code>Slider</code>.
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
 * @see Slider
 * @see Scrollable
 * @see Scrollable#getHorizontalBar
 * @see Scrollable#getVerticalBar
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ScrollBar extends Widget {
	Scrollable parent;
	int increment, pageIncrement;
	private int minimum = 0;
	private int maximum = 100;
	private int selection;
	private int thumb = 10;
	private int dragOffset;
	private int drawWidth;
	private int drawHeight;
	private int thumbPosition;

	private Rectangle trackRectangle;
	private Rectangle thumbRectangle;

	private final ScrollBarRenderer renderer;

	private boolean autoRepeating;
	private boolean visible = true;
	private boolean enabled = true;
	private final boolean horizontal;
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
	ScrollBar(Scrollable parent, int style) {
		super(parent, style);
		this.style |= SWT.DOUBLE_BUFFERED;
		this.parent = parent;
		createWidget();
		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = rendererFactory.createScrollBarRenderer(this);

		Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.KeyDown -> onKeyDown(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseMove -> onMouseMove(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.Resize -> redraw();
			case SWT.MouseEnter -> onMouseEnter();
			case SWT.MouseExit -> onMouseExit();
			}
		};

		addListener(SWT.KeyDown, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.MouseEnter, listener);
		addListener(SWT.MouseExit, listener);

		horizontal = (style & SWT.VERTICAL) == 0;
		super.style |= horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the user changes the receiver's value, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the event object detail field
	 * contains one of the following values: <code>SWT.NONE</code> - for the end of
	 * a drag. <code>SWT.DRAG</code>. <code>SWT.HOME</code>. <code>SWT.END</code>.
	 * <code>SWT.ARROW_DOWN</code>. <code>SWT.ARROW_UP</code>.
	 * <code>SWT.PAGE_DOWN</code>. <code>SWT.PAGE_UP</code>.
	 * <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 *
	 * @param listener the listener which should be notified when the user changes
	 *                 the receiver's value
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

	static int checkStyle(int style) {
		return checkBits(style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}

	void createWidget() {
		increment = 1;
		pageIncrement = 10;
		/*
		 * Do not set the initial values of the maximum or the thumb. These values
		 * normally default to 100 and 10 but may have been set already by the widget
		 * that owns the scroll bar. For example, a scroll bar that is created for a
		 * list widget, setting these defaults would override the initial values
		 * provided by the list widget.
		 */
	}

	Rectangle getBounds() {
		parent.forceResize();

		Rectangle clientArea = parent.getClientArea();
		final int barThickness = 11;

		int x = 0, y = 0, width = 0, height = 0;

		if (isHorizontal()) {
			x = 0;
			y = clientArea.height;
			width = parent.getBounds().width;
			height = barThickness;
		} else {
			x = clientArea.width;
			y = 0;
			width = barThickness;
			height = parent.getBounds().height;
		}
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled, and <code>false</code>
	 * otherwise. A disabled control is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #isEnabled
	 */
	public boolean getEnabled() {
		return (state & DISABLED) == 0;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public boolean isVertical() {
		return !horizontal;
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
		return increment;
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
		return maximum;
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
		return minimum;
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
		return pageIncrement;
	}

	/**
	 * Returns the receiver's parent, which must be a Scrollable.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Scrollable getParent() {
		return parent;
	}

	/**
	 * Returns the single 'selection' that is the receiver's value.
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
		return selection;
	}

	/**
	 * Returns a point describing the receiver's size. The x coordinate of the
	 * result is the width of the receiver. The y coordinate of the result is the
	 * height of the receiver.
	 *
	 * @return the receiver's size
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Point getSize() {
		parent.forceResize();

		Rectangle parentBounds = parent.getBounds();
		final int barThickness = 16;

		int width, height;
		if (isHorizontal()) {
			width = parentBounds.width;
			height = barThickness;
		} else {
			width = barThickness;
			height = parentBounds.height;
		}
		return DPIUtil.autoScaleDown(new Point(width, height));
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
	 *
	 * @see ScrollBar
	 */
	public int getThumb() {
		return thumb;
	}

	/**
	 * Returns a rectangle describing the size and location of the receiver's thumb
	 * relative to its parent.
	 *
	 * @return the thumb bounds, relative to the {@link #getParent() parent}
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public Rectangle getThumbBounds() {
		return DPIUtil.autoScaleDown(thumbRectangle);
	}

	/**
	 * Returns a rectangle describing the size and location of the receiver's thumb
	 * track relative to its parent. This rectangle comprises the areas 2, 3, and 4
	 * as described in {@link ScrollBar}.
	 *
	 * @return the thumb track bounds, relative to the {@link #getParent() parent}
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public Rectangle getThumbTrackBounds() {
		return DPIUtil.autoScaleDown(trackRectangle);
	}

	/**
	 * Returns <code>true</code> if the receiver is visible, and <code>false</code>
	 * otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean getVisible() {
		return visible;
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all of the
	 * receiver's ancestors are enabled, and <code>false</code> otherwise. A
	 * disabled control is typically not selectable from the user interface and
	 * draws with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getEnabled
	 */
	public boolean isEnabled() {
		return getEnabled() && parent.isEnabled();
	}

	/**
	 * Returns <code>true</code> if the receiver is visible and all of the
	 * receiver's ancestors are visible and <code>false</code> otherwise.
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getVisible
	 */
	public boolean isVisible() {
		return getVisible() && parent != null && parent.isVisible();
	}

	@Override
	void releaseHandle() {
		super.releaseHandle();
		parent = null;
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		if (parent.horizontalBar == this)
			parent.horizontalBar = null;
		if (parent.verticalBar == this)
			parent.verticalBar = null;
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
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);

		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
	}

	int scrollBarType() {
		return (style & SWT.VERTICAL) != 0 ? 1 : 0; // check if this method is required
	}

	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables it
	 * otherwise. A disabled control is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 *
	 * @param enabled the new enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setEnabled(boolean enabled) {
		checkWidget();
		if (this.enabled != enabled) {
			this.enabled = enabled;
			redraw();
		}
		if (enabled) {
			state &= ~DISABLED;
		} else {
			state |= DISABLED;
		}
	}

	public void redraw() {
		if (parent instanceof Control) {
			Control control = (Control) parent;
			if (!control.isDisposed()) {
				control.redraw();
				control.update();
			}
		}
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down (or right/left) arrows are pressed to the argument, which must be at
	 * least one.
	 *
	 * @param value the new increment (must be greater than zero)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setIncrement(int value) {
		checkWidget();
		if (value < 1)
			return;
		increment = value;
	}

	/**
	 * Sets the maximum. If this value is negative or less than or equal to the
	 * minimum, the value is ignored. If necessary, first the thumb and then the
	 * selection are adjusted to fit within the new range.
	 *
	 * @param value the new maximum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMaximum(int value) {
		checkWidget();
		setMaximum(value, true);
	}

	private void setMaximum(int max, boolean redraw) {
		if (max <= minimum) {
			return;
		}
		this.maximum = max;
		this.thumb = Math.min(this.thumb, this.maximum - minimum);

		this.selection = Math.min(this.selection, this.maximum - this.thumb);
		this.selection = Math.max(this.selection, minimum);
		if (redraw) {
			redraw();
		}
	}

	/**
	 * Sets the minimum value. If this value is negative or greater than or equal to
	 * the maximum, the value is ignored. If necessary, first the thumb and then the
	 * selection are adjusted to fit within the new range.
	 *
	 * @param value the new minimum
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setMinimum(int value) {
		checkWidget();
		setMinimum(value, true);
	}

	private void setMinimum(int minimum, boolean redraw) {
		if (internalSetMinimum(minimum) && redraw) {
			redraw();
		}
	}

	private boolean internalSetMinimum(int minimum) {
		if (minimum < 0) {
			return false;
		}
		if (minimum <= this.maximum && minimum <= this.selection) {
			this.minimum = minimum;
			return true;
		}
		if (minimum <= this.maximum - this.thumb && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			return true;
		}
		if (minimum >= maximum) {
			this.minimum = this.thumb;
			return true;
		}
		if (minimum > this.maximum - this.thumb && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			this.thumb = this.maximum - this.minimum;
			return true;
		}
		return false;
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the page
	 * increment/decrement areas are selected to the argument, which must be at
	 * least one.
	 *
	 * @param value the page increment (must be greater than zero)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setPageIncrement(int value) {
		checkWidget();
		this.pageIncrement = Math.max(1, value);
		redraw();
	}

	/**
	 * Sets the single <em>selection</em> that is the receiver's value to the
	 * argument which must be greater than or equal to zero.
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
		setSelection(selection, true);
	}

	private void setSelection(int selection, boolean redraw) {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();

		// Ensure selection does not exceed max - thumb
		int newValue = Math.max(min, Math.min(selection, max - thumb));

		if (newValue != this.selection) {
			this.selection = newValue;
			if (redraw) {
				redraw();
				sendEvent(SWT.Selection, new Event());
			}
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
	 * @param value the new thumb value, which must be at least one and not larger
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
	public void setThumb(int value) {
		checkWidget();
		setThumb(value, true);
	}

	private void setThumb(int thumb, boolean redraw) {
		if (thumb <= 0 || this.thumb == thumb) {
			return;
		}
		if (thumb <= this.maximum && thumb <= this.selection) {
			this.thumb = thumb;
			if (redraw) {
				redraw();
			}
			return;
		}
		if (thumb <= (this.maximum - this.minimum) && thumb > this.selection) {
			this.selection = this.maximum - thumb;
			this.thumb = thumb;
		}
		if (thumb > (this.maximum - this.minimum) && thumb > this.selection) {
			this.thumb = this.maximum - this.minimum;
			this.selection = this.minimum;
		}
		if (redraw) {
			redraw();
		}
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
		setSelection(selection, false);
		setMinimum(minimum, false);
		setMaximum(maximum, false);
		setThumb(thumb, false);
		this.increment = Math.max(1, increment);
		this.pageIncrement = Math.max(1, pageIncrement);
		redraw();
	}

	/**
	 * Marks the receiver as visible if the argument is <code>true</code>, and marks
	 * it invisible otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, marking it visible may not actually cause it
	 * to be displayed.
	 * </p>
	 *
	 * @param visible the new visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			redraw();
			Event event = new Event();
			event.widget = this;
			notifyListeners(visible ? SWT.Show : SWT.Hide, event);

		}
	}

	public void setCustomBounds(int x, int y, int width, int height) {
		trackRectangle = new Rectangle(x, y, width, height);
	}

	public void draw(GC gc) {
		if (!isVisible()) {
			System.err.println("scrollbar not visible");
			return;
		}
		Rectangle drawingArea = getBounds();
		Drawing.drawWithGC(this.parent, gc, gc1 -> renderer.paint(gc1, drawingArea.width, drawingArea.height));
		this.drawWidth = drawingArea.width;
		this.drawHeight = drawingArea.height;
		this.thumbRectangle = renderer.getThumbRectangle();
		this.trackRectangle = renderer.getTrackRectangle();

	}

	private void onMouseEnter() {
		renderer.setDrawTrack(true);
		redraw();
	}

	private void onMouseExit() {
		renderer.setDrawTrack(false);
		renderer.setThumbHovered(false);
		redraw();
	}

//Is not called
	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}
		Rectangle drawingArea = getBounds();
		Drawing.drawWithGC(this.parent, event.gc, gc -> renderer.paint(gc, drawingArea.width, drawingArea.height));
		this.drawWidth = drawingArea.width;
		this.drawHeight = drawingArea.height;
		this.thumbRectangle = renderer.getThumbRectangle();
		this.trackRectangle = renderer.getTrackRectangle();
	}

	private void onMouseDown(Event event) {
		if (event.button != 1) {
			return;
		}

		// Drag of the thumb.
		if (thumbRectangle != null && thumbRectangle.contains(event.x, event.y)) {
			renderer.setDragging(true);
			dragOffset = isHorizontal() ? event.x - thumbRectangle.x : event.y - thumbRectangle.y;
			return;
		}

		// Click on the track. i.e page increment/decrement
		if (trackRectangle == null || !trackRectangle.contains(event.x, event.y)) {
			return;
		}

		handleTrackClick(event.x, event.y, 500, new Runnable() {
			@Override
			public void run() {
				if (!autoRepeating) {
					return;
				}

				autoRepeating = false;
				if (ScrollBar.this.isDisposed()) {
					return;
				}

				final Point cursorPos = parent.toControl(display.getCursorLocation());
				if (thumbRectangle != null && thumbRectangle.contains(cursorPos.x, cursorPos.y)) {
					return;
				}

				if (trackRectangle == null || !trackRectangle.contains(cursorPos.x, cursorPos.y)) {
					return;
				}

				handleTrackClick(cursorPos.x, cursorPos.y, 150, this);
			}
		});
	}

	private void handleTrackClick(int x, int y, int milliseconds, Runnable runnable) {
		int pageIncrement = getPageIncrement();
		int oldSelection = getSelection();

		int newSelection;
		if (isHorizontal()) {
			if (x < thumbRectangle.x) {
				newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
			} else {
				newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
			}
		} else {
			if (y < thumbRectangle.y) {
				newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
			} else {
				newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
			}
		}

		setSelection(newSelection);

		autoRepeating = true;
		display.timerExec(milliseconds, runnable);
	}

	private void onMouseUp(Event event) {
		if (event.button != 1) {
			return;
		}

		autoRepeating = false;
		// setCapture(false);
		if (renderer.getDragging()) {
			renderer.setDragging(false);
			updateValueFromThumbPosition();
			redraw();
		}
	}

	private void updateValueFromThumbPosition() {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();
		int range = max - min;

		int newValue;
		if (isHorizontal()) {
			int trackWidth = this.drawWidth - 4 - thumbRectangle.width;
			int relativeX = Math.min(trackWidth, Math.max(0, thumbPosition - 2));
			newValue = min + (relativeX * (range - thumb)) / trackWidth;
		} else {
			int trackHeight = this.drawHeight - 4 - thumbRectangle.height;
			int relativeY = Math.min(trackHeight, Math.max(0, thumbPosition - 2));
			newValue = min + (relativeY * (range - thumb)) / trackHeight;
		}
		setSelection(newValue);
	}

	private void increment(int count) {
		int delta = getIncrement() * count;
		int newValue = SliderRenderer.minMax(getMinimum(), getSelection() + delta, getMaximum());
		setSelection(newValue);
		redraw();
	}

	private void onKeyDown(Event event) {
		autoRepeating = false;

		KeyEvent keyEvent = new KeyEvent(event);
		switch (keyEvent.keyCode) {
		case SWT.ARROW_DOWN, SWT.ARROW_LEFT -> increment(-1);
		case SWT.ARROW_RIGHT, SWT.ARROW_UP -> increment(1);
		case SWT.PAGE_DOWN -> pageIncrement(-1);
		case SWT.PAGE_UP -> pageIncrement(1);
		case SWT.HOME -> setSelection(getMinimum());
		case SWT.END -> setSelection(getMaximum());
		}
	}

	private void pageIncrement(int count) {
		int delta = getPageIncrement() * count;
		int newValue = SliderRenderer.minMax(getMinimum(), getSelection() + delta, getMaximum());
		setSelection(newValue);
		redraw();
	}

	private void onMouseMove(Event event) {
		if (thumbRectangle == null) {
			return;
		}

		boolean isThumbHovered = thumbRectangle.contains(event.x, event.y);
		if (isThumbHovered != renderer.getHovered()) {
			renderer.setThumbHovered(isThumbHovered);
			redraw();
		}

		if (renderer.getDragging()) {
			int newPos;
			int min = getMinimum();
			int max = getMaximum();
			int thumb = getThumb();
			int range = max - min;
			int newValue;

			if (isHorizontal()) {
				newPos = event.x - dragOffset;

				int minX = 2;
				int maxX = drawWidth - thumbRectangle.width - 2;
				newPos = Math.max(minX, Math.min(newPos, maxX));

				thumbRectangle.x = newPos;
				thumbPosition = newPos;

				int trackWidth = drawWidth - 4 - thumbRectangle.width;
				int relativeX = thumbPosition - 2;
				newValue = min + Math.round((relativeX * (float) (range - thumb)) / trackWidth);
			} else {
				newPos = event.y - dragOffset;

				int minY = 2;
				int maxY = drawHeight - thumbRectangle.height - 2;
				newPos = Math.max(minY, Math.min(newPos, maxY));

				thumbRectangle.y = newPos;
				thumbPosition = newPos;

				int trackHeight = drawHeight - 4 - thumbRectangle.height;
				int relativeY = thumbPosition - 2;
				newValue = min + Math.round((relativeY * (float) (range - thumb)) / trackHeight);
			}

			newValue = Math.max(min, Math.min(newValue, max - thumb));

			if (newValue != getSelection()) {
				setSelection(newValue);
			}
			redraw();
		}
	}
}
