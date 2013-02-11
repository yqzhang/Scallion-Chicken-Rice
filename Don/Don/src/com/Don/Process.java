package com.Don;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class Process {
	
	private Socket socket;
	public Process(Socket socket2) {
		this.socket = socket2;
	}
	
	public void query(String str,List<Map<String, Object>> lists) throws Exception {
		Log.v("dddd1", str);
		String[] temp = str.split(" ");
		int type = Integer.parseInt(temp[0]);
		String deskNo = null;
		String dishTime = null;
		String dishNo = null;
		String orderId = null;
		//System.out.println("type:" + type);
		
		switch(type) {
		case(0):
			orderId = temp[1];
			deskNo = temp[2];
			dishNo = temp[3];
			dishTime = temp[5];
			for(int i = 6;i < temp.length;i++)
			{
				Map<String, Object> mapTmp = new HashMap<String,Object>();
				mapTmp.put("orderId", orderId);
				Log.v("orderId",orderId);
				mapTmp.put("itemDesk", deskNo);//Log.v("deskNo",deskNo);
				mapTmp.put("itemTime", dishTime);//Log.v("dishTime",dishTime);
				mapTmp.put("itemName", temp[i]);//Log.v("temp[i]",temp[i]);
				mapTmp.put("itemAmount", temp[i + 1]);//Log.v("temp[i + 1]",temp[i + 1]);
				mapTmp.put("itemStatus", "µÈ´ýÖÆ×÷");
				i++;
				lists.add(mapTmp);

			}
		case(1):
			orderId = temp[1];
			String dishName = temp[2];
			
			for(int i = 0;i < lists.size();i++)
			{
				Map<String, Object> mapTmp = lists.get(i);
				if(mapTmp.get("orderId").toString().equalsIgnoreCase(orderId) && mapTmp.get("itemName").toString().equalsIgnoreCase(dishName))
				{
					Log.v("good", "good");
					int dishAmountTmp = Integer.parseInt(mapTmp.get("itemAmount").toString());
					dishAmountTmp--;
					if(dishAmountTmp == 0)
						lists.remove(i);
					else
						mapTmp.put("itemAmount", Integer.toString(dishAmountTmp));
					break;
				}
			}
			
		}
	}
}
