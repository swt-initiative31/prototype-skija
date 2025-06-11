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

class DefaultSliderRenderer extends SliderRenderer {

	private final ScrollBarRenderer scrollBarRenderer;

	protected DefaultSliderRenderer(Slider slider, ScrollBarRenderer scrollBarRenderer) {
		super(slider);
		this.scrollBarRenderer = scrollBarRenderer;
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		scrollBarRenderer.paint(gc, 0, 0, width, height);
	}

	@Override
	public Point computeDefaultSize() {
		return scrollBarRenderer.computeDefaultSize();
	}
}
