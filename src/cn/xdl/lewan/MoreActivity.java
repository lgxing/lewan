package cn.xdl.lewan;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import cn.xdl.lewan.adapter.HomeLvAdapter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;

public class MoreActivity extends BaseActivity implements BaseInterface {

	private List<GatherBean> datas;
	private ListView lv;
	private TextView tvtitle;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_more);
		lv = (ListView) findViewById(R.id.act_more_lv);
		tvtitle = (TextView) findViewById(R.id.act_more_tv);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initDatas() {
		Intent intent = getIntent();
		datas = (List<GatherBean>) MyApplication.getData(getIntent().getStringExtra("key"), true);
		String title = null;
		//�л�����
		switch (intent.getIntExtra("NumberKey", -1)) {
		case 1:
			title = datas.get(0).getType();
			break;
		case 2:
			title = "���";
			break;
		case 3:
			title = "����";
			break;
		case 4:
			title = "����";
			break;
		case 5:
			title = "��"+intent.getStringExtra("title_Str")+"���";
			break;
		
		}
		tvtitle.setText(title);
		lv.setAdapter(new HomeLvAdapter(datas, getAct()));
	}

	@Override
	public void initViewOper() {
		
	}

}
