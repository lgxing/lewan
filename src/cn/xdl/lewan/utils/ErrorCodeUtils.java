package cn.xdl.lewan.utils;

/**��ʾ���ʴ�����Ϣ����
 * @author lgx
 *
 */
public class ErrorCodeUtils {
	
	/**��ô�����Ϣ
	 * @param code ������
	 * @return ������Ϣ
	 */
	public static String getErrorMessage(int code){
		String msg = null;
		switch (code) {
		case 9016:
			msg = "���������ӣ����������ֻ�����";
			break;
		case 9010:
			msg = "���糬ʱ";
			break;
		case 101:
			msg = "�û��������벻��ȷ,����������";
			break;
		case 202:
			msg = "�û����Ѿ�����";
			break;
		case 209:
			msg = "���ֻ������Ѿ�����";
			break;
		case 207:
			msg = "��֤���������������";
			break;
		case 9018:
			msg = "ѡ���Ϊ��";
			break;
		case 9019:
			msg = "���ڲ���ȷ�ĸ�ʽѡ����֤��";
			break;
		case 210:
			msg = "�����벻��ȷ,���������룡";
			break;
		}
		return msg;
	}
}
