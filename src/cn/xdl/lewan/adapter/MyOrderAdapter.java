package cn.xdl.lewan.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.xdl.lewan.R;
import cn.xdl.lewan.base.MyBaseAdapter;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.OrderBean;

public class MyOrderAdapter extends MyBaseAdapter {
	private List<OrderBean> datas;
	private LayoutInflater inflater;
	private Context context;

	public MyOrderAdapter(List<OrderBean> datas, Context context) {
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
		// TODO Auto-generated method stub
		return datas.get(position);
	}
	
	@SuppressLint("InflateParams")
	@Override
	//个人中心订单的展示
	public View getView(int position, View v, ViewGroup parent) {
		
		ViewHolder vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.myorder_item, null);
			vh = new ViewHolder();
			vh.tvGname = (TextView) v.findViewById(R.id.myorder_item_gname);
			vh.tvGtime = (TextView) v.findViewById(R.id.myorder_item_gtime);
			vh.tvOrder = (TextView) v.findViewById(R.id.myorder_item_order);
			vh.tvOrderTime = (TextView) v.findViewById(R.id.myorder_item_ordertime);
			vh.tvOrderRmb = (TextView) v.findViewById(R.id.myorder_item_orderrmb);
			
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		vh.tvOrder.setText(datas.get(position).getOrderNum());
		vh.tvOrderTime.setText(datas.get(position).getCreatedAt());
		vh.tvOrderRmb.setText(datas.get(position).getOrderRmb()+"元");
		
		String gid = datas.get(position).getGatherId();
		setText(gid,vh.tvGname,vh.tvGtime);
		return v;
	}

	private void setText(String gid, final TextView tvGname, final TextView tvGtime) {
		BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
		query.addWhereEqualTo("objectId", gid);
		query.findObjects(context, new FindListener<GatherBean>() {
			
			@Override
			public void onSuccess(List<GatherBean> arg0) {
				tvGname.setText(arg0.get(0).getName());
				tvGtime.setText(arg0.get(0).getTime());
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
	}

	class ViewHolder {
		TextView tvGname, tvGtime, tvOrder,tvOrderTime,tvOrderRmb;
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
