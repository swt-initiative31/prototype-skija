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
 *     Conrad Groth - Bug 23837 [FEEP] Button, do not respect foreground and background color on Windows
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT, WRAP</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE may be
 * specified.
 * </p>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p>
 * <p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified when
 * the ARROW style is specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Button extends Control implements ICustomWidget {

	String message = "";
	boolean ignoreMouse, useDarkModeExplorerTheme;
	Image disabledImage;
	static final int ICON_WIDTH = 128, ICON_HEIGHT = 128;
	static /* final */ boolean COMMAND_LINK = false;
	static final char[] STRING_WITH_ZERO_CHAR = new char[]{'0'};
	private Point computedSize;

	private static Color background;

	private Accessible acc;
	private AccessibleAdapter accAdapter;
	private boolean spaceDown;
	private boolean defaultButton;

	private final LWAbstractButton lwControl;

	private boolean ignoreSelectionEvent;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 *
	 * @see SWT#ARROW
	 * @see SWT#CHECK
	 * @see SWT#PUSH
	 * @see SWT#RADIO
	 * @see SWT#TOGGLE
	 * @see SWT#FLAT
	 * @see SWT#UP
	 * @see SWT#DOWN
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Button(Composite parent, int style) {
		super(parent, checkStyle(style));

		Listener listener = event -> {
			switch (event.type) {
			case SWT.MouseEnter -> onMouseEnter();
			case SWT.MouseExit -> onMouseExit();
			case SWT.Dispose -> onDispose(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.Paint -> onPaint(event);
			case SWT.Resize -> onResize();
			case SWT.FocusIn -> onFocusIn();
			case SWT.FocusOut -> onFocusOut();
			case SWT.Traverse -> onTraverse(event);
			case SWT.Selection -> onSelection(event);
			case SWT.KeyDown -> onKeyDown(event);
			case SWT.KeyUp -> onKeyUp(event);
			}
		};
		addListener(SWT.Dispose, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.KeyUp, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.Selection, listener);
		addListener(SWT.MouseEnter, listener);
		addListener(SWT.MouseExit, listener);

		style = this.style;
		if ((style & SWT.RADIO) != 0) {
			lwControl = new LWRadioButton(style, new LWRadioButton.Group() {
				@Override
				public void add(LWRadioButton radioButton) {
				}

				@Override
				public List<LWRadioButton> getRadioButtons() {
					final List<LWRadioButton> radioButtons = new ArrayList<>();
					final Control[] children = parent.getChildren();
					for (Control child : children) {
						if (child instanceof Button button
						    && button.lwControl instanceof LWRadioButton radioButton) {
							radioButtons.add(radioButton);
						}
					}
					return radioButtons;
				}
			});
		} else if ((style & SWT.CHECK) != 0) {
			lwControl = new LWCheckbox(style);
		} else {
			lwControl = new LWButton(style);
		}

		lwControl.addListener(type -> {
			switch (type) {
			case LWAbstractButton.EVENT_SELECTED -> {
				if (!ignoreSelectionEvent) {
					sendSelectionEvent(SWT.Selection);
				}
			}
			default -> redraw();
			}
		});

		initializeAccessible();
	}

	/**
	 * TODO: improve this support and make it completely similar to native
	 * windows buttons.
	 * ---------------------------------------------------------------------------
	 * Add accessibility support for the widget.
	 */
	void initializeAccessible() {
		acc = getAccessible();

		Button current = this;

		accAdapter = new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = lwControl.getAccessibleText();
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}

			@Override
			public void getKeyboardShortcut(AccessibleEvent e) {
				String shortcut = null;
				String text = getText();
				if (text != null) {
					char mnemonic = _findMnemonic(text);
					if (mnemonic != '\0') {
						shortcut = "Alt+" + mnemonic; //$NON-NLS-1$
					}
				}
				e.result = shortcut;
			}
		};
		acc.addAccessibleListener(accAdapter);
		addListener(SWT.FocusIn, event -> acc.setFocus(ACC.CHILDID_SELF));
	}

	private boolean isPushButton() {
		return (style & SWT.PUSH) != 0;
	}

	/*
	 * Return the lowercase of the first non-'&' character following an '&'
	 * character in the given string. If there are no '&' characters in the
	 * given string, return '\0'.
	 */
	char _findMnemonic(String string) {
		if (string == null) {
			return '\0';
		}

		int index = 0;
		int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&') {
				index++;
			}
			if (++index >= length) {
				return '\0';
			}
			if (string.charAt(index) != '&') {
				return Character.toLowerCase(string.charAt(index));
			}
			index++;
		} while (index < length);
		return '\0';
	}

	@Override
	void sendSelectionEvent(int type) {
		super.sendSelectionEvent(type);
	}

	private void onSelection(Event event) {
		redraw();
	}

	private void onTraverse(Event event) {
	}

	private void onFocusIn() {
		redraw();
	}

	private void onFocusOut() {
		redraw();
	}

	private void onKeyDown(Event event) {
		if (event.character == SWT.SPACE) {
			this.spaceDown = true;
			redraw();
		}
	}

	private void onKeyUp(Event event) {
		if (event.character == SWT.SPACE) {
			this.spaceDown = false;
			lwControl.handleSelection();
			redraw();
		}
	}

	private void onResize() {
		final Point size = getSize();
		lwControl.setSize(size.x, size.y);
		redraw();
	}

	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}
		GC gc = event.gc;
		if (gc == null) {
			gc = new GC(this);
			event.gc = gc;
		}
		doPaint(event);
		gc.dispose();
	}

	private void onDispose(Event event) {
		dispose();
	}

	private void onMouseDown(Event e) {
		lwControl.handleEvent(e);
	}

	private void onMouseUp(Event e) {
		lwControl.handleEvent(e);
	}

	private void onMouseEnter() {
		lwControl.setHovered(true);
	}

	private void onMouseExit() {
		lwControl.setHovered(false);
	}

	private void doPaint(Event e) {
		Rectangle r = getBounds();
		if (r.width == 0 && r.height == 0) {
			return;
		}

		e.gc.setForeground(getForeground());
		e.gc.setBackground(getBackground());
		e.gc.setClipping(new Rectangle(0, 0, r.width, r.height));
		e.gc.setAntialias(SWT.ON);

		GC originalGC = e.gc;
		GC gc = originalGC;
		Image doubleBufferingImage = null;

		if (SWT.getPlatform().equals("win32") | SWT.getPlatform().equals("gtk")) {
			// Extract background color on first execution
			if (background == null) {
				extractAndStoreBackgroundColor(r, originalGC);
			}
			style |= SWT.NO_BACKGROUND;
		}

		if (SWT.USE_SKIJA) {
			gc = GCFactory.createGraphicsContext(e.gc);
		} else {
			if (SWT.getPlatform().equals("win32")) {
				// Use double buffering on windows
				doubleBufferingImage = new Image(getDisplay(), r.width, r.height);
				originalGC.copyArea(doubleBufferingImage, 0, 0);
				GC doubleBufferingGC = new GC(doubleBufferingImage);
				doubleBufferingGC.setForeground(originalGC.getForeground());
				doubleBufferingGC.setBackground(background);
				doubleBufferingGC.setAntialias(SWT.ON);
				doubleBufferingGC.fillRectangle(0, 0, r.width, r.height);
				gc = doubleBufferingGC;
			}
		}

		lwControl.paint(gc, getColorProvider());

		gc.commit();
		gc.dispose();
		if (doubleBufferingImage != null) {
			originalGC.drawImage(doubleBufferingImage, 0, 0);
			doubleBufferingImage.dispose();
		}
		originalGC.dispose();
	}

	private void extractAndStoreBackgroundColor(Rectangle r, GC originalGC) {
		Image backgroundColorImage = new Image(getDisplay(), r.width, r.height);
		originalGC.copyArea(backgroundColorImage, 0, 0);
		int pixel = backgroundColorImage.getImageData().getPixel(0, 0);
		backgroundColorImage.dispose();
		background = SWT.convertPixelToColor(pixel);
	}

	private boolean isCheckButton() {
		return (style & SWT.CHECK) != 0;
	}

	@Override
	public void setSize(int width, int height) {
		checkWidget();
		lwControl.setSize(width, height);
		super.setSize(width, height);
		redraw();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		lwControl.setSize(width, height);
		super.setBounds(x, y, width, height);
		redraw();
	}

	@Override
	public Point computeSize(int wHint, int hHint) {
		return computeSize(wHint, hHint, true);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		if (computedSize == null || changed) {
			final MeasureContext measureContext = new MeasureContext(this);
			computedSize = lwControl.getPreferredSize(measureContext);
			measureContext.dispose();
		}

		final Point size = new Point(computedSize.x, computedSize.y);
		if (wHint != SWT.DEFAULT) {
			size.x = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			size.y = wHint;
		}
		return size;
	}

	@Override
	public Point getSize() {
		return lwControl.getSize();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the control is selected by the
	 * user. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * <p>
	 * When the <code>SWT.RADIO</code> style bit is set, the
	 * <code>widgetSelected</code> method is also called when the receiver loses
	 * selection because another item in the same radio group was selected by
	 * the user. During <code>widgetSelected</code> the application can use
	 * <code>getSelection()</code> to determine the current selected state of
	 * the receiver.
	 * </p>
	 *
	 * @param listener
	 *            the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	static int checkStyle(int style) {
		style = checkBits(style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO,
				SWT.TOGGLE, COMMAND_LINK ? SWT.COMMAND : 0);
		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
			return checkBits(style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
		}
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			return checkBits(style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
		}
		if ((style & SWT.ARROW) != 0) {
			style |= SWT.NO_FOCUS;
			return checkBits(style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0,
					0);
		}
		return style;
	}

	void click() {
		lwControl.handleSelection();
	}

	/**
	 * Returns a value which describes the position of the text or image in the
	 * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code>
	 * or <code>CENTER</code> unless the receiver is an <code>ARROW</code>
	 * button, in which case, the alignment will indicate the direction of the
	 * arrow (one of <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or
	 * <code>DOWN</code>).
	 *
	 * @return the alignment
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getAlignment() {
		checkWidget();
		if ((style & SWT.ARROW) != 0) {
			if ((style & SWT.UP) != 0) {
				return SWT.UP;
			}
			if ((style & SWT.DOWN) != 0) {
				return SWT.DOWN;
			}
			if ((style & SWT.LEFT) != 0) {
				return SWT.LEFT;
			}
			if ((style & SWT.RIGHT) != 0) {
				return SWT.RIGHT;
			}
			return SWT.UP;
		}
		if ((style & SWT.LEFT) != 0) {
			return SWT.LEFT;
		}
		if ((style & SWT.CENTER) != 0) {
			return SWT.CENTER;
		}
		if ((style & SWT.RIGHT) != 0) {
			return SWT.RIGHT;
		}
		return SWT.LEFT;
	}

	boolean getDefault() {
		if (!isPushButton() || !isEnabled() || isDisposed()) {
			return false;
		}

		return defaultButton;
	}

	/**
	 * Returns <code>true</code> if the receiver is grayed, and false otherwise.
	 * When the widget does not have the <code>CHECK</code> style, return false.
	 *
	 * @return the grayed state of the checkbox
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.4
	 */
	public boolean getGrayed() {
		checkWidget();
		if (lwControl instanceof LWCheckbox checkbox) {
			return checkbox.isGrayed();
		}
		return false;
	}

	/**
	 * Returns the receiver's image if it has one, or null if it does not.
	 *
	 * @return the receiver's image
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getImage() {
		checkWidget();
		if (lwControl instanceof LWButton button) {
			return button.getImage();
		}
		return null;
	}

	/**
	 * Returns the widget message. When the widget is created with the style
	 * <code>SWT.COMMAND</code>, the message text is displayed to provide
	 * further information for the user.
	 *
	 * @return the widget message
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.3
	 */
	/* public */ String getMessage() {
		checkWidget();
		return message;
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns <code>true</code> if the receiver is selected, and false
	 * otherwise.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked. When it is of type <code>TOGGLE</code>,
	 * it is selected when it is pushed in. If the receiver is of any other
	 * type, this method returns false.
	 *
	 * @return the selection state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getSelection() {
		checkWidget();
		if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) {
			return false;
		}
		return lwControl.isSelected();
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has
	 * never been set or if the receiver is an <code>ARROW</code> button.
	 *
	 * @return the receiver's text
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getText() {
		checkWidget();
		if ((style & SWT.ARROW) != 0) {
			return "";
		}
		return lwControl.getText();
	}

	@Override
	boolean isTabGroup() {
		if (isPushButton() || isCheckButton()) {
			return true;
		}
		boolean b = super.isTabGroup();
		return b;
	}

//	@Override
//	boolean setTabItemFocus() {
//
//		if (isRadioButton()) {
//
//			for (Button b : getRadioGroup()) {
//				// we only tab on this element, if there is no other radio
//				// button which is selected.
//				// in case of another selected radio button, this other radio
//				// button should be tabbed.
//				// But if the other checked radio button has focus, then the
//				// tabbing should not be blocked.
//				if (!b.hasFocus() && b.isChecked() && b != this) {
//					return false;
//				}
//
//			}
//
//		}
//
//		boolean b = super.setTabItemFocus();
//		return b;
//	}
	//
	@Override
	boolean isTabItem() {
		boolean b = super.isTabItem();
		return b;
	}

	@Override
	boolean traverseItem(boolean next) {
		boolean b = super.traverseItem(next);
		// if the next item is selected, a radio button loses the check.
		if (b && lwControl instanceof LWRadioButton) {
			setSelection(false);
		}
		redraw();
		return b;
	}

	@Override
	boolean traverseGroup(boolean next) {
		boolean b = super.traverseGroup(next);
		return b;
	}

	// menmonicHis(char ch) does not exist on mac. It seems on mac there is no
	// mnemonic...
	boolean mnemonicISHit(char ch) {
		if ((style & SWT.RADIO) == 0 && !setFocus()) {
			return false;
		}
		click();
		return true;
	}

	// mnemonicMatch(char key) does not exist on mac...
	boolean mnemonicHasMatch(char key) {
		// char mnemonic = findMnemonic (getText ());
		// if (mnemonic == '\0') return false;
		// return Character.toUpperCase (key) == Character.toUpperCase
		// (mnemonic);
		System.out.println("WARN: Not implemented yet: "
				+ new Throwable().getStackTrace()[0]);
		return false;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		if (disabledImage != null) {
			disabledImage.dispose();
		}
		disabledImage = null;
		lwControl.setText(null);
		if (lwControl instanceof LWButton button) {
			button.setImage(null);
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is selected by the user.
	 *
	 * @param listener
	 *            the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Controls how text, images and arrows will be displayed in the receiver.
	 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code> unless the receiver is an <code>ARROW</code> button,
	 * in which case, the argument indicates the direction of the arrow (one of
	 * <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or
	 * <code>DOWN</code>).
	 *
	 * @param alignment
	 *            the new alignment
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setAlignment(int alignment) {
		checkWidget();

		lwControl.setAlignment(alignment);

		redraw();

	}

	/**
	 * Sets the button's background color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
	 * <p>
	 * Note: This is custom paint operation and only affects {@link SWT#PUSH}
	 * and {@link SWT#TOGGLE} buttons. If the native button has a 3D look an
	 * feel (e.g. Windows 7), this method will cause the button to look FLAT
	 * irrespective of the state of the {@link SWT#FLAT} style. For
	 * {@link SWT#CHECK} and {@link SWT#RADIO} buttons, this method delegates to
	 * {@link Control#setBackground(Color)}.
	 * </p>
	 *
	 * @param color
	 *            the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setBackground(Color color) {
		// This method only exists in order to provide custom documentation
		super.setBackground(color);
	}

	void setDefault(boolean value) {
		if ((style & SWT.PUSH) == 0) {
			return;
		}
		defaultButton = value;
	}

	@Override
	public boolean setFocus() {
		checkWidget();

		/*
		 * If the button should get focus, use forceFocus().
		 * Here it should be prevented, that focus changes the selection of radion buttons.
		 */
		/*
		 * Feature in Windows. When a radio button gets focus, it selects the
		 * button in WM_SETFOCUS. The fix is to not assign focus to an
		 * unselected radio button.
		 */
		if (lwControl instanceof LWRadioButton && !lwControl.isSelected()) {
			return false;
		}
		boolean b = super.setFocus();
		return b;
	}

	@Override
	public boolean forceFocus() {
		boolean b = super.forceFocus();
		if (b && lwControl instanceof LWRadioButton radioButton) { // if a radio button gets focus, then all
									// other radio buttons of the group lose the
									// selection
			radioButton.handleSelection();
		}
		return b;
	}

	/**
	 * Sets the receiver's image to the argument, which may be <code>null</code>
	 * indicating that no image should be displayed.
	 * <p>
	 * Note that a Button can display an image and text simultaneously.
	 * </p>
	 *
	 * @param image
	 *            the image to display on the receiver (may be
	 *            <code>null</code>)
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setImage(Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (lwControl instanceof LWButton button) {
			button.setImage(image);

			if (disabledImage != null) {
				disabledImage.dispose();
			}
			disabledImage = new Image(getDisplay(), image, SWT.IMAGE_GRAY);
			button.setDisabledImage(disabledImage);
			redraw();
		}
	}

	/**
	 * Sets the grayed state of the receiver. This state change only applies if
	 * the control was created with the SWT.CHECK style.
	 *
	 * @param grayed
	 *            the new grayed state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.4
	 */
	public void setGrayed(boolean grayed) {
		checkWidget();
		if (lwControl instanceof LWCheckbox checkbox) {
			checkbox.setGrayed(grayed);
		}
	}

	/**
	 * Sets the widget message. When the widget is created with the style
	 * <code>SWT.COMMAND</code>, the message text is displayed to provide
	 * further information for the user.
	 *
	 * @param message
	 *            the new message
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.3
	 */
	/* public */ void setMessage(String message) {
		// TODO not yet implemented, never heard of this...
		checkWidget();
		if (message == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.message = message;
		if ((style & SWT.COMMAND) != 0) {
			int length = message.length();
			char[] chars = new char[length + 1];
			message.getChars(0, length, chars, 0);
			// OS.SendMessage (handle, OS.BCM_SETNOTE, 0, chars);
			System.out.println("WARN: Not implemented yet: "
					+ new Throwable().getStackTrace()[0]);
		}
	}

	/**
	 * Sets the selection state of the receiver, if it is of type
	 * <code>CHECK</code>, <code>RADIO</code>, or <code>TOGGLE</code>.
	 *
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked. When it is of type <code>TOGGLE</code>,
	 * it is selected when it is pushed in.
	 *
	 * @param selected
	 *            the new selection state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(boolean selected) {
		checkWidget();
		ignoreSelectionEvent = true;
		try {
			lwControl.setSelected(selected);
		}
		finally {
			ignoreSelectionEvent = false;
		}
	}

	@Override
	boolean setRadioSelection(boolean value) {
		if (lwControl instanceof LWRadioButton radioButton) {
			radioButton.setSelected(value);
			return true;
		}
		return false;
	}

	/**
	 * Sets the receiver's text.
	 * <p>
	 * This method sets the button label. The label may include the mnemonic
	 * character but must not contain line delimiters.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, a selection event occurs. On most platforms, the mnemonic
	 * appears underlined but may be emphasized in a platform specific manner.
	 * The mnemonic indicator character '&amp;' can be escaped by doubling it in
	 * the string, causing a single '&amp;' to be displayed.
	 * </p>
	 * <p>
	 * Note that a Button can display an image and text simultaneously on
	 * Windows (starting with XP), GTK+ and OSX. On other platforms, a Button
	 * that has an image and text set into it will display the image or text
	 * that was set most recently.
	 * </p>
	 * <p>
	 * Also note, if control characters like '\n', '\t' etc. are used in the
	 * string, then the behavior is platform dependent.
	 * </p>
	 *
	 * @param string
	 *            the new text
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if ((style & SWT.ARROW) != 0) {
			return;
		}
		lwControl.setText(string);
		redraw();
	}

	static int getThemeStateId(int style, boolean pressed, boolean enabled) {
		int direction = style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);

		/*
		 * Feature in Windows. DrawThemeBackground() does not mirror the
		 * drawing. The fix is switch left to right and right to left.
		 */
		if ((style & SWT.MIRRORED) != 0) {
			if (direction == SWT.LEFT) {
				direction = SWT.RIGHT;
			} else if (direction == SWT.RIGHT) {
				direction = SWT.LEFT;
			}
		}

		/*
		 * On Win11, scrollbars no longer show arrows by default. Arrows only
		 * show up when hot/disabled/pushed. The workaround is to use hot image
		 * in place of default.
		 */
		// boolean hot = false;
		// if (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN11_21H2) {
		// if (!pressed && enabled) {
		// hot = true;
		// }
		// }
		//
		// if (hot) {
		// switch (direction) {
		// case SWT.UP: return OS.ABS_UPHOT;
		// case SWT.DOWN: return OS.ABS_DOWNHOT;
		// case SWT.LEFT: return OS.ABS_LEFTHOT;
		// case SWT.RIGHT: return OS.ABS_RIGHTHOT;
		// }
		// }
		//
		// if (pressed) {
		// switch (direction) {
		// case SWT.UP: return OS.ABS_UPPRESSED;
		// case SWT.DOWN: return OS.ABS_DOWNPRESSED;
		// case SWT.LEFT: return OS.ABS_LEFTPRESSED;
		// case SWT.RIGHT: return OS.ABS_RIGHTPRESSED;
		// }
		// }
		//
		// if (!enabled) {
		// switch (direction) {
		// case SWT.UP: return OS.ABS_UPDISABLED;
		// case SWT.DOWN: return OS.ABS_DOWNDISABLED;
		// case SWT.LEFT: return OS.ABS_LEFTDISABLED;
		// case SWT.RIGHT: return OS.ABS_RIGHTDISABLED;
		// }
		// }
		//
		// switch (direction) {
		// case SWT.UP: return OS.ABS_UPNORMAL;
		// case SWT.DOWN: return OS.ABS_DOWNNORMAL;
		// case SWT.LEFT: return OS.ABS_LEFTNORMAL;
		// case SWT.RIGHT: return OS.ABS_RIGHTNORMAL;
		// }
		//
		// // Have some sane value if all else fails
		// return OS.ABS_LEFTNORMAL;
		System.out.println("WARN: Not implemented yet: "
				+ new Throwable().getStackTrace()[0]);
		return -1;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		lwControl.setEnabled(enabled);
		redraw();
	}

	// TODO can this be used for a better arrow image?
	static int[] bezier(int x0, int y0, int x1, int y1, int x2, int y2, int x3,
			int y3, int count) {
		// The parametric equations for a Bezier curve for x[t] and y[t] where 0
		// <= t <=1 are:
		// x[t] = x0+3(x1-x0)t+3(x0+x2-2x1)t^2+(x3-x0+3x1-3x2)t^3
		// y[t] = y0+3(y1-y0)t+3(y0+y2-2y1)t^2+(y3-y0+3y1-3y2)t^3
		double a0 = x0;
		double a1 = 3 * (x1 - x0);
		double a2 = 3 * (x0 + x2 - 2 * x1);
		double a3 = x3 - x0 + 3 * x1 - 3 * x2;
		double b0 = y0;
		double b1 = 3 * (y1 - y0);
		double b2 = 3 * (y0 + y2 - 2 * y1);
		double b3 = y3 - y0 + 3 * y1 - 3 * y2;

		int[] polygon = new int[2 * count + 2];
		for (int i = 0; i <= count; i++) {
			double t = (double) i / (double) count;
			polygon[2 * i] = (int) (a0 + a1 * t + a2 * t * t + a3 * t * t * t);
			polygon[2 * i
					+ 1] = (int) (b0 + b1 * t + b2 * t * t + b3 * t * t * t);
		}
		return polygon;
	}
}
