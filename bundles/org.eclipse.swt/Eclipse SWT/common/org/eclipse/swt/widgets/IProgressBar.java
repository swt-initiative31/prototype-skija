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

public interface IProgressBar extends IControl {

	int getMaximum();

	int getMinimum();

	int getSelection();

	int getState();

	void setMaximum(int value);

	void setMinimum(int value);

	void setSelection(int value);

	void setState(int state);

	@Override
	ProgressBar getWrapper();

}