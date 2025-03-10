package org.eclipse.swt.widgets;

public abstract class CheckboxRenderer extends ButtonRenderer {

	private boolean grayed;

	public CheckboxRenderer(Button button) {
		super(button);
	}

	public final boolean isGrayed() {
		return grayed;
	}

	public final void setGrayed(boolean grayed) {
		this.grayed = grayed;
	}
}
