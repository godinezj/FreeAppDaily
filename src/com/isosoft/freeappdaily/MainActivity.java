package com.isosoft.freeappdaily;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.isosoft.freeappdaily.task.AppCheckUiTask;
import com.isosoft.freeappdaily.task.AsyncResponse;

public class MainActivity extends Activity implements AsyncResponse {
	private String TAG = MainActivity.class.getSimpleName();
	private AppCheckUiTask task;
	private AlertDialog dialog;
	public static final String PREFS_NAME = "freeappTaskPrefs";
	public static final String ASIN = "ASIN";
	
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String svcName = AppCheckService.class.getName();
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (svcName.equals(service.service.getClassName())) {
				return true;
			}
		}
		Log.d(TAG, svcName+" is not running");
		return false;
	}
	
	private void startService() {
		new ScheduleReceiver().onReceive(this, new Intent());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// show EULA
//		new SimpleEula(this).show();
		
		// first things first
		if (!isServiceRunning()) {
			Log.d(TAG, "Starting service");
			startService();
		}

		this.task = new AppCheckUiTask(this);
		task.execute();
	}
	
	
	@Override
	public void processFinish(String appName, String asin) {
		if (this.task != null) {
			Log.d(TAG, "Destroying task");
			this.task.cancel(true);
			this.task = null;
		}
		
		if (asin == null) {
			Log.d(TAG, "No ASIN given");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Sorry, something went wrong.");
			builder.setMessage("Retry?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					onResume();
				}
			}).setNegativeButton("Later!",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					MainActivity.this.finish();
				}
			});
			
			this.dialog = builder.create();
			dialog.show();
		} else {
			Log.i(TAG, "Launching appstore with ASIN: "+asin);
			try {
				String amznLink = "amzn://apps/android?asin="+asin;
				Log.d(TAG, "Opening "+amznLink);
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(amznLink)));
				getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(ASIN, asin).commit();
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?asin="+asin)));
			}
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "Resuming Main Activity");
		if (this.dialog != null) {
			this.dialog.cancel();
		}
		
		if (this.task == null) {
			Log.d(TAG, "No task scheduled, running one.");
			this.task = new AppCheckUiTask(this);
			this.task.execute();
		
		} else {
			Log.d(TAG, "Task running, doing nothing...");
		}
		
	}
	
}
