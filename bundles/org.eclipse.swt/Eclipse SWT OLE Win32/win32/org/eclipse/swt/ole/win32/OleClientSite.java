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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.widgets.*;
/**
 * OleClientSite provides a site to manage an embedded OLE Document within a container.
 *
 * <p>The OleClientSite provides the following capabilities:
 * <ul>
 *  <li>creates the in-place editor for a blank document or opening an existing OLE Document
 * 	<li>lays the editor out
 *	<li>provides a mechanism for activating and deactivating the Document
 *	<li>provides a mechanism for saving changes made to the document
 * </ul>
 *
 * <p>This object implements the OLE Interfaces IUnknown, IOleClientSite, IAdviseSink,
 * IOleInPlaceSite
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
public class OleClientSite extends Composite {

	private final NativeOleClientSite wrappedOleClientSite;

protected OleClientSite(Composite parent, int style) {
	this.wrappedOleClientSite = new NativeOleClientSite(Widget.checkNative(parent), style) {
		@Override
		public OleClientSite getWrapper() {
			return OleClientSite.this;
		}
	};
}

/**
 * Create an OleClientSite child widget using the OLE Document type associated with the
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
 */
public OleClientSite(Composite parent, int style, File file) {
	this.wrappedOleClientSite = new NativeOleClientSite(Widget.checkNative(parent), style, file) {
		@Override
		public OleClientSite getWrapper() {
			return OleClientSite.this;
		}
	};
}
/**
 * Create an OleClientSite child widget to edit a blank document using the specified OLE Document
 * application.  Use style bits to select a particular look or set of properties.
 *
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param progId the unique program identifier of an OLE Document application;
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for the desired OLE Document (for example, the VersionIndependentProgID
 *               for Word is Word.Document)
 *
 * @exception IllegalArgumentException
 *<ul>
 *     <li>ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame
 *</ul>
 * @exception SWTException
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 * </ul>
 */
