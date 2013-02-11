package bit.scallionchickenrice.raph.ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import bit.scallionchickenrice.raph.action.DishManage;

public class ClassManageDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Combo combo;
	private Button btnNewButton;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ClassManageDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
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
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(450, 129);
		shell.setText("\u7C7B\u522B\u7BA1\u7406");
		
		Rectangle parentBounds = getParent().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width)/2, parentBounds.y + (parentBounds.height - shellBounds.height)/2);
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(7, false));
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 182);
		fd_composite.right = new FormAttachment(0, 444);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		new Label(composite, SWT.NONE);
		
		combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ArrayList<String> classList = DishManage.getAllClass();
		String[] tempList = new String[classList.size()];
		
		for(int i = 0; i < classList.size(); i++) {
			tempList[i] = classList.get(i);
		}
		
		combo.setItems(tempList);
		combo.select(0);
		
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				shell.getParent().redraw();
			}
		});
		
		new Label(composite, SWT.NONE);
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setText("\u7F16\u8F91\u7C7B\u522B");
		
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new EditClassDialog(Display.getCurrent().getActiveShell(), 1).open(combo.getItem(combo.getSelectionIndex()),combo);
			}
		});
		
		new Label(composite, SWT.NONE);
		
		btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("\u5220\u9664\u7C7B\u522B");
		if(tempList.length <= 1) {
			btnNewButton.setEnabled(false);
		}
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DishManage.deleteClass(combo.getItem(combo.getSelectionIndex()));
				
				ArrayList<String> classList = DishManage.getAllClass();
				String[] tempList = new String[classList.size()];
				
				for(int i = 0; i < classList.size(); i++) {
					tempList[i] = classList.get(i);
				}
				
				combo.setItems(tempList);
				combo.select(0);
				
				System.out.println("haha");
			}
		});
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.setText("\u6DFB\u52A0\u7C7B\u522B");
		new Label(composite, SWT.NONE);
		
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DishManage.addClass(text.getText());
				
				ArrayList<String> classList = DishManage.getAllClass();
				String[] tempList = new String[classList.size()];
				
				for(int i = 0; i < classList.size(); i++) {
					tempList[i] = classList.get(i);
				}
				
				combo.setItems(tempList);
				combo.select(0);
				
				if(tempList.length > 1) {
					btnNewButton.setEnabled(true);
				}
			}
		});

		Button btnNewButton_3 = new Button(composite, SWT.NONE);
		btnNewButton_3.setText("   \u8FD4\u56DE   ");
		new Label(composite, SWT.NONE);
		
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}
}
