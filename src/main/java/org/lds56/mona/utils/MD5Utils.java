package org.lds56.mona.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: MD5 encoder, ref: https://gist.github.com/hekjje/376cf2572d9e3f6925800d889edaff86
 */
public class MD5Utils {

    /**
     * MD5 encode string.
     *
     * @param input the string to encode
     * @return the encoded string as a encoded MD5
     */
    public static String encode(String input) {
        try {
            byte[] bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            return convertByteArrayToHexString(thedigest);
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
