package com.Mickey;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Mickey.R;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DishCommon extends Activity{
	
	private ListView listView;
	private int screenHeight;
	private int screenWidth;
	AlertDialog.Builder builder;
	private List<Map<String,Object>> lists;
	AlertDialog alertDialog;
	private InfoStore infoStore;
	private Button orderInfoCommon;
	private Button synchCommon;
	private Button ipAddressCommon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_common);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);    
		screenHeight = dm.heightPixels;	
		screenWidth = dm.widthPixels;
		infoStore = (InfoStore)getApplication();
		listView=(ListView)this.findViewById(R.id.MyListViewCommon);
        displayAllDishes();
        orderInfoCommon = (Button)this.findViewById(R.id.orderInfoCommon);
        synchCommon = (Button)this.findViewById(R.id.synchCommon);
        ipAddressCommon = (Button)this.findViewById(R.id.ipAddressCommon);
        orderInfoCommon.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						
						//OrderInfo orderInfo = new OrderInfo();
						OrderInfo orderInfo =  new OrderInfo(DishCommon.this,infoStore,screenHeight,screenWidth);
						AlertDialog tmpArtDialog = orderInfo.getAlertDialog();
						tmpArtDialog.show();
						//alertDialogOrderInfo.show();
					}				
				}				
			);
        ipAddressCommon.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						
						IpAddress ipAddress = null;
						try {
							ipAddress = new IpAddress(DishCommon.this,infoStore,screenHeight,screenWidth);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AlertDialog tmpArtDialog = ipAddress.getAlertDialog();
						tmpArtDialog.show();
					}				
				}				
			);
        synchCommon.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						
						Sync sync =  new Sync(DishCommon.this,infoStore,screenHeight,screenWidth);
						AlertDialog tmpArtDialog = sync.getAlertDialog();
						tmpArtDialog.show();
					}				
				}				
			);
        
        
    }
    void displayAllDishes()
	{

		lists=infoStore.getLists();
		List<Map<String,Object>> listTmp = new ArrayList<Map<String,Object>>();
			for(int i = 0;i < lists.size();i++){
				//Log.v("name", dishTypeName[typeNum]);	
				Map<String,Object> list=new HashMap<String, Object>();
	            list.put("itemImage", getBitMap((String)lists.get(i).get("imageSrc"),10));
	            list.put("itemType", (String)lists.get(i).get("itemType"));
	            list.put("itemNo", (String)lists.get(i).get("itemNo"));
	            list.put("itemName", (String)lists.get(i).get("itemName"));
	            //Log.v("name", (String)lists.get(i).get("itemName"));
	            list.put("itemMix", "配料:"+(String)lists.get(i).get("itemMix"));
	            list.put("itemPrice", "价格:"+(String)lists.get(i).get("itemPrice")+"$");
	            list.put("imageSrc", (String)lists.get(i).get("imageSrc"));
	            listTmp.add(list);
					//Log.v("i",Integer.toString(i));
		  }			   
		  SimpleAdapter simpleAdapter=new SimpleAdapter(this, listTmp, R.layout.dish_list_element, new String[]{"itemImage","itemNo","itemName","itemPrice","itemMix","itemType"}, new int[]{R.id.itemImage,R.id.itemNo,R.id.itemName,R.id.itemPrice,R.id.itemMix,R.id.itemType});
		  listView.setAdapter(simpleAdapter);

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
		Log.v("123", "123");
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
		//Log.v("be", Integer.toString(be));
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
			showDialog();
		} 
	private void showDialog() {
		listType = lists.get(itemIndex);

		LayoutInflater inflater = (LayoutInflater)  DishCommon.this
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
		

		builder = new AlertDialog.Builder(DishCommon.this);// 创建一个弹出对话框构造器
		builder.setView(layout);
		alertDialog = builder.create();// 通过构造器产生一个对话框
		alertDialog.show();
		
		LinearLayout dialogRootLayout = (LinearLayout)layout.findViewById(R.id.layout_root);
		dialogRootLayout.setMinimumHeight(screenHeight*4/5);
		dialogRootLayout.setMinimumWidth(screenWidth*4/5);
		//dishImage.setImageDrawable(getResources().getDrawable(R.drawable.icon));		
		//Log.v("itemNo", (String)listType.get("itemNo"));
		//Log.v("itemName", (String)listType.get("itemName"));		
		dishInfo.setText((String)listType.get("itemNo") + ". "+(String)listType.get("itemName")+' '+(String)listType.get("itemPrice")+'$');
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
					String tmpNo = (String)listType.get("itemNo");//get the dish title
					if(mapTmp.get(tmpNo) == null)
					{
						if(Integer.parseInt(number.getText().toString()) != 0)
							mapTmp.put(tmpNo, number.getText().toString());
					}
					else
					{
						int tmpNumber = Integer.parseInt(number.getText().toString())+Integer.parseInt(mapTmp.get(tmpNo).toString());
						mapTmp.put(tmpNo,Integer.toString(tmpNumber));
					}
					infoStore.setUnorderInfo(mapTmp);
					Toast.makeText(DishCommon.this,
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
							Toast.makeText(DishCommon.this,
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
