package com.isosoft.freeappdaily;

import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.isosoft.freeappdaily.task.AppCheckServiceTask;
import com.isosoft.freeappdaily.task.AsyncResponse;


public class AppCheckService extends Service implements AsyncResponse {
	private static final String TAG = AppCheckService.class.getSimpleName();
	private AppCheckServiceTask task;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "Service Started: onStartCommand, received "+intent.getAction()+" "+new Date());
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		this.task = new AppCheckServiceTask(this);
		task.execute();
		return Service.START_NOT_STICKY;
	}
	
	private void notifyUser(String appName, String asin) {
		Intent notiIntent = new Intent(this, MainActivity.class);
		notiIntent.putExtra(MainActivity.ASIN, asin);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, notiIntent, 0);
		
		Notification noti = new Notification.Builder(this)
			.setContentTitle(appName)
			.setContentText("Today's Amazon free app: "+appName)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pIntent).getNotification();
		
		
		NotificationManager notificationManager = 
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, noti);
	}

	@Override
	public void processFinish(String appName, String asin) {
		Log.d(TAG, "Setting notification with ASIN: "+asin);
		if (appName != null && asin != null) {
			notifyUser(appName, asin);
		} else {
			Log.e(TAG, "App name or asin null: "+appName +", "+asin);
		}
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	
//	public void onDestroy() {
//		Toast.makeText(this, "DailyApp Service Stopped :(", Toast.LENGTH_LONG).show();
//		Log.d(TAG, "onDestroy");
//	}
	
	
}
