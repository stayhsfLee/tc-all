package com.thenorthw.tc.common.utils.string;

/**
 * Created by theNorthW on 09/04/2017.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public class ByteAndStringConverter {
    public static String transBytesToString(byte[] encryption_char){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        int k=0;
        char[] cs = new char[encryption_char.length*2];
        for (int i = 0; i < encryption_char.length; i++) {
            byte byte0 = encryption_char[i];
            cs[k++] = hexDigits[byte0 >>> 4 & 0xf];
            cs[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(cs);
    }

    public static byte[] transHexStringToBytes(String s){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] cs = s.toCharArray();
        byte[] bs = new byte[s.length()/2];
        for (int i = 0; i < s.length(); i=i+2) {
            char c1 = cs[i];
            char c2 = cs[i+1];
            bs[i/2]=twoHexCharToByte(c1,c2);
        }
        return bs;
    }

    public static byte twoHexCharToByte(char c1,char c2){
        if(c1 == '8' && c2 == '0'){
            return (byte)-128;
        }
        int a=0,c=0;
        boolean isNe=false;
        if(Character.isDigit(c1)){
            if(a>=8){
                //如果是负数,就要从补码的角度来思考
                isNe = true;
            }
        }else{
            isNe = true;
        }

        boolean needBorrow = false;
        if(isNe){
            if(Character.isDigit(c2)){
                //需要向高位借1
                if(Integer.parseInt(c2+"") == 0){
                    c = 0;
                    needBorrow = true;
                }else{
                    c = 15 - Integer.parseInt(c2+"") + 1;
                }
            }else{
                c = 7 - (2 + c2 - 'A' - 1);
            }
            if(Character.isDigit(c1)){
                a = 7 - Integer.parseInt(c1+"") + (needBorrow?1:0);
            }else{
                a = 7 - (2 + c1 - 'A' - (needBorrow?1:0));
            }
        }else{
            a = Integer.parseInt(c1+"");
            if(Character.isDigit(c2)) {
                c = Integer.parseInt(c2 + "");
            }else{
                c = 10 + c2 -'A';
            }
        }

        int r = ((a*16+c)*(isNe?-1:1));

        return (byte)(r);
    }
}
