package cn.xdl.lewan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.listener.SaveListener;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ErrorCodeUtils;

public class LoginActivity extends BaseActivity implements BaseInterface {
	
	private Button regBut,logBut;
	private EditText etuser,etPassword;
	private UserBean ub2;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
		
	}
	@Override
	public void initViews() {
		setContentView(R.layout.login);
		regBut = butByid(R.id.login_regbut);
		logBut = butByid(R.id.login_logbut);
		etuser = etByid(R.id.login_username);
		etPassword = etByid(R.id.login_password);
	}

	@Override
	public void initDatas() {
		ub2 = new UserBean();
	}

	@Override
	public void initViewOper() {
		//注册操作
		regBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转注册界面
				 startActivityForResult(new Intent(getAct(), RegisterActivity.class),0);
			}
		});
		
		
		//登陆操作
		logBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取用户信息
				String username = etuser.getText().toString().trim();
				String password = etPassword.getText().toString().trim();
				if ("".equals(username)||"".equals(password)) {
					toastS("用户名和密码不能为空");
					return;
				}
				DialogUtils.showDialog(getAct(), null, null, true);
				//验证登陆
				ub2.setUsername(username);
				ub2.setPassword(password);
				ub2.setMobilePhoneNumber(username);
				ub2.login(getAct(), new SaveListener() {
					
					@Override
					public void onSuccess() {
						DialogUtils.dismiss();
						startActivity(new Intent(getAct(), HomeActivity.class));
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						//弹出错误信息
//						logI("arg0:"+arg0+"-arg1:"+arg1);
						if (ErrorCodeUtils.getErrorMessage(arg0)!=null) {
							toastS(ErrorCodeUtils.getErrorMessage(arg0));
							DialogUtils.dismiss();
						}
					}
				});
			}
		});
	}
	//注册数据的回显
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1==1) {
			etuser.setText(arg2.getStringExtra("phone"));
			etPassword.setText(arg2.getStringExtra("password"));
		}
	}
	
	//找回密码
	public void findPswOnClick(View v){
		startActivity(new Intent(getAct(), FindPswActivity.class));
	}
}
