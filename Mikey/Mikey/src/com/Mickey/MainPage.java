package com.Mickey;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author GV
 * 
 */
public class MainPage extends ActivityGroup {
	// private static boolean ifEnd = false;
	private GridView gvTopBar;
	private ImageAdapter topImgAdapter;
	public LinearLayout container;// 装载sub Activity的容器
	private int screenHeight;
	private InfoStore infoStore;
	// private int screenWidth;
	/** 顶部按钮图片 **/
	int[] topbar_image_array = { R.drawable.background_common, R.drawable.background_type,
			R.drawable.background_search };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*
		 * Thread t = new Thread() { public void run() { MyServer server = new
		 * MyServer(); //server.delAllFile("mnt/sdcard/Dishes");
		 * server.ServerStart(); synchronized (this) { ifEnd = true; } } };
		 * 
		 * t.start();
		 * 
		 * while (!ifEnd) { try { Thread.sleep(1000); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
		/* Login */
		
		
		
		
		/* Login end */

		gvTopBar = (GridView) this.findViewById(R.id.gvTopBar);
		gvTopBar.setNumColumns(topbar_image_array.length);// 设置每行列数
		gvTopBar.setSelector(new ColorDrawable(Color.TRANSPARENT));// 选中的时候为透明色
		gvTopBar.setGravity(Gravity.CENTER);// 位置居中
		gvTopBar.setVerticalSpacing(0);// 垂直间隔
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels;
		// screenWidth = dm.widthPixels;
		int height = this.getWindowManager().getDefaultDisplay().getHeight();
		int width = this.getWindowManager().getDefaultDisplay().getWidth()
				/ topbar_image_array.length;
		topImgAdapter = new ImageAdapter(this, topbar_image_array, width,
				height / 15, R.drawable.bkg1);
		gvTopBar.setAdapter(topImgAdapter);// 设置菜单Adapter

		gvTopBar.setOnItemClickListener(new ItemClickEvent());// 项目点击事件

		container = (LinearLayout) findViewById(R.id.Container);

		String IPAddress = "";
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
			IPAddress = br.readLine();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		/* Configuration files initialization end */

		/* Dish List Initialization */

		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		f = new File("mnt/sdcard/Dishes/dish.dat");
		if (f.exists()) {
			try {
				in = new BufferedInputStream(new FileInputStream(f));
			} catch (FileNotFoundException e3) {
			}
			try {
				br = new BufferedReader(new InputStreamReader(in, "gb2312"));
			} catch (UnsupportedEncodingException e1) {
			}
			String tmp;
			try {
				int length = Integer.parseInt(br.readLine());
				for (i = 1; i <= length; i++) {
					String dishNumber = Integer.toString(i);
					tmp = br.readLine();
					String type = tmp;
					tmp = br.readLine();
					String dishName = tmp;
					tmp = br.readLine();
					String dishMix = tmp;
					tmp = br.readLine();
					String price = tmp;
					tmp = br.readLine();
					String imageSrc = tmp;
					Map<String, Object> list = new HashMap<String, Object>();
					list.put("itemImage", getBitMap(imageSrc, 7));
					list.put("itemType", type);
					list.put("itemNo", dishNumber);
					list.put("itemName", dishName);
					list.put("itemMix", dishMix);
					list.put("itemPrice", price);
					list.put("imageSrc", imageSrc);
					lists.add(list);
				}
				br.close();
				in.close();
			} catch (IOException e) {
			}
		}
		/* Dish List Initialization End */

		/* Dish Type Initialization */
		String[] typeLists = new String[0];
		if (f.exists()) {
			f = new File("mnt/sdcard/Dishes/class.dat");
			try {
				in = new BufferedInputStream(new FileInputStream(f));
			} catch (FileNotFoundException e3) {
			}
			try {
				br = new BufferedReader(new InputStreamReader(in, "gb2312"));
			} catch (UnsupportedEncodingException e1) {
			}
			try {
				int length = Integer.parseInt(br.readLine());
				typeLists = new String[length];
				for (i = 0; i < length; i++)
					typeLists[i] = br.readLine();
				br.close();
				in.close();
			} catch (IOException e) {
			}
		}
		/* Dish Type Initialization End */

		/* Dish Order Initialization */
		Map<String, Object> dishOrder = new HashMap<String, Object>();
		Map<String, Object> dishUnOrder = new HashMap<String, Object>();
		/* Dish Order Initialization End */

		/* Discount Info Initialization */
		Map<String, Object> discountInfo = new HashMap<String, Object>();
		if (f.exists()) {
			f = new File("mnt/sdcard/Dishes/discount.dat");
			try {
				in = new BufferedInputStream(new FileInputStream(f));
			} catch (FileNotFoundException e3) {
			}
			try {
				br = new BufferedReader(new InputStreamReader(in, "gb2312"));
			} catch (UnsupportedEncodingException e1) {
			}
			try {
				int length = Integer.parseInt(br.readLine());
				for (i = 0; i < length; i++)
					discountInfo.put(br.readLine(), br.readLine());
				br.close();
				in.close();
			} catch (IOException e) {
			}
		}
		/* Discount Info Initialization End */

		infoStore = (InfoStore) getApplication();
		infoStore.setLists(lists);
		infoStore.setTypeLists(typeLists);
		infoStore.setOrderInfo(dishOrder);
		infoStore.setUnorderInfo(dishUnOrder);
		infoStore.setDisAccountInfo(discountInfo);
		infoStore.setCosumptionNo(new String());
		infoStore.setIPAddress(IPAddress);
		infoStore.setMainGroup(this);
		infoStore.setIfLogin(false);
		UpdateStatus updateStatus = new UpdateStatus(infoStore);
		SwitchActivity(3);// 默认打开第3页
	}

	Bitmap getBitMap(String imageSrc, int scale) {
		Bitmap bit;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bit = BitmapFactory
				.decodeFile("mnt/sdcard/Dishes/" + imageSrc, options);
		options.inJustDecodeBounds = false;
		int be = (int) (options.outHeight / (float) (screenHeight / scale));
		if (be <= 0)
			be = 1;
		// Log.v("be", Integer.toString(be));
		options.inSampleSize = be;
		bit = BitmapFactory
				.decodeFile("mnt/sdcard/Dishes/" + imageSrc, options);
		return bit;
	}

	class ItemClickEvent implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// Log.v("arg2", Integer.toString(arg2));
			// Log.v("arg3", Long.toString(arg3));
			SwitchActivity(arg2);
		}
	}

	/**
	 * 根据ID打开指定的Activity
	 * 
	 * @param id
	 *            GridView选中项的序号
	 */
	void SwitchActivity(int id) {
		Log.v("00", "00");
		if(infoStore.isIfLogin() == false && id < 3)
			return;
		if(id < 3)
			topImgAdapter.SetFocus(id);// 选中项获得高亮
		container.removeAllViews();// 必须先清除容器中所有的View
		Intent intent = null;
		if (id == 1)
			intent = new Intent(MainPage.this, DishAccordingToClass.class);
		else if (id == 0)
			intent = new Intent(MainPage.this, DishCommon.class);
		else if (id == 2) 
			intent = new Intent(MainPage.this, DishSearch.class);
		else
			intent = new Intent(MainPage.this, Login.class);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// Activity 转为 View
		Window subActivity = getLocalActivityManager().startActivity(
				"subActivity", intent);
		// 容器添加View
		container.addView(subActivity.getDecorView(), LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

}