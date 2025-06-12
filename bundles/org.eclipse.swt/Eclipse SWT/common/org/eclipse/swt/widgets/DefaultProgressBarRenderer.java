package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefaultProgressBarRenderer extends ProgressBarRenderer {

	private ProgressBar progressBar;

	protected DefaultProgressBarRenderer(ProgressBar progressBar) {
		super(progressBar);
		this.progressBar = progressBar;

	}

	@Override
	public void paint(GC gc, int width, int height) {

		drawRectangle(gc, width, height);
		drawSelection(gc, width, height);

	}

	private void drawSelection(GC gc, int width, int height) {

		var prevBG = gc.getBackground();
		var b = new Rectangle(0, 0, width, height);
		gc.setBackground(progressBar.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		gc.fillRectangle(b.x + 1, b.y + 1, b.width - 2, b.height - 2);
		gc.setBackground(progressBar.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		if (progressBar.getState() == SWT.PAUSED)
			gc.setBackground(progressBar.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		else if (progressBar.getState() == SWT.ERROR) {
			gc.setBackground(progressBar.getDisplay().getSystemColor(SWT.COLOR_RED));
		}

		if ((SWT.INDETERMINATE & progressBar.getStyle()) != 0) {

			int middleHeight = b.height / 2;
			int middleWidth  = b.width / 2;

			gc.fillRectangle(new Rectangle(b.x + 1, b.y + middleHeight - 1, b.width - 2, 2));

			if (b.width > 50) {
				drawDiamond(gc, b, b.width / 4);
				drawDiamond(gc, b, b.width / 2);
				drawDiamond(gc, b, b.width / 4 + b.width / 2);
			}

		} else if ((progressBar.getStyle() & SWT.HORIZONTAL) != 0) {

			int value = progressBar.getSelection();
			int min = progressBar.getMinimum();
			int max = progressBar.getMaximum();

			int completeDiff = max - min;
			var diff = value - min;


			int pixelWidth = (int) ((b.width - 2) * (((double) diff) / (double) (completeDiff)));


			gc.fillRectangle(new Rectangle(b.x + 1, b.y + 1, pixelWidth, b.height - 2));

		} else if ((progressBar.getStyle() & SWT.VERTICAL) != 0) {

			int value = progressBar.getSelection();
			int min = progressBar.getMinimum();
			int max = progressBar.getMaximum();

			int completeDiff = max - min;

			var diff = value - min;

			int pixelheight = (int) ((b.height - 2) * (((double) diff) / (double) (completeDiff)));


			gc.fillRectangle(
					new Rectangle(b.x + 1, b.y + 1 + ((b.height - 2) - pixelheight), b.width - 2, pixelheight));

		}

		gc.setBackground(prevBG);

	}

	private void drawDiamond(GC gc, Rectangle b, int position) {

		int middleHeight = b.height / 2;

		// draw a diamond
		gc.fillPolygon(new int[] { b.x + position - 5, b.y + middleHeight, // left of center
				b.x + position, b.y, // middle up
				b.x + position + 5, b.y + middleHeight, // right of center
				b.x + position, b.y + b.height, // middle down
		});

	}

	private void drawRectangle(GC gc, int width, int height) {

		var b = new Rectangle(0, 0, width, height);
		var prevFG = gc.getForeground();

		gc.setForeground(progressBar.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		gc.drawRectangle(b);

		gc.setForeground(prevFG);

	}

	@Override
	public Point computeDefaultSize() {

		return new Point(30, 30);

	}

}
