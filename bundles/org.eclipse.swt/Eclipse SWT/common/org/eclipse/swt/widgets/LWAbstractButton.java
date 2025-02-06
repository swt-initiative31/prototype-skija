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
	public static final int EVENT_HOVERED = EVENT_SELECTED + 1;
	public static final int EVENT_STYLE = EVENT_HOVERED + 1;
	public static final int EVENT_TEXT = EVENT_STYLE + 1;

	protected int style;
	private String text;
	private boolean selected;
	private boolean hovered;

	protected LWAbstractButton(int style) {
		this.style = style;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if (selected == this.selected) {
			return;
		}

		this.selected = selected;
		notifyListeners(EVENT_SELECTED);
	}

	public boolean isHovered() {
		return hovered;
	}

	public void setHovered(boolean hovered) {
		if (hovered == this.hovered) {
			return;
		}
		this.hovered = hovered;
		notifyListeners(EVENT_HOVERED);
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

	public void handleEvent(Event e) {
		switch (e.type) {
		case SWT.MouseUp -> {
			if ((e.stateMask & SWT.BUTTON1) != 0) {
				handleSelection();
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
}
