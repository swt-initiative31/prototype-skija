package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWBridge {
	private final LWContainer container;
	private final Composite composite;

	private LWControl mouseOverControl;
	private LWControl focusControl;

	public LWBridge(Composite composite) {
		this.composite = composite;

		container = new LWContainer(this);

		final Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.MouseEnter -> onMouseEnter(event);
			case SWT.MouseMove -> onMouseMove(event);
			case SWT.MouseExit -> onMouseExit(event);
			case SWT.FocusIn -> onFocusIn();
			case SWT.Traverse -> onTraverse(event);
			case SWT.KeyDown -> onKeyDown(event);
			case SWT.KeyUp -> onKeyUp(event);
			}
		};
		composite.addListener(SWT.Paint, listener);
		composite.addListener(SWT.MouseDown, listener);
		composite.addListener(SWT.MouseUp, listener);
		composite.addListener(SWT.MouseEnter, listener);
		composite.addListener(SWT.MouseMove, listener);
		composite.addListener(SWT.MouseExit, listener);
		composite.addListener(SWT.FocusIn, listener);
		composite.addListener(SWT.Traverse, listener);
		composite.addListener(SWT.KeyDown, listener);
		composite.addListener(SWT.KeyUp, listener);
	}

	public LWContainer getContainer() {
		return container;
	}

	public void layout() {
		final MeasureContext measureContext = new MeasureContext(composite);
		container.layout(measureContext);
		measureContext.dispose();
	}

	public void requestFocus(LWControl control) {
		if (focusControl == control) {
			return;
		}

		if (!control.isEnabled() || !control.isVisible() || !control.canBeFocused()) {
			return;
		}

		if (focusControl != null) {
			focusControl.setFocus(false);
		}
		focusControl = control;
		if (focusControl != null) {
			focusControl.setFocus(true);
		}
	}

	public void redraw(int x, int y, int width, int height) {
		composite.redraw(x, y, width, height, false);
	}

	private void onPaint(Event event) {
		final IColorProvider colorProvider = composite.getDisplay().getColorProvider();
		final Rectangle clipping = event.gc.getClipping();
		event.gc.fillRectangle(clipping);
		container.paint(event.gc, colorProvider);
	}

	private void onMouseDown(Event event) {
		container.handleEvent(event);
	}

	private void onMouseUp(Event event) {
		container.handleEvent(event);
	}

	private void onMouseEnter(Event event) {
		mouseOverControl = getControlAt(event);
		if (mouseOverControl != null) {
			final Point location = mouseOverControl.getLocationAbsolute();
			event.x += location.x;
			event.y += location.y;
			mouseOverControl.handleEvent(event);
		}
	}

	private void onMouseMove(Event event) {
		final LWControl mouseControl = getControlAt(event);
		if (mouseControl != mouseOverControl) {
			if (mouseOverControl != null) {
				event.type = SWT.MouseExit;
				mouseOverControl.handleEvent(event);
			}
			mouseOverControl = mouseControl;
			event.type = SWT.MouseEnter;
		}
		if (mouseOverControl != null) {
			mouseOverControl.handleEvent(event);
		}
	}

	private void onMouseExit(Event event) {
		final LWControl mouseControl = getControlAt(event);
		if (mouseControl != null) {
			final Point location = mouseControl.getLocationAbsolute();
			event.x += location.x;
			event.y += location.y;
			mouseControl.handleEvent(event);
		}
		mouseOverControl = null;
	}

	private void onFocusIn() {
		if (focusControl == null) {
			focusControl = container.getFirstFocusable();
			if (focusControl != null) {
				focusControl.setFocus(true);
			}
		}
	}

	private void onTraverse(Event event) {
		switch (event.detail) {
		case SWT.TRAVERSE_ARROW_NEXT,
		     SWT.TRAVERSE_TAB_NEXT -> {
			final List<LWControl> controls = new ArrayList<>();
			container.getFocusableControls(controls);
			final int count = controls.size();
			if (count == 0) {
				requestFocus(null);
				return;
			}

			final int i = controls.indexOf(focusControl);
			final int next = (i + 1) % count;
			requestFocus(controls.get(next));
		}
		case SWT.TRAVERSE_ARROW_PREVIOUS,
		     SWT.TRAVERSE_TAB_PREVIOUS -> {
			final List<LWControl> controls = new ArrayList<>();
			container.getFocusableControls(controls);
			final int count = controls.size();
			if (count == 0) {
				requestFocus(null);
				return;
			}

			final int i = controls.indexOf(focusControl);
			final int prev = (i + count - 1) % count;
			requestFocus(controls.get(prev));
		}
		}
	}

	private void onKeyDown(Event event) {
		handleKeyEvents(event);
	}

	private void onKeyUp(Event event) {
		handleKeyEvents(event);
	}

	private void handleKeyEvents(Event event) {
		for (LWControl control = focusControl; control != null; control = control.getParent()) {
			focusControl.handleEvent(event);
			if (!event.doit) {
				break;
			}
		}
	}

	private LWControl getControlAt(Event event) {
		LWControl control = container.getControlAt(event.x, event.y, true);
		while (!control.isEnabled()) {
			control = control.getParent();
		}
		return control;
	}
}
