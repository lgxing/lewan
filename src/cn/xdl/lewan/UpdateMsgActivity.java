package cn.xdl.lewan;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.ImageLoaderutils;

public class UpdateMsgActivity extends BaseActivity implements BaseInterface {
	
	private ImageView imgIcon;
	private TextView username;
	private EditText etAge,etAddress;
	private RadioGroup rGroup;
	private UserBean ub;
	private ImageLoader loader;
	private DisplayImageOptions opt;
	private String address,sex;
	private Integer age;
	
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
		setContentView(R.layout.act_updatemsg);
		imgIcon = imgByid(R.id.act_updatemsg_icon);
		username = tvByid(R.id.act_updatemsg_name);
		etAge = etByid(R.id.act_updatemsg_age);
		etAddress = etByid(R.id.act_updatemsg_address);
		rGroup = (RadioGroup) findViewById(R.id.act_updatemsg_group);
	}

	@Override
	public void initDatas() {
		//�õ���ǰ�û�����
		ub = BmobUser.getCurrentUser(getAct(),UserBean.class);
		loader = ImageLoaderutils.getInstance(getAct());
		opt = ImageLoaderutils.getOpt();
	}
	
	@Override
	public void initViewOper() {
		//��ʾ�û�ͷ��
		loader.displayImage(ub.getUserIcon().getFileUrl(getAct()), imgIcon, opt);
		//��ʾ�û���
		username.setText(ub.getUsername());
		//ѡ���Ա�
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.act_updatemsg_radio1:
					sex = "��";
					break;
				case R.id.act_updatemsg_radio2:
					sex = "Ů";
					break;
				default:
					sex = "";
					break;
				}
			}
		});
	}
	//�ύ����
	public void updateOnClick(View v){
		address = etAddress.getText().toString().trim();
		if ("".equals(etAge.getText().toString().trim())) {
			age = 0;
		}else {
			age = Integer.parseInt(etAge.getText().toString().trim());
		}
		UserBean newUser = new UserBean();
		newUser.setAddress(address);
		newUser.setAge(age);
		newUser.setSex(sex);
		newUser.update(this,ub.getObjectId(),new UpdateListener() {
		    @Override
		    public void onSuccess() {
		    	toastS("�����޸ĳɹ�");
		    	finish();
		    }
		    @Override
		    public void onFailure(int code, String msg) {
		    	toastS("�����޸�ʧ��");
		    }
		});
		
	}
	//����
	public void backOnClick(View v){
		finish();
	}
	
	
}
