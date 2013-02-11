package com.Mickey;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class OrderInfo {

	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;
	private Button orderInfoReturn;
	private Button endConsumptionBtn;
	private Button orderedDishBtn;
	private Button unorderedDishBtn;
	private Button submitDishesBtn;
	private ListView orderListView;
	private int screenHeight;
	private int costInSum;
	private TextView costInSumView;
	private InfoStore infoStoreGlobal;
	private int ifNew = 0;
	private final Activity activitySource;
	private static boolean ifEnd = false;

	public AlertDialog getAlertDialog() {
		return alertDialog;
	}

	public void setAlertDialog(AlertDialog alertDialog) {
		this.alertDialog = alertDialog;
	}

	public void clearZeroDishes(int orderOrNot) {
		Map<String, Object> mapClear = null;
		if (orderOrNot == 1)
			mapClear = infoStoreGlobal.getOrderInfo();
		else
			mapClear = infoStoreGlobal.getUnorderInfo();
		Iterator<Map.Entry<String, Object>> it = mapClear.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
					.next();
			String value = entry.getValue().toString();
			if (orderOrNot == 1)
				value = value.split("&")[0];
			if (value.equalsIgnoreCase("0")) {
				Log.v("value", value);
				it.remove();
				String key = entry.getKey().toString();
				mapClear.remove(key);
			}
		}
		if (orderOrNot == 1)
			infoStoreGlobal.setOrderInfo(mapClear);
		else
			infoStoreGlobal.setUnorderInfo(mapClear);
	}

	List<Map<String, Object>> adapterList(int orderOrNot) {
		List<Map<String, Object>> orderListTmp = new ArrayList<Map<String, Object>>();
		Map<String, Object> orderList;
		costInSum = 0;
		List<Map<String, Object>> dishList = infoStoreGlobal.getLists();
		if (orderOrNot == 1)
			orderList = infoStoreGlobal.getOrderInfo();
		else
			orderList = infoStoreGlobal.getUnorderInfo();

		Iterator<Map.Entry<String, Object>> it = orderList.entrySet()
				.iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
					.next();
			String key = entry.getKey().toString();// dish no in dishlist
			String value = entry.getValue().toString();
			Map<String, Object> listTmp = new HashMap<String, Object>();
			int itemNo = Integer.parseInt(key.split("&")[0]) - 1;

			listTmp.put("itemNo", key);
			listTmp.put("itemName", dishList.get(itemNo).get("itemName")
					.toString());
			listTmp.put("itemAmount", value.split("&")[0]);
			int itemPrice = Integer.parseInt(value.split("&")[0])
					* Integer.parseInt(dishList.get(itemNo).get("itemPrice")
							.toString());
			costInSum += itemPrice;
			listTmp.put("itemPrice", Integer.toString(itemPrice));
			listTmp.put("itemImage", getBitMap(dishList.get(itemNo).get(
					"imageSrc").toString(), 8));
			listTmp.put("itemPerPrice", dishList.get(itemNo).get("itemPrice")
					.toString());
			if (orderOrNot == 1) {
				listTmp.put("itemStatus", value.split("&")[1]);
			}
			orderListTmp.add(listTmp);

		}

		costInSumView.setText(Integer.toString(costInSum));
		return orderListTmp;

	}

	public OrderInfo(final Activity activitySource, InfoStore infoStore,
			int screenHeight, int screenWidth) {
		this.activitySource = activitySource;
		LayoutInflater inflater = (LayoutInflater) activitySource
				.getSystemService("layout_inflater");
		View layout = inflater.inflate(R.layout.order_list, null);
		orderInfoReturn = (Button) layout.findViewById(R.id.orderInfoReturn);
		endConsumptionBtn = (Button) layout
				.findViewById(R.id.endConsumptionBtn);
		unorderedDishBtn = (Button) layout.findViewById(R.id.unorderedDishBtn);
		orderedDishBtn = (Button) layout.findViewById(R.id.orderedDishBtn);
		submitDishesBtn = (Button) layout.findViewById(R.id.submitDishesBtn);

		costInSumView = (TextView) layout.findViewById(R.id.costInSum);
		builder = new AlertDialog.Builder(activitySource);
		builder.setView(layout);
		alertDialog = builder.create();// 通过构造器产生一个对话框

		orderListView = (ListView) layout.findViewById(R.id.orderListView);
		this.screenHeight = screenHeight;
		infoStoreGlobal = infoStore;

		MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(activitySource,
				adapterList(0), R.layout.order_list_element, new String[] {
						"itemNo", "itemName", "itemAmount", "itemPrice",
						"itemImage", "itemPerPrice" }, new int[] {
						R.id.orderItemNo, R.id.orderItemName,
						R.id.orderItemAmount, R.id.orderItemPrice,
						R.id.orderItemImage, R.id.orderItemPerPrice });
		orderListView.setAdapter(mySimpleAdapter);
		mySimpleAdapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// 判断是否为我们要处理的对象
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				} else
					return false;
			}
		});

		submitDishesBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String orderMessage = new String();
				Map<String, Object> mapOrder = infoStoreGlobal.getOrderInfo();
				Map<String, Object> mapUnOrder = infoStoreGlobal
						.getUnorderInfo();
				List<Map<String, Object>> dishList = infoStoreGlobal.getLists();
				if (mapOrder.size() == 0) {
					ifNew = 1;
					orderMessage = "1 "+infoStoreGlobal.getDeskNo()+" ";// 桌号;
				} else {
					ifNew = 0;
					orderMessage = "2 " + infoStoreGlobal.getCosumptionNo()
							+ " ";
				}
				Iterator<Map.Entry<String, Object>> it = mapUnOrder.entrySet()
						.iterator();
				// int sizeNow = mapUnOrder.size();
				orderMessage += Integer.toString(mapUnOrder.size());
				while (it.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
							.next();
					String key = entry.getKey().toString();// dish no in
					String value = entry.getValue().toString();
					// 0:等待制作；1：正在制作；2：制作完毕
					// mapTmp.put(tmpNo,Integer.toString(tmpNumber));
					mapOrder.put(key, value + "&" + "等待制作");
					orderMessage += " "
							+ dishList.get(Integer.parseInt(key) - 1).get(
									"itemName") + " " + value;
				}
				mapUnOrder.clear();
				infoStoreGlobal.setUnorderInfo(mapUnOrder);
				infoStoreGlobal.setOrderInfo(mapOrder);
				MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(
						activitySource, adapterList(0),
						R.layout.order_list_element, new String[] { "itemNo",
								"itemName", "itemAmount", "itemPrice",
								"itemImage", "itemPerPrice" }, new int[] {
								R.id.orderItemNo, R.id.orderItemName,
								R.id.orderItemAmount, R.id.orderItemPrice,
								R.id.orderItemImage, R.id.orderItemPerPrice });
				orderListView.setAdapter(mySimpleAdapter);
				final String orderMessageFinal = orderMessage;
				Thread t = new Thread() {
					public void run() {

						// send the data of ordered dishes
						Socket socket = null;
						try {
							socket = new Socket(infoStoreGlobal.getIPAddress(),
									7797);
							DataOutputStream out = new DataOutputStream(socket
									.getOutputStream());

							out.writeUTF(orderMessageFinal);

							byte[] buff = new byte[1024];

							String recieved = new String();

							while (true) {
								int read = -1;
								read = socket.getInputStream().read(buff);

								if (read <= 0) {
									break;
								} else {
									recieved += new String(buff, "GB2312");
								}
							}
							Log.v("re", recieved.split("\n")[0]);
							if (ifNew == 1)
								infoStoreGlobal.setCosumptionNo(recieved
										.split("\n")[0].replace("\r", ""));
							socket.close();
						} catch (IOException e) {
							// System.out.println("Error");
						}
						// send the data of ordered dishes end
						synchronized (this) {
							ifEnd = true;
						}
					}
				};
				t.start();

				while (!ifEnd) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Log.v("message", orderMessageFinal);

			}
		});

		orderInfoReturn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearZeroDishes(0);
				clearZeroDishes(1);
				alertDialog.cancel();
			}
		});

		unorderedDishBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearZeroDishes(0);
				MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(
						activitySource, adapterList(0),
						R.layout.order_list_element, new String[] { "itemNo",
								"itemName", "itemAmount", "itemPrice",
								"itemImage", "itemPerPrice" }, new int[] {
								R.id.orderItemNo, R.id.orderItemName,
								R.id.orderItemAmount, R.id.orderItemPrice,
								R.id.orderItemImage, R.id.orderItemPerPrice });
				orderListView.setAdapter(mySimpleAdapter);
			}
		});

		orderedDishBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearZeroDishes(1);
				MySimpleAdapter mySimpleAdapter = new MySimpleAdapter(
						activitySource, adapterList(1),
						R.layout.order_list_element, new String[] { "itemNo",
								"itemName", "itemAmount", "itemPrice",
								"itemImage", "itemPerPrice", "itemStatus" },
						new int[] { R.id.orderItemNo, R.id.orderItemName,
								R.id.orderItemAmount, R.id.orderItemPrice,
								R.id.orderItemImage, R.id.orderItemPerPrice,
								R.id.orderItemStatus });
				orderListView.setAdapter(mySimpleAdapter);
			}
		});

		endConsumptionBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) activitySource
						.getSystemService("layout_inflater");
				View layoutEnd = inflater.inflate(R.layout.end_consumption,
						null);
				final TextView endConsumptionCost = (TextView) layoutEnd
						.findViewById(R.id.endConsumptionCost);
				final Spinner endConsumptionDiscount = (Spinner) layoutEnd
						.findViewById(R.id.endConsumptionDiscount);
				Button endConsumptionCancel = (Button) layoutEnd
						.findViewById(R.id.endConsumptionCancel);

				final int costSum = Integer.parseInt(costInSumView.getText()
						.toString());
				AlertDialog.Builder builderEnd = new AlertDialog.Builder(
						activitySource);
				builderEnd.setView(layoutEnd);
				final AlertDialog alertDialogEnd = builderEnd.create();

				/* Spinner */
				Map<String, Object> tmp = infoStoreGlobal.getDiscountInfo();
				List<String> discountListTmp = new ArrayList<String>();// the
				// name
				// of
				// each
				// discount
				final int[] discountValue = new int[tmp.size()];// the value of
				// each discount
				int i = 0;
				Iterator<Map.Entry<String, Object>> it = tmp.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
							.next();
					discountListTmp.add(entry.getKey().toString());
					discountValue[i] = Integer.parseInt(entry.getValue()
							.toString());
					i++;
				}
				ArrayAdapter<String> adapter;
				adapter = new ArrayAdapter<String>(activitySource,
						android.R.layout.simple_spinner_item, discountListTmp);
				endConsumptionDiscount.setAdapter(adapter);

				endConsumptionDiscount
						.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								endConsumptionCost.setText(Integer
										.toString(costSum * discountValue[arg2]
												/ 100));
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
							}
						});
				/* Spinner End */

				endConsumptionCost.setText(costInSumView.getText().toString());

				endConsumptionCancel
						.setOnClickListener(new Button.OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								Thread t = new Thread() {
									public void run() {
										Socket socket;
										try {
											socket = new Socket(infoStoreGlobal
													.getIPAddress(), 7797);
											DataOutputStream out = new DataOutputStream(
													socket.getOutputStream());
											infoStoreGlobal.getCosumptionNo();
											endConsumptionCost.getText();
											endConsumptionDiscount
													.getSelectedItem()
													.toString();
											out.writeUTF("3 "
													+ infoStoreGlobal
															.getCosumptionNo()
													+ " "
													+ endConsumptionDiscount
															.getSelectedItem()
															.toString()
													+ " "
													+ endConsumptionCost
															.getText());
											
											socket = new Socket(infoStoreGlobal
													.getIPAddress(), 7797);
											out = new DataOutputStream(
													socket.getOutputStream());
											infoStoreGlobal.getCosumptionNo();
											endConsumptionCost.getText();
											endConsumptionDiscount
													.getSelectedItem()
													.toString();
											out.writeUTF("7 "+infoStoreGlobal.getUserName());
											
											
										} catch (UnknownHostException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								};

								t.start();
								
								Toast.makeText(activitySource,
									 	"此次消费已结束", 
				                        Toast.LENGTH_LONG).show();
								try {
									Thread.sleep(1500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								activitySource.finish();
								
								alertDialogEnd.cancel();
							}
						});

				alertDialogEnd.show();

			}
		});

		RelativeLayout dialogRootLayout = (RelativeLayout) layout
				.findViewById(R.id.orderListRoot);
		dialogRootLayout.setMinimumHeight(screenHeight * 4 / 5);
		dialogRootLayout.setMinimumWidth(screenWidth * 4 / 5);

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
		options.inSampleSize = be;
		bit = BitmapFactory
				.decodeFile("mnt/sdcard/Dishes/" + imageSrc, options);
		return bit;
	}

	class MySimpleAdapter extends SimpleAdapter {
		private final Context context;
		private List<Map<String, Object>> data;
		private int resource;
		private String[] from;
		private int[] to;

		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.context = context;
			this.data = (List<Map<String, Object>>) data;
			this.resource = resource;
			this.from = from;
			this.to = to;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			final View rowView = inflater.inflate(resource, null, true);

			final Map<String, Object> medMap = data.get(position);

			final TextView orderItemNo = (TextView) rowView.findViewById(to[0]);
			orderItemNo.setText(medMap.get(from[0]).toString());

			final TextView orderItemName = (TextView) rowView
					.findViewById(to[1]);
			orderItemName.setText(medMap.get(from[1]).toString());

			final TextView orderItemAmount = (TextView) rowView
					.findViewById(to[2]);
			orderItemAmount.setText(medMap.get(from[2]).toString());

			final TextView orderItemPrice = (TextView) rowView
					.findViewById(to[3]);
			orderItemPrice.setText(medMap.get(from[3]).toString());

			final ImageView orderImage = (ImageView) rowView
					.findViewById(to[4]);
			orderImage.setImageBitmap((Bitmap) medMap.get(from[4]));

			final TextView orderItemPerPrice = (TextView) rowView
					.findViewById(to[5]);
			orderItemPerPrice.setText(medMap.get(from[5]).toString());
			orderItemPerPrice.setVisibility(View.INVISIBLE);

			if (from.length == 7) {
				final TextView orderItemStatus = (TextView) rowView
						.findViewById(to[6]);
				orderItemStatus.setText(medMap.get(from[6]).toString());
			}

			final Button btnMinus = (Button) rowView
					.findViewById(R.id.orderItemMinus);
			final Button btnPlus = (Button) rowView
					.findViewById(R.id.orderItemPlus);
			if (from.length == 6)// unordered Dish
			{
				Button.OnClickListener minusClickListener = new Button.OnClickListener() {
					public void onClick(View v) {
						TextView AmountViewTmp = (TextView) rowView
								.findViewById(R.id.orderItemAmount);
						TextView PriceViewTmp = (TextView) rowView
								.findViewById(R.id.orderItemPrice);
						TextView PricePerViewTmp = (TextView) rowView
								.findViewById(R.id.orderItemPerPrice);

						int amountTmp = Integer.parseInt(AmountViewTmp
								.getText().toString());
						int priceTmp = Integer.parseInt(PriceViewTmp.getText()
								.toString());
						int pricePerTmp = Integer.parseInt(PricePerViewTmp
								.getText().toString());

						if (amountTmp == 0)
							return;

						costInSumView.setText(Integer.toString(Integer
								.parseInt(costInSumView.getText().toString())
								- pricePerTmp));
						priceTmp -= pricePerTmp;

						amountTmp--;
						AmountViewTmp.setText(Integer.toString(amountTmp));
						PriceViewTmp.setText(Integer.toString(priceTmp));

						Map<String, Object> orderList = infoStoreGlobal
								.getUnorderInfo();
						orderList.put(medMap.get(from[0]).toString(), Integer
								.toString(amountTmp));
					}
				};
				btnMinus.setOnClickListener(minusClickListener);
				Button.OnClickListener plusClickListener = new Button.OnClickListener() {
					public void onClick(View v) {
						TextView AmountViewTmp = (TextView) rowView
								.findViewById(R.id.orderItemAmount);
						TextView PriceViewTmp = (TextView) rowView
								.findViewById(R.id.orderItemPrice);
						TextView PricePerViewTmp = (TextView) rowView
								.findViewById(R.id.orderItemPerPrice);

						int amountTmp = Integer.parseInt(AmountViewTmp
								.getText().toString());
						int priceTmp = Integer.parseInt(PriceViewTmp.getText()
								.toString());
						int pricePerTmp = Integer.parseInt(PricePerViewTmp
								.getText().toString());

						costInSumView.setText(Integer.toString(Integer
								.parseInt(costInSumView.getText().toString())
								+ pricePerTmp));
						priceTmp += pricePerTmp;
						amountTmp++;
						AmountViewTmp.setText(Integer.toString(amountTmp));
						PriceViewTmp.setText(Integer.toString(priceTmp));

						Map<String, Object> orderList = infoStoreGlobal
								.getUnorderInfo();
						orderList.put(medMap.get(from[0]).toString(), Integer
								.toString(amountTmp));
					}
				};
				btnPlus.setOnClickListener(plusClickListener);
			}

			else {
				btnMinus.setText("退一份");
				btnPlus.setVisibility(View.INVISIBLE);
				Button.OnClickListener minusClickListener = new Button.OnClickListener() {
					public void onClick(View v) {
						final String dishStatus = ((TextView) rowView
								.findViewById(R.id.orderItemStatus)).getText()
								.toString();
						final int dishAmount = Integer
								.parseInt(((TextView) rowView
										.findViewById(R.id.orderItemAmount))
										.getText().toString());
						if (dishAmount == 0)
							return;
						if (dishStatus.equalsIgnoreCase("等待制作") == true) {
							Thread t = new Thread() {
								public void run() {
									Socket socket;
									try {
										socket = new Socket(infoStoreGlobal
												.getIPAddress(), 7797);
										DataOutputStream out = new DataOutputStream(
												socket.getOutputStream());
										out
												.writeUTF("5 "
														+ infoStoreGlobal
																.getCosumptionNo()
																.replace("\r",
																		"")
														+ " "
														+ ((TextView) rowView
																.findViewById(R.id.orderItemName))
																.getText()
																.toString());
										Map<String, Object> orderListTmp = infoStoreGlobal
												.getOrderInfo();
										String minusOrderDishNo = ((TextView) rowView
												.findViewById(R.id.orderItemNo))
												.getText().toString();
										orderListTmp
												.put(
														minusOrderDishNo,
														Integer
																.toString(dishAmount - 1)
																+ "&"
																+ dishStatus);
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

							TextView AmountViewTmp = (TextView) rowView
									.findViewById(R.id.orderItemAmount);
							TextView PriceViewTmp = (TextView) rowView
									.findViewById(R.id.orderItemPrice);
							TextView PricePerViewTmp = (TextView) rowView
									.findViewById(R.id.orderItemPerPrice);

							int amountTmp = Integer.parseInt(AmountViewTmp
									.getText().toString());
							int priceTmp = Integer.parseInt(PriceViewTmp
									.getText().toString());
							int pricePerTmp = Integer.parseInt(PricePerViewTmp
									.getText().toString());

							if (amountTmp == 0)
								return;

							costInSumView.setText(Integer.toString(Integer
									.parseInt(costInSumView.getText()
											.toString())
									- pricePerTmp));
							priceTmp -= pricePerTmp;

							amountTmp--;
							AmountViewTmp.setText(Integer.toString(amountTmp));
							PriceViewTmp.setText(Integer.toString(priceTmp));

						} else
							Toast.makeText(activitySource, "此菜品目前不允许退菜",
									Toast.LENGTH_LONG).show();
					}
				};
				btnMinus.setOnClickListener(minusClickListener);

			}

			return rowView;
		}
	}

}
