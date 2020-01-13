package comv.example.zyrmj.precious_time01.Utils;

public class StringNotsUtils {

	public static String setText(int times) {

		if (times == 0) {
			return "";
		} else if (times > 20) {
			return "You can uninstall me";
		} else if (times % 5 == 0) {
			String zheng = "";
			for (int i = 0; i < times / 5; i++) {
				zheng = "5" + zheng;
			}

			return "has used " + zheng + " times";
		} else {

			return "has used " + times + " times";
		}
	}
}
