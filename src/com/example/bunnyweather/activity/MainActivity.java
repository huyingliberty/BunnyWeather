package com.example.bunnyweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.bunnyweather.R;
import com.example.bunnyweather.adapter.MyAdapter;
import com.example.bunnyweather.db.DB;
import com.example.bunnyweather.model.ListItem;
import com.example.bunnyweather.util.Const;
import com.example.bunnyweather.util.HttpUtil;
import com.example.bunnyweather.util.HttpUtilCallBack;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnItemClickListener {
	private TextView title;
	private ListView list;
	private ProgressBar progressBar;
	private DB db;
	private List<ListItem> listData = new ArrayList<ListItem>();
	private String titleString;
	private String parentCode;
	private String address;
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		getTitleAndParentCode();
		getAddress();
		initView();
		initData();
	}
	
	@Override
	protected void onRestart() {
		Log.e("onRestart parentCode", parentCode);
		initData();
		super.onRestart();
	}
	
	@Override
	protected void onPause() {
		Log.e("onPause", list.getAdapter().getCount() +"");
		adapter = null;
		super.onPause();
	}
	
	
	
	private void getTitleAndParentCode() {
		Intent intent = getIntent();
		titleString = intent.getStringExtra("title");
		parentCode = intent.getStringExtra("parentCode");
		if(titleString == null) {
			titleString = "";
		} 
		if(parentCode == null) {
			parentCode = "0";
		}
		
		Log.e("titleString", titleString);
		Log.e("parentCode", parentCode);
	}
	
	

	private void getAddress() {
		if(parentCode.equals("0")) {
			address = Const.URL_FULL;
		} else {
			address = Const.URL_SINGLE + parentCode + ".xml";
		}
		Log.e("address", address);
	}
	
	private void initView() {
		if(titleString.equals(getResources().getString(R.string.country))) {
			Intent intent = new Intent();
			intent.putExtra("address", address);
			intent.setClass(this, WeatherActivity.class);
			startActivity(intent);
			finish();
		}
		
		
		db = DB.getInstance(this);
		title = (TextView) findViewById(R.id.title);
		list = (ListView) findViewById(R.id.list);
		progressBar = (ProgressBar) findViewById(R.id.pro_bar);
		list.setOnItemClickListener(this);
	}
	
	private void initData() {
		if(titleString.equals(getResources().getString(R.string.province))) {
			title.setText(R.string.city);
		} else if (titleString.equals(getResources().getString(R.string.city))) {
			title.setText(R.string.country);
		} else {
			title.setText(R.string.province);
		}
		setListData();	
	
	}

	private void setListData() {
		listData = db.getListData(parentCode);
		if(listData.isEmpty()) {
			getListDataFromServer();			
		}else {
			Log.e("setListData listData", listData.size()+"");
			setListAdapter(listData);
		} 
		
	}

	private void setListAdapter(List<ListItem> listData) {
		Log.e("setListAdapter", listData.size()+"");
		adapter = new MyAdapter(MainActivity.this, listData);
		list.setAdapter(adapter);		
	}

	private void getListDataFromServer() {
		progressBar.setVisibility(View.VISIBLE);
		HttpUtil.sendHttpRequest(address,  new HttpUtilCallBack(){

			@Override
			public void onFinished(String response) {
				ArrayList<String[]> list = dataProcess(response);
				Log.e("list.size()####################", list.size() + "");
				store2Database(list, parentCode);
				Log.e("store2Database ####################", "store2Database end");
				MainActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Log.e("onFinished####################", "onFinished");
						listData = db.getListData(parentCode);
						setListAdapter(listData);
						progressBar.setVisibility(View.GONE);
						
					}
				});
				
				
			}

			@Override
			public void onError() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(MainActivity.this, R.string.network_error, Const.TOAST_DURATION).show();
						
					}
				});
				
				
			}
			
		});
		
	}
	
	private ArrayList<String[]> dataProcess(String response) {
     // 01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾
		String[] code_name_string_list = response.split(",");
		ArrayList<String[]> list = new ArrayList<String[]>();
		for(int i = 0; i < code_name_string_list.length; ++i) {
			String tmpString = code_name_string_list[i];
			list.add(code_name_string_list[i].split("\\|"));
		}
		return list;
	}
	
	private void store2Database(ArrayList<String[]> list, String parentCode) {
		Log.e("store2Database", "store2Database");
		db.insert2Table(list, parentCode);	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		String pCode = adapter.getCode(position);
//		String address = Const.URL_SINGLE + parentCode + ".xml";
		String titleString = title.getText().toString();
//		if(titleString.equals(getResources().getString(R.string.province))) {
//			setListData(address, parentCode);
//			title.setText(R.string.city);		
//		} else if (titleString.equals(getResources().getString(R.string.city))) {
//			setListData(address, parentCode);
//			title.setText(R.string.country);
//		}
		startActivity(titleString, pCode);		
		
	}
	
	private void startActivity(String title, String parentCode) {
		Intent intent = new Intent();
		intent.putExtra("title", title);
		intent.putExtra("parentCode", parentCode);
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		//finish();
	}

	
}
