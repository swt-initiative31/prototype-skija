package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public abstract class LWControl {

	public static final int EVENT_SIZE = 0;
	public static final int EVENT_ENABLED = EVENT_SIZE + 1;
	public static final int EVENT_VISIBLE = EVENT_ENABLED + 1;
	public static final int EVENT_FOCUS = EVENT_VISIBLE + 1;
	protected static final int EVENT_NEXT = EVENT_FOCUS + 1;

	public abstract Point getPreferredSize(IMeasureContext measureContext);

	public abstract void paint(GC gc, IColorProvider colorProvider);

	private final List<Listener> listeners = new ArrayList<>();

	private int width;
	private int height;
	private boolean enabled = true;
	private boolean visible = true;
	private boolean hasFocus;

	protected LWControl() {
	}

	public Point getSize() {
		return new Point(width, height);
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

	public boolean hasFocus() {
		return hasFocus;
	}

	public void setHasFocus(boolean hasFocus) {
		if (hasFocus == this.hasFocus) {
			return;
		}

		this.hasFocus = hasFocus;
		notifyListeners(EVENT_FOCUS);
	}

	public void addListener(Listener listener) {
		Objects.requireNonNull(listener);

		listeners.add(listener);
	}

	protected final void notifyListeners(int type) {
		for (Listener listener : new ArrayList<>(listeners)) {
			listener.handleEvent(type);
		}
	}

	public interface Listener {
		void handleEvent(int type);
	}
}
