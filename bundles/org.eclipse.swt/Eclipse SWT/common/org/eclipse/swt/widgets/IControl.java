/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public interface IControl extends IWidget {

	void addControlListener(ControlListener listener);

	void addDragDetectListener(DragDetectListener listener);

	void addFocusListener(FocusListener listener);

	void addGestureListener(GestureListener listener);

	void addHelpListener(HelpListener listener);

	void addKeyListener(KeyListener listener);

	void addMenuDetectListener(MenuDetectListener listener);

	void addMouseListener(MouseListener listener);

	void addMouseTrackListener(MouseTrackListener listener);

	void addMouseMoveListener(MouseMoveListener listener);

	void addMouseWheelListener(MouseWheelListener listener);

	void addPaintListener(PaintListener listener);

	void addTouchListener(TouchListener listener);

	void addTraverseListener(TraverseListener listener);

	Point computeSize(int wHint, int hHint);

	Point computeSize(int wHint, int hHint, boolean changed);

	boolean dragDetect(Event event);

	boolean dragDetect(MouseEvent event);

	boolean forceFocus();

	Accessible getAccessible();

	Color getBackground();

	Image getBackgroundImage();

	int getBorderWidth();

	Rectangle getBounds();

	Cursor getCursor();

	boolean getDragDetect();

	boolean getEnabled();

	Font getFont();

	Color getForeground();

	Object getLayoutData();

	Point getLocation();

	Menu getMenu();

	Monitor getMonitor();

	int getOrientation();

	Composite getParent();

	Region getRegion();

	Point getSize();

	int getTextDirection();

	String getToolTipText();

	boolean getTouchEnabled();

	boolean getVisible();

	long internal_new_GC(GCData data);

	void internal_dispose_GC(long hDC, GCData data);

	boolean isEnabled();

	boolean isFocusControl();

	boolean isReparentable();

	boolean isVisible();

	void moveAbove(Control control);

	void moveBelow(Control control);

	void pack();

	void pack(boolean changed);

	boolean print(GC gc);

	void requestLayout();

	void redraw();

	void redraw(int x, int y, int width, int height, boolean all);

	void removeControlListener(ControlListener listener);

	void removeDragDetectListener(DragDetectListener listener);

	void removeFocusListener(FocusListener listener);

	void removeGestureListener(GestureListener listener);

	void removeHelpListener(HelpListener listener);

	void removeKeyListener(KeyListener listener);

	void removeMenuDetectListener(MenuDetectListener listener);

	void removeMouseTrackListener(MouseTrackListener listener);

	void removeMouseListener(MouseListener listener);

	void removeMouseMoveListener(MouseMoveListener listener);

	void removeMouseWheelListener(MouseWheelListener listener);

	void removePaintListener(PaintListener listener);

	void removeTouchListener(TouchListener listener);

	void removeTraverseListener(TraverseListener listener);

	void setBackground(Color color);

	void setBackgroundImage(Image image);

	void setBounds(int x, int y, int width, int height);

	void setBounds(Rectangle rect);

	void setCapture(boolean capture);

	void setCursor(Cursor cursor);

	void setDragDetect(boolean dragDetect);

	void setEnabled(boolean enabled);

	boolean setFocus();

	void setFont(Font font);

	void setForeground(Color color);

	void setLayoutData(Object layoutData);

	void setLocation(int x, int y);

	void setLocation(Point location);

	void setMenu(Menu menu);

	void setOrientation(int orientation);

	void setRedraw(boolean redraw);

	void setRegion(Region region);

	void setSize(int width, int height);

	void setSize(Point size);

	void setTextDirection(int textDirection);

	void setToolTipText(String string);

	void setTouchEnabled(boolean enabled);

	void setVisible(boolean visible);

	Point toControl(int x, int y);

	Point toControl(Point point);

	Point toDisplay(int x, int y);

	Point toDisplay(Point point);

	boolean traverse(int traversal);

	boolean traverse(int traversal, Event event);

	boolean traverse(int traversal, KeyEvent event);

	void update();

	boolean setParent(Composite parent);

	@Override
	Control getWrapper();

}