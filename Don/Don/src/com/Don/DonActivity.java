package com.Don;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

public class DonActivity extends Activity {
	/** Called when the activity is first created. */
	private List<Map<String, Object>> lists;
	private TableLayout tableRoot;
	private static final int PORT = 9998;
	private List<Socket> mList = new ArrayList<Socket>();
	private ServerSocket server = null;
	private Button updateTimeBtn;
	private Button iPAddressBtn;
	private EditText updateTimeText;
	private ExecutorService mExecutorService = null;
	private int updateTime = 4;
	private Timer timer = null;
	private TimerTask task;
	private Handler handler;
	private ipAddressClass ipAddressSet = new ipAddressClass();
	private EditText loginUsername;
	private EditText loginPassword;
	private EditText loginIpAddress;
	private EditText jhEdit;
	private Button loginSubmit;
	private Button loginCancel;
	private Button validateSubmit;
	private Button validateCancel;
	private AlertDialog alertDialog;
	private boolean ifLogin = false;
	private AlertDialog alertDialogLogin;
	private boolean ifValidation = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Configuration File Initialization */
		InputStream in = null;
		BufferedReader br = null;
		File f = null;
		int i;
		f = new File("mnt/sdcard/Dishes");
		if (f.exists() == false)
			f.mkdirs();
		/* Configuration files initialization */
		f = new File("mnt/sdcard/CJFConfiguration");
		if (f.exists() == false)
			f.mkdirs();
		f = new File("mnt/sdcard/CJFConfiguration/IP.dat");
		if (f.exists() == false)
			try {
				f.createNewFile();
			} catch (IOException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
		try {

			in = new BufferedInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e3) {
		}
		try {
			br = new BufferedReader(new InputStreamReader(in, "gb2312"));
		} catch (UnsupportedEncodingException e1) {
		}
		try {
			ipAddressSet.setIpAddress(br.readLine());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		LayoutInflater inflater;
		View layout;
		AlertDialog.Builder builder;
		try {
			ifValidation = AuthorizeTool.validate(this);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		if (ifValidation == false) {
			// 授权成功
			/* validate */
			inflater = (LayoutInflater) DonActivity.this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.validate, null);
			builder = new AlertDialog.Builder(DonActivity.this);// 创建一个弹出对话框构造器
			builder.setView(layout);
			alertDialog = builder.create();// 通过构造器产生一个对话框
			Log.v("s", "s");
			jhEdit = (EditText)layout.findViewById(R.id.jhEdit);
			validateSubmit = (Button) layout.findViewById(R.id.validateSubmit);
			validateCancel = (Button) layout.findViewById(R.id.validateCancel);
			validateSubmit.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String jhString = jhEdit.getText().toString();
					if(jhString.length() == 0)
					{
						Toast.makeText(DonActivity.this, "激活码不能为空", Toast.LENGTH_LONG)
						.show();
						return;						
					}
					Socket socket = null;
					try {
						socket = new Socket("192.168.0.12", 7798);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DataOutputStream out = null;
					try {
						out = new DataOutputStream(socket
								.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 向授权服务器发送授权请求
					// 0 激活码 MAC地址 餐厅服务器/点菜客户端/厨师客户端
					try {
						out.writeUTF("0 " + jhString + " "
								+ AuthorizeTool.getMACAddress(DonActivity.this) + " "
								+ "厨师客户端");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					DataInputStream dis = null;
					try {
						dis = new DataInputStream(socket
								.getInputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DataOutputStream dos = null;
					try {
						dos = new DataOutputStream(socket
								.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					byte[] buffer = new byte[1000];
					int length = 0;
					try {
						length = dis.read(buffer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						dos.write("g".getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String ifSuccess = new String(buffer, 0, length);

					if (ifSuccess.equals("true")) {
						// 授权成功

						// 创建boot.ini文件
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(new File(
									"mnt/sdcard/CJFConfiguration/boot.ini"));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						while (true) {
							int read = -1;
							try {
								read = dis.read(buffer);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (read <= 0) {
								break;
							} else {
								try {
									fos.write(buffer, 0, read);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						try {
							fos.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Toast.makeText(DonActivity.this, "授权成功", Toast.LENGTH_LONG)
						.show();
						alertDialog.cancel();
						alertDialogLogin.show();
					} else {
						// 授权失败
						Toast.makeText(DonActivity.this, "授权失败", Toast.LENGTH_LONG)
								.show();
					}
				}
			});

			validateCancel.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DonActivity.this.finish();
				}
			});
			/* validate end */
			alertDialog.show();
		}

		/* Login */
		inflater = (LayoutInflater) DonActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.login, null);
		builder = new AlertDialog.Builder(DonActivity.this);// 创建一个弹出对话框构造器
		builder.setView(layout);
		alertDialogLogin = builder.create();// 通过构造器产生一个对话框

		loginUsername = (EditText) layout.findViewById(R.id.loginUsername);
		loginPassword = (EditText) layout.findViewById(R.id.loginPassword);
		loginSubmit = (Button) layout.findViewById(R.id.loginSubmit);
		loginCancel = (Button) layout.findViewById(R.id.loginCancel);
		loginIpAddress = (EditText) layout.findViewById(R.id.loginIpAddress);

		loginIpAddress.setText(ipAddressSet.getIpAddress());

		loginSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = loginUsername.getText().toString();
				String password = loginPassword.getText().toString();
				String ipAddress = loginIpAddress.getText().toString();
				if (username.length() == 0 || password.length() == 0
						|| ipAddress.length() == 0) {
					Toast.makeText(DonActivity.this, "必要信息不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}

				ipAddressSet.setIpAddress(ipAddress);
				// TODO Auto-generated method stub

				Socket socket = null;
				try {
					socket = new Socket(ipAddressSet.ipAddress, 7797);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				DataOutputStream out = null;
				try {
					out = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// 6 用户名 密码
				try {
					out.writeUTF("6 " + username + " " + password);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				DataInputStream dis = null;
				try {
					dis = new DataInputStream(socket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataOutputStream dos = null;
				try {
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				byte[] buffer = new byte[1000];
				int length = 0;
				try {
					length = dis.read(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					dos.write("g".getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String ifSuccess = new String(buffer, 0, length);

				ifSuccess = ifSuccess.replaceAll("\n", "").replace("\r", "");

				// Log.v("ifSuccess", ifSuccess);
				if (ifSuccess.equals("true")) {
					Toast.makeText(DonActivity.this, "登录成功", Toast.LENGTH_LONG)
							.show();

					File f = new File("mnt/sdcard/CJFConfiguration/IP.dat");
					OutputStream outIp = null;
					BufferedWriter bw = null;
					try {
						outIp = new BufferedOutputStream(
								new FileOutputStream(f));
						try {
							bw = new BufferedWriter(new OutputStreamWriter(
									outIp, "gb2312"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							bw.write(ipAddress);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							bw.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							bw.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					ifLogin = true;
					alertDialogLogin.cancel();
				} else {
					Toast.makeText(DonActivity.this, "登录失败", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		loginCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DonActivity.this.finish();
			}
		});
		/* Login end */

		if (ifValidation == true)
			alertDialogLogin.show();
		/* Configuration files initialization end */

		setContentView(R.layout.main);
		tableRoot = (TableLayout) findViewById(R.id.tableLayoutRoot);
		updateTimeBtn = (Button) findViewById(R.id.updateTimeBtn);
		updateTimeText = (EditText) findViewById(R.id.updateTimeText);
		iPAddressBtn = (Button) findViewById(R.id.iPAddressBtn);
		/*设置刷新时间*/
		updateTimeBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (updateTimeText.getText().length() == 0)
					updateTimeText.setText("4");
				updateTime = Integer.parseInt(updateTimeText.getText()
						.toString());
				timer.cancel();

				task = new TimerTask() {
					@Override
					public void run() {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				};

				timer = new Timer();
				timer.schedule(task, updateTime * 1000, updateTime * 1000);
			}
		});
		/* Dish List Initialization */
		lists = new ArrayList<Map<String, Object>>();
		/*SOCKET 接受每行点菜的信息*/
		Thread t = new Thread() {
			public void run() {
				try {
					server = new ServerSocket(PORT);
					mExecutorService = Executors.newCachedThreadPool();
					Socket client = null;
					while (true) {
						client = server.accept();
						//Log.v("dfj", "dfj");
						mList.add(client);
						mExecutorService.execute(new Service(client, lists));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
		/*SOCKET 接受每行点菜的信息 end*/
		/*设置IP地址*/
		iPAddressBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				IpAddress ipAddress = null;
				try {
					ipAddress = new IpAddress(DonActivity.this, ipAddressSet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AlertDialog tmpArtDialog = ipAddress.getAlertDialog();
				tmpArtDialog.show();
			}
		});
		/* Dish List Initialization End */
		// ////////////////
		handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				displayAllDishes();
				super.handleMessage(message);
			}
		};

		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};

		timer = new Timer();
		timer.schedule(task, updateTime * 1000, updateTime * 1000);

		// displayAllDishes(0);
	}

	void displayAllDishes() {
		Log.v("size", Integer.toString(lists.size()));
		if (tableRoot.getChildCount() > 1) {
			Log.v("count", Integer.toString(tableRoot.getChildCount()));
			tableRoot.removeViews(1, tableRoot.getChildCount() - 1);
		}
		Collections.sort(lists, new comparatorTimeDown());
		for (int i = 0; i < lists.size(); i++) {
			// list.put("itemImage",
			// getBitMap((String)lists.get(i).get("imageSrc"),10));
			TableRow row = new TableRow(this);
			TextView tmpName = new TextView(this);
			try {
				tmpName.setText(new String(lists.get(i).get("itemName")
						.toString().getBytes("UTF-8"), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			row.addView(tmpName);

			TextView tmpDesk = new TextView(this);
			tmpDesk.setText((String) lists.get(i).get("itemDesk"));
			row.addView(tmpDesk);

			TextView tmpAmount = new TextView(this);
			tmpAmount.setText((String) lists.get(i).get("itemAmount"));
			row.addView(tmpAmount);

			TextView tmpTime = new TextView(this);
			tmpTime.setText((String) lists.get(i).get("itemTime"));
			row.addView(tmpTime);

			// 4 orderId dishName before after
			List<String> kwDishTypeList = new ArrayList<String>();
			kwDishTypeList.add("等待制作");
			kwDishTypeList.add("正在制作");
			kwDishTypeList.add("制作完毕");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, kwDishTypeList);

			Spinner dishType = new Spinner(this);
			dishType.setAdapter(adapter);
			if (((String) lists.get(i).get("itemStatus"))
					.equalsIgnoreCase("等待制作"))
				dishType.setSelection(0);
			else if (((String) lists.get(i).get("itemStatus"))
					.equalsIgnoreCase("正在制作"))
				dishType.setSelection(1);
			else
				dishType.setSelection(2);
			final int j = i;

			dishType
					.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// Log.v("content",arg0.getSelectedItem().toString());
							final String kwDishType = arg0.getSelectedItem()
									.toString();
							final String dishStatusBefore = lists.get(j).get(
									"itemStatus").toString();
							if (kwDishType.equalsIgnoreCase(dishStatusBefore) == true)
								return;
							lists.get(j).put("itemStatus", kwDishType);

							Thread t = new Thread() {
								public void run() {
									Socket socket;
									try {
										socket = new Socket(ipAddressSet
												.getIpAddress(), 7797);
										DataOutputStream out = new DataOutputStream(
												socket.getOutputStream());
										out.writeUTF("4 "
												+ lists.get(j).get("orderId")
												+ " "
												+ lists.get(j).get("itemName")
												+ " " + dishStatusBefore + " "
												+ kwDishType);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// 4 orderId dishName before after
								}
							};
							t.start();

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
						}
					});

			row.addView(dishType);

			// row.setTextSize(16);
			tableRoot.addView(row, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		}
	}

	public class comparatorTimeDown implements Comparator {
		public int compare(Object o1, Object o2) {
			Map<String, Object> p1 = (Map<String, Object>) o1;
			Map<String, Object> p2 = (Map<String, Object>) o2;
			if (p1.get("itemTime").toString().compareTo(
					p2.get("itemTime").toString()) < 0)
				return 1;
			else if (p1.get("itemTime").toString().compareTo(
					p2.get("itemTime").toString()) == 0)
				return 0;
			else
				return -1;
		}
	}

	public class Service implements Runnable {

		private Socket socket;
		private String msg = "";
		private List<Map<String, Object>> lists;

		public Service(Socket socket, List<Map<String, Object>> lists) {
			this.socket = socket;
			this.lists = lists;
		}

		public void run() {
			// TODO Auto-generated method stub
			try {
				DataInputStream in = new DataInputStream(socket
						.getInputStream());
				if ((msg = in.readUTF()) != null) {
					System.out.println(msg);
					Process ps = new Process(socket);
					Log.v("dddd2", "ddd2");
					ps.query(msg, lists);
					socket.close();
					mList.remove(socket);
				}
			} catch (Exception ex) {
				System.out.println("server 读取数据异常");
				ex.printStackTrace();
			}
		}
	}

	class ipAddressClass {
		String ipAddress;

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

	}

}