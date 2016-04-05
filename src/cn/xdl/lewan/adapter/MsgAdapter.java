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
import cn.xdl.lewan.bean.MsgBean;

public class MsgAdapter extends MyBaseAdapter {
	
	private List<MsgBean> datas;
	private LayoutInflater inflater;
	@SuppressWarnings("unused")
	private Context context;
	
	
	public MsgAdapter(List<MsgBean> datas, Context context) {
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
	//对接收到的推送消息进行展示
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh;
		if (v==null) {
			v = inflater.inflate(R.layout.msg_item, null);
			vh = new ViewHolder();
			vh.tvName = (TextView) v.findViewById(R.id.msg_item_uname);
			vh.tvTime = (TextView) v.findViewById(R.id.msg_item_time);
			vh.tvContent = (TextView) v.findViewById(R.id.msg_item_content);
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		
		//显示收到消息的各种字段
		vh.tvContent.setText(datas.get(position).getContent());
		vh.tvName.setText(datas.get(position).getSendName());
		vh.tvTime.setText(datas.get(position).getCreatedAt());
		return v;
	}
	
	class ViewHolder{
		TextView tvName,tvTime,tvContent ;
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
