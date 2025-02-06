package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public abstract class LWControl {

	public abstract Point getPreferredSize(IMeasureContext measureContext);

	public abstract void paint(GC gc, IColorProvider colorProvider);

	public static final int EVENT_SIZE = 0;
	public static final int EVENT_LOCATION = EVENT_SIZE + 1;
	public static final int EVENT_ENABLED = EVENT_LOCATION + 1;
	public static final int EVENT_VISIBLE = EVENT_ENABLED + 1;
	public static final int EVENT_FOCUS = EVENT_VISIBLE + 1;
	protected static final int EVENT_NEXT = EVENT_FOCUS + 1;

	private final List<Listener> listeners = new ArrayList<>();
	private final LWContainer parent;

	private int x;
	private int y;
	private int width;
	private int height;
	private boolean enabled = true;
	private boolean visible = true;
	private boolean hasFocus;
	private Object layoutData;

	protected LWControl(LWContainer parent) {
		this.parent = parent;
		if (parent != null) {
			parent.add(this);
		}
	}

	public Point getSize() {
		return new Point(width, height);
	}

	public void setSize(Point size) {
		setSize(size.x, size.y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setSize(int width, int height) {
		if (width == this.width && height == this.height) {
			return;
		}

		this.width = width;
		this.height = height;
		notifyListeners(EVENT_SIZE);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public void setLocation(int x, int y) {
		if (x == this.x && y == this.y) {
			return;
		}
		this.x = x;
		this.y = y;
		notifyListeners(EVENT_LOCATION);
	}

	public Point getLocationAbsolute() {
		final Point location = new Point(x, y);
		for (LWContainer parent = this.parent; parent != null; parent = ((LWControl)parent).parent) {
			location.x += parent.getX();
			location.y += parent.getY();
		}
		return location;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (enabled == this.enabled) {
			return;
		}

		this.enabled = enabled;
		notifyListeners(EVENT_ENABLED);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		if (visible == this.visible) {
			return;
		}

		this.visible = visible;
		notifyListeners(EVENT_VISIBLE);
	}

	public void setHasFocus(boolean hasFocus) {
		if (hasFocus == this.hasFocus) {
			return;
		}

		this.hasFocus = hasFocus;
		notifyListeners(EVENT_FOCUS);
	}

	public LWContainer getParent() {
		return parent;
	}

	public void redraw() {
		redraw(0, 0, width, height);
	}

	public void redraw(int x, int y, int width, int height) {
		if (parent != null) {
			parent.redraw(x + this.x, y + this.y, width, height);
		}
		else {
			final LWBridge bridge = getBridge();
			if (bridge != null) {
				bridge.redraw(x, y, width, height);
			}
		}
	}

	public Object getLayoutData() {
		return layoutData;
	}

	public void setLayoutData(Object layoutData) {
		this.layoutData = layoutData;
	}

	public boolean hasFocus() {
		return hasFocus;
	}

	public void setFocus(boolean hasFocus) {
		if (hasFocus == this.hasFocus) {
			return;
		}
		this.hasFocus = hasFocus;
		notifyListeners(EVENT_FOCUS);
	}

	protected boolean canBeFocused() {
		return isVisible() && isEnabled();
	}


	public void addListener(Listener listener) {
		Objects.requireNonNull(listener);

		listeners.add(listener);
	}

	protected void notifyListeners(int type) {
		for (Listener listener : new ArrayList<>(listeners)) {
			listener.handleEvent(type);
		}
	}

	protected void handleEvent(Event event) {
	}

	public void requestFocus() {
		final LWBridge bridge = getBridge();
		if (bridge == null) {
			return;
		}

		bridge.requestFocus(this);
	}

	protected LWBridge getBridge() {
		final LWContainer root = getRoot();
		if (root == null) {
			return null;
		}
		return root.getBridge();
	}

	private LWContainer getRoot() {
		if (parent == null) {
			return null;
		}
		return parent.getRoot();
	}

	public interface Listener {
		void handleEvent(int type);
	}
}
