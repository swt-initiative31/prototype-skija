/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public interface IScrollable extends IControl {

	Rectangle computeTrim(int x, int y, int width, int height);

	Rectangle getClientArea();

	ScrollBar getHorizontalBar();

	int getScrollbarsMode();

	void setScrollbarsMode(int mode);

	ScrollBar getVerticalBar();

	@Override
	Scrollable getWrapper();

}