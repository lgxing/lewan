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
	 * ������������Դ
	 */
	@SuppressWarnings("rawtypes")
	public abstract void setData(List data);
	
	/**
	 * ������������Դ
	 */
	@SuppressWarnings("rawtypes")
	public abstract void addData(List data);
}
