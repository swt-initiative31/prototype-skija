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
package org.eclipse.swt.ole.win32;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * OleControlSite provides a site to manage an embedded ActiveX Control within a container.
 *
 * <p>In addition to the behaviour provided by OleClientSite, this object provides the following:
 * <ul>
 *	<li>events from the ActiveX control
 * 	<li>notification of property changes from the ActiveX control
 *	<li>simplified access to well known properties of the ActiveX Control (e.g. font, background color)
 *	<li>expose ambient properties of the container to the ActiveX Control
 * </ul>
 *
 * <p>This object implements the OLE Interfaces IOleControlSite, IDispatch, and IPropertyNotifySink.
 *
 * <p>Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p>
 * <dl>
 *	<dt><b>Styles</b> <dd>BORDER
 *	<dt><b>Events</b> <dd>Dispose, Move, Resize
 * </dl>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#ole">OLE and ActiveX snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: OLEExample, OleWebBrowser</a>
 */
public class OleControlSite extends OleClientSite
{
	private final NativeOleControlSite wrappedOleControlSite;

/**
 * Create an OleControlSite child widget using the OLE Document type associated with the
 * specified file.  The OLE Document type is determined either through header information in the file
 * or through a Registry entry for the file extension. Use style bits to select a particular look
 * or set of properties.
 *
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param file the file that is to be opened in this OLE Document
 *
 * @exception IllegalArgumentException
 * <ul><li>ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame</ul>
 * @exception SWTException
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_CANNOT_OPEN_FILE when failed to open file
 *     <li>ERROR_INTERFACE_NOT_FOUND when unable to create callbacks for OLE Interfaces
 *     <li>ERROR_INVALID_CLASSID
 * </ul>
 *
 * @since 3.5
 */
public OleControlSite(Composite parent, int style, File file) {
	super (parent, style, file);
	this.wrappedOleControlSite = new NativeOleControlSite(Widget.checkNative(parent), style, file) {
		@Override
		public OleControlSite getWrapper() {
			return OleControlSite.this;
		}
	};
}
/**
 * Create an OleControlSite child widget using style bits
 * to select a particular look or set of properties.
 *
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param progId the unique program identifier which has been registered for this ActiveX Control;
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for this Control (for example, the VersionIndependentProgID for
 *               Internet Explorer is Shell.Explorer)
 *
 * @exception IllegalArgumentException <ul>
 *     <li>ERROR_NULL_ARGUMENT when the parent is null
 *</ul>
 * @exception SWTException <ul>
 *     <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_CANNOT_ACCESS_CLASSFACTORY when Class Factory could not be found
 *     <li>ERROR_CANNOT_CREATE_LICENSED_OBJECT when failed to create a licensed OLE Object
 * </ul>
 */
public OleControlSite(Composite parent, int style, String progId) {
	super (parent, style, progId);
	this.wrappedOleControlSite = new NativeOleControlSite(Widget.checkNative(parent), style, progId) {
		@Override
		public OleControlSite getWrapper() {
			return OleControlSite.this;
		}
	};
}
/**
 * Create an OleClientSite child widget to edit the specified file using the specified OLE Document
 * application.  Use style bits to select a particular look or set of properties.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>OleClientSite</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param progId the unique program identifier of am OLE Document application;
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for the desired OLE Document (for example, the VersionIndependentProgID
 *               for Word is Word.Document)
 * @param file the file that is to be opened in this OLE Document
 *
 * @exception IllegalArgumentException
 * <ul><li>ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame</ul>
 * @exception SWTException
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_CANNOT_OPEN_FILE when failed to open file
 * </ul>
 *
 * @noreference This method is not intended to be referenced by clients.
 *
 * @since 3.5
 */
public OleControlSite(Composite parent, int style, String progId, File file) {
	super (parent, style, progId, file);
	this.wrappedOleControlSite = new NativeOleControlSite(Widget.checkNative(parent), style, progId, file) {
		@Override
		public OleControlSite getWrapper() {
			return OleControlSite.this;
		}
	};
	setSiteProperty(COM.DISPID_AMBIENT_UIDEAD, new Variant(false));
}
/**
 * Adds the listener to receive events.
 *
 * @param eventID the id of the event
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *	    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addEventListener(int eventID, OleListener listener) {
	wrappedOleControlSite.addEventListener(eventID, listener);

}
/**
 * Adds the listener to receive events.
 *
 * @since 2.0
 *
 * @param automation the automation object that provides the event notification
 * @param eventID the id of the event
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *	   <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addEventListener(OleAutomation automation, int eventID, OleListener listener) {
	wrappedOleControlSite.addEventListener(automation, eventID, listener);
}
/**
 * Adds the listener to receive events.
 *
 * @since 3.2
 *
 * @param automation the automation object that provides the event notification
 * @param eventSinkId the GUID of the event sink
 * @param eventID the id of the event
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *	   <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addEventListener(OleAutomation automation, String eventSinkId, int eventID, OleListener listener) {
	wrappedOleControlSite.addEventListener(automation, eventSinkId, eventID, listener);
}

/**
 * Adds the listener to receive events.
 *
 * @param propertyID the identifier of the property
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *	    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addPropertyListener(int propertyID, OleListener listener) {
	wrappedOleControlSite.addPropertyListener(propertyID, listener);
}

protected long getLicenseInfo(GUID clsid) {
	return wrappedOleControlSite.getLicenseInfo(clsid);
}
/**
 *
 * Get the control site property specified by the dispIdMember, or
 * <code>null</code> if the dispId is not recognised.
 *
 * @param dispId the dispId
 *
 * @return the property value or <code>null</code>
 *
 * @since 2.1
 */
public Variant getSiteProperty(int dispId){
	return wrappedOleControlSite.getSiteProperty(dispId);
}
/**
 * Removes the listener.
 *
 * @param eventID the event identifier
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *	    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeEventListener(int eventID, OleListener listener) {
	wrappedOleControlSite.removeEventListener(eventID, listener);
}
/**
 * Removes the listener.
 *
 * @since 2.0
 * @deprecated - use OleControlSite.removeEventListener(OleAutomation, int, OleListener)
 *
 * @param automation the automation object that provides the event notification
 *
 * @param guid the identifier of the events COM interface
 *
 * @param eventID the event identifier
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *	    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
@Deprecated
public void removeEventListener(OleAutomation automation, GUID guid, int eventID, OleListener listener) {
	wrappedOleControlSite.removeEventListener(automation, guid, eventID, listener);
}
/**
 * Removes the listener.
 *
 * @param automation the automation object that provides the event notification
 * @param eventID the event identifier
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *	    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 *
 * @since 2.0
 */
public void removeEventListener(OleAutomation automation, int eventID, OleListener listener) {
	wrappedOleControlSite.removeEventListener(automation, eventID, listener);
}
/**
 * Removes the listener.
 *
 * @param propertyID the identifier of the property
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *	    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removePropertyListener(int propertyID, OleListener listener) {
	wrappedOleControlSite.removePropertyListener(propertyID, listener);
}
/**
 * Sets the control site property specified by the dispIdMember to a new value.
 * The value will be disposed by the control site when it is no longer required
 * using Variant.dispose.  Passing a value of null will clear the dispId value.
 *
 * @param dispId the ID of the property as specified by the IDL of the ActiveX Control
 * @param value The new value for the property as expressed in a Variant.
 *
 * @since 2.1
 */
public void setSiteProperty(int dispId, Variant value){
	wrappedOleControlSite.setSiteProperty(dispId, value);
}
}
