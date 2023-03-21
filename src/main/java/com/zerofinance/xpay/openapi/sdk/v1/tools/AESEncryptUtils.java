package com.zerofinance.xpay.openapi.sdk.v1.tools;

import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AesEncryptUtils
 *
 * <p>
 * <a href="AesEncryptUtils.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/14/2022 3:44 PM
 */
class AESEncryptUtils {

    /**
     * 参数分别代表 算法名称/加密模式/数据填充方式
     */
    private static final String ALGORITHMS = "AES/ECB/PKCS5Padding";

    /**
     * 初始化向量(根据需求调整向量的值, 也可以将向量添加到入参变量中)
     */
    private static final byte[] SIV = new byte[16];

    /**
     * 加密
     *
     * @param content    加密的字符串
     * @param encryptKey key值
     * @return 加密后的内容
     * @throws Exception 异常
     */
    public static String encrypt(String content, String encryptKey){
        try {
            SecretKey key = generateMySQLAESKey(encryptKey,"ASCII");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = content.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return Base64.encode(ciphertextBytes);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 解密
     *
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return 解密后的内容
     * @throws Exception 异常
     */
    public static String decrypt(String encryptStr, String decryptKey) {
        try {
            SecretKey key = generateMySQLAESKey(decryptKey,"ASCII");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = Base64.decode(encryptStr);
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(ciphertextBytes, "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for(byte b : key.getBytes(encoding))
                finalKey[i++%16] ^= b;
            return new SecretKeySpec(finalKey, "AES");
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
