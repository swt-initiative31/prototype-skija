package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWButton extends LWAbstractButton {

	private static final int EVENT_IMAGE = LWAbstractButton.EVENT_NEXT;

	private Image image;
	private Image disabledImage;
	private LWButtonUI ui;

	public LWButton(int style) {
		super(style);
		ui = new LWButtonUI(this);
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
		return getText() + " button.\r\n To activate press space bar.";
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		if (image == this.image) {
			return;
		}
		this.image = image;
		notifyListeners(EVENT_IMAGE);
	}

	public void setDisabledImage(Image disabledImage) {
		if (disabledImage == this.disabledImage) {
			return;
		}
		this.disabledImage = disabledImage;
		notifyListeners(EVENT_IMAGE);
	}

	public Image getDisabledImage() {
		return disabledImage;
	}
}
