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
		
		// �õ���ǰ����
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);

		bmobPushManager = new BmobPushManager(this);
		// �õ��û����
		gather = (GatherBean) MyApplication.getData(getIntent().getStringExtra("key"), true);
		// �õ��Ѹ����û���ID����
		yifkid = gather.getYifukuanIds();
		// ���ø��ϲ�ѯ��ѯ����û���û�
		List<BmobQuery<UserBean>> queries = new ArrayList<BmobQuery<UserBean>>();

		for (int i = 0; i < yifkid.size(); i++) {
			uid = yifkid.get(i);
			//��װ������or����
			BmobQuery<UserBean> eq = new BmobQuery<UserBean>();
			eq.addWhereEqualTo("objectId", uid);
			queries.add(eq);
		}
		//��ѯ��������or�������û�
		BmobQuery<UserBean> mainQuery = new BmobQuery<UserBean>();
		mainQuery.or(queries);
		mainQuery.findObjects(this, new FindListener<UserBean>() {
			@Override
			public void onSuccess(List<UserBean> object) {
				// ��������Դ
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

	// ������Ϣ
	@SuppressWarnings("unchecked")
	public void pushOnClick(View v) {
		
		iddatas =  (List<String>) (MyApplication.getData("ischecked", true));
		
		String text = pushMsg.getText().toString().trim();
		if (text == null || text.length() == 0) {
			toastS("������Ϣ����Ϊ�գ�");
			return;
		}
		
		// ����Installation���BmobQuery����
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		if (iddatas==null) {
			logI("ִ�����𣿣�������������");
			//�����и�����˷�
			query.addWhereContainedIn("uid", yifkid);
			
		}else{
			logI(iddatas.get(0));
			//��ѡ����˷���Ϣ
			query.addWhereContainedIn("uid", iddatas);
		}
		
		// ��������������bmobPushManager����
		bmobPushManager.setQuery(query);
		// ��װJson����
		JSONObject json = new JSONObject();
		try {
			json.put("name", ub.getUsername());
			json.put("content", text);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// ����������Ϣ������˻��������Ĳ�ѯ����������������������Ϣ
		bmobPushManager.pushMessage(json, new PushListener() {

			@Override
			public void onSuccess() {
				toastS("���ͳɹ���");
				pushMsg.setText("");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				logI(arg1);
			}
		});
	}

	// ����
	public void backOnClick(View v) {
		finish();
	}
}
