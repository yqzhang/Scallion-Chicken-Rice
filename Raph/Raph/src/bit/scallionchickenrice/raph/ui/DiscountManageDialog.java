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
import org.eclipse.swt.widgets.Spinner;

public class DiscountManageDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Combo combo;
	private Button btnNewButton;
	private Text text_1;
	private Spinner spinner;
	private ArrayList<String> myList = new ArrayList<String>();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DiscountManageDialog(Shell parent, int style) {
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
		shell.setText("\u6298\u6263\u7BA1\u7406");
		
		Rectangle parentBounds = getParent().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width)/2, parentBounds.y + (parentBounds.height - shellBounds.height)/2);
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(8, false));
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 182);
		fd_composite.right = new FormAttachment(0, 444);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		new Label(composite, SWT.NONE);
		
		combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ArrayList<String> classList = DishManage.getAllDiscount();
		String[] tempList = new String[classList.size()];
		
		String[] temp = new String[2];
		
		myList = new ArrayList<String>();
		for(int i = 0; i < classList.size(); i++) {
			
			temp = classList.get(i).split(" ");
			
			tempList[i] = temp[0];
			myList.add(temp[1]);
		}
		
		combo.setItems(tempList);
		combo.select(0);
		
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(combo.getSelectionIndex() == -1) {
					combo.select(0);
				}
				text_1.setText(myList.get(combo.getSelectionIndex()));
			}
		});
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_1.setText(myList.get(0));
		text_1.setEnabled(false);
		
		new Label(composite, SWT.NONE);
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setText("\u7F16\u8F91\u6298\u6263");
		
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new EditDiscountDialog(Display.getCurrent().getActiveShell(), 1).open(combo.getItem(combo.getSelectionIndex()),text_1.getText(), combo);
				
				ArrayList<String> classList = DishManage.getAllDiscount();
				String[] tempList = new String[classList.size()];
				
				String[] temp = new String[2];
				
				myList = new ArrayList<String>();
				for(int i = 0; i < classList.size(); i++) {
					
					temp = classList.get(i).split(" ");
					
					tempList[i] = temp[0];
					myList.add(temp[1]);
				}
				
				combo.setItems(tempList);
				combo.select(0);
			}
		});
		
		new Label(composite, SWT.NONE);
		
		btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setText("\u5220\u9664\u6298\u6263");
		if(tempList.length <= 0) {
			btnNewButton.setEnabled(false);
		}
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DishManage.deleteDiscount(combo.getItem(combo.getSelectionIndex()));
				
				ArrayList<String> classList = DishManage.getAllDiscount();
				String[] tempList = new String[classList.size()];
				
				String[] temp = new String[2];
				
				myList = new ArrayList<String>();
				for(int i = 0; i < classList.size(); i++) {
					
					temp = classList.get(i).split(" ");
					
					tempList[i] = temp[0];
					myList.add(temp[1]);
				}
				
				combo.setItems(tempList);
				combo.select(0);
				
				if(tempList.length <= 0) {
					btnNewButton.setEnabled(false);
				}
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
		new Label(composite, SWT.NONE);
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		spinner = new Spinner(composite, SWT.BORDER);
		new Label(composite, SWT.NONE);
		
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.setText("\u6DFB\u52A0\u6298\u6263");
		new Label(composite, SWT.NONE);
		
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DishManage.addDiscount(text.getText(),spinner.getText());
				
				ArrayList<String> classList = DishManage.getAllDiscount();
				String[] tempList = new String[classList.size()];
				
				String[] temp = new String[2];
				
				myList = new ArrayList<String>();
				for(int i = 0; i < classList.size(); i++) {
					
					temp = classList.get(i).split(" ");
					
					tempList[i] = temp[0];
					myList.add(temp[1]);
				}
				
				combo.setItems(tempList);
				combo.select(0);
				
				if(tempList.length > 0) {
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
