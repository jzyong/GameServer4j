package org.mmo.engine.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public final static Random random = new Random();
	final static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String nullToString(int value) {
		return value == 0 ? "" : String.valueOf(value);
	}

	public static String nullToString(String value) {
		return (value == null ? "" : value);
	}

	public static String nullToString(String value, String encode) {
		if (value == null) return "";
		if (encode == null || encode.length() == 0) return value;
		try {
			return new String(value.getBytes("ISO-8859-1"), encode);
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	public static String emptyToString(String value, String defaultString) {
		if (value == null || value.trim().length() == 0) return defaultString;

		return value;
	}

	public static String left(String value, int len) {
		String rc = nullToString(value);
		if (len < 0) len = 0;

		return rc.length() > len ? rc.substring(0, len) : rc;
	}

	public static String right(String value, int len) {
		String rc = nullToString(value);
		if (len < 0) len = 0;

		return rc.length() > len ? rc.substring(rc.length() - len) : rc;
	}

	public static String paddingStringLeft(String s, int width, char ch) {
		if (s == null) s = "";
		for (int i = s.length(); i < width; i++)
			s = ch + s;

		return s;
	}

	public static String paddingStringRight(String s, int width, char ch) {
		if (s == null) s = "";
		for (int i = s.length(); i < width; i++)
			s = s + ch;

		return s;
	}

	public static short stringToShort(String strNumber) {
		return stringToShort(strNumber, (short) 0);
	}

	public static short stringToShort(String strNumber, short defaultValue) {
		short result = defaultValue;
		try {
			if (strNumber != null && strNumber.length() > 0) result = Short.parseShort(strNumber);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public static int stringToInt(String strNumber) {
		return stringToInt(strNumber, 0);
	}

	public static int stringToInt(String strNumber, int defaultValue) {
		int result = defaultValue;
		try {
			if (strNumber != null && strNumber.length() > 0) result = Integer.parseInt(strNumber);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public static long stringToLong(String strNumber) {
		return stringToLong(strNumber, 0);
	}

	public static long stringToLong(String strNumber, long defaultValue) {
		long result = defaultValue;
		try {
			if (strNumber != null && strNumber.length() > 0) result = Long.parseLong(strNumber);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public static double stringToDouble(String strNumber) {
		return stringToDouble(strNumber, 0.00);
	}

	public static double stringToDouble(String strNumber, double defaultValue) {
		double result = defaultValue;
		try {
			if (strNumber != null && strNumber.length() > 0) result = Double.parseDouble(strNumber);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	public static java.util.Date stringToDate(String strDate) {
		return stringToDate(strDate, new java.util.Date(0));
	}

	public static java.util.Date stringToDate(String strDate, java.util.Date defaultValue) {
		java.util.Date date = defaultValue;
		try {
			if (strDate != null && strDate.length() > 0) date = java.sql.Date.valueOf(strDate);
		} catch (IllegalArgumentException e) {
		}
		return date;
	}

	public static java.sql.Date stringToSqlDate(String strDate) {
		return stringToSqlDate(strDate, null);
	}

	public static java.sql.Date stringToSqlDate(String strDate, java.sql.Date defaultValue) {
		java.sql.Date date = defaultValue;
		try {
			if (strDate != null && strDate.length() > 0) date = java.sql.Date.valueOf(strDate);
		} catch (IllegalArgumentException e) {
		}
		return date;
	}

	public static Time stringToSqlTime(String strTime) {
		return stringToSqlTime(strTime, null);
	}

	public static Time stringToSqlTime(String strTime, Time defaultValue) {
		Time time = defaultValue;
		try {
			if (strTime != null && strTime.length() > 0) {
				if (strTime.indexOf(':') == strTime.lastIndexOf(':')) strTime += ":00";
				time = Time.valueOf(strTime);
			}
		} catch (IllegalArgumentException e) {
		}
		return time;
	}

	public static Timestamp stringToTimestamp(String strTime) {
		return stringToTimestamp(strTime, new Timestamp(0));
	}

	public static Timestamp stringToTimestamp(String strTime, Timestamp defaultValue) {
		Timestamp time = defaultValue;
		try {
			if (strTime != null && strTime.length() > 0) time = Timestamp.valueOf(strTime);
		} catch (IllegalArgumentException e) {
		}
		return time;
	}

	public static boolean isNumber(String s) {
		if (s == null || s.length() == 0) return false;
		byte b[] = s.getBytes();
		for (int i = 0; i < b.length; i++) {
			if (b[i] < '0' || b[i] > '9') return false;
		}

		return true;
	}

	public static String shortString(String value, int len) {
		String rc = nullToString(value);
		if (rc.length() <= len && len > 0) return rc;

		return value.substring(0, len) + "...";
	}

	public static String shortAnsiString(String value, int len) {
		String rc = nullToString(value);
		if (rc.getBytes().length > len) {
			int trimLen = (rc.getBytes().length - len + 1) / 2;
			int strLen = rc.length();
			while (rc.substring(0, strLen - trimLen).getBytes().length > len) {
				trimLen++;
			}
			rc = value.substring(0, strLen - trimLen) + "...";
		}

		return rc;
	}

	public static String formatCurrency(double value) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(value);
	}

	public static String setSentencePeriod(String value) {
		if (value == null || value.trim().length() == 0) {
			return "";
		}

		if (!value.matches(".*[,.，。]$")) {
			value += "。";
		}

		return value;
	}

	public static boolean isEmpty(String value) {
		if (value == null || value.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String getRandomString(int length) {
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			sb.append(hexChar[random.nextInt(16)]);
		}
		return sb.toString();
	}

	public static String getRandomNumber(int length) {
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	public static String getSafeString(String value) {
		return value == null ? null : value.replaceAll("'", "\\\\'");
	}

	public static byte[] hex2Byte(String str) {
		byte[] rc = null;
		if (str == null || str.length() == 0) return rc;
		int len = str.length() / 2;
		rc = new byte[len];
		for (int i = 0; i < len; i++) {
			try {
				rc[i] = (byte) (Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16) & 0XFF);
			} catch (NumberFormatException e) {
			}
		}

		return rc;
	}
	public static String byte2Hex(byte b[]) {
		if (b == null) return "";
		StringBuffer tmp = new StringBuffer();
		int len = b.length;
		for (int i = 0; i < len; i++) {
			tmp.append(hexChar[(b[i] >>> 4) & 0x0F]);
			tmp.append(hexChar[b[i] & 0x0F]);
		}

		return tmp.toString();
	}

	public static float stringSimilarity(String str1, String str2) {
		// 计算两个字符串的长度。
		int len1 = str1.length();
		int len2 = str2.length();
		// 建立上面说的数组，比字符长度大一个空间
		int[][] dif = new int[len1 + 1][len2 + 1];
		// 赋初值，步骤B。
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		// 计算两个字符是否一样，计算左上的值
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 取三个值中最小的
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1, dif[i - 1][j] + 1);
			}
		}

		return 1 - (float) dif[len1][len2] / Math.max(len1, len2);
	}

	// 得到最小值
	private static int min(int... is) {
		int min = Integer.MAX_VALUE;
		for (int i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}

	/*
	public static String formatCommaString(String value) {
		if (value == null || value.length() == 0) {
			return value;
		}
		String tmp[] = value.trim().split(ApiUtil.PHONE_DELIMITER);

		return ArrayUtil.arrayToString(tmp, ",");
	}
	 */
	public static String camel2Dash(String value) {
		if (value == null || value.length() == 0) {
			return value;
		}
		StringBuffer sb = new StringBuffer();
		for (char c : value.toCharArray()) {
			if (c >= 'A' && c <= 'Z') {
				sb.append('_');
				sb.append((char) (c + 0x20));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static String dash2Camel(String value) {
		if (value == null || value.length() == 0) {
			return value;
		}
		StringBuffer sb = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '_' && i < chars.length && chars[i + 1] >= 'a' && chars[i + 1] <= 'z') {
				sb.append((char) (chars[++i] - 0x20));
			} else {
				sb.append(chars[i]);
			}
		}

		return sb.toString();
	}
	
	public static String safeString(String value) {
		return value == null ? "" : value.replaceAll("'", "\\\\'");
	}
	
	public static String safeToString(String value) {
		return value == null ? "" : value.replaceAll("\\\\'","'");
	}

	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}
	
	public static int getActivityCount(String value,int level) {
		int count = 0;
		String[] strArr = value.split(";");
		if(strArr.length>0) {
			for (String string : strArr) {
				String [] strArr2 = string.split(",");
				int start = Integer.parseInt(strArr2[0]);
				int end = Integer.parseInt(strArr2[1]);
				int number = Integer.parseInt(strArr2[2]);
				if(level>end) {
					continue;
				}else {
					count = number;
					break;
				}
			}
		}
		
		return count;
	}


	/**
	 * 根据Map转换http请求参数
	 * @param map
	 * @return
	 */
	public static String genParam(Map<String, ?> map) {
		String param = "";
		for (Map.Entry<String, ?> set : map.entrySet()) {
			String key = set.getKey();
			Object value = set.getValue();
			if (value != null) {
				try {
					param += "&" + key + "=" + URLEncoder.encode(value.toString(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					param += "&" + key + "=" + value;
				}
			}
		}
		if(param.length()>1){
			return param.substring(1);
		} else{
			return param;
		}
	}
}
