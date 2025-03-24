/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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

import java.util.*;

import org.eclipse.swt.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select or enter a file name.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SAVE, OPEN, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SAVE and OPEN may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#filedialog">FileDialog snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FileDialog extends Dialog {

	private final NativeFileDialog wrappedFileDialog;

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FileDialog (Shell parent) {
	this (new NativeFileDialog(Widget.checkNative(parent)));
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
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#SAVE
 * @see SWT#OPEN
 * @see SWT#MULTI
 */
public FileDialog (Shell parent, int style) {
	this (new NativeFileDialog(Widget.checkNative(parent), style));
}

FileDialog(NativeFileDialog nativeFileDialog) {
	super (nativeFileDialog);
	this.wrappedFileDialog = nativeFileDialog;
}

/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path, or an
 * empty string if no such file has been selected.
 *
 * @return the relative path of the file
 */
public String getFileName () {
	return wrappedFileDialog.getFileName();
}

/**
 * Returns a (possibly empty) array with the paths of all files
 * that were selected in the dialog relative to the filter path.
 *
 * @return the relative paths of the files
 */
public String [] getFileNames () {
	return wrappedFileDialog.getFileNames();
}

/**
 * Returns the file extensions which the dialog will
 * use to filter the files it shows.
 *
 * @return the file extensions filter
 */
public String [] getFilterExtensions () {
	return wrappedFileDialog.getFilterExtensions();
}

/**
 * Get the 0-based index of the file extension filter
 * which was selected by the user, or -1 if no filter
 * was selected.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @return index the file extension filter index
 *
 * @see #getFilterExtensions
 * @see #getFilterNames
 *
 * @since 3.4
 */
public int getFilterIndex () {
	return wrappedFileDialog.getFilterIndex();
}

/**
 * Returns the names that describe the filter extensions
 * which the dialog will use to filter the files it shows.
 *
 * @return the list of filter names
 */
public String [] getFilterNames () {
	return wrappedFileDialog.getFilterNames();
}

/**
 * Returns the directory path that the dialog will use, or an empty
 * string if this is not set.  File names in this path will appear
 * in the dialog, filtered according to the filter extensions.
 *
 * @return the directory path string
 *
 * @see #setFilterExtensions
 */
public String getFilterPath () {
	return wrappedFileDialog.getFilterPath();
}

/**
 * Returns the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 *
 * @return true if the dialog will prompt for file overwrite, false otherwise
 *
 * @since 3.4
 */
public boolean getOverwrite () {
	return wrappedFileDialog.getOverwrite();
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the first selected file,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	return wrappedFileDialog.open();
}

/**
 * Makes the dialog visible and brings it to the front of the display.
 * Equal to {@link FileDialog#open()} but also exposes for state information like user cancellation.
 *
 * @return an Optional that either contains the absolute path of the first selected file
 *         or is empty in case the dialog was canceled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 *    <li>ERROR_INVALID_RETURN_VALUE - if the dialog was not cancelled and did not return a valid path</li>
 * </ul>
 *
 * @since 3.126
 */
public Optional<String> openDialog () {
	return wrappedFileDialog.openDialog();
}

/**
 * Set the initial filename which the dialog will
 * select by default when opened to the argument,
 * which may be null.  The name will be prefixed with
 * the filter path when one is supplied.
 *
 * @param string the file name
 */
public void setFileName (String string) {
	wrappedFileDialog.setFileName(string);
}

/**
 * Set the file extensions which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 * <p>
 * The strings are platform specific. For example, on
 * some platforms, an extension filter string is typically
 * of the form "*.extension", where "*.*" matches all files.
 * For filters with multiple extensions, use semicolon as
 * a separator, e.g. "*.jpg;*.png".
 * </p>
 * <p>
 * Note: On Mac, setting the file extension filter affects how
 * app bundles are treated by the dialog. When a filter extension
 * having the app extension (.app) is selected, bundles are treated
 * as files. For all other extension filters, bundles are treated
 * as directories. When no filter extension is set, bundles are
 * treated as files.
 * </p>
 *
 * @param extensions the file extension filter
 *
 * @see #setFilterNames to specify the user-friendly
 * names corresponding to the extensions
 */
public void setFilterExtensions (String [] extensions) {
	wrappedFileDialog.setFilterExtensions(extensions);
}

/**
 * Set the 0-based index of the file extension filter
 * which the dialog will use initially to filter the files
 * it shows to the argument.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @param index the file extension filter index
 *
 * @see #setFilterExtensions
 * @see #setFilterNames
 *
 * @since 3.4
 */
public void setFilterIndex (int index) {
	wrappedFileDialog.setFilterIndex(index);
}

/**
 * Sets the names that describe the filter extensions
 * which the dialog will use to filter the files it shows
 * to the argument, which may be null.
 * <p>
 * Each name is a user-friendly short description shown for
 * its corresponding filter. The <code>names</code> array must
 * be the same length as the <code>extensions</code> array.
 * </p>
 *
 * @param names the list of filter names, or null for no filter names
 *
 * @see #setFilterExtensions
 */
public void setFilterNames (String [] names) {
	wrappedFileDialog.setFilterNames(names);
}

/**
 * Sets the directory path that the dialog will use
 * to the argument, which may be null. File names in this
 * path will appear in the dialog, filtered according
 * to the filter extensions. If the string is null,
 * then the operating system's default filter path
 * will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the directory path
 *
 * @see #setFilterExtensions
 */
public void setFilterPath (String string) {
	wrappedFileDialog.setFilterPath(string);
}

/**
 * Sets the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 * <p>
 * Note: On some platforms where suppressing the overwrite prompt
 * is not supported, the prompt is shown even when invoked with
 * overwrite false.
 * </p>
 *
 * @param overwrite true if the dialog will prompt for file overwrite, false otherwise
 *
 * @since 3.4
 */
public void setOverwrite (boolean overwrite) {
	wrappedFileDialog.setOverwrite(overwrite);
}

@Override
protected NativeFileDialog getWrappedDialog() {
	return wrappedFileDialog;
}

}
