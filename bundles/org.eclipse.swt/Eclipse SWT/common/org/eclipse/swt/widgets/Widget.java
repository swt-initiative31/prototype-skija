/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
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
 *     Pierre-Yves B., pyvesdev@gmail.com - Bug 219750: [styled text] Typing ~~ inserts é~~
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * This class is the abstract superclass of all user interface objects. Widgets
 * are created, disposed and issue notification to listeners when events occur
 * which affect them.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation. However, it has not been marked final to allow those
 * outside of the SWT development team to implement patched versions of the
 * class in order to get around specific limitations in advance of when those
 * limitations can be addressed by the team. Any class built using subclassing
 * to access the internals of this class will likely fail to compile or run
 * between releases and may be strongly platform specific. Subclassing should
 * not be attempted without an intimate and detailed understanding of the
 * workings of the hierarchy. No support is provided for user-written classes
 * which are implemented as subclasses of this class.
 * </p>
 *
 * @see #checkSubclass
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public abstract class Widget extends NativeWidget {

    /**
     * the native zoom of the monitor in percent (Warning: This field is platform
     * dependent)
     * <p>
     * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API. It
     * is marked public only so that it can be shared within the packages provided
     * by SWT. It is not available on all platforms and should never be accessed
     * from application code.
     * </p>
     *
     * @noreference This field is not intended to be referenced by clients.
     */
//    public int nativeZoom;
//    int style, state;
//    Display display;
//    protected EventTable eventTable;
//    Object data;

    /**
     * the native zoom of the monitor in percent (Warning: This field is platform
     * dependent)
     * <p>
     * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API. It
     * is marked public only so that it can be shared within the packages provided
     * by SWT. It is not available on all platforms and should never be accessed
     * from application code.
     * </p>
     *
     * @noreference This field is not intended to be referenced by clients.
     */
    public int nativeZoom;
    int style, state;
    Display display;
    protected EventTable eventTable;
    Object data;



/**
 * Prevents uninitialized instances from being created outside the package.
 */
Widget() {
    notifyCreationTracker();
}

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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see #checkSubclass
 * @see #getStyle
 */
public Widget(Widget parent, int style) {
    checkSubclass();
    checkParent(parent);
    this.style = style;
    this.nativeZoom = parent != null ? parent.nativeZoom : DPIUtil.getNativeDeviceZoom();
    this.display = parent.display;
    reskinWidget();
    notifyCreationTracker();
}



void _addListener(int eventType, Listener listener) {
    if (eventTable == null)
	eventTable = new EventTable();
    eventTable.hook(eventType, listener);
}

void _removeListener(int eventType, Listener listener) {
    if (eventTable == null)
	return;
    eventTable.unhook(eventType, listener);
}

/**
 * Adds the listener to the collection of {@link Listener listeners} who will be
 * notified when an event of the given type occurs. When the event does occur in
 * the widget, the listener is notified by sending it the
 * <code>handleEvent()</code> message. The event type is one of the event
 * constants defined in class {@link SWT}.
 *
 * @param eventType the type of event to listen for
 * @param listener  the listener which should be notified when the event occurs
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
 * @see #getListeners(int)
 * @see #removeListener(int, Listener)
 * @see #notifyListeners
 */


public void addListener(int eventType, Listener listener) {
	checkWidget();
    if (listener == null)
	error(SWT.ERROR_NULL_ARGUMENT);
    _addListener(eventType, listener);
}

/**
 * Adds the {@link EventListener typed listener} to the collection of listeners
 * who will be notified when an event of the given types occurs. When the event
 * does occur in the widget, the listener is notified by calling the type's
 * handling methods. The event type is one of the event constants defined in
 * class {@link SWT} and must correspond to the listeners type. If for example a
 * {@link SelectionListener} is passed the {@code eventTypes} can be
 * {@link SWT#Selection} or {@link SWT#DefaultSelection}.
 *
 * @param listener   the listener which should be notified when the event occurs
 * @param eventTypes the types of event to listen for
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
 * @see #getTypedListeners(int, Class)
 * @see #removeTypedListener(int, EventListener)
 * @see #notifyListeners
 * @since 3.126
 */


protected void addTypedListener(EventListener listener, int... eventTypes) {
	checkWidget();
	if (listener == null) {
	    SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	TypedListener typedListener = new TypedListener(listener);
	for (int eventType : eventTypes) {
	    _addListener(eventType, typedListener);
	}
    }

    /**
     * Adds the listener to the collection of listeners who will be notified when
     * the widget is disposed. When the widget is disposed, the listener is notified
     * by sending it the <code>widgetDisposed()</code> message.
     *
     * @param listener the listener which should be notified when the receiver is
     *                 disposed
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
     * @see DisposeListener
     * @see #removeDisposeListener
     */


    public void addDisposeListener(DisposeListener listener) {
	addTypedListener(listener, SWT.Dispose);
    }

    /**
     * Returns a style with exactly one style bit set out of the specified set of
     * exclusive style bits. All other possible bits are cleared when the first
     * matching bit is found. Bits that are not part of the possible set are
     * untouched.
     *
     * @param style the original style bits
     * @param int0  the 0th possible style bit
     * @param int1  the 1st possible style bit
     * @param int2  the 2nd possible style bit
     * @param int3  the 3rd possible style bit
     * @param int4  the 4th possible style bit
     * @param int5  the 5th possible style bit
     *
     * @return the new style bits
     */
    static int checkBits(int style, int int0, int int1, int int2, int int3, int int4, int int5) {
	int mask = int0 | int1 | int2 | int3 | int4 | int5;
	if ((style & mask) == 0)
	    style |= int0;
	if ((style & int0) != 0)
	    style = (style & ~mask) | int0;
	if ((style & int1) != 0)
	    style = (style & ~mask) | int1;
	if ((style & int2) != 0)
	    style = (style & ~mask) | int2;
	if ((style & int3) != 0)
	    style = (style & ~mask) | int3;
	if ((style & int4) != 0)
	    style = (style & ~mask) | int4;
	if ((style & int5) != 0)
	    style = (style & ~mask) | int5;
	return style;
    }



    void checkOrientation(Widget parent) {
	style &= ~SWT.MIRRORED;
	if ((style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)) == 0) {
	    if (parent != null) {
		if ((((Widget) parent).style & SWT.LEFT_TO_RIGHT) != 0)
		    style |= SWT.LEFT_TO_RIGHT;
		if ((((Widget) parent).style & SWT.RIGHT_TO_LEFT) != 0)
		    style |= SWT.RIGHT_TO_LEFT;
	    }
	}
	style = checkBits(style, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT, 0, 0, 0, 0);
    }

    void checkOpened() {
	/* Do nothing */
    }

    /**
     * Throws an exception if the specified widget can not be used as a parent for
     * the receiver.
     *
     * @exception IllegalArgumentException
     *                                     <ul>
     *                                     <li>ERROR_NULL_ARGUMENT - if the parent
     *                                     is null</li>
     *                                     <li>ERROR_INVALID_ARGUMENT - if the
     *                                     parent is disposed</li>
     *                                     </ul>
     * @exception SWTException
     *                                     <ul>
     *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
     *                                     called from the thread that created the
     *                                     parent</li>
     *                                     </ul>
     */


    void checkParent(Widget parent) {
	if (parent == null)
	    error(SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed())
	    error(SWT.ERROR_INVALID_ARGUMENT);
	parent.checkWidget();
	parent.checkOpened();
    }


    /**
     * Checks that this class can be subclassed.
     * <p>
     * The SWT class library is intended to be subclassed only at specific,
     * controlled points (most notably, <code>Composite</code> and
     * <code>Canvas</code> when implementing new widgets). This method enforces this
     * rule unless it is overridden.
     * </p>
     * <p>
     * <em>IMPORTANT:</em> By providing an implementation of this method that allows
     * a subclass of a class which does not normally allow subclassing to be
     * created, the implementer agrees to be fully responsible for the fact that any
     * such subclass will likely fail between SWT releases and will be strongly
     * platform specific. No support is provided for user-written classes which are
     * implemented in this fashion.
     * </p>
     * <p>
     * The ability to subclass outside of the allowed SWT classes is intended purely
     * to enable those not on the SWT development team to implement patches in order
     * to get around specific limitations in advance of when those limitations can
     * be addressed by the team. Subclassing should not be attempted without an
     * intimate and detailed understanding of the hierarchy.
     * </p>
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_INVALID_SUBCLASS - if this class is not an
     *                         allowed subclass</li>
     *                         </ul>
     */


    protected void checkSubclass() {
	if (!isValidSubclass())
	    error(SWT.ERROR_INVALID_SUBCLASS);
    }

    /**
     * Throws an <code>SWTException</code> if the receiver can not be accessed by
     * the caller. This may include both checks on the state of the receiver and
     * more generally on the entire execution context. This method <em>should</em>
     * be called by widget implementors to enforce the standard SWT invariants.
     * <p>
     * Currently, it is an error to invoke any method (other than
     * <code>isDisposed()</code>) on a widget that has had its
     * <code>dispose()</code> method called. It is also an error to call widget
     * methods from any thread that is different from the thread that created the
     * widget.
     * </p>
     * <p>
     * In future releases of SWT, there may be more or fewer error checks and
     * exceptions may be thrown for different reasons.
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


    protected void checkWidget() {
	Display display = this.display;
	if (display == null)
	    error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.thread != Thread.currentThread())
	    error(SWT.ERROR_THREAD_INVALID_ACCESS);
	if ((state & DISPOSED) != 0)
	    error(SWT.ERROR_WIDGET_DISPOSED);
    }

    /**
     * Destroys the widget in the operating system and releases the widget's handle.
     * If the widget does not have a handle, this method may hide the widget, mark
     * the widget as destroyed or do nothing, depending on the widget.
     * <p>
     * When a widget is destroyed in the operating system, its descendants are also
     * destroyed by the operating system. This means that it is only necessary to
     * call <code>destroyWidget</code> on the root of the widget tree.
     * </p>
     * <p>
     * This method is called after <code>releaseWidget()</code>.
     * </p>
     * <p>
     * See also <code>releaseChild()</code>, <code>releaseWidget()</code> and
     * <code>releaseHandle()</code>.
     * </p>
     *
     * @see #dispose
     */


    @Override
    void destroyWidget() {
		super.destroyWidget();
    }

    /**
     * Disposes of the operating system resources associated with the receiver and
     * all its descendants. After this method has been invoked, the receiver and all
     * descendants will answer <code>true</code> when sent the message
     * <code>isDisposed()</code>. Any internal connections between the widgets in
     * the tree will have been removed to facilitate garbage collection. This method
     * does nothing if the widget is already disposed.
     * <p>
     * NOTE: This method is not called recursively on the descendants of the
     * receiver. This means that, widget implementers can not detect when a widget
     * is being disposed of by re-implementing this method, but should instead
     * listen for the <code>Dispose</code> event.
     * </p>
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
     *                         the thread that created the receiver</li>
     *                         </ul>
     *
     * @see #addDisposeListener
     * @see #removeDisposeListener
     * @see #checkWidget
     */


    public void dispose() {
	/*
	 * Note: It is valid to attempt to dispose a widget more than once. If this
	 * happens, fail silently.
	 */
	if (isDisposed())
	    return;
	if (!isValidThread())
	    error(SWT.ERROR_THREAD_INVALID_ACCESS);
	release(true);
    }


    /**
     * Does whatever widget specific cleanup is required, and then uses the code in
     * <code>SWTError.error</code> to handle the error.
     *
     * @param code the descriptive error code
     *
     * @see SWT#error(int)
     */


    void error(int code) {
	SWT.error(code);
    }



    protected boolean filters(int eventType) {
	return display.filters(eventType);
    }


    char[] fixMnemonic(String string) {
	return fixMnemonic(string, false, false);
    }

    char[] fixMnemonic(String string, boolean spaces) {
	return fixMnemonic(string, spaces, false);
    }

    char[] fixMnemonic(String string, boolean spaces, boolean removeAppended) {
	// fixMnemonic must return a null-terminated array
	char[] buffer = new char[string.length() + 1];
	string.getChars(0, string.length(), buffer, 0);
	int i = 0, j = 0;
	while (i < buffer.length) {
	    if (buffer[i] == '&') {
		if (i + 1 < buffer.length && buffer[i + 1] == '&') {
		    buffer[j++] = spaces ? ' ' : buffer[i];
		    i++;
		}
		i++;
	    } else if (buffer[i] == '(' && removeAppended && i + 4 == string.length() && buffer[i + 1] == '&'
		    && buffer[i + 3] == ')') {
		if (spaces)
		    buffer[j++] = ' ';
		i += 4;
	    } else {
		buffer[j++] = buffer[i++];
		}
	}
	while (j < buffer.length)
	    buffer[j++] = 0;
	return buffer;
    }

    /**
     * Returns the application defined widget data associated with the receiver, or
     * null if it has not been set. The <em>widget data</em> is a single, unnamed
     * field that is stored with every widget.
     * <p>
     * Applications may put arbitrary objects in this field. If the object stored in
     * the widget data needs to be notified when the widget is disposed of, it is
     * the application's responsibility to hook the Dispose event on the widget and
     * do so.
     * </p>
     *
     * @return the widget data
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - when the receiver has
     *                         been disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - when called from
     *                         the wrong thread</li>
     *                         </ul>
     *
     * @see #setData(Object)
     */


    public Object getData() {
	checkWidget();
	return (state & KEYED_DATA) != 0 ? ((Object[]) data)[0] : data;
    }

    /**
     * Returns the application defined property of the receiver with the specified
     * name, or null if it has not been set.
     * <p>
     * Applications may have associated arbitrary objects with the receiver in this
     * fashion. If the objects stored in the properties need to be notified when the
     * widget is disposed of, it is the application's responsibility to hook the
     * Dispose event on the widget and do so.
     * </p>
     *
     * @param key the name of the property
     * @return the value of the property or null if it has not been set
     *
     * @exception IllegalArgumentException
     *                                     <ul>
     *                                     <li>ERROR_NULL_ARGUMENT - if the key is
     *                                     null</li>
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
     * @see #setData(String, Object)
     */


    public Object getData(String key) {
	checkWidget();
	if (key == null)
	    error(SWT.ERROR_NULL_ARGUMENT);
	if ((state & KEYED_DATA) != 0 && data instanceof Object[] table) {
	    for (int i = 1; i < table.length; i += 2) {
		if (key.equals(table[i]))
		    return table[i + 1];
	    }
	}
	return null;
    }

    /**
     * Returns the <code>Display</code> that is associated with the receiver.
     * <p>
     * A widget's display is either provided when it is created (for example, top
     * level <code>Shell</code>s) or is the same as its parent's display.
     * </p>
     *
     * @return the receiver's display
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                         disposed</li>
     *                         </ul>
     */


    public Display getDisplay() {
	Display display = this.display;
	if (display == null)
	    error(SWT.ERROR_WIDGET_DISPOSED);
	return display;
    }

    /**
     * Returns an array of {@link Listener listeners} who will be notified when an
     * event of the given type occurs. The event type is one of the event constants
     * defined in class {@link SWT}.
     *
     * @param eventType the type of event to listen for
     * @return an array of listeners that will be notified when the event occurs
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                         disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
     *                         the thread that created the receiver</li>
     *                         </ul>
     *
     * @see #addListener(int, Listener)
     * @see #removeListener(int, Listener)
     * @see #notifyListeners
     *
     * @since 3.4
     */


    public Listener[] getListeners(int eventType) {
	checkWidget();
	if (eventTable == null)
	    return new Listener[0];
	return eventTable.getListeners(eventType);
    }

    /**
     * Returns the typed listeners who will be notified when an event of the given
     * type occurs. The event type is one of the event constants defined in class
     * {@link SWT} and the specified listener-type must correspond to that event. If
     * for example the {@code eventType} is {@link SWT#Selection}, the listeners
     * type should be {@link SelectionListener}.
     *
     * @param eventType the type of event to listen for
     * @return a stream of typed listeners that will be notified when the event
     *         occurs
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                         disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
     *                         the thread that created the receiver</li>
     *                         </ul>
     *
     * @see #addTypedListener(EventListener, int...)
     * @see #removeTypedListener(int, EventListener)
     * @see #notifyListeners
     *
     * @since 3.126
     */


    public <L extends EventListener> Stream<L> getTypedListeners(int eventType, Class<L> listenerType) {
	return Arrays.stream(getListeners(eventType)) //
		.filter(TypedListener.class::isInstance).map(l -> ((TypedListener) l).eventListener)
		.filter(listenerType::isInstance).map(listenerType::cast);
    }

    Menu getMenu() {
	return null;
    }

    /**
     * Returns the name of the widget. This is the name of the class without the
     * package name.
     *
     * @return the name of the widget
     */


    String getName() {
	String string = getClass().getName();
	int index = string.lastIndexOf('.');
	if (index == -1)
	    return string;
	return string.substring(index + 1, string.length());
    }

    /*
     * Returns a short printable representation for the contents of a widget. For
     * example, a button may answer the label text. This is used by
     * <code>toString</code> to provide a more meaningful description of the widget.
     *
     * @return the contents string for the widget
     *
     * @see #toString
     */


    String getNameText() {
	return ""; //$NON-NLS-1$
    }

    /**
     * Returns the receiver's style information.
     * <p>
     * Note that the value which is returned by this method <em>may not match</em>
     * the value which was provided to the constructor when the receiver was
     * created. This can occur when the underlying operating system does not support
     * a particular combination of requested styles. For example, if the platform
     * widget used to implement a particular SWT widget always has scroll bars, the
     * result of calling this method would always have the <code>SWT.H_SCROLL</code>
     * and <code>SWT.V_SCROLL</code> bits set.
     * </p>
     *
     * @return the style bits
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                         disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
     *                         the thread that created the receiver</li>
     *                         </ul>
     */


    public int getStyle() {
	checkWidget();
	return style;
    }

    /*
     * Returns <code>true</code> if the specified eventType is hooked, and
     * <code>false</code> otherwise. Implementations of SWT can avoid creating
     * objects and sending events when an event happens in the operating system but
     * there are no listeners hooked for the event.
     *
     * @param eventType the event to be checked
     *
     * @return <code>true</code> when the eventType is hooked and <code>false</code>
     * otherwise
     *
     * @see #isListening
     */


    protected boolean hooks(int eventType) {
	if (eventTable == null)
	    return false;
	return eventTable.hooks(eventType);
    }

    /**
     * Returns <code>true</code> if the widget has auto text direction, and
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> when the widget has auto direction and
     *         <code>false</code> otherwise
     *
     * @see SWT#AUTO_TEXT_DIRECTION
     *
     * @since 3.105
     */


    public boolean isAutoDirection() {
	return (state & HAS_AUTO_DIRECTION) != 0;
    }


    /**
     * Returns <code>true</code> if the widget has been disposed, and
     * <code>false</code> otherwise.
     * <p>
     * This method gets the dispose state for the widget. When a widget has been
     * disposed, it is an error to invoke any other method (except
     * {@link #dispose()}) using the widget.
     * </p>
     *
     * @return <code>true</code> when the widget is disposed and <code>false</code>
     *         otherwise
     */


    public boolean isDisposed() {
	return (state & DISPOSED) != 0;
    }

    /**
     * Returns <code>true</code> if there are any listeners for the specified event
     * type associated with the receiver, and <code>false</code> otherwise. The
     * event type is one of the event constants defined in class <code>SWT</code>.
     *
     * @param eventType the type of event
     * @return true if the event is hooked
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                         disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
     *                         the thread that created the receiver</li>
     *                         </ul>
     *
     * @see SWT
     */


    public boolean isListening(int eventType) {
	checkWidget();
	return hooks(eventType);
    }

    /*
     * Returns <code>true</code> when subclassing is allowed and <code>false</code>
     * otherwise
     *
     * @return <code>true</code> when subclassing is allowed and <code>false</code>
     * otherwise
     */


    boolean isValidSubclass() {
	return Display.isValidClass(getClass());
    }

    /*
     * Returns <code>true</code> when the current thread is the thread that created
     * the widget and <code>false</code> otherwise.
     *
     * @return <code>true</code> when the current thread is the thread that created
     * the widget and <code>false</code> otherwise
     */


    boolean isValidThread() {
	return getDisplay().isValidThread();
    }


    /**
     * Notifies all of the receiver's listeners for events of the given type that
     * one such event has occurred by invoking their <code>handleEvent()</code>
     * method. The event type is one of the event constants defined in class
     * <code>SWT</code>.
     *
     * @param eventType the type of event which has occurred
     * @param event     the event data
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                         disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
     *                         the thread that created the receiver</li>
     *                         </ul>
     *
     * @see SWT
     * @see #addListener
     * @see #getListeners(int)
     * @see #removeListener(int, Listener)
     */


    public void notifyListeners(int eventType, Event event) {
	checkWidget();
	if (event == null)
	    event = new Event();
	sendEvent(eventType, event);
    }



    void postEvent(int eventType) {
	sendEvent(eventType, null, false);
    }



    void postEvent(int eventType, Event event) {
	sendEvent(eventType, event, false);
    }

    /*
     * Releases the widget hierarchy and optionally destroys the receiver. <p>
     * Typically, a widget with children will broadcast this message to all children
     * so that they too can release their resources. The <code>releaseHandle</code>
     * method is used as part of this broadcast to zero the handle fields of the
     * children without calling <code>destroyWidget</code>. In this scenario, the
     * children are actually destroyed later, when the operating system destroys the
     * widget tree. </p>
     *
     * @param destroy indicates that the receiver should be destroyed
     *
     * @see #dispose
     *
     * @see #releaseHandle
     *
     * @see #releaseParent
     *
     * @see #releaseWidget
     */


    void release(boolean destroy) {
	try (ExceptionStash exceptions = new ExceptionStash()) {
	    if ((state & DISPOSE_SENT) == 0) {
		state |= DISPOSE_SENT;
		try {
		    sendEvent(SWT.Dispose);
		} catch (Error | RuntimeException ex) {
		    exceptions.stash(ex);
		}
		}
	    if ((state & DISPOSED) == 0) {
		try {
		    releaseChildren(destroy);
		} catch (Error | RuntimeException ex) {
		    exceptions.stash(ex);
		}
		}
	    if ((state & RELEASED) == 0) {
		state |= RELEASED;
		if (destroy) {
		    releaseParent();
		    releaseWidget();
		    destroyWidget();
		} else {
		    releaseWidget();
		    releaseHandle();
		}
		}
	    notifyDisposalTracker();
	}
    }



    void releaseChildren(boolean destroy) {
    }

    /*
     * Releases the widget's handle by zero'ing it out. Does not destroy or release
     * any operating system resources. <p> This method is called after
     * <code>releaseWidget</code> or from <code>destroyWidget</code> when a widget
     * is being destroyed to ensure that the widget is marked as destroyed in case
     * the act of destroying the widget in the operating system causes application
     * code to run in callback that could access the widget. </p>
     *
     * @see #dispose
     *
     * @see #releaseChildren
     *
     * @see #releaseParent
     *
     * @see #releaseWidget
     */


    @Override
    void releaseHandle() {
    	super.releaseHandle();
		state |= DISPOSED;
		display = null;
    }

    /*
     * Releases the receiver, a child in a widget hierarchy, from its parent. <p>
     * When a widget is destroyed, it may be necessary to remove it from an internal
     * data structure of the parent. When a widget has no handle, it may also be
     * necessary for the parent to hide the widget or otherwise indicate that the
     * widget has been disposed. For example, disposing a menu bar requires that the
     * menu bar first be released from the shell when the menu bar is active. </p>
     *
     * @see #dispose
     *
     * @see #releaseChildren
     *
     * @see #releaseWidget
     *
     * @see #releaseHandle
     */


    void releaseParent() {
    }

    /*
     * Releases any internal resources back to the operating system and clears all
     * fields except the widget handle. <p> When a widget is destroyed, resources
     * that were acquired on behalf of the programmer need to be returned to the
     * operating system. For example, if the widget made a copy of an icon, supplied
     * by the programmer, this copy would be freed in <code>releaseWidget</code>.
     * Also, to assist the garbage collector and minimize the amount of memory that
     * is not reclaimed when the programmer keeps a reference to a disposed widget,
     * all fields except the handle are zero'd. The handle is needed by
     * <code>destroyWidget</code>. </p>
     *
     * @see #dispose
     *
     * @see #releaseChildren
     *
     * @see #releaseHandle
     *
     * @see #releaseParent
     */


    @Override
    void releaseWidget() {
		super.releaseWidget();
		eventTable = null;
		data = null;
    }

    /**
     * Removes the listener from the collection of listeners who will be notified
     * when an event of the given type occurs. The event type is one of the event
     * constants defined in class <code>SWT</code>.
     *
     * @param eventType the type of event to listen for
     * @param listener  the listener which should no longer be notified
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
     * @see Listener
     * @see SWT
     * @see #addListener
     * @see #getListeners(int)
     * @see #notifyListeners
     */


    public void removeListener(int eventType, Listener listener) {
	checkWidget();
	if (listener == null)
	    error(SWT.ERROR_NULL_ARGUMENT);
	_removeListener(eventType, listener);
    }

    /**
     * Removes the listener from the collection of listeners who will be notified
     * when an event of the given type occurs.
     * <p>
     * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT public API. It
     * is marked public only so that it can be shared within the packages provided
     * by SWT. It should never be referenced from application code.
     * </p>
     *
     * @param eventType the type of event to listen for
     * @param listener  the listener which should no longer be notified
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
     * @see Listener
     * @see #addListener
     *
     * @noreference This method is not intended to be referenced by clients.
     * @nooverride This method is not intended to be re-implemented or extended by
     *             clients.
     */


    protected void removeListener(int eventType, SWTEventListener listener) {
	removeTypedListener(eventType, listener);
    }

    /**
     * Removes the listener from the collection of listeners who will be notified
     * when an event of the given type occurs.
     * <p>
     * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT public API. It
     * is marked public only so that it can be shared within the packages provided
     * by SWT. It should never be referenced from application code.
     * </p>
     *
     * @param eventType the type of event to listen for
     * @param listener  the listener which should no longer be notified
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
     * @see Listener
     * @see #addListener
     *
     * @noreference This method is not intended to be referenced by clients.
     * @nooverride This method is not intended to be re-implemented or extended by
     *             clients.
     */


    protected void removeTypedListener(int eventType, EventListener listener) {
	checkWidget();
	if (listener == null)
	    error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null)
	    return;
	eventTable.unhook(eventType, listener);
    }

    /**
     * Removes the listener from the collection of listeners who will be notified
     * when the widget is disposed.
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
     * @see DisposeListener
     * @see #addDisposeListener
     */


    public void removeDisposeListener(DisposeListener listener) {
	checkWidget();
	if (listener == null)
	    error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null)
	    return;
	eventTable.unhook(SWT.Dispose, listener);
    }

    /**
     * Marks the widget to be skinned.
     * <p>
     * The skin event is sent to the receiver's display when appropriate (usually
     * before the next event is handled). Widgets are automatically marked for
     * skinning upon creation as well as when its skin id or class changes. The skin
     * id and/or class can be changed by calling
     * {@link Display#setData(String, Object)} with the keys {@link SWT#SKIN_ID}
     * and/or {@link SWT#SKIN_CLASS}. Once the skin event is sent to a widget, it
     * will not be sent again unless <code>reskin(int)</code> is called on the
     * widget or on an ancestor while specifying the <code>SWT.ALL</code> flag.
     * </p>
     * <p>
     * The parameter <code>flags</code> may be either:
     * </p>
     * <dl>
     * <dt><b>{@link SWT#ALL}</b></dt>
     * <dd>all children in the receiver's widget tree should be skinned</dd>
     * <dt><b>{@link SWT#NONE}</b></dt>
     * <dd>only the receiver should be skinned</dd>
     * </dl>
     *
     * @param flags the flags specifying how to reskin
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
     *                         disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
     *                         the thread that created the receiver</li>
     *                         </ul>
     * @since 3.6
     */


	public void reskin(int flags) {
	checkWidget();
	reskinWidget();
	if ((flags & SWT.ALL) != 0)
	    reskinChildren(flags);
    }



	void reskinChildren(int flags) {
    }



	void reskinWidget() {
	if ((state & SKIN_NEEDED) != SKIN_NEEDED) {
	    this.state |= SKIN_NEEDED;
	    display.addSkinnableWidget(this);
	}
    }


    boolean sendDragEvent(int button, int x, int y) {
	Event event = new Event();
	event.button = button;
	int zoom = getZoom();
	event.setLocation(DPIUtil.scaleDown(x, zoom), DPIUtil.scaleDown(y, zoom));
	setInputState(event, SWT.DragDetect);
	postEvent(SWT.DragDetect, event);
	if (isDisposed())
	    return false;
	return event.doit;
    }

    boolean sendDragEvent(int button, int stateMask, int x, int y) {
	Event event = new Event();
	event.button = button;
	int zoom = getZoom();
	event.setLocation(DPIUtil.scaleDown(x, zoom), DPIUtil.scaleDown(y, zoom));
	event.stateMask = stateMask;
	postEvent(SWT.DragDetect, event);
	if (isDisposed())
	    return false;
	return event.doit;
    }

