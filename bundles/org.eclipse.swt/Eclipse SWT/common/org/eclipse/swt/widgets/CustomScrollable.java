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
public class CustomScrollable {
	ScrollBar horizontalBar, verticalBar;
	protected int scrollX = 0, scrollY = 0;
	private int lastMouseX, lastMouseY;
	private final int scrollStep = 20;
	private Point minSize;
	private boolean expandHorizontal = false;
	private boolean expandVertical = false;
	private int style;
	private Scrollable scrollable;
	private Composite parent;
	private Control content;

	/**
	 * The regular expression used to determine the string which should be deleted
	 * when Ctrl+Bs is hit.
	 */
	static final java.util.regex.Pattern CTRL_BS_PATTERN = java.util.regex.Pattern
			.compile("\\r?\\n\\z|[\\p{Punct}]+[\\t ]*\\z|[^\\p{Punct}\\s\\n\\r]*[\\t ]*\\z");

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
	 * @param parent     a composite control which will be the parent of the new
	 *                   instance (cannot be null)
	 * @param style      the style of control to construct
	 * @param scrollable
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

	public CustomScrollable(Composite parent, int style, Scrollable scrollable) {
		this.scrollable = scrollable;
		this.style = style;
		this.parent = parent;

		Listener listener = event -> {
			switch (event.type) {
			case SWT.MouseHorizontalWheel -> onMouseHorizontalWheel(event);
			case SWT.MouseVerticalWheel -> onMouseVerticalWheel(event);
			case SWT.H_SCROLL -> onMouseHorizontalScroll(event);
			case SWT.V_SCROLL -> onMouseVerticalScroll(event);
			case SWT.Resize -> onResize(event);
			case SWT.Paint -> onPaint(event);
			case SWT.MouseDown -> onMouseDown(event);
			}
		};

		addListener(SWT.MouseHorizontalWheel, listener);
		addListener(SWT.MouseVerticalWheel, listener);
		addListener(SWT.H_SCROLL, listener);
		addListener(SWT.V_SCROLL, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.MouseDown, listener);

		// Issue : Mouse wheel events are not received by the scrollable . received only
		// on Label after this fix
		Display.getDefault().addFilter(SWT.MouseVerticalWheel, event -> {
			if (event.widget instanceof Control control && isAncestor(control)) {
				onMouseVerticalWheel(event);
				event.doit = false;
			}
		});

		Display.getDefault().addFilter(SWT.MouseHorizontalWheel, event -> {
			if (event.widget instanceof Control control && isAncestor(control)) {
				onMouseHorizontalWheel(event);
				event.doit = false;
			}
		});
		parent.addListener(SWT.Show, e -> {
			layoutContent();
			layoutScrollbars();
		});

	}

//Test method
	private void onMouseDown(Event event) {
		System.out.println("mouse down event on scrollable");
		if (event.button == 1) {
			System.out.println("Mouse down at: " + lastMouseX + ", " + lastMouseY);
		}
	}

	private void addListener(int type, Listener listener) {
		parent.addListener(type, listener);
	}

	private boolean isAncestor(Control control) {
		while (control != null) {
			if (control == scrollable)
				return true;
			control = control.getParent();
		}
		return false;
	}

	private boolean layoutScheduled = false;

	private void onResize(Event event) {
		if (layoutScheduled)
			return;

		layoutScheduled = true;
		System.out.println("Scrollable resize event — scheduling layout");

		scrollable.getDisplay().asyncExec(() -> {
			layoutScheduled = false;

			if (!scrollable.isDisposed()) {
				this.scrollable.sendResize();
				layoutContent();
				layoutScrollbars();
			}
			System.out.println("Scrollable resize layout done");
		});
	}


	public void setMinSize(int width, int height) {
		this.minSize = new Point(width, height);
		if (this.scrollable != null && !minSize.equals(this.scrollable.getSize())) {
			this.scrollable.setSize(minSize);
			System.out.println("setMinSize: content size set");
		}
		layoutContent();
		layoutScrollbars();
		this.scrollable.redraw();
		this.scrollable.update();

	}

	public void setMinSize(Point size) {
		this.minSize = size;
		if (content != null && !minSize.equals(content.getSize())) {
			content.setSize(minSize); // force size so getBounds() works
			System.out.println("setMinSixeP: content size set");
		}
		layoutContent();
		layoutScrollbars();
		this.scrollable.redraw();
		this.scrollable.update();
	}

	public void setExpandHorizontal(boolean expand) {
		this.expandHorizontal = expand;
	}

	public void setExpandVertical(boolean expand) {
		this.expandVertical = expand;
	}

