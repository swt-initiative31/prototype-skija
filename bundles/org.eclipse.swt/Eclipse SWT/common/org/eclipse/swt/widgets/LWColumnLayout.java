package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWColumnLayout implements LWLayoutManager {

	private final Alignment alignment;

	private int marginLeft;
	private int marginTop;
	private int marginRight;
	private int marginBottom;
	private int spacing;

	public LWColumnLayout(Alignment alignment) {
		this.alignment = Objects.requireNonNull(alignment);
	}

	@Override
	public Point getPreferredSize(List<LWControl> children, IMeasureContext measureContext) {
		int maxWidth = 0;
		int heightSum = 0;
		for (LWControl child : children) {
			if (heightSum > 0) {
				heightSum += spacing;
			}
			final Point preferredSize = child.getPreferredSize(measureContext);
			heightSum += preferredSize.y;
			maxWidth = Math.max(preferredSize.x, maxWidth);
		}
		return new Point(marginLeft + maxWidth + marginRight,
		                 marginTop + heightSum + marginBottom);
	}

	@Override
	public void layout(List<LWControl> children, Point size, IMeasureContext measureContext) {
		int maxWidth = 0;
		for (LWControl child : children) {
			final Point preferredSize = child.getPreferredSize(measureContext);
			child.setSize(preferredSize);
			maxWidth = Math.max(preferredSize.x, maxWidth);
		}

		int y = marginTop;
		for (LWControl child : children) {
			final int width = child.getWidth();
			Alignment alignment = this.alignment;
			if (child.getLayoutData() instanceof Alignment controlAlignment) {
				alignment = controlAlignment;
			}
			final int x = switch (alignment) {
				case BEGIN -> 0;
				case CENTER -> (maxWidth - width) / 2;
				case END -> maxWidth - width;
				case FILL -> {
					child.setSize(maxWidth, child.getHeight());
					yield 0;
				}
			} + marginLeft;
			child.setLocation(x, y);
			y += child.getHeight();
			y += spacing;
		}
	}

	public LWColumnLayout setMargin(int all) {
		return setMargin(all, all);
	}

	public LWColumnLayout setMargin(int leftRight, int topBottom) {
		return setMargin(leftRight, topBottom, leftRight, topBottom);
	}

	public LWColumnLayout setMargin(int left, int top, int right, int bottom) {
		marginLeft = left;
		marginTop = top;
		marginRight = right;
		marginBottom = bottom;
		return this;
	}

	public LWColumnLayout setSpacing(int spacing) {
		this.spacing = spacing;
		return this;
	}

	public enum Alignment {
		BEGIN, CENTER, FILL, END
	}
}
