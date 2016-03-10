package com.example.bunnyweather.activity;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

import com.example.bunnyweather.R;
import com.example.bunnyweather.util.Const;
import com.example.bunnyweather.util.HttpUtil;
import com.example.bunnyweather.util.HttpUtilCallBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity {
	private ProgressBar pb;
	private TextView title;
	private TextView date;
	private TextView weather;
	private TextView temp1;
	private TextView temp2;
	private LinearLayout ll;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		
		initView();
		getData();
	}
	
	private void getData() {
		Intent intent = getIntent();
		String address = intent.getStringExtra("address");
		Log.e("address", address + "");
		getWeatherCodeFromServer(address);
	}
	
	private void initView(){
		title = (TextView) findViewById(R.id.title);
		pb = (ProgressBar) findViewById(R.id.pb);
		date = (TextView) findViewById(R.id.date);
		weather = (TextView) findViewById(R.id.weather);
		temp1 = (TextView) findViewById(R.id.temp1);
		temp2 = (TextView) findViewById(R.id.temp2);
		ll = (LinearLayout) findViewById(R.id.ll_weather);
	}
	
	private void initData(HashMap<String, String> map) {
		title.setText(map.get(Const.JSON_CITY));
		ll.setVisibility(View.VISIBLE);
		date.setText(getDate());
		weather.setText(map.get(Const.JSON_WEATHER));
		temp1.setText(map.get(Const.JSON_TEMP1));
		temp2.setText(map.get(Const.JSON_TEMP2));
	}
	
	private String getDate() {
		Calendar c = Calendar.getInstance();  
		int year = c.get(Calendar.YEAR);  
		int month = c.get(Calendar.MONTH);  
		int day = c.get(Calendar.DAY_OF_MONTH);
		return year + "年" + month + "月" + day + "日";
	}
	
	private void getWeatherCodeFromServer(String address) {
		pb.setVisibility(View.VISIBLE);
		HttpUtil.sendHttpRequest(address, new HttpUtilCallBack() {
			
			@Override
			public void onFinished(String response) {
				String[] tmpStrings = response.split("\\|");
				if(tmpStrings.length == 2) {
					String weatherCode = tmpStrings[1];
					getWeatherFromServer(weatherCode); 
				} else {
					onError();
				}
			}
			
			@Override
			public void onError() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						doError();
					}
				});
				
			}
		});
		
	}
	
	private void getWeatherFromServer(String weatherCode) {
		String address = Const.URL_WEATHER_SINGLE + weatherCode + ".html";
		HttpUtil.sendHttpRequest(address, new HttpUtilCallBack() {
			
			@Override
			public void onFinished(String response) {
				Log.e("response",response);
				final HashMap<String, String> map = HttpUtil.getWeatherInfo(response);
				if(map.isEmpty()) {
					onError();
				} else {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							pb.setVisibility(View.GONE);
							initData(map);
							
						}
					});
					
				}
			}
			
			@Override
			public void onError() {
				 doError();
				
			}
		});
	}
	
	private void doError() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				pb.setVisibility(View.GONE);
				Toast.makeText(WeatherActivity.this, R.string.network_error, Const.TOAST_DURATION).show();
				
			}
		});
	}

}
