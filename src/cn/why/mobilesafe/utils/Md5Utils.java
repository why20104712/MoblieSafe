package cn.why.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

	/**
	 * 加密密码
	 * @return
	 */
	public static String encryptPwd(String pwd){
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(pwd.getBytes());
			StringBuffer buffer = new StringBuffer();
			for (byte b : digest) {
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append("0");//补齐位数
				}
				buffer.append(str);
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
