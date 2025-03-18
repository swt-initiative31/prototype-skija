package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.graphics.*;

public abstract class ControlState {

	protected abstract Control getControl();

	private int x;
	private int y;
	private int width;
	private int height;
	private boolean enabled = true;
	private Color background;
	private Color foreground;

	protected ControlState() {
	}

	public final int getX() {
		return x;
	}

	public final void setX(int x) {
		if (x == this.x) {
			return;
		}

		this.x = x;
		propertyChanged();
	}

	public final int getY() {
		return y;
	}

	public final void setY(int y) {
		if (y == this.y) {
			return;
		}

		this.y = y;
		propertyChanged();
	}

	public final Point getLocation() {
		return new Point(x, y);
	}

	public final void setLocation(int x, int y) {
		if (x == this.x && y == this.y) {
			return;
		}

		this.x = x;
		this.y = y;
		propertyChanged();
	}

	public final int getWidth() {
		return width;
	}

	public final void setWidth(int width) {
		if (width == this.width) {
			return;
		}

		this.width = width;
		propertyChanged();
	}

	public final int getHeight() {
		return height;
	}

	public final void setHeight(int height) {
		if (height == this.height) {
			return;
		}

		this.height = height;
		propertyChanged();
	}

	public final Point getSize() {
		return new Point(width, height);
	}

	public final void setSize(int width, int height) {
		if (width == this.width && height == this.height) {
			return;
		}

		this.width = width;
		this.height = height;
		propertyChanged();
	}

	public final Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public final void setBounds(int x, int y, int width, int height) {
		if (x == this.x && y == this.y && width == this.width && height == this.height) {
			return;
		}

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		propertyChanged();
	}

	public final boolean isEnabled() {
		return enabled && getControl().getParent().isEnabled();
	}

	public final boolean getEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		if (enabled == this.enabled) {
			return;
		}

		this.enabled = enabled;
		if (getControl().getParent().isEnabled()) {
			propertyChanged();
		}
	}

	public final Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		if (Objects.equals(background, this.background)) {
			return;
		}

		this.background = background;
		propertyChanged();
	}

	public final Color getForeground() {
		return foreground;
	}

	public final void setForeground(Color foreground) {
		if (Objects.equals(foreground, this.foreground)) {
			return;
		}

		this.foreground = foreground;
		propertyChanged();
	}

	protected final void propertyChanged() {
		getControl().redraw();
	}
}
