/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
 *     Latha Patil (ETAS GmbH) - Custom draw Group widget using SkijaGC
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Instances of this class provide an etched border
 * with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a group with the
 * default shadow style for the platform, do not
 * specify a shadow style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Group extends Composite {
	private String text = "";
	private static final int CLIENT_INSET = 3;
	private Listener listener;
	private int orientation = SWT.LEFT_TO_RIGHT;
	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;
	private Point textExtent = new Point(0, 0);

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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#SHADOW_ETCHED_IN
 * @see SWT#SHADOW_ETCHED_OUT
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Group (Composite parent, int style) {
	super (parent, checkStyle (style));
	listener = event -> {
		switch (event.type) {
		case SWT.Paint -> onPaint(event);
		case SWT.Resize -> redraw();
		}
	};
	addListener(SWT.Paint, listener);
	addListener(SWT.Resize, listener);
	orientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

private void onPaint(Event event) {
	Drawing.drawWithGC(this, event.gc, this::drawGroup);
}

private void drawGroup(GC gc) {

	int width = getBounds().width;
	int height = getBounds().height;
	int titleWidth = textExtent.x;
	int titleHeight = textExtent.y;

	int inset = 2;
	int groupInset = 8;
	int borderRadius = 6;
	int textX = inset + groupInset;
	int textY = titleHeight;
	int newWidth = width - (inset * 2);
	int newHeight = height - (inset * 5);

	// Set shadow color
	gc.setForeground(getShadowColor());

	// Draw group border
	gc.drawRoundRectangle(inset, textY / 2, newWidth, newHeight, borderRadius, borderRadius);

	// Draw text background (A fillRectangle to erase the part of the group border
	// where text is drawn) and text
	int textPosX = (orientation == SWT.RIGHT_TO_LEFT) ? newWidth - groupInset - titleWidth : textX;
	gc.fillRectangle(textPosX, textY / 2, titleWidth, gc.getLineWidth() + 1);
	gc.setForeground(getForeground());
	gc.drawText(text, textPosX, 0, DRAW_FLAGS);
}

private Color getShadowColor() {
	if ((style & SWT.SHADOW_ETCHED_IN) != 0) {
		return new Color(136, 136, 136);
	}
	if ((style & SWT.SHADOW_ETCHED_OUT) != 0) {
		return new Color(68, 68, 68);
	}
	if ((style & SWT.SHADOW_IN) != 0) {
		return new Color(102, 102, 102);
	}
	if ((style & SWT.SHADOW_OUT) != 0) {
		return new Color(187, 187, 187);
	}
	return getForeground();
}


static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	if ((style & SWT.LEFT_TO_RIGHT) != 0) {
		style &= ~SWT.RIGHT_TO_LEFT;
	} else if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		style &= ~SWT.LEFT_TO_RIGHT;
	} else {
		style |= SWT.LEFT_TO_RIGHT;
		style &= ~SWT.RIGHT_TO_LEFT;
	}
	if ((style & SWT.BORDER) != 0) {
		style |= SWT.SHADOW_IN;
	}
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	Point size = DPIUtil.autoScaleUp(super.computeSize(wHint, hHint, changed));

	if (!changed && new Point(wHint, hHint).equals(size)) {
		return size;
	}
	if (text == null || text.isEmpty()) {
		return size;
	}
	Layout layout = getLayout();
	Point layoutSize = (layout != null) ? layout.computeSize(this, wHint, hHint, changed)
			: computeChildrenSize(changed);

	int width = Math.max(10, Math.max(layoutSize.x + CLIENT_INSET * 2, textExtent.x + CLIENT_INSET * 6));
	int height = Math.max(10, Math.max(layoutSize.y + CLIENT_INSET * 4, textExtent.y + CLIENT_INSET * 4));

	size = DPIUtil.autoScaleUp(new Point(wHint != SWT.DEFAULT ? wHint : width, hHint != SWT.DEFAULT ? hHint : height));
	return size;
}

private Point computeChildrenSize(boolean changed) {
	Point size = new Point(0, 0);
	for (Control child : getChildren()) {
		Point childSize = child.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		size.x = Math.max(size.x, childSize.x);
		size.y += childSize.y; // Stack vertically
	}
	return size;
}

@Override
public Rectangle computeTrim(int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrim(x, y, width, height);
	trim.width = width + CLIENT_INSET * 2;
	trim.height = height + CLIENT_INSET * 2;

	trim.height += textExtent.y + CLIENT_INSET * 2;
	return DPIUtil.autoScaleUp(trim);
}


@Override
public void setEnabled(boolean enabled) {
	super.setEnabled(enabled);
	redraw();
}

@Override
void enableWidget(boolean enabled) {
	super.enableWidget(enabled);
}

@Override
public boolean isEnabled() {
	checkWidget();
	return getEnabled() && parent.isEnabled();
}

@Override
public Rectangle getClientArea() {
	checkWidget();
	Rectangle rect = super.getClientArea();
	if (rect.isEmpty())
		return rect;

	int y = 5;
	if (text != null && !text.isEmpty()) {
		y = textExtent.y - 5;
	}
	return new Rectangle(rect.x + CLIENT_INSET, y, Math.max(0, rect.width - CLIENT_INSET * 2),
			Math.max(0, rect.height - CLIENT_INSET * 6));

}

@Override
String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which is the string that the
 * is used as the <em>title</em>. If the text has not previously
 * been set, returns an empty string.
 *
 * @return the text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	return text;
}

@Override
boolean mnemonicHit(char key) {
	return setFocus();
}

@Override
boolean mnemonicMatch(char key) {
	char mnemonic = findMnemonic(getText());
	if (mnemonic == '\0')
		return false;
	return Character.toUpperCase(key) == Character.toUpperCase(mnemonic);
}

@Override
public void setFont(Font font) {
	checkWidget();
	Rectangle oldRect = getClientAreaInPixels();
	super.setFont(font);
	Rectangle newRect = getClientAreaInPixels();
	if (!oldRect.equals(newRect)) {
		sendResize();
	}
}

/**
 * Sets the receiver's text, which is the string that will be displayed as the
 * receiver's <em>title</em>, to the argument, which may not be null. The string
 * may include the mnemonic character.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next character to be
 * the mnemonic. When the user presses a key sequence that matches the mnemonic,
 * focus is assigned to the first child of the group. On most platforms, the
 * mnemonic appears underlined but may be emphasised in a platform specific
 * manner. The mnemonic indicator character '&amp;' can be escaped by doubling
 * it in the string, causing a single '&amp;' to be displayed.
 * </p>
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used in the string, then
 * the behavior is platform dependent.
 * </p>
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException
 *                                     <ul>
 *                                     <li>ERROR_NULL_ARGUMENT - if the text is
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
public void setText(String text) {
	checkWidget();
	if (text == null) {
		text = ""; //$NON-NLS-1$
	}
	if (!text.equals(this.text)) {
		this.text = text;
		redraw();
		textExtent = Drawing.executeOnGC(this, gc -> {
			gc.setFont(getFont());
			return gc.textExtent(this.text, DRAW_FLAGS);
		});
	}
}

@Override
void printWidget(long hwnd, long hdc, GC gc) {
	System.err.println("printWidget of Group is not implemented yet.");
}
}
