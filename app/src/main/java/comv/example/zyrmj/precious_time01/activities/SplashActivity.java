package comv.example.zyrmj.precious_time01.activities;

import comv.example.zyrmj.precious_time01.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class SplashActivity extends BaseActivity {

	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);

		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		view.startAnimation(animation);
		new Handler().postDelayed(new LoadMainActivityTask(), 2000);

	}

	private class LoadMainActivityTask implements Runnable {

		public void run() {
			Intent intent = new Intent(SplashActivity.this, TimeActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			startActivity(intent);
			finish();

		}

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// tv_register = (TextView) this.findViewById(R.id.tv_register);

	}

	@Override
	protected void setLinstener() {
		// tv_reget_pwd.setOnClickListener(this);

	}

	@Override
	protected void fillData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// case R.id.tv_reget_pwd:
		// openActivity(MainTab2.class,null);
		// break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		view.clearAnimation();
		super.onDestroy();
	}
}
