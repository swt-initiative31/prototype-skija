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

public interface ICombo extends IComposite {

	void add(String string);

	void add(String string, int index);

	void addModifyListener(ModifyListener listener);

	void addSegmentListener(SegmentListener listener);

	void addSelectionListener(SelectionListener listener);

	void addVerifyListener(VerifyListener listener);

	void clearSelection();

	void copy();

	void cut();

	void deselect(int index);

	void deselectAll();

	Point getCaretLocation();

	int getCaretPosition();

	String getItem(int index);

	int getItemCount();

	int getItemHeight();

	String[] getItems();

	boolean getListVisible();

	void setListVisible(boolean visible);

	Point getSelection();

	int getSelectionIndex();

	String getText();

	int getTextHeight();

	int getTextLimit();

	int getVisibleItemCount();

	int indexOf(String string);

	int indexOf(String string, int start);

	void paste();

	void remove(int index);

	void remove(int start, int end);

	void remove(String string);

	void removeAll();

	void removeModifyListener(ModifyListener listener);

	void removeSegmentListener(SegmentListener listener);

	void removeSelectionListener(SelectionListener listener);

	void removeVerifyListener(VerifyListener listener);

	void select(int index);

	void setItem(int index, String string);

	void setItems(String... items);

	void setSelection(Point selection);

	void setText(String string);

	void setTextLimit(int limit);

	void setVisibleItemCount(int count);

	@Override
	Combo getWrapper();

}