
package comv.example.zyrmj.precious_time01.activities;

import comv.example.zyrmj.precious_time01.activitymanager.ActivityStackControlUtil;
import comv.example.zyrmj.precious_time01.activitymanager.AppManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View.OnClickListener;


public abstract class BaseActivity extends Activity implements OnClickListener {

	public static final String TAG = BaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			this.finish();

		}

		return true;
		// return super.onKeyDown(keyCode, event);
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// �رռ����е�activity
		ActivityStackControlUtil.remove(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	/**
	 * ��ʼ���������� �������մ��ݵ�intent������ʵ���� �������󡣡���
	 */
	protected abstract void initData();

	/**
	 * ��ʼ���ؼ�
	 */
	protected abstract void initView();

	/**
	 * �����¼�����
	 */
	protected abstract void setLinstener();

	/**
	 * ������
	 * 
	 */
	protected abstract void fillData();

	/**
	 * ͨ����������Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * ͨ����������Activity�����Һ���Bundle����
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);

	}

	/**
	 * ͨ��Action����Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * ͨ��Action����Activity�����Һ���Bundle����
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);

	}

}
