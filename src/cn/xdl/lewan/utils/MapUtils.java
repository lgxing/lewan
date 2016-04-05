package cn.xdl.lewan.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class MapUtils {
	
	/**定位于检索结果的距离计算
	 * @param start 起始位置坐标
	 * @param end 结束为止坐标
	 * @return 距离，若大于等于1000m，返回xkm,否则返回xm
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
