package com.example.bunnyweather.adapter;

import java.util.List;

import com.example.bunnyweather.model.ListItem;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private Context context;
	private List<ListItem> listData;
	
	public MyAdapter(Context context, List<ListItem> listData) {
		this.context = context;
		this.listData = listData;
		Log.e("Constructor return listData.get(position);", listData.toString());
		Log.e("Constructor listData.size()", listData.size() +"");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public ListItem getItem(int position) {
		Log.e("getItem position", position +" " + context + " " + this);
		if(position < listData.size()) {
			return listData.get(position);
		} 
		return new ListItem();
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
		//return 0;
	}
	
	public int getId(int position) {
		return getItem(position).getId();
	}
	
	public String getName(int position) {
		return getItem(position).getName();
	}
	
	public String getCode(int position) {
		//Log.e("getCode", position +"");
		return getItem(position).getCode();
	}
	
	public String getParentCode(int position) {
		return getItem(position).getParentCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Log.e("getView position", position + "");
		Log.e("getView arg1", convertView +"");
//		TextView textView = new TextView(context);
//		textView.setTextSize(25);
//		textView.setText(getId(position) + " " + getName(position) + " " + getCode(position) + " " + getParentCode(position));
//		int id = listData.get(position).getId();
//		String name = listData.get(position).getName();
//		String code = listData.get(position).getCode();
//		String parentCode = listData.get(position).getParentCode();
		
		ViewHolder holder;
		if(convertView == null) {
			convertView = new TextView(context);
			holder=new ViewHolder();
			holder.textView = (TextView) convertView;
			convertView.setTag(holder);
		} else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.textView.setTextSize(25);
		holder.textView.setText(getId(position) + " " + getName(position) + " " + getCode(position) + " " + getParentCode(position));
		//holder.textView.setText(id + " " + name + " " + code + " " +parentCode);
		return holder.textView;
	}
	
	static class ViewHolder {//匿名内部类，使用的是静态类
		TextView textView;
		}

}
