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