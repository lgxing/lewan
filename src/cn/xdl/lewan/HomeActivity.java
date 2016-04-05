package cn.xdl.lewan;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.bmob.v3.BmobUser;
import cn.xdl.lewan.adapter.HomeVpAdapter;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseFragment;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.fragment.HomeFragment;
import cn.xdl.lewan.fragment.MoreFragment;
import cn.xdl.lewan.fragment.MsgFragment;
import cn.xdl.lewan.fragment.MyFragment;

public class HomeActivity extends BaseActivity implements BaseInterface, OnClickListener {
	
	private ViewPager vPager;
	private PagerAdapter adapter;
	private List<BaseFragment> frags;
	private ImageView addGather ;
	private ImageView []imgs = new ImageView[4];
	private int [] resId = {R.id.act_home_home,R.id.act_home_msg,R.id.act_home_my,R.id.act_home_more};
	private int [] imgOn = {R.drawable.home,R.drawable.msg,R.drawable.my,R.drawable.more};
	private int [] imgOff = {R.drawable.home_1,R.drawable.msg_1,R.drawable.my_1,R.drawable.more_1};
	private boolean flag = false;
	@SuppressWarnings("unused")
	private UserBean ub;
	public static DrawerLayout drawer;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		initViews();
		initDatas();
		initViewOper();
		
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_home);
		vPager = (ViewPager) findViewById(R.id.act_home_vp);
		drawer = (DrawerLayout) findViewById(R.id.drawer);
		for (int i = 0; i < 4; i++) {
			imgs[i] = imgByid(resId[i]);
			//��ѡ�����ü���
			imgs[i].setOnClickListener(this);
		}
		addGather = imgByid(R.id.act_home_addgather);
	}

	@Override
	public void initDatas() {
		//׼������Դ
		frags = new ArrayList<>();
		frags.add(new HomeFragment());
		frags.add(new MsgFragment());
		frags.add(new MyFragment());
		frags.add(new MoreFragment());
		
		adapter = new HomeVpAdapter(getSupportFragmentManager(), frags);
		
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		vPager.setAdapter(adapter);
		//��ViewPager���û�������
		vPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				updateView(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		//�������ת
		addGather.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getAct(), SendGatherActivity.class));
			}
		});
	}
	//����ѡ���л�ҳ��
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_home_home:
			updateView(0);
			vPager.setCurrentItem(0);
			break;
		case R.id.act_home_msg:
			updateView(1);
			vPager.setCurrentItem(1);
			break;
		case R.id.act_home_my:
			updateView(2);
			vPager.setCurrentItem(2);
			break;
		case R.id.act_home_more:
			updateView(3);
			vPager.setCurrentItem(3);
			break;
		}
	}
	//����ѡ��ͼ��
	private void updateView(int i) {
		for (int j = 0; j < 4; j++) {
			if (j==i) {
				imgs[j].setImageResource(imgOn[j]);
			}else {
				imgs[j].setImageResource(imgOff[j]);
			}
		}
	}
	
	//�������ؼ��˳�Ӧ��
	@Override
	public void onBackPressed() {
			
		if (flag) {
			super.onBackPressed();
			
		}else {
			toastS("�ٰ�һ�η��ؼ�������");
			flag = true;
			CountDownTimer timer = new CountDownTimer(3000,1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
				}
				@Override
				public void onFinish() {
					flag = false;
				}
			};
			timer.start();
		}
	}
	//����������ת
	public void completeOnClick(View v){
		if (drawer.isDrawerOpen(Gravity.START)) {
			drawer.closeDrawers();
		}
		startActivity(new Intent(this, UpdateMsgActivity.class));
	}
	//�޸�������ת
	public void updatepswOnClick(View v){
		if (drawer.isDrawerOpen(Gravity.START)) {
			drawer.closeDrawers();
		}
		startActivity(new Intent(getAct(), UpdatePswActivity.class));
	}
	
	//�˳���¼
	public void exitOnClick(View v){
		BmobUser.logOut(this);   //��������û�����
		finish();
		startActivity(new Intent(getAct(), LoginActivity.class));
	}
}
