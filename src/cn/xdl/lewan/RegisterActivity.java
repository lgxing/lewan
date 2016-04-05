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
	//��ʼ���ؼ�
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

				// ��ȡ�ֻ���
				phone = etPhone.getText().toString().trim();
				// ��֤�ֻ����Ƿ�����
				if (!phone.matches("^(17[0|7]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					toastS("�����ֻ�������������");
					return;
				}
				// ��ȡ��֤�����е���ʱ����
				codeBut.setClickable(false);
				codeBut.setBackgroundResource(R.drawable.reg_selector2);
				codeBut.setTextColor(Color.parseColor("#000000"));
				CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
					//����UI
					@Override
					public void onTick(long millisUntilFinished) {
						codeBut.setText(millisUntilFinished / 1000 + "s");
					}
					//����ʱ���
					@Override
					public void onFinish() {
						codeBut.setClickable(true);
						codeBut.setBackgroundResource(R.drawable.reg_selector);
						codeBut.setText("��ȡ��֤��");
						codeBut.setTextColor(Color.parseColor("#2adddd"));
					}
				};
				// ��������ʱ
				timer.start();
				// ��������֤��
				BmobSMS.requestSMSCode(getAct(), phone, "Ȥ��", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// ������֤������֤�ɹ�
							logI("���ŷ��ͳɹ�������id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
						} else {
							logI("��֤ʧ�ܣ�code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
						}
					}
				});
			}
		});
		// ����ע��
		regBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// ��ȡע���û����û���������
				username = etUserame.getText().toString().trim();
				password = etPassword.getText().toString().trim();
				password2 = etPassword2.getText().toString().trim();

				// ��ȡ�ֻ��ź���֤��
				phone = etPhone.getText().toString().trim();
				code = etCode.getText().toString().trim();
				if ("".equals(username) || "".equals(password)) {
					toastS("�û��������벻��Ϊ��");
					return;
				}
				if (password.length()<6||password.length()>12) {
					toastS("���볤��ӦΪ6-12��ĸ���������");
					return;
				}
				if (!password.equals(password2)) {
					toastS("�������벻һ�£�����������");
					return;
				}
				DialogUtils.showDialog(getAct(), null, null, true);
				// ��֤��֤���Ƿ���ȷ
				BmobSMS.verifySmsCode(getAct(), phone, code, new VerifySMSCodeListener() {

					@Override
					public void done(BmobException ex) {
						if (ex == null) {// ������֤������֤�ɹ�
							logI("������֤ͨ��");
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
											toastS("��ϲ��ע��ɹ�����ȥ��½");
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
							//������֤ʧ��
							toastS(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
							DialogUtils.dismiss();
						}
					}
				});
			}
		});
	}

	// ���ص�½����
	public void backOnClick(View v) {
		finish();
	}
}
