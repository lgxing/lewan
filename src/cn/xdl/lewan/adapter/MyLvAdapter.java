package cn.xdl.lewan.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.xdl.lewan.R;
import cn.xdl.lewan.base.MyBaseAdapter;
import cn.xdl.lewan.bean.GatherBean;

public class MyLvAdapter extends MyBaseAdapter {
	
	private List<GatherBean> datas;
	private LayoutInflater inflater;
	@SuppressWarnings("unused")
	private Context context;
	
	public MyLvAdapter(List<GatherBean> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@SuppressLint("InflateParams")
	@Override
	//对个人界面活动的展示
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh = null;
		if (v==null) {
			v = inflater.inflate(R.layout.my_item, null);
			vh = new ViewHolder();
			vh.tvgName = (TextView) v.findViewById(R.id.my_item_gname);
			vh.tvgTime = (TextView) v.findViewById(R.id.my_item_gtime);
			vh.tvYCy = (TextView) v.findViewById(R.id.my_item_yicanyu);
			vh.tvLoved = (TextView) v.findViewById(R.id.my_item_loved);
			vh.tvaddress = (TextView) v.findViewById(R.id.my_item_address);
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		vh.tvgName.setText(datas.get(position).getName());
		vh.tvgTime.setText(datas.get(position).getTime());
		vh.tvLoved.setText(datas.get(position).getLoveCount()+"人已收藏");
		vh.tvaddress.setText(datas.get(position).getPoi().name);
		vh.tvYCy.setText(datas.get(position).getYifukuanIds().size()+"人已参与");
		
		return v;
	}
	
	class ViewHolder{
		TextView tvgName,tvgTime,tvYCy,tvLoved,tvaddress;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setData(List data) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addData(List data) {

	}

}
