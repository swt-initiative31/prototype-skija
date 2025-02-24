package org.eclipse.swt.widgets;

import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.*;

public interface IWidget {

	/**
	 * Adds the listener to the collection of {@link Listener listeners} who will
	 * be notified when an event of the given type occurs. When the
	 * event does occur in the widget, the listener is notified by
	 * sending it the <code>handleEvent()</code> message. The event
	 * type is one of the event constants defined in class {@link SWT}.
	 *
	 * @param eventType the type of event to listen for
	 * @param listener the listener which should be notified when the event occurs
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #getListeners(int)
	 * @see #removeListener(int, Listener)
	 * @see #notifyListeners
	 */
	void addListener(int eventType, Listener listener);

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the widget is disposed. When the widget is
	 * disposed, the listener is notified by sending it the
	 * <code>widgetDisposed()</code> message.
	 *
	 * @param listener the listener which should be notified when the receiver is disposed
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see DisposeListener
	 * @see #removeDisposeListener
	 */
	void addDisposeListener(DisposeListener listener);

	/**
	 * Disposes of the operating system resources associated with
	 * the receiver and all its descendants. After this method has
	 * been invoked, the receiver and all descendants will answer
	 * <code>true</code> when sent the message <code>isDisposed()</code>.
	 * Any internal connections between the widgets in the tree will
	 * have been removed to facilitate garbage collection.
	 * This method does nothing if the widget is already disposed.
	 * <p>
	 * NOTE: This method is not called recursively on the descendants
	 * of the receiver. This means that, widget implementers can not
	 * detect when a widget is being disposed of by re-implementing
	 * this method, but should instead listen for the <code>Dispose</code>
	 * event.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #addDisposeListener
	 * @see #removeDisposeListener
	 */
	void dispose();

	/**
	 * Returns the application defined widget data associated
	 * with the receiver, or null if it has not been set. The
	 * <em>widget data</em> is a single, unnamed field that is
	 * stored with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If
	 * the object stored in the widget data needs to be notified
	 * when the widget is disposed of, it is the application's
	 * responsibility to hook the Dispose event on the widget and
	 * do so.
	 * </p>
	 *
	 * @return the widget data
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
	 * </ul>
	 *
	 * @see #setData(Object)
	 */
	Object getData();

	/**
	 * Returns the application defined property of the receiver
	 * with the specified name, or null if it has not been set.
	 * <p>
	 * Applications may have associated arbitrary objects with the
	 * receiver in this fashion. If the objects stored in the
	 * properties need to be notified when the widget is disposed
	 * of, it is the application's responsibility to hook the
	 * Dispose event on the widget and do so.
	 * </p>
	 *
	 * @param	key the name of the property
	 * @return the value of the property or null if it has not been set
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #setData(String, Object)
	 */
	Object getData(String key);

	/**
	 * Returns the <code>Display</code> that is associated with
	 * the receiver.
	 * <p>
	 * A widget's display is either provided when it is created
	 * (for example, top level <code>Shell</code>s) or is the
	 * same as its parent's display.
	 * </p>
	 *
	 * @return the receiver's display
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	Display getDisplay();

	/**
	 * Returns an array of {@link Listener listeners} who will be notified when an event
	 * of the given type occurs. The event type is one of the event constants
	 * defined in class {@link SWT}.
	 *
	 * @param eventType the type of event to listen for
	 * @return an array of listeners that will be notified when the event occurs
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #addListener(int, Listener)
	 * @see #removeListener(int, Listener)
	 * @see #notifyListeners
	 *
	 * @since 3.4
	 */
	Listener[] getListeners(int eventType);

	/**
	 * Returns the typed listeners who will be notified when an event of the given type occurs.
	 * The event type is one of the event constants defined in class {@link SWT}
	 * and the specified listener-type must correspond to that event.
	 * If for example the {@code eventType} is {@link SWT#Selection},
	 * the listeners type should be {@link SelectionListener}.
	 *
	 * @param eventType the type of event to listen for
	 * @return a stream of typed listeners that will be notified when the event occurs
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #notifyListeners
	 *
	 * @since 3.126
	 */
	<L extends EventListener> Stream<L> getTypedListeners(int eventType, Class<L> listenerType);

