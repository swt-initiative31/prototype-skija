package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public abstract class TextRenderer extends ControlRenderer {

	protected abstract Rectangle getVisibleArea();

	abstract Point getLocationByTextLocation(TextLocation textLocation, GC gc);

	protected abstract int getLineHeight(GC gc);

	protected final Text text;

	protected final TextModel model;

	public TextRenderer(Text text, TextModel model) {
		super(text);
		this.text = text;
		this.model = model;
	}
}
