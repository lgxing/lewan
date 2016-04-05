package cn.xdl.lewan.base;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter extends BaseAdapter {

	@Override
	public abstract int getCount() ;

	@Override
	public abstract Object getItem(int position) ;

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent) ;
	
	/**
	 * 用来更改数据源
	 */
	@SuppressWarnings("rawtypes")
	public abstract void setData(List data);
	
	/**
	 * 用来增加数据源
	 */
	@SuppressWarnings("rawtypes")
	public abstract void addData(List data);
}
