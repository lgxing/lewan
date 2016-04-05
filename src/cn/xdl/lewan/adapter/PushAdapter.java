package cn.xdl.lewan.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.xdl.lewan.R;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.MyBaseAdapter;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.ImageLoaderutils;
import cn.xdl.lewan.view.MyImageView;

public class PushAdapter extends MyBaseAdapter {

	private List<UserBean> datas;
	private Context context;
	private LayoutInflater inflater;
	private ImageLoader loader;
	private DisplayImageOptions opt;
	private List<String> iddatds;
	public PushAdapter(List<UserBean> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
		loader = ImageLoaderutils.getInstance(context);
		opt = ImageLoaderutils.getOpt(); 
		iddatds = new ArrayList<>();
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
	//接收推送消息用户的展示
	public View getView(final int position, View v, ViewGroup parent) {
		ViewHolder vh = null;
		if (v==null) {
			v = inflater.inflate(R.layout.push_item, null);
			vh = new ViewHolder();
			vh.imgUicon = (MyImageView) v.findViewById(R.id.push_item_usericon);
			vh.tvName = (TextView) v.findViewById(R.id.push_item_uname);
			vh.checkBox = (CheckBox) v.findViewById(R.id.push_item_checked);
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		vh.tvName.setText(datas.get(position).getUsername());
		loader.displayImage(datas.get(position).getUserIcon().getFileUrl(context), vh.imgUicon, opt);
		vh.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				iddatds.add(datas.get(position).getObjectId());
				if (isChecked) {
					MyApplication.putData("ischecked", iddatds);
				}
			}
		});
		
		return v;
	}
	
	class ViewHolder{
		MyImageView imgUicon;
		TextView tvName;
		CheckBox checkBox;
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
