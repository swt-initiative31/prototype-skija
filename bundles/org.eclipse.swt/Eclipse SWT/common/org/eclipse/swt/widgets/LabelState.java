package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public final class LabelState extends TextImageControlState {

	static final int DEFAULT_MARGIN = 3;

	private final Label label;

	private int align;

	private Image backgroundImage;
	private Color[] gradientColors;
	private int[] gradientPercents;
	private boolean gradientVertical;

	private int leftMargin = DEFAULT_MARGIN;
	private int topMargin = DEFAULT_MARGIN;
	private int rightMargin = DEFAULT_MARGIN;
	private int bottomMargin = DEFAULT_MARGIN;

	public LabelState(Label label) {
		this.label = label;
	}

	@Override
	protected Control getControl() {
		return label;
	}

	public int getAlign() {
		return align;
	}

	public void setAlign(int align) {
		if (align == this.align) {
			return;
		}

		this.align = align;
		propertyChanged();
	}

	public void setBackground(Color color) {
		backgroundImage = null;
		gradientColors = null;
		gradientPercents = null;
		super.setBackground(color);
	}

	public Color[] getGradientColors() {
		return gradientColors;
	}

	public int[] getGradientPercents() {
		return gradientPercents;
	}

	public boolean isGradientVertical() {
		return gradientVertical;
	}

	public void setBackground(Color[] colors, int[] percents, boolean vertical) {
		if (colors != null) {
			if (percents == null || percents.length != colors.length - 1) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (label.getDisplay().getDepth() < 15) {
				// Don't use gradients on low color displays
				colors = new Color[]{colors[colors.length - 1]};
				percents = new int[]{};
			}
			for (int i = 0; i < percents.length; i++) {
				if (percents[i] < 0 || percents[i] > 100) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (i > 0 && percents[i] < percents[i - 1]) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
			}
		}

		// Are these settings the same as before?
		final Color background = getBackground();
		if (backgroundImage == null) {
			if ((gradientColors != null) && (colors != null)
				&& (gradientColors.length == colors.length)) {
				boolean same = false;
				for (int i = 0; i < gradientColors.length; i++) {
					same = (gradientColors[i] == colors[i])
						   || ((gradientColors[i] == null)
							   && (colors[i] == background))
						   || ((gradientColors[i] == background)
							   && (colors[i] == null));
					if (!same) {
						break;
					}
				}
				if (same) {
					for (int i = 0; i < gradientPercents.length; i++) {
						same = gradientPercents[i] == percents[i];
						if (!same) {
							break;
						}
					}
				}
				if (same && this.gradientVertical == vertical)
					return;
			}
		} else {
			backgroundImage = null;
		}
		// Store the new settings
		if (colors == null) {
			gradientColors = null;
			gradientPercents = null;
			gradientVertical = false;
		} else {
			gradientColors = new Color[colors.length];
			for (int i = 0; i < colors.length; ++i)
				gradientColors[i] = (colors[i] != null)
						? colors[i]
						: background;
			gradientPercents = new int[percents.length];
			System.arraycopy(percents, 0, gradientPercents, 0, percents.length);
			gradientVertical = vertical;
		}
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image image) {
		if (image != null) {
			gradientColors = null;
			gradientPercents = null;
		}
		backgroundImage = image;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		if (leftMargin == this.leftMargin) {
			return;
		}

		this.leftMargin = leftMargin;
		propertyChanged();
	}

	public int getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(int topMargin) {
		if (topMargin == this.topMargin) {
			return;
		}

		this.topMargin = topMargin;
		propertyChanged();
	}

	public int getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(int rightMargin) {
		if (rightMargin == this.rightMargin) {
			return;
		}

		this.rightMargin = rightMargin;
		propertyChanged();
	}

	public int getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		if (bottomMargin == this.bottomMargin) {
			return;
		}
		this.bottomMargin = bottomMargin;
		propertyChanged();
	}

	public void setMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
		if (leftMargin == this.leftMargin
			&& topMargin == this.topMargin
			&& rightMargin == this.rightMargin
			&& bottomMargin == this.bottomMargin) {
			return;
		}

		this.leftMargin = leftMargin;
		this.topMargin = topMargin;
		this.rightMargin = rightMargin;
		this.bottomMargin = bottomMargin;
		propertyChanged();
	}

	public void dispose() {
		super.dispose();
		gradientColors = null;
		gradientPercents = null;
		backgroundImage = null;
	}
}