public OleClientSite(Composite parent, int style, String progId) {
	this.wrappedOleClientSite = new NativeOleClientSite(Widget.checkNative(parent), style, progId) {
		@Override
		public OleClientSite getWrapper() {
			return OleClientSite.this;
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
 */
public OleClientSite(Composite parent, int style, String progId, File file) {
	this.wrappedOleClientSite = new NativeOleClientSite(Widget.checkNative(parent), style, progId, file) {
		@Override
		public OleClientSite getWrapper() {
			return OleClientSite.this;
		}
	};
}

protected void addObjectReferences() {
	wrappedOleClientSite.addObjectReferences();
}
protected int AddRef() {
	return wrappedOleClientSite.AddRef();
}
protected void createCOMInterfaces() {
	wrappedOleClientSite.createCOMInterfaces();
}
protected IStorage createTempStorage() {
	return wrappedOleClientSite.createTempStorage();
}
/**
 * Deactivates an active in-place object and discards the object's undo state.
 */
public void deactivateInPlaceClient() {
	wrappedOleClientSite.deactivateInPlaceClient();
}
protected void disposeCOMInterfaces() {
	wrappedOleClientSite.disposeCOMInterfaces();
}
/**
 * Requests that the OLE Document or ActiveX Control perform an action; actions are almost always
 * changes to the activation state.
 *
 * @param verb the operation that is requested.  This is one of the OLE.OLEIVERB_ values
 *
 * @return an HRESULT value indicating the success of the operation request; OLE.S_OK indicates
 *         success
 */
public int doVerb(int verb) {
	return wrappedOleClientSite.doVerb(verb);
}
/**
 * Asks the OLE Document or ActiveX Control to execute a command from a standard
 * list of commands. The OLE Document or ActiveX Control must support the IOleCommandTarget
 * interface.  The OLE Document or ActiveX Control does not have to support all the commands
 * in the standard list.  To check if a command is supported, you can call queryStatus with
 * the cmdID.
 *
 * @param cmdID the ID of a command; these are the OLE.OLECMDID_ values - a small set of common
 *              commands
 * @param options the optional flags; these are the OLE.OLECMDEXECOPT_ values
 * @param in the argument for the command
 * @param out the return value of the command
 *
 * @return an HRESULT value; OLE.S_OK is returned if successful
 */
public int exec(int cmdID, int options, Variant in, Variant out) {
	return wrappedOleClientSite.exec(cmdID, options, in, out);
}
/**
 * Returns the indent value that would be used to compute the clipping area
 * of the active X object.
 *
 * NOTE: The indent value is no longer being used by the client site.
 *
 * @return the rectangle representing the indent
 */
public Rectangle getIndent() {
	return wrappedOleClientSite.getIndent();
}
/**
 * Returns the program ID of the OLE Document or ActiveX Control.
 *
 * @return the program ID of the OLE Document or ActiveX Control
 */
public String getProgramID(){
	return wrappedOleClientSite.getProgramID();
}
protected int GetWindow(long phwnd) {
	return wrappedOleClientSite.GetWindow(phwnd);
}
/**
 * Returns whether ole document is dirty by checking whether the content
 * of the file representing the document is dirty.
 *
 * @return <code>true</code> if the document has been modified,
 *         <code>false</code> otherwise.
 * @since 3.1
 */
public boolean isDirty() {
	return wrappedOleClientSite.isDirty();
}
@Override
public boolean isFocusControl () {
	return wrappedOleClientSite.isFocusControl();
}
protected int QueryInterface(long riid, long ppvObject) {
	return wrappedOleClientSite.QueryInterface(riid, ppvObject);
}
/**
 * Returns the status of the specified command.  The status is any bitwise OR'd combination of
 * SWTOLE.OLECMDF_SUPPORTED, SWTOLE.OLECMDF_ENABLED, SWTOLE.OLECMDF_LATCHED, SWTOLE.OLECMDF_NINCHED.
 * You can query the status of a command before invoking it with OleClientSite.exec.  The
 * OLE Document or ActiveX Control must support the IOleCommandTarget to make use of this method.
 *
 * @param cmd the ID of a command; these are the OLE.OLECMDID_ values - a small set of common
 *            commands
 *
 * @return the status of the specified command or 0 if unable to query the OLE Object; these are the
 *			  OLE.OLECMDF_ values
 */
public int queryStatus(int cmd) {
	return wrappedOleClientSite.queryStatus(cmd);
}
protected int Release() {
	return wrappedOleClientSite.Release();
}
protected void releaseObjectInterfaces() {
	wrappedOleClientSite.releaseObjectInterfaces();
}
/**
 * Saves the document to the specified file and includes OLE specific information if specified.
 * This method must <b>only</b> be used for files that have an OLE Storage format.  For example,
 * a word file edited with Word.Document should be saved using this method because there is
 * formating information that should be stored in the OLE specific Storage format.
 *
 * @param file the file to which the changes are to be saved
 * @param includeOleInfo the flag to indicate whether OLE specific information should be saved.
 *
 * @return true if the save was successful
 */
public boolean save(File file, boolean includeOleInfo) {
	return wrappedOleClientSite.save(file, includeOleInfo);
}
/**
 * The indent value is no longer being used by the client site.
 *
 * @param newIndent the rectangle representing the indent amount
 */
public void setIndent(Rectangle newIndent) {
	wrappedOleClientSite.setIndent(newIndent);
}
/**
 * Displays a dialog with the property information for this OLE Object.  The OLE Document or
 * ActiveX Control must support the ISpecifyPropertyPages interface.
 *
 * @param title the name that will appear in the titlebar of the dialog
 */
public void showProperties(String title) {
	wrappedOleClientSite.showProperties(title);
}

@Override
protected NativeOleClientSite getWrappedWidget() {
	return wrappedOleClientSite;
}
}
