package com.zerofinance.xpay.openapi.sdk.v1.utils;

import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AesEncryptUtils
 *
 * <p>
 * <a href="AesEncryptUtils.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/14/2022 3:44 PM
 */
public class AESEncryptUtils {

    /**
     * 参数分别代表 算法名称/加密模式/数据填充方式
     */
    private static final String ALGORITHMS = "AES/CBC/PKCS5Padding";

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
            KeyGenerator.getInstance("AES").init(128);
            Cipher cipher = Cipher.getInstance(ALGORITHMS);
            // 加密向量
            IvParameterSpec iv = new IvParameterSpec(SIV);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"), iv);
            byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            // 采用base64算法进行转码,避免出现中文乱码
            return Base64.encode(b);
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
            KeyGenerator.getInstance("AES").init(128);
            Cipher cipher = Cipher.getInstance(ALGORITHMS);
            // 加密向量
            IvParameterSpec iv = new IvParameterSpec(SIV);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"), iv);
            // 采用base64算法进行转码,避免出现中文乱码
            byte[] encryptBytes = Base64.decode(encryptStr);
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
