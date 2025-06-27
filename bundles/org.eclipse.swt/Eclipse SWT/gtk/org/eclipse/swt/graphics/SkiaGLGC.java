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

import org.eclipse.swt.widgets.*;

public final class SkiaGLGC extends AbstractSkiaGC {

	public SkiaGLGC(GC gc, SkiaCanvas src) {
		super(gc, src, src.surface);
	}

	@Override
	public void dispose() {
		if (drawable instanceof SkiaCanvas) {
			glFinishDrawing();
		}
		super.dispose();
	}

	@Override
	public void init(Drawable drawable, GCData data, long hDC) {
		glPrepareSurface();
		super.init(drawable, data, hDC);
	}

	@Override
	protected void glFinishDrawing() {
		getSkiaCanvas().skijaContext.flush();
		getSkiaCanvas().swapBuffers();
	}

	@Override
	protected void glPrepareSurface() {
		io.github.humbleui.skija.Canvas canvas = getSkiaCanvas().surface.getCanvas();
		canvas.clear(0xFFFFFFFF);
	}

	SkiaCanvas getSkiaCanvas() {
		return (SkiaCanvas) drawable;
	}
}
