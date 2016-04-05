package cn.xdl.lewan.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.xdl.lewan.R;
import cn.xdl.lewan.bean.MoreBean;

public class MoreGridAdapter extends BaseAdapter {
	private List<MoreBean> datas;
	private LayoutInflater inflater;
	
	
	public MoreGridAdapter(List<MoreBean> datas, LayoutInflater inflater) {
		super();
		this.datas = datas;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		
		v = inflater.inflate(R.layout.grid_item, null);
		ImageView img = (ImageView) v.findViewById(R.id.grid_item_img);
		TextView tv = (TextView) v.findViewById(R.id.grid_item_tv);
		img.setImageBitmap(datas.get(position).getIcon());
		tv.setText(datas.get(position).getText());
		
		return v;
	}

}
