package bit.scallionchickenrice.raph.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Button;

import bit.scallionchickenrice.raph.DAO.ControlDB;
import bit.scallionchickenrice.raph.communication.ProcessSocket;
import bit.scallionchickenrice.raph.datafactory.HistoryDataFactory;
import bit.scallionchickenrice.raph.datafactory.MenuManagementFactory;
import bit.scallionchickenrice.raph.datafactory.UserClientFactory;
import bit.scallionchickenrice.raph.security.AuthorizeTool;
import bit.scallionchickenrice.raph.tool.HistoryDataContentProvider;
import bit.scallionchickenrice.raph.tool.HistoryDataLabelProvider;
import bit.scallionchickenrice.raph.tool.MenuManagementContentProvider;
import bit.scallionchickenrice.raph.tool.MenuManagementLabelProvider;
import bit.scallionchickenrice.raph.tool.UserClientContentProvider;
import bit.scallionchickenrice.raph.tool.UserClientLabelProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class RaphMainApp {

	protected Shell shlRaph;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table rcTable;
	private Table mmTable;
	private Table hdTable;
	
	private TableViewer rcTableViewer;
	private TableViewer mmTableViewer;
	private TableViewer hdTableViewer;
	private Text text;
	
	private final static String AUTSERADDRESS = "192.168.0.12";
	private final static int AUTSERPORT = 7798;
			
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
			
			RaphMainApp window = new RaphMainApp();
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
		shlRaph.open();
		shlRaph.layout();
		while (!shlRaph.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlRaph = new Shell();
		shlRaph.setSize(660, 424);
		shlRaph.setText("Raph");
		shlRaph.setMaximized(true);
		shlRaph.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		setOffline();
		
		TabFolder tabFolder = new TabFolder(shlRaph, SWT.NONE);
		
		try {
			if(AuthorizeTool.validate()) {
			
				shlRaph.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						rcTableViewer.setInput(UserClientFactory.getUserClient());
						mmTableViewer.setInput(MenuManagementFactory.getMenuManagement());
						hdTableViewer.setInput(HistoryDataFactory.getHistoryData());
					}
				});
				
				TabItem tbtmRuntimeCondition = new TabItem(tabFolder, SWT.NONE);
				tbtmRuntimeCondition.setText("\u8FD0\u884C\u76D1\u63A7");
				
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
				rcTCUsername.setText("\u7528\u6237\u540D");
				rcUpTCLayout.setColumnData(rcTCUsername, new ColumnWeightData(150,100,false));
			    
			    TableColumn rcTCPassword = new TableColumn(rcTable, SWT.CENTER);
			    rcTCPassword.setText("\u5BC6\u7801");
			    rcUpTCLayout.setColumnData(rcTCPassword, new ColumnWeightData(150,100,false));
			    
			    TableColumn tcTCUserType = new TableColumn(rcTable, SWT.CENTER);
			    tcTCUserType.setText("\u7528\u6237\u7C7B\u578B");
			    rcUpTCLayout.setColumnData(tcTCUserType, new ColumnWeightData(150,100,false));
			    
			    TableColumn rcTCCondition = new TableColumn(rcTable, SWT.CENTER);
			    rcTCCondition.setText("\u7528\u6237\u72B6\u6001");
			    rcUpTCLayout.setColumnData(rcTCCondition, new ColumnWeightData(150,100,false));
			    
			    TableColumn rcTCIPAddress = new TableColumn(rcTable, SWT.CENTER);
			    rcTCIPAddress.setText("IP\u5730\u5740");
			    rcUpTCLayout.setColumnData(rcTCIPAddress, new ColumnWeightData(150,100,false));
			    
			    rcTableViewer.setContentProvider(new UserClientContentProvider());
			    rcTableViewer.setLabelProvider(new UserClientLabelProvider());
			    rcTableViewer.setInput(UserClientFactory.getUserClient());
				
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
				rcDownBTNInsert.setText("\u6DFB\u52A0\u7528\u6237");
				
				rcDownBTNInsert.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						new InsertUserDialog(Display.getCurrent().getActiveShell(), 1).open();
					}
				});
				
				Button rcDownBTNEdit = new Button(rcDown, SWT.NONE);
				formToolkit.adapt(rcDownBTNEdit, true, true);
				rcDownBTNEdit.setText("\u4FEE\u6539\u4FE1\u606F");
				
				rcDownBTNEdit.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if(rcTableViewer.getTable().getSelection().length <= 0) {
							return ;
						}
						else {
							new EditUserDialog(Display.getCurrent().getActiveShell(), 1).open(rcTableViewer.getTable().getSelection());
						}
					}
				});
				
				Button rcDownBTNDelete = new Button(rcDown, SWT.NONE);
				formToolkit.adapt(rcDownBTNDelete, true, true);
				rcDownBTNDelete.setText("\u5220\u9664\u7528\u6237");
				
				rcDownBTNDelete.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if(rcTableViewer.getTable().getSelection().length <= 0) {
							return ;
						}
						else {
							new DeleteUserDialog(Display.getCurrent().getActiveShell(), 1).open(rcTableViewer.getTable().getSelection());
						}
					}
				});
				
				Button rcDownBTNRefresh = new Button(rcDown, SWT.NONE);
				formToolkit.adapt(rcDownBTNRefresh, true, true);
				rcDownBTNRefresh.setText("\u5237\u65B0\u5217\u8868");
				
				rcDownBTNRefresh.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						rcTableViewer.setInput(UserClientFactory.getUserClient());
					}
				});
				
				TabItem tbtmMenuManagement = new TabItem(tabFolder, SWT.NONE);
				tbtmMenuManagement.setText("\u83DC\u5355\u7BA1\u7406");
				
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
				mmTCDishName.setText("\u83DC\u54C1\u540D\u79F0");
				mmUpTCLayout.setColumnData(mmTCDishName, new ColumnWeightData(150,100,false));
			    
			    TableColumn mmTCDishType = new TableColumn(mmTable, SWT.CENTER);
			    mmTCDishType.setText("\u83DC\u54C1\u7C7B\u522B");
			    mmUpTCLayout.setColumnData(mmTCDishType, new ColumnWeightData(150,100,false));
			    
			    TableColumn mmTCDishResource = new TableColumn(mmTable, SWT.CENTER);
			    mmTCDishResource.setText("\u539F\u6599\u6E05\u5355");
			    mmUpTCLayout.setColumnData(mmTCDishResource, new ColumnWeightData(150,100,false));
			    
			    TableColumn mmTCDishPrice = new TableColumn(mmTable, SWT.CENTER);
			    mmTCDishPrice.setText("\u83DC\u54C1\u4EF7\u683C");
			    mmUpTCLayout.setColumnData(mmTCDishPrice, new ColumnWeightData(150,100,false));
			    
				mmTableViewer.setContentProvider(new MenuManagementContentProvider());
				mmTableViewer.setLabelProvider(new MenuManagementLabelProvider());
				mmTableViewer.setInput(MenuManagementFactory.getMenuManagement());
				
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
				mmDownBTNView.setText("\u6DFB\u52A0\u83DC\u54C1");
				
				mmDownBTNView.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						new InsertDishDialog(Display.getCurrent().getActiveShell(), 1).open();
					}
				});
				
				Button mmDownBTNInsert = new Button(mmDown, SWT.NONE);
				formToolkit.adapt(mmDownBTNInsert, true, true);
				mmDownBTNInsert.setText("\u4FEE\u6539\u83DC\u54C1");
				
				mmDownBTNInsert.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if(mmTableViewer.getTable().getSelection().length <= 0) {
							return ;
						}
						else {
							new EditDishDialog(Display.getCurrent().getActiveShell(), 1).open(mmTableViewer.getTable().getSelection());
						}
					}
				});
				
				Button mmDownBTNDelete = new Button(mmDown, SWT.NONE);
				formToolkit.adapt(mmDownBTNDelete, true, true);
				mmDownBTNDelete.setText("\u5220\u9664\u83DC\u54C1");
				
				mmDownBTNDelete.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if(mmTableViewer.getTable().getSelection().length <= 0) {
							return ;
						}
						else {
							new DeleteDishDialog(Display.getCurrent().getActiveShell(), 1).open(mmTableViewer.getTable().getSelection());
						}
					}
				});
				
				Button mmDownBTNDishClass = new Button(mmDown, SWT.NONE);
				formToolkit.adapt(mmDownBTNDishClass, true, true);
				mmDownBTNDishClass.setText("\u7C7B\u522B\u7BA1\u7406");
				
				mmDownBTNDishClass.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						new ClassManageDialog(Display.getCurrent().getActiveShell(), 1).open();
					}
				});
				
				Button mmDownBTNDiscount = new Button(mmDown, SWT.NONE);
				formToolkit.adapt(mmDownBTNDiscount, true, true);
				mmDownBTNDiscount.setText("\u6298\u6263\u7BA1\u7406");
				
				mmDownBTNDiscount.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						new DiscountManageDialog(Display.getCurrent().getActiveShell(), 1).open();
					}
				});
				
				TabItem tbtmHistoryData = new TabItem(tabFolder, SWT.NONE);
				tbtmHistoryData.setText("\u5386\u53F2\u6570\u636E");
				
				Composite hdParent = new Composite(tabFolder, SWT.NONE);
				tbtmHistoryData.setControl(hdParent);
				formToolkit.paintBordersFor(hdParent);
				hdParent.setLayout(new FormLayout());
				
				Composite hdUp = new Composite(hdParent, SWT.NONE);
				formToolkit.paintBordersFor(hdUp);
				TableColumnLayout hdUpTCLayout = new TableColumnLayout();
				hdUp.setLayout(hdUpTCLayout);
				FormData hdUpFD = new FormData();
				hdUpFD.top = new FormAttachment(0,5);
				hdUpFD.bottom = new FormAttachment(100,-43);
				hdUpFD.left = new FormAttachment(0,5);
				hdUpFD.right = new FormAttachment(100,5);
				hdUp.setLayoutData(hdUpFD);
				
				hdTableViewer = new TableViewer(hdUp, SWT.BORDER | SWT.FULL_SELECTION);
				hdTable = hdTableViewer.getTable();
				hdTable.setHeaderVisible(true);
				hdTable.setLinesVisible(true);
				formToolkit.paintBordersFor(hdTable);
				
				TableColumn hdTCOrderId = new TableColumn(hdTable, SWT.CENTER);
				hdTCOrderId.setText("\u6D88\u8D39\u5355\u53F7");
				hdUpTCLayout.setColumnData(hdTCOrderId, new ColumnWeightData(150,100,false));
				
				TableColumn hdTCTalbeNumber = new TableColumn(hdTable, SWT.CENTER);
			    hdTCTalbeNumber.setText("\u6D88\u8D39\u684C\u53F7");
			    hdUpTCLayout.setColumnData(hdTCTalbeNumber, new ColumnWeightData(150,100,false));
			    
			    TableColumn hdTCHistoryDiscount = new TableColumn(hdTable, SWT.CENTER);
			    hdTCHistoryDiscount.setText("\u6298\u6263\u4FE1\u606F");
			    hdUpTCLayout.setColumnData(hdTCHistoryDiscount, new ColumnWeightData(150,100,false));
			    
			    TableColumn hdTCIfPaid = new TableColumn(hdTable, SWT.CENTER);
			    hdTCIfPaid.setText("\u662F\u5426\u652F\u4ED8");
			    hdUpTCLayout.setColumnData(hdTCIfPaid, new ColumnWeightData(150,100,false));
				
			    TableColumn hdTCHistoryPrice = new TableColumn(hdTable, SWT.CENTER);
			    hdTCHistoryPrice.setText("\u6D88\u8D39\u91D1\u989D");
			    hdUpTCLayout.setColumnData(hdTCHistoryPrice, new ColumnWeightData(150,100,false));
			    
				TableColumn hdTCHistoryTime = new TableColumn(hdTable, SWT.CENTER);
				hdTCHistoryTime.setText("\u6D88\u8D39\u65F6\u95F4");
				hdUpTCLayout.setColumnData(hdTCHistoryTime, new ColumnWeightData(150,100,false));
			    
				hdTableViewer.setContentProvider(new HistoryDataContentProvider());
				hdTableViewer.setLabelProvider(new HistoryDataLabelProvider());
				hdTableViewer.setInput(HistoryDataFactory.getHistoryData());
				
				Composite hdDown = new Composite(hdParent, SWT.NONE);
				formToolkit.adapt(hdDown);
				RowLayout hdDownRLayout = new RowLayout(SWT.HORIZONTAL);
				hdDownRLayout.justify = true;
				hdDownRLayout.center = true;
				hdDown.setLayout(hdDownRLayout);
				FormData hdDownFD = new FormData();
				hdDownFD.top = new FormAttachment(100, -38);
				hdDownFD.bottom = new FormAttachment(100, -5);
				hdDownFD.left = new FormAttachment(0,5);
				hdDownFD.right = new FormAttachment(100,5);
				hdDown.setLayoutData(hdDownFD);
				
				Button hdDownBTNView = new Button(hdDown, SWT.NONE);
				hdDownBTNView.setText("\u67E5\u770B\u8BE6\u60C5");
				formToolkit.adapt(hdDownBTNView, true, true);
				
				hdDownBTNView.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if(hdTableViewer.getTable().getSelection().length <= 0) {
							return ;
						}
						else {
							new HistoryDetailDialog(Display.getCurrent().getActiveShell(), 1).open(hdTableViewer.getTable().getSelection());
						}
					}
				});
				
				Button hdDownBTNRefresh = new Button(hdDown, SWT.NONE);
				hdDownBTNRefresh.setText("\u5237\u65B0\u5217\u8868");
				formToolkit.adapt(hdDownBTNRefresh, true, true);
				
				hdDownBTNRefresh.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						hdTableViewer.setInput(HistoryDataFactory.getHistoryData());
					}
				});
				
			}
			else {
				TabItem tbtmSystemSetting = new TabItem(tabFolder, SWT.NONE);
				tbtmSystemSetting.setText("\u7CFB\u7EDF\u6388\u6743");
				
				Composite composite = new Composite(tabFolder, SWT.NONE);
				tbtmSystemSetting.setControl(composite);
				formToolkit.paintBordersFor(composite);
				composite.setLayout(new GridLayout(5, false));
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				
				Label label_1 = new Label(composite, SWT.NONE);
				formToolkit.adapt(label_1, true, true);
				label_1.setText("    \u6FC0\u6D3B\u7801    ");
				new Label(composite, SWT.NONE);
				
				text = new Text(composite, SWT.BORDER);
				text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				formToolkit.adapt(text, true, true);
				
				Label label = new Label(composite, SWT.NONE);
				formToolkit.adapt(label, true, true);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				new Label(composite, SWT.NONE);
				
				Button btnNewButton = new Button(composite, SWT.NONE);
				formToolkit.adapt(btnNewButton, true, true);
				btnNewButton.setText("       \u6FC0\u6D3B       ");
				
				btnNewButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Socket mySocket;
						try {
							mySocket = new Socket(AUTSERADDRESS, AUTSERPORT);
							DataOutputStream out = new DataOutputStream(mySocket.getOutputStream());
							out.writeUTF("0 " + text.getText().replaceAll(" ", "") + " " + AuthorizeTool.getMACAddress() + " " + "²ÍÌü·þÎñÆ÷");
							
							DataInputStream dis = new DataInputStream(mySocket.getInputStream());
							DataOutputStream dos = new DataOutputStream(mySocket.getOutputStream());
							byte[] buffer = new byte[1000];
							int length = dis.read(buffer);
							
							dos.write("g".getBytes());
							
							String ifSuccess = new String(buffer, 0, length);
							
							if(ifSuccess.equals("true")) {
								FileOutputStream fos=new FileOutputStream(new File("boot.ini"));
								while(true) {
									int read = -1;
									read = dis.read(buffer);
									
									if(read <= 0) {
										break;
									}
									else {
										fos.write(buffer, 0, read);
									}
								}
								fos.flush();
								fos.close();
								
								new AuthorizeSuccessDialog(Display.getCurrent().getActiveShell(), 1).open();
								shlRaph.close();
							}
							else {
								new AuthorizeFailDialog(Display.getCurrent().getActiveShell(), 1).open();
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setOffline() {
		// TODO Auto-generated method stub
		ControlDB dsm = ControlDB.getInstance();
		
		String sql = "UPDATE userInfo SET ifOnline = 0, ipAddress = NULL WHERE 1 = 1";
		
		try {
			dsm.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
