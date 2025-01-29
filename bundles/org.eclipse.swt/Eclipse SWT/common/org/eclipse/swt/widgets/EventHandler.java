package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 *
 * Handles events from the parent control, reinterprets these for the child
 * controls and call triggers the new Events for them.
 *
 */
class EventHandler implements Listener {

	// TODO for all for loops: first filter not visible controls and don't check the
	// specific handling.
	private final java.util.List<Control> childControls;

	EventHandler(java.util.List<Control> childControls) {
		this.childControls = childControls;
	}

	Control mouseOverWidget = null;
	Control focusWidget = null;

	public void mouseMove(Event e) {
		if (mouseOverWidget != null) {
			if (translatePosition(mouseOverWidget.getBounds()).contains(e.x, e.y)) {
				mouseOverWidget.triggerEvent(e);
				return;
			} else {
				mouseOverWidget.triggerEvent(createMouseExitEvent(e));
				mouseOverWidget = null;
			}
		}
		// TODO: iterative is very inefficient. Binary search would be
		// better.
		for (var w : childControls) {

			if (translatePosition(w.getBounds()).contains(e.x, e.y)) {
				mouseOverWidget = w;
				w.triggerEvent(createMouseEnterEvent(e));
				w.triggerEvent(e);
				return;
			}
		}

	}

	Rectangle translatePosition(Rectangle r) {
		return r;
	}

	private Event createMouseEnterEvent(Event e) {

		Event me = copyEvent(e);
		me.type = SWT.MouseEnter;

		return me;
	}

	private Event createMouseExitEvent(Event e) {
		Event me = copyEvent(e);
		me.type = SWT.MouseExit;

		return me;
	}

	public static Event copyEvent(Event original) {
		Event copy = new Event();
		copy.character = original.character;
		copy.keyCode = original.keyCode;
		copy.stateMask = original.stateMask;
		copy.time = original.time;
		copy.data = original.data;
		copy.display = original.display;
		copy.widget = original.widget;
		copy.type = original.type;
		copy.x = original.x;
		copy.y = original.y;
		copy.width = original.width;
		copy.height = original.height;
		copy.count = original.count;
		copy.button = original.button;
		copy.detail = original.detail;
		return copy;
	}

	public void mouseExit(Event e) {

		if (mouseOverWidget != null) {
			mouseOverWidget.triggerEvent(createMouseExitEvent(e));
			mouseOverWidget.redraw();
			mouseOverWidget = null;
		}

	}

	public void mouseEnter(Event e) {

		for (var w : childControls) {
			if (translatePosition(w.getBounds()).contains(e.x, e.y)) {
				if (mouseOverWidget != w) {
					w.triggerEvent(createMouseEnterEvent(e));
					w.redraw();
					if (mouseOverWidget != null) {
						mouseOverWidget.triggerEvent(createMouseExitEvent(e));
						mouseOverWidget.redraw();
					}
					w = mouseOverWidget;
				}
			}
		}

	}

	@Override
	public void handleEvent(Event e) {

		switch (e.type) {

		case SWT.Paint:
			onPaint(e);
			break;
		case SWT.Dispose:
			onDispose(e);
			break;
		case SWT.MouseMove:
			mouseMove(e);
			break;
		case SWT.MouseEnter:
			mouseEnter(e);
			break;
		case SWT.MouseExit:
			mouseExit(e);
			break;
		case SWT.MouseDown:
		case SWT.MouseDoubleClick:
		case SWT.MouseUp:
		case SWT.MouseWheel:
			onWidgetMouseEvent(e);
			break;
		case SWT.KeyDown:
		case SWT.KeyUp:
			onWidgetKeyEvent(e);
			break;
		case SWT.Gesture:
			onGesture(e);
			break;
		case SWT.FocusIn:
			onFocusIn(e);
			break;
		case SWT.FocusOut:
			onFocusOut(e);
			break;
		}

	}

	private void onPaint(Event e) {

		for (var w : childControls) {

			var b = w.getBounds();

			trigger(e, w);
			if (Control.isCustomDrawn(w)) {

				var bfg = e.gc.getForeground();
				var bbg = e.gc.getBackground();
				e.gc.setForeground(w.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				e.gc.setBackground(w.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
				e.gc.drawLine(0, 0, b.x, b.y);
				e.gc.drawRectangle(b.x, b.y, b.width, b.height);
//				if (w instanceof MockControl)
//					e.gc.fillRectangle(b);

				e.gc.setForeground(bfg);
				e.gc.setBackground(bbg);

			}

		}
	}

	private Event createPaintEvent(Control w, Event e) {

		Event ne = new Event();

		Rectangle b = w.getBounds();

		ne.type = SWT.Paint;
		ne.widget = w;
		ne.gc = GCFactory.createChildGC(e.gc, w);
		ne.x = 0;
		ne.y = 0;
		ne.height = b.height;
		ne.width = b.width;

		return ne;
	}

	private void onFocusOut(Event e) {


//		childControls.stream().forEach(c -> c.handleEvent(e));
//		focusWidget = null;

	}

	private void onFocusIn(Event e) {

		if (focusWidget == null && !childControls.isEmpty()) {
			focusWidget = childControls.get(0);
		}

		if (focusWidget != null)
			focusWidget.triggerEvent(e);

	}

	private void onGesture(Event e) {
		// these events can be handled similar to mouse events
		onWidgetMouseEvent(e);

	}

	private void trigger(Event e, Control c) {

		var ne = Control.translateEvent(e, c);
		c.triggerEvent(ne);
		disposeGC(ne.gc);

	}

	private void onWidgetKeyEvent(Event e) {
		if (focusWidget != null) {

		}
			focusWidget.triggerEvent(e);
	}

	private void onWidgetMouseEvent(Event e) {

		if (e.type == SWT.MouseDown) {
			if (focusWidget != mouseOverWidget) {
				if (focusWidget != null) {
					var fe = createFocusOutEvent(e, focusWidget);
					focusWidget.triggerEvent(fe);
					disposeGC(fe.gc);
					focusWidget = null;
				}

				if (mouseOverWidget != null) {
					var fe = createFocusInEvent(e, mouseOverWidget);
					mouseOverWidget.triggerEvent(fe);
					disposeGC(fe.gc);
					focusWidget = mouseOverWidget;
				}

			}
		}

		if (mouseOverWidget != null)
			mouseOverWidget.triggerEvent(e);

	}

	private void disposeGC(GC gc) {
		if (gc != null && !gc.isDisposed())
			gc.dispose();
	}

	private Event createFocusInEvent(Event e, Control c) {

		var ne = Control.translateEvent(e, c);
		ne.type = SWT.FocusIn;
		ne.display = c.getDisplay();

		return ne;
	}

	private Event createFocusOutEvent(Event e, Control c) {

		var ne = Control.translateEvent(e, c);
		ne.type = SWT.FocusOut;
		ne.display = c.getDisplay();
		return ne;
	}

	private void onDispose(Event e) {

		childControls.stream().forEach(c -> {

			trigger(e, c);

		});

	}

	public void setFocusElement(Control c) {

		if (this.focusWidget == c) {
			return;
		} else
			this.focusWidget = c;

	}

}
