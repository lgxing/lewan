package cn.xdl.lewan;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.bmob.v3.BmobUser;
import cn.xdl.lewan.base.BaseActivity;
import cn.xdl.lewan.base.BaseInterface;
import cn.xdl.lewan.bean.UserBean;

public class InitActivity extends BaseActivity implements BaseInterface {
	private ImageView imageView;
	private Animation anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();

	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_init);
		imageView = (ImageView) findViewById(R.id.init_img);
	}

	@Override
	public void initDatas() {
		anim = AnimationUtils.loadAnimation(this, R.anim.init_alpha);

	}

	@Override
	public void initViewOper() {
		imageView.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				//MyApplication.putData("key", "≤‚ ‘ª∫¥Êø’º‰");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				UserBean userBean = BmobUser.getCurrentUser(getAct(), UserBean.class);
				Intent intent = null;
				if (userBean == null) {
					intent = new Intent(InitActivity.this, LoginActivity.class);
				}else {
					intent = new Intent(InitActivity.this, HomeActivity.class);
				}
				startActivity(intent);
				InitActivity.this.finish();
			}
		});
	}
	
}
