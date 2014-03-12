package com.isosoft.freeappdaily.task;

import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.isosoft.freeappdaily.PropertiesUtil;



public class AppCheckTask extends AsyncTask<Void, Void, Void> {
	private static final String TAG = AppCheckTask.class.getSimpleName();
	private String appstoreUrl = "http://www.amazon.com/mobile-apps/b?node=2350149011";
	private String appHeader = "h3.fad-widget-app-name"; // "div.app-info-name";
	private String appName;
	private String asin;
	private int numtries = 3;
	private long sleeptime = 5 * 1000; 
	
	public AppCheckTask(Context ctx) {
		Properties props = PropertiesUtil.getProperties(ctx);
		appstoreUrl = props.getProperty("amazon.url");
		appHeader = props.getProperty("amazon.app.header");
	}
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAsin() {
		return asin;
	}
	
	public void setAsin(String asin) {
		this.asin = asin;
	}
	
	private HttpResponse getContent() {
		for (int num=0; num<numtries; ++num) {
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(appstoreUrl);
				return client.execute(get);
			} catch (Exception e) {
				Log.d(TAG, "Try to retrieve content failed, this is try "+num+" sleeping...");
				try {
					Thread.currentThread().sleep(sleeptime);
				} catch (InterruptedException e1) {Log.d(TAG, "can't sleep");}
			}
		}
		return null;
	}
	
	protected Void doInBackground(Void... v) {
		Log.d(TAG, "In AppCheckTask.doInBackground()");
		try {
			HttpResponse response = getContent();
			if (response == null) return null;
			
			// did not use this, for some reason it crashes on resume
			/* int timeout = 1000 * 30;
			Document doc = Jsoup.parse(new URL(appstoreUrl), timeout); */
			
			// behaves much better on activity resume
			Document doc = Jsoup.parse(response.getEntity().getContent(), null, appstoreUrl);
			
			// find the daily app link (a-href)
			Element divAppName = doc.select(appHeader).first();
			Log.d(TAG, "Retrieved "+appHeader+": "+divAppName.html());
			
			// <a href='/gp/product/B005XQLBU4'>Missile Defender</a>
			Element a = divAppName.getElementsByTag("a").first();
			String href = a.attr("href");
			this.setAsin(href.split("/")[3]);  // B005XQLBU4
			this.setAppName(a.text()); // Missile Defender
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error retrieving content");
		}
		return null;
	}
}