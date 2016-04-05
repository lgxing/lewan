package cn.xdl.lewan;

import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.xdl.lewan.adapter.PinglunAdpter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.OrderBean;
import cn.xdl.lewan.bean.PinglunBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ImageLoaderutils;
import cn.xdl.lewan.utils.ListViewForScrollView;

public class ShowGatherActivity extends BaseActivity implements BaseInterface {

	private GatherBean gather;
	private UserBean ub;
	private TextView tvCanYu, tvGuanLi, tvGName, tvGType, tvGInfo, tvGTime, tvGRmb, tvCeHua, tvGPhone, tvAddress,
			tvlove, tvYicanyu;
	private ImageView imgGPng;
	private DisplayImageOptions opt;// �ͼƬ
	private ImageLoader loader;
	private String cehua, number;
	private PoiInfo poi;
	private EditText etPinglun;
	private PinglunAdpter adapter;
	private ListViewForScrollView lv;
	private String uid;
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
		setContentView(R.layout.act_gathershow);
		tvCanYu = tvByid(R.id.act_gathershow_canyu);
		tvGuanLi = tvByid(R.id.act_gathershow_guanli);
		tvGName = tvByid(R.id.act_gathershow_gname);
		tvGType = tvByid(R.id.act_gathershow_gtype);
		tvGInfo = tvByid(R.id.act_gathershow_ginfo);
		tvGTime = tvByid(R.id.act_gathershow_gtime);
		tvGRmb = tvByid(R.id.act_gathershow_grmb);
		imgGPng = imgByid(R.id.act_gathershow_gpng);
		tvCeHua = tvByid(R.id.act_gathershow_gcehua);
		tvGPhone = tvByid(R.id.act_gathershow_gnumber);
		tvAddress = tvByid(R.id.act_gathershow_gaddress);
		tvlove = tvByid(R.id.act_gathershow_loved);
		tvYicanyu = tvByid(R.id.act_gathershow_yicanyu);
		etPinglun = etByid(R.id.act_gathershow_pinglun);
		lv = (ListViewForScrollView) findViewById(R.id.act_gathershow_lv);

	}

	@Override
	public void initDatas() {
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		gather = (GatherBean) MyApplication.getData(getIntent().getStringExtra("key"), true);
		poi = gather.getPoi();
		loader = ImageLoaderutils.getInstance(getAct());
		findGatehrCehua(gather.getU_id());
		uid = gather.getU_id();
		
	}

	// ��ѯ��ķ�����
	private void findGatehrCehua(String u_id) {
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", u_id);
		query.findObjects(getAct(), new FindListener<UserBean>() {

			@Override
			public void onSuccess(List<UserBean> arg0) {
				cehua = arg0.get(0).getUsername();
				number = arg0.get(0).getMobilePhoneNumber();
				tvCeHua.setText(cehua);
				tvGPhone.setText(number);
			}

			@Override
			public void onError(int arg0, String arg1) {
				toastS(arg1);
			}
		});

	}

	@Override
	public void initViewOper() {

		if (ub.getObjectId().equals(gather.getU_id())) {
			tvCanYu.setVisibility(View.INVISIBLE);
			tvGuanLi.setVisibility(View.VISIBLE);
		}
		tvGName.setText(gather.getName());
		tvGType.setText(gather.getType());
		tvGInfo.setText(gather.getInfo());
		tvGTime.setText(gather.getTime());
		tvAddress.setText(poi.name);
		tvGRmb.setText(gather.getRmb() + "Ԫ");
		tvYicanyu.setText(gather.getYifukuanIds().size() + "���Ѳ���");
		tvlove.setText(gather.getLoveCount() + "�����ղ�");
		
		imgGPng.setImageResource(R.drawable.jiazaipng);// Ĭ�ϼ���һ��ͼƬ
		// ���ػͼƬ
		loader.displayImage(gather.getGatherPNG().getFileUrl(getAct()), imgGPng, opt);
		// ׼�����۵�����Դ
		getDatas();
	}

	private void getDatas() {
		//���ݻId�õ��û����������
		BmobQuery<PinglunBean> query = new BmobQuery<PinglunBean>();
		query.addWhereEqualTo("gatherid", gather.getObjectId());
		query.findObjects(getAct(), new FindListener<PinglunBean>() {

			@Override
			public void onError(int arg0, String arg1) {

			}
			//��ѯ�ɹ��Ժ���������Դ
			@Override
			public void onSuccess(List<PinglunBean> datas) {
				if (datas.size() == 0) {
					return;
				}
				adapter = new PinglunAdpter(datas, getAct(),uid);
				lv.setAdapter(adapter);
			}
		});
	}
	//���������û������Ļ
	public void canyuOnClick(View v) {
		DialogUtils.showDialog(getAct(), null, null, true);
		final Integer rmb = gather.getRmb();
		// �ж��Ƿ��ѲμӸû
		if (gather.getYifukuanIds().contains(ub.getObjectId())) {
			toastS("���Ѳ���˻");
			return;
		}
		
		// ֧������
		BP.pay(getAct(), "����:" + gather.getName(), gather.getInfo(), 0.02, true, new PListener() {
			// ������
			private String orderId;
			
			@Override
			public void unknow() {
				DialogUtils.dismiss();
				toastS("֧�����δ֪,���Ժ��ֶ���ѯ");
			}
			
			// ֧���ɹ�
			@Override
			public void succeed() {
				DialogUtils.dismiss();
				// toastS(orderId);
				// ��ѯ�����ţ��������Ų�ѯ�ɹ�����д洢
				BP.query(getAct(), orderId, new QListener() {

					@Override
					public void succeed(String arg0) {
						//֧���ɹ����涩����
						OrderBean oBean = new OrderBean();
						oBean.setOrderNum(orderId);
						oBean.setOrderRmb(rmb);
						oBean.setUserId(ub.getObjectId());
						oBean.setGatherId(gather.getObjectId());
						oBean.save(getAct(), new SaveListener() {
							// ��������ɹ�����û��ͻ����и���
							@Override
							public void onSuccess() {
								logI("��������ɹ�");

								GatherBean ngather = new GatherBean();
								// ���������Ѹ�����û�Id
								gather.getYifukuanIds().add(ub.getObjectId());
								// �����������Ѹ�����û�Id
								ngather.addUnique("yifukuanIds", ub.getObjectId());
								ngather.update(getAct(), gather.getObjectId(), new UpdateListener() {

									@Override
									public void onSuccess() {

									}

									@Override
									public void onFailure(int arg0, String arg1) {
										logI(arg1);
									}
								});
								
								UserBean uuBean = new UserBean();
								// ���ؼ�֧����Ķ�����
								ub.getAplayedONum().add(orderId);
								// ��������֧����Ķ�����
								uuBean.addUnique("aplayedONum", orderId);
								uuBean.update(getAct(), ub.getObjectId(), new UpdateListener() {

									@Override
									public void onSuccess() {
										toastS("֧���ɹ���");
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										toastS(arg0 + "-" + arg1);
									}
								});
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								logI(arg1);
							}
						});

					}

					@Override
					public void fail(int arg0, String arg1) {
						DialogUtils.dismiss();
						
					}
				});
			}

			@Override
			public void orderId(String orderId) {
				this.orderId = orderId;
			}

			@Override
			public void fail(int arg0, String arg1) {
				if (arg0 == 6001 || arg0 == -2) {
					toastS("֧�����ж�");
					DialogUtils.dismiss();
				} else if (arg0 == -3) {
					toastS("֧���쳣");
				}
			}
		});
	}

	// ���۲���
	public void pinglunOnClick(View v) {
		
		String text = etPinglun.getText().toString().trim();
		//�����۶��������ֶ�
		PinglunBean pb = new PinglunBean();
		pb.setContent(text);
		pb.setGatherid(gather.getObjectId());
		pb.setUid(ub.getObjectId());
		// �ָ�Ĭ��ֵ
		etPinglun.setText("");
		
		// ��������
		pb.save(getAct(), new SaveListener() {

			@Override
			public void onSuccess() {
				toastS("���۳ɹ���");
				//��������Դ
				getDatas();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				logI(arg0 + "--" + arg1);
			}
		});
	}
	
	//�����Լ������Ļ
	public void manageOnClick(View v){
		
		MyApplication.putData("gather", gather);
		Intent intent = new Intent(getAct(), PushActivty.class);
		intent.putExtra("key", "gather");
		getAct().startActivity(intent);
		
		
	}
	
	// ����
	public void backOnClick(View v) {
		finish();
	}
}
