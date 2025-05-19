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
 *     Syntevo GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

final class CompositeLightWeightChildHandling {

	private final Composite composite;

	private Control mouseOverControl;

	public CompositeLightWeightChildHandling(Composite composite) {
		composite.assertIsNative();
		this.composite = Objects.requireNonNull(composite);

		final Listener listener = e -> {
			switch (e.type) {
			case SWT.Paint -> onPaint(e);
			case SWT.MouseMove -> onMouseMove(e);
			case SWT.MouseEnter -> onMouseEnter(e);
			case SWT.MouseExit -> onMouseExit(e);
			case SWT.MouseDown,
			     SWT.MouseUp -> onMouseDownOrUp(e);
			}
		};
		composite.addListener(SWT.Paint, listener);
		composite.addListener(SWT.MouseMove, listener);
		composite.addListener(SWT.MouseEnter, listener);
		composite.addListener(SWT.MouseExit, listener);
		composite.addListener(SWT.MouseDown, listener);
		composite.addListener(SWT.MouseUp, listener);
	}

	private void onPaint(Event e) {
		final Rectangle clipping = e.gc.getClipping();
		Drawing.drawWithGC(composite, e.gc, gc -> {
			e.gc = gc;
			gc.setPreventDispose(true);
			try {
				drawChildren(composite, clipping, e);
			} finally {
				gc.setPreventDispose(false);
			}
		});
		e.widget = composite;
	}

	private void onMouseMove(Event e) {
		final Point location = new Point(e.x, e.y);
		final Control mouseControl = getChildAt(location);
		int type = e.type;
		if (mouseControl != mouseOverControl) {
			if (mouseOverControl != null) {
				sendMouseEvent(SWT.MouseExit, location, mouseOverControl, e);
			}
			mouseOverControl = mouseControl;
			type = SWT.MouseEnter;
		}
		if (mouseControl != null) {
			sendMouseEvent(type, location, mouseControl, e);
		}
	}

	private void onMouseEnter(Event e) {
		final Point location = new Point(e.x, e.y);
		mouseOverControl = getChildAt(location);
		if (mouseOverControl != null) {
			sendMouseEvent(e.type, location, mouseOverControl, e);
		}
	}

	private void onMouseExit(Event e) {
		if (mouseOverControl != null) {
			sendMouseEvent(e.type, new Point(e.x, e.y), mouseOverControl, e);
			mouseOverControl = null;
		}
	}

	private void onMouseDownOrUp(Event e) {
		final Point location = new Point(e.x, e.y);
		final Control mouseControl = getChildAt(location);
		if (mouseControl != mouseOverControl) {
			if (mouseOverControl != null) {
				sendMouseEvent(SWT.MouseExit, location, mouseOverControl, e);
			}
			mouseOverControl = mouseControl;
			if (mouseControl != null) {
				sendMouseEvent(SWT.MouseEnter, location, mouseControl, e);
			}
		}
		if (mouseControl != null) {
			sendMouseEvent(e.type, location, mouseControl, e);
		}
	}

	private Control getChildAt(Point location) {
		return getChildAt(location, composite);
	}

	private Control getChildAt(Point location, Composite composite) {
		for (Control child : composite.getChildren()) {
			if (!child.isLightWeight()) {
				continue;
			}

			if (!child.isVisible()) {
				continue;
			}

			final Rectangle bounds = child.getBounds();
			if (bounds.contains(location.x, location.y)) {
				location.x -= bounds.x;
				location.y -= bounds.y;
				if (child instanceof Composite childComposite) {
					final Control grandChild = getChildAt(location, childComposite);
					if (grandChild != null) {
						return grandChild;
					}
				}
				return child;
			}
		}
		return null;
	}

	private static void drawChildren(Composite composite, Rectangle clipping, Event e) {
		for (Control child : composite.getChildren()) {
			if (!child.isLightWeight()) {
				continue;
			}

			if (!child.getVisible()) {
				continue;
			}

			final Rectangle b = child.getBounds();
			if (b.width < 1 || b.height < 1) {
				continue;
			}

			if (!clipping.intersects(b)) {
				continue;
			}

//				gc.setClipping(b);
			e.gc.translate(b.x, b.y);
			try {
				e.gc.setForeground(child.getForeground());
				e.gc.setBackground(child.getBackground());
				e.widget = child;
				child.sendEvent(e);

				if (child instanceof Composite childComposite) {
					final Rectangle subClipping = b.intersection(clipping);
					subClipping.x -= b.x;
					subClipping.y -= b.y;
					drawChildren(childComposite, subClipping, e);
				}
			}
			finally {
				e.gc.translate(-b.x, -b.y);
			}
		}
	}

	private static void sendMouseEvent(int type, Point location, Control control, Event original) {
		final Event event = new Event();
		event.display = original.display;
		event.widget = control;
		event.type = type;
		event.x = location.x;
		event.y = location.y;
		event.button = original.button;
		event.count = original.count;
		event.doit = original.doit;
		event.stateMask = original.stateMask;
		event.time = original.time;
		control.sendEvent(event);
	}
}
