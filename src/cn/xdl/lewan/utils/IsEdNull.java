package cn.xdl.lewan.utils;

import android.widget.TextView;

public class IsEdNull {

	/**�����ж�editText �Ƿ�Ϊ��
	 * @param gatherName �ɱ����
	 * @return ��false�п�ֵ���ڣ� true��������
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
