package com.thenorthw.tc.common.utils;

import com.thenorthw.blog.common.utils.string.ByteAndStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by theNorthW on 09/04/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public class ActivateHelper {
    private static Logger logger = LoggerFactory.getLogger(ActivateHelper.class);

    public static String getActivateCode(String loginname){
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(writeRandomChar());
            byte[] bs = aesEncryptToBytes(loginname, "theNorthW3376");
            sb.append(ByteAndStringConverter.transBytesToString(bs));

            return sb.toString();
        }catch (Exception e){
            logger.error(e.toString());
        }
        return null;
    }


    public static String getLoginname(String code){
        try {
            String s = eraseRandomChar(code);
            return aesDecryptByBytes(ByteAndStringConverter.transHexStringToBytes(s), "theNorthW3376");
        }catch (Exception e){
            logger.error(e.toString());
        }
        return null;
    }

    public static String writeRandomChar(){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        //生成首部随机字符
        StringBuilder sb = new StringBuilder("");
        Random random = new Random();
        int num = Math.abs(random.nextInt())%10+3;

        int k = num;
        while(k>0){
            sb.append(hexDigits[Math.abs(random.nextInt())%16]);
            k--;
        }
        sb.insert(2,hexDigits[num]);
        return sb.toString();
    }

    public static String eraseRandomChar(String s){
        char c = s.charAt(2);
        int length ;
        if(Character.isDigit(c)){
            length = Integer.parseInt(s.charAt(2)+"");
        }else{
            length = 10+c-'A';
        }

        return s.substring(length+1);
    }

    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(encryptKey.getBytes());
        kgen.init(128, secureRandom);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(decryptKey.getBytes());
        kgen.init(128, secureRandom);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

}
