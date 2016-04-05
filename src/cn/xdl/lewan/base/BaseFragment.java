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

/**向上抽取的Fragment父类
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

	/**方便的根据id获得一个TextView的对象
	 * @param id 控件id
	 * @return TextView对象
	 */
	public TextView tvByid(int id){
		return (TextView) findViewById(id);
	}
	/**方便的根据id获得一个Button的对象
	 * @param id 控件id
	 * @return Button对象
	 */
	public Button butByid(int id){
		return (Button) findViewById(id);
	}
	/**方便的根据id获得一个ImageView的对象
	 * @param id 控件id
	 * @return ImageView对象
	 */
	public ImageView imgByid(int id){
		return (ImageView) findViewById(id);
	}
	/**方便的根据id获得一个LinearLayout的对象
	 * @param id 控件id
	 * @return LinearLayout对象
	 */
	public LinearLayout linByid(int id){
		return (LinearLayout) findViewById(id);
	}
	/**方便的根据id获得一个EditText的对象
	 * @param id 控件id
	 * @return EditText对象
	 */
	public EditText etByid(int id){
		return (EditText) findViewById(id);
	}
	
	/**弹窗提示 (短时提示)
	 * @param text 需要弹出的文本
	 */
	public void toastS(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}
	/**弹窗提示 (长时提示)
	 * @param text 需要弹出的文本
	 */
	public void toastL(String text){
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	/**打印提示
	 * @param text 需要打印的文字
	 */
	public void logI(String text){
		Log.i(TAG, text);
	}
	
}
