package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWContainer extends LWControl {

	private final List<LWControl> children = new ArrayList<>();
	private final LWBridge bridge;

	private LWLayoutManager layoutManager;

	public LWContainer(LWBridge bridge) {
		super(null);
		this.bridge = bridge;
	}

	@Override
	public Point getPreferredSize(IMeasureContext measureContext) {
		if (layoutManager != null) {
			return layoutManager.getPreferredSize(getChildren(), measureContext);
		}
		return new Point(0, 0);
	}

	@Override
	public void paint(GC gc, IColorProvider colorProvider) {
		final Rectangle clipping = gc.getClipping();
		for (LWControl child : children) {
			if (!child.isVisible()) {
				continue;
			}

			final Rectangle bounds = child.getBounds();
			if (!bounds.intersects(clipping)) {
				continue;
			}

			gc.transform(bounds.x, bounds.y);
			try {
/*
				gc.setClipping(clipping.x - bounds.x, clipping.y - bounds.y,
				               clipping.width, clipping.height);
*/
				child.paint(gc, colorProvider);
			}
			finally {
				gc.transform(-bounds.x, -bounds.y);
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.MouseDown,
		     SWT.MouseEnter,
		     SWT.MouseExit,
		     SWT.MouseMove,
		     SWT.MouseUp -> handleMouseEvent(event);
		}
	}

	@Override
	protected LWBridge getBridge() {
		return bridge;
	}

	public void add(LWControl control) {
		if (children.contains(control)) {
			throw new IllegalArgumentException("duplicate");
		}
		children.add(control);
	}

	public List<LWControl> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public void setLayoutManager(LWLayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	public void layout(IMeasureContext measureContext) {
		if (layoutManager != null) {
			layoutManager.layout(getChildren(), getSize(), measureContext);
		}
	}

	public LWControl getControlAt(int x, int y, boolean skipInvisible) {
		for (LWControl child : children) {
			if (skipInvisible && !child.isVisible()) {
				continue;
			}

			final Rectangle bounds = child.getBounds();
			if (!bounds.contains(x, y)) {
				continue;
			}

			if (child instanceof LWContainer container) {
				return container.getControlAt(x - bounds.x, y - bounds.y, skipInvisible);
			}
			return child;
		}
		return this;
	}

	public LWControl getFirstFocusable() {
		for (LWControl child : children) {
			if (!child.isVisible() || !child.isEnabled()) {
				continue;
			}

			if (child.canBeFocused()) {
				return child;
			}

			if (child instanceof LWContainer container) {
				return container.getFirstFocusable();
			}
		}
		return null;
	}

	protected LWContainer getRoot() {
		final LWContainer parent = getParent();
		if (parent == null) {
			return this;
		}
		return parent.getRoot();
	}

	public void getFocusableControls(List<LWControl> focusableControls) {
		for (LWControl child : children) {
			if (child.canBeFocused()) {
				focusableControls.add(child);
			}
			if (child instanceof LWContainer container) {
				container.getFocusableControls(focusableControls);
			}
		}
	}

	private void handleMouseEvent(Event event) {
		for (LWControl child : children) {
			if (!child.isVisible()) {
				continue;
			}

			final Rectangle bounds = child.getBounds();
			if (!bounds.contains(event.x, event.y)) {
				continue;
			}

			final int x = bounds.x;
			final int y = bounds.y;
			event.x -= x;
			event.y -= y;
			try {
				child.handleEvent(event);
			}
			finally {
				event.x += x;
				event.y += y;
			}
		}
	}
}
