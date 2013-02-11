package bit.scallionchickenrice.leo.ui;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import bit.scallionchickenrice.leo.DAO.ControlDB;
import bit.scallionchickenrice.leo.communication.ProcessSocket;
import bit.scallionchickenrice.leo.datafactory.ActiveInfoFactory;
import bit.scallionchickenrice.leo.datafactory.KeyInfoFactory;
import bit.scallionchickenrice.leo.tool.ActiveInfoContentProvider;
import bit.scallionchickenrice.leo.tool.ActiveInfoLabelProvider;
import bit.scallionchickenrice.leo.tool.KeyInfoContentProvider;
import bit.scallionchickenrice.leo.tool.KeyInfoLabelProvider;

public class LeoMainApp {

	protected Shell shlLeo;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private TableViewer rcTableViewer;
	private TableViewer mmTableViewer;
	
	private Table rcTable;
	private Table mmTable;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			Thread t = new Thread() {
				public void run() {
					new ProcessSocket();
				}
			};
			
			t.start();
			
			LeoMainApp window = new LeoMainApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlLeo.open();
		shlLeo.layout();
		while (!shlLeo.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlLeo = new Shell();
		shlLeo.setSize(660, 424);
		shlLeo.setText("Leo");
		shlLeo.setMaximized(true);
		shlLeo.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(shlLeo, SWT.NONE);
		
		TabItem tbtmRuntimeCondition = new TabItem(tabFolder, SWT.NONE);
		tbtmRuntimeCondition.setText("\u6388\u6743\u5386\u53F2");
		
		Composite rcParent = new Composite(tabFolder, SWT.NONE);
		tbtmRuntimeCondition.setControl(rcParent);
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
		
		rcTableViewer = new TableViewer(rcUp, SWT.BORDER | SWT.FULL_SELECTION);
		rcTable = rcTableViewer.getTable();
		rcTable.setHeaderVisible(true);
		rcTable.setLinesVisible(true);
		formToolkit.paintBordersFor(rcTable);
		
		TableColumn rcTCUsername = new TableColumn(rcTable, SWT.CENTER);
		rcTCUsername.setText("\u6388\u6743\u65F6\u95F4");
		rcUpTCLayout.setColumnData(rcTCUsername, new ColumnWeightData(150,100,false));
        
        TableColumn rcTCPassword = new TableColumn(rcTable, SWT.CENTER);
        rcTCPassword.setText("\u6388\u6743\u7C7B\u578B");
        rcUpTCLayout.setColumnData(rcTCPassword, new ColumnWeightData(150,100,false));
        
        TableColumn tcTCUserType = new TableColumn(rcTable, SWT.CENTER);
        tcTCUserType.setText("MAC\u5730\u5740");
        rcUpTCLayout.setColumnData(tcTCUserType, new ColumnWeightData(150,100,false));
        
        TableColumn rcTCCondition = new TableColumn(rcTable, SWT.CENTER);
        rcTCCondition.setText("\u6FC0\u6D3B\u7801");
        rcUpTCLayout.setColumnData(rcTCCondition, new ColumnWeightData(150,100,false));
        
        rcTableViewer.setContentProvider(new ActiveInfoContentProvider());
        rcTableViewer.setLabelProvider(new ActiveInfoLabelProvider());
        rcTableViewer.setInput(ActiveInfoFactory.getActiveInfo());
		
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
		
		Button rcDownBTNRefresh = new Button(rcDown, SWT.NONE);
		formToolkit.adapt(rcDownBTNRefresh, true, true);
		rcDownBTNRefresh.setText("\u5237\u65B0\u5217\u8868");
		
		rcDownBTNRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				rcTableViewer.setInput(ActiveInfoFactory.getActiveInfo());
			}
		});
		
		TabItem tbtmMenuManagement = new TabItem(tabFolder, SWT.NONE);
		tbtmMenuManagement.setText("\u6FC0\u6D3B\u7801\u7BA1\u7406");
		
		Composite mmParent = new Composite(tabFolder, SWT.NONE);
		tbtmMenuManagement.setControl(mmParent);
		formToolkit.paintBordersFor(mmParent);
		mmParent.setLayout(new FormLayout());
		
		Composite mmUp = new Composite(mmParent, SWT.NONE);
		formToolkit.paintBordersFor(mmUp);
		TableColumnLayout mmUpTCLayout = new TableColumnLayout();
		mmUp.setLayout(mmUpTCLayout);
		FormData mmUpFD = new FormData();
		mmUpFD.top = new FormAttachment(0,5);
		mmUpFD.bottom = new FormAttachment(100,-43);
		mmUpFD.left = new FormAttachment(0,5);
		mmUpFD.right = new FormAttachment(100,5);
		mmUp.setLayoutData(mmUpFD);
		
		mmTableViewer = new TableViewer(mmUp, SWT.BORDER | SWT.FULL_SELECTION);
		mmTable = mmTableViewer.getTable();
		mmTable.setHeaderVisible(true);
		mmTable.setLinesVisible(true);
		formToolkit.paintBordersFor(mmTable);
		
		TableColumn mmTCDishName = new TableColumn(mmTable, SWT.CENTER);
		mmTCDishName.setText("\u6FC0\u6D3B\u7801");
		mmUpTCLayout.setColumnData(mmTCDishName, new ColumnWeightData(150,100,false));
        
		mmTableViewer.setContentProvider(new KeyInfoContentProvider());
		mmTableViewer.setLabelProvider(new KeyInfoLabelProvider());
		mmTableViewer.setInput(KeyInfoFactory.getKeyInfo());
		
		Composite mmDown = new Composite(mmParent, SWT.NONE);
		formToolkit.adapt(mmDown);
		RowLayout mmDownRLayout = new RowLayout(SWT.HORIZONTAL);
		mmDownRLayout.justify = true;
		mmDownRLayout.center = true;
		mmDown.setLayout(mmDownRLayout);
		FormData mmDownFD = new FormData();
		mmDownFD.top = new FormAttachment(100, -38);
		mmDownFD.bottom = new FormAttachment(100, -5);
		mmDownFD.left = new FormAttachment(0,5);
		mmDownFD.right = new FormAttachment(100,5);
		mmDown.setLayoutData(mmDownFD);
		
		Button mmDownBTNView = new Button(mmDown, SWT.NONE);
		formToolkit.adapt(mmDownBTNView, true, true);
		mmDownBTNView.setText("\u751F\u6210\u6FC0\u6D3B\u7801");
		
		Button btnNewButton = new Button(mmDown, SWT.NONE);
		formToolkit.adapt(btnNewButton, true, true);
		btnNewButton.setText("\u5237\u65B0\u5217\u8868");
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mmTableViewer.setInput(KeyInfoFactory.getKeyInfo());
			}
		});
		
		mmDownBTNView.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//Éú³É¼¤»îÂë
				ControlDB dsm = ControlDB.getInstance();
				
				String sql = "INSERT INTO keyInfo(activeKey) VALUES('" + System.currentTimeMillis() + "')";
				
				//System.out.println(sql);
				
				try {
					dsm.executeUpdate(sql);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				mmTableViewer.setInput(KeyInfoFactory.getKeyInfo());
			}
		});

	}

}
