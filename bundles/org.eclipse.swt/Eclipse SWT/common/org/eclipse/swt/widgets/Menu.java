/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Instances of this class are user interface objects that contain menu items.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BAR, DROP_DOWN, POP_UP, NO_RADIO_GROUP</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Help, Hide, Show</dd>
 * </dl>
 * <p>
 * Note: Only one of BAR, DROP_DOWN and POP_UP may be specified. Only one of
 * LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#menu">Menu snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Menu extends Widget {
	/**
	 * the handle to the OS resource (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API. It
	 * is marked public only so that it can be shared within the packages provided
	 * by SWT. It is not available on all platforms and should never be accessed
	 * from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public long handle;

	int x, y;
	long hBrush;
	Color foreground = null;
	Color background = null;
	Image backgroundImage;
	boolean hasLocation;
	MenuItem cascade;
	Decorations parent;
	MenuItem selectedMenuItem;

	java.util.List<MenuItem> items = new ArrayList<>();

	private Point location;

	private MenuItem defaultItem;

	/* Timer ID for MenuItem ToolTip */
	static final int ID_TOOLTIP_TIMER = 110;


	/**
	 * Constructs a new instance of this class given its parent, and sets the style
	 * for the instance so that the instance will be a popup menu on the given
	 * parent's shell.
	 * <p>
	 * After constructing a menu, it can be set into its parent using
	 * <code>parent.setMenu(menu)</code>. In this case, the parent may be any
	 * control in the same widget tree as the parent.
	 * </p>
	 *
	 * @param parent a control which will be the parent of the new instance (cannot
	 *               be null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#POP_UP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(Control parent) {
		this(checkNull(parent).menuShell(), SWT.POP_UP);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Decorations</code>) and a style value describing its behavior and
	 * appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * <p>
	 * After constructing a menu or menuBar, it can be set into its parent using
	 * <code>parent.setMenu(menu)</code> or <code>parent.setMenuBar(menuBar)</code>.
	 * </p>
	 *
	 * @param parent a decorations control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of menu to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#BAR
	 * @see SWT#DROP_DOWN
	 * @see SWT#POP_UP
	 * @see SWT#NO_RADIO_GROUP
	 * @see SWT#LEFT_TO_RIGHT
	 * @see SWT#RIGHT_TO_LEFT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(Decorations parent, int style) {
		this(parent, checkStyle(style), 0);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>Menu</code>) and sets the style for the instance so that the instance
	 * will be a drop-down menu on the given parent's parent.
	 * <p>
	 * After constructing a drop-down menu, it can be set into its parentMenu using
	 * <code>parentMenu.setMenu(menu)</code>.
	 * </p>
	 *
	 * @param parentMenu a menu which will be the parent of the new instance (cannot
	 *                   be null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(Menu parentMenu) {
		this(checkNull(parentMenu).parent, SWT.DROP_DOWN);
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>MenuItem</code>) and sets the style for the instance so that the
	 * instance will be a drop-down menu on the given parent's parent menu.
	 * <p>
	 * After constructing a drop-down menu, it can be set into its parentItem using
	 * <code>parentItem.setMenu(menu)</code>.
	 * </p>
	 *
	 * @param parentItem a menu item which will be the parent of the new instance
	 *                   (cannot be null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#DROP_DOWN
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Menu(MenuItem parentItem) {
		this(checkNull(parentItem).parent);
	}

	Menu(Decorations parent, int style, long handle) {
		super(parent, checkStyle(style));
		this.parent = parent;
		this.handle = handle;
		createWidget();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * help events are generated for the control, by sending it one of the messages
	 * defined in the <code>HelpListener</code> interface.
	 *
	 * @param listener the listener which should be notified
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
	 * @see HelpListener
	 * @see #removeHelpListener
	 */
	public void addHelpListener(HelpListener listener) {
		addTypedListener(listener, SWT.Help);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * menus are hidden or shown, by sending it one of the messages defined in the
	 * <code>MenuListener</code> interface.
	 *
	 * @param listener the listener which should be notified
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
	 * @see MenuListener
	 * @see #removeMenuListener
	 */
	public void addMenuListener(MenuListener listener) {
		addTypedListener(listener, SWT.Hide, SWT.Show);
	}

	static Control checkNull(Control control) {
		if (control == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return control;
	}

	static Menu checkNull(Menu menu) {
		if (menu == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return menu;
	}

	static MenuItem checkNull(MenuItem item) {
		if (item == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return item;
	}

	static int checkStyle(int style) {
		return checkBits(style, SWT.POP_UP, SWT.BAR, SWT.DROP_DOWN, 0, 0, 0);
	}

	void createHandle() {
//	if (handle != 0)
//		return;
//	if ((style & SWT.BAR) != 0) {
//		handle = OS.CreateMenu();
//	} else {
//		handle = OS.CreatePopupMenu();
//	}
//	if (handle == 0)
//		error(SWT.ERROR_NO_HANDLES);
//	updateBackground();
	}

	void createItem(MenuItem item, int index) {
		int count = items.size();
		if (!(0 <= index && index <= count))
			error(SWT.ERROR_INVALID_RANGE);
		items.add(index, item);

		redraw();
	}

	void createWidget() {
		checkOrientation(parent);
		initThemeColors();
		createHandle();
		parent.addMenu(this);
	}


	void destroyItem(MenuItem item) {

		items.remove(item);

		redraw();
	}

	@Override
	void destroyWidget() {
		MenuItem cascade = this.cascade;
		releaseHandle();
		if (cascade != null) {
			cascade.setMenu(null, true);
		} else {
		}
	}

	void fixMenus(Decorations newParent) {
		if (isDisposed()) {
			return;
		}
		for (MenuItem item : getItems()) {
			item.fixMenus(newParent);
		}
		parent.removeMenu(this);
		newParent.addMenu(this);
		this.parent = newParent;
	}

	/**
	 * Returns the receiver's background color.
	 *
	 * @return the background color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.3
	 */
	/* public */ Color getBackground() {
		checkWidget();
		return background;
	}

	/**
	 * Returns the receiver's background image.
	 *
	 * @return the background image
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.3
	 */
	/* public */ Image getBackgroundImage() {
		checkWidget();
		return backgroundImage;
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative to
	 * its parent (or its display if its parent is null), unless the receiver is a
	 * menu or a shell. In this case, the location is relative to the display.
	 * <p>
	 * Note that the bounds of a menu or menu item are undefined when the menu is
	 * not visible. This is because most platforms compute the bounds of a menu
	 * dynamically just before it is displayed.
	 * </p>
	 *
	 * @return the receiver's bounding rectangle
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.1
	 */
	/* public */ Rectangle getBounds() {
		checkWidget();

		return new Rectangle(0, 0, 0, 0);
	}

	/**
	 * Returns the default menu item or null if none has been previously set.
	 *
	 * @return the default menu item.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public MenuItem getDefaultItem() {
		checkWidget();
		return this.defaultItem;
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled, and <code>false</code>
	 * otherwise. A disabled menu is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #isEnabled
	 */
	public boolean getEnabled() {
		checkWidget();
		return (state & DISABLED) == 0;
	}

	/**
	 * Returns the foreground color that the receiver will use to draw.
	 *
	 * @return the receiver's foreground color
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	/* public */ Color getForeground() {
		checkWidget();
		return foreground;
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver. Throws an
	 * exception if the index is out of range.
	 *
	 * @param index the index of the item to return
	 * @return the item at the given index
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_RANGE - if the index is
	 *                                     not between 0 and the number of elements
	 *                                     in the list minus 1 (inclusive)</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public MenuItem getItem(int index) {
		checkWidget();

		return items.get(index);

	}

	/**
	 * Returns the number of items contained in the receiver.
	 *
	 * @return the number of items
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getItemCount() {
		checkWidget();
		return items.size();
	}

	/**
	 * Returns a (possibly empty) array of <code>MenuItem</code>s which are the
	 * items in the receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain its
	 * list of items, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return the items in the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public MenuItem[] getItems() {
		checkWidget();
		return items.toArray(new MenuItem[0]);
	}

	@Override
	String getNameText() {
		String result = "";
		MenuItem[] items = getItems();
		int length = items.length;
		if (length > 0) {
			for (int i = 0; i <= length - 1; i++) {
				result += (items[i] == null ? "null" : items[i].getNameText()) + (i < (length - 1) ? ", " : "");
			}
		}
		return result;
	}

	/**
	 * Returns the orientation of the receiver, which will be one of the constants
	 * <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 *
	 * @return the orientation style
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.7
	 */
	public int getOrientation() {
		checkWidget();
		return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Decorations</code>.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Decorations getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns the receiver's parent item, which must be a <code>MenuItem</code> or
	 * null when the receiver is a root.
	 *
	 * @return the receiver's parent item
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public MenuItem getParentItem() {
		checkWidget();
		return cascade;
	}

	/**
	 * Returns the receiver's parent item, which must be a <code>Menu</code> or null
	 * when the receiver is a root.
	 *
	 * @return the receiver's parent item
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Menu getParentMenu() {
		checkWidget();
		if (cascade != null)
			return cascade.parent;
		return null;
	}

	/**
	 * Returns the receiver's shell. For all controls other than shells, this simply
	 * returns the control's nearest ancestor shell. Shells return themselves, even
	 * if they are children of other shells. Returns null if receiver or its
	 * ancestor is the application menubar.
	 *
	 * @return the receiver's shell or null
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getParent
	 */
	public Shell getShell() {
		checkWidget();
		return parent.getShell();
	}

	/**
	 * Returns <code>true</code> if the receiver is visible, and <code>false</code>
	 * otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean getVisible() {
		checkWidget();
		if ((style & SWT.BAR) != 0) {
			return this == parent.menuShell().menuBar;
		}
		if ((style & SWT.POP_UP) != 0) {
			Menu[] popups = display.popups;
			if (popups == null)
				return false;
			for (Menu popup : popups) {
				if (popup == this)
					return true;
			}
		}
		Shell shell = getShell();

		Menu menu = shell.getMenu();
		while (menu != null && menu != this) {
			menu = menu.getParentMenu();
		}
		return this == menu;
	}

	void hideCurrentToolTip() {
		if (this.selectedMenuItem != null) {
			selectedMenuItem.hideToolTip();
		}
	}

	/**
	 * Searches the receiver's list starting at the first item (index 0) until an
	 * item is found that is equal to the argument, and returns the index of that
	 * item. If no item is found, returns -1.
	 *
	 * @param item the search item
	 * @return the index of the item
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the item is
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
	 */
	public int indexOf(MenuItem item) {
		checkWidget();
		if (item == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (item.isDisposed())
			error(SWT.ERROR_INVALID_ARGUMENT);
		if (item.parent != this)
			return -1;

		return items.indexOf(item);

	}

	void initThemeColors() {
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all of the
	 * receiver's ancestors are enabled, and <code>false</code> otherwise. A
	 * disabled menu is typically not selectable from the user interface and draws
	 * with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getEnabled
	 */
	public boolean isEnabled() {
		checkWidget();
		Menu parentMenu = getParentMenu();
		if (parentMenu == null) {
			return getEnabled() && parent.isEnabled();
		}
		return getEnabled() && parentMenu.isEnabled();
	}

	/**
	 * Returns <code>true</code> if the receiver is visible and all of the
	 * receiver's ancestors are visible and <code>false</code> otherwise.
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getVisible
	 */
	public boolean isVisible() {
		checkWidget();
		return getVisible();
	}

	void redraw() {
		if (!isVisible())
			return;
			update();
	}

	@Override
	void releaseHandle() {
		super.releaseHandle();
		handle = 0;
		cascade = null;
	}

	@Override
	void releaseChildren(boolean destroy) {
		for (MenuItem item : getItems()) {
			if (item != null && !item.isDisposed()) {
				item.release(false);
			}
		}
		super.releaseChildren(destroy);
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		if ((style & SWT.BAR) != 0) {
			if (this == parent.menuBar) {
				parent.setMenuBar(null);
			}
		} else {
			if ((style & SWT.POP_UP) != 0) {
				display.removePopup(this);
			}
		}
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		backgroundImage = null;
		background = null;
		foreground = null;
		if (parent != null)
			parent.removeMenu(this);
		parent = null;
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the help events are generated for the control.
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
	 * @see HelpListener
	 * @see #addHelpListener
	 */
	public void removeHelpListener(HelpListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Help, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the menu events are generated for the control.
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
	 * @see MenuListener
	 * @see #addMenuListener
	 */
	public void removeMenuListener(MenuListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Hide, listener);
		eventTable.unhook(SWT.Show, listener);
	}

	@Override
	void reskinChildren(int flags) {
		for (MenuItem item : getItems()) {
			item.reskin(flags);
		}
		super.reskinChildren(flags);
	}

	/**
	 * Sets the receiver's background color to the color specified by the argument,
	 * or to the default system color for the control if the argument is null.
	 *
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
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
	 * @since 3.3
	 */
	/* public */ void setBackground(Color color) {
		checkWidget();
		this.background = color;
	}

	/**
	 * Sets the receiver's background image to the image specified by the argument,
	 * or to the default system color for the control if the argument is null. The
	 * background image is tiled to fill the available space.
	 *
	 * @param image the new image (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument is not a bitmap</li>
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
	 * @since 3.3
	 */
	/* public */ void setBackgroundImage(Image image) {
		checkWidget();
		if (image != null) {
			if (image.isDisposed())
				error(SWT.ERROR_INVALID_ARGUMENT);
			if (image.type != SWT.BITMAP)
				error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (backgroundImage == image)
			return;
		backgroundImage = image;
		updateBackground();
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the argument,
	 * or to the default system color for the control if the argument is null.
	 *
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
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
	 * @since 3.3
	 */
	/* public */ void setForeground(Color color) {
		checkWidget();
		this.foreground = color;
	}

	/**
	 * Sets the default menu item to the argument or removes the default emphasis
	 * when the argument is <code>null</code>.
	 *
	 * @param item the default menu item or null
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the menu
	 *                                     item has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setDefaultItem(MenuItem item) {
		checkWidget();

		if (this.defaultItem == item)
			return;

		this.defaultItem = item;
		redraw();
	}

	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables it
	 * otherwise. A disabled menu is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 *
	 * @param enabled the new enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setEnabled(boolean enabled) {
		checkWidget();
		state &= ~DISABLED;
		if (!enabled)
			state |= DISABLED;
	}

	/**
	 * Sets the location of the receiver, which must be a popup, to the point
	 * specified by the arguments which are relative to the display.
	 * <p>
	 * Note that this is different from most widgets where the location of the
	 * widget is relative to the parent.
	 * </p>
	 * <p>
	 * Also note that the actual location of the menu is dependent on platform
	 * specific behavior. For example: on Linux with Wayland this operation is a
	 * hint due to lack of global coordinates.
	 * </p>
	 *
	 * @param x the new x coordinate for the receiver
	 * @param y the new y coordinate for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setLocation(int x, int y) {
		setLocation(new Point(x, y));
	}

	void setLocationInPixels(int x, int y) {
		if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0)
			return;
		this.location = new Point(x, y);
		this.x = x;
		this.y = y;
		hasLocation = true;
	}

	/**
	 * Sets the location of the receiver, which must be a popup, to the point
	 * specified by the argument which is relative to the display.
	 * <p>
	 * Note that this is different from most widgets where the location of the
	 * widget is relative to the parent.
	 * </p>
	 * <p>
	 * Note that the platform window manager ultimately has control over the
	 * location of popup menus.
	 * </p>
	 *
	 * @param location the new location for the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the point is
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
	 * @since 2.1
	 */
	public void setLocation(Point location) {
		checkWidget();
		if (location == null)
			error(SWT.ERROR_NULL_ARGUMENT);

		this.location = location;
		setLocationInPixels(location.x, location.y);
	}

	/**
	 * Sets the orientation of the receiver, which must be one of the constants
	 * <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 *
	 * @param orientation new orientation style
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.7
	 */
	public void setOrientation(int orientation) {
		checkWidget();
		if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0)
			return;
		_setOrientation(orientation);
	}

	void _setOrientation(int orientation) {
		int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
		if ((orientation & flags) == 0 || (orientation & flags) == flags)
			return;
		style &= ~flags;
		style |= orientation & flags;
		style &= ~SWT.FLIP_TEXT_DIRECTION;
		for (MenuItem itm : getItems()) {
			itm.setOrientation(orientation);
		}
	}

	/**
	 * Marks the receiver as visible if the argument is <code>true</code>, and marks
	 * it invisible otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, marking it visible may not actually cause it
	 * to be displayed.
	 * </p>
	 *
	 * @param visible the new visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setVisible(boolean visible) {
		checkWidget();

		sendEvent(SWT.Show);

		var menuWindow = new MenuWindow(parent, getItems(), this.location);

//	menuWindow.setRelativePosition(this.location);

		if (this.location != null)
			menuWindow.setLocation(this.location);
		System.out.println("MenuWindowBounds: " + menuWindow.getBounds());


		menuWindow.open();

//	if ((style & (SWT.BAR | SWT.DROP_DOWN)) != 0) return;
//	if (visible) {
//		display.addPopup (this);
//	} else {
//		display.removePopup (this);
//		_setVisible (false);
//	}
	}

	void update() {
		boolean hasCheck = false, hasImage = false;
		for (MenuItem item : getItems()) {
			if (item.image != null) {
				if ((hasImage = true) && hasCheck)
					break;
			}
			if ((item.style & (SWT.CHECK | SWT.RADIO)) != 0) {
				if ((hasCheck = true) && hasImage)
					break;
			}
		}

	}

	void updateBackground() {

	}

	void updateForeground() {
		redraw();
	}

	private static void handleDPIChange(Widget widget, int newZoom, float scalingFactor) {
		if (!(widget instanceof Menu menu)) {
			return;
		}
	}
}
