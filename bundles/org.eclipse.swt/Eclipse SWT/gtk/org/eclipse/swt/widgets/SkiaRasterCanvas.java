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

import java.util.*;
import java.util.concurrent.locks.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;

import io.github.humbleui.skija.*;

public class SkiaRasterCanvas extends Canvas {

	public SkiaRasterCanvas(Composite parent, int style) {
		super(parent, style);

		// TODO make sure this works really really reliable, take care of the memory
		// handling!!
		this.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				lock.lock();
				setupSurface(e);
				lock.unlock();
			}
		});
	}

	private long cairoSurface;
	private Surface surface;
	private final Lock lock = new ReentrantLock();
	private Map<Long, Long> cairoSurfaceCleanup = new HashMap<>();
	private Pixmap p;
	private long pointer;

	void setupSurface(ControlEvent e) {
		if (surface != null && !surface.isClosed())
			surface.close();

		if (p != null && !p.isClosed())
			p.close();

		if (this.pointer != 0) {
			Cairo.cairo_surface_destroy(cairoSurface);
			cairoSurface = 0;
			C.free(this.pointer);
			this.pointer = 0;
		}

		var s = this.getSize();
		int width = s.x;
		int height = s.y;

		int size = width * height * 4;
		this.pointer = C.malloc(size);

		var info = ImageInfo.makeN32(width, height, ColorAlphaType.UNPREMUL);
		// this.buffer = ByteBuffer.allocateDirect(size);
		this.p = Pixmap.make(info, this.pointer, 4 * width);
		surface = Surface.makeRasterDirect(p);

		System.out.println("CairoSurface: create " + System.currentTimeMillis());
		cairoSurface = Cairo.cairo_image_surface_create_for_data(this.pointer, Cairo.CAIRO_FORMAT_ARGB32, width, height,
				4 * width);
		cleanupOldCairoSurface();
	}

	private void cleanupOldCairoSurface() {
		long current = System.currentTimeMillis();
		java.util.List<Long> remove = new ArrayList<>();

		for (var e : cairoSurfaceCleanup.entrySet()) {
			if (current - e.getKey() > 100) {
				Cairo.cairo_destroy(e.getKey());
				remove.add(e.getKey());
			}
		}
		remove.forEach(e -> cairoSurfaceCleanup.remove(e));
	}

	public GC createGC(GC innerGC) {
		surface.getCanvas().clear(0xFFFFFFFF);
		return new SkiaRasterGC(innerGC, this, surface, cairoSurface);
	}

}
