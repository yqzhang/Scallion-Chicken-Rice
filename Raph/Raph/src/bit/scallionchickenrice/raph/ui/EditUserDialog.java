package bit.scallionchickenrice.raph.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bit.scallionchickenrice.raph.action.UserManage;

public class EditUserDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	
	private Text textUserName;
	private Text textPassword;
	private Combo comboUserType;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditUserDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(TableItem[] item) {
		createContents(item);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(TableItem[] item) {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(450, 122);
		shell.setText("\u4FEE\u6539\u4FE1\u606F");
		
		Rectangle parentBounds = getParent().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width)/2, parentBounds.y + (parentBounds.height - shellBounds.height)/2);
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(6, false));
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 95);
		fd_composite.right = new FormAttachment(0, 444);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		
		Label labeUserName = new Label(composite, SWT.NONE);
		labeUserName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labeUserName.setText("\u7528\u6237\u540D");
		
		textUserName = new Text(composite, SWT.BORDER);
		textUserName.setEnabled(false);
		textUserName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUserName.setText((String)item[0].getText(0));
		
		new Label(composite, SWT.NONE);
		
		Label labelPassword = new Label(composite, SWT.NONE);
		labelPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelPassword.setText("\u5BC6\u7801");
		
		textPassword = new Text(composite, SWT.BORDER);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPassword.setText((String)item[0].getText(1));
		
		new Label(composite, SWT.NONE);
		
		Label labelUserType = new Label(composite, SWT.NONE);
		labelUserType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelUserType.setText("\u7528\u6237\u7C7B\u578B");
		
		comboUserType = new Combo(composite, SWT.NONE);
		comboUserType.setItems(new String[] {"\u7BA1\u7406\u5458", "\u670D\u52A1\u5458", "\u53A8\u5E08"});
		comboUserType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(item[0].getText(2).equals("管理员")) {
			comboUserType.select(0);
		}
		else if(item[0].getText(2).equals("服务员")){
			comboUserType.select(1);
		}
		else {
			comboUserType.select(2);
		}
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("    \u786E\u5B9A    ");
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				UserManage.changeProfile(textUserName.getText(), textPassword.getText(), comboUserType.getItem(comboUserType.getSelectionIndex()));
				getParent().redraw();
				shell.close();
			}
		});
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnNewButton_2.setText("    \u53D6\u6D88    ");
		
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		new Label(composite, SWT.NONE);
	}

}
