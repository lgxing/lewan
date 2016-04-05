package cn.xdl.lewan;


import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.baidumap.PoiOverlay;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.utils.DialogUtils;

public class MapActivity extends BaseActivity implements BaseInterface {
	private EditText et;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private PoiSearch mPoiSearch;
	private List<PoiInfo> allPoi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_map);
		
		et = (EditText) findViewById(R.id.act_map_et);
		mMapView = (MapView) findViewById(R.id.act_map_mapView);
		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	}
	
	public void onClick(View view) {
		
		String text = et.getText().toString().trim();
		String address = (String) MyApplication.getData("dwaddress", false);
		//搜索
		mPoiSearch.searchInCity((new PoiCitySearchOption()).
				city(address).keyword(text).pageNum(0));
		//定位
//		//定义Maker坐标点  
//		LatLng point = MyApplication.getLocations().get(0);  
//		//构建Marker图标  
//		BitmapDescriptor bitmap = BitmapDescriptorFactory  
//		    .fromResource(R.drawable.dingwei);  
//		//构建MarkerOption，用于在地图上添加Marker  
//		OverlayOptions option = new MarkerOptions()  
//		    .position(point)  
//		    .icon(bitmap);  
//		//在地图上添加Marker，并显示  
//		mBaiduMap.addOverlay(option);

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

			@Override
			public void onGetPoiResult(PoiResult result) {
				if (result == null || result.error == PoiResult.ERRORNO.RESULT_NOT_FOUND) {
					return;
				}
				if (result.error == PoiResult.ERRORNO.NO_ERROR) {
					allPoi = result.getAllPoi();
					mBaiduMap.clear();
					// 创建PoiOverlay
					PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
					// 设置overlay可以处理标注点击事件
					mBaiduMap.setOnMarkerClickListener(overlay);
					// 设置PoiOverlay数据
					overlay.setData(result);
					// 添加PoiOverlay到地图中
					overlay.addToMap();
					overlay.zoomToSpan();
					return;
				}
			}

			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	private class MyPoiOverlay extends PoiOverlay {
		
		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}
		
		@Override
		public boolean onPoiClick(final int index) {
			super.onPoiClick(index);
			Button button = new Button(getAct());  
			button.setBackgroundResource(R.drawable.map); 
			button.setText(allPoi.get(index).name);
			button.setTextColor(Color.BLACK);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DialogUtils.showAlertDialog(getAct(), "提示:", "您确定选择："+allPoi.get(index).name+"吗？", false, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MyApplication.putData("mapKey", allPoi.get(index));
							finish();
						}
					} );
				}
			});
			//定义用于显示该InfoWindow的坐标点  
			LatLng pt = allPoi.get(index).location;
			//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
			InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);  
			//显示InfoWindow  
			mBaiduMap.showInfoWindow(mInfoWindow);
			
			return true;
		}
	}
	
}
