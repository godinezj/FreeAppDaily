package com.isosoft.freeappdaily.task;

import android.content.Context;
import android.content.SharedPreferences;

import com.isosoft.freeappdaily.AppCheckService;
import com.isosoft.freeappdaily.Log;
import com.isosoft.freeappdaily.MainActivity;



public class AppCheckServiceTask extends AppCheckTask {
	private static final String TAG = AppCheckServiceTask.class.getSimpleName();
	private AppCheckService service;
	private SharedPreferences settings;
	private String currentAsin = "NOT_YET_SET_92837463";
	
	public AppCheckServiceTask(AppCheckService service) {
		super(service);
		this.service = service;
		this.settings = service.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
		this.currentAsin = settings.getString(MainActivity.ASIN, currentAsin);
		Log.d(TAG, "Retrieved current ASIN: "+currentAsin);
	}
	
	protected Void doInBackground(Void... v) {
		Log.d(TAG, "In AppCheckServiceTask.doInBackground()");
		super.doInBackground(v);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.d(TAG, currentAsin+" =? "+super.getAsin());
		if (super.getAsin() == null)
			return; 
		
		if (!currentAsin.equals(super.getAsin())) {
			this.settings.edit().putString(MainActivity.ASIN, super.getAsin()).commit();
			service.processFinish(super.getAppName(), super.getAsin());
		}
	}
}