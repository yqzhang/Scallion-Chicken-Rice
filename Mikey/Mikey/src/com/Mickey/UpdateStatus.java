package com.Mickey;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;


public class UpdateStatus {
	private InfoStore infoStore;
	private ServerSocket server = null;
	private ExecutorService mExecutorService = null;
	private static final int PORT = 9998;
	private List<Socket> mList = new ArrayList<Socket>();
	public UpdateStatus(InfoStore infoStore)
	{
		this.infoStore = infoStore;
		Thread t = new Thread() {
			public void run() {
				try {
					server = new ServerSocket(PORT);
					mExecutorService = Executors.newCachedThreadPool();
					Socket client = null;
					while (true) {
						client = server.accept();
						mList.add(client);
						mExecutorService.execute(new Service(client));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	


	public class Service implements Runnable {

		private Socket socket;
		private String msg = "";

		public Service(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			try {
				DataInputStream in = new DataInputStream(socket
						.getInputStream());
				if ((msg = in.readUTF()) != null) {
					System.out.println(msg);
					Process ps = new Process(socket);
					ps.query(msg);
					socket.close();
					mList.remove(socket);
				}
			} catch (Exception ex) {
				System.out.println("server 读取数据异常");
				ex.printStackTrace();
			}
		}
	}
	
	
	 class Process {
			
			private Socket socket;
			public Process(Socket socket) {
				this.socket = socket;
			}			
			public void query(String str) throws Exception {
				String[] temp = str.split(" ");
				//0 dishname statusbefore statusafter
				 Map<String,Object> orderList = infoStore.getOrderInfo();
				 List<Map<String,Object>> dishLists = infoStore.getLists();
				 int i = 0;
				 for(i=0;i < dishLists.size();i++)
					 if(dishLists.get(i).get("itemName").toString().equalsIgnoreCase(temp[1]) == true)
						 {
							 String itemNo = dishLists.get(i).get("itemNo").toString();
							 String statusTmp = orderList.get(itemNo).toString();
							 orderList.put(itemNo, statusTmp.split("&")[0]+"&"+temp[3]);
							 break;							 
						 }
				 infoStore.setOrderInfo(orderList);
				 
			}
		}
	
}
