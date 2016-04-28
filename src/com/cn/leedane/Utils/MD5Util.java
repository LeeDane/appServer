package com.cn.leedane.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5加密
 * @author LeeDane
 * 2015年7月17日 下午6:39:07
 * Version 1.0
 */
public class MD5Util {

	private static MessageDigest md;
	/**
	 * 输出转化后的字符串
	 * @param origin  //原始密码
	 * @return
	 */
	public static String compute(String origin) { 
		 try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		 
        char[] charArray = origin.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] mdBytes = md.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < mdBytes.length; i++) {
            int val = ((int) mdBytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    
}
