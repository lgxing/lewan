package cn.xdl.lewan.fragment;

import java.util.List;

import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.xdl.lewan.HomeActivity;
import cn.xdl.lewan.R;
import cn.xdl.lewan.adapter.MsgAdapter;
import cn.xdl.lewan.base.BaseFragment;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.MsgBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;

public class MsgFragment extends BaseFragment implements BaseInterface {

	private ImageView menu;
	private ListView lv;
	private MsgAdapter adapter;
	private String uid;
	private BmobRealTimeData rtd;
	private List<MsgBean> datas;
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
		return inflater.inflate(R.layout.fragment_msg, null);
	}

	@Override
	public void initViews() {
		
		menu = imgByid(R.id.frag_msg_menu);
		lv = (ListView) findViewById(R.id.fragment_msg_lv);
	}

	@Override
	public void initDatas() {
		uid = BmobUser.getCurrentUser(getActivity(), UserBean.class).getObjectId();
		serchDatas();
		//实时更新数据
		rtd = new BmobRealTimeData();
		rtd.start(getActivity(), new ValueEventListener() {
			
			@Override
			public void onDataChange(JSONObject arg0) {
				serchDatas();
			}
			
			@Override
			public void onConnectCompleted() {
				if (rtd.isConnected()) {
					rtd.subTableUpdate("MsgBean");
				}
			}
		});
	}
	//设置数据源
	private void serchDatas() {
		
		BmobQuery<MsgBean> query = new BmobQuery<MsgBean>();
		query.addWhereEqualTo("reciveId", uid);
		query.findObjects(getActivity(), new FindListener<MsgBean>() {
			

			@Override
			public void onSuccess(List<MsgBean> arg0) {
				if (arg0==null||arg0.size()==0) {
					return;
				}
				datas = arg0;
				adapter = new MsgAdapter(arg0, getActivity());
				lv.setAdapter(adapter);
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				logI("MsgFragment:"+arg1);
			}
		});
	}
	
	@Override
	public void initViewOper() {
		//删除消息
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				DialogUtils.showAlertDialog(getActivity(), "警告!", "您确定删除该消息吗？", true, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						MsgBean mBean = new MsgBean();
						mBean.setObjectId(datas.get(position).getObjectId());
						mBean.delete(getContext(),new DeleteListener() {
							
							@Override
							public void onSuccess() {
								Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								DialogUtils.dismiss();
								Toast.makeText(getActivity(), "删除失败！:"+arg1, Toast.LENGTH_SHORT).show();
							}
						});
						
					}
				});
				return true;
			}
		});
		//监听抽屉的状态变化
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

	}
	

	
	
	
	
}
