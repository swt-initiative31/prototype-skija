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

public interface ICanvas extends IComposite {

	void drawBackground(GC gc, int x, int y, int width, int height);

	Caret getCaret();

	IME getIME();

	void scroll(int destX, int destY, int x, int y, int width, int height, boolean all);

	void setCaret(Caret caret);

	void setIME(IME ime);

	@Override
	Canvas getWrapper();

}