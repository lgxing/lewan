package cn.xdl.lewan.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.xdl.lewan.HomeActivity;
import cn.xdl.lewan.R;
import cn.xdl.lewan.adapter.HomeLvAdapter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.application.MyApplication.findDatasListener;
import cn.xdl.lewan.base.BaseFragment;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.view.XListView;
import cn.xdl.lewan.view.XListView.IXListViewListener;

public class HomeFragment extends BaseFragment implements BaseInterface {
	private XListView lv;
	private HomeLvAdapter adapter;
	private List<GatherBean> gatherdatas;
	private ImageView menu;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViews();
		((MyApplication)(getActivity().getApplication())).initData();
		getGatherBean();
	}

	@SuppressWarnings("unchecked")
	//获得所有活动的数据源
	private void getGatherBean() {
		gatherdatas = (List<GatherBean>) MyApplication.getData("findDatasKey", true);
		//数据获得成功的监听
		((MyApplication) getActivity().getApplication()).setmFindDatasListener(new findDatasListener() {
			//获得成功后对其进行操作
			@Override
			public void onSuccess(List<GatherBean> datas) {
				if (datas==null) {
					gatherdatas = (List<GatherBean>) MyApplication.getData("skidDatasKey", true);
					lv.stopLoadMore();
					if (adapter == null) {
						initDatas();
						initViewOper();
					}
					adapter.addData(gatherdatas);
				}else {
					logI("此处是回调接口触发了刷新Ui");
					gatherdatas = datas;
					if (adapter == null) {
						initDatas();
						initViewOper();
					} else {
						adapter.setData(gatherdatas);
						lv.stopRefresh();
					}
				}
			}
		});
		if (gatherdatas != null) {
			logI("当界面被加载时，已经有数据了");
			initDatas();
			initViewOper();
		}

	}

	@Override
	public void initViews() {
		lv = (XListView) findViewById(R.id.fragment_home_lv);
		menu = imgByid(R.id.fragment_home_menu);
	}

	@Override
	public void initDatas() {
		adapter = new HomeLvAdapter(gatherdatas, getActivity());
	}

	@Override
	public void initViewOper() {
		lv.setAdapter(adapter);
		lv.setPullLoadEnable(true);
		lv.setXListViewListener(new IXListViewListener() {
			//上拉刷新操作
			@Override
			public void onRefresh() {
				((MyApplication) (getActivity().getApplication())).initData();
			}
			//向下加载数据
			@Override
			public void onLoadMore() {
				((MyApplication) getActivity().getApplication()).onLoadMore();
			}
		});
		//弹出菜单
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(HomeActivity.drawer.isDrawerOpen(Gravity.START)){
					HomeActivity.drawer.closeDrawers();
				}else{
					HomeActivity.drawer.openDrawer(Gravity.START);
				}
			}
		});

	}

	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_home, null);
	}
}
