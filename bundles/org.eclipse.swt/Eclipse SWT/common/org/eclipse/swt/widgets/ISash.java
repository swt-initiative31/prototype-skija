package org.eclipse.swt.widgets;

import org.eclipse.swt.events.*;

public interface ISash extends IControl {

	void addSelectionListener(SelectionListener listener);

	void removeSelectionListener(SelectionListener listener);

	@Override
	Sash getWrapper();

}