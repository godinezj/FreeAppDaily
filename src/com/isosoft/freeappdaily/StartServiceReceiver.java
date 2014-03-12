package com.isosoft.freeappdaily;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class StartServiceReceiver extends BroadcastReceiver {
	private static String TAG = StartServiceReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive, received intent: "+intent.getAction()+" "+new Date());
		Intent service = new Intent(context, AppCheckService.class);
		service.setAction("START_SERVICE");
		context.startService(service);
	}
} 