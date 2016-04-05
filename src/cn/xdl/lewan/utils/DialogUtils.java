package cn.xdl.lewan.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * ����dialog�Ĺ�����
 * 
 * @author lgx
 *
 */
public class DialogUtils {

	private static ProgressDialog dialog;

	/**
	 * ��ʾdialog
	 * 
	 * @param context
	 *            ������
	 * @param title
	 *            dialog����
	 * @param msg
	 *            dialog����
	 * @param isCancelable
	 *            �Ƿ����ȡ��
	 */
	public static void showDialog(Context context, String title, String msg, boolean isCancelable) {
		dialog = new ProgressDialog(context);
		dialog.setCancelable(isCancelable);
		if (title != null) {
			dialog.setTitle(title);
		}
		if (msg != null) {
			dialog.setMessage(msg);
		}
		dialog.show();
	}

	/**
	 * ȡ��dialog
	 */
	public static void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
	}
	private static AlertDialog alertDialog;
	 
	public static void showAlertDialog(Context context, String title, String msg, boolean isCancelable,DialogInterface.OnClickListener listener) {
		alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("ȷ��", listener)
				.setNegativeButton("ȡ��", null).create();
		alertDialog.show();
	}
}
