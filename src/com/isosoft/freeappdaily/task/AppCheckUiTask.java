package com.isosoft.freeappdaily.task;

import android.app.ProgressDialog;

import com.isosoft.freeappdaily.Log;
import com.isosoft.freeappdaily.MainActivity;



public class AppCheckUiTask extends AppCheckTask {
	private String TAG = AppCheckUiTask.class.getSimpleName();
	private MainActivity activity;
	private ProgressDialog pd;
	
	public AppCheckUiTask(MainActivity activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(activity);
		pd.setTitle("Checking Amazon Appstore...");
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.setIndeterminate(true);
//		final AppCheckUiTask task = this;
//		pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Settings", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				pd.dismiss();
//				task.cancel(true);
//				// TODO settings, maybe
//			}
//		});
		
		try {
			pd.show();
		} catch (Exception e) {
			Log.e(TAG, "Error showing progress dialog");
		}
	}
	
	protected Void doInBackground(Void... v) {
		Log.d(TAG, "In AppCheckUiTask.doInBackground()");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		super.doInBackground(v);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.d(TAG, "Post exec, appname: "+super.getAppName());
		if (pd != null) {
			try {
				pd.dismiss();
			} catch (Exception e) {
				Log.e(TAG, "Error dismissing progress dialog");
			}
		}
		
		activity.processFinish(super.getAppName(), super.getAsin());
	}
}