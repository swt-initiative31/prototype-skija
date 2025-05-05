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

final class CustomBridge {

	private final Composite composite;

	private Control mouseOverControl;

	public CustomBridge(Composite composite) {
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
			for (Control child : composite.getChildren()) {
				if (!child.isCustomControl()) {
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
				gc.translate(b.x, b.y);
				try {
					sendEvent(child, e);
				}
				finally {
					gc.translate(-b.x, -b.y);
				}
			}
		});
	}

	private void onMouseMove(Event e) {
		final Control mouseControl = getChildAt(e);
		if (mouseControl != mouseOverControl) {
			if (mouseOverControl != null) {
				e.type = SWT.MouseExit;
				sendEvent(mouseOverControl, e);
			}
			mouseOverControl = mouseControl;
			e.type = SWT.MouseEnter;
		}
		if (mouseControl != null) {
			sendEvent(mouseControl, e);
		}
	}

	private void onMouseEnter(Event e) {
		mouseOverControl = getChildAt(e);
		if (mouseOverControl != null) {
			sendEvent(mouseOverControl, e);
		}
	}

	private void onMouseExit(Event e) {
		if (mouseOverControl != null) {
			sendEvent(mouseOverControl, e);
			mouseOverControl = null;
		}
	}

	private void onMouseDownOrUp(Event e) {
		final Control mouseControl = getChildAt(e);
		if (mouseControl != mouseOverControl) {
			if (mouseOverControl != null) {
				final Event event = new Event();
				event.type = SWT.MouseExit;
				event.x = e.x;
				event.y = e.y;
				sendEvent(mouseOverControl, event);
			}
			mouseOverControl = mouseControl;
		}
		if (mouseControl != null) {
			sendEvent(mouseControl, e);
		}
	}

	private Control getChildAt(Event e) {
		if (mouseOverControl != null && mouseOverControl.isEnabled() && mouseOverControl.isVisible()) {
			final Rectangle bounds = mouseOverControl.getBounds();
			if (bounds.contains(e.x, e.y)) {
				return mouseOverControl;
			}
		}
		for (Control child : composite.getChildren()) {
			if (!child.isCustomControl()) {
				continue;
			}

			if (!child.isVisible()) {
				continue;
			}

			final Rectangle bounds = child.getBounds();
			if (bounds.contains(e.x, e.y)) {
				return child;
			}
		}
		return null;
	}

	private static void sendEvent(Control control, Event event) {
		event.widget = control;
		control.sendEvent(event);
	}
}
