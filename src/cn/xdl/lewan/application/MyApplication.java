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
 * ��Ϊ��ȫ�ֵ�����ȫ�ֿ��Է��ʵ�һ���࣬������������ݵĳ�ʼ����洢
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
	 * ���ڼ����������سɹ�����������˴˽ӿڣ��������ݴ��������У������سɹ�ʱ����ص��˽ӿ��е�onSuccess�������������ص����ݴ��ݹ�ȥ
	 * 
	 * @param mFindDatasListener
	 */
	public void setmFindDatasListener(findDatasListener mFindDatasListener) {
		this.mFindDatasListener = mFindDatasListener;
	}

	public void initData() {
		//���һ���ݣ�һ�β�10��
		FindDatasUtils.findDatas(10, this, new FindListener<GatherBean>() {

			@Override
			public void onSuccess(List<GatherBean> datas) {
				putData("findDatasKey", datas);
				Log.i("lewan", "��ѯ�ɹ�");
				count = datas.size();
				if (mFindDatasListener != null) {
					mFindDatasListener.onSuccess(datas);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				Log.i("Log", "��ѯʧ��");
				initData();// �ݹ�
			}
		}, true);
	}
	//���¼��ظ���ķ���
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
	 * ���ڼ���MyApplication�� �������ݵ�����
	 * 
	 * @author lgx
	 *
	 */
	public interface findDatasListener {
		void onSuccess(List<GatherBean> datas);
	}

	private void initBmob() {
		// ��ʼ�� Bmob SDK
		// ʹ��ʱ�뽫�ڶ�������Application ID�滻������Bmob�������˴�����Application ID
		Bmob.initialize(this, "333032f238c8ccfcc51865542e980ff1");
		BmobSMS.initialize(this, "333032f238c8ccfcc51865542e980ff1");

		BP.init(this, "333032f238c8ccfcc51865542e980ff1");
		
		// ʹ�����ͷ���ʱ�ĳ�ʼ������
		BmobInstallation.getCurrentInstallation(this).save();
		// �������ͷ���
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
							 Log.i("lewan","�豸��Ϣ���³ɹ�");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							  Log.i("lewan","�豸��Ϣ����ʧ��:"+arg1);
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
	 * �õ�һ����λ���꼯��
	 * 
	 * @return ���꼯��
	 */
	public static List<LatLng> getLocations() {
		return locations;
	}

	// �ٶȵ�ͼ����
	private void initBaidu() {
		SDKInitializer.initialize(getApplicationContext());
		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				// TODO Auto-generated method stub
				MyApplication.putData("dwaddress", arg0.getCity());
				Log.i("Log", "��λ�����" + arg0.getLongitude() + "-" + arg0.getLatitude());
				LatLng point = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				if (locations == null || locations.size() > 5) {
					locations = new ArrayList<>();
				}
				locations.add(point);
			}
		});
		initLocation();
		// ��ʼ��λ
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 1000;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(false);// ��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(false);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}

	/**
	 * Application���л������򣬵��ô˷������Ի������� �� ��ȡ����ʱ��ʹ��putData(String key, Object
	 * value)����
	 * 
	 * @param key
	 *            �洢���ݵ�key
	 * @param value
	 *            �洢������
	 */
	public static void putData(String key, Object value) {
		map.put(key, value);
	}

	/**
	 * Application���л������򣬵��ô˷������Ի�ȡputData()������洢������
	 * 
	 * @param key
	 *            Ҫ��ȡ���ݵ�key
	 * @param isDelete
	 *            ��ȡ����Ƿ�ӻ���ռ���ɾ���������
	 * @return ���ػ�õ�����
	 */
	public static Object getData(String key, boolean isDelete) {
		Object object = map.get(key);
		if (isDelete) {
			map.remove(key);
		}
		return object;

	}
}
