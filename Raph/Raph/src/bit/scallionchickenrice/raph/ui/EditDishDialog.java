package bit.scallionchickenrice.raph.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;

import bit.scallionchickenrice.raph.action.DishManage;

public class EditDishDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Combo combo;
	private Spinner spinner;
	
	private String pictureDir;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditDishDialog(Shell parent, int style) {
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
		shell.setSize(450, 210);
		shell.setText("\u7F16\u8F91\u83DC\u54C1");
		shell.setLayout(new FormLayout());
		
		Rectangle parentBounds = getParent().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width)/2, parentBounds.y + (parentBounds.height - shellBounds.height)/2);
		//shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.bottom = new FormAttachment(0, 182);
		fd_composite.right = new FormAttachment(0, 444);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new GridLayout(8, false));
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("\u83DC\u54C1\u540D\u79F0");
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		text.setText(item[0].getText(0));
		text.setEnabled(false);
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("\u83DC\u54C1\u7C7B\u522B");
		
		combo = new Combo(composite, SWT.NONE);
		
		ArrayList<String> list = DishManage.getDishClasses();
		String[] items = new String[list.size()];
		
		for(int i = 0; i < list.size(); i++) {
			items[i] = list.get(i);
		}
		
		combo.setItems(items);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(int i = 0; i < items.length; i++) {
			if(item[0].getText(1).equals(items[i])) {
				combo.select(i);
				break;
			}
		}
		
		new Label(composite, SWT.NONE);
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setText("\u83DC\u54C1\u4EF7\u683C");
		
		spinner = new Spinner(composite, SWT.BORDER);
		spinner.setMaximum(10000);
		spinner.setSelection(Integer.parseInt(item[0].getText(3)));
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Label lblNewLabel_4 = new Label(composite, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("\u83DC\u54C1\u56FE\u7247");
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_2.setText(DishManage.getDishPictureDir(item[0].getText(0)));
		
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.setText("\u6D4F\u89C8");
		
		pictureDir = text_2.getText();
		
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileSelect=new FileDialog(shell,SWT.SINGLE);
				fileSelect.setFilterNames(new String[]{"*.jpg"}); 
				fileSelect.setFilterExtensions(new String[]{"*.jpg"});
				
				pictureDir = fileSelect.open();
				if(pictureDir != null) {
					text_2.setText(pictureDir);
				}
			}
		});
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_5 = new Label(composite, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("\u539F\u6599\u6E05\u5355");
		
		text_1 = new Text(composite, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_text_1.heightHint = 69;
		text_1.setLayoutData(gd_text_1);
		text_1.setText(item[0].getText(2));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("    \u786E\u5B9A    ");
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
				String imageName = df.format(calendar.getTime());
				
				//System.out.println(pictureDir);
				//System.out.println(DishManage.getDishPictureDir(text.getText()));
				
				if(pictureDir.equals(DishManage.getDishPictureDir(text.getText()))) {
					DishManage.editDish(text.getText(), combo.getItem(combo.getSelectionIndex()), spinner.getText(), pictureDir, text_1.getText());
				}
				else {
					File fromFile = new File(pictureDir);
					File targetFile = new File("pic/" + imageName + ".jpg");
					File targetDir = new File("pic");
					
					if(!targetDir.exists()) {
						targetDir.mkdirs();
					}
					
					try {
						InputStream is = new FileInputStream(fromFile);
						FileOutputStream fos =  new FileOutputStream(targetFile);
						
						byte[] buffer = new byte[1024];
						
						while(is.read(buffer) != -1) {
							fos.write(buffer);
						}
						
						is.close();
						fos.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					DishManage.editDish(text.getText(), combo.getItem(combo.getSelectionIndex()), spinner.getText(), "pic/" + imageName + ".jpg", text_1.getText());
				}
				
				getParent().redraw();
				shell.close();
			}
		});
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnNewButton_1.setText("    \u53D6\u6D88    ");
		
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);


	}

}
