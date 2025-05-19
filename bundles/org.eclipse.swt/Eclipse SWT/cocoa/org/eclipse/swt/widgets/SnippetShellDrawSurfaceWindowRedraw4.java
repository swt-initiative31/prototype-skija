/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.layout.*;

import io.github.humbleui.skija.*;

public class SnippetShellDrawSurfaceWindowRedraw4 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SnippetShellMacNSImage");

		shell.setSize(800, 800);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Canvas c1 = new Canvas(shell, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		c1.setSize(800, 400);
		Canvas c2 = new Canvas(shell, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		c2.setSize(800, 400);

		c1.addListener(SWT.Paint, e -> {

			var img = display.getSystemImage(SWT.ICON_INFORMATION);

			e.gc.drawImage(img, 0, 0);
		});

		var l = new Listener() {

			AtomicReference<Surface> surfaceRef = new AtomicReference<>();
			AtomicReference<NSBitmapImageRep> repRef = new AtomicReference<>();
			NSImage i;

			int width = 100;
			int height = 100;

			Lock l = new ReentrantLock();

			long lastStart = 0;
			long draws;
			long startTime = System.currentTimeMillis();

			@Override
			public void handleEvent(Event e) {

				if (e.type == SWT.Paint) {

					l.lock();

					try {

						if (System.currentTimeMillis() - lastStart > 1000) {
							System.out.println("Frames: " + draws);
							lastStart = System.currentTimeMillis();
							draws = 0;
						}
						draws++;

						long currentPosTime = System.currentTimeMillis() - startTime;

						currentPosTime = currentPosTime % 10000;

						double position = (double) currentPosTime / (double) 10000;

						drawSurface(surfaceRef.get(), position, width, height);
						// The .bitmapData() call is necessary to update NSBitmapImageRep with the
						// pointer data in order to
						// invalidate caches.
						repRef.get().bitmapData();

						pushImage(i, width, height);

						c2.redraw();
						return;
					} finally {
						l.unlock();
					}
				}

				if (e.type == SWT.Resize) {

					l.lock();

					try {

						if (repRef.get() != null) {
							surfaceRef.get().close();
							repRef.get().release();
						}

						createRepresentation(repRef, surfaceRef, width, height);

						i = new NSImage();
						i.alloc();

						NSSize size = new NSSize();
						size.width = width;
						size.height = height;

						i.initWithSize(size);
						i.setCacheMode(OS.NSImageCacheNever);

						i.addRepresentation(repRef.get());

						return;

					} finally {
						l.unlock();
					}

				}

				if (e.type == SWT.Dispose) {
					l.lock();
					try {
						if (repRef.get() != null) {
							surfaceRef.get().close();
							repRef.get().release();
						}
					} finally {
						l.unlock();
					}
				}

			}

			private void drawSurface(Surface surface, double position, int width, int height) {

				int colorAsRGB = 0xFF42FFF4;
				int colorRed = 0xFFFF0000;
				int colorGreen = 0xFF00FF00;
				int colorBlue = 0xFF0000FF;

				try (var paint = new Paint()) {
					paint.setColor(colorRed);

					surface.getCanvas().clear(colorBlue);

					surface.getCanvas().drawCircle((int) (position * width), width / 2, height / 2, paint);

					surface.getCanvas().drawArc(5, 5, 50, 50, 5, 50, true, paint);

					surface.getCanvas().drawLine(5, 5, 100, 5, paint);

					surface.getCanvas().drawLine(5, 5, 5, 100, paint);

				}

			}

			private void pushImage(NSImage i, int width, int height) {

				NSGraphicsContext handle = NSGraphicsContext.currentContext();
				handle.saveGraphicsState();

				NSRect srcRect = new NSRect();
				srcRect.x = 0;
				srcRect.y = 0;
				srcRect.width = width;
				srcRect.height = height;
				NSRect destRect = new NSRect();
				destRect.x = 0;
				destRect.y = 0;
				destRect.width = srcRect.width;
				destRect.height = srcRect.height;

				i.drawInRect(destRect, srcRect, OS.NSCompositeSourceOver, 1);

				handle.restoreGraphicsState();

			}
		};

		c2.addListener(SWT.Resize, l);

		c2.addListener(SWT.Paint, l);

		shell.open();
		while (!shell.isDisposed()) {
			c1.redraw();
			c2.redraw();
			display.readAndDispatch();
		}
		display.dispose();
	}

	public static void createRepresentation(AtomicReference<NSBitmapImageRep> repRef, AtomicReference<Surface> surfRef,
			int width, int height) {

		NSBitmapImageRep rep = (NSBitmapImageRep) new NSBitmapImageRep().alloc();

		repRef.set(rep);

		boolean hasAlpha = true;

		rep = rep.initWithBitmapDataPlanes(0, width, height, 8, hasAlpha ? 4 : 3, hasAlpha, false,
				OS.NSDeviceRGBColorSpace, OS.NSAlphaNonpremultipliedBitmapFormat, width * 4, 32);

		long pointer = rep.bitmapData();

		Pixmap p;

		ImageInfo info = new ImageInfo(new ColorInfo(ColorType.RGBA_8888, ColorAlphaType.UNPREMUL, null), width,
				height);

		p = Pixmap.make(info, pointer, 4 * width);
		Surface surface = Surface.makeRasterDirect(p);
		surfRef.set(surface);
		surface.getCanvas().scale(1, -1);
		surface.getCanvas().translate(0, -height);

	}

}
