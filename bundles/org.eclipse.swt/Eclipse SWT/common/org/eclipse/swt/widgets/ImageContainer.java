package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Handles an image and its disabled variant.
 */
public class ImageContainer {

	private final Display display;

	private Image image;
	private Image imageDisabled;

	public ImageContainer(Image image, Display display) {
		if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (display == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

		this.image = image;
		this.display = display;
	}

	public Image getImage() {
		return image;
	}

	public Point getSize() {
		final Rectangle bounds = image.getBounds();
		return new Point(bounds.width, bounds.height);
	}

	public void draw(GC gc, int x, int y, boolean enabled) {
		final Image image = getImage(enabled);
		gc.drawImage(image, x, y);
	}

	public void draw(GC gc,
			int srcX, int srcY, int srcWidth, int srcHeight,
			int destX, int destY, int destWidth, int destHeight,
			boolean enabled) {
		final Image image = getImage(enabled);
		gc.drawImage(image,
				srcX, srcY, srcWidth, srcHeight,
				destX, destY, destWidth, destHeight);
	}

	public void disposeDisabled() {
		if (imageDisabled != null) {
			imageDisabled.dispose();
			imageDisabled = null;
		}
		image = null;
	}

	private Image getImage(boolean enabled) {
		if (enabled) {
			return image;
		}

		if (imageDisabled == null) {
			imageDisabled = new Image(display, image, SWT.IMAGE_DISABLE);
		}
		return imageDisabled;
	}
}
