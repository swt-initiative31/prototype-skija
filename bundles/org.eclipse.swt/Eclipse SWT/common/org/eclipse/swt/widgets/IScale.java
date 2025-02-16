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

import org.eclipse.swt.events.*;

public interface IScale extends IControl {

	void addSelectionListener(SelectionListener listener);

	int getIncrement();

	int getMaximum();

	int getMinimum();

	int getPageIncrement();

	int getSelection();

	void removeSelectionListener(SelectionListener listener);

	void setIncrement(int increment);

	void setMaximum(int value);

	void setMinimum(int value);

	void setPageIncrement(int pageIncrement);

	void setSelection(int value);

	@Override
	Scale getWrapper();

}