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
	private DisplayImageOptions opt1;// 头像
	private DisplayImageOptions opt2;// 活动图片
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
		// 设置用户name，头像，性别
		setText(gb.getU_id(), vh.username, vh.usericon,vh.usersex);
		vh.km.setText(MapUtils.getKm(MyApplication.getLocations().get(0), gb.getPoi().location));
		vh.rmb.setText(gb.getRmb() + "元");
		// 活动发布时间
		vh.time.setText(gb.getCreatedAt());
		// 默认加载一张图片
		vh.gatherPng.setImageResource(R.drawable.jiazaipng);
		// 加载活动图片
		loader.displayImage(gb.getGatherPNG().getFileUrl(context), vh.gatherPng, opt2);

		// 更改活动的点赞图标
		List<String> ids = gb.getLoveUserIds();
		if (!ids.contains(ub.getObjectId())) {
			vh.love.setImageResource(R.drawable.loveoff);
		} else {
			vh.love.setImageResource(R.drawable.loveon);
		}
		// 点赞与取消点赞
		updateView(vh.love, gb);

		// 进入活动详情
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
	 * 收藏与取消(心形的点击事件) 1.更改图标 （添加点赞的次数） 2.将当前用户的id添加至活动 对象中 \ 3.将当前用户的id添加至服务器中 \
	 * (保证本地数据与服务器的一致性 这个更改不能使用Bmob的Update方法 ) 4.将当前的活动id添加至用户对象中 /
	 * 5.将当前的活动id添加至服务器中 /
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
					Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
					GatherBean gbb = new GatherBean();
					// 更改点赞的次数 利用bmob原子计数器
					gbb.increment("loveCount");
					// 更改完服务器，更改本地数据
					gb.setLoveCount(gb.getLoveCount() + 1);
					// 在服务器的数据数组中追加一条
					gbb.add("loveUserIds", ub.getObjectId());
					// 本地数组中追加一条
					gb.getLoveUserIds().add(ub.getObjectId());
					// 更改服务器
					gbb.update(context, gb.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
					UserBean uu = new UserBean();
					// 服务器加一
					uu.add("loveGather_id", gb.getObjectId());
					// 本地数组加一
					ub.getLoveGather_id().add(gb.getObjectId());
					//对数据进行更新
					uu.update(context, ub.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
				} else {// 取消点赞
					love.setImageResource(R.drawable.loveoff);
					Toast.makeText(context, "已取消收藏", Toast.LENGTH_SHORT).show();
					//活动被点赞次数本地减一
					gb.setLoveCount(gb.getLoveCount() - 1);
					//活动点赞所有用户的ID本地集合移除取消点赞的用户ID
					gb.getLoveUserIds().remove(ub.getObjectId());
					GatherBean gbb = new GatherBean();
					//活动被点赞次数服务器减一
					gbb.increment("loveCount", -1);
					List<String> values = new ArrayList<>();
					values.add(ub.getObjectId());
					//活动点赞所有用户的ID服务器集合移除取消点赞的用户ID
					gbb.removeAll("loveUserIds", values);
					//更新数据
					gbb.update(context, gb.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
					UserBean uu = new UserBean();
					//用户收藏活动ID的集合本地移除该活动ID
					ub.getLoveGather_id().remove(gb.getObjectId());
					List<String> values2 = new ArrayList<>();
					values2.add(gb.getObjectId());
					//用户收藏活动ID的集合服务器移除该活动ID
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
				//设置活动发布者的用户名
				tv.setText(arg0.get(0).getUsername());
				//设置活动发布者的性别
				if (arg0.get(0).getSex().equals("男")) {
					usersex.setImageResource(R.drawable.nan);
				}else if (arg0.get(0).getSex().equals("女")) {
					usersex.setImageResource(R.drawable.nv);
				}else {
					usersex.setVisibility(View.INVISIBLE);
				}
				// 加载用户图片
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
