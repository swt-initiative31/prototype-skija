/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.

 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.widgets.*;

import io.github.humbleui.skija.*;

public final class SkiaRasterGC extends AbstractSkiaGC {

	private final long cairoSurface;

	public SkiaRasterGC(GC gc, SkiaRasterCanvas src, Surface surface, long cairoSurface) {
		super(gc, src, surface);
		this.cairoSurface = cairoSurface;
	}

	@Override
	public void dispose() {
		var cairo = getGCData().cairo;
		Cairo.cairo_set_source_surface(cairo, cairoSurface, 0, 0);
		Cairo.cairo_paint(cairo);
		Cairo.cairo_surface_flush(cairoSurface);
		Cairo.cairo_surface_flush(cairo);
		super.dispose();
	}

}