boolean sendDragEvent(int button, int stateMask, int x, int y, boolean isStateMask) {
	Event event = new Event();
	event.button = button;
	int zoom = getZoom();
	event.setLocation(DPIUtil.scaleDown(x, zoom), DPIUtil.scaleDown(y, zoom));
	if (isStateMask)
		event.stateMask = stateMask;
	postEvent(SWT.DragDetect, event);
	if (isDisposed())
	    return false;
	return event.doit;
    }



    void sendEvent(Event event) {
	Display display = event.display;
	if (!display.filterEvent(event)) {
	    if (eventTable != null)
		display.sendEvent(eventTable, event);
	}
    }



    protected void sendEvent(int eventType) {
	sendEvent(eventType, null, true);
    }



	protected void sendEvent(int eventType, Event event) {
	sendEvent(eventType, event, true);
    }



	void sendEvent(int eventType, Event event, boolean send) {
	if (eventTable == null && !display.filters(eventType)) {
	    return;
	}
	if (event == null)
	    event = new Event();
	event.type = eventType;
	event.display = display;
	event.widget = this;
	if (event.time == 0) {
	    event.time = display.getLastEventTime();
	}
	if (send) {
	    sendEvent(event);
	} else {
	    display.postEvent(event);
	}
    }



    void sendSelectionEvent(int type) {
	sendSelectionEvent(type, null, false);
    }



	@Override
	void sendSelectionEvent(int type, Event event, boolean send) {
	if (eventTable == null && !display.filters(type)) {
	    return;
	}
	if (event == null)
	    event = new Event();
	setInputState(event, type);
	sendEvent(type, event, send);
    }



    /**
     * Sets the application defined widget data associated with the receiver to be
     * the argument. The <em>widget data</em> is a single, unnamed field that is
     * stored with every widget.
     * <p>
     * Applications may put arbitrary objects in this field. If the object stored in
     * the widget data needs to be notified when the widget is disposed of, it is
     * the application's responsibility to hook the Dispose event on the widget and
     * do so.
     * </p>
     *
     * @param data the widget data
     *
     * @exception SWTException
     *                         <ul>
     *                         <li>ERROR_WIDGET_DISPOSED - when the receiver has
     *                         been disposed</li>
     *                         <li>ERROR_THREAD_INVALID_ACCESS - when called from
     *                         the wrong thread</li>
     *                         </ul>
     *
     * @see #getData()
     */


    public void setData(Object data) {
	checkWidget();
	if ((state & KEYED_DATA) != 0) {
	    ((Object[]) this.data)[0] = data;
	} else {
	    this.data = data;
	}
    }



	boolean isActive() {
		return true;
	}

    /**
     * Sets the application defined property of the receiver with the specified name
     * to the given value.
     * <p>
     * Applications may associate arbitrary objects with the receiver in this
     * fashion. If the objects stored in the properties need to be notified when the
     * widget is disposed of, it is the application's responsibility to hook the
     * Dispose event on the widget and do so.
     * </p>
     *
     * @param key   the name of the property
     * @param value the new value for the property
     *
     * @exception IllegalArgumentException
     *                                     <ul>
     *                                     <li>ERROR_NULL_ARGUMENT - if the key is
     *                                     null</li>
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
     * @see #getData(String)
     */


    @Override
    public void setData(String key, Object value) {
	checkWidget();
	if (key == null)
	    error(SWT.ERROR_NULL_ARGUMENT);
	int index = 1;
	Object[] table = null;
	if ((state & KEYED_DATA) != 0) {
	    table = (Object[]) data;
	    while (index < table.length) {
		if (key.equals(table[index]))
		    break;
		index += 2;
	    }
	}
	if (value != null) {
	    if ((state & KEYED_DATA) != 0) {
		if (index == table.length) {
		    Object[] newTable = new Object[table.length + 2];
		    System.arraycopy(table, 0, newTable, 0, table.length);
		    data = table = newTable;
		}
	    } else {
		table = new Object[3];
		table[0] = data;
		data = table;
		state |= KEYED_DATA;
		}
	    table[index] = key;
	    table[index + 1] = value;
	} else {
	    if ((state & KEYED_DATA) != 0) {
		if (index != table.length) {
		    int length = table.length - 2;
		    if (length == 1) {
			data = table[0];
			state &= ~KEYED_DATA;
		    } else {
			Object[] newTable = new Object[length];
			System.arraycopy(table, 0, newTable, 0, index);
			System.arraycopy(table, index + 2, newTable, index, length - index);
			data = newTable;
		    }
		}
		}
	}
	if (key.equals(SWT.SKIN_CLASS) || key.equals(SWT.SKIN_ID))
	    this.reskin(SWT.ALL);
    }


    boolean setTabGroupFocus() {
	return setTabItemFocus();
    }



    boolean setTabItemFocus() {
	return false;
    }

	boolean setTabGroupFocus(boolean next) {
		return setTabItemFocus(next);
	}

	boolean setTabItemFocus(boolean next) {
		return false;
	}

	void setOrientation(boolean create) {


	}

    boolean showMenu(int x, int y) {
	return showMenu(x, y, SWT.MENU_MOUSE);
    }

    boolean showMenu(int x, int y, int detail) {
	Event event = new Event();
	Point mappedLocation = getDisplay().translateLocationInPointInDisplayCoordinateSystem(x, y);
	event.setLocation(mappedLocation.x, mappedLocation.y);
	event.detail = detail;
	if (event.detail == SWT.MENU_KEYBOARD) {
	    updateMenuLocation(event);
	}
	sendEvent(SWT.MenuDetect, event);
	// widget could be disposed at this point
	if (isDisposed())
	    return false;
	if (!event.doit)
	    return true;
	Menu menu = getMenu();
	if (menu != null && !menu.isDisposed()) {
	    Point locInPixels = DPIUtil.scaleUp(event.getLocation(), getZoom()); // In Pixels
	    if (x != locInPixels.x || y != locInPixels.y) {
		menu.setLocation(event.getLocation());
	    }
	    menu.setVisible(true);
	    return true;
	}
	return false;
    }

    /**
     * Returns a string containing a concise, human-readable description of the
     * receiver.
     *
     * @return a string representation of the receiver
     */


    @Override
    public String toString() {
	String string = "*Disposed*"; //$NON-NLS-1$
	if (!isDisposed()) {
	    string = "*Wrong Thread*"; //$NON-NLS-1$
	    if (isValidThread())
		string = getNameText();
	}
	return getName() + " {" + string + "}"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    void updateMenuLocation(Event event) {
	/* Do nothing */
    }




    void notifyCreationTracker() {
	if (WidgetSpy.isEnabled) {
	    WidgetSpy.getInstance().widgetCreated(this);
	}
    }



	void checkOpen() {
		/* Do nothing */
	}



    void notifyDisposalTracker() {
	if (WidgetSpy.isEnabled) {
	    WidgetSpy.getInstance().widgetDisposed(this);
	}
    }

    int getZoom() {
	return DPIUtil.getZoomForAutoscaleProperty(nativeZoom);
    }

    private static void handleDPIChange(Widget widget, int newZoom, float scalingFactor) {
		widget.nativeZoom = newZoom;
    }




    void copyToClipboard(char[] buffer) {
	// maybe with AWT this can be done for every OS.
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
    }

}
