import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Thomas Singer
 */
public class ScrollableTest {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Text text = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setText("hello Good Morning. Have a nice day \nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\n"
				+ "hello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\n"
				+ "hello\nhello\nhello\nhello\nhello\nhello\nhello\nhello\n");

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
