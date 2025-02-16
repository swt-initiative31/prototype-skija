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