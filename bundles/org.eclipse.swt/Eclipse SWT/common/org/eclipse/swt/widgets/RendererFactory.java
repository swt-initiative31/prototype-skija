package org.eclipse.swt.widgets;

public interface RendererFactory {
	ButtonRenderer createCheckboxRenderer(Button button);

	ButtonRenderer createRadioButtonRenderer(Button button);

	ButtonRenderer createArrowButtonRenderer(Button button);

	ButtonRenderer createPushToggleButtonRenderer(Button button);

	LabelRenderer createLabelRenderer(Label label);

	LinkRenderer createLinkRenderer(Link link);

	ScaleRenderer createScaleRenderer(Scale scale);
}
