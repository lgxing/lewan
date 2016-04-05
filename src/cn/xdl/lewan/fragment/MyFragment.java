package cn.xdl.lewan.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.xdl.lewan.HomeActivity;
import cn.xdl.lewan.R;
import cn.xdl.lewan.ShowGatherActivity;
import cn.xdl.lewan.adapter.MyLvAdapter;
import cn.xdl.lewan.adapter.MyOrderAdapter;
import cn.xdl.lewan.adapter.YouHQAdapter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseFragment;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.OrderBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.bean.YouHuiQBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ErrorCodeUtils;
import cn.xdl.lewan.utils.ImageLoaderutils;

public class MyFragment extends BaseFragment implements BaseInterface, OnClickListener {
	private TextView tvUsername, myLove, mySend, uAge, uAdress, uSex ,myDingdan,myYouhui;
	private ImageView imUserIcon, menu;
	private UserBean ub;
	private ImageLoader loader;
	private DisplayImageOptions opt;
	private MyLvAdapter adapter;
	private MyOrderAdapter orderadapter;
	private ListView lView;
	private LinearLayout[] linears = new LinearLayout[4];
	private int[] ids = { R.id.fragment_my_sclin, R.id.fragment_my_sendlin, R.id.fragment_my_ddlin,
			R.id.fragment_my_yhlin };

	@SuppressLint("InflateParams")
	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	// 根据不同的查询条件查询相应活动
	private void getGatherBean(BmobQuery<GatherBean> query) {
		DialogUtils.showDialog(getActivity(), null, "加载中...", true);
		query.findObjects(getActivity(), new FindListener<GatherBean>() {
			
			@Override
			public void onSuccess(final List<GatherBean> datas) {
				DialogUtils.dismiss();
				if (datas==null||datas.size()==0) {
					toastS("没有相关信息！");
					return;
				}
				adapter = new MyLvAdapter(datas, getActivity());
				lView.setAdapter(adapter);
				//对ListView进行监听，做出跳转
				lView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MyApplication.putData("my_gather", datas.get(position));
						Intent intent = new Intent(getActivity(), ShowGatherActivity.class);
						intent.putExtra("key", "my_gather");
						getActivity().startActivity(intent);
					}
				});
			}

