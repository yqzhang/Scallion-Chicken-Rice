package com.Don;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import com.Don.DonActivity.ipAddressClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class IpAddress {
	private AlertDialog alertDialog;
	private AlertDialog.Builder builder;
	private EditText ipAddressEditText;
	private Button ipAddressChangeBtn;
	private Button cancelIpAddressBtn;

	public AlertDialog getAlertDialog() {
		return alertDialog;
	}

	public void setAlertDialog(AlertDialog alertDialog) {
		this.alertDialog = alertDialog;
	}


	
	public IpAddress(final Activity activitySource,final ipAddressClass ipAddressSet) throws IOException {
		
		LayoutInflater inflater = (LayoutInflater) activitySource
		.getSystemService("layout_inflater");
		View layout = inflater.inflate(R.layout.ip_address, null);
		builder = new AlertDialog.Builder(activitySource);
		builder.setView(layout);
		alertDialog = builder.create();// 通过构造器产生一个对话框

		ipAddressChangeBtn = (Button) layout.findViewById(R.id.ipAddressChangeBtn);
		cancelIpAddressBtn = (Button) layout.findViewById(R.id.cancelIpAddressBtn);
		ipAddressEditText = (EditText) layout.findViewById(R.id.ipAddressEditText);
		
		InputStream in = null;
		BufferedReader br = null;
		File f = null;
		
		f = new File("mnt/sdcard/CJFConfiguration/IP.dat");
		in = new BufferedInputStream(new FileInputStream(f));
		br = new BufferedReader(new InputStreamReader(in, "gb2312"));

		ipAddressEditText.setText(br.readLine());
		ipAddressSet.setIpAddress(br.readLine());
		in.close();
		br.close();
		
		//Log.v("2b", "2b");
		
		cancelIpAddressBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OutputStream out = null;
				BufferedWriter bw = null;
				File f = new File("mnt/sdcard/CJFConfiguration/IP.dat");
				try {
					out = new BufferedOutputStream(new FileOutputStream(f));
					try {
						bw = new BufferedWriter(new OutputStreamWriter(out,"gb2312"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						bw.write(ipAddressEditText.getText().toString());
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
				ipAddressSet.setIpAddress(ipAddressEditText.getText().toString());
				alertDialog.cancel();
			}
		});

		ipAddressChangeBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] str = ipAddressEditText.getText().toString().split("\\.");
				if(str.length != 4)
				{
					Toast.makeText(activitySource,
						 	"IP地址格式错误", 
	                        Toast.LENGTH_LONG).show();
					return;
				}
				for(int i=0;i<str.length;i++){
					if(Integer.parseInt(str[i]) > 255){//转换类型并比较字符串是否小于255
						Toast.makeText(activitySource,
							 	"IP地址格式错误", 
		                        Toast.LENGTH_LONG).show();
						return;
					}
				}
				Toast.makeText(activitySource,
					 	"修改成功", 
                        Toast.LENGTH_LONG).show();
				//alertDialog.cancel();
			}
		});
		
	
	}

}
