package org.eclipse.swt.widgets;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public interface IButton extends IControl {

	void addSelectionListener(SelectionListener listener);

	int getAlignment();

	boolean getGrayed();

	Image getImage();

	boolean getSelection();

	String getText();

	void removeSelectionListener(SelectionListener listener);

	void setAlignment(int alignment);

	void setImage(Image image);

	void setGrayed(boolean grayed);

	void setSelection(boolean selected);

	void setText(String string);

	@Override
	Button getWrapper();

}