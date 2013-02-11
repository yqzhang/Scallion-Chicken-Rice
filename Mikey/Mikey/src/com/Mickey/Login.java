package com.Mickey;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private InfoStore infoStore;
	private EditText loginUsername;
	private EditText loginPassword;
	private EditText loginIpAddress;
	private EditText jhEdit;
	private EditText deskNo;
	private Button loginSubmit;
	private Button loginCancel;
	private Button validateSubmit;
	private Button validateCancel;
	private AlertDialog alertDialog;
	private AlertDialog alertDialogLogin;
	private boolean ifValidation = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		infoStore = (InfoStore) getApplication();
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.login);
		this.setTitle("Login");
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
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
			inflater = (LayoutInflater) Login.this
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.validate, null);
			builder = new AlertDialog.Builder(Login.this);// 创建一个弹出对话框构造器
			builder.setView(layout);
			alertDialog = builder.create();// 通过构造器产生一个对话框
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
						Toast.makeText(Login.this, "激活码不能为空", Toast.LENGTH_LONG)
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
								+ AuthorizeTool.getMACAddress(Login.this) + " "
								+ "点餐客户端");
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
						
						Toast.makeText(Login.this, "授权成功", Toast.LENGTH_LONG)
						.show();
						alertDialog.cancel();
						alertDialogLogin.show();
					} else {
						// 授权失败
						Toast.makeText(Login.this, "授权失败", Toast.LENGTH_LONG)
								.show();
					}
				}
			});

			validateCancel.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Login.this.finish();
				}
			});
			/* validate end */
			alertDialog.show();
		}


		/* Login window */
		inflater = (LayoutInflater) Login.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.login, null);
		builder = new AlertDialog.Builder(Login.this);// 创建一个弹出对话框构造器
		builder.setView(layout);
		alertDialogLogin = builder.create();// 通过构造器产生一个对话框

		loginUsername = (EditText) layout.findViewById(R.id.loginUsername);
		loginPassword = (EditText) layout.findViewById(R.id.loginPassword);
		loginIpAddress = (EditText) layout.findViewById(R.id.loginIpAddress);
		deskNo = (EditText) layout.findViewById(R.id.deskNo);
		loginSubmit = (Button) layout.findViewById(R.id.loginSubmit);
		loginCancel = (Button) layout.findViewById(R.id.loginCancel);
		
		loginIpAddress.setText(infoStore.getIPAddress());
		
		loginSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Log.v("ip", infoStore.getIPAddress());
				String username = loginUsername.getText().toString();
				String password = loginPassword.getText().toString();
				String ipAddress = loginIpAddress.getText().toString();
				String deskno = deskNo.getText().toString();
				if (deskno.length() == 0 ||username.length() == 0 || password.length() == 0 || ipAddress.length() == 0) {
					Toast.makeText(Login.this, "必要信息不能为空", Toast.LENGTH_LONG)
							.show();
					return;
				}
				infoStore.setIPAddress(ipAddress);
				infoStore.setDeskNo(deskno);
				// TODO Auto-generated method stub
				Socket socket = null;
				//Log.v("ip", infoStore.getIPAddress());
				try {
					socket = new Socket(infoStore.getIPAddress(), 7797);
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

				Log.v("ifSuccess", ifSuccess);
				if (ifSuccess.equals("true")) {
					infoStore.setIfLogin(true);
					infoStore.setUserName(username);
					infoStore.getMainGroup().SwitchActivity(0);
					Toast.makeText(Login.this, "登录成功", Toast.LENGTH_LONG)
							.show();
					
					File f = new File("mnt/sdcard/CJFConfiguration/IP.dat");
					OutputStream outIp = null;
					BufferedWriter bw = null;
					try {
						outIp = new BufferedOutputStream(new FileOutputStream(f));
						try {
							bw = new BufferedWriter(new OutputStreamWriter(outIp,"gb2312"));
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
					alertDialogLogin.cancel();
				} else {
					Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		loginCancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Login.this.finish();
			}
		});
		if(ifValidation == true)
			alertDialogLogin.show();
		/* Login window end */
	}
}
