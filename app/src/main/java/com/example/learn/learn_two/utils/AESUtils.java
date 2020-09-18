package com.example.learn.learn_two.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private static final String CIPHER_MODE_CBC = "AES/CBC/PKCS5Padding";
    private static final String CIPHER_MODE_ECB = "AES/ECB/PKCS5Padding";
    public static final String KEY = "kW34lXfds4ELyJS2jSGrDUYrcG8lrsR1";
    public static String IV = "WipauqWxu6WgOOY1";
    private static final int SECRET_KEY_SPEC = 32;



    private static SecretKeySpec createKey(String key) {
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    public static byte[] encrypt(byte[] content, String password, String iv) {
        try {
            Cipher cipher = null;
            SecretKeySpec key = createKey(password);
            if (StringUtils.isEmpty(iv)) {
                cipher = Cipher.getInstance(CIPHER_MODE_ECB);
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher = Cipher.getInstance(CIPHER_MODE_CBC);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            }
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String content, String password, String iv) {
        try {
            byte[] result = encrypt(content.getBytes(), password, iv);
            return Base64Utils.encrypt(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String content) {
        try {
            return encrypt(content, KEY, IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] decrypt(byte[] content, String password, String iv) {
        try {
            Cipher cipher = null;
            SecretKeySpec key = createKey(password);
            if (StringUtils.isEmpty(iv)) {
                cipher = Cipher.getInstance(CIPHER_MODE_ECB);
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher = Cipher.getInstance(CIPHER_MODE_CBC);
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
            }
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String content, String password, String iv) {
        byte[] data = null;
        String result = null;
        try {
            data = Base64Utils.decrypt(content);
            data = decrypt(data, password, iv);
            if (data == null) {
                return null;
            }
            result = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String content) {
        return decrypt(content,KEY,IV);
    }

    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

}

