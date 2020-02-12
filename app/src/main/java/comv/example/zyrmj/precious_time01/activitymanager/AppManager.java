package comv.example.zyrmj.precious_time01.activitymanager;

import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;


public class AppManager {
	private static Stack<Activity> mActivityStack;
	private static AppManager mAppManager;

	static int sysVersion;// ϵͳ�汾��

	private AppManager() {
	}

	/**
	 * ��һʵ��
	 */
	public static AppManager getInstance() {
		if (mAppManager == null) {
			mAppManager = new AppManager();
		}
		return mAppManager;
	}

	/**
	 * ���Activity����ջ
	 */
	public void addActivity(Activity activity) {
		if (mActivityStack == null) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * ��ȡջ��Activity����ջ�����һ��ѹ��ģ�
	 */
	public Activity getTopActivity() {
		Activity activity = mActivityStack.lastElement();
		return activity;
	}

	/**
	 * ����ջ��Activity����ջ�����һ��ѹ��ģ�
	 */
	public void killTopActivity() {
		Activity activity = mActivityStack.lastElement();
		killActivity(activity);
	}

	/**
	 * ����ָ����Activity
	 */
	public void killActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);

			activity.finish();

			activity = null;
		}
	}

	/**
	 * ����ָ��������Activity
	 */
	public void killActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				killActivity(activity);
			}
		}
	}

	/**
	 * ��������Activity
	 */
	public void killAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * �˳�Ӧ�ó���
	 */
	public void AppExit(Context context) {
		try {
			killAllActivity();

			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);

			// �˳���̨�̣߳�
			sysVersion = android.os.Build.VERSION.SDK_INT;

			if (sysVersion < android.os.Build.VERSION_CODES.GINGERBREAD) {// 2.3
																			// ���µİ汾
				activityMgr.restartPackage(context.getPackageName());
			} else {
				// 2.2�����ǹ�ʱ��,����killBackgroundProcesses����
				activityMgr.killBackgroundProcesses(context.getPackageName());
			}

			System.exit(0);// ֻ�� �˵��˳� �������˳�

		} catch (Exception e) {
		}
	}
}
