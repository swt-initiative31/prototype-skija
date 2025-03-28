/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
package org.eclipse.swt.ole.win32;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * OleFrame is an OLE Container's top level frame.
 *
 * <p>This object implements the OLE Interfaces IUnknown and IOleInPlaceFrame
 *
 * <p>OleFrame allows the container to do the following: <ul>
 *	<li>position and size the ActiveX Control or OLE Document within the application
 *	<li>insert menu items from the application into the OLE Document's menu
 *	<li>activate and deactivate the OLE Document's menus
 *	<li>position the OLE Document's menu in the application
 *	<li>translate accelerator keystrokes intended for the container's frame</ul>
 *
 * <dl>
 *	<dt><b>Styles</b> <dd>BORDER
 *	<dt><b>Events</b> <dd>Dispose, Move, Resize
 * </dl>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#ole">OLE and ActiveX snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: OLEExample, OleWebBrowser</a>
 */
final public class OleFrame extends Composite
{

	private final NativeOleFrame wrappedOleFrame;

/**
 * Create an OleFrame child widget using style bits
 * to select a particular look or set of properties.
 *
 * @param parent a composite widget (cannot be null)
 * @param style the bitwise OR'ing of widget styles
 *
 * @exception IllegalArgumentException <ul>
 *     <li>ERROR_NULL_ARGUMENT when the parent is null
 * </ul>
 * @exception SWTException <ul>
 *     <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 * </ul>
 */
public OleFrame(Composite parent, int style) {
	this.wrappedOleFrame = new NativeOleFrame(Widget.checkNative(parent), style) {
		@Override
		public OleFrame getWrapper() {
			return OleFrame.this;
		}
	};
}
public OleFrame(NativeComposite parent, int style) {
	this.wrappedOleFrame = new NativeOleFrame(parent, style) {
		@Override
		public OleFrame getWrapper() {
			return OleFrame.this;
		}
	};
}

/**
 *
 * Returns the application menu items that will appear in the Container location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * @return the application menu items that will appear in the Container location when an OLE Document
 *         is in-place activated.
 */
public MenuItem[] getContainerMenus(){
	return Arrays.stream(wrappedOleFrame.getContainerMenus()).map(NativeMenuItem::getWrapper).toArray(MenuItem[]::new);
}
/**
 *
 * Returns the application menu items that will appear in the File location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * @return the application menu items that will appear in the File location when an OLE Document
 *         is in-place activated.
 */
public MenuItem[] getFileMenus(){
	return Arrays.stream(wrappedOleFrame.getFileMenus()).map(NativeMenuItem::getWrapper).toArray(MenuItem[]::new);
}
/**
 *
 * Returns the application menu items that will appear in the Window location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * @return the application menu items that will appear in the Window location when an OLE Document
 *         is in-place activated.
 */
public MenuItem[] getWindowMenus(){
	return Arrays.stream(wrappedOleFrame.getWindowMenus()).map(NativeMenuItem::getWrapper).toArray(MenuItem[]::new);
}
/**
 *
 * Specify the menu items that should appear in the Container location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * <p>This method must be called before in place activation of the OLE Document.  After the Document
 * is activated, the menu bar will not be modified until a subsequent activation.
 *
 * @param containerMenus an array of top level MenuItems to be inserted into the Container location of
 *        the menubar
 */
public void setContainerMenus(MenuItem[] containerMenus){
	wrappedOleFrame.setContainerMenus(Widget.checkNative(containerMenus));
}
/**
 *
 * Specify the menu items that should appear in the File location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * <p>This method must be called before in place activation of the OLE Document.  After the Document
 * is activated, the menu bar will not be modified until a subsequent activation.
 *
 * @param fileMenus an array of top level MenuItems to be inserted into the File location of
 *        the menubar
 */
public void setFileMenus(MenuItem[] fileMenus){
	wrappedOleFrame.setContainerMenus(Widget.checkNative(fileMenus));
}
/**
 *
 * Set the menu items that should appear in the Window location when an OLE Document
 * is in-place activated.
 *
 * <p>When an OLE Document is in-place active, the Document provides its own menus but the application
 * is given the opportunity to merge some of its menus into the menubar.  The application
 * is allowed to insert its menus in three locations: File (far left), Container(middle) and Window
 * (far right just before Help).  The OLE Document retains control of the Edit, Object and Help
 * menu locations.  Note that an application can insert more than one menu into a single location.
 *
 * <p>This method must be called before in place activation of the OLE Document.  After the Document
 * is activated, the menu bar will not be modified until a subsequent activation.
 *
 * @param windowMenus an array of top level MenuItems to be inserted into the Window location of
 *        the menubar
 */
public void setWindowMenus(MenuItem[] windowMenus){
	wrappedOleFrame.setWindowMenus(Widget.checkNative(windowMenus));
}

@Override
public NativeOleFrame getWrappedWidget() {
	return wrappedOleFrame;
}

}
