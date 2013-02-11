package com.Mickey;




import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class DishSearch extends Activity { 
	
	private InfoStore infoStore;
	private ListView listView;
	private int screenHeight;
	private int screenWidth;
	AlertDialog.Builder builder;
	private List<Map<String,Object>> lists;
	private ArrayAdapter<String> adapter;
	AlertDialog alertDialog;
	
	private Button searchAdmit;
	private String priceRangeLow;
	private String priceRangeHigh;
	private String kwDishName;
	private String kwDishType;
	private String kwMixName;
	private Button orderInfoSearch;
	private Button synchSearch;
	private Button ipAddressSearch;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_search);	
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);    
		screenHeight = dm.heightPixels;	
		screenWidth = dm.widthPixels;
		infoStore = (InfoStore)getApplication();
		listView=(ListView)this.findViewById(R.id.SearchInfo);	
		searchAdmit = (Button)this.findViewById(R.id.SearchAdmit);	
		synchSearch = (Button)this.findViewById(R.id.synchSearch);
		ipAddressSearch = (Button)this.findViewById(R.id.ipAddressSearch);
		Spinner dishType = (Spinner)this.findViewById(R.id.KwDishType);
		String[] tmp  = infoStore.getTypeLists();
		List<String> kwDishTypeList = new ArrayList<String>();
		for(int i = 0;i < tmp.length;i++)
			kwDishTypeList.add(tmp[i]);
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kwDishTypeList);
		dishType.setAdapter(adapter);
		
		dishType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//Log.v("content",arg0.getSelectedItem().toString());
				kwDishType = arg0.getSelectedItem().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub				
			}			
		}			
		);
		
		searchAdmit.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						priceRangeLow = null;
						priceRangeHigh = null;
						kwDishName = null;
						kwMixName = null;
						//TODO Auto-generated method stub
						if( ((CheckBox)DishSearch.this.findViewById(R.id.CbPriceRange) ).isChecked())
						{
							priceRangeLow = ((EditText)DishSearch.this.findViewById(R.id.PriceLow)).getText().toString();
							if(priceRangeLow.length() == 0)
								priceRangeLow = "0";
							
							priceRangeHigh = ((EditText)DishSearch.this.findViewById(R.id.PriceHigh)).getText().toString();
							if(priceRangeHigh.length() == 0)
								priceRangeHigh = "99999";						
						}
						Log.v("oh","yeah");
						if( ((CheckBox)DishSearch.this.findViewById(R.id.CbDishName) ).isChecked())
						{	
							Log.v("oh","yeah");
							kwDishName = ((EditText)DishSearch.this.findViewById(R.id.KwDishName)).getText().toString();
							if(kwDishName.length() == 0)
								kwDishName = null;
						}
						if( ((CheckBox)DishSearch.this.findViewById(R.id.CbMixName) ).isChecked())
						{	
							kwMixName = ((EditText)DishSearch.this.findViewById(R.id.KwMixName)).getText().toString();
							if(kwMixName.length() == 0)
								kwMixName = null;
						}
						if( ((CheckBox)DishSearch.this.findViewById(R.id.CbDishType) ).isChecked() == false)
							kwDishType = null; 

						displayDishAccodringToType(1);
						
					}				
				}				
			);	
		
		displayDishAccodringToType(0);
		
		orderInfoSearch = (Button)this.findViewById(R.id.orderInfoSearch);
		orderInfoSearch.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						OrderInfo orderInfo =  new OrderInfo(DishSearch.this,infoStore,screenHeight,screenWidth);
						AlertDialog tmpArtDialog = orderInfo.getAlertDialog();
						tmpArtDialog.show();
					}				
				}				
			);
		synchSearch.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						
						Sync sync =  new Sync(DishSearch.this,infoStore,screenHeight,screenWidth);
						AlertDialog tmpArtDialog = sync.getAlertDialog();
						tmpArtDialog.show();
					}				
				}				
			);
		ipAddressSearch.setOnClickListener(
				new Button.OnClickListener()	
				{
					@Override
					public void onClick(View v)
					{
						//TODO Auto-generated method stub
						
						IpAddress ipAddress = null;
						try {
							ipAddress = new IpAddress(DishSearch.this,infoStore,screenHeight,screenWidth);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AlertDialog tmpArtDialog = ipAddress.getAlertDialog();
						tmpArtDialog.show();
					}				
				}				
			);
	}
	
	void displayDishAccodringToType(int flag)
	{
		lists = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> listTmp = infoStore.getLists();
		for(int i = 0;i < listTmp.size();i++){
				//if(((String)lists.get(i).get("itemTitle")).indexOf(searchContent) == -1&&searchContent.length() != 0)
					//continue;
				Map<String,Object> list=new HashMap<String, Object>();
				String itemPrice = (String)listTmp.get(i).get("itemPrice");
				String itemType = (String)listTmp.get(i).get("itemType");
	            String itemNo = (String)listTmp.get(i).get("itemNo");
	            String itemName = (String)listTmp.get(i).get("itemName");
	            String itemMix = (String)listTmp.get(i).get("itemMix");
				if(flag == 1)
					if(judgeDish(itemPrice,itemType,itemName,itemMix) == false)
						continue;
	            list.put("itemImage", getBitMap((String)listTmp.get(i).get("imageSrc"),10));
	            list.put("itemType", itemType);
	            list.put("itemNo", itemNo);
	            list.put("itemName", itemName);
	            list.put("itemMix", "配料:"+itemMix);
	            list.put("itemPrice", "价格:"+itemPrice+"$");
	            list.put("imageSrc", (String)listTmp.get(i).get("imageSrc"));
	            lists.add(list);
					//Log.v("i",Integer.toString(i));
		  }	
			SimpleAdapter simpleAdapter=new SimpleAdapter(this, lists, R.layout.dish_list_element, new String[]{"itemImage","itemNo","itemName","itemPrice","itemMix","itemType"}, new int[]{R.id.itemImage,R.id.itemNo,R.id.itemName,R.id.itemPrice,R.id.itemMix,R.id.itemType});listView.setAdapter(simpleAdapter);
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
	
	Boolean judgeDish(String itemPrice,String itemType,String itemName,String itemMix )
	{
		/*
		 	private String priceRangeLow;
	private String priceRangeHigh;
	private String kwDishName;
	private String kwDishType;
	private String kwMixName;
		 */
		//Log.v("dishType",kwDishType);
		if(priceRangeLow != null && (Integer.parseInt(priceRangeLow) > Integer.parseInt(itemPrice) ||  Integer.parseInt(priceRangeHigh) < Integer.parseInt(itemPrice)))	
			return false;
		
		if(kwDishType != null && kwDishType.equalsIgnoreCase(itemType) == false)		
			return false;
		if(kwDishName != null && itemName.indexOf(kwDishName) == -1)
			return false;
		if(kwMixName != null && itemMix.indexOf(kwMixName) == -1)
			return false;		
		return true;
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
			showDialog();
		} 
	private void showDialog() {
		//lists = null;
		//Log.v("index",Integer.toString(itemIndex));
		//Map<String,Object> listType=new HashMap<String, Object>();
		listType = lists.get(itemIndex);

		LayoutInflater inflater = (LayoutInflater)  DishSearch.this
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
		

		builder = new AlertDialog.Builder(DishSearch.this);// 创建一个弹出对话框构造器
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
						Toast.makeText(DishSearch.this,
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
							Toast.makeText(DishSearch.this,
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
