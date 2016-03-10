package com.example.bunnyweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

public class HttpUtil {
	private static HttpURLConnection connection;
	private static URL url;
	private static StringBuilder response ;
	private static InputStream in;
	private static BufferedReader bufferedReader;
	private static String line;
	
	public static void sendHttpRequest(final String address, final HttpUtilCallBack listener) {
		new Thread(new Runnable() {
			// http://www.weather.com.cn/data/cityinfo/101010100.html
			//http://www.weather.com.cn/data/cityinfo/101010200.html

			@Override
			public void run() {
				try {
					url = new URL(address);
					Log.e("url",url.toString());
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(60000);
					connection.setRequestMethod("GET");
					connection.setReadTimeout(60000);
					in = connection.getInputStream();
					bufferedReader = new BufferedReader(new InputStreamReader(in));
					response = new StringBuilder();
					if((line = bufferedReader.readLine()) != null) {
						response.append(line);
					}
//					int len;
//					char[] data = new char[2018]; 
//					Log.e("in", in.toString());
//					Log.e("bufferedReader", bufferedReader.toString());
//					if((len = bufferedReader.read(data)) != 0) {
//						line = String.valueOf(data, 0, len);
//						response.append(line);
//					}
					listener.onFinished(response.toString());
				} catch (Exception e) {
					Log.e("Network", e.toString());
					e.printStackTrace();
					listener.onError();
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					connection.disconnect();
				}
			}
		}).start();
	}
	
	public static HashMap<String, String> getWeatherInfo(String json) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject(json).getJSONObject("weatherinfo");
//			String city = object.getString(Const.JSON_WEATHERINFO); 
//			String tmp1 = object.getString(Const.JSON_TEMP1);
//			String tmp2 = object.getString(Const.JSON_TEMP2);
//			String weather = object.getString(Const.JSON_WEATHER);
			map.put(Const.JSON_CITY, object.getString(Const.JSON_CITY));
			map.put(Const.JSON_TEMP1, object.getString(Const.JSON_TEMP1));
			map.put(Const.JSON_TEMP2, object.getString(Const.JSON_TEMP2));
			map.put(Const.JSON_WEATHER, object.getString(Const.JSON_WEATHER));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
		
	}

}