	/**
	 * Returns the receiver's style information.
	 * <p>
	 * Note that the value which is returned by this method <em>may
	 * not match</em> the value which was provided to the constructor
	 * when the receiver was created. This can occur when the underlying
	 * operating system does not support a particular combination of
	 * requested styles. For example, if the platform widget used to
	 * implement a particular SWT widget always has scroll bars, the
	 * result of calling this method would always have the
	 * <code>SWT.H_SCROLL</code> and <code>SWT.V_SCROLL</code> bits set.
	 * </p>
	 *
	 * @return the style bits
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	int getStyle();

	/**
	 * Returns <code>true</code> if the widget has auto text direction,
	 * and <code>false</code> otherwise.
	 *
	 * @return <code>true</code> when the widget has auto direction and <code>false</code> otherwise
	 *
	 * @see SWT#AUTO_TEXT_DIRECTION
	 *
	 * @since 3.105
	 */
	boolean isAutoDirection();

	/**
	 * Returns <code>true</code> if the widget has been disposed,
	 * and <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the widget.
	 * When a widget has been disposed, it is an error to
	 * invoke any other method (except {@link #dispose()}) using the widget.
	 * </p>
	 *
	 * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
	 */
	boolean isDisposed();

	/**
	 * Returns <code>true</code> if there are any listeners
	 * for the specified event type associated with the receiver,
	 * and <code>false</code> otherwise. The event type is one of
	 * the event constants defined in class <code>SWT</code>.
	 *
	 * @param eventType the type of event
	 * @return true if the event is hooked
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see SWT
	 */
	boolean isListening(int eventType);

	/**
	 * Notifies all of the receiver's listeners for events
	 * of the given type that one such event has occurred by
	 * invoking their <code>handleEvent()</code> method.  The
	 * event type is one of the event constants defined in class
	 * <code>SWT</code>.
	 *
	 * @param eventType the type of event which has occurred
	 * @param event the event data
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see SWT
	 * @see #addListener
	 * @see #getListeners(int)
	 * @see #removeListener(int, Listener)
	 */
	void notifyListeners(int eventType, Event event);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when an event of the given type occurs. The event
	 * type is one of the event constants defined in class <code>SWT</code>.
	 *
	 * @param eventType the type of event to listen for
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
	 * @see Listener
	 * @see SWT
	 * @see #addListener
	 * @see #getListeners(int)
	 * @see #notifyListeners
	 */
	void removeListener(int eventType, Listener listener);

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the widget is disposed.
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
	 * @see DisposeListener
	 * @see #addDisposeListener
	 */
	void removeDisposeListener(DisposeListener listener);

	/**
	 * Marks the widget to be skinned.
	 * <p>
	 * The skin event is sent to the receiver's display when appropriate (usually before the next event
	 * is handled). Widgets are automatically marked for skinning upon creation as well as when its skin
	 * id or class changes. The skin id and/or class can be changed by calling {@link Display#setData(String, Object)}
	 * with the keys {@link SWT#SKIN_ID} and/or {@link SWT#SKIN_CLASS}. Once the skin event is sent to a widget, it
	 * will not be sent again unless <code>reskin(int)</code> is called on the widget or on an ancestor
	 * while specifying the <code>SWT.ALL</code> flag.
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
	 * @param flags the flags specifying how to reskin
	 *
	 * @exception SWTException
	 * <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 * @since 3.6
	 */
	void reskin(int flags);

	/**
	 * Sets the application defined widget data associated
	 * with the receiver to be the argument. The <em>widget
	 * data</em> is a single, unnamed field that is stored
	 * with every widget.
	 * <p>
	 * Applications may put arbitrary objects in this field. If
	 * the object stored in the widget data needs to be notified
	 * when the widget is disposed of, it is the application's
	 * responsibility to hook the Dispose event on the widget and
	 * do so.
	 * </p>
	 *
	 * @param data the widget data
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
	 * </ul>
	 *
	 * @see #getData()
	 */
	void setData(Object data);

	/**
	 * Sets the application defined property of the receiver
	 * with the specified name to the given value.
	 * <p>
	 * Applications may associate arbitrary objects with the
	 * receiver in this fashion. If the objects stored in the
	 * properties need to be notified when the widget is disposed
	 * of, it is the application's responsibility to hook the
	 * Dispose event on the widget and do so.
	 * </p>
	 *
	 * @param key the name of the property
	 * @param value the new value for the property
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #getData(String)
	 */
	void setData(String key, Object value);

	/**
	 * Returns a string containing a concise, human-readable
	 * description of the receiver.
	 *
	 * @return a string representation of the receiver
	 */
	@Override
	String toString();

	// Internal methods
	void addTypedListener (EventListener listener, int... eventTypes);
	void checkSubclass ();
	void checkWidget ();
	void removeListener (int eventType, SWTEventListener listener);
	void removeTypedListener (int eventType, EventListener listener);

}