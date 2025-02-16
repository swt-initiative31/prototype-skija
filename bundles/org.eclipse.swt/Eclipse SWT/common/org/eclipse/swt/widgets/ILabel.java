package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public interface ILabel extends IControl {

	int getAlignment();

	Image getImage();

	String getText();

	void setAlignment(int alignment);

	void setImage(Image image);

	void setText(String string);

	@Override
	Label getWrapper();

}