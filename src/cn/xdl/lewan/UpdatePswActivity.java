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
		// 修改密码
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String oldpsw_Str = oldPsw.getText().toString().trim();
				String newPsw_Str1 = newPsw1.getText().toString().trim();
				String newPsw_Str2 = newPsw2.getText().toString().trim();

				// 验证密码
				if (newPsw_Str1.length() < 6 || newPsw_Str1.length() > 12) {
					toastS("密码长度应为6-12字母、数字组成");
					return;
				}
				if (!newPsw_Str1.equals(newPsw_Str2)) {
					toastS("两次密码不一致，请重新输入");
					return;
				}
				DialogUtils.showDialog(getAct(), null, "修改中...", true);
				// 修改密码
				UserBean.updateCurrentUserPassword(getAct(), oldpsw_Str, newPsw_Str1, new UpdateListener() {

					@Override
					public void onSuccess() {
						DialogUtils.dismiss();
						toastS("密码修改成功，可以用新密码进行登录啦");
						finish();
					}

					@Override
					public void onFailure(int code, String msg) {
						DialogUtils.dismiss();
						toastS(ErrorCodeUtils.getErrorMessage(code));
						// logI("密码修改失败："+msg+"("+code+")");
					}
				});
			}
		});
	}

	// 返回
	public void backOnClick(View v) {
		finish();
	}
	
}
