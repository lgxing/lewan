package cn.xdl.lewan.utils;

/**截取字符串工具类
 * @author lgx
 *
 */
public class TextUtils {
	
	public static String textLengthMax(String text){
		if (text.length()>=5) {
			text = text.substring(0,5)+"...";
		}
		return text;
	}
}
