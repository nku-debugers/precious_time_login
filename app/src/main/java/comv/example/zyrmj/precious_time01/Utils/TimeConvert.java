package comv.example.zyrmj.precious_time01.Utils;

public class TimeConvert {

	/**
	 * TODO<秒数转换成分秒数字格式>
	 * @param seconds
	 * @return
	 * @throw
	 * @return String
	 */
	public static String secondsToMinute(int seconds) {

		// if(seconds!=0){
		// String m = (seconds % 3600) / 60<10 ? "0"+(seconds % 3600) / 60 :
		// (seconds % 3600) / 60+"";
		// String s = seconds % 60<10 ? "0"+seconds % 60:seconds % 60+"";
		// return m+ ":" + s ;
		// }else{
		// return "";
		// }
		String h = seconds / 3600 < 10 ? "0" + seconds / 3600 : seconds / 3600 + "" ;
		String m = (seconds % 3600) / 60 < 10 ? "0" + (seconds % 3600) / 60
				: (seconds % 3600) / 60 + "";
		String s = seconds % 60 < 10 ? "0" + seconds % 60 : seconds % 60 + "";
		return h + ":" + m + ":" + s;

	}

	/**
	 * TODO<秒数转换成分秒汉字格式>
	 * @param seconds
	 * @return
	 * @throw
	 * @return String
	 */
	public static String secondsToMinute1(int seconds) {

		String h = seconds / 3600 < 10 ? "0" + seconds / 3600 : seconds / 3600 + "" ;
		String m = (seconds % 3600) / 60 < 10 ? "0" + (seconds % 3600) / 60
				: (seconds % 3600) / 60 + "";
		String s = seconds % 60 < 10 ? "0" + seconds % 60 : seconds % 60 + "";
		return h + "小时" + m + "分钟" + s + "秒";

	}

}

