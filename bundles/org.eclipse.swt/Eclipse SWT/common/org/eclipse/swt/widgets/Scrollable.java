/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
 *     Latha Patil (ETAS GmbH) - OS agnostic Scrollable implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * This class is the abstract superclass of all classes which represent controls
 * that have standard scroll bars.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>H_SCROLL, V_SCROLL</dd>
 * <dt><b>Events:</b>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Scrollable extends Control {
	ScrollBar horizontalBar, verticalBar;
	protected int scrollX = 0, scrollY = 0;
	private boolean isDragging = false;
	private int lastMouseX, lastMouseY;
	private final int scrollStep = 20;
	protected Control content;
	private Point minSize;
	private boolean expandHorizontal = false;
	private boolean expandVertical = false;

	/**
	 * The regular expression used to determine the string which should be deleted
	 * when Ctrl+Bs is hit.
	 */
	static final java.util.regex.Pattern CTRL_BS_PATTERN = java.util.regex.Pattern
			.compile("\\r?\\n\\z|[\\p{Punct}]+[\\t ]*\\z|[^\\p{Punct}\\s\\n\\r]*[\\t ]*\\z");

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Scrollable() {
	}

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
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
	 * @see SWT#H_SCROLL
	 * @see SWT#V_SCROLL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Scrollable(Composite parent, int style) {
		super(parent, style);

		Listener listener = event -> {
			switch (event.type) {
			case SWT.MouseHorizontalWheel -> onMouseHorizontalWheel(event);
			case SWT.MouseVerticalWheel -> onMouseVerticalWheel(event);
			case SWT.H_SCROLL -> onMouseHorizontalScroll(event);
			case SWT.V_SCROLL -> onMouseVerticalScroll(event);
			case SWT.Resize -> onResize(event);// redraw();
			case SWT.Paint -> onPaint(event);
			}
		};
		addListener(SWT.MouseHorizontalWheel, listener);
		addListener(SWT.MouseVerticalWheel, listener);
		addListener(SWT.H_SCROLL, listener);
		addListener(SWT.V_SCROLL, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		Display.getDefault().addFilter(SWT.MouseVerticalWheel, event -> {
			if (isDisposed() || !isVisible())
				return;

			Control target = (Control) event.widget;
			if (target != null && isAncestor(target)) {
				onMouseVerticalWheel(event);
				System.out.println("Mouse vertical wheel event handled in Scrollable");
				event.doit = false; // Prevent default handling
			}
		});

		Display.getDefault().addFilter(SWT.MouseHorizontalWheel, event -> {
			if (isDisposed() || !isVisible())
				return;

			Control target = (Control) event.widget;
			if (target != null && isAncestor(target)) {
				onMouseHorizontalWheel(event);
				event.doit = false;
			}
		});
	}

	private boolean isAncestor(Control control) {
		while (control != null) {
			if (control == this)
				return true;
			control = control.getParent();
		}
		return false;
	}

	private void onResize(Event event) {
		getDisplay().asyncExec(() -> {
			if (!isDisposed()) {
				layoutScrollbars();
				layoutContent();
				redraw();
				update();
			}
		});

	}

	public void setMinSize(int width, int height) {
		this.minSize = new Point(width, height);
		if (content != null && !minSize.equals(content.getSize())) {
			content.setSize(minSize);
			System.out.println("setMinSixe: content size set");// force size so getBounds() works
		}
		// getDisplay().asyncExec(() -> {
			if (!isDisposed()) {
				layoutScrollbars();
				layoutContent();
				redraw();
				update();
			}
			// });

	}

	public void setMinSize(Point size) {
		this.minSize = size;
		if (content != null && !minSize.equals(content.getSize())) {
			content.setSize(minSize); // force size so getBounds() works
			System.out.println("setMinSixeP: content size set");
		}
		getDisplay().asyncExec(() -> {
			if (!isDisposed()) {
				layoutScrollbars();
				layoutContent();
				redraw();
				update();
			}
		});

	}

	public void setExpandHorizontal(boolean expand) {
		this.expandHorizontal = expand;
	}

	public void setExpandVertical(boolean expand) {
		this.expandVertical = expand;
	}

	private void layoutScrollbars() {
		Rectangle clientArea = getClientArea();
		if (clientArea.width == 0 || clientArea.height == 0)
			return;

		Rectangle contentBounds;
		System.out.println("Layout scrollbars called");
		if (content instanceof Composite composite) {
			Point computed = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			Point current = content.getSize();
			if (!computed.equals(current)) {
				content.setSize(computed);
			}
			contentBounds = new Rectangle(0, 0, computed.x, computed.y);
		} else {
			contentBounds = content.getBounds();
		}

		if ((contentBounds.width == 0 || contentBounds.height == 0) && minSize != null) {
			contentBounds = new Rectangle(0, 0, minSize.x, minSize.y);
		}

		int barSize = 11;
		boolean needHBar = contentBounds.width > clientArea.width;
		boolean needVBar = contentBounds.height > clientArea.height;

		if (needHBar && !needVBar && verticalBar != null && contentBounds.height > clientArea.height - barSize) {
			needVBar = true;
		}

		if (needVBar && !needHBar && horizontalBar != null && contentBounds.width > clientArea.width - barSize) {
			needHBar = true;
		}

		if (horizontalBar != null) {
			horizontalBar.setVisible(needHBar);
			if (needHBar) {
				horizontalBar.setCustomBounds(clientArea.x, clientArea.y + clientArea.height - barSize,
						clientArea.width - (needVBar ? barSize : 0), barSize);
				horizontalBar.setMaximum(contentBounds.width);
				horizontalBar.setThumb(Math.min(clientArea.width, contentBounds.width));
				horizontalBar.redraw();
			}
		}

		if (verticalBar != null) {
			verticalBar.setVisible(needVBar);
			if (needVBar) {
				verticalBar.setCustomBounds(clientArea.x + clientArea.width - barSize, clientArea.y, barSize,
						clientArea.height - (needHBar ? barSize : 0));
				verticalBar.setMaximum(contentBounds.height);
				verticalBar.setThumb(Math.max(1, Math.min(clientArea.height, contentBounds.height)));
				verticalBar.redraw();
			}
		}
	}




	void layoutContent() {
		if (content != null) {
			Rectangle area = getClientArea();
			if (minSize != null) {
			int width = Math.max(minSize.x, expandHorizontal ? area.width : minSize.x);
			int height = Math.max(minSize.y, expandVertical ? area.height : minSize.y);
			Point current = content.getSize();
			if (current.x != width || current.y != height) {
				content.setSize(width, height);
				System.out.println("layoutContent: content size set");
			}
		}
			content.setLocation(-scrollX, -scrollY);
		}
	}
	private void onPaint(Event event) {
		System.err.println("Scrollable on paint called");
		if (horizontalBar != null) {
			((ScrollBar) horizontalBar).draw(event.gc); // or onPaint(event)
		}
		if (verticalBar != null) {
			((ScrollBar) verticalBar).draw(event.gc);
		}
	}

	private void onMouseHorizontalScroll(Event event) {

	}

	private void onMouseVerticalScroll(Event event) {
		scrollY += (event.count > 0) ? -20 : 20;
		scrollY = Math.max(0, Math.min(scrollY, verticalBar.getMaximum() - verticalBar.getThumb()));
		((ScrollBar) verticalBar).setSelection(scrollY);
		redraw();
	}

	private void onMouseVerticalWheel(Event e) {
		if ((e.stateMask & SWT.CTRL) != 0) {
			return;
		}
		boolean shift = (e.stateMask & SWT.SHIFT) != 0;
		boolean scrollVertically = !(shift && horizontalBar != null);

		if (scrollVertically && verticalBar != null) {
			int delta = e.count > 0 ? -scrollStep : scrollStep;

			int contentHeight = content.getSize().y;
			int clientHeight = getClientArea().height;
			int maxScrollY = Math.max(0, contentHeight - clientHeight);
			int newScroll = Math.max(0, Math.min(scrollY + delta, maxScrollY));

			scrollY = newScroll;
			System.out.printf("scrollY=%d, maxScrollY=%d%n", newScroll, maxScrollY);

			((ScrollBar) verticalBar).setSelection(scrollY);
			// redraw();

			Event selEvent = new Event();
			selEvent.widget = verticalBar;
			selEvent.type = SWT.Selection;
			selEvent.detail = (delta < 0) ? SWT.PAGE_UP : SWT.PAGE_DOWN;
			verticalBar.notifyListeners(SWT.Selection, selEvent);
		} else if (!scrollVertically && horizontalBar != null) {
			onMouseHorizontalWheel(e);
		}
	}

	private void onMouseHorizontalWheel(Event e) {
		if (horizontalBar != null) {
			int delta = e.count > 0 ? -scrollStep : scrollStep;
			int newScroll = Math.max(0,
					Math.min(scrollX + delta, horizontalBar.getMaximum() - horizontalBar.getThumb()));
			scrollX = newScroll;
			((ScrollBar) horizontalBar).setSelection(scrollX);
			redraw();

			Event selEvent = new Event();
			selEvent.widget = horizontalBar;
			selEvent.type = SWT.Selection;
			selEvent.detail = (delta < 0) ? SWT.PAGE_UP : SWT.PAGE_DOWN;
			horizontalBar.notifyListeners(SWT.Selection, selEvent);
		}
	}
	/**
	 * Given a desired <em>client area</em> for the receiver (as described by the
	 * arguments), returns the bounding rectangle which would be required to produce
	 * that client area.
	 * <p>
	 * In other words, it returns a rectangle such that, if the receiver's bounds
	 * were set to that rectangle, the area of the receiver which is capable of
	 * displaying data (that is, not covered by the "trimmings") would be the
	 * rectangle described by the arguments (relative to the receiver's parent).
	 * </p>
	 *
	 * @param x      the desired x coordinate of the client area
	 * @param y      the desired y coordinate of the client area
	 * @param width  the desired width of the client area
	 * @param height the desired height of the client area
	 * @return the required bounds to produce the given client area
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getClientArea
	 */
	public Rectangle computeTrim(int x, int y, int width, int height) {
		checkWidget();
		Rectangle rectangle = DPIUtil.autoScaleUp(new Rectangle(x, y, width, height));
		return DPIUtil.autoScaleDown(computeTrimInPixels(rectangle.x, rectangle.y, rectangle.width, rectangle.height));
	}

	Rectangle computeTrimInPixels(int x, int y, int width, int height) {
		System.out.println("computeTrimInPixels : x " + x + " y " + y + " width " + width + " height " + height);
		if (horizontalBar != null && horizontalBar.getVisible()) {
			int hScrollHeight = horizontalBar.getSize().y;
			System.out.println("Scrollbar height : " + hScrollHeight);
			height += hScrollHeight;
		}
		if (verticalBar != null && verticalBar.getVisible()) {
			int vScrollWidth = verticalBar.getSize().x;
			System.out.println("Scrollbar width : " + vScrollWidth);
			width += vScrollWidth;
		}
		int borderWidth = getBorderWidth();// 2
		System.out.println("Border width : " + borderWidth);
		x -= borderWidth;
		y -= borderWidth;
		width += 2 * borderWidth;
		height += 2 * borderWidth;
		Rectangle trimRectangle = new Rectangle(x, y, width, height);
		System.out.println("computeTrimInPixels return : " + trimRectangle);
		return trimRectangle;
	}

	ScrollBar createScrollBar(int type) {
		ScrollBar bar = new ScrollBar(this, type);
		if ((state & CANVAS) != 0) {
			bar.setMaximum(100);
			bar.setThumb(10);
		}
		return bar;
	}

	@Override
	void createWidget() {
		super.createWidget();
		if ((style & SWT.H_SCROLL) != 0) {
			horizontalBar = createScrollBar(SWT.H_SCROLL);
			horizontalBar.addListener(SWT.Selection, e -> {
				scrollX = ((ScrollBar) horizontalBar).getSelection();
				layoutContent();
				redraw();
			});
		}
		if ((style & SWT.V_SCROLL) != 0) {
			verticalBar = createScrollBar(SWT.V_SCROLL);
			verticalBar.addListener(SWT.Selection, e -> {
				scrollY = ((ScrollBar) verticalBar).getSelection();
				layoutContent();
				redraw();
			});
		}
	}

	@Override
	void updateBackgroundColor() {
		switch (applyThemeBackground()) {
		case 0:
			state &= ~THEME_BACKGROUND;
			break;
		case 1:
			state |= THEME_BACKGROUND;
			break;
		default: /* No change */
		}
		super.updateBackgroundColor();
	}

	/**
	 * @return
	 *         <li>0 to remove THEME_BACKGROUND</li>
	 *         <li>1 to apply THEME_BACKGROUND</li>
	 *         <li>otherwise don't change THEME_BACKGROUND state</li>
	 */
	int applyThemeBackground() {
		return (backgroundAlpha == 0) ? 1 : 0;
	}
	/**
	 * Returns a rectangle which describes the area of the receiver which is capable
	 * of displaying data (that is, not covered by the "trimmings").
	 *
	 * @return the client area
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #computeTrim
	 */
	public Rectangle getClientArea() {
		checkWidget();
		return DPIUtil.scaleDown(getClientAreaInPixels(), getZoom());
	}

	Rectangle getClientAreaInPixels() {
		forceResize();
		Rectangle widgetBounds = getBounds();
		System.out.println("Scrollable bounds:" + widgetBounds);
		int scrollbarWidth = 0;
		int scrollbarHeight = 0;
		if (horizontalBar != null && horizontalBar.getVisible()) {
			scrollbarHeight = horizontalBar.getSize().y;
		}
		if (verticalBar != null && verticalBar.getVisible()) {
			scrollbarWidth = verticalBar.getSize().x;
		}
		scrollbarWidth = scrollbarWidth == 0 ? 11 : scrollbarWidth;
		scrollbarHeight = scrollbarHeight == 0 ? 25 : scrollbarHeight;

		int clientWidth = Math.max(0, widgetBounds.width - scrollbarWidth);
		int clientHeight = Math.max(0, widgetBounds.height - scrollbarHeight);

		System.out.println("getClientAreaInPixels scrollabr: Width " + scrollbarWidth + " height " + scrollbarHeight);
		Rectangle clientArea = new Rectangle(0, 0, clientWidth, clientHeight);
		System.out.println("getClientAreaInPixels return : " + clientArea);
		return clientArea;
	}

	/**
	 * Returns the receiver's horizontal scroll bar if it has one, and null if it
	 * does not.
	 *
	 * @return the horizontal scroll bar (or null)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public ScrollBar getHorizontalBar() {
		return horizontalBar;
	}

	/**
	 * Returns the mode of the receiver's scrollbars. This will be <em>bitwise</em>
	 * OR of one or more of the constants defined in class <code>SWT</code>.<br>
	 * <ul>
	 * <li><code>SWT.SCROLLBAR_OVERLAY</code> - if receiver uses overlay
	 * scrollbars</li>
	 * <li><code>SWT.NONE</code> - otherwise</li>
	 * </ul>
	 *
	 * @return the mode of scrollbars
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see SWT#SCROLLBAR_OVERLAY
	 *
	 * @since 3.8
	 */
	public int getScrollbarsMode() {
		return SWT.NONE;
	}

	/**
	 * Returns the receiver's vertical scroll bar if it has one, and null if it does
	 * not.
	 *
	 * @return the vertical scroll bar (or null)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public ScrollBar getVerticalBar() {
		return verticalBar;
	}

	@Override
	void releaseChildren(boolean destroy) {
		if (horizontalBar != null) {
			horizontalBar.release(false);
			horizontalBar = null;
		}
		if (verticalBar != null) {
			verticalBar.release(false);
			verticalBar = null;
		}
		super.releaseChildren(destroy);
	}

	@Override
	void reskinChildren(int flags) {
		if (horizontalBar != null)
			horizontalBar.reskin(flags);
		if (verticalBar != null)
			verticalBar.reskin(flags);
		super.reskinChildren(flags);
	}

	long scrolledHandle() {
		return handle;
	}

	@Override
	int widgetStyle() {
		int bits = super.widgetStyle() | OS.WS_TABSTOP;
		if ((style & SWT.H_SCROLL) != 0)
			bits |= OS.WS_HSCROLL;
		if ((style & SWT.V_SCROLL) != 0)
			bits |= OS.WS_VSCROLL;
		return bits;
	}

	@Override
	TCHAR windowClass() {
		return display.windowClass;
	}

	@Override
	long windowProc() {
		return display.windowProc;
	}

	/**
	 * Set the content that will be scrolled.
	 *
	 * @param content the control to be displayed in the content area
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setContentControl(Control content) {
		this.content = content;
		if (content instanceof Composite composite) {
			System.out.println("layout set to true for composite");
			composite.layout(true);
		}
		getDisplay().asyncExec(() -> {
			if (!isDisposed()) {
				layoutScrollbars();
				layoutContent();
				redraw();
			}
		});

	}

	void destroyScrollBar(int type) {
	}

	LRESULT wmScroll(ScrollBar horizontalBar, boolean b, long hwndParent, int wmHscroll, long wParam, long lParam) {
		return LRESULT.ZERO;
	}

	LRESULT wmScrollWheel(boolean b, long wParam, long lParam, boolean b1) {
		return LRESULT.ZERO;
	}
}
