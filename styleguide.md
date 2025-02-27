# Code Style Guide

This document should describe the expected coding style.

## Naming
- variables should be named according to the Java code conventions
	- classes, interfaces, ... are named in `PascalCase`
	- fields, methods are named in `camelCase`
	- (static final) constants are named in `UPPER_SNAKE_CASE`


## Visibility
- the public fields and methods should be limited to the official API; internal API should be package private, those meant for access/overriding from sub classes should be protected


## Whitespace
- all leading whitespace *must* be tabs, except for indentation where the correct alignment should be kept independent of the set tab size by using spaces after initial tabs, e.g. (`--->` means tab, `.` leading space)
```
--->/**
--->.* This method returns the default size.
--->.*/
```
- tabs *must only* be used as the first characters in a line; they are *never* used after a non-tab character

### Empty Lines
- one empty line *must* be used to separate blocks, e.g. constants from fields, methods from each other
- there *must not* be an empty line after an opening `{` (except of class, interface, ... definition) or before a closing `}`
- there *should* be an empty line
	- if the previous block ended with a control flow statement like `break`, `continue`, `return` or `throw` and there are more statements in the block
	```java
	// Bad:
	if (a > b) {
		return 1;
	}
	doSomething();

	// Good:
	if (a > b) {
		return 1;
	}

	doSomething();
	```
	- after the end of an `if` or `if-else` statements
	```java
	// Bad:
	if (a > b) {
		doSomething();
	}
	if (a < c) {
		doSomethingElse();
	}

	// Good:
	if (a > b) {
		doSomething();
	}

	if (a < c) {
		doSomethingElse();
	}
	```
- there *should not* be an empty line between different field definitions, except they define some groups (e.g. properties, cached values):
```java
private boolean enabled;
private boolean visible;

private Point cachedDefaultSize;
```

### Spaces in expressions and statements
| Good                   | Bad                      |
|------------------------|--------------------------|
| `a = b + c;`           | `a=b+c;`                 |
| `a = foo(b, c);`       | `a = foo ( b,c );`       |
| `a = b.c;`             | `a = b. c;`              |


## Block statements
- each statement of an `if`, `else`, `do...while`, `while`, `for` *must* use a block statement (`{ ... }`), except single-line assertions or returns:
```java
if (a == null) error (SWT.ERROR_NULL_ARGUMENT);
if (b == 1) return 0;

if (c > b) {
	doSomething();
}
else {
	doSomethingElse();
}

for (int i = 0; i < 10; i++) {
	print(i);
}
```

## Others
- local variables *must* be defined in the closest scope
```java
// Bad:
boolean enabled = isEnabled();
if (c > b) {
	foo(enabled);
}

// Good:
if (c > b) {
	boolean selected = isSelected() || parent.isSelected();
	bar(selected);
}
```

- there *should* not be an `else` statement if the `if` branch ended with a control flow statement like `break`, `continue`, `return` or `throw`
```java
// Bad:
if (a > b) {
	return 1;
}
else {
	doSomething();
}

// Good:
if (a > b) {
	return 1;
}

doSomething();
```

## SWT specific
- internally only use light-weight, generic `Listener`s, not the heavier-weight `SWTEventListener` subclasses
- each public API methods that must only be accessed from the GUI thread, must invoke `checkWidget();` as first statement
