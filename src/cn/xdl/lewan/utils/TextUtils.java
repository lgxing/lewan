package cn.xdl.lewan.utils;

/**��ȡ�ַ���������
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
