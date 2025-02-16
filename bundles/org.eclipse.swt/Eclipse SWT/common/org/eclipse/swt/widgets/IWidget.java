/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
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
	void removeListener (int eventType, EventListener listener);
	void removeTypedListener (int eventType, EventListener listener);

	Widget getWrapper();

}