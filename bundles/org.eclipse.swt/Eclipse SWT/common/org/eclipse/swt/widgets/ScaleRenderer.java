package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Scale.*;

class ScaleRenderer implements IScaleRenderer {
	private static final Color IDLE_COLOR = new Color(Display.getDefault(), 0, 95, 184);
	private static final Color HOVER_COLOR = new Color(Display.getDefault(), 0, 0, 0);
	private static final Color DRAG_COLOR = new Color(Display.getDefault(), 204, 204, 204);

	private static Color background;

	private final Scale scale;

	private double ppu;

	private Rectangle handleBounds;
	private Rectangle bar;

	ScaleRenderer(Scale scale) {
		this.scale = scale;
	}

	@Override
	public void render(GC gc, Rectangle bounds) {
		initBackground(gc, bounds);

		GcLifeCycle gcLifeCylce = createLifeCycle();
		IGraphicsContext graphicalContext = gcLifeCylce.init(gc, bounds);

		renderScale(graphicalContext, 0, 0, bounds.width - 1, bounds.height - 1);

		gcLifeCylce.commit();
	}

	private void initBackground(GC originalGC, Rectangle bounds) {
		if (SWT.getPlatform().equals("win32")) {
			// Extract background color on first execution
			if (background == null) {
				Image backgroundColorImage = new Image(scale.getDisplay(), bounds.width, bounds.height);
				originalGC.copyArea(backgroundColorImage, 0, 0);
				int pixel = backgroundColorImage.getImageData().getPixel(0, 0);
				backgroundColorImage.dispose();
				background = new Color((pixel & 0xFF000000) >>> 24, (pixel & 0xFF0000) >>> 16, (pixel & 0xFF00) >>> 8);
			}
			scale.style |= SWT.NO_BACKGROUND;
		}
	}

