package cn.xdl.lewan.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.xdl.lewan.R;
import cn.xdl.lewan.ShowMsgActivity;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.MyBaseAdapter;
import cn.xdl.lewan.bean.PinglunBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;

public class PinglunAdpter extends MyBaseAdapter {
	
	private List<PinglunBean> datas;
	private Context context;
	private LayoutInflater inflater;
	private UserBean uBean;
	private String sendgID;//���͸û���û�id
	
	public PinglunAdpter(List<PinglunBean> datas, Context context,String uid) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
		uBean = BmobUser.getCurrentUser(context, UserBean.class);
		sendgID = uid;
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
	public View getView(final int position, View v, ViewGroup parent) {
		ViewHolder vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.gathershow_item, null);
			vh = new ViewHolder();
			vh.tvName = (TextView) v.findViewById(R.id.gathershow_item_name);
			vh.tvPinglun = (TextView) v.findViewById(R.id.gathershow_item_pinglun);
			vh.imgDelete = (ImageView) v.findViewById(R.id.gathershow_item_delete);
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		//��ʾ��������
		vh.tvPinglun.setText(datas.get(position).getContent());
		//�õ����۷����ߵ�Id
		String uid = datas.get(position).getUid();
		//����ÿ�����۵��û���
		setText(uid,vh.tvName);
		
		//�õ����۵�Id
		final String plid = datas.get(position).getObjectId();
		//�ж�ɾ����ť�ĳ������
		if (uBean.getObjectId().equals(sendgID)) {
			vh.imgDelete.setVisibility(View.VISIBLE);
		}else {
			if(uBean.getObjectId().equals(uid)){
				vh.imgDelete.setVisibility(View.VISIBLE);
			}
		}
		//ɾ������
		vh.imgDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				PinglunBean pBean = new PinglunBean();
				pBean.setObjectId(plid);
				DialogUtils.showDialog(context, null, "ɾ����...", true);
				//ɾ������
				pBean.delete(context, new DeleteListener() {
					@Override
					public void onSuccess() {
						DialogUtils.dismiss();
						Toast.makeText(context, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
						//��������Դ
						datas.remove(position);
						notifyDataSetChanged();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						DialogUtils.dismiss();
						Log.i("lewan", arg1);
					}
				});
			}
		});
		
		return v;
	}
	
	

	private void setText(final String uid, final TextView tvName) {
		
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", uid);
		
		query.findObjects(context, new FindListener<UserBean>() {
			
			@Override
			public void onSuccess(final List<UserBean> arg0) {
				tvName.setText(arg0.get(0).getUsername()+":");
				//��ת����������
				tvName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, ShowMsgActivity.class);
						MyApplication.putData("uid", arg0.get(0).getObjectId());
						intent.putExtra("key", "uid");
						context.startActivity(intent);
					}
				});
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.i("lewan", arg1);
			}
		});
	}

	class ViewHolder{
		TextView tvName,tvPinglun;
		ImageView imgDelete;
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
