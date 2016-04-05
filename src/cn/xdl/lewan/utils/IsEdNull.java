package cn.xdl.lewan.utils;

import android.widget.TextView;

public class IsEdNull {

	/**用来判断editText 是否为空
	 * @param gatherName 可变参数
	 * @return 若false有空值存在， true代表都不空
	 */
	public static boolean isNull(TextView... gatherName) {
		
		for (TextView et : gatherName) {
			if (et==null||et.getText().toString().trim().equals("")) {
				return false;
			}
		}
		return true;
	}

}
