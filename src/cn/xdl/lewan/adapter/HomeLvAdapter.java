package cn.xdl.lewan.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.xdl.lewan.R;
import cn.xdl.lewan.ShowGatherActivity;
import cn.xdl.lewan.ShowMsgActivity;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.MyBaseAdapter;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.ImageLoaderutils;
import cn.xdl.lewan.utils.MapUtils;

public class HomeLvAdapter extends MyBaseAdapter {
	private List<GatherBean> datas;
	private LayoutInflater inflater;
	private Context context;
	private ImageLoader loader;
	private DisplayImageOptions opt1;// ͷ��
	private DisplayImageOptions opt2;// �ͼƬ
	private UserBean ub;

	public HomeLvAdapter(List<GatherBean> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
		loader = ImageLoaderutils.getInstance(context);
		opt1 = ImageLoaderutils.getOpt();
		opt2 = ImageLoaderutils.getOpt2();
		ub = BmobUser.getCurrentUser(context, UserBean.class);
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
			v = inflater.inflate(R.layout.home_item, null);
			vh = new ViewHolder();
			vh.usericon = (ImageView) v.findViewById(R.id.home_item_usericon);
			vh.gatherPng = (ImageView) v.findViewById(R.id.home_item_gatherpng);
			vh.gatherName = (TextView) v.findViewById(R.id.home_item_gathername);
			vh.username = (TextView) v.findViewById(R.id.home_item_username);
			vh.rmb = (TextView) v.findViewById(R.id.home_item_rmb);
			vh.time = (TextView) v.findViewById(R.id.home_item_time);
			vh.km = (TextView) v.findViewById(R.id.home_item_km);
			vh.usersex = (ImageView) v.findViewById(R.id.home_item_usersex);
			vh.love = (ImageView) v.findViewById(R.id.home_item_love);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}
		GatherBean gb = datas.get(position);
		vh.gatherName.setText(gb.getName());
		// �����û�name��ͷ���Ա�
		setText(gb.getU_id(), vh.username, vh.usericon,vh.usersex);
		vh.km.setText(MapUtils.getKm(MyApplication.getLocations().get(0), gb.getPoi().location));
		vh.rmb.setText(gb.getRmb() + "Ԫ");
		// �����ʱ��
		vh.time.setText(gb.getCreatedAt());
		// Ĭ�ϼ���һ��ͼƬ
		vh.gatherPng.setImageResource(R.drawable.jiazaipng);
		// ���ػͼƬ
		loader.displayImage(gb.getGatherPNG().getFileUrl(context), vh.gatherPng, opt2);

		// ���Ļ�ĵ���ͼ��
		List<String> ids = gb.getLoveUserIds();
		if (!ids.contains(ub.getObjectId())) {
			vh.love.setImageResource(R.drawable.loveoff);
		} else {
			vh.love.setImageResource(R.drawable.loveon);
		}
		// ������ȡ������
		updateView(vh.love, gb);

		// ��������
		vh.gatherPng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyApplication.putData("home_gather", datas.get(position));
				Intent intent = new Intent(context, ShowGatherActivity.class);
				intent.putExtra("key", "home_gather");
				context.startActivity(intent);
			}
		});

		return v;
	}

	/**
	 * �ղ���ȡ��(���εĵ���¼�) 1.����ͼ�� ����ӵ��޵Ĵ����� 2.����ǰ�û���id������ ������ \ 3.����ǰ�û���id������������� \
	 * (��֤�����������������һ���� ������Ĳ���ʹ��Bmob��Update���� ) 4.����ǰ�Ļid������û������� /
	 * 5.����ǰ�Ļid������������� /
	 * 
	 * @param love
	 * @param gb
	 */
	private void updateView(final ImageView love, final GatherBean gb) {
		love.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<String> ids = gb.getLoveUserIds();
				if (!ids.contains(ub.getObjectId())) {
					love.setImageResource(R.drawable.loveon);
					Toast.makeText(context, "�ղسɹ�", Toast.LENGTH_SHORT).show();
					GatherBean gbb = new GatherBean();
					// ���ĵ��޵Ĵ��� ����bmobԭ�Ӽ�����
					gbb.increment("loveCount");
					// ����������������ı�������
					gb.setLoveCount(gb.getLoveCount() + 1);
					// �ڷ�����������������׷��һ��
					gbb.add("loveUserIds", ub.getObjectId());
					// ����������׷��һ��
					gb.getLoveUserIds().add(ub.getObjectId());
					// ���ķ�����
					gbb.update(context, gb.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
					UserBean uu = new UserBean();
					// ��������һ
					uu.add("loveGather_id", gb.getObjectId());
					// ���������һ
					ub.getLoveGather_id().add(gb.getObjectId());
					//�����ݽ��и���
					uu.update(context, ub.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
				} else {// ȡ������
					love.setImageResource(R.drawable.loveoff);
					Toast.makeText(context, "��ȡ���ղ�", Toast.LENGTH_SHORT).show();
					//������޴������ؼ�һ
					gb.setLoveCount(gb.getLoveCount() - 1);
					//����������û���ID���ؼ����Ƴ�ȡ�����޵��û�ID
					gb.getLoveUserIds().remove(ub.getObjectId());
					GatherBean gbb = new GatherBean();
					//������޴�����������һ
					gbb.increment("loveCount", -1);
					List<String> values = new ArrayList<>();
					values.add(ub.getObjectId());
					//����������û���ID�����������Ƴ�ȡ�����޵��û�ID
					gbb.removeAll("loveUserIds", values);
					//��������
					gbb.update(context, gb.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
					UserBean uu = new UserBean();
					//�û��ղػID�ļ��ϱ����Ƴ��ûID
					ub.getLoveGather_id().remove(gb.getObjectId());
					List<String> values2 = new ArrayList<>();
					values2.add(gb.getObjectId());
					//�û��ղػID�ļ��Ϸ������Ƴ��ûID
					uu.removeAll("loveGather_id", values2);
					uu.update(context, ub.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
				}
			}

		});

	}

	public void setText(final String text, final TextView tv, final ImageView imgview ,final ImageView usersex) {
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", text);
		query.findObjects(context, new FindListener<UserBean>() {

			@Override
			public void onSuccess(List<UserBean> arg0) {
				//���û�����ߵ��û���
				tv.setText(arg0.get(0).getUsername());
				//���û�����ߵ��Ա�
				if (arg0.get(0).getSex().equals("��")) {
					usersex.setImageResource(R.drawable.nan);
				}else if (arg0.get(0).getSex().equals("Ů")) {
					usersex.setImageResource(R.drawable.nv);
				}else {
					usersex.setVisibility(View.INVISIBLE);
				}
				// �����û�ͼƬ
				loader.displayImage(arg0.get(0).getUserIcon().getFileUrl(context), imgview, opt1);
				imgview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, ShowMsgActivity.class);
						MyApplication.putData("uid", text);
						intent.putExtra("key", "uid");
						context.startActivity(intent);
					}
				});
			
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	class ViewHolder {
		private ImageView usericon, gatherPng, love, usersex;
		private TextView gatherName, username, rmb, time, km;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setData(List data) {
		this.datas = data;
		notifyDataSetChanged();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addData(List data) {
		this.datas.addAll(data);
		notifyDataSetChanged();

	}
}
