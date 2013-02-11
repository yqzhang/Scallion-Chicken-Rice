package com.Mickey;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Mickey.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
public class DishAccordingToClass extends Activity {
	private ListView listView;
	private ListView dishType;
	//private EditText editText;

	private String[] dishTypeName;
	private int screenHeight;
	private int screenWidth;
	AlertDialog.Builder builder;
	private List<Map<String,Object>> lists;
	AlertDialog alertDialog;
	private InfoStore infoStore;
	private Button orderInfoClass;
	private Button synchType;
	private Button ipAddressType;
	/** 左边按钮图片 **/
	int[] topbar_image_array = { R.drawable.dishtypetransparent};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_according_class);
		this.setTitle("BaseAdapter for ListView");
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);    
		screenHeight = dm.heightPixels;	
		screenWidth = dm.widthPixels;
		infoStore = (InfoStore)getApplication();
		listView=(ListView)this.findViewById(R.id.MyListView);
		orderInfoClass = (Button)this.findViewById(R.id.orderInfoClass);
		synchType = (Button)this.findViewById(R.id.synchType);
		ipAddressType = (Button)this.findViewById(R.id.ipAddressType);
		//Log.v("successful","successful");
		displayDishType();
		//Log.v("successful","successful");
		displayDishAccodringToType(0);
		orderInfoClass.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						OrderInfo orderInfo =  new OrderInfo(DishAccordingToClass.this,infoStore,screenHeight,screenWidth);
						AlertDialog tmpArtDialog = orderInfo.getAlertDialog();
						tmpArtDialog.show();
					}				
				}				
			);
		ipAddressType.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						
						IpAddress ipAddress = null;
						try {
							ipAddress = new IpAddress(DishAccordingToClass.this,infoStore,screenHeight,screenWidth);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AlertDialog tmpArtDialog = ipAddress.getAlertDialog();
						tmpArtDialog.show();
					}				
				}				
			);
		synchType.setOnClickListener(
					new Button.OnClickListener()	
					{
						@Override
						public void onClick(View v)
						{
							//TODO Auto-generated method stub
							
							Sync sync =  new Sync(DishAccordingToClass.this,infoStore,screenHeight,screenWidth);
							AlertDialog tmpArtDialog = sync.getAlertDialog();
							tmpArtDialog.show();
						}				
					}				
				);
	}
	void displayDishType()
	{
		dishType=(ListView)this.findViewById(R.id.DishType);	
		List<Map<String,Object>> listsType=new ArrayList<Map<String,Object>>();
		
			dishTypeName = infoStore.getTypeLists();
			for(int i = 0;i < dishTypeName.length;i++){
				Map<String,Object> listType=new HashMap<String, Object>();
				listType.put("typeItemImage", topbar_image_array[0]);
				listType.put("typeItemText", dishTypeName[i]);
				listsType.add(listType);
			}
		 SimpleAdapter simpleAdapterType=new SimpleAdapter(this, listsType, R.layout.dish_type_list_element, new String[]{"typeItemImage","typeItemText"}, new int[]{R.id.typeItemImage,R.id.typeItemText});
		 dishType.setAdapter(simpleAdapterType);
		 dishType.setOnItemClickListener(new typeItemClickEvent());
	}
	class typeItemClickEvent implements OnItemClickListener {
	
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			displayDishAccodringToType(arg2);
		}
	}
	void displayDishAccodringToType(int typeNum)
	{
		lists = new ArrayList<Map<String,Object>>();	
		List<Map<String,Object>> listTmp = infoStore.getLists();
			for(int i = 0;i < listTmp.size();i++){
				if(dishTypeName[typeNum].equalsIgnoreCase((String)listTmp.get(i).get("itemType"))!=true)
					continue;
				Map<String,Object> list=new HashMap<String, Object>();
		            list.put("itemImage", getBitMap((String)listTmp.get(i).get("imageSrc"),10));
		            list.put("itemType", (String)listTmp.get(i).get("itemType"));
		            list.put("itemNo", (String)listTmp.get(i).get("itemNo"));
		            list.put("itemName", (String)listTmp.get(i).get("itemName"));
		            list.put("itemMix", "配料:"+(String)listTmp.get(i).get("itemMix"));
		            list.put("itemPrice", "价格:"+(String)listTmp.get(i).get("itemPrice")+"$");
		            list.put("imageSrc", (String)listTmp.get(i).get("imageSrc"));
		            lists.add(list);
		  }			   

			SimpleAdapter simpleAdapter=new SimpleAdapter(this, lists, R.layout.dish_list_element, new String[]{"itemImage","itemNo","itemName","itemPrice","itemMix","itemType"}, new int[]{R.id.itemImage,R.id.itemNo,R.id.itemName,R.id.itemPrice,R.id.itemMix,R.id.itemType}); listView.setAdapter(simpleAdapter);
		    simpleAdapter.setViewBinder(new ViewBinder() {    
	             public boolean setViewValue(  
	                                 View view,   
	                                 Object data,    
	                              String textRepresentation) {    
	                 //判断是否为我们要处理的对象    
	                 if(view instanceof ImageView  && data instanceof Bitmap){    
	                     ImageView iv = (ImageView) view;    
	                     iv.setImageBitmap((Bitmap) data);    
	                     return true;    
	                 }else    
	                 return false;    
	             }    
	         }); 
		listView.setOnItemClickListener(new ItemClickEvent());
		//Log.v("123", "123");
	}
	Bitmap getBitMap(String imageSrc,int scale)
	{
		Bitmap bit;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bit = BitmapFactory.decodeFile("mnt/sdcard/Dishes/"+imageSrc,options);
		options.inJustDecodeBounds = false;
		int be = (int)(options.outHeight/(float)(screenHeight/scale));
		if(be <= 0)
			be = 1;		
		options.inSampleSize = be;
		bit = BitmapFactory.decodeFile("mnt/sdcard/Dishes/"+imageSrc,options);
		return bit;
	}
	
	class ItemClickEvent implements OnItemClickListener {
		private Button buttonAdmit;
		private Button buttonCancel;
		private Button buttonPlus;
		private Button buttonMinus;
		private TextView number;
		private TextView dishInfo;
		private TextView dishMix;
		private ImageView dishImage;
		private Map<String,Object> listType;
		int itemIndex;
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			itemIndex = arg2;
			Log.v("index",Integer.toString(arg2));
			showDialog();
		} 
	private void showDialog() {
		//lists = null;
		//Log.v("index",Integer.toString(itemIndex));
		//Map<String,Object> listType=new HashMap<String, Object>();
		listType = lists.get(itemIndex);
		LayoutInflater inflater = (LayoutInflater)  DishAccordingToClass.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dish_detail, null);
		//get the widget in the alertDialog
		buttonAdmit = (Button) layout.findViewById(R.id.admit);
		buttonCancel = (Button) layout.findViewById(R.id.cancel);		
		dishImage = (ImageView)layout.findViewById(R.id.dishImage);
		buttonPlus = (Button)layout.findViewById(R.id.plus);
		buttonMinus =(Button)layout.findViewById(R.id.minus);
		number = (TextView)layout.findViewById(R.id.number);
		dishInfo = (TextView)layout.findViewById(R.id.dishInfo);
		dishMix = (TextView)layout.findViewById(R.id.dishMix);
		

		builder = new AlertDialog.Builder(DishAccordingToClass.this);// 创建一个弹出对话框构造器
		builder.setView(layout);
		alertDialog = builder.create();// 通过构造器产生一个对话框
		alertDialog.show();
		
		LinearLayout dialogRootLayout = (LinearLayout)layout.findViewById(R.id.layout_root);
		dialogRootLayout.setMinimumHeight(screenHeight*4/5);
		dialogRootLayout.setMinimumWidth(screenWidth*4/5);
		//dishImage.setImageDrawable(getResources().getDrawable(R.drawable.icon));		
		dishInfo.setText((String)listType.get("itemNo") + ". "+(String)listType.get("itemName")+' '+(String)listType.get("itemPrice"));
		dishMix.setText((String)listType.get("itemMix"));
		
		String tmp = (String)listType.get("imageSrc");
		dishImage.setImageBitmap(getBitMap(tmp,1));
		
		buttonAdmit.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						Map<String,Object> mapTmp = infoStore.getUnorderInfo();
						String tmpNo = (String)listType.get("itemNo");//get the dish No
						//Log.v("tmpNo", tmpNo);
						if(mapTmp.get(tmpNo) == null)
						{
							if(Integer.parseInt(number.getText().toString()) != 0)
							{
								mapTmp.put(tmpNo, number.getText().toString());
							}
						}
						else
						{
							int tmpNumber = Integer.parseInt(number.getText().toString())+Integer.parseInt(mapTmp.get(tmpNo).toString());
							mapTmp.put(tmpNo,Integer.toString(tmpNumber));
						}
						infoStore.setUnorderInfo(mapTmp);
						Toast.makeText(DishAccordingToClass.this,
							 	"菜品添加成功", 
		                        Toast.LENGTH_LONG).show();
						alertDialog.cancel();
					}				
				}				
			);
		buttonCancel.setOnClickListener(
			new Button.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//TODO Auto-generated method stub
					alertDialog.cancel();
				}				
			}				
		);
		
		buttonPlus.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						int dishNumber = Integer.parseInt(number.getText().toString());
						if(dishNumber > 100)
						{
							Toast.makeText(DishAccordingToClass.this,
								 	"您点的菜品份数过多", 
			                        Toast.LENGTH_LONG).show();
						}
						else
							number.setText(String.valueOf(++dishNumber));
					}				
				}				
			);
		buttonMinus.setOnClickListener(
			new Button.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//TODO Auto-generated method stub
					int dishNumber = Integer.parseInt(number.getText().toString());
					if(--dishNumber >= 0)
					number.setText(String.valueOf(dishNumber));
				}				
			}				
		);
		
		

	}
}
}