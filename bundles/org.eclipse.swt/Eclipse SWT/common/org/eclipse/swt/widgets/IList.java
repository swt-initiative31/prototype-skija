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

public interface IList extends IScrollable {

	void add(String string);

	void add(String string, int index);

	void addSelectionListener(SelectionListener listener);

	void deselect(int[] indices);

	void deselect(int index);

	void deselect(int start, int end);

	void deselectAll();

	int getFocusIndex();

	String getItem(int index);

	int getItemCount();

	int getItemHeight();

	String[] getItems();

	String[] getSelection();

	int getSelectionCount();

	int getSelectionIndex();

	int[] getSelectionIndices();

	int getTopIndex();

	int indexOf(String string);

	int indexOf(String string, int start);

	boolean isSelected(int index);

	void remove(int[] indices);

	void remove(int index);

	void remove(int start, int end);

	void remove(String string);

	void removeAll();

	void removeSelectionListener(SelectionListener listener);

	void select(int[] indices);

	void select(int index);

	void select(int start, int end);

	void selectAll();

	void setItem(int index, String string);

	void setItems(String... items);

	void setSelection(int[] indices);

	void setSelection(String[] items);

	void setSelection(int index);

	void setSelection(int start, int end);

	void setTopIndex(int index);

	void showSelection();

	@Override
	List getWrapper();

}