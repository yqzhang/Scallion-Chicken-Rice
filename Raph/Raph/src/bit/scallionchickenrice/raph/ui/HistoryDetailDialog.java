package bit.scallionchickenrice.raph.ui;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

import bit.scallionchickenrice.raph.datafactory.OrderDetailFactory;
import bit.scallionchickenrice.raph.tool.OrderDetailContentProvider;
import bit.scallionchickenrice.raph.tool.OrderDetailLabelProvider;

public class HistoryDetailDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public HistoryDetailDialog(Shell parent, int style) {
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
		shell.setSize(384, 207);
		shell.setText("\u6D88\u8D39\u8BE6\u60C5");
		
		Rectangle parentBounds = getParent().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width)/2, parentBounds.y + (parentBounds.height - shellBounds.height)/2);
		
		Composite rcParent = new Composite(shell, SWT.NONE);
		FormData fd_rcParent = new FormData();
		fd_rcParent.left = new FormAttachment(0);
		fd_rcParent.right = new FormAttachment(100);
		fd_rcParent.top = new FormAttachment(0);
		fd_rcParent.bottom = new FormAttachment(100);
		rcParent.setLayoutData(fd_rcParent);
		shell.setLayout(new FormLayout());
		formToolkit.paintBordersFor(rcParent);
		rcParent.setLayout(new FormLayout());
		
		Composite rcUp = new Composite(rcParent, SWT.NONE);
		formToolkit.paintBordersFor(rcUp);
		TableColumnLayout rcUpTCLayout = new TableColumnLayout();
		rcUp.setLayout(rcUpTCLayout);
		FormData rcUpFD = new FormData();
		rcUpFD.top = new FormAttachment(0,5);
		rcUpFD.bottom = new FormAttachment(100,-43);
		rcUpFD.left = new FormAttachment(0,5);
		rcUpFD.right = new FormAttachment(100,5);
		rcUp.setLayoutData(rcUpFD);
		
		TableViewer rcTableViewer = new TableViewer(rcUp, SWT.BORDER | SWT.FULL_SELECTION);
		Table rcTable = rcTableViewer.getTable();
		rcTable.setHeaderVisible(true);
		rcTable.setLinesVisible(true);
		formToolkit.paintBordersFor(rcTable);
		
		TableColumn rcTCUsername = new TableColumn(rcTable, SWT.CENTER);
		rcTCUsername.setText("\u83DC\u54C1\u540D\u79F0");
		rcUpTCLayout.setColumnData(rcTCUsername, new ColumnWeightData(150,100,false));
        
        TableColumn rcTCPassword = new TableColumn(rcTable, SWT.CENTER);
        rcTCPassword.setText("\u83DC\u54C1\u6570\u91CF");
        rcUpTCLayout.setColumnData(rcTCPassword, new ColumnWeightData(150,100,false));
        
        rcTableViewer.setContentProvider(new OrderDetailContentProvider());
        rcTableViewer.setLabelProvider(new OrderDetailLabelProvider());
        rcTableViewer.setInput(OrderDetailFactory.getOrderDetail(item[0].getText(0)));
        
        Composite rcDown = new Composite(rcParent, SWT.NONE);
		formToolkit.adapt(rcDown);
		RowLayout rcDownRLayout = new RowLayout(SWT.HORIZONTAL);
		rcDownRLayout.justify = true;
		rcDownRLayout.center = true;
		rcDown.setLayout(rcDownRLayout);
		FormData rcDownFD = new FormData();
		rcDownFD.top = new FormAttachment(100, -38);
		rcDownFD.bottom = new FormAttachment(100, -5);
		rcDownFD.left = new FormAttachment(0,5);
		rcDownFD.right = new FormAttachment(100,5);
		rcDown.setLayoutData(rcDownFD);
		
		Button rcDownBTNInsert = new Button(rcDown, SWT.NONE);
		formToolkit.adapt(rcDownBTNInsert, true, true);
		rcDownBTNInsert.setText("    \u8FD4\u56DE    ");
		
		rcDownBTNInsert.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}

}
