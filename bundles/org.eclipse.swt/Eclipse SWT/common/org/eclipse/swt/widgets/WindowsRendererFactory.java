package org.eclipse.swt.widgets;

public class WindowsRendererFactory implements RendererFactory {
	@Override
	public ButtonRenderer createCheckboxRenderer(Button button) {
		return new WindowsCheckboxRenderer(button);
	}

	@Override
	public ButtonRenderer createRadioButtonRenderer(Button button) {
		return new WindowsRadioButtonRenderer(button);
	}

	@Override
	public ButtonRenderer createArrowButtonRenderer(Button button) {
		return new WindowsArrowButtonRenderer(button);
	}

	@Override
	public ButtonRenderer createPushToggleButtonRenderer(Button button) {
		return new WindowsButtonRenderer(button);
	}

	@Override
	public ScaleRenderer createScaleRenderer(Scale scale) {
		return new WindowsScaleRenderer(scale);
	}

	@Override
	public LabelRenderer createLabelRenderer(Label label) {
		return new BasicLabelRenderer(label);
	}

	@Override
	public LinkRenderer createLinkRenderer(Link link) {
		return new BasicLinkRenderer(link);
	}
}
