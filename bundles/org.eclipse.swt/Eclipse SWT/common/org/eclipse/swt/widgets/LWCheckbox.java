package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWCheckbox extends LWAbstractButton {

	private static final int EVENT_GRAYED = LWAbstractButton.EVENT_NEXT;

	private boolean grayed;
	private LWCheckboxUI ui;

	public LWCheckbox(int style) {
		super(style);
		ui = new LWCheckboxUI(this);
	}

	@Override
	public Point getPreferredSize(IMeasureContext measureContext) {
		return ui.getPreferredSize(measureContext);
	}

	@Override
	public void paint(GC gc, IColorProvider colorProvider) {
		ui.paint(gc, colorProvider);
	}

	@Override
	protected void handleSelection() {
		setSelected(!isSelected());
	}

	@Override
	public String getAccessibleText() {
		return getText();
	}

	public boolean isGrayed() {
		return grayed;
	}

	public void setGrayed(boolean grayed) {
		if (grayed == this.grayed) {
			return;
		}
		this.grayed = grayed;
		notifyListeners(EVENT_GRAYED);
	}
}
