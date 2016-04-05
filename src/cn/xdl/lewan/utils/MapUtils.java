package cn.xdl.lewan.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class MapUtils {
	
	/**��λ�ڼ�������ľ������
	 * @param start ��ʼλ������
	 * @param end ����Ϊֹ����
	 * @return ���룬�����ڵ���1000m������xkm,���򷵻�xm
	 */
	public static String getKm(LatLng start, LatLng end){
		String text = null;
		int juli = (int) DistanceUtil.getDistance(start, end);
		if (juli>=1000) {
			juli+=500;
			text = (juli/1000)+"km";
		}else{
			text = juli+"m";
		}
		return text;
	}
}
