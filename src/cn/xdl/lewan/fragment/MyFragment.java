package cn.xdl.lewan.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.xdl.lewan.HomeActivity;
import cn.xdl.lewan.R;
import cn.xdl.lewan.ShowGatherActivity;
import cn.xdl.lewan.adapter.MyLvAdapter;
import cn.xdl.lewan.adapter.MyOrderAdapter;
import cn.xdl.lewan.adapter.YouHQAdapter;
import cn.xdl.lewan.application.MyApplication;
import cn.xdl.lewan.base.BaseFragment;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.GatherBean;
import cn.xdl.lewan.bean.OrderBean;
import cn.xdl.lewan.bean.UserBean;
import cn.xdl.lewan.bean.YouHuiQBean;
import cn.xdl.lewan.utils.DialogUtils;
import cn.xdl.lewan.utils.ErrorCodeUtils;
import cn.xdl.lewan.utils.ImageLoaderutils;

public class MyFragment extends BaseFragment implements BaseInterface, OnClickListener {
	private TextView tvUsername, myLove, mySend, uAge, uAdress, uSex ,myDingdan,myYouhui;
	private ImageView imUserIcon, menu;
	private UserBean ub;
	private ImageLoader loader;
	private DisplayImageOptions opt;
	private MyLvAdapter adapter;
	private MyOrderAdapter orderadapter;
	private ListView lView;
	private LinearLayout[] linears = new LinearLayout[4];
	private int[] ids = { R.id.fragment_my_sclin, R.id.fragment_my_sendlin, R.id.fragment_my_ddlin,
			R.id.fragment_my_yhlin };

	@SuppressLint("InflateParams")
	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	// ���ݲ�ͬ�Ĳ�ѯ������ѯ��Ӧ�
	private void getGatherBean(BmobQuery<GatherBean> query) {
		DialogUtils.showDialog(getActivity(), null, "������...", true);
		query.findObjects(getActivity(), new FindListener<GatherBean>() {
			
			@Override
			public void onSuccess(final List<GatherBean> datas) {
				DialogUtils.dismiss();
				if (datas==null||datas.size()==0) {
					toastS("û�������Ϣ��");
					return;
				}
				adapter = new MyLvAdapter(datas, getActivity());
				lView.setAdapter(adapter);
				//��ListView���м�����������ת
				lView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MyApplication.putData("my_gather", datas.get(position));
						Intent intent = new Intent(getActivity(), ShowGatherActivity.class);
						intent.putExtra("key", "my_gather");
						getActivity().startActivity(intent);
					}
				});
			}

