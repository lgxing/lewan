package cn.xdl.lewan.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**���ϳ�ȡ��Fragment����
 * @author lgx
 *
 */
public abstract class BaseFragment extends Fragment {
	private View fragmentview;
	public View findViewById(int id){
		return getView().findViewById(id);
	}
	private static final String TAG = "lewan";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (fragmentview == null) {
			fragmentview = getfragmentView(inflater,container,savedInstanceState);
		}
		return fragmentview;
	}

	public abstract View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) ;

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
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}
	/**������ʾ (��ʱ��ʾ)
	 * @param text ��Ҫ�������ı�
	 */
	public void toastL(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	/**��ӡ��ʾ
	 * @param text ��Ҫ��ӡ������
	 */
	public void logI(String text){
		Log.i(TAG, text);
	}
	
}
