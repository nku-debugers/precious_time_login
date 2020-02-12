package comv.example.zyrmj.precious_time01.activitymanager;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

public class ActivityStackControlUtil extends Activity {

	/* ÿ��activity��add����ӽ����ϣ�Ȼ��رվ��Ƴ����� �� */
	private static List<Activity> activityList = new ArrayList<Activity>();

	public static void remove(Activity activity) {

		activityList.remove(activity);

	}

	public static void add(Activity activity) {

		activityList.add(activity);

	}

	public static void finishProgram() {

		for (Activity activity : activityList) {

			activity.finish();

		}

		// ɱ����ǰ�������
		// android.os.Process.killProcess(android.os.Process.myPid());

	}

}
