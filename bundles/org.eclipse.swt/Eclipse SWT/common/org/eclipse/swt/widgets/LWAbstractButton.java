package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;

/**
 * @author Thomas Singer
 */
public abstract class LWAbstractButton extends LWControl {

	public abstract String getAccessibleText();

	protected abstract void handleSelection();

	public static final int EVENT_SELECTED = LWControl.EVENT_NEXT;
	public static final int EVENT_STYLE = EVENT_SELECTED + 1;
	public static final int EVENT_HOVERED = EVENT_STYLE + 1;
	public static final int EVENT_PRESSED = EVENT_HOVERED + 1;
	public static final int EVENT_TEXT = EVENT_PRESSED + 1;
	private static final int FLAG_SELECTED = 1;
	private static final int FLAG_HOVERED = 2;
	private static final int FLAG_PRESSED = 4;

	protected int style;
	private String text;
	private int flags;

	protected LWAbstractButton(int style) {
		super(null);
		this.style = style;
	}

	protected LWAbstractButton(int style, LWContainer parent) {
		super(parent);
		this.style = style;
	}

	public boolean isSelected() {
		return isFlagSet(FLAG_SELECTED);
	}

	public void setSelected(boolean selected) {
		setFlag(FLAG_SELECTED, selected, EVENT_SELECTED);
	}

	public boolean isHovered() {
		return isFlagSet(FLAG_HOVERED);
	}

	public void setHovered(boolean hovered) {
		setFlag(FLAG_HOVERED, hovered, EVENT_HOVERED);
	}

	public boolean isPressed() {
		return isFlagSet(FLAG_PRESSED);
	}

	public void setPressed(boolean pressed) {
		setFlag(FLAG_PRESSED, pressed, EVENT_PRESSED);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (Objects.equals(text, this.text)) {
			return;
		}

		this.text = text;
		notifyListeners(EVENT_TEXT);
	}

	@Override
	protected void notifyListeners(int type) {
		super.notifyListeners(type);
		redraw();
	}

	@Override
	public void handleEvent(Event e) {
		switch (e.type) {
		case SWT.MouseDown -> {
			if (e.button == 1) {
				setPressed(true);
				redraw();
			}
		}
		case SWT.MouseUp -> {
			if (e.button == 1) {
				setPressed(false);
				handleSelection();
				requestFocus();
			}
		}
		case SWT.MouseEnter -> setHovered(true);
		case SWT.MouseExit -> setHovered(false);
		case SWT.KeyDown -> {
			if (e.keyCode == SWT.SPACE) {
				setPressed(true);
				handleSelection();
				e.doit = false;
			}
		}
		case SWT.KeyUp -> {
			if (e.keyCode == SWT.SPACE) {
				setPressed(false);
				e.doit = false;
			}
		}
		}
	}

	public int getStyle() {
		return style;
	}

	public void setAlignment(int alignment) {
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.CENTER | SWT.RIGHT);
		style |= alignment
		         & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.CENTER | SWT.RIGHT);
		notifyListeners(EVENT_STYLE);
	}

	private boolean isFlagSet(int mask) {
		return (flags & mask) == mask;
	}

	private void setFlag(int mask, boolean set, int eventType) {
		if (set) {
			if ((flags & mask) == mask) {
				return;
			}

			flags |= mask;
		}
		else {
			if ((flags & mask) == 0) {
				return;
			}

			flags &= ~mask;
		}
		notifyListeners(eventType);
	}
}