			@Override
			public void onError(int arg0, String arg1) {
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}
		});
	}

	@Override
	public void initViews() {
		tvUsername = tvByid(R.id.fragment_my_username);
		uAge = tvByid(R.id.fragment_my_userage);
		uSex = tvByid(R.id.fragment_my_usersex);
		uAdress = tvByid(R.id.fragment_my_useraddress);
		imUserIcon = imgByid(R.id.fragment_my_usericon);
		myLove = tvByid(R.id.fragment_my_love);
		mySend = tvByid(R.id.fragment_my_send);
		myDingdan = tvByid(R.id.fragment_my_dingdan);
		myYouhui = tvByid(R.id.fragment_my_youhui);
		
		menu = imgByid(R.id.fragment_my_menu);
		lView = (ListView) findViewById(R.id.fragment_my_lv);
		for (int i = 0; i < 4; i++) {
			linears[i] = linByid(ids[i]);
			linears[i].setOnClickListener(this);
		}
	}

	@Override
	public void initDatas() {
		// �õ���ǰ��¼�û�����
		ub = BmobUser.getCurrentUser(getActivity(), UserBean.class);

		loader = ImageLoaderutils.getInstance(getActivity());
		opt = ImageLoaderutils.getOpt();
		//��ʾ��ǰ�û���ͷ��
		loader.displayImage(ub.getUserIcon().getFileUrl(getActivity()), imUserIcon, opt);
		
	}

	@Override
	public void initViewOper() {
		
		//�Գ���״̬���м���
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (HomeActivity.drawer.isDrawerOpen(Gravity.START)) {
					HomeActivity.drawer.closeDrawers();
				} else {
					HomeActivity.drawer.openDrawer(Gravity.START);
				}
			}
		});

		// ��ʾ��½�û����û���
		tvUsername.setText(ub.getUsername());
		if (ub.getAge()==null) {
			uAge.setText("");
		}else {
			uAge.setText(ub.getAge() + "��");
		}
		uAdress.setText(ub.getAddress());
		uSex.setText(ub.getSex());
		// �����û�ͷ��
		imUserIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

				intent.putExtra("return-data", true);
				intent.setType("image/*");
				// ����
				intent.putExtra("crop", "circleCrop");
				// ���ñ���
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				// ���ô�С
				intent.putExtra("outputX", 50);
				intent.putExtra("outputY", 50);
				startActivityForResult(intent, 1);
			}
		});
	}
	//�����ѡ������Ժ�Ĳ���
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			//����浽����
			if (data != null && data.getExtras() != null) {
				final Bitmap bit = (Bitmap) data.getExtras().get("data");
				File file = new File(getActivity().getCacheDir(), "usericon.png");
				try {
					bit.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				DialogUtils.showDialog(getActivity(), null, null, true);
				final BmobFile bmfile = new BmobFile(file);
				//���·������������
				bmfile.uploadblock(getActivity(), new UploadFileListener() {

					@Override
					public void onSuccess() {
						imUserIcon.setImageBitmap(bit);
						ub.setUserIcon(bmfile);
						UserBean uu = new UserBean();
						uu.setUserIcon(bmfile);
						uu.update(getActivity(), ub.getObjectId(), new UpdateListener() {

							@Override
							public void onSuccess() {
								DialogUtils.dismiss();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								DialogUtils.dismiss();
								toastS("ͷ�����ʧ��");
							}
						});
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						DialogUtils.dismiss();
						toastS("ͷ���޸�ʧ��");
					}
				});
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		// ��ѯ�û������������
		BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
		query.addWhereEqualTo("u_id", ub.getObjectId());
		query.count(getActivity(), GatherBean.class, new CountListener() {

			@Override
			public void onFailure(int arg0, String arg1) {

			}

			@Override
			public void onSuccess(int count) {
				mySend.setText("" + count);
			}
		});

		// ��ѯ�û��ղػ������
		BmobQuery<UserBean> query2 = new BmobQuery<UserBean>();
		query2.getObject(getActivity(), ub.getObjectId(), new GetListener<UserBean>() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				
			}
			
			@Override
			public void onSuccess(UserBean arg0) {
				myLove.setText(""+arg0.getLoveGather_id().size());
			}
		});
		
		//��ѯ��ǰ�û���������
		BmobQuery<UserBean> query3 = new BmobQuery<UserBean>();
		query3.getObject(getActivity(), ub.getObjectId(), new GetListener<UserBean>() {
			
			@Override
			public void onFailure(int arg0, String arg1) {
				
			}
			
			@Override
			public void onSuccess(UserBean arg0) {
				myDingdan.setText(""+arg0.getAplayedONum().size());
			}
		});
		
		//��ѯ�Ż�ȯ������
		getYhqNum();
		
	}
	private void getYhqNum() {
		BmobQuery<YouHuiQBean> query4 = new BmobQuery<YouHuiQBean>();
		query4.addWhereEqualTo("uid", ub.getObjectId());
		query4.findObjects(getActivity(), new FindListener<YouHuiQBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(List<YouHuiQBean> arg0) {
				if (arg0==null) {
					myYouhui.setText(0+"");
				}else {
					myYouhui.setText(arg0.size()+"");
				}
				
			}
		});
	}

	//�Ը�������ѡ����м���
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_my_sclin://�ղ�
			updateView(R.id.fragment_my_sclin);
			BmobQuery<GatherBean> query = new BmobQuery<GatherBean>();
			query.addWhereContains("loveUserIds", ub.getObjectId());
			//��ѯ�ղصĻ
			getGatherBean(query);
			break;
		case R.id.fragment_my_sendlin://����
			updateView(R.id.fragment_my_sendlin);
			BmobQuery<GatherBean> query2 = new BmobQuery<GatherBean>();
			query2.addWhereEqualTo("u_id", ub.getObjectId());
			//��ѯ����Ļ
			getGatherBean(query2);
			//ɾ���
			break;
		case R.id.fragment_my_ddlin:
			updateView(R.id.fragment_my_ddlin);
			BmobQuery<OrderBean> query3 = new BmobQuery<OrderBean>();
			query3.addWhereEqualTo("userId", ub.getObjectId());
			//��ѯ����
			getOrderBean(query3);
			lView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
				}
			});
			break;
		case R.id.fragment_my_yhlin://�Ż�
			updateView(R.id.fragment_my_yhlin);
			getYouhuiQ();
			break;
		}
	}
	


	//��ѯ��ǰ�û����Ż�ȯ
	private void getYouhuiQ() {
		
		BmobQuery<YouHuiQBean> query = new BmobQuery<YouHuiQBean>();
		query.addWhereEqualTo("uid", ub.getObjectId());
		DialogUtils.showDialog(getActivity(), null, "������...", true);
		query.findObjects(getActivity(), new FindListener<YouHuiQBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				logI(arg1+"--------");
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}

			@Override
			public void onSuccess(List<YouHuiQBean> arg0) {
				DialogUtils.dismiss();
				if (arg0==null||arg0.size()==0) {
					toastS("���ź�������û���Ż�ȯ");
					return;
				}
				
				YouHQAdapter adapter = new YouHQAdapter(arg0, getActivity());
				lView.setAdapter(adapter);
				lView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					}
				});
			}
		});
	}

	//���ߵ�ǰ�û�ID���Ҹ��û������ж���
	private void getOrderBean(BmobQuery<OrderBean> query3) {
		DialogUtils.showDialog(getActivity(), null, "������...", true);
		query3.findObjects(getActivity(), new FindListener<OrderBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				logI(arg0+"--"+arg1);
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}

			@Override
			public void onSuccess(List<OrderBean> datas) {
				DialogUtils.dismiss();
				if (datas==null||datas.size()==0) {
					toastS("����û�ж������Ͽ����ȥ�ɣ�");
					return;
				}
				//���ҳɹ��Ժ���������Դ
				orderadapter = new MyOrderAdapter(datas, getActivity());
				lView.setAdapter(orderadapter);
			}
		});
	}

	
	//����ѡ����ɫ
	private void updateView(int linid) {
		for (int i = 0; i < 4; i++) {
			if (ids[i]==linid) {
				linears[i].setBackgroundColor(Color.parseColor("#e2e2e2"));
			}else {
				linears[i].setBackgroundColor(Color.parseColor("#ffffff"));
			}
		}
	}

	
}
