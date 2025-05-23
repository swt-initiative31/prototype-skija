/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class TextLayoutWin32Tests {
	final static String text = "This is a text for testing.";

	@Test
	public void testGetBoundPublicAPIshouldReturnTheSameValueRegardlessOfZoomLevel() {
		Display display = Display.getDefault();

		final TextLayout layout = new TextLayout(display);
		GCData unscaledData = new GCData();
		unscaledData.nativeZoom = DPIUtil.getNativeDeviceZoom();
		GC gc = GC.win32_new(display, unscaledData);
		layout.draw(gc, 10, 10);
		Rectangle unscaledBounds = layout.getBounds();

		int scalingFactor = 2;
		int newZoom = DPIUtil.getNativeDeviceZoom() * scalingFactor;
		GCData scaledData = new GCData();
		scaledData.nativeZoom = newZoom;
		GC scaledGc = GC.win32_new(display, scaledData);
		layout.draw(scaledGc, 10, 10);
		Rectangle scaledBounds = layout.getBounds();

		assertEquals(unscaledBounds, scaledBounds, "The public API for getBounds should give the same result for any zoom level");
	}

	@Test
	@Disabled("Not working (yet)")
	public void testCalculateGetBoundsWithVerticalIndent() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);

		TextLayout layout = new TextLayout(display);
		layout.setVerticalIndent(16);
		layout.setText(text);
		Rectangle unscaledBounds = layout.getBounds();

		int scalingFactor = 2;
		int newZoom = DPIUtil.getNativeDeviceZoom() * scalingFactor;
		DPITestUtil.changeDPIZoom(shell, newZoom);
		TextLayout scaledLayout = new TextLayout(display);
		scaledLayout.setVerticalIndent(16);
		scaledLayout.setText(text);
		Rectangle scaledBounds = scaledLayout.getBounds();

		assertNotEquals(layout.nativeZoom, scaledLayout.nativeZoom, "The native zoom for the TextLayouts must differ");
		assertEquals(unscaledBounds.height, scaledBounds.height, 1, "The public API for getBounds with vertical indent > 0 should give a similar result for any zoom level");
	}

}
