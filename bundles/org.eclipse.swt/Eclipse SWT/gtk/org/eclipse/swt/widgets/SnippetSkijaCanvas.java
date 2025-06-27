package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class SnippetSkijaCanvas {

	static final int RECTANGLES_PER_LINE = 100;

	static record RecDraw(int xPos, int yPos, Color c) {
	}

	private static final RecDraw[][] recDraws = new RecDraw[RECTANGLES_PER_LINE][RECTANGLES_PER_LINE];
	private static long minFrameRate = Long.MAX_VALUE;
	private static long maxFrameRate = 0;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 1");

		shell.setLayout(new FillLayout());

		Canvas c = new SkiaCanvas(shell, SWT.FILL | SWT.DOUBLE_BUFFERED);

		for (int x = 0; x < RECTANGLES_PER_LINE; x++) {
			for (int y = 0; y < RECTANGLES_PER_LINE; y++) {
				recDraws[x][y] = new RecDraw(x * 2, y * 2, Display.getDefault().getSystemColor((x + y) % 16));
			}
		}

		c.setSize(100, 100);

		shell.addListener(SWT.Resize, e -> onResize(e, c));
		c.addListener(SWT.Paint, e -> onPaint(e));
		c.addListener(SWT.Paint, e -> onPaint2(e));

		shell.setSize(1000, RECTANGLES_PER_LINE * 3 + 80);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static long startTime = System.currentTimeMillis();
	private static boolean printFrameRate = true;
	private static int frames;
	private static long lastFrame;
	private static long framesToDraw;

	private static void onPaint(Event e) {
		var s = ((Canvas) e.widget);

		Point size = s.getSize();
		long currentPosTime = System.currentTimeMillis() - startTime;
		currentPosTime = currentPosTime % 10000;

		double position = (double) currentPosTime / (double) 10000;
		int shift = (int) (position * size.x);
		int shiftDown = 20;

		for (int x = 0; x < RECTANGLES_PER_LINE; x++) {
			for (int y = 0; y < RECTANGLES_PER_LINE; y++) {
				var rec = recDraws[x][y];
				e.gc.setForeground(rec.c);
				e.gc.drawRectangle(shift + rec.xPos, shiftDown + rec.yPos, 2, 2);

			}
		}
	}

	private static void onPaint2(Event e) {
		var s = ((Canvas) e.widget);

		if (printFrameRate) {
			if (System.currentTimeMillis() - lastFrame > 1000) {
				framesToDraw = frames;
				frames = 0;
				lastFrame = System.currentTimeMillis();
			}
			frames++;
			if(framesToDraw != 0) {
				minFrameRate = Math.min(minFrameRate, framesToDraw);
			}
			maxFrameRate = Math.max(maxFrameRate, framesToDraw);
			e.gc.drawText("Frames: min: " + minFrameRate + ", max: " + maxFrameRate + " cur: " + framesToDraw, 10, 10);
		}
		s.redraw();
	}

	private static void onResize(Event e, Canvas c) {
		var ca = c.getShell().getClientArea();
		c.setSize(ca.width, ca.height);
	}

}
