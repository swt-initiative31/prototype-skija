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

public interface IText extends IScrollable {

	void addModifyListener(ModifyListener listener);

	void addSegmentListener(SegmentListener listener);

	void addSelectionListener(SelectionListener listener);

	void addVerifyListener(VerifyListener listener);

	void append(String string);

	void clearSelection();

	void copy();

	void cut();

	int getCaretLineNumber();

	Point getCaretLocation();

	int getCaretPosition();

	int getCharCount();

	boolean getDoubleClickEnabled();

	char getEchoChar();

	boolean getEditable();

	int getLineCount();

	String getLineDelimiter();

	int getLineHeight();

	String getMessage();

	Point getSelection();

	int getSelectionCount();

	String getSelectionText();

	int getTabs();

	String getText();

	char[] getTextChars();

	String getText(int start, int end);

	int getTextLimit();

	int getTopIndex();

	int getTopPixel();

	void insert(String string);

	void paste();

	void removeModifyListener(ModifyListener listener);

	void removeSegmentListener(SegmentListener listener);

	void removeSelectionListener(SelectionListener listener);

	void removeVerifyListener(VerifyListener listener);

	void selectAll();

	void setDoubleClickEnabled(boolean doubleClick);

	void setEchoChar(char echo);

	void setEditable(boolean editable);

	void setMessage(String message);

	void setSelection(int start);

	void setSelection(int start, int end);

	void setSelection(Point selection);

	void setTabs(int tabs);

	void setText(String string);

	void setTextChars(char[] text);

	void setTextLimit(int limit);

	void setTopIndex(int index);

	void showSelection();

	@Override
	Text getWrapper();

}