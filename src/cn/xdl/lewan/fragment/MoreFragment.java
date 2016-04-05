package cn.xdl.lewan.fragment;

import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.xdl.lewan.MoreActivity;
import cn.xdl.lewan.R;
import cn.xdl.lewan.adapter.MoreGridAdapter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseFragment;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.MoreBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.bean.YouHuiQBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.GetMoreMenuBean;
import cn.xdl.lewan.utils.TextUtils;

public class MoreFragment extends BaseFragment implements BaseInterface, OnClickListener {

	private GridView grid;
	private List<MoreBean> menuIcon;
	private Button[] Buts = new Button[3];
	private ImageView imgCz;
	private EditText etCz;
	private String address;
	private TextView tvaddress;
	private ImageView chouImg;
	private int money;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_more, null);
	}

	@Override
	public void initViews() {
		
		chouImg = imgByid(R.id.fragment_more_imgyhq);
		grid = (GridView) findViewById(R.id.fragment_more_grid);
		Buts[0] = (Button) findViewById(R.id.fragment_more_butmf);
		Buts[1] = (Button) findViewById(R.id.fragment_more_butrm);
		Buts[2] = (Button) findViewById(R.id.fragment_more_butfj);
		tvaddress = tvByid(R.id.fragment_more_address);
		for (int i = 0; i < Buts.length; i++) {
			Buts[i].setOnClickListener(this);
		}
		imgCz = imgByid(R.id.fragment_more_chazhao);
		etCz = etByid(R.id.fragment_more_etcz);
		imgCz.setOnClickListener(this);
		
	}

	@Override
	public void initDatas() {
		menuIcon = GetMoreMenuBean.getMenuIcon(getActivity());
		address = (String) MyApplication.getData("dwaddress", false);
		
		chouImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imgChouyhq();
			}
		});
	}

	@Override
	public void initViewOper() {
		
		tvaddress.setText(address);
		grid.setAdapter(new MoreGridAdapter(menuIcon, getActivity().getLayoutInflater()));
		// 设置响应事件
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String text = menuIcon.get(position).getText();
				//根据活动类型查找该类型活动的集合
				BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
				query.addWhereEqualTo("type", text);
				DialogUtils.showDialog(getActivity(), null, null, true);
				query.findObjects(getActivity(), new FindListener<GatherBean>() {

					@Override//查找成功后进行存储，以便在另外界面进行显示
					public void onSuccess(List<GatherBean> data) {
						DialogUtils.dismiss();
						if (data != null && data.size() > 0) {
							String key = "findGatherBean";
							// 检索数据
							MyApplication.putData(key, data);
							// 跳转界面
							Intent intent = new Intent(getActivity(), MoreActivity.class);
							intent.putExtra("key", key);
							intent.putExtra("NumberKey", 1);
							startActivity(intent);
						} else {
							toastS("对不起，没有相关内容");
						}
					}

					@Override
					public void onError(int arg0, String arg1) {
						DialogUtils.dismiss();
					}
				});
			}
		});
	}

	int numberKey = -1;
	@Override
	public void onClick(View v) {
		numberKey = -1;
		BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
		switch (v.getId()) {
		case R.id.fragment_more_butmf://免费
			query.addWhereEqualTo("rmb", 0);
			numberKey = 2;
			break;
		case R.id.fragment_more_butrm://热门
			query.order("-loveCount"); 
			numberKey = 3;
			break;
		case R.id.fragment_more_butfj://附近
			query.addWhereNear("point", new BmobGeoPoint(MyApplication.getLocations().get(0).longitude, MyApplication.getLocations().get(0).latitude));
			numberKey = 4;
			break;
		case R.id.fragment_more_chazhao://模糊查找
			query.addWhereContains("name", etCz.getText().toString().trim());
			numberKey = 5;
			break;
//		case R.id.fragment_more_imgyhq://抽优惠券
//			imgChouyhq();
//			break;
		}
		DialogUtils.showDialog(getActivity(), null, null, true);
		//根据不同查询条件进行查询，做出相应的跳转
		query.findObjects(getActivity(), new FindListener<GatherBean>() {
			
			@Override
			public void onSuccess(List<GatherBean> data) {
				DialogUtils.dismiss();
				if (data != null && data.size() > 0) {
					String key = "findGatherBean";
					// 检索数据
					MyApplication.putData(key, data);
					// 跳转界面
					Intent intent = new Intent(getActivity(), MoreActivity.class);
					intent.putExtra("key", key);
					intent.putExtra("NumberKey", numberKey);
					if (numberKey==5) {
						intent.putExtra("title_Str", TextUtils.textLengthMax(etCz.getText().toString().trim()));
					}
					startActivity(intent);
				} else {
					toastS("对不起，没有相关内容");
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				DialogUtils.dismiss();
			}
		});
		
		
	}
	public void imgChouyhq(){
		
		logI("点了吗？？？？");
		Random random = new Random();
		int[] nums = {0,5,0,0,0,0,15,0,0,0,0,20,0,0,0,0,10,0,0,5,0,0,0,0,5,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0};
		//产生优惠券的金额
		money = nums[random.nextInt(nums.length)];
		if (money==0) {
			toastS("很遗憾，您没有抽中！！！");
		}else {
			YouHuiQBean yBean = new YouHuiQBean();
			yBean.setUid(BmobUser.getCurrentUser(getActivity(), UserBean.class).getObjectId());
			yBean.setJine(money);
			yBean.save(getActivity(), new SaveListener() {
				
				@Override
				public void onSuccess() {
					toastS("恭喜您抽中"+money+"元优惠券");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					logI(arg1+"抽优惠");
				}
			});
		}
		
	}
}
