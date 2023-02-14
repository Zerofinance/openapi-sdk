/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
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
