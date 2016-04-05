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

/**接收消息的广播，在这对消息进行上传服务器的操作
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
			Log.e("lewan", "BmobPushDemo收到消息：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
			Toast.makeText(context,"您有新的推送消息", Toast.LENGTH_LONG).show();
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
	//将消息上传服务器
	private void saveMsg(String name, String content) {
		MsgBean mBean = new MsgBean();
		mBean.setReciveId(BmobUser.getCurrentUser(context, UserBean.class).getObjectId());
		mBean.setSendName(name);
		mBean.setContent(content);
		mBean.save(context, new SaveListener() {

			@Override
			public void onSuccess() {
				Log.i("lewan", "MyPushMessageReceiver:上传成功");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("lewan", "MyPushMessageReceiver:上传失败");
			}
		});
	}
}
