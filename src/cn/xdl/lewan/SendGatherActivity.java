package cn.xdl.lewan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ErrorCodeUtils;
import cn.xdl.lewan.utils.GetMoreMenuBean;
import cn.xdl.lewan.utils.IsEdNull;

public class SendGatherActivity extends BaseActivity implements BaseInterface {
	
	private LinearLayout getCityLayout,getTimeLayout,imgLayout;
	private TextView tvCity,tvTime;
	private PoiInfo poiInfo;
	private ImageView mGImg;
	private EditText gatherName,gatherInfo,gatherRMB;
	private Spinner sptype;
	private BmobFile bmfile;
	private UserBean ub;
	private List<String> types = GetMoreMenuBean.getMenuText();
	protected String gatherType_Str = types.get(0);
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	
	/**�����
	 * @param v
	 */
	public void sendOnClick(View v){
		if (!IsEdNull.isNull(gatherName,gatherInfo,gatherRMB,tvTime)) {
			toastS("��������д��Ϣ");
			return;
		}
		if (bmfile==null||bmfile.getFileUrl(getAct())==null) {
			toastS("��ѡ��ͼƬ");
			return;
		}
		if (poiInfo==null) {
			toastS("��ѡ���ص�");
			return;
		}
		//��������ֶ�
		GatherBean gBean = new GatherBean();
		gBean.setName(getText(gatherName));
		gBean.setInfo(getText(gatherInfo));
		gBean.setType(gatherType_Str);
		gBean.setGatherPNG(bmfile);
		gBean.setTime(getText(tvTime));
		gBean.setU_id(ub.getObjectId());
		gBean.setRmb(Integer.parseInt(getText(gatherRMB)));
		gBean.setPoi(poiInfo);
		gBean.setPoint(new BmobGeoPoint(poiInfo.location.longitude,poiInfo.location.latitude));
		DialogUtils.showDialog(getAct(), null, null, true);
		//����
		gBean.save(getAct(), new SaveListener() {
			
			@Override
			public void onSuccess() {
				DialogUtils.dismiss();
				toastS("������ɹ�");
				((MyApplication)(getAct().getApplication())).initData();
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				DialogUtils.dismiss();
				toastS("����ʧ��");
				return;
			}
		});
	}
	
	@Override
	public void initViews() {
		setContentView(R.layout.act_sendgather);
		getCityLayout = linByid(R.id.act_sendgather_maplayout);
		getTimeLayout = linByid(R.id.act_sendgather_timelayout);
		imgLayout = linByid(R.id.act_sendgather_pnglayout);
		mGImg = imgByid(R.id.act_sendgather_png);
		tvCity = tvByid(R.id.act_sendgather_city);
		tvTime = tvByid(R.id.act_sendgather_time);
		gatherName = etByid(R.id.act_sendgather_name);
		gatherInfo = etByid(R.id.act_sendgather_info);
		gatherRMB = etByid(R.id.act_sendgather_rmb);
		sptype = (Spinner) findViewById(R.id.act_sendgather_type);
	}

	@Override
	public void initDatas() {
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		
	}
	@SuppressLint("InflateParams")
	@Override
	//�Ի��Ƭ���л���
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0==1) {
			if (arg2!=null&&arg2.getExtras()!=null) {
				Bitmap bit = (Bitmap) arg2.getExtras().get("data");
				imgLayout.removeView(mGImg);
				//�ڻ��ϸ���ͼƬ
				View v = getLayoutInflater().inflate(R.layout.gatherimg, null);
				ImageView gatherimg_img = (ImageView) v.findViewById(R.id.gatherimg_img);
				gatherimg_img.setImageBitmap(bit);
				imgLayout.addView(v);
				imgLayout.addView(mGImg);
				
				try {
					File file = new File(getCacheDir(),"mgpng.png");
					bit.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
					bmfile = new BmobFile(file);
					bmfile.uploadblock(getAct(), new UploadFileListener() {
						
						@Override
						public void onSuccess() {
							logI("�ɹ�");
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							toastS(ErrorCodeUtils.getErrorMessage(arg0));
							return;
						}
					});
				
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
		
		}
	}
	@Override
	public void initViewOper() {
		
		//��ӻͼƬ
		mGImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				
				intent.putExtra("return-data", true);
				intent.setType("image/*");
				//����
				intent.putExtra("crop", "circleCrop");
				//���ñ���
				intent.putExtra("aspectX", 3);
				intent.putExtra("aspectY", 2);
				//���ô�С
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 214);
				startActivityForResult(intent, 1);
			}
		});
		
		//��ת����ͼҳ��ѡ��ص�
		getCityLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getAct(), MapActivity.class));
			}
		});
		//ѡ��ʱ�����
		getTimeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerDialog date = new DatePickerDialog(getAct(), new OnDateSetListener() {
					TimePickerDialog time = null;
					@Override
					public void onDateSet(DatePicker view,  final int year,  final int monthOfYear,  final int dayOfMonth) {
						if (time==null)
							time =  new TimePickerDialog(getAct(), new OnTimeSetListener() {
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								tvTime.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" "+hourOfDay+":"+minute);
//								logI(year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" "+hourOfDay+":"+minute);
							}
						}, 0, 0, true);
						if(!time.isShowing()){
							time.show();
						}
						
					}
				}, 2016, 0, 1);
				date.show();
			}
		});
		//ѡ������
		sptype.setAdapter(new ArrayAdapter<>(getAct(), android.R.layout.simple_list_item_1,types));
		sptype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				gatherType_Str = types.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//�Ե�ͼ���ݽ��л���
		poiInfo = (PoiInfo)MyApplication.getData("mapKey", true);
		if (poiInfo!=null) {
			tvCity.setText(poiInfo.name);
		}
	}
	//��������
	public void backOnClick(View v){
		DialogUtils.showAlertDialog(getAct(), "����", "��ȷ�������༭��", true, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
	}
	
}
