/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawing;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class CustomScrollBar {

	private final Control control;
	private final int style;
	private final ScrollBarRenderer renderer;
	private final Rectangle bounds;

	private boolean enabled;

	private int selection;
	private int minimum;
	private int maximum = 100;
	private int thumb = 10;
	private int increment;
	private int pageIncrement;

	private int dragOffset;
	private int thumbPosition;

	private Rectangle trackRectangle;
	private Rectangle thumbRectangle;

	private boolean autoRepeating;

	public CustomScrollBar(Control control, int style) {
		if (control == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

		this.control = control;
		this.style = style;

		Listener listener = event -> {
			switch (event.type) {
				case SWT.Paint -> onPaint(event);
				case SWT.KeyDown -> onKeyDown(event);
				case SWT.MouseDown -> onMouseDown(event);
				case SWT.MouseMove -> onMouseMove(event);
				case SWT.MouseUp -> onMouseUp(event);
				case SWT.MouseHorizontalWheel -> onMouseHorizontalWheel(event);
				case SWT.MouseVerticalWheel -> onMouseVerticalWheel(event);
				case SWT.MouseEnter -> onMouseEnter();
				case SWT.MouseExit -> onMouseExit();
			}
		};

		addListener(SWT.Paint, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseHorizontalWheel, listener);
		addListener(SWT.MouseVerticalWheel, listener);
		addListener(SWT.MouseEnter, listener);
		addListener(SWT.MouseExit, listener);

		bounds = new Rectangle(0, 0, 0, 0);

		final RendererFactory rendererFactory = control.display.getRendererFactory();
		renderer = rendererFactory.createScrollBarRenderer(this);
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		redraw();
	}

	public void setBounds(int x, int y, int width, int height) {
		this.bounds.x = x;
		this.bounds.y = y;
		this.bounds.width = width;
		this.bounds.height = height;
	}

	public boolean isHorizontal() {
		return (style & SWT.VERTICAL) == 0;
	}

	private void onPaint(Event event) {
		Drawing.drawWithGC(control, event.gc, gc -> renderer.paint(gc, bounds.x, bounds.y, bounds.width, bounds.height));
		this.thumbRectangle = renderer.getThumbRectangle();
		this.trackRectangle = renderer.getTrackRectangle();
	}

	private void onMouseEnter() {
		renderer.setDrawTrack(true);
		redraw();
	}

	private void onMouseExit() {
		renderer.setDrawTrack(false);
		renderer.setThumbHovered(false);
		redraw();
	}

	private void onMouseVerticalWheel(Event event) {
		if (event.count > 0) {
			increment(1);
		} else if (event.count < 0) {
			increment(-1);
		}
	}

	private void onMouseHorizontalWheel(Event event) {
		if (event.count < 0) {
			increment(1);
		} else if (event.count > 0) {
			increment(-1);
		}
	}

	private void increment(int count) {
		int delta = getIncrement() * count;
		int newValue = minMax(getMinimum(), getSelection() + delta, getMaximum());
		setSelection(newValue);
		redraw();
	}

	private void onKeyDown(Event event) {
		autoRepeating = false;

		KeyEvent keyEvent = new KeyEvent(event);
		switch (keyEvent.keyCode) {
			case SWT.ARROW_DOWN, SWT.ARROW_LEFT -> increment(-1);
			case SWT.ARROW_RIGHT, SWT.ARROW_UP -> increment(1);
			case SWT.PAGE_DOWN -> pageIncrement(-1);
			case SWT.PAGE_UP -> pageIncrement(1);
			case SWT.HOME -> setSelection(getMinimum());
			case SWT.END -> setSelection(getMaximum());
		}
	}

	private void pageIncrement(int count) {
		int delta = getPageIncrement() * count;
		int newValue = minMax(getMinimum(), getSelection() + delta, getMaximum());
		setSelection(newValue);
		redraw();
	}

	private void onMouseDown(Event event) {
		if (event.button != 1) {
			return;
		}

		control.setCapture(true);

		// Drag of the thumb.
		if (thumbRectangle != null && thumbRectangle.contains(event.x, event.y)) {
			renderer.setDragging(true);
			dragOffset = isHorizontal() ? event.x - thumbRectangle.x : event.y - thumbRectangle.y;
			return;
		}

		// Click on the track. i.e page increment/decrement
		if (trackRectangle == null || !trackRectangle.contains(event.x, event.y)) {
			return;
		}

		handleTrackClick(event.x, event.y, 500, new Runnable() {
			@Override
			public void run() {
				if (!autoRepeating) {
					return;
				}

				autoRepeating = false;
				if (control.isDisposed()) {
					return;
				}

				final Point cursorPos = control.toControl(control.display.getCursorLocation());
				if (thumbRectangle != null && thumbRectangle.contains(cursorPos.x, cursorPos.y)) {
					return;
				}

				if (trackRectangle == null || !trackRectangle.contains(cursorPos.x, cursorPos.y)) {
					return;
				}

				handleTrackClick(cursorPos.x, cursorPos.y, 150, this);
			}
		});
	}

	private void handleTrackClick(int x, int y, int milliseconds, Runnable runnable) {
		int pageIncrement = getPageIncrement();
		int oldSelection = getSelection();

		int newSelection;
		if (isHorizontal()) {
			if (x < thumbRectangle.x) {
				newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
			} else {
				newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
			}
		} else {
			if (y < thumbRectangle.y) {
				newSelection = Math.max(getMinimum(), oldSelection - pageIncrement);
			} else {
				newSelection = Math.min(getMaximum() - getThumb(), oldSelection + pageIncrement);
			}
		}

		setSelection(newSelection);

		autoRepeating = true;
		control.display.timerExec(milliseconds, runnable);
	}

	private void onMouseUp(Event event) {
		if (event.button != 1) {
			return;
		}

		autoRepeating = false;
		control.setCapture(false);
		if (renderer.getDragging()) {
			renderer.setDragging(false);
			updateValueFromThumbPosition();
			redraw();
		}
	}

	private void updateValueFromThumbPosition() {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();
		int range = max - min;

		int newValue;
		if (isHorizontal()) {
			int trackWidth = bounds.width - 4 - thumbRectangle.width;
			int relativeX = Math.min(trackWidth, Math.max(0, thumbPosition - 2));
			newValue = min + (relativeX * (range - thumb)) / trackWidth;
		} else {
			int trackHeight = bounds.height - 4 - thumbRectangle.height;
			int relativeY = Math.min(trackHeight, Math.max(0, thumbPosition - 2));
			newValue = min + (relativeY * (range - thumb)) / trackHeight;
		}
		setSelection(newValue);
	}

	private void onMouseMove(Event event) {
		if (thumbRectangle == null) {
			return;
		}

		boolean isThumbHovered = thumbRectangle.contains(event.x, event.y);
		if (isThumbHovered != renderer.getHovered()) {
			renderer.setThumbHovered(isThumbHovered);
			redraw();
		}

		if (renderer.getDragging()) {
			int newPos;
			int min = getMinimum();
			int max = getMaximum();
			int thumb = getThumb();
			int range = max - min;
			int newValue;

			if (isHorizontal()) {
				newPos = event.x - dragOffset;

				int minX = 2;
				int maxX = bounds.width - thumbRectangle.width - 2;
				newPos = Math.max(minX, Math.min(newPos, maxX));

				thumbRectangle.x = newPos;
				thumbPosition = newPos;

				int trackWidth = bounds.width - 4 - thumbRectangle.width;
				int relativeX = thumbPosition - 2;
				newValue = min + Math.round((relativeX * (float) (range - thumb)) / trackWidth);
			} else {
				newPos = event.y - dragOffset;

				int minY = 2;
				int maxY = bounds.height - thumbRectangle.height - 2;
				newPos = Math.max(minY, Math.min(newPos, maxY));

				thumbRectangle.y = newPos;
				thumbPosition = newPos;

				int trackHeight = bounds.height - 4 - thumbRectangle.height;
				int relativeY = thumbPosition - 2;
				newValue = min + Math.round((relativeY * (float) (range - thumb)) / trackHeight);
			}

			newValue = Math.max(min, Math.min(newValue, max - thumb));

			if (newValue != getSelection()) {
				setSelection(newValue);
			}
			redraw();
		}
	}

	public Point computeDefaultSize() {
		return renderer.computeDefaultSize();
	}

	public int getIncrement() {
		return increment;
	}

	public int getMaximum() {
		return maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	public int getPageIncrement() {
		return pageIncrement;
	}

	public int getSelection() {
		return selection;
	}

	public int getThumb() {
		return thumb;
	}

	public void setIncrement(int increment) {
		checkWidget();
		this.increment = Math.max(1, increment);
		redraw();
	}

	public void setMaximum(int max) {
		checkWidget();
		setMaximum(max, true);
	}

	private void setMaximum(int max, boolean redraw) {
		if (max <= minimum) {
			return;
		}

		this.maximum = max;

		this.thumb = Math.min(this.thumb, this.maximum - minimum);

		this.selection = Math.min(this.selection, this.maximum - this.thumb);
		this.selection = Math.max(this.selection, minimum);
		if (redraw) {
			redraw();
		}
	}

	public void setMinimum(int minimum) {
		checkWidget();
		setMinimum(minimum, true);
	}

	private void setMinimum(int minimum, boolean redraw) {
		if (internalSetMinimum(minimum) && redraw) {
			redraw();
		}
	}

	private boolean internalSetMinimum(int minimum) {
		if (minimum < 0) {
			return false;
		}

		if (minimum <= this.maximum && minimum <= this.selection) {
			this.minimum = minimum;
			return true;
		}

		if (minimum <= this.maximum - this.thumb && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			return true;
		}

		if (minimum >= maximum) {
			this.minimum = this.thumb;
			return true;
		}

		if (minimum > this.maximum - this.thumb && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			this.thumb = this.maximum - this.minimum;
			return true;
		}
		return false;
	}

	public void setPageIncrement(int pageIncrement) {
		checkWidget();
		this.pageIncrement = Math.max(1, pageIncrement);
		redraw();
	}

	public void setSelection(int selection) {
		checkWidget();
		setSelection(selection, true);
	}

	private void setSelection(int selection, boolean redraw) {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();

		// Ensure selection does not exceed max - thumb
		int newValue = Math.max(min, Math.min(selection, max - thumb));

		if (newValue != this.selection) {
			this.selection = newValue;
			if (redraw) {
				redraw();
				control.sendEvent(SWT.Selection, new Event());
			}
		}
	}

	public void setThumb(int thumb) {
		checkWidget();
		setThumb(thumb, true);
	}

	private void setThumb(int thumb, boolean redraw) {
		if (thumb <= 0 || this.thumb == thumb) {
			return;
		}

		if (thumb <= this.maximum && thumb <= this.selection) {
			this.thumb = thumb;
			if (redraw) {
				redraw();
			}
			return;
		}

		if (thumb <= (this.maximum - this.minimum) && thumb > this.selection) {
			this.selection = this.maximum - thumb;
			this.thumb = thumb;
		}

		if (thumb > (this.maximum - this.minimum) && thumb > this.selection) {
			this.thumb = this.maximum - this.minimum;
			this.selection = this.minimum;
		}
		if (redraw) {
			redraw();
		}
	}

	public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
		checkWidget();
		setSelection(selection, false);
		setMinimum(minimum, false);
		setMaximum(maximum, false);
		setThumb(thumb, false);
		this.increment = Math.max(1, increment);
		this.pageIncrement = Math.max(1, pageIncrement);
		redraw();
	}

	private void addListener(int type, Listener listener) {
		control.addListener(type, listener);
	}

	private void checkWidget() {
		control.checkWidget();
	}

	private void redraw() {
		if (bounds.width > 0 && bounds.height > 0) {
			control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
		}
	}

	public Color getColor(String key) {
		return control.getColorProvider().getColor(key);
	}

	private static int minMax(int min, int value, int max) {
		return Math.min(Math.max(min, value), max);
	}
}
