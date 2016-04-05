package cn.xdl.lewan;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ErrorCodeUtils;

public class FindPswActivity extends BaseActivity implements BaseInterface {

	private EditText etPassword, etPassword2, etPhone, etCode;
	private Button getCodeBut, surebut;
	private String password, password2, phone, code;

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
		setContentView(R.layout.act_findpsw);
		etPassword = etByid(R.id.act_findpsw_password);
		etPassword2 = etByid(R.id.act_findpsw_password2);
		etPhone = etByid(R.id.act_findpsw_phone);
		etCode = etByid(R.id.act_findpsw_code);

		getCodeBut = butByid(R.id.act_findpsw_getCode);
		surebut = butByid(R.id.act_findpsw_surebut);
	}

	@Override
	public void initDatas() {
		
	}

	@Override
	public void initViewOper() {
		//获取验证码
		getCodeBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取手机号
				phone = etPhone.getText().toString().trim();
				// 验证手机号是否有误
				if (!phone.matches("^(17[0|7]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					toastS("您的手机号码有误，请检查");
					return;
				}
				// 获取验证码后进行倒计时操作
				getCodeBut.setClickable(false);
				getCodeBut.setBackgroundResource(R.drawable.reg_selector2);
				getCodeBut.setTextColor(Color.parseColor("#000000"));
				CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
					// 更新UI
					@Override
					public void onTick(long millisUntilFinished) {
						getCodeBut.setText(millisUntilFinished / 1000 + "s");
					}

					// 倒计时完成
					@Override
					public void onFinish() {
						getCodeBut.setClickable(true);
						getCodeBut.setBackgroundResource(R.drawable.reg_selector);
						getCodeBut.setText("获取验证码");
						getCodeBut.setTextColor(Color.parseColor("#2adddd"));
					}
				};
				// 开启倒计时
				timer.start();
				// 请求发送验证码
				BmobSMS.requestSMSCode(getAct(), phone, "乐玩验证码", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// 验证码发送成功
							Log.i("smile", "短信id：" + smsId);// 用于查询本次短信发送详情
						}
					}
				});
			}

		});

		// 找回密码
		surebut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				password = etPassword.getText().toString().trim();
				password2 = etPassword2.getText().toString().trim();

				// 获取手机号和验证码
				phone = etPhone.getText().toString().trim();
				code = etCode.getText().toString().trim();
				if (password.length() < 6 || password.length() > 12) {
					toastS("密码长度应为6-12字母、数字组成");
					return;
				}
				if (!password.equals(password2)) {
					toastS("两次密码不一致，请重新输入");
					return;
				}
				DialogUtils.showDialog(getAct(), null, null, true);
				//对密码进行重置
				UserBean.resetPasswordBySMSCode(getAct(), code, password, new ResetPasswordByCodeListener() {
					//重置完成
					@Override
					public void done(cn.bmob.v3.exception.BmobException ex) {
						 if(ex==null){
					            DialogUtils.dismiss();
					            toastS("密码重置成功");
					            //TODO 数据未回显
//					            MyApplication.putData("find_username", phone);
//					            MyApplication.putData("find_password", password);
					            finish();
					        }else{
					        	toastS(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
					        	DialogUtils.dismiss();
					        }
					}
				});
			}
		});
	}
	//返回
	public void backOnClick(View v){
		finish();
	}
	
}
