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
package com.zerofinance.xpay.openapi.sdk.v1;


import com.zerofinance.xpay.openapi.sdk.v1.utils.RSAUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Map;

/**
 * <p>
 * <a href="RSAUtilsTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RSAUtilsTest {

    private static String privateKey;

    private static String publicKey;

    /**
     * Set up
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        Map<String, Object> kyePair = RSAUtils.genKeyPair();
        privateKey = RSAUtils.getPrivateKey(kyePair);
        publicKey = RSAUtils.getPublicKey(kyePair);
        System.out.println("privateKey: " + privateKey);
        System.out.println("publicKey: " + publicKey);
    }

    @AfterClass
    public static void tearDown() {
    }

    @Test
    public void encrypt() throws Exception {
        String str = "aaaaaa";
        String encrytpStr = new String(RSAUtils.encrypt(str.getBytes(), privateKey));
        System.out.println("encrytpStr: "+encrytpStr);
        Assert.assertNotNull(encrytpStr);
    }

    @Test
    public void decode() throws Exception {
        String str = "aaaaaa";
        String decryptStr = new String(RSAUtils.decrypt(RSAUtils.encrypt(str.getBytes(), privateKey), publicKey));
        System.out.println("decryptStr: " + decryptStr);
        Assert.assertEquals(str, decryptStr);
    }

    @Test
    public void sign() throws Exception {
        String str = "aaaaaa";
        String sign = RSAUtils.sign(str.getBytes(), privateKey);
        System.out.println("sign: "+sign);
        Assert.assertNotNull(sign);
    }

    @Test
    public void verify() throws Exception {
        String str = "aaaaaa";
        String sign = RSAUtils.sign(str.getBytes(), privateKey);
        System.out.println("sign: "+sign);
        boolean verified = RSAUtils.verify(str.getBytes(), publicKey, sign);
        Assert.assertTrue(verified);
    }
}
