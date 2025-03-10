package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class ButtonRenderer extends ControlRenderer {

	private final Button button;

	private String text = "";
	private Image image;
	private Image disabledImage;
	private boolean selected;
	private boolean pressed;
	private boolean hover;

	public ButtonRenderer(Button button) {
		super(button);
		this.button = button;
	}

	public final String getText() {
		return text;
	}

	public final void setText(String text) {
		this.text = text;
	}

	public final Image getImage() {
		return image;
	}

	public final void setImage(Image image) {
		if (image == this.image) {
			return;
		}

		if (this.disabledImage != null) {
			this.disabledImage.dispose();
			this.disabledImage = null;
		}
		this.image = image;
	}

	public final boolean isSelected() {
		return selected;
	}

	public final void setSelected(boolean checked) {
		this.selected = checked;
	}

	public final boolean isHover() {
		return hover;
	}

	public final void setHover(boolean hover) {
		this.hover = hover;
	}

	public final boolean isPressed() {
		return pressed;
	}

	public final void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	protected final void drawImage(GC gc, int x, int y) {
		if (isEnabled()) {
			gc.drawImage(image, x, y);
		}
		else {
			if (disabledImage == null) {
				disabledImage = new Image(button.getDisplay(), image,
						SWT.IMAGE_GRAY);
			}
			gc.drawImage(disabledImage, x, y);
		}
	}

	protected final boolean hasBorder() {
		final int style = getStyle();
		return (style & SWT.BORDER) != 0;
	}

	protected final boolean hasFocus() {
		return button.hasFocus();
	}
}
