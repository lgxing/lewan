package cn.xdl.lewan.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;

import android.app.Application;
import android.util.Log;
import c.b.BP;
import cn.bmob.push.BmobPush;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.push.MyBmobInstallation;
import cn.xdl.lewan.utils.FindDatasUtils;

/**
 * 因为是全局单利，全局可以访问的一个类，在这里进行数据的初始化与存储
 * 
 * @author lgx
 *
 */
public class MyApplication extends Application {
	private static Map<String, Object> map;
	private LocationClient mLocationClient = null;
	private static List<LatLng> locations;
	private findDatasListener mFindDatasListener;
	int count = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		map = new HashMap<>();
		initBaidu();
		initBmob();
		initData();
	}

	public findDatasListener getmFindDatasListener() {
		return mFindDatasListener;
	}

	/**
	 * 用于监听数据下载成功，如果设置了此接口，并且数据处于下载中，当下载成功时，会回调此接口中的onSuccess方法，并将下载的数据传递过去
	 * 
	 * @param mFindDatasListener
	 */
	public void setmFindDatasListener(findDatasListener mFindDatasListener) {
		this.mFindDatasListener = mFindDatasListener;
	}

	public void initData() {
		//查找活动数据，一次查10条
		FindDatasUtils.findDatas(10, this, new FindListener<GatherBean>() {

			@Override
			public void onSuccess(List<GatherBean> datas) {
				putData("findDatasKey", datas);
				Log.i("lewan", "查询成功");
				count = datas.size();
				if (mFindDatasListener != null) {
					mFindDatasListener.onSuccess(datas);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				Log.i("Log", "查询失败");
				initData();// 递归
			}
		}, true);
	}
	//向下加载更多的方法
	public void onLoadMore() {
		FindDatasUtils.findDatas(count, 10, this, new FindListener<GatherBean>() {

			@Override
			public void onSuccess(List<GatherBean> datas) {
				count += datas.size();
				putData("skidDatasKey", datas);
				if (mFindDatasListener != null) {
					mFindDatasListener.onSuccess(null);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		}, true);
	}

	/**
	 * 用于监听MyApplication中 对于数据的下载
	 * 
	 * @author lgx
	 *
	 */
	public interface findDatasListener {
		void onSuccess(List<GatherBean> datas);
	}

	private void initBmob() {
		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, "333032f238c8ccfcc51865542e980ff1");
		BmobSMS.initialize(this, "333032f238c8ccfcc51865542e980ff1");

		BP.init(this, "333032f238c8ccfcc51865542e980ff1");
		
		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation(this).save();
		// 启动推送服务
		BmobPush.startWork(this, "333032f238c8ccfcc51865542e980ff1");
		updateBmobInstallation();
	}

	private void updateBmobInstallation() {
		BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
		query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(this));
		query.findObjects(this, new FindListener<MyBmobInstallation>() {

			@Override
			public void onSuccess(List<MyBmobInstallation> object) {
				// TODO Auto-generated method stub
				if (object.size() > 0) {
					MyBmobInstallation mbi = object.get(0);
					
					mbi.setUid(BmobUser.getCurrentUser(MyApplication.this, UserBean.class).getObjectId());
				
					mbi.update(MyApplication.this, new UpdateListener() {

						@Override
						public void onSuccess() {
							 Log.i("lewan","设备信息更新成功");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							  Log.i("lewan","设备信息更新失败:"+arg1);
						}
					});

				} else {
				}
			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 得到一个定位坐标集合
	 * 
	 * @return 坐标集合
	 */
	public static List<LatLng> getLocations() {
		return locations;
	}

	// 百度地图操作
	private void initBaidu() {
		SDKInitializer.initialize(getApplicationContext());
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				// TODO Auto-generated method stub
				MyApplication.putData("dwaddress", arg0.getCity());
				Log.i("Log", "定位结果：" + arg0.getLongitude() + "-" + arg0.getLatitude());
				LatLng point = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				if (locations == null || locations.size() > 5) {
					locations = new ArrayList<>();
				}
				locations.add(point);
			}
		});
		initLocation();
		// 开始定位
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	/**
	 * Application中有缓存区域，调用此方法可以缓存数据 ， 获取数据时，使用putData(String key, Object
	 * value)方法
	 * 
	 * @param key
	 *            存储数据的key
	 * @param value
	 *            存储的数据
	 */
	public static void putData(String key, Object value) {
		map.put(key, value);
	}

	/**
	 * Application中有缓存区域，调用此方法可以获取putData()方法里存储的数据
	 * 
	 * @param key
	 *            要获取数据的key
	 * @param isDelete
	 *            获取完毕是否从缓存空间中删除这个数据
	 * @return 返回获得的数据
	 */
	public static Object getData(String key, boolean isDelete) {
		Object object = map.get(key);
		if (isDelete) {
			map.remove(key);
		}
		return object;

	}
}
