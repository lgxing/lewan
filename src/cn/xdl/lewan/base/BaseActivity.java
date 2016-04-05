package cn.xdl.lewan.base;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**���ϳ�ȡ������Activity����
 * ���ã�1.�ѹ��Եķ�����ȡ������2.�𵽹淶����
 * @author lgx
 *
 */
public class BaseActivity extends FragmentActivity {
	
	private static final String TAG = "lewan";

	/**����ĸ���id���һ��TextView�Ķ���
	 * @param id �ؼ�id
	 * @return TextView����
	 */
	public TextView tvByid(int id){
		return (TextView) findViewById(id);
	}
	/**����ĸ���id���һ��Button�Ķ���
	 * @param id �ؼ�id
	 * @return Button����
	 */
	public Button butByid(int id){
		return (Button) findViewById(id);
	}
	/**����ĸ���id���һ��ImageView�Ķ���
	 * @param id �ؼ�id
	 * @return ImageView����
	 */
	public ImageView imgByid(int id){
		return (ImageView) findViewById(id);
	}
	/**����ĸ���id���һ��LinearLayout�Ķ���
	 * @param id �ؼ�id
	 * @return LinearLayout����
	 */
	public LinearLayout linByid(int id){
		return (LinearLayout) findViewById(id);
	}
	/**����ĸ���id���һ��EditText�Ķ���
	 * @param id �ؼ�id
	 * @return EditText����
	 */
	public EditText etByid(int id){
		return (EditText) findViewById(id);
	}
	
	/**������ʾ (��ʱ��ʾ)
	 * @param text ��Ҫ�������ı�
	 */
	public void toastS(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	/**������ʾ (��ʱ��ʾ)
	 * @param text ��Ҫ�������ı�
	 */
	public void toastL(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	/**��ӡ��ʾ
	 * @param text ��Ҫ��ӡ������
	 */
	public void logI(String text){
		Log.i(TAG, text);
	}
	
	/**
	 * @return ����һ��������
	 */
	public BaseActivity getAct(){
		return this;
	}
	
	public String getText(TextView tv){
		return tv.getText().toString().trim();
	}
}
