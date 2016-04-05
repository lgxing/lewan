package cn.xdl.lewan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.xdl.lewan.adapter.PushAdapter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.UserBean;

public class PushActivty extends BaseActivity implements BaseInterface {

	private ListView lv;
	private PushAdapter adapter;
	private GatherBean gather;
	private List<String> yifkid;
	private String uid;
	private EditText pushMsg;
	@SuppressWarnings("rawtypes")
	private BmobPushManager bmobPushManager;
	private UserBean ub;
	private List<String> iddatas;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_managergather);
		lv = (ListView) findViewById(R.id.act_managergather_lv);
		pushMsg = etByid(R.id.act_managergather_pushmsg);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initDatas() {
		
		// 得到当前对象
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);

		bmobPushManager = new BmobPushManager(this);
		// 得到该活动对象
		gather = (GatherBean) MyApplication.getData(getIntent().getStringExtra("key"), true);
		// 得到已付款用户的ID集合
		yifkid = gather.getYifukuanIds();
		// 利用复合查询查询参与该活动的用户
		List<BmobQuery<UserBean>> queries = new ArrayList<BmobQuery<UserBean>>();

		for (int i = 0; i < yifkid.size(); i++) {
			uid = yifkid.get(i);
			//组装完整的or条件
			BmobQuery<UserBean> eq = new BmobQuery<UserBean>();
			eq.addWhereEqualTo("objectId", uid);
			queries.add(eq);
		}
		//查询符合整个or条件的用户
		BmobQuery<UserBean> mainQuery = new BmobQuery<UserBean>();
		mainQuery.or(queries);
		mainQuery.findObjects(this, new FindListener<UserBean>() {
			@Override
			public void onSuccess(List<UserBean> object) {
				// 设置数据源
				adapter = new PushAdapter(object, getAct());
				lv.setAdapter(adapter);
			}

			@Override
			public void onError(int code, String msg) {
				logI(msg);
			}
		});

	}

	@Override
	public void initViewOper() {

	}

	// 发送消息
	@SuppressWarnings("unchecked")
	public void pushOnClick(View v) {
		
		iddatas =  (List<String>) (MyApplication.getData("ischecked", true));
		
		String text = pushMsg.getText().toString().trim();
		if (text == null || text.length() == 0) {
			toastS("推送消息不能为空！");
			return;
		}
		
		// 创建Installation表的BmobQuery对象
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		if (iddatas==null) {
			logI("执行了吗？？？？？？？？");
			//给所有付款的人发
			query.addWhereContainedIn("uid", yifkid);
			
		}else{
			logI(iddatas.get(0));
			//给选择的人发消息
			query.addWhereContainedIn("uid", iddatas);
		}
		
		// 设置推送条件给bmobPushManager对象
		bmobPushManager.setQuery(query);
		// 组装Json数据
		JSONObject json = new JSONObject();
		try {
			json.put("name", ub.getUsername());
			json.put("content", text);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// 设置推送消息，服务端会根据上面的查询条件，来进行推送这条消息
		bmobPushManager.pushMessage(json, new PushListener() {

			@Override
			public void onSuccess() {
				toastS("发送成功！");
				pushMsg.setText("");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				logI(arg1);
			}
		});
	}

	// 返回
	public void backOnClick(View v) {
		finish();
	}
}
