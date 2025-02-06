package org.eclipse.swt.widgets;

import java.util.List;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWRowLayout implements LWLayoutManager {

	@Override
	public Point getPreferredSize(List<LWControl> children, IMeasureContext measureContext) {
		int widthSum = 0;
		int maxHeight = 0;
		for (LWControl child : children) {
			final Point preferredSize = child.getPreferredSize(measureContext);
			widthSum += preferredSize.x;
			maxHeight = Math.max(preferredSize.y, maxHeight);
		}
		return new Point(widthSum, maxHeight);
	}

	@Override
	public void layout(List<LWControl> children, Point size, IMeasureContext measureContext) {
		int maxHeight = 0;
		for (LWControl child : children) {
			final Point preferredSize = child.getPreferredSize(measureContext);
			child.setSize(preferredSize);
			maxHeight = Math.max(preferredSize.y, maxHeight);
		}

		int x = 0;
		for (LWControl child : children) {
			final int height = child.getHeight();
			final int y = (maxHeight - height) / 2;
			child.setLocation(x, y);
			x += child.getWidth();
		}
	}
}
