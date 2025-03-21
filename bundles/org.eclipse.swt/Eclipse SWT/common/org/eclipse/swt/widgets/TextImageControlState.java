package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class TextImageControlState extends ControlState {

	private String text;
	private Image image;
	private Image disabledImage;

	protected TextImageControlState() {
	}

	public final String getText() {
		return text;
	}

	public final void setText(String text) {
		if (Objects.equals(text, this.text)) {
			return;
		}

		this.text = text;
		propertyChanged();
	}

	public final Image getImage() {
		return image;
	}

	public final void setImage(Image image) {
		if (image == this.image) {
			return;
		}

		if (disabledImage != null) {
			disabledImage.dispose();
			disabledImage = null;
		}

		this.image = image;
		propertyChanged();
	}

	public final Image getImageForDrawing() {
		if (image == null || isEnabled()) {
			return image;
		}

		if (disabledImage == null) {
			disabledImage = new Image(getControl().display, image, SWT.IMAGE_DISABLE);
		}
		return disabledImage;
	}

	public void dispose() {
		text = null;
		setImage(null);
	}
}
