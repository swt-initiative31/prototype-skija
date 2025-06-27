/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.

 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.opengl.*;

import io.github.humbleui.skija.*;

public class SkiaCanvas extends GLCanvas {

	public DirectContext skijaContext;
	public Surface surface;

	public SkiaCanvas(Composite parent, int style) {
		this(parent, style, createGLData());
	}

	private static GLData createGLData() {
		GLData data = new GLData();
		data.doubleBuffer = true;
		return data;
	}

	public SkiaCanvas(Composite parent, int style, GLData data) {
		super(parent, style, data);
		state |= FOREIGN_HANDLE;
		setCurrent();
		skijaContext = DirectContext.makeGL();
		addListener(SWT.Resize, this::onResize);
	}

	private void onResize(Event e) {
		Rectangle rect = getClientArea();

		BackendRenderTarget renderTarget = BackendRenderTarget.makeGL(rect.width, rect.height, /* samples */0,
				/* stencil */8, /* fbid */0, FramebufferFormat.GR_GL_RGBA8);

		System.out.println("CreateOpenGLRenderTarget");

		if (surface != null && !surface.isClosed()) {
			surface.close();
		}

		surface = Surface.makeFromBackendRenderTarget(skijaContext, renderTarget, SurfaceOrigin.BOTTOM_LEFT,
				SurfaceColorFormat.RGBA_8888, ColorSpace.getDisplayP3(), new SurfaceProps(PixelGeometry.RGB_H));
		surface.getCanvas().clear(0xFFFFFFFF);

		skijaContext.flush();
		redraw();
	}

	public GC createGC(GC innerGC) {
		return new SkiaGLGC(innerGC, this);
	}

}
