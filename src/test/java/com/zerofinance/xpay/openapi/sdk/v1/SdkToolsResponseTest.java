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

import com.zerofinance.xpay.openapi.sdk.v1.dto.ResponseQuery;
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
 * <a href="SdkToolsResponseTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdkToolsResponseTest {

    private static RSAKey rsaKey;

    private static String aesKey;

    private static ResponseQuery query;

    @BeforeClass
    public static void setUp() {
        rsaKey = SdkTools.genRSAKey();
        aesKey = "121212312312312312312312";
    }

    @Test
    public void t1SignResponse() {
        String privateKey = rsaKey.getPrivateKey();
        String data = "{a:1,b:2,c:3}";
        query = ResponseQuery.buildSuccess(data);
        SdkTools.signResponse(query, privateKey, aesKey);
        System.out.println("query--->"+query);
        Assert.assertNotNull(query);
    }



    @Test
    public void t2VerifyResponse() {
        String publicKey = rsaKey.getPublicKey();
        boolean verified = SdkTools.verifyResponse(query, publicKey);
        System.out.println("verified--->"+verified);
        Assert.assertTrue(verified);
    }

    @Test
    public void t3GetResponseQuery() {
        ResponseQuery query1 = SdkTools.getResponseQuery(query, aesKey);
        System.out.println("query--->"+query1);
        Assert.assertNotNull(query);
    }
}
