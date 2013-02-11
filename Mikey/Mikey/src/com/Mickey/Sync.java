package com.Mickey;



import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Sync {
	private AlertDialog alertDialog;
	private AlertDialog.Builder builder;
	private ProgressBar progressBar;
	private Button syncDataBtn;
	private Button cancelSyncBtn;
	private TextView syncText;
	private int screenHeight;

	private static boolean ifEnd = false;

	public AlertDialog getAlertDialog() {
		return alertDialog;
	}

	public void setAlertDialog(AlertDialog alertDialog) {
		this.alertDialog = alertDialog;
	}

	public Sync(final Activity activitySource, final InfoStore infoStore,
			int screenHeight, int screenWidth) {

		this.screenHeight = screenHeight;
		LayoutInflater inflater = (LayoutInflater) activitySource
				.getSystemService("layout_inflater");
		View layout = inflater.inflate(R.layout.sync_info, null);
		builder = new AlertDialog.Builder(activitySource);
		builder.setView(layout);
		alertDialog = builder.create();// 通过构造器产生一个对话框

		// LinearLayout syncRoot =
		// (LinearLayout)layout.findViewById(R.id.syncRoot);
		progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
		syncDataBtn = (Button) layout.findViewById(R.id.syncDataBtn);
		cancelSyncBtn = (Button) layout.findViewById(R.id.cancelSyncBtn);
		syncText = (TextView) layout.findViewById(R.id.syncText);

		progressBar.setIndeterminate(true);
		progressBar.setVisibility(View.INVISIBLE);
		syncText.setText("是否同步数据？");
		syncText.setVisibility(View.VISIBLE);

		// syncRoot.setMinimumWidth(100);

		syncDataBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				syncText.setVisibility(View.INVISIBLE);
				Thread t = new Thread() {
					public void run() {
						MyServer server = new MyServer();
						server.delAllFile("mnt/sdcard/Dishes");
						server.ServerStart(infoStore);
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

				Log.v("Thread", "End");
				progressBar.setVisibility(View.INVISIBLE);
				syncText.setText("同步成功,请重新登录");
				syncText.setVisibility(View.VISIBLE);
				activitySource.finish();
				//ActivityManager activityMgr= (ActivityManager)activitySource.getSystemService("activity");
				//activityMgr.restartPackage(infoStore.getPackageName());

			}
		});

		cancelSyncBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.cancel();
			}
		});
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
}
