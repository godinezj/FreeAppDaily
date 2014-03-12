package com.isosoft.freeappdaily;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ScheduleReceiver extends BroadcastReceiver {
	private String TAG = ScheduleReceiver.class.getSimpleName();
	
	// Restart service every 8hr
	private static final long REPEAT_TIME = 8 * 60 * 60 * 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received Broadcast: "+intent.getAction()+" "+new Date());
		AlarmManager service = (AlarmManager) context.getSystemService(
				Context.ALARM_SERVICE);
		Intent i = new Intent(context, StartServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar cal = Calendar.getInstance();
		// Start 30 seconds after boot completed
		cal.add(Calendar.SECOND, 30);
		
		// Fetch every REPEAT_TIME seconds
		// InexactRepeating allows Android to optimize the energy consumption
		service.setInexactRepeating(
				AlarmManager.RTC_WAKEUP, 
				cal.getTimeInMillis(), 
				REPEAT_TIME, 
				pending);

		// service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// REPEAT_TIME, pending);

	}
} 
