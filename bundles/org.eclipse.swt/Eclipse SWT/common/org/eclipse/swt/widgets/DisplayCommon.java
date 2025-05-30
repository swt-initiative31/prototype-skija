package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.DeviceData;

public abstract class DisplayCommon extends Device {

Control focusControl;

private ColorProvider colorProvider;
private RendererFactory rendererFactory;

public DisplayCommon(DeviceData data) {
	super(data);

	colorProvider = DefaultColorProvider.createLightInstance();
	rendererFactory = new DefaultRendererFactory();
}

protected Control getFocusControl () {
	if (focusControl != null && focusControl.isDisposed()) {
		focusControl = null;
	}
	return focusControl;
}

/**
 * @noreference this is still experimental API and might be removed
 */
public final RendererFactory getRendererFactory() {
	return rendererFactory;
}

/**
 * @noreference this is still experimental API and might be removed
 */
public final void setRendererFactory(RendererFactory rendererFactory) {
	if (rendererFactory == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.rendererFactory = rendererFactory;
}

/**
 * Return the color provider used for custom-drawn controls.
 * @return a non-null instance of the color provider
 * @noreference this is still experimental API and might be removed
 */
public final ColorProvider getColorProvider() {
	return colorProvider;
}

/**
 * Set the color provider used for custom-drawn controls.
 * @param colorProvider a non-null color provider
 * @noreference this is still experimental API and might be removed
 */
public final void setColorProvider(ColorProvider colorProvider) {
	if (colorProvider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.colorProvider = colorProvider;
	// todo: redraw all (custom-drawn) widgets
}
}
