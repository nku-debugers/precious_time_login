package comv.example.zyrmj.precious_time01.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;


public class StringRandomUtils {

	private static char[] charsNumber = ("0123456789").toCharArray();
	private static char[] charsLetter = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
			.toCharArray();
	private static char[] charsRandom = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
			.toCharArray();
	private static Random random = new Random();


	/**
	 * TODO<参数为生成的字符串的长度，根据给定的char集合生成字符串数字>
	 * @param min
	 * @param max
	 * @return
	 * @throw
	 * @return int
	 */
	public static String randomNumber(int length) {

		char[] data = new char[length];

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(charsNumber.length);
			data[i] = charsNumber[index];
		}
		String s = new String(data);
		return s;
	}

	/**
	 * TODO<参数为生成的字符串的长度，根据给定的char集合生成字符串 字母>
	 * @param min
	 * @param max
	 * @return
	 * @throw
	 * @return int
	 */
	public static String randomLetter(int length) {

		char[] data = new char[length];

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(charsLetter.length);
			data[i] = charsLetter[index];
		}
		String s = new String(data);
		return s;
	}


	/**
	 * TODO<参数为生成的字符串的长度，根据给定的char集合生成字符串>
	 * @param min
	 * @param max
	 * @return
	 * @throw
	 * @return int
	 */
	public static String getStringRandom(int length) {

		char[] data = new char[length];

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(charsRandom.length);
			data[i] = charsRandom[index];
		}
		String s = new String(data);
		return s;
	}

	/**
	 * TODO<生成简体中文字符串>
	 * @param min
	 * @param max
	 * @return
	 * @throw
	 * @return int
	 */
	public static String getRandomJianHan(int len) {
		String ret = "";
		for (int i = 0; i < len; i++) {
			String str = null;
			int hightPos, lowPos; // 定义高低位
			Random random = new Random();
			hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
			lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
			byte[] b = new byte[2];
			b[0] = (new Integer(hightPos).byteValue());
			b[1] = (new Integer(lowPos).byteValue());
			try {
				str = new String(b, "GBk"); // 转成中文
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			ret += str;
		}
		return ret;
	}


	/**
	 * TODO<生成在[min,max]之间的随机整数，>
	 * @param min
	 * @param max
	 * @return
	 * @throw
	 * @return int
	 */
	public static int getRandomNumber(int min, int max) {

		Random random = new Random();

		return random.nextInt(max) % (max - min + 1) + min;

	}

}
