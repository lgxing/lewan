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
		//��ȡ��֤��
		getCodeBut.setOnClickListener(new OnClickListener() {

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
				getCodeBut.setClickable(false);
				getCodeBut.setBackgroundResource(R.drawable.reg_selector2);
				getCodeBut.setTextColor(Color.parseColor("#000000"));
				CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
					// ����UI
					@Override
					public void onTick(long millisUntilFinished) {
						getCodeBut.setText(millisUntilFinished / 1000 + "s");
					}

					// ����ʱ���
					@Override
					public void onFinish() {
						getCodeBut.setClickable(true);
						getCodeBut.setBackgroundResource(R.drawable.reg_selector);
						getCodeBut.setText("��ȡ��֤��");
						getCodeBut.setTextColor(Color.parseColor("#2adddd"));
					}
				};
				// ��������ʱ
				timer.start();
				// ��������֤��
				BmobSMS.requestSMSCode(getAct(), phone, "������֤��", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// ��֤�뷢�ͳɹ�
							Log.i("smile", "����id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
						}
					}
				});
			}

		});

		// �һ�����
		surebut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				password = etPassword.getText().toString().trim();
				password2 = etPassword2.getText().toString().trim();

				// ��ȡ�ֻ��ź���֤��
				phone = etPhone.getText().toString().trim();
				code = etCode.getText().toString().trim();
				if (password.length() < 6 || password.length() > 12) {
					toastS("���볤��ӦΪ6-12��ĸ���������");
					return;
				}
				if (!password.equals(password2)) {
					toastS("�������벻һ�£�����������");
					return;
				}
				DialogUtils.showDialog(getAct(), null, null, true);
				//�������������
				UserBean.resetPasswordBySMSCode(getAct(), code, password, new ResetPasswordByCodeListener() {
					//�������
					@Override
					public void done(cn.bmob.v3.exception.BmobException ex) {
						 if(ex==null){
					            DialogUtils.dismiss();
					            toastS("�������óɹ�");
					            //TODO ����δ����
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
	//����
	public void backOnClick(View v){
		finish();
	}
	
}
