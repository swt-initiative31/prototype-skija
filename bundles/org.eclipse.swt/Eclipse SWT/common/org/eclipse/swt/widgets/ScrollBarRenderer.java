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
 * 				Raghunandana Murthappa, Thomas Singer (syntevo)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public abstract class ScrollBarRenderer {

	protected final CustomScrollBar scrollBar;

	protected ScrollBarRenderer(CustomScrollBar scrollBar) {
		if (scrollBar == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.scrollBar = scrollBar;
	}

	public abstract void paint(GC gc, int x, int y, int width, int height);

	public abstract Point computeDefaultSize();

	public abstract void setDrawTrack(boolean drawTrack);

	public abstract void setDragging(boolean isDragging);

	public abstract void setThumbHovered(boolean isHovered);

	public abstract boolean getDragging();

	public abstract boolean getHovered();

	public abstract Rectangle getThumbRectangle();

	public abstract Rectangle getTrackRectangle();
}
