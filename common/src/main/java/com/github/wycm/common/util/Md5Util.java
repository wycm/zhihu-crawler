package com.github.wycm.common.util;

import java.security.MessageDigest;

/**
 * md5转换工具
 */
public class Md5Util {
	public static String Convert2Md5(String password){
		MessageDigest md5 = null;
        try{  
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = password.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));
        }
//        System.out.println(hexValue);
        return hexValue.toString();  
	}
}
