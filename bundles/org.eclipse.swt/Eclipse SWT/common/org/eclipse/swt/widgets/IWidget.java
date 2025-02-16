package org.eclipse.swt.widgets;

import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.*;

public interface IWidget {

	void addListener(int eventType, Listener listener);

	void addDisposeListener(DisposeListener listener);

	void dispose();

	Object getData();

	Object getData(String key);

	Display getDisplay();

	Listener[] getListeners(int eventType);

	<L extends EventListener> Stream<L> getTypedListeners(int eventType, Class<L> listenerType);

	int getStyle();

	boolean isAutoDirection();

	boolean isDisposed();

	boolean isListening(int eventType);

	void notifyListeners(int eventType, Event event);

	void removeListener(int eventType, Listener listener);

	void removeDisposeListener(DisposeListener listener);

	void reskin(int flags);

	void setData(Object data);

	void setData(String key, Object value);

	// Internal methods
	void addTypedListener (EventListener listener, int... eventTypes);
	void checkSubclass ();
	void checkWidget ();
	void removeListener (int eventType, SWTEventListener listener);
	void removeTypedListener (int eventType, EventListener listener);

	Widget getWrapper();

}