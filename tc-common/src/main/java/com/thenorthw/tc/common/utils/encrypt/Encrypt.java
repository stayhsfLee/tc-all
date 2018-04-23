package com.thenorthw.tc.common.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LeeAutumn on 10/10/16.
 * blog: thenorthw.com
 */
public class Encrypt {

    public static String encrypt(String prime, EncryptMode encryptMode){
        switch (encryptMode){
            case MD5:
                return md5(prime);
            case DES:
                return des(prime);
            case SHA1:
                return sha1Encrypt(prime);
            default:
                return "You need to realize the encryption function by yourself.";
        }
    }


    public static String decode(String secret, EncryptMode encryptMode){
        switch (encryptMode){
            case MD5:
                return null;
            default:
                return null;
        }
    }
    /**
     * MD5加密
     * @param prime 未被MD5加密前的String
     * @return  返回被MD5加密过后的32位字符串
     */
    protected static String md5(String prime){
        return md5(prime.getBytes());
    }

    public static String md5(byte[] bytes){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        String encryption="";
        try {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            md5.update(bytes);
            byte[] md=md5.digest();
            char[] encryption_char=new char[md.length*2];
            int k=0;
            for (int i = 0; i < md.length; i++) {
                byte byte0 = md[i];
                encryption_char[k++] = hexDigits[byte0 >>> 4 & 0xf];
                encryption_char[k++] = hexDigits[byte0 & 0xf];
            }
            encryption=new String(encryption_char);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryption;
    }

    protected static String des(String prime){
        return null;
    }

    protected static String sha1Encrypt(String prime){
        if (null == prime || 0 == prime.length()){
            return null;
        }
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(prime.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
