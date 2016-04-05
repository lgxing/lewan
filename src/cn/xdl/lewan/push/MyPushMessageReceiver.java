package cn.xdl.lewan.push;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.xdl.lewan.bean.MsgBean;
import cn.xdl.lewan.bean.UserBean;

/**������Ϣ�Ĺ㲥���������Ϣ�����ϴ��������Ĳ���
 * @author lgx
 *
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
	
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			Log.e("lewan", "BmobPushDemo�յ���Ϣ��" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
			Toast.makeText(context,"�����µ�������Ϣ", Toast.LENGTH_LONG).show();
			// MyApplication.putData("receiveMsg",
			// intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
			String text = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			try {
				JSONObject json = new JSONObject(text);
				String name = json.getString("name");
				String content = json.getString("content");
				saveMsg(name, content);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	//����Ϣ�ϴ�������
	private void saveMsg(String name, String content) {
		MsgBean mBean = new MsgBean();
		mBean.setReciveId(BmobUser.getCurrentUser(context, UserBean.class).getObjectId());
		mBean.setSendName(name);
		mBean.setContent(content);
		mBean.save(context, new SaveListener() {

			@Override
			public void onSuccess() {
				Log.i("lewan", "MyPushMessageReceiver:�ϴ��ɹ�");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("lewan", "MyPushMessageReceiver:�ϴ�ʧ��");
			}
		});
	}
}
