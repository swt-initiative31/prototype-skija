package org.eclipse.swt.widgets;

public final class ButtonState extends TextImageControlState {

	private static final int SELECTED = 1;
	private static final int GRAYED = 2;
	private static final int HOVER = 4;
	private static final int PRESSED = 8;

	private final Button button;

	private int booleanStates;

	public ButtonState(Button button) {
		this.button = button;
	}

	@Override
	protected Control getControl() {
		return button;
	}

	public boolean isSelected() {
		return isBitSet(SELECTED);
	}

	public void setSelected(boolean selected) {
		setBit(selected, SELECTED);
	}

	public boolean isGrayed() {
		return isBitSet(GRAYED);
	}

	public void setGrayed(boolean grayed) {
		setBit(grayed, GRAYED);
	}

	public boolean isHover() {
		return isBitSet(HOVER);
	}

	public void setHover(boolean hover) {
		setBit(hover, HOVER);
	}

	public boolean isPressed() {
		return isBitSet(PRESSED);
	}

	public void setPressed(boolean pressed) {
		setBit(pressed, PRESSED);
	}

	private boolean isBitSet(int mask) {
		return (booleanStates & mask) == mask;
	}

	private void setBit(boolean selected, int mask) {
		if (selected) {
			if ((booleanStates & mask) == mask) {
				return;
			}
			booleanStates |= mask;
		}
		else {
			if ((booleanStates & mask) == 0) {
				return;
			}
			booleanStates &= ~mask;
		}

		propertyChanged();
	}
}