			@Override
			public void onError(int arg0, String arg1) {
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}
		});
	}

	@Override
	public void initViews() {
		tvUsername = tvByid(R.id.fragment_my_username);
		uAge = tvByid(R.id.fragment_my_userage);
		uSex = tvByid(R.id.fragment_my_usersex);
		uAdress = tvByid(R.id.fragment_my_useraddress);
		imUserIcon = imgByid(R.id.fragment_my_usericon);
		myLove = tvByid(R.id.fragment_my_love);
		mySend = tvByid(R.id.fragment_my_send);
		myDingdan = tvByid(R.id.fragment_my_dingdan);
		myYouhui = tvByid(R.id.fragment_my_youhui);
		
		menu = imgByid(R.id.fragment_my_menu);
		lView = (ListView) findViewById(R.id.fragment_my_lv);
		for (int i = 0; i < 4; i++) {
			linears[i] = linByid(ids[i]);
			linears[i].setOnClickListener(this);
		}
	}

	@Override
	public void initDatas() {
		// 得到当前登录用户对象
		ub = BmobUser.getCurrentUser(getActivity(), UserBean.class);

		loader = ImageLoaderutils.getInstance(getActivity());
		opt = ImageLoaderutils.getOpt();
		//显示当前用户的头像
		loader.displayImage(ub.getUserIcon().getFileUrl(getActivity()), imUserIcon, opt);
		
	}

	@Override
	public void initViewOper() {
		
		//对抽屉状态进行监听
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (HomeActivity.drawer.isDrawerOpen(Gravity.START)) {
					HomeActivity.drawer.closeDrawers();
				} else {
					HomeActivity.drawer.openDrawer(Gravity.START);
				}
			}
		});

		// 显示登陆用户的用户名
		tvUsername.setText(ub.getUsername());
		if (ub.getAge()==null) {
			uAge.setText("");
		}else {
			uAge.setText(ub.getAge() + "岁");
		}
		uAdress.setText(ub.getAddress());
		uSex.setText(ub.getSex());
		// 更改用户头像
		imUserIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

				intent.putExtra("return-data", true);
				intent.setType("image/*");
				// 剪裁
				intent.putExtra("crop", "circleCrop");
				// 剪裁比例
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				// 剪裁大小
				intent.putExtra("outputX", 50);
				intent.putExtra("outputY", 50);
				startActivityForResult(intent, 1);
			}
		});
	}
	//在相册选择完毕以后的操作
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			//将其存到本地
			if (data != null && data.getExtras() != null) {
				final Bitmap bit = (Bitmap) data.getExtras().get("data");
				File file = new File(getActivity().getCacheDir(), "usericon.png");
				try {
					bit.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				DialogUtils.showDialog(getActivity(), null, null, true);
				final BmobFile bmfile = new BmobFile(file);
				//更新服务器里的数据
				bmfile.uploadblock(getActivity(), new UploadFileListener() {

					@Override
					public void onSuccess() {
						imUserIcon.setImageBitmap(bit);
						ub.setUserIcon(bmfile);
						UserBean uu = new UserBean();
						uu.setUserIcon(bmfile);
						uu.update(getActivity(), ub.getObjectId(), new UpdateListener() {

							@Override
							public void onSuccess() {
								DialogUtils.dismiss();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								DialogUtils.dismiss();
								toastS("头像更新失败");
							}
						});
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						DialogUtils.dismiss();
						toastS("头像修改失败");
					}
				});
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		// 查询用户发布活动的数量
		BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
		query.addWhereEqualTo("u_id", ub.getObjectId());
		query.count(getActivity(), GatherBean.class, new CountListener() {

			@Override
			public void onFailure(int arg0, String arg1) {

			}

			@Override
			public void onSuccess(int count) {
				mySend.setText("" + count);
			}
		});

		// 查询用户收藏活动的数量
		BmobQuery<UserBean> query2 = new BmobQuery<UserBean>();
		query2.getObject(getActivity(), ub.getObjectId(), new GetListener<UserBean>() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				
			}
			
			@Override
			public void onSuccess(UserBean arg0) {
				myLove.setText(""+arg0.getLoveGather_id().size());
			}
		});
		
		//查询当前用户订单数量
		BmobQuery<UserBean> query3 = new BmobQuery<UserBean>();
		query3.getObject(getActivity(), ub.getObjectId(), new GetListener<UserBean>() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				
			}
			
			@Override
			public void onSuccess(UserBean arg0) {
				myDingdan.setText(""+arg0.getAplayedONum().size());
			}
		});
		
		//查询优惠券的数量
		getYhqNum();
		
	}
	private void getYhqNum() {
		BmobQuery<YouHuiQBean> query4 = new BmobQuery<YouHuiQBean>();
		query4.addWhereEqualTo("uid", ub.getObjectId());
		query4.findObjects(getActivity(), new FindListener<YouHuiQBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(List<YouHuiQBean> arg0) {
				if (arg0==null) {
					myYouhui.setText(0+"");
				}else {
					myYouhui.setText(arg0.size()+"");
				}
				
			}
		});
	}

	//对个人中心选项进行监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_my_sclin://收藏
			updateView(R.id.fragment_my_sclin);
			BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
			query.addWhereContains("loveUserIds", ub.getObjectId());
			//查询收藏的活动
			getGatherBean(query);
			break;
		case R.id.fragment_my_sendlin://发起
			updateView(R.id.fragment_my_sendlin);
			BmobQuery<GatherBean> query2 = new BmobQuery<GatherBean>();
			query2.addWhereEqualTo("u_id", ub.getObjectId());
			//查询发起的活动
			getGatherBean(query2);
			//删除活动
			break;
		case R.id.fragment_my_ddlin:
			updateView(R.id.fragment_my_ddlin);
			BmobQuery<OrderBean> query3 = new BmobQuery<OrderBean>();
			query3.addWhereEqualTo("userId", ub.getObjectId());
			//查询订单
			getOrderBean(query3);
			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
				}
			});
			break;
		case R.id.fragment_my_yhlin://优惠
			updateView(R.id.fragment_my_yhlin);
			getYouhuiQ();
			break;
		}
	}
	


	//查询当前用户的优惠券
	private void getYouhuiQ() {
		
		BmobQuery<YouHuiQBean> query = new BmobQuery<YouHuiQBean>();
		query.addWhereEqualTo("uid", ub.getObjectId());
		DialogUtils.showDialog(getActivity(), null, "加载中...", true);
		query.findObjects(getActivity(), new FindListener<YouHuiQBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				logI(arg1+"--------");
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}

			@Override
			public void onSuccess(List<YouHuiQBean> arg0) {
				DialogUtils.dismiss();
				if (arg0==null||arg0.size()==0) {
					toastS("很遗憾，您还没有优惠券");
					return;
				}
				
				YouHQAdapter adapter = new YouHQAdapter(arg0, getActivity());
				lView.setAdapter(adapter);
				lView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					}
				});
			}
		});
	}

	//更具当前用户ID查找该用户的所有订单
	private void getOrderBean(BmobQuery<OrderBean> query3) {
		DialogUtils.showDialog(getActivity(), null, "加载中...", true);
		query3.findObjects(getActivity(), new FindListener<OrderBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				logI(arg0+"--"+arg1);
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}

			@Override
			public void onSuccess(List<OrderBean> datas) {
				DialogUtils.dismiss();
				if (datas==null||datas.size()==0) {
					toastS("您还没有订单，赶快参与活动去吧！");
					return;
				}
				//查找成功以后设置数据源
				orderadapter = new MyOrderAdapter(datas, getActivity());
				lView.setAdapter(orderadapter);
			}
		});
	}

	
	//更改选项颜色
	private void updateView(int linid) {
		for (int i = 0; i < 4; i++) {
			if (ids[i]==linid) {
				linears[i].setBackgroundColor(Color.parseColor("#e2e2e2"));
			}else {
				linears[i].setBackgroundColor(Color.parseColor("#ffffff"));
			}
		}
	}

	
}