	private void renderScale(IGraphicsContext gc, int x, int y, int w, int h) {
		int fix = getGCCorrectionValue();

		int value = scale.getSelection();
		int min = scale.getMinimum();
		int max = scale.getMaximum();
		int units = Math.max(1, max - min);
		int effectiveValue = Math.min(max, Math.max(min, value));

		// draw background
		if (background != null) {
			gc.setBackground(background);
			gc.fillRectangle(max, y, w - fix, h - fix);
		}

		int firstNotch;
		int lastNotch;

		if (isVertical()) {
			bar = new Rectangle(x + 19, y + 8, 4 - fix, h - (y + 8) - 7 - fix);
			firstNotch = bar.y + 5;
			lastNotch = bar.y + bar.height - 5;
		} else {
			bar = new Rectangle(x + 8, y + 19, w - (x + 8) - 7 - fix, 4 - fix);
			firstNotch = bar.x + 5;
			lastNotch = bar.x + bar.width - 5;
		}

		gc.fillRectangle(bar);
		gc.setForeground(scale.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		gc.drawRectangle(bar);

		// prepare for line drawing
		gc.setForeground(scale.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		gc.setLineWidth(1);
		gc.setLineWidth(1);

		// draw first and last notch
		drawNotch(gc, firstNotch, 4);
		drawNotch(gc, lastNotch, 4);

		// draw center notches
		int unitPerPage = scale.getPageIncrement();
		double totalPixel = lastNotch - firstNotch;
		ppu = totalPixel / units;
		drawCenterNotches(gc, firstNotch, lastNotch, units, unitPerPage, totalPixel);

		drawHande(gc, effectiveValue);
	}

	private void drawCenterNotches(IGraphicsContext gc, int firstNotchPos, int lastNotchPos, int units, int unitPerPage,
			double scalePixels) {
		if (isRTL() && !isVertical()) {
			for (int i = unitPerPage; i < units; i += unitPerPage) {
				int position = lastNotchPos - (int) (i * ppu);
				drawNotch(gc, position, 3);
			}
		} else { // SWT.LEFT_TO_RIGHT or SWT.VERTICAL
			for (int i = unitPerPage; i < units; i += unitPerPage) {
				int position = firstNotchPos + (int) (i * ppu);
				drawNotch(gc, position, 3);
			}
		}
	}

	private void drawHande(IGraphicsContext gc, int value) {
		// draw handle
		Color handleColor = switch (scale.getHandleState()) {
		case IDLE -> IDLE_COLOR;
		case HOVER -> HOVER_COLOR;
		case DRAG -> DRAG_COLOR;
		};
		gc.setBackground(handleColor);
		handleBounds = calculateHandleBounds(value);
		gc.fillRectangle(handleBounds);
	}

	private Rectangle calculateHandleBounds(int value) {
		int pixelValue = (int) (ppu * value);
		if (isVertical()) {
			return new Rectangle(bar.x - 9, bar.y + pixelValue, 21, 10);
		} else if (isRTL()) {
			return new Rectangle(bar.x + bar.width - pixelValue - 10, bar.y - 9, 10, 21);
		} else {
			return new Rectangle(bar.x + pixelValue, bar.y - 9, 10, 21);
		}
	}

	@Override
	public int handlePosToValue(Point pos) {
		if (isVertical()) {
			return (int) Math.round((pos.y - bar.y) / ppu);
		} else {
			return (int) Math.round((pos.x - bar.x) / ppu);
		}
	}

	private boolean isRTL() {
		return scale.getOrientation() == SWT.RIGHT_TO_LEFT;
	}

	private boolean isVertical() {
		return scale.getAlignement() == SWT.VERTICAL;
	}

	private void drawNotch(IGraphicsContext gc, int pos, int size) {
		if (isVertical()) {
			gc.drawLine(bar.x - 10 - size, pos, bar.x - 10, pos);
			gc.drawLine(bar.x + 14, pos, bar.x + 14 + size, pos);
		} else {
			gc.drawLine(pos, bar.y - 10 - size, pos, bar.y - 10);
			gc.drawLine(pos, bar.y + 14, pos, bar.y + 14 + size);
		}
	}

	// TODO move this into the GC itself
	private int getGCCorrectionValue() {
		return SWT.USE_SKIJA ? 0 : 1;
	}

	/*
	 * If in RTL mode, the points of events are relative to the top right corner of
	 * the widget. Since everything else is still relative to the top left, we
	 * mirror vertically.
	 */
	private Point mirrorVertically(Point p) {
		return new Point(scale.getBounds().width - p.x, p.y);
	}

	@Override
	public boolean isWithinHandle(Point position) {
		if (isRTL()) {
			position = mirrorVertically(position);
		}
		return handleBounds.contains(position);
	}

	@Override
	public boolean isAfterHandle(Point position) {
		if (isVertical()) {
			return position.y > handleBounds.y + handleBounds.height;
		} else if (isRTL()) {
			position = mirrorVertically(position);
			return position.x < handleBounds.x;
		} else {
			return position.x > handleBounds.x + handleBounds.width;
		}
	}

	@Override
	public boolean isBeforeHandle(Point position) {
		if (isVertical()) {
			return position.y < handleBounds.y;
		} else if (isRTL()) {
			position = mirrorVertically(position);
			return position.x > handleBounds.x + handleBounds.width;
		} else {
			return position.x < handleBounds.x;
		}
	}

	private GcLifeCycle createLifeCycle() {
		if (SWT.USE_SKIJA) {
			return new SkijaLifeCycle();
		} else {
			return new NaticeLifeCycle();
		}
	}

	/*
	 * XXX: GcLifeCycle is used to abstract away IGraphicsContext implementation
	 * specific stuff. As soon as we only have Skija, this would be removed.
	 */
	private interface GcLifeCycle {
		IGraphicsContext init(GC originalGC, Rectangle bounds);

		void commit();
	}

	private class SkijaLifeCycle implements GcLifeCycle {
		IGraphicsContext gc;

		@Override
		public IGraphicsContext init(GC originalGC, Rectangle bounds) {
			gc = new SkijaGC(originalGC, background);

			originalGC.setClipping(bounds.x, bounds.y, bounds.width, bounds.height);

			originalGC.setForeground(scale.getForeground());
			originalGC.setBackground(scale.getBackground());
			originalGC.setClipping(new Rectangle(0, 0, bounds.width, bounds.height));
			originalGC.setAntialias(SWT.ON);

			return gc;
		}

		@Override
		public void commit() {
			gc.commit();
			gc.dispose();
		}
	}

	private class NaticeLifeCycle implements GcLifeCycle {
		private Image doubleBufferingImage;

		private GC originalGC;
		private GC bufferGC;

		@Override
		public IGraphicsContext init(GC originalGC, Rectangle bounds) {
			if (SWT.getPlatform().equals("win32")) {
				// Use double buffering on windows
				this.originalGC = originalGC;
				doubleBufferingImage = new Image(scale.getDisplay(), bounds.width, bounds.height);
				originalGC.copyArea(doubleBufferingImage, 0, 0);
				GC doubleBufferingGC = new GC(doubleBufferingImage);
				doubleBufferingGC.setForeground(originalGC.getForeground());
				doubleBufferingGC.setBackground(background);
				doubleBufferingGC.setAntialias(SWT.ON);
				doubleBufferingGC.fillRectangle(0, 0, bounds.width, bounds.height);
				bufferGC = doubleBufferingGC;
			}

			originalGC.setClipping(bounds.x, bounds.y, bounds.width, bounds.height);

			originalGC.setForeground(scale.getForeground());
			originalGC.setBackground(scale.getBackground());
			originalGC.setClipping(new Rectangle(0, 0, bounds.width, bounds.height));
			originalGC.setAntialias(SWT.ON);

			return bufferGC;
		}

		@Override
		public void commit() {
			bufferGC.commit();
			bufferGC.dispose();
			if (doubleBufferingImage != null) {
				originalGC.drawImage(doubleBufferingImage, 0, 0);
				doubleBufferingImage.dispose();
			}
			originalGC.dispose();
		}
	}
}
