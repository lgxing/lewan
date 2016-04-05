package cn.xdl.lewan;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.xdl.lewan.adapter.MyLvAdapter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.ImageLoaderutils;
import cn.xdl.lewan.view.MyImageView;

public class ShowMsgActivity extends BaseActivity implements BaseInterface {
	
	private ListView lv;
	private MyLvAdapter adapter;
	private MyImageView usericon;
	private TextView tvUname,tvUaddress, tvUage,tvUsex;
	private String uid;
	private ImageLoader loader;
	private DisplayImageOptions opt;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		setContentView(R.layout.act_message);
		usericon = (MyImageView) findViewById(R.id.act_message_usericon);
		tvUname = tvByid(R.id.act_message__username);
		tvUaddress = tvByid(R.id.act_message__useraddress);
		tvUage = tvByid(R.id.act_message__userage);
		tvUsex = tvByid(R.id.act_message__usersex);
		lv = (ListView) findViewById(R.id.act_message_lv);
	}

	@Override
	public void initDatas() {
		uid = (String) MyApplication.getData(getIntent().getStringExtra("key"), true);
		loader = ImageLoaderutils.getInstance(getAct());
		opt = ImageLoaderutils.getOpt();
	}

	@Override
	public void initViewOper() {
		
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", uid);
		query.findObjects(getAct(), new FindListener<UserBean>() {
			
			@Override
			public void onSuccess(List<UserBean> arg0) {
				tvUname.setText(arg0.get(0).getUsername());
				loader.displayImage(arg0.get(0).getUserIcon().getFileUrl(getAct()), usericon, opt);
				tvUage.setText(arg0.get(0).getAge()+"");
				tvUaddress.setText(arg0.get(0).getAddress());
				tvUsex.setText(arg0.get(0).getSex());
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				logI("ShowMsgActivity:"+arg1);
			}
		});
		//根据用户id查询活动
		BmobQuery<GatherBean> query2 = new BmobQuery<GatherBean>();
		query2.addWhereEqualTo("u_id", uid);
		query2.findObjects(getAct(), new FindListener<GatherBean>() {
			
			@Override
			public void onSuccess(final List<GatherBean> datas) {
				//成功以后设置数据源
				adapter = new MyLvAdapter(datas, getAct());
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					//对listView的item进行监听跳转到活动详情界面
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MyApplication.putData("my_gather", datas.get(position));
						Intent intent = new Intent(getAct(), ShowGatherActivity.class);
						intent.putExtra("key", "my_gather");
						getAct().startActivity(intent);
					}
				});
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
	}
	//返回
	public void backOnClick(View v){
		finish();
	}
	
}
