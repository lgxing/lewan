package cn.xdl.lewan.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 操作dialog的工具类
 * 
 * @author lgx
 *
 */
public class DialogUtils {

	private static ProgressDialog dialog;

	/**
	 * 显示dialog
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            dialog标题
	 * @param msg
	 *            dialog内容
	 * @param isCancelable
	 *            是否可以取消
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
	 * 取消dialog
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
		alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).create();
		alertDialog.show();
	}
}
