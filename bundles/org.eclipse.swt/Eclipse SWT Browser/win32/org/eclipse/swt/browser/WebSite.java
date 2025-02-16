/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.browser;

import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

class WebSite extends OleControlSite {
	private final NativeWebSite wrappedWebSite;

public WebSite(Composite parent, int style, String progId) {
	super (parent, style, progId);
	this.wrappedWebSite = new NativeWebSite(Widget.checkNative(parent), style, progId);
	this.wrappedWebSite.wrapperWebSite = this;
}

@Override
protected NativeWebSite getWrappedWidget() {
	return wrappedWebSite;
}

}
