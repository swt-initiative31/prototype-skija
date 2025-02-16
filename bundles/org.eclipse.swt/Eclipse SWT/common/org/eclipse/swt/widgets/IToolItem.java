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
import org.eclipse.swt.graphics.*;

public interface IToolItem extends IItem {

	void addSelectionListener(SelectionListener listener);

	Rectangle getBounds();

	Control getControl();

	Image getDisabledImage();

	Color getBackground();

	boolean getEnabled();

	Color getForeground();

	Image getHotImage();

	ToolBar getParent();

	boolean getSelection();

	String getToolTipText();

	int getWidth();

	boolean isEnabled();

	void removeSelectionListener(SelectionListener listener);

	void setBackground(Color color);

	void setControl(Control control);

	void setEnabled(boolean enabled);

	void setDisabledImage(Image image);

	void setForeground(Color color);

	void setHotImage(Image image);

	void setSelection(boolean selected);

	void setToolTipText(String string);

	void setWidth(int width);

	@Override
	ToolItem getWrapper();

}