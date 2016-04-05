package cn.xdl.lewan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ErrorCodeUtils;

public class RegisterActivity extends BaseActivity implements BaseInterface {

	private EditText etUserame, etPassword, etPassword2, etPhone, etCode;
	private Button codeBut, regBut;
	private String username, password, password2, code, phone;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	//初始化控件
	@Override
	public void initViews() {

		setContentView(R.layout.register);

		etUserame = etByid(R.id.register_username);
		etPassword = etByid(R.id.register_password);
		etPassword2 = etByid(R.id.register_password2);
		etPhone = etByid(R.id.register_phone);
		etCode = etByid(R.id.register_code);

		codeBut = butByid(R.id.register_getCode);
		regBut = butByid(R.id.register_regbut);
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {
		
		codeBut.setOnClickListener(new OnClickListener() {

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
				codeBut.setClickable(false);
				codeBut.setBackgroundResource(R.drawable.reg_selector2);
				codeBut.setTextColor(Color.parseColor("#000000"));
				CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
					//更新UI
					@Override
					public void onTick(long millisUntilFinished) {
						codeBut.setText(millisUntilFinished / 1000 + "s");
					}
					//倒计时完成
					@Override
					public void onFinish() {
						codeBut.setClickable(true);
						codeBut.setBackgroundResource(R.drawable.reg_selector);
						codeBut.setText("获取验证码");
						codeBut.setTextColor(Color.parseColor("#2adddd"));
					}
				};
				// 开启倒计时
				timer.start();
				// 请求发送验证码
				BmobSMS.requestSMSCode(getAct(), phone, "趣游", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// 短信验证码已验证成功
							logI("短信发送成功，短信id：" + smsId);// 用于查询本次短信发送详情
						} else {
							logI("验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
						}
					}
				});
			}
		});
		// 进行注册
		regBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// 获取注册用户的用户名和密码
				username = etUserame.getText().toString().trim();
				password = etPassword.getText().toString().trim();
				password2 = etPassword2.getText().toString().trim();

				// 获取手机号和验证码
				phone = etPhone.getText().toString().trim();
				code = etCode.getText().toString().trim();
				if ("".equals(username) || "".equals(password)) {
					toastS("用户名和密码不能为空");
					return;
				}
				if (password.length()<6||password.length()>12) {
					toastS("密码长度应为6-12字母、数字组成");
					return;
				}
				if (!password.equals(password2)) {
					toastS("两次密码不一致，请重新输入");
					return;
				}
				DialogUtils.showDialog(getAct(), null, null, true);
				// 验证验证码是否正确
				BmobSMS.verifySmsCode(getAct(), phone, code, new VerifySMSCodeListener() {

					@Override
					public void done(BmobException ex) {
						if (ex == null) {// 短信验证码已验证成功
							logI("短信验证通过");
							final UserBean ub = new UserBean();
							Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.mruicon);
							File file = new File(getCacheDir(),"lewan.png");
							try {
								bit.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							final BmobFile bmfile = new BmobFile(file);
							bmfile.upload(getAct(), new UploadFileListener() {
								
								@Override
								public void onSuccess() {
									ub.setUserIcon(bmfile);
									ub.setUsername(username);
									ub.setPassword(password);
									ub.setMobilePhoneNumber(phone);
									ub.setMobilePhoneNumberVerified(true);
									ub.signUp(getAct(), new SaveListener() {

										@Override
										public void onSuccess() {
											DialogUtils.dismiss();
											toastS("恭喜你注册成功，请去登陆");
											Intent data = new Intent();
											data.putExtra("phone", phone);
											data.putExtra("password", password);
											setResult(1, data);
											finish();
										}

										@Override
										public void onFailure(int arg0, String arg1) {
											toastS(ErrorCodeUtils.getErrorMessage(arg0));
											DialogUtils.dismiss();
										}
									});
								}
								
								@Override
								public void onFailure(int arg0, String arg1) {
									
								}
							});
							
						} else {
							//短信验证失败
							toastS(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
							DialogUtils.dismiss();
						}
					}
				});
			}
		});
	}

	// 返回登陆界面
	public void backOnClick(View v) {
		finish();
	}
}
