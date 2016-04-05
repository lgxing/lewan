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
import cn.xdl.lewan.bean.YouHuiQBean;

public class YouHQAdapter extends MyBaseAdapter {

	private List<YouHuiQBean> datas;
	private LayoutInflater inflater;
	@SuppressWarnings("unused")
	private Context context;
	
	
	
	public YouHQAdapter(List<YouHuiQBean> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh = new ViewHolder();
		if (v == null) {
			v= inflater.inflate(R.layout.youhq_item, null);
			vh = new ViewHolder();
			vh.tvMoney = (TextView) v.findViewById(R.id.youhq_item_money);
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		vh.tvMoney.setText(datas.get(position).getJine()+"Ԫ");
		return v;
	}
	
	class ViewHolder{
		TextView tvMoney;
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
