package cn.xdl.lewan;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.listener.UpdateListener;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ErrorCodeUtils;

public class UpdatePswActivity extends BaseActivity implements BaseInterface {

	private EditText oldPsw, newPsw1, newPsw2;
	private Button button;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_updatepsw);
		oldPsw = etByid(R.id.act_updatepsw_oldpsw);
		newPsw1 = etByid(R.id.act_updatepsw_newpsw1);
		newPsw2 = etByid(R.id.act_updatepsw_newpsw2);
		button = (Button) findViewById(R.id.act_updatepsw_but);
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {
		// �޸�����
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String oldpsw_Str = oldPsw.getText().toString().trim();
				String newPsw_Str1 = newPsw1.getText().toString().trim();
				String newPsw_Str2 = newPsw2.getText().toString().trim();

				// ��֤����
				if (newPsw_Str1.length() < 6 || newPsw_Str1.length() > 12) {
					toastS("���볤��ӦΪ6-12��ĸ���������");
					return;
				}
				if (!newPsw_Str1.equals(newPsw_Str2)) {
					toastS("�������벻һ�£�����������");
					return;
				}
				DialogUtils.showDialog(getAct(), null, "�޸���...", true);
				// �޸�����
				UserBean.updateCurrentUserPassword(getAct(), oldpsw_Str, newPsw_Str1, new UpdateListener() {

					@Override
					public void onSuccess() {
						DialogUtils.dismiss();
						toastS("�����޸ĳɹ�����������������е�¼��");
						finish();
					}

					@Override
					public void onFailure(int code, String msg) {
						DialogUtils.dismiss();
						toastS(ErrorCodeUtils.getErrorMessage(code));
						// logI("�����޸�ʧ�ܣ�"+msg+"("+code+")");
					}
				});
			}
		});
	}

	// ����
	public void backOnClick(View v) {
		finish();
	}
	
}
