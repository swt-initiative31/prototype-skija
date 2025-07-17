package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

public class CustomComposite extends Composite {

	protected ControlRenderer getRenderer() {
		return new ControlRenderer(this) {
			
			@Override
			protected void paint(GC gc, int width, int height) {
				
			}
		};
	}
	
	void createHandle () {
		parent.addWidget(this);
	}

	private int x;
	private int y;
	private int width;
	private int height;

	protected Color background;
	protected Color foreground;
	
	private boolean visible = true;
	private boolean enabled = true;

	public CustomComposite(Composite parent, int style) {
		super(parent, style);
		addListener(SWT.Paint, e->onPaint(e));
	}
	
	private void onPaint(Event e) {
		
		
	}

	boolean isNativeScrollable(){
		return false;
	}
	
	public boolean isVisible () {
		return getVisible () && parent.isVisible ();
	}
	
	public boolean getVisible() {
		return visible ;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isEnabled () {
		checkWidget ();
		return getEnabled () && parent.isEnabled ();
	}
	
	public boolean getEnabled () {
		return enabled;
	}
	
	public void setEnabled ( boolean enable ) {
		this.enabled = enable;
	}
	
	
	@Override
	public final Color getBackground() {
		return background != null ? background : getRenderer().getDefaultBackground();
	}

	@Override
	public void setBackground(Color color) {
		if (color != null && color.isDisposed())
			error(SWT.ERROR_INVALID_ARGUMENT);
		this.background = color;
		super.setBackground(color);
	}

	@Override
	public final Color getForeground() {
		return foreground != null ? foreground : getRenderer().getDefaultForeground();
	}

	@Override
	public void setForeground(Color color) {
		if (color != null && color.isDisposed())
			error(SWT.ERROR_INVALID_ARGUMENT);
		this.foreground = color;
		super.setForeground(color);
	}



	@Override
	public Point getSize() {
		return new Point(width, height);
	}

	@Override
	public void setSize(int width, int height) {
		checkWidget();
		if (width == this.width && height == this.height) {
			return;
		}

		this.width = width;
		this.height = height;
		super.setSize(this.width, this.height);
		redraw();
	}

	@Override
	public void setSize(Point size) {
		checkWidget();
		if (size == null)
			error(SWT.ERROR_NULL_ARGUMENT);

		setSize(size.x, size.y);
	}

	@Override
	public Point getLocation() {
		checkWidget();
		return new Point(x, y);
	}

	@Override
	public void setLocation(int x, int y) {
		checkWidget();
		if (x == this.x && y == this.y) {
			return;
		}

		this.x = x;
		this.y = y;
		super.setLocation(x, y);
		redraw();
	}

	@Override
	public void setLocation(Point location) {
		if (location == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		setLocation(location.x, location.y);
	}

	@Override
	public Rectangle getBounds() {
		checkWidget();
		return new Rectangle(x, y, width, height);
	}

	@Override
	public void setBounds(Rectangle rect) {
		if (rect == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		checkWidget();
		if (rect.x == this.x && rect.y == this.y && rect.width == this.width && rect.height == this.height) {
			return;
		}

		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
		super.setBounds(rect);
		redraw();
	}
	
	public void redraw () {
		getParent().redraw();
	}
	
	public void redraw (int x, int y, int width, int height, boolean all) {
		getParent().redraw();
	}
	

	@Override
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
	}
	
	@Override
	Point minimumSize (int wHint, int hHint, boolean changed) {

		var p = super.minimumSize(wHint, hHint, changed);
		
		return new Point (Math.max(p.x ,  40), Math.max( p.y , 40)  );
	}

}
