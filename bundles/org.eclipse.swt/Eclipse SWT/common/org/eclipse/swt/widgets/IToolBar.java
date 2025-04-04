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

public interface IToolBar extends IComposite {

	ToolItem getItem(int index);

	ToolItem getItem(Point point);

	int getItemCount();

	ToolItem[] getItems();

	int getRowCount();

	int indexOf(ToolItem item);

	@Override
	ToolBar getWrapper();
}