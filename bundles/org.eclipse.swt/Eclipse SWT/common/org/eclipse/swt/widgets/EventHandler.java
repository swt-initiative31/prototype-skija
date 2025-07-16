package org.eclipse.swt.widgets;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.*;

import io.github.humbleui.skija.*;

public class EventHandler implements Listener {

	private Composite composite;
	int[] events = new int[] { SWT.Paint, SWT.MouseMove, SWT.MouseEnter, SWT.MouseExit, SWT.Dispose, SWT.MouseDown,
			SWT.MouseUp };

	public EventHandler(Composite c) {
		this.composite = c;

		for (int i : events) {
			this.composite.addListener(i, this);
		}

	}

	public void handleEvent(Event e) {
		switch (e.type) {
		case SWT.Paint:
			onPaint(e);
			break;
		case SWT.MouseMove:
			onMouseMove(e);
			break;
		case SWT.Dispose:
			onDispose(e);
			break;
		case SWT.MouseDown:
			onMouseDown(e);
			break;
		case SWT.MouseUp:
			onMouseUp(e);
		}

	}

	private void onMouseUp(Event e) {
		Composite s = (Composite) e.widget;

		var chs = s.getChildren();

		for (var c : chs) {

			if (c instanceof CustomControl cc) {
				if (cc.getBounds().contains(e.x, e.y)) {
					var ev = createChildEvent(e, cc);
					cc.sendEvent(ev);
				}
			}

		}

	}

	private void onMouseDown(Event e) {

		Composite s = (Composite) e.widget;

		var chs = s.getChildren();

		for (var c : chs) {

			if (c instanceof CustomControl cc) {
				if (cc.getBounds().contains(e.x, e.y)) {
					var ev = createChildEvent(e, cc);
					cc.sendEvent(ev);
				}
			}

		}

	}

	private void onDispose(Event e) {

		for (int i : events) {
			this.composite.removeListener(i, this);
		}

	}

	private void onMouseMove(Event event) {

		Composite s = (Composite) event.widget;

		var chs = s.getChildren();
		boolean elementHovered = false;

		for (var c : chs) {
			if (c instanceof CustomControl cc) {
				if (cc.getBounds().contains(event.x, event.y)) {
					elementHovered = true;
					if (hoverControl != cc) {
						if (hoverControl != null && !hoverControl.isDisposed()) {
							var e = createChildEvent(event, cc);
							e.type = SWT.MouseExit;
							hoverControl.sendEvent(e);
						}

						var e = createChildEvent(event, cc);
						e.type = SWT.MouseEnter;
						cc.sendEvent(e);
						hoverControl = cc;
						hoverControl = cc;
					}

					var e = createChildEvent(event, cc);
					cc.sendEvent(e);
				}
			}
		}

		if (!elementHovered && hoverControl != null && !hoverControl.isDisposed()) {
			var e = createChildEvent(event, hoverControl);
			e.type = SWT.MouseExit;
			hoverControl.sendEvent(e);
			hoverControl = null;
		}

	}

	private void onPaint(Event event) {
		Composite s = (Composite) event.widget;

		var chs = s.getChildren();

		for (var c : chs) {
			if (c instanceof CustomControl cc) {
				var e = createChildEvent(event, cc);
				cc.sendEvent(e);
				transferImage(event, e, cc);
			}
		}
	}

	CustomControl hoverControl = null;

	private static void transferImage(Event event, Event e, CustomControl cc) {

		var l = cc.getLocation();

		SkijaGC sgc = (SkijaGC) e.gc.innerGC;

		if (sgc.isEmpty(sgc.originalDrawingSize)) {
			return;
		}

		io.github.humbleui.skija.Image im = sgc.surface.makeImageSnapshot();
		byte[] imageBytes = EncoderPNG.encode(im).getBytes();

		Image transferImage = new Image(event.display, new ByteArrayInputStream(imageBytes));

		Point drawingSizeInPixels = DPIUtil.autoScaleUp(sgc.originalDrawingSize);
		event.gc.drawImage(transferImage, 0, 0, drawingSizeInPixels.x, drawingSizeInPixels.y, //
				l.x, l.y, sgc.originalDrawingSize.x, sgc.originalDrawingSize.y);
		transferImage.dispose();
		sgc.surface.close();

	}

	private static Event createChildEvent(Event event, CustomControl cc) {

		Event e = new Event();

		var b = cc.getBounds();

		e.widget = cc;
		e.width = b.width;
		e.height = b.height;
		e.display = cc.getDisplay();
		e.type = event.type;
		e.button = event.button;

		e.x = event.x - b.x;
		e.y = event.y - b.y;

		e.x = Math.max(0, e.x);
		e.y = Math.max(0, e.y);

		if (e.type == SWT.Paint) {
			GC gc = new GC();
			gc.innerGC = new SkijaGC(cc);
			e.gc = gc;
		}

		return e;
	}

}
