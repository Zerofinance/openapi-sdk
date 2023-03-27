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

import com.zerofinance.xpay.openapi.sdk.v1.entity.RSAKey;
import com.zerofinance.xpay.openapi.sdk.v1.tools.SdkTools;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * A Testcase for SdkTools.
 *
 * <p>
 * <a href="SdkToolsContextTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdkToolsContextTest {

    private static RSAKey rsaKey;

    private static String sign;

    @BeforeClass
    public static void setUp() {
        rsaKey = SdkTools.genRSAKey();
    }

    @Test
    public void t1Callback() {
        String privateKey = rsaKey.getPrivateKey();
        System.out.println("privateKey--->"+privateKey);
        String url = "https://xxxxxx/callback?outlet=123&status=1";
        this.sign = SdkTools.signUrl(url, privateKey);
        System.out.println("sign--->"+sign);
        // Mock: Puts the sign to the request header, and pass to the outlet's server-side.
        Assert.assertNotNull(sign);
    }



    @Test
    public void t2VerifyUrl() {
        String publicKey = rsaKey.getPublicKey();
        String url = "https://xxxxxx/callback?outlet=123&status=1";
        System.out.println("publicKey--->"+publicKey);
        // Mock: in the normal senior, a certain outlet may get the sign from request header.
        // String sign = request.getHeader("sign");
        boolean verified = SdkTools.verifyUrl(url, sign, publicKey);
        System.out.println("verified--->"+verified);
        Assert.assertTrue(verified);
    }
}
