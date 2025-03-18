/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public abstract class SliderRenderer extends ControlRenderer {

	protected Slider slider;
	private int selection;
	private int minimum = 0;
	private int maximum = 100;
	private int thumb = 10;
	private int increment;
	private int pageIncrement;

	protected SliderRenderer(Slider slider) {
		super(slider);
		this.slider = slider;
	}

	public int getIncrement() {
		return this.increment;
	}

	public int getMaximum() {
		return this.maximum;
	}

	public int getMinimum() {
		return this.minimum;
	}

	public int getPageIncrement() {
		return this.pageIncrement;
	}

	public int getSelection() {
		return this.selection;
	}

	public int getThumb() {
		return this.thumb;
	}

	public void setIncrement(int increment) {
		this.increment = Math.max(1, increment);
		slider.redraw();
	}

	public void setMaximum(int max) {

		int min = getMinimum();

		if (max <= min) {
			return;
		}

		this.maximum = max;

		this.thumb = Math.min(this.thumb, this.maximum - min);

		this.selection = Math.min(this.selection, this.maximum - this.thumb);
		this.selection = Math.max(this.selection, min);

		slider.redraw();
	}

	public void setMinimum(int minimum) {
		if (minimum < 0) {
			return;
		}

		if (minimum <= this.maximum && minimum <= this.selection) {
			this.minimum = minimum;
			slider.redraw();
			return;
		}


		if (minimum <= (this.maximum - this.thumb) && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			slider.redraw();
			return;
		}

		if (minimum >= maximum) {
			this.minimum = this.thumb;
			slider.redraw();
			return;
		}

		if (minimum > (this.maximum - this.thumb) && minimum >= this.selection) {
			this.minimum = minimum;
			this.selection = minimum;
			this.thumb = this.maximum - this.minimum;
			slider.redraw();
			return;
		}
	}

	public void setPageIncrement(int pageIncrement) {
		this.pageIncrement = Math.max(1, pageIncrement);
		slider.redraw();
	}

	/**
	 * @param selection
	 * @return true if clients needs to be notified. i.e. selection really changed.
	 */
	public boolean setSelection(int selection) {
		int min = getMinimum();
		int max = getMaximum();
		int thumb = getThumb();

		// Ensure selection does not exceed max - thumb
		int newValue = Math.max(min, Math.min(selection, max - thumb));

		if (newValue != this.selection) {
			this.selection = newValue;
			slider.redraw();
			return true;
		}
		return false;
	}

	public void setThumb(int thumb) {

		if (thumb <= 0 || this.thumb == thumb) {
			return;
		}

		if (thumb <= this.maximum && thumb <= this.selection) {
			this.thumb = thumb;
			slider.redraw();
			return;
		}

		if (thumb <= (this.maximum - this.minimum) && thumb > this.selection) {
			this.selection = this.maximum - thumb;
			this.thumb = thumb;
		}

		if (thumb > (this.maximum - this.minimum) && thumb > this.selection) {
			this.thumb = this.maximum - this.minimum;
			this.selection = this.minimum;
		}
		slider.redraw();
	}

	public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement) {
		this.selection = selection;
		this.minimum = minimum;
		this.maximum = maximum;
		this.thumb = thumb;
		this.increment = increment;
		this.pageIncrement = pageIncrement;
	}

	public static int minMax(int min, int value, int max) {
		return Math.min(Math.max(min, value), max);
	}

	public abstract void onMouseMove(Event event);

	public abstract void onMouseUp(Event event);

	public abstract void onMouseDown(Event event);

	public abstract void onKeyDown(Event event);

	public abstract void onMouseVerticalWheel(Event event);

	public abstract void onMouseHorizontalWheel(Event event);

	public abstract Point computeDefaultSize();

	public abstract void setDrawTrack(boolean drawTrack);
}
