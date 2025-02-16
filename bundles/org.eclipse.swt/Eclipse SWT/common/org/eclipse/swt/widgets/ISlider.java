package org.eclipse.swt.widgets;

import org.eclipse.swt.events.*;

public interface ISlider extends IControl {

	void addSelectionListener(SelectionListener listener);

	int getIncrement();

	int getMaximum();

	int getMinimum();

	int getPageIncrement();

	int getSelection();

	int getThumb();

	void removeSelectionListener(SelectionListener listener);

	void setIncrement(int value);

	void setMaximum(int value);

	void setMinimum(int value);

	void setPageIncrement(int value);

	void setSelection(int value);

	void setThumb(int value);

	void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement);

	@Override
	Slider getWrapper();

}