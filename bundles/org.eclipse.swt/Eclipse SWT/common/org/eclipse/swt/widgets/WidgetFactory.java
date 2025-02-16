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

final class WidgetFactory {
	private WidgetFactory() {
	}

	static NativeWidget createWidget(Widget wrapper, Widget parent, int style) {
		return new NativeWidget(Widget.checkNative(parent), style) {
			@Override
			public Widget getWrapper() {
				return wrapper;
			}
		};
	}

	/*
	 * CONTROLS
	 */

	static NativeButton createButton(Button wrapper, Composite parent, int style) {
		return new NativeButton(Widget.checkNative(parent), style) {
			@Override
			public Button getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeLabel createLabel(Label wrapper, Composite parent, int style) {
		return new NativeLabel(Widget.checkNative(parent), style) {
			@Override
			public Label getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeLink createLink(Link wrapper, Composite parent, int style) {
		return new NativeLink(Widget.checkNative(parent), style) {
			@Override
			public Link getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeProgressBar createProgressBar(ProgressBar wrapper, Composite parent, int style) {
		return new NativeProgressBar(Widget.checkNative(parent), style) {
			@Override
			public ProgressBar getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeSash createSash(Sash wrapper, Composite parent, int style) {
		return new NativeSash(Widget.checkNative(parent), style) {
			@Override
			public Sash getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeScale createScale(Scale wrapper, Composite parent, int style) {
		return new NativeScale(Widget.checkNative(parent), style) {
			@Override
			public Scale getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeComposite createComposite(Composite wrapper, Composite parent, int style) {
		return new NativeComposite(Widget.checkNative(parent), style) {
			@Override
			public Composite getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeCanvas createCanvas(Canvas wrapper, Composite parent, int style) {
		return new NativeCanvas(Widget.checkNative(parent), style) {
			@Override
			public Canvas getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeDecorations createDecorations(Decorations wrapper, Composite parent, int style) {
		return new NativeDecorations(Widget.checkNative(parent), style) {
			@Override
			public Decorations getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeShell createShell(Shell wrapper, Display display, Shell parent, int style, long handle,
			boolean embedded) {
		return new NativeShell(display, Widget.checkNative(parent), style, handle, embedded) {
			@Override
			public Shell getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeCombo createCombo(Combo wrapper, Composite parent, int style) {
		return new NativeCombo(Widget.checkNative(parent), style) {
			@Override
			public Combo getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeCoolBar createCoolBar(CoolBar wrapper, Composite parent, int style) {
		return new NativeCoolBar(Widget.checkNative(parent), style) {
			@Override
			public CoolBar getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeDateTime createDateTime(DateTime wrapper, Composite parent, int style) {
		return new NativeDateTime(Widget.checkNative(parent), style) {
			@Override
			public DateTime getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeExpandBar createExpandBar(ExpandBar wrapper, Composite parent, int style) {
		return new NativeExpandBar(Widget.checkNative(parent), style) {
			@Override
			public ExpandBar getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeGroup createGroup(Group wrapper, Composite parent, int style) {
		return new NativeGroup(Widget.checkNative(parent), style) {
			@Override
			public Group getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeSpinner createSpinner(Spinner wrapper, Composite parent, int style) {
		return new NativeSpinner(Widget.checkNative(parent), style) {
			@Override
			public Spinner getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTabFolder createTabFolder(TabFolder wrapper, Composite parent, int style) {
		return new NativeTabFolder(Widget.checkNative(parent), style) {
			@Override
			public TabFolder getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTable createTable(Table wrapper, Composite parent, int style) {
		return new NativeTable(Widget.checkNative(parent), style) {
			@Override
			public Table getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeToolBar createToolBar(ToolBar wrapper, Composite parent, int style) {
		return new NativeToolBar(Widget.checkNative(parent), style) {
			@Override
			public ToolBar getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTree createTree(Tree wrapper, Composite parent, int style) {
		return new NativeTree(Widget.checkNative(parent), style) {
			@Override
			public Tree getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeSlider createSlider(Slider wrapper, Composite parent, int style) {
		return new NativeSlider(Widget.checkNative(parent), style) {
			@Override
			public Slider getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeList createList(List wrapper, Composite parent, int style) {
		return new NativeList(Widget.checkNative(parent), style) {
			@Override
			public List getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeText createText(Text wrapper, Composite parent, int style) {
		return new NativeText(Widget.checkNative(parent), style) {
			@Override
			public Text getWrapper() {
				return wrapper;
			}
		};
	}

	/*
	 * ITEMS
	 */

	static NativeItem createItem(Item wrapper, Widget parent, int style) {
		return new NativeItem(Widget.checkNative(parent), style) {
			@Override
			public Item getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeCoolItem createCoolItem(CoolItem wrapper, CoolBar parent, int style) {
		return new NativeCoolItem(Widget.checkNative(parent), style) {
			@Override
			public CoolItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeCoolItem createCoolItem(CoolItem wrapper, CoolBar parent, int style, int index) {
		return new NativeCoolItem(Widget.checkNative(parent), style, index) {
			@Override
			public CoolItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeExpandItem createExpandItem(ExpandItem wrapper, ExpandBar parent, int style) {
		return new NativeExpandItem(Widget.checkNative(parent), style) {
			@Override
			public ExpandItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeExpandItem createExpandItem(ExpandItem wrapper, ExpandBar parent, int style, int index) {
		return new NativeExpandItem(Widget.checkNative(parent), style, index) {
			@Override
			public ExpandItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeMenuItem createMenuItem(MenuItem wrapper, Menu parent, int style) {
		return new NativeMenuItem(Widget.checkNative(parent), style) {
			@Override
			public MenuItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeMenuItem createMenuItem(MenuItem wrapper, Menu parent, int style, int index) {
		return new NativeMenuItem(Widget.checkNative(parent), style, index) {
			@Override
			public MenuItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTabItem createTabItem(TabItem wrapper, TabFolder parent, int style) {
		return new NativeTabItem(Widget.checkNative(parent), style) {
			@Override
			public TabItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTabItem createTabItem(TabItem wrapper, TabFolder parent, int style, int index) {
		return new NativeTabItem(Widget.checkNative(parent), style, index) {
			@Override
			public TabItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTableColumn createTableColumn(TableColumn wrapper, Table parent, int style) {
		return new NativeTableColumn(Widget.checkNative(parent), style) {
			@Override
			public TableColumn getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTableColumn createTableColumn(TableColumn wrapper, Table parent, int style, int index) {
		return new NativeTableColumn(Widget.checkNative(parent), style, index) {
			@Override
			public TableColumn getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTableItem createTableItem(TableItem wrapper, Table parent, int style) {
		return new NativeTableItem(Widget.checkNative(parent), style) {
			@Override
			public TableItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTableItem createTableItem(TableItem wrapper, Table parent, int style, int index) {
		return new NativeTableItem(Widget.checkNative(parent), style, index) {
			@Override
			public TableItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTableItem createTableItem(TableItem wrapper, Table parent, int style, int index, boolean create) {
		return new NativeTableItem(Widget.checkNative(parent), style, index, create) {
			@Override
			public TableItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTaskItem createTaskItem(TaskItem wrapper, TaskBar parent, int style) {
		return new NativeTaskItem(Widget.checkNative(parent), style) {
			@Override
			public TaskItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeToolItem createToolItem(ToolItem wrapper, ToolBar parent, int style) {
		return new NativeToolItem(Widget.checkNative(parent), style) {
			@Override
			public ToolItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeToolItem createToolItem(ToolItem wrapper, ToolBar parent, int style, int index) {
		return new NativeToolItem(Widget.checkNative(parent), style, index) {
			@Override
			public ToolItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTrayItem createTrayItem(TrayItem wrapper, Tray parent, int style) {
		return new NativeTrayItem(Widget.checkNative(parent), style) {
			@Override
			public TrayItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTreeColumn createTreeColumn(TreeColumn wrapper, Tree parent, int style) {
		return new NativeTreeColumn(Widget.checkNative(parent), style) {
			@Override
			public TreeColumn getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTreeColumn createTreeColumn(TreeColumn wrapper, Tree parent, int style, int index) {
		return new NativeTreeColumn(Widget.checkNative(parent), style, index) {
			@Override
			public TreeColumn getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTreeItem createTreeItem(TreeItem wrapper, Tree parent, int style) {
		return new NativeTreeItem(Widget.checkNative(parent), style) {
			@Override
			public TreeItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTreeItem createTreeItem(TreeItem wrapper, Tree parent, int style, int index) {
		return new NativeTreeItem(Widget.checkNative(parent), style, index) {
			@Override
			public TreeItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTreeItem createTreeItem(TreeItem wrapper, TreeItem parent, int style) {
		return new NativeTreeItem(Widget.checkNative(parent), style) {
			@Override
			public TreeItem getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTreeItem createTreeItem(TreeItem wrapper, TreeItem parent, int style, int index) {
		return new NativeTreeItem(Widget.checkNative(parent), style, index) {
			@Override
			public TreeItem getWrapper() {
				return wrapper;
			}
		};
	}

	/*
	 * OTHERS
	 */

	static NativeCaret createCaret(Caret wrapper, Canvas parent, int style) {
		return new NativeCaret(Widget.checkNative(parent), style) {
			@Override
			public Caret getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeIME createIME(IME wrapper, Canvas parent, int style) {
		return new NativeIME(Widget.checkNative(parent), style) {
			@Override
			public IME getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeMenu createMenu(Menu wrapper, Decorations parent, int style) {
		return new NativeMenu(Widget.checkNative(parent), style) {
			@Override
			public Menu getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeScrollBar createScrollBar(ScrollBar wrapper, Scrollable parent, int style) {
		return new NativeScrollBar(Widget.checkNative(parent), style) {
			@Override
			public ScrollBar getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTaskBar createTaskBar(TaskBar wrapper, Display display, int style) {
		return new NativeTaskBar(display, style) {
			@Override
			public TaskBar getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeToolTip createToolTip(ToolTip wrapper, Shell parent, int style) {
		return new NativeToolTip(Widget.checkNative(parent), style) {
			@Override
			public ToolTip getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTracker createTracker(Tracker wrapper, Composite parent, int style) {
		return new NativeTracker(Widget.checkNative(parent), style) {
			@Override
			public Tracker getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTracker createTracker(Tracker wrapper, Display display, int style) {
		return new NativeTracker(display, style) {
			@Override
			public Tracker getWrapper() {
				return wrapper;
			}
		};
	}

	static NativeTray createTray(Tray wrapper, Display display, int style) {
		return new NativeTray(display, style) {
			@Override
			public Tray getWrapper() {
				return wrapper;
			}
		};
	}

}
