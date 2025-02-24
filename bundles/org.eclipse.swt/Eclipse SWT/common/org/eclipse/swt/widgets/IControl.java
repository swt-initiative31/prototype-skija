package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public interface IControl extends IWidget {

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the control is moved or resized, by sending
	 * it one of the messages defined in the <code>ControlListener</code>
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
	 * @see ControlListener
	 * @see #removeControlListener
	 */
	void addControlListener(ControlListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when a drag gesture occurs, by sending it
	 * one of the messages defined in the <code>DragDetectListener</code>
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
	 * @see DragDetectListener
	 * @see #removeDragDetectListener
	 *
	 * @since 3.3
	 */
	void addDragDetectListener(DragDetectListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the control gains or loses focus, by sending
	 * it one of the messages defined in the <code>FocusListener</code>
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
	 * @see FocusListener
	 * @see #removeFocusListener
	 */
	void addFocusListener(FocusListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when gesture events are generated for the control,
	 * by sending it one of the messages defined in the
	 * <code>GestureListener</code> interface.
	 * <p>
	 * NOTE: If <code>setTouchEnabled(true)</code> has previously been
	 * invoked on the receiver then <code>setTouchEnabled(false)</code>
	 * must be invoked on it to specify that gesture events should be
	 * sent instead of touch events.
	 * </p>
	 * <p>
	 * <b>Warning</b>: This API is currently only implemented on Windows and Cocoa.
	 * SWT doesn't send Gesture or Touch events on GTK.
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
	 * @see GestureListener
	 * @see #removeGestureListener
	 * @see #setTouchEnabled
	 *
	 * @since 3.7
	 */
	void addGestureListener(GestureListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when help events are generated for the control,
	 * by sending it one of the messages defined in the
	 * <code>HelpListener</code> interface.
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
	 * @see HelpListener
	 * @see #removeHelpListener
	 */
	void addHelpListener(HelpListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when keys are pressed and released on the system keyboard, by sending
	 * it one of the messages defined in the <code>KeyListener</code>
	 * interface.
	 * <p>
	 * When a key listener is added to a control, the control
	 * will take part in widget traversal.  By default, all
	 * traversal keys (such as the tab key and so on) are
	 * delivered to the control.  In order for a control to take
	 * part in traversal, it should listen for traversal events.
	 * Otherwise, the user can traverse into a control but not
	 * out.  Note that native controls such as table and tree
	 * implement key traversal in the operating system.  It is
	 * not necessary to add traversal listeners for these controls,
	 * unless you want to override the default traversal.
	 * </p>
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
	 * @see KeyListener
	 * @see #removeKeyListener
	 */
	void addKeyListener(KeyListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the platform-specific context menu trigger
	 * has occurred, by sending it one of the messages defined in
	 * the <code>MenuDetectListener</code> interface.
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
	 * @see MenuDetectListener
	 * @see #removeMenuDetectListener
	 *
	 * @since 3.3
	 */
	void addMenuDetectListener(MenuDetectListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when mouse buttons are pressed and released, by sending
	 * it one of the messages defined in the <code>MouseListener</code>
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
	 * @see MouseListener
	 * @see #removeMouseListener
	 */
	void addMouseListener(MouseListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the mouse passes or hovers over controls, by sending
	 * it one of the messages defined in the <code>MouseTrackListener</code>
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
	 * @see MouseTrackListener
	 * @see #removeMouseTrackListener
	 */
	void addMouseTrackListener(MouseTrackListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the mouse moves, by sending it one of the
	 * messages defined in the <code>MouseMoveListener</code>
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
	 * @see MouseMoveListener
	 * @see #removeMouseMoveListener
	 */
	void addMouseMoveListener(MouseMoveListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the mouse wheel is scrolled, by sending
	 * it one of the messages defined in the
	 * <code>MouseWheelListener</code> interface.
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
	 * @see MouseWheelListener
	 * @see #removeMouseWheelListener
	 *
	 * @since 3.3
	 */
	void addMouseWheelListener(MouseWheelListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the receiver needs to be painted, by sending it
	 * one of the messages defined in the <code>PaintListener</code>
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
	 * @see PaintListener
	 * @see #removePaintListener
	 */
	void addPaintListener(PaintListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when touch events occur, by sending it
	 * one of the messages defined in the <code>TouchListener</code>
	 * interface.
	 * <p>
	 * NOTE: You must also call <code>setTouchEnabled(true)</code> to
	 * specify that touch events should be sent, which will cause gesture
	 * events to not be sent.
	 * </p>
	 * <p>
	 * <b>Warning</b>: This API is currently only implemented on Windows and Cocoa.
	 * SWT doesn't send Gesture or Touch events on GTK.
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
	 * @see TouchListener
	 * @see #removeTouchListener
	 * @see #setTouchEnabled
	 *
	 * @since 3.7
	 */
	void addTouchListener(TouchListener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when traversal events occur, by sending it
	 * one of the messages defined in the <code>TraverseListener</code>
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
	 * @see TraverseListener
	 * @see #removeTraverseListener
	 */
	void addTraverseListener(TraverseListener listener);

	/**
	 * Returns the preferred size (in points) of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would
	 * best be displayed at. The width hint and height hint arguments
	 * allow the caller to ask a control questions such as "Given a particular
	 * width, how high does the control need to be to show all of the contents?"
	 * To indicate that the caller does not wish to constrain a particular
	 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 *
	 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
	 * @return the preferred size of the control
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	Point computeSize(int wHint, int hHint);

	/**
	 * Returns the preferred size (in points) of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would
	 * best be displayed at. The width hint and height hint arguments
	 * allow the caller to ask a control questions such as "Given a particular
	 * width, how high does the control need to be to show all of the contents?"
	 * To indicate that the caller does not wish to constrain a particular
	 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p><p>
	 * If the changed flag is <code>true</code>, it indicates that the receiver's
	 * <em>contents</em> have changed, therefore any caches that a layout manager
	 * containing the control may have been keeping need to be flushed. When the
	 * control is resized, the changed flag will be <code>false</code>, so layout
	 * manager caches can be retained.
	 * </p>
	 *
	 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
	 * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
	 * @return the preferred size of the control.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	Point computeSize(int wHint, int hHint, boolean changed);

	/**
	 * Detects a drag and drop gesture.  This method is used
	 * to detect a drag gesture when called from within a mouse
	 * down listener.
	 *
	 * <p>By default, a drag is detected when the gesture
	 * occurs anywhere within the client area of a control.
	 * Some controls, such as tables and trees, override this
	 * behavior.  In addition to the operating system specific
	 * drag gesture, they require the mouse to be inside an
	 * item.  Custom widget writers can use <code>setDragDetect</code>
	 * to disable the default detection, listen for mouse down,
	 * and then call <code>dragDetect()</code> from within the
	 * listener to conditionally detect a drag.
	 * </p>
	 *
	 * @param event the mouse down event
	 *
	 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
	 *
	 * @exception IllegalArgumentException <ul>
	 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 *
	 * @see #getDragDetect
	 * @see #setDragDetect
	 *
	 * @since 3.3
	 */
	boolean dragDetect(Event event);

	/**
	 * Detects a drag and drop gesture.  This method is used
	 * to detect a drag gesture when called from within a mouse
	 * down listener.
	 *
	 * <p>By default, a drag is detected when the gesture
	 * occurs anywhere within the client area of a control.
	 * Some controls, such as tables and trees, override this
	 * behavior.  In addition to the operating system specific
	 * drag gesture, they require the mouse to be inside an
	 * item.  Custom widget writers can use <code>setDragDetect</code>
	 * to disable the default detection, listen for mouse down,
	 * and then call <code>dragDetect()</code> from within the
	 * listener to conditionally detect a drag.
	 * </p>
	 *
	 * @param event the mouse down event
	 *
	 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
	 *
	 * @exception IllegalArgumentException <ul>
	 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 *
	 * @see #getDragDetect
	 * @see #setDragDetect
	 *
	 * @since 3.3
	 */
	boolean dragDetect(MouseEvent event);

	/**
	 * Forces the receiver to have the <em>keyboard focus</em>, causing
	 * all keyboard events to be delivered to it.
	 *
	 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #setFocus
	 */
	boolean forceFocus();

	/**
	 * Returns the accessible object for the receiver.
	 * <p>
	 * If this is the first time this object is requested,
	 * then the object is created and returned. The object
	 * returned by getAccessible() does not need to be disposed.
	 * </p>
	 *
	 * @return the accessible object
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see Accessible#addAccessibleListener
	 * @see Accessible#addAccessibleControlListener
	 *
	 * @since 2.0
	 */
	Accessible getAccessible();

	/**
	 * Returns the receiver's background color.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * For example, on some versions of Windows the background of a TabFolder,
	 * is a gradient rather than a solid color.
	 * </p>
	 * @return the background color
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Color getBackground();

	/**
	 * Returns the receiver's background image.
	 *
	 * @return the background image
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.2
	 */
	Image getBackgroundImage();

	/**
	 * Returns the receiver's border width in points.
	 *
	 * @return the border width
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	int getBorderWidth();

	/**
	 * Returns a rectangle describing the receiver's size and location in points
	 * relative to its parent (or its display if its parent is null),
	 * unless the receiver is a shell. In this case, the location is
	 * relative to the display.
	 *
	 * @return the receiver's bounding rectangle
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Rectangle getBounds();

	/**
	 * Returns the receiver's cursor, or null if it has not been set.
	 * <p>
	 * When the mouse pointer passes over a control its appearance
	 * is changed to match the control's cursor.
	 * </p>
	 *
	 * @return the receiver's cursor or <code>null</code>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.3
	 */
	Cursor getCursor();

	/**
	 * Returns <code>true</code> if the receiver is detecting
	 * drag gestures, and  <code>false</code> otherwise.
	 *
	 * @return the receiver's drag detect state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.3
	 */
	boolean getDragDetect();

	/**
	 * Returns <code>true</code> if the receiver is enabled, and
	 * <code>false</code> otherwise. A disabled control is typically
	 * not selectable from the user interface and draws with an
	 * inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #isEnabled
	 */
	boolean getEnabled();

	/**
	 * Returns the font that the receiver will use to paint textual information.
	 *
	 * @return the receiver's font
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Font getFont();

	/**
	 * Returns the foreground color that the receiver will use to draw.
	 *
	 * @return the receiver's foreground color
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Color getForeground();

	/**
	 * Returns layout data which is associated with the receiver.
	 *
	 * @return the receiver's layout data
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Object getLayoutData();

	/**
	 * Returns a point describing the receiver's location relative
	 * to its parent in points (or its display if its parent is null), unless
	 * the receiver is a shell. In this case, the point is
	 * relative to the display.
	 *
	 * @return the receiver's location
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Point getLocation();

	/**
	 * Returns the receiver's pop up menu if it has one, or null
	 * if it does not. All controls may optionally have a pop up
	 * menu that is displayed when the user requests one for
	 * the control. The sequence of key strokes, button presses
	 * and/or button releases that are used to request a pop up
	 * menu is platform specific.
	 *
	 * @return the receiver's menu
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Menu getMenu();

	/**
	 * Returns the receiver's monitor.
	 *
	 * @return the receiver's monitor
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.0
	 */
	Monitor getMonitor();

	/**
	 * Returns the orientation of the receiver, which will be one of the
	 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 *
	 * @return the orientation style
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.7
	 */
	int getOrientation();

	/**
	 * Returns the receiver's parent, which must be a <code>Composite</code>
	 * or null when the receiver is a shell that was created with null or
	 * a display for a parent.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Composite getParent();

	/**
	 * Returns the region that defines the shape of the control,
	 * or null if the control has the default shape.
	 *
	 * @return the region that defines the shape of the shell (or null)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.4
	 */
	Region getRegion();

	/**
	 * Returns a point describing the receiver's size in points. The
	 * x coordinate of the result is the width of the receiver.
	 * The y coordinate of the result is the height of the
	 * receiver.
	 *
	 * @return the receiver's size
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Point getSize();

	/**
	 * Returns the text direction of the receiver, which will be one of the
	 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 *
	 * @return the text direction style
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.102
	 */
	int getTextDirection();

	/**
	 * Returns the receiver's tool tip text, or null if it has
	 * not been set.
	 *
	 * @return the receiver's tool tip text
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	String getToolTipText();

	/**
	 * Returns <code>true</code> if this control is set to send touch events, or
	 * <code>false</code> if it is set to send gesture events instead.  This method
	 * also returns <code>false</code> if a touch-based input device is not detected
	 * (this can be determined with <code>Display#getTouchEnabled()</code>).  Use
	 * {@link #setTouchEnabled(boolean)} to switch the events that a control sends
	 * between touch events and gesture events.
	 *
	 * @return <code>true</code> if the control is set to send touch events, or <code>false</code> otherwise
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #setTouchEnabled
	 * @see Display#getTouchEnabled
	 *
	 * @since 3.7
	 */
	boolean getTouchEnabled();

	/**
	 * Returns <code>true</code> if the receiver is visible, and
	 * <code>false</code> otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some
	 * other condition makes the receiver not visible, this method
	 * may still indicate that it is considered visible even though
	 * it may not actually be showing.
	 * </p>
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	boolean getVisible();

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Control</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param data the platform specific GC data
	 * @return the platform specific GC handle
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	long internal_new_GC(GCData data);

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>Control</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param hDC the platform specific GC handle
	 * @param data the platform specific GC data
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	void internal_dispose_GC(long hDC, GCData data);

	/**
	 * Returns <code>true</code> if the receiver is enabled and all
	 * ancestors up to and including the receiver's nearest ancestor
	 * shell are enabled.  Otherwise, <code>false</code> is returned.
	 * A disabled control is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #getEnabled
	 */
	boolean isEnabled();

	/**
	 * Returns <code>true</code> if the receiver has the user-interface
	 * focus, and <code>false</code> otherwise.
	 *
	 * @return the receiver's focus state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	boolean isFocusControl();

	/**
	 * Returns <code>true</code> if the underlying operating
	 * system supports this reparenting, otherwise <code>false</code>
	 *
	 * @return <code>true</code> if the widget can be reparented, otherwise <code>false</code>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	boolean isReparentable();

	/**
	 * Returns <code>true</code> if the receiver is visible and all
	 * ancestors up to and including the receiver's nearest ancestor
	 * shell are visible. Otherwise, <code>false</code> is returned.
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #getVisible
	 */
	boolean isVisible();

	/**
	 * Moves the receiver above the specified control in the
	 * drawing order. If the argument is null, then the receiver
	 * is moved to the top of the drawing order. The control at
	 * the top of the drawing order will not be covered by other
	 * controls even if they occupy intersecting areas.
	 *
	 * @param control the sibling control (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see Control#moveBelow
	 * @see Composite#getChildren
	 */
	void moveAbove(Control control);

	/**
	 * Moves the receiver below the specified control in the
	 * drawing order. If the argument is null, then the receiver
	 * is moved to the bottom of the drawing order. The control at
	 * the bottom of the drawing order will be covered by all other
	 * controls which occupy intersecting areas.
	 *
	 * @param control the sibling control (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see Control#moveAbove
	 * @see Composite#getChildren
	 */
	void moveBelow(Control control);

	/**
	 * Causes the receiver to be resized to its preferred size.
	 * For a composite, this involves computing the preferred size
	 * from its layout, if there is one.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #computeSize(int, int, boolean)
	 */
	void pack();

	/**
	 * Causes the receiver to be resized to its preferred size.
	 * For a composite, this involves computing the preferred size
	 * from its layout, if there is one.
	 * <p>
	 * If the changed flag is <code>true</code>, it indicates that the receiver's
	 * <em>contents</em> have changed, therefore any caches that a layout manager
	 * containing the control may have been keeping need to be flushed. When the
	 * control is resized, the changed flag will be <code>false</code>, so layout
	 * manager caches can be retained.
	 * </p>
	 *
	 * @param changed whether or not the receiver's contents have changed
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #computeSize(int, int, boolean)
	 */
	void pack(boolean changed);

	/**
	 * Prints the receiver and all children.
	 *
	 * @param gc the gc where the drawing occurs
	 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.4
	 */
	boolean print(GC gc);

	/**
	 * Requests that this control and all of its ancestors be repositioned by
	 * their layouts at the earliest opportunity. This should be invoked after
	 * modifying the control in order to inform any dependent layouts of
	 * the change.
	 * <p>
	 * The control will not be repositioned synchronously. This method is
	 * fast-running and only marks the control for future participation in
	 * a deferred layout.
	 * <p>
	 * Invoking this method multiple times before the layout occurs is an
	 * inexpensive no-op.
	 *
	 * @since 3.105
	 */
	void requestLayout();

	/**
	 * Causes the entire bounds of the receiver to be marked
	 * as needing to be redrawn. The next time a paint request
	 * is processed, the control will be completely painted,
	 * including the background.
	 * <p>
	 * Schedules a paint request if the invalidated area is visible
	 * or becomes visible later. It is not necessary for the caller
	 * to explicitly call {@link #update()} after calling this method,
	 * but depending on the platform, the automatic repaints may be
	 * delayed considerably.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #update()
	 * @see PaintListener
	 * @see SWT#Paint
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#DOUBLE_BUFFERED
	 */
	void redraw();

	/**
	 * Causes the rectangular area of the receiver specified by
	 * the arguments to be marked as needing to be redrawn.
	 * The next time a paint request is processed, that area of
	 * the receiver will be painted, including the background.
	 * If the <code>all</code> flag is <code>true</code>, any
	 * children of the receiver which intersect with the specified
	 * area will also paint their intersecting areas. If the
	 * <code>all</code> flag is <code>false</code>, the children
	 * will not be painted.
	 * <p>
	 * Schedules a paint request if the invalidated area is visible
	 * or becomes visible later. It is not necessary for the caller
	 * to explicitly call {@link #update()} after calling this method,
	 * but depending on the platform, the automatic repaints may be
	 * delayed considerably.
	 * </p>
	 *
	 * @param x the x coordinate of the area to draw
	 * @param y the y coordinate of the area to draw
	 * @param width the width of the area to draw
	 * @param height the height of the area to draw
	 * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #update()
	 * @see PaintListener
	 * @see SWT#Paint
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#DOUBLE_BUFFERED
	 */
	void redraw(int x, int y, int width, int height, boolean all);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the control is moved or resized.
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
	 * @see ControlListener
	 * @see #addControlListener
	 */
	void removeControlListener(ControlListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when a drag gesture occurs.
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
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 *
	 * @since 3.3
	 */
	void removeDragDetectListener(DragDetectListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the control gains or loses focus.
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
	 * @see FocusListener
	 * @see #addFocusListener
	 */
	void removeFocusListener(FocusListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when gesture events are generated for the control.
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
	 * @see GestureListener
	 * @see #addGestureListener
	 *
	 * @since 3.7
	 */
	void removeGestureListener(GestureListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the help events are generated for the control.
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
	 * @see HelpListener
	 * @see #addHelpListener
	 */
	void removeHelpListener(HelpListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when keys are pressed and released on the system keyboard.
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
	 * @see KeyListener
	 * @see #addKeyListener
	 */
	void removeKeyListener(KeyListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the platform-specific context menu trigger has
	 * occurred.
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
	 * @see MenuDetectListener
	 * @see #addMenuDetectListener
	 *
	 * @since 3.3
	 */
	void removeMenuDetectListener(MenuDetectListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the mouse passes or hovers over controls.
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
	 * @see MouseTrackListener
	 * @see #addMouseTrackListener
	 */
	void removeMouseTrackListener(MouseTrackListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when mouse buttons are pressed and released.
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
	 * @see MouseListener
	 * @see #addMouseListener
	 */
	void removeMouseListener(MouseListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the mouse moves.
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
	 * @see MouseMoveListener
	 * @see #addMouseMoveListener
	 */
	void removeMouseMoveListener(MouseMoveListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the mouse wheel is scrolled.
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
	 * @see MouseWheelListener
	 * @see #addMouseWheelListener
	 *
	 * @since 3.3
	 */
	void removeMouseWheelListener(MouseWheelListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the receiver needs to be painted.
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
	 * @see PaintListener
	 * @see #addPaintListener
	 */
	void removePaintListener(PaintListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when touch events occur.
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
	 * @see TouchListener
	 * @see #addTouchListener
	 *
	 * @since 3.7
	 */
	void removeTouchListener(TouchListener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when traversal events occur.
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
	 * @see TraverseListener
	 * @see #addTraverseListener
	 */
	void removeTraverseListener(TraverseListener listener);

	/**
	 * Sets the receiver's background color to the color specified
	 * by the argument, or to the default system color for the control
	 * if the argument is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * </p>
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setBackground(Color color);

	/**
	 * Sets the receiver's background image to the image specified
	 * by the argument, or to the default system color for the control
	 * if the argument is null.  The background image is tiled to fill
	 * the available space.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * For example, on Windows the background of a Button cannot be changed.
	 * </p>
	 * @param image the new image (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.2
	 */
	void setBackgroundImage(Image image);

	/**
	 * Sets the receiver's size and location in points to the rectangular
	 * area specified by the arguments. The <code>x</code> and
	 * <code>y</code> arguments are relative to the receiver's
	 * parent (or its display if its parent is null), unless
	 * the receiver is a shell. In this case, the <code>x</code>
	 * and <code>y</code> arguments are relative to the display.
	 * <p>
	 * Note: Attempting to set the width or height of the
	 * receiver to a negative number will cause that
	 * value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the
	 * receiver to a number higher or equal 2^14 will cause them to be
	 * set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param x the new x coordinate for the receiver
	 * @param y the new y coordinate for the receiver
	 * @param width the new width for the receiver
	 * @param height the new height for the receiver
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setBounds(int x, int y, int width, int height);

	/**
	 * Sets the receiver's size and location in points to the rectangular
	 * area specified by the argument. The <code>x</code> and
	 * <code>y</code> fields of the rectangle are relative to
	 * the receiver's parent (or its display if its parent is null).
	 * <p>
	 * Note: Attempting to set the width or height of the
	 * receiver to a negative number will cause that
	 * value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the
	 * receiver to a number higher or equal 2^14 will cause them to be
	 * set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param rect the new bounds for the receiver
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setBounds(Rectangle rect);

	/**
	 * If the argument is <code>true</code>, causes the receiver to have
	 * all mouse events delivered to it until the method is called with
	 * <code>false</code> as the argument.  Note that on some platforms,
	 * a mouse button must currently be down for capture to be assigned.
	 *
	 * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setCapture(boolean capture);

	/**
	 * Sets the receiver's cursor to the cursor specified by the
	 * argument, or to the default cursor for that kind of control
	 * if the argument is null.
	 * <p>
	 * When the mouse pointer passes over a control its appearance
	 * is changed to match the control's cursor.
	 * </p>
	 *
	 * @param cursor the new cursor (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setCursor(Cursor cursor);

	/**
	 * Sets the receiver's drag detect state. If the argument is
	 * <code>true</code>, the receiver will detect drag gestures,
	 * otherwise these gestures will be ignored.
	 *
	 * @param dragDetect the new drag detect state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.3
	 */
	void setDragDetect(boolean dragDetect);

	/**
	 * Enables the receiver if the argument is <code>true</code>,
	 * and disables it otherwise. A disabled control is typically
	 * not selectable from the user interface and draws with an
	 * inactive or "grayed" look.
	 *
	 * @param enabled the new enabled state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setEnabled(boolean enabled);

	/**
	 * Causes the receiver to have the <em>keyboard focus</em>,
	 * such that all keyboard events will be delivered to it.  Focus
	 * reassignment will respect applicable platform constraints.
	 *
	 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #forceFocus
	 */
	boolean setFocus();

	/**
	 * Sets the font that the receiver will use to paint textual information
	 * to the font specified by the argument, or to the default font for that
	 * kind of control if the argument is null.
	 *
	 * @param font the new font (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setFont(Font font);

	/**
	 * Sets the receiver's foreground color to the color specified
	 * by the argument, or to the default system color for the control
	 * if the argument is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * </p>
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setForeground(Color color);

	/**
	 * Sets the layout data associated with the receiver to the argument.
	 *
	 * @param layoutData the new layout data for the receiver.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setLayoutData(Object layoutData);

	/**
	 * Sets the receiver's location to the point specified by
	 * the arguments which are relative to the receiver's
	 * parent (or its display if its parent is null), unless
	 * the receiver is a shell. In this case, the point is
	 * relative to the display.
	 *
	 * @param x the new x coordinate for the receiver
	 * @param y the new y coordinate for the receiver
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setLocation(int x, int y);

	/**
	 * Sets the receiver's location to the point specified by
	 * the arguments which are relative to the receiver's
	 * parent (or its display if its parent is null), unless
	 * the receiver is a shell. In this case, the point is
	 * relative to the display.
	 *
	 * @param location the new location for the receiver
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setLocation(Point location);

	/**
	 * Sets the receiver's pop up menu to the argument.
	 * All controls may optionally have a pop up
	 * menu that is displayed when the user requests one for
	 * the control. The sequence of key strokes, button presses
	 * and/or button releases that are used to request a pop up
	 * menu is platform specific.
	 * <p>
	 * Note: Disposing of a control that has a pop up menu will
	 * dispose of the menu.  To avoid this behavior, set the
	 * menu to null before the control is disposed.
	 * </p>
	 *
	 * @param menu the new pop up menu
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
	 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setMenu(Menu menu);

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
	 * @since 3.7
	 */
	void setOrientation(int orientation);

	/**
	 * If the argument is <code>false</code>, causes subsequent drawing
	 * operations in the receiver to be ignored. No drawing of any kind
	 * can occur in the receiver until the flag is set to true.
	 * Graphics operations that occurred while the flag was
	 * <code>false</code> are lost. When the flag is set to <code>true</code>,
	 * the entire widget is marked as needing to be redrawn.  Nested calls
	 * to this method are stacked.
	 * <p>
	 * Note: This operation is a hint and may not be supported on some
	 * platforms or for some widgets.
	 * </p>
	 *
	 * @param redraw the new redraw state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #redraw(int, int, int, int, boolean)
	 * @see #update()
	 */
	void setRedraw(boolean redraw);

	/**
	 * Sets the shape of the control to the region specified
	 * by the argument.  When the argument is null, the
	 * default shape of the control is restored.
	 *
	 * @param region the region that defines the shape of the control (or null)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.4
	 */
	void setRegion(Region region);

	/**
	 * Sets the receiver's size to the point specified by the arguments.
	 * <p>
	 * Note: Attempting to set the width or height of the
	 * receiver to a negative number will cause that
	 * value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the
	 * receiver to a number higher or equal 2^14 will cause them to be
	 * set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param width the new width in points for the receiver
	 * @param height the new height in points for the receiver
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setSize(int width, int height);

	/**
	 * Sets the receiver's size to the point specified by the argument.
	 * <p>
	 * Note: Attempting to set the width or height of the
	 * receiver to a negative number will cause them to be
	 * set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the
	 * receiver to a number higher or equal 2^14 will cause them to be
	 * set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param size the new size in points for the receiver
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setSize(Point size);

	/**
	 * Sets the base text direction (a.k.a. "paragraph direction") of the receiver,
	 * which must be one of the constants <code>SWT.LEFT_TO_RIGHT</code>,
	 * <code>SWT.RIGHT_TO_LEFT</code>, or <code>SWT.AUTO_TEXT_DIRECTION</code>.
	 * <p>
	 * <code>setOrientation</code> would override this value with the text direction
	 * that is consistent with the new orientation.
	 * </p>
	 * <p>
	 * <b>Warning</b>: This API is currently only implemented on Windows.
	 * It doesn't set the base text direction on GTK and Cocoa.
	 * </p>
	 *
	 * @param textDirection the base text direction style
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see SWT#LEFT_TO_RIGHT
	 * @see SWT#RIGHT_TO_LEFT
	 * @see SWT#AUTO_TEXT_DIRECTION
	 * @see SWT#FLIP_TEXT_DIRECTION
	 *
	 * @since 3.102
	 */
	void setTextDirection(int textDirection);

	/**
	 * Sets the receiver's tool tip text to the argument, which
	 * may be null indicating that the default tool tip for the
	 * control will be shown. For a control that has a default
	 * tool tip, such as the Tree control on Windows, setting
	 * the tool tip text to an empty string replaces the default,
	 * causing no tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
	 * To display a single '&amp;' in the tool tip, the character '&amp;' can be
	 * escaped by doubling it in the string.
	 * </p>
	 * <p>
	 * NOTE: This operation is a hint and behavior is platform specific, on Windows
	 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
	 * are not shown in tooltip.
	 * </p>
	 *
	 * @param string the new tool tip text (or null)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	void setToolTipText(String string);

	/**
	 * Sets whether this control should send touch events (by default controls do not).
	 * Setting this to <code>false</code> causes the receiver to send gesture events
	 * instead.  No exception is thrown if a touch-based input device is not
	 * detected (this can be determined with <code>Display#getTouchEnabled()</code>).
	 *
	 * @param enabled the new touch-enabled state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    </ul>
	 *
	 * @see Display#getTouchEnabled
	 *
	 * @since 3.7
	 */
	void setTouchEnabled(boolean enabled);

	/**
	 * Marks the receiver as visible if the argument is <code>true</code>,
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
	 */
	void setVisible(boolean visible);

	/**
	 * Returns a point which is the result of converting the
	 * argument, which is specified in display relative coordinates,
	 * to coordinates relative to the receiver.
	 * <p>
	 *
	 * @param x the x coordinate in points to be translated
	 * @param y the y coordinate in points to be translated
	 * @return the translated coordinates
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 2.1
	 */
	Point toControl(int x, int y);

	/**
	 * Returns a point which is the result of converting the
	 * argument, which is specified in display relative coordinates,
	 * to coordinates relative to the receiver.
	 * <p>
	 *
	 * @param point the point to be translated (must not be null)
	 * @return the translated coordinates
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Point toControl(Point point);

	/**
	 * Returns a point which is the result of converting the
	 * argument, which is specified in coordinates relative to
	 * the receiver, to display relative coordinates.
	 * <p>
	 *
	 * @param x the x coordinate to be translated
	 * @param y the y coordinate to be translated
	 * @return the translated coordinates
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 2.1
	 */
	Point toDisplay(int x, int y);

	/**
	 * Returns a point which is the result of converting the
	 * argument, which is specified in coordinates relative to
	 * the receiver, to display relative coordinates.
	 * <p>
	 *
	 * @param point the point to be translated (must not be null)
	 * @return the translated coordinates
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	Point toDisplay(Point point);

	/**
	 * Based on the argument, perform one of the expected platform
	 * traversal action. The argument should be one of the constants:
	 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
	 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
	 *
	 * @param traversal the type of traversal
	 * @return true if the traversal succeeded
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	boolean traverse(int traversal);

	/**
	 * Performs a platform traversal action corresponding to a <code>KeyDown</code> event.
	 *
	 * <p>Valid traversal values are
	 * <code>SWT.TRAVERSE_NONE</code>, <code>SWT.TRAVERSE_MNEMONIC</code>,
	 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
	 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
	 * If <code>traversal</code> is <code>SWT.TRAVERSE_NONE</code> then the Traverse
	 * event is created with standard values based on the KeyDown event.  If
	 * <code>traversal</code> is one of the other traversal constants then the Traverse
	 * event is created with this detail, and its <code>doit</code> is taken from the
	 * KeyDown event.
	 * </p>
	 *
	 * @param traversal the type of traversal, or <code>SWT.TRAVERSE_NONE</code> to compute
	 * this from <code>event</code>
	 * @param event the KeyDown event
	 *
	 * @return <code>true</code> if the traversal succeeded
	 *
	 * @exception IllegalArgumentException <ul>
	 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.6
	 */
	boolean traverse(int traversal, Event event);

	/**
	 * Performs a platform traversal action corresponding to a <code>KeyDown</code> event.
	 *
	 * <p>Valid traversal values are
	 * <code>SWT.TRAVERSE_NONE</code>, <code>SWT.TRAVERSE_MNEMONIC</code>,
	 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
	 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
	 * If <code>traversal</code> is <code>SWT.TRAVERSE_NONE</code> then the Traverse
	 * event is created with standard values based on the KeyDown event.  If
	 * <code>traversal</code> is one of the other traversal constants then the Traverse
	 * event is created with this detail, and its <code>doit</code> is taken from the
	 * KeyDown event.
	 * </p>
	 *
	 * @param traversal the type of traversal, or <code>SWT.TRAVERSE_NONE</code> to compute
	 * this from <code>event</code>
	 * @param event the KeyDown event
	 *
	 * @return <code>true</code> if the traversal succeeded
	 *
	 * @exception IllegalArgumentException <ul>
	 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.6
	 */
	boolean traverse(int traversal, KeyEvent event);

	/**
	 * Forces all outstanding paint requests for the widget
	 * to be processed before this method returns. If there
	 * are no outstanding paint request, this method does
	 * nothing.
	 * <p>Note:</p>
	 * <ul>
	 * <li>This method does not cause a redraw.</li>
	 * <li>Some OS versions forcefully perform automatic deferred painting.
	 * This method does nothing in that case.</li>
	 * </ul>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #redraw()
	 * @see #redraw(int, int, int, int, boolean)
	 * @see PaintListener
	 * @see SWT#Paint
	 */
	void update();

	/**
	 * Changes the parent of the widget to be the one provided.
	 * Returns <code>true</code> if the parent is successfully changed.
	 *
	 * @param parent the new parent for the control.
	 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
	 *    <li>ERROR_NULL_ARGUMENT - if the parent is <code>null</code></li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *	</ul>
	 */
	boolean setParent(Composite parent);

}