	void layoutScrollbars() {
		Rectangle clientArea = scrollable.getClientArea();
		if (clientArea.width <= 0 || clientArea.height <= 0)
			return;

		Point computedSize = scrollable.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		Rectangle contentBounds = new Rectangle(0, 0, computedSize.x, computedSize.y);

		if ((contentBounds.width == 0 || contentBounds.height == 0) && minSize != null) {
			contentBounds = new Rectangle(0, 0, minSize.x, minSize.y);
		}

		final int barSize = 11;
		final int padding = 5;

		boolean needHBar = contentBounds.width > clientArea.width;
		boolean needVBar = contentBounds.height > clientArea.height;

		// Interdependence
		if (needHBar && !needVBar && verticalBar != null && contentBounds.height > (clientArea.height - barSize)) {
			needVBar = true;
		}
		if (needVBar && !needHBar && horizontalBar != null && contentBounds.width > (clientArea.width - barSize)) {
			needHBar = true;
		}

		System.out.println("LayoutScrollBars: contentBounds=" + contentBounds + ", clientArea=" + clientArea
				+ " needHBar=" + needHBar + " needVBar=" + needVBar);

		// Horizontal ScrollBar
		if (horizontalBar != null) {
			if (needHBar) {
				horizontalBar.setVisible(true);
				horizontalBar.setEnabled(needHBar);
				int hBarWidth = clientArea.width - (needVBar ? barSize : 0);
				int hBarHeight = barSize;
				int hBarX = clientArea.x;
				int hBarY = clientArea.y + clientArea.height - barSize;

				horizontalBar.setBounds(hBarX, hBarY, hBarWidth, hBarHeight);
				horizontalBar.setMaximum(contentBounds.width);
				horizontalBar.setThumb(Math.max(1, Math.min(hBarWidth, contentBounds.width)));
				horizontalBar.setPageIncrement(hBarWidth);
			} else {
				horizontalBar.setVisible(false);
				horizontalBar.setBounds(0, 0, 0, 0);
			}
		}

		// Vertical ScrollBar
		if (verticalBar != null) {
			if (needVBar) {
				verticalBar.setVisible(true);
				verticalBar.setEnabled(needVBar);
				int vBarWidth = barSize;
				int vBarHeight = clientArea.height - (needHBar ? barSize : 0) - 2 * padding;
				int vBarX = clientArea.x + clientArea.width - barSize;
				int vBarY = clientArea.y + padding;

				verticalBar.setBounds(vBarX, vBarY, vBarWidth, vBarHeight);
				verticalBar.setMaximum(contentBounds.height);
				verticalBar.setThumb(Math.max(1, Math.min(vBarHeight, contentBounds.height)));
				verticalBar.setPageIncrement(vBarHeight);

				// Clamp scrollY only if needed
				if (scrollY > contentBounds.height - verticalBar.getThumb()) {
					scrollY = Math.max(0, contentBounds.height - verticalBar.getThumb());
					verticalBar.setSelection(scrollY);
				} else {
					verticalBar.setSelection(scrollY);
				}
			} else {
				verticalBar.setVisible(false);
				verticalBar.setBounds(0, 0, 0, 0);
				verticalBar.setSelection(0);
				scrollY = 0;
			}
		}
	}
	/**
	 * Layouts the content control within the scrollable area. Adjusts the size of
	 * the content based on the client area.
	 */

	void layoutContent() {
		Control contentControl = (content != null) ? content : scrollable;

		Rectangle clientArea = scrollable.getClientArea();
		if (clientArea.width <= 0 || clientArea.height <= 0)
			return;

		Point preferredSize = contentControl.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		// Respect expansion flags
		int width = expandHorizontal ? Math.max(preferredSize.x, clientArea.width) : preferredSize.x;
		int height = expandVertical ? Math.max(preferredSize.y, clientArea.height) : preferredSize.y;

		// Clamp scroll offset if content shrunk
		if (scrollX > width - clientArea.width) {
			scrollX = Math.max(0, width - clientArea.width);
		}
		if (scrollY > height - clientArea.height) {
			scrollY = Math.max(0, height - clientArea.height);
		}

		Point currentSize = contentControl.getSize();
		if (currentSize.x != width || currentSize.y != height) {
			contentControl.setSize(width, height);
			System.out.println("layoutContent: " + contentControl.getClass().getSimpleName() + " size set → " + width
					+ "x" + height);

			if (contentControl instanceof Composite composite) {
				composite.layout(true);
			}
		}

		// Position content if scrollable is acting as a container
		if (content != null) {
			content.setLocation(-scrollX, -scrollY);
		}
	}




	private void onPaint(Event event) {
		System.out.println("Scrollable on paint called");
	}

	private void onMouseHorizontalScroll(Event event) {

	}

	private void onMouseVerticalScroll(Event event) {
		int delta = (event.count > 0) ? -scrollStep : scrollStep;
		scrollY = Math.max(0, Math.min(scrollY + delta, verticalBar.getMaximum() - verticalBar.getThumb()));

		if (verticalBar != null) {
			verticalBar.setSelection(scrollY);
		}
		layoutContent();
		requestRedraw();
	}

	private void onMouseVerticalWheel(Event e) {
		if ((e.stateMask & SWT.CTRL) != 0)
			return;

		boolean shift = (e.stateMask & SWT.SHIFT) != 0;
		boolean scrollVertically = !(shift && horizontalBar != null);

		if (scrollVertically && verticalBar != null) {
			int delta = (e.count > 0) ? -scrollStep : scrollStep;

			Control contentControl = (content != null) ? content : scrollable;

			int contentHeight = contentControl.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
			int clientHeight = scrollable.getClientArea().height;
			int maxScrollY = Math.max(0, contentHeight - clientHeight);

			int newScrollY = Math.max(0, Math.min(scrollY + delta, maxScrollY));

			if (newScrollY != scrollY) {
				scrollY = newScrollY;
				verticalBar.setSelection(scrollY);

				layoutContent();
				requestRedraw();

				// Notify scroll event
				Event selEvent = new Event();
				selEvent.widget = verticalBar;
				selEvent.type = SWT.Selection;
				selEvent.detail = (delta < 0) ? SWT.PAGE_UP : SWT.PAGE_DOWN;
				verticalBar.notifyListeners(SWT.Selection, selEvent);
			}
		} else if (!scrollVertically && horizontalBar != null) {
			onMouseHorizontalWheel(e);
		}
	}

	private void requestRedraw() {
		if (scrollable != null && !scrollable.isDisposed()) {
			scrollable.redraw();
		} else if (content != null && !content.isDisposed()) {
			content.redraw();
		}
	}


	private void onMouseHorizontalWheel(Event e) {
		if (horizontalBar != null) {
			int delta = e.count > 0 ? -scrollStep : scrollStep;
			int newScroll = Math.max(0,
					Math.min(scrollX + delta, horizontalBar.getMaximum() - horizontalBar.getThumb()));
			scrollX = newScroll;
			((ScrollBar) horizontalBar).setSelection(scrollX);
			content.redraw();

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
		Rectangle rectangle = DPIUtil.autoScaleUp(new Rectangle(x, y, width, height));
		return DPIUtil.autoScaleDown(computeTrimInPixels(rectangle.x, rectangle.y, rectangle.width, rectangle.height));
	}

	Rectangle computeTrimInPixels(int x, int y, int width, int height) {
		// System.out.println("computeTrimInPixels : x " + x + " y " + y + " width " +
		// width + " height " + height);
		if (horizontalBar != null && horizontalBar.getVisible()) {
			int hScrollHeight = horizontalBar.getSize().y;
			//System.out.println("Scrollbar height : " + hScrollHeight);
			height += hScrollHeight;
		}
		if (verticalBar != null && verticalBar.getVisible()) {
			int vScrollWidth = verticalBar.getSize().x;
			//System.out.println("Scrollbar width : " + vScrollWidth);
			width += vScrollWidth;
		}
		int borderWidth = 2;
		// System.out.println("Border width : " + borderWidth);
		x -= borderWidth;
		y -= borderWidth;
		width += 2 * borderWidth;
		height += 2 * borderWidth;
		Rectangle trimRectangle = new Rectangle(x, y, width, height);
		// System.out.println("computeTrimInPixels return : " + trimRectangle);
		return trimRectangle;
	}

//	void createWidget() {
//		if ((style & SWT.H_SCROLL) != 0) {
//			horizontalBar = super.createScrollBar(SWT.H_SCROLL);
//			horizontalBar.addListener(SWT.Selection, e -> {
//				scrollX = ((ScrollBar) horizontalBar).getSelection();
//				layoutContent();
//				redraw();
//			});
//		}
//		if ((style & SWT.V_SCROLL) != 0) {
//			verticalBar = super.createScrollBar(SWT.V_SCROLL);
//			verticalBar.addListener(SWT.Selection, e -> {
//				scrollY = ((ScrollBar) verticalBar).getSelection();
//				layoutContent();
//				redraw();
//			});
//		}
//	}

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
		// checkWidget();
		return DPIUtil.autoScaleDown(getClientAreaInPixels());
	}

	Rectangle getClientAreaInPixels() {
		// this.scrollable.forceResize();
		Rectangle widgetBounds = this.scrollable.getBounds();
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

		// System.out.println("getClientAreaInPixels scrollabr: Width " + scrollbarWidth
		// + " height " + scrollbarHeight);
		Rectangle clientArea = new Rectangle(0, 0, clientWidth, clientHeight);
		// System.out.println("getClientAreaInPixels return : " + clientArea);
		return clientArea;
	}

	public ScrollBar getHorizontalBar() {
		return horizontalBar;
	}

	public void setHorizontalBar(ScrollBar horizontalBar) {
		this.horizontalBar = horizontalBar;
	}

	public ScrollBar getVerticalBar() {
		return verticalBar;
	}

	public void setVerticalBar(ScrollBar verticalBar) {
		this.verticalBar = verticalBar;
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
		layoutContent();
		layoutScrollbars();
		content.redraw();
	}


}
