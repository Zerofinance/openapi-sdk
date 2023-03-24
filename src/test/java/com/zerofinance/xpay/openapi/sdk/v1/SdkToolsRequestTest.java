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

import cn.hutool.core.net.url.UrlQuery;
import com.zerofinance.xpay.openapi.sdk.v1.dto.RequestQuery;
import com.zerofinance.xpay.openapi.sdk.v1.entity.RSAKey;
import com.zerofinance.xpay.openapi.sdk.v1.tools.SdkTools;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * A Testcase for SdkTools.
 *
 * <p>
 * <a href="SdkToolsRequestTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdkToolsRequestTest {

    private static RSAKey rsaKey;

    private static String aesKey;

    private static String queryString;

    @BeforeClass
    public static void setUp() {
        rsaKey = SdkTools.genRSAKey();
        aesKey = "121212312312312312312312";
        System.out.println("aesKey--->"+aesKey);
    }

    @Test
    public void t1SignRequest() {
        String privateKey = rsaKey.getPrivateKey();
        System.out.println("privateKey--->"+privateKey);
        String bizContent = "{a:1,b:2,c:3}";
        RequestQuery query = RequestQuery.builder()
                                         .outletId("111222")
//                .timestamp(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
                                         .version("1.0.0")
                                         .bizContent(bizContent)
                                         .build();
        this.queryString = SdkTools.signRequest(query, privateKey, aesKey);
        System.out.println("queryString--->"+queryString);
        Assert.assertNotNull(queryString);
    }



    @Test
    public void t2VerifyRequest() {
        String publicKey = rsaKey.getPublicKey();
        System.out.println("publicKey--->"+publicKey);
        UrlQuery parseQuery = new UrlQuery();
        parseQuery.parse(queryString, StandardCharsets.UTF_8);
        boolean verified = SdkTools.verifyRequest(queryString, publicKey);
        System.out.println("verified--->"+verified);
        Assert.assertTrue(verified);
    }

    @Test
    public void t3GetRequestQuery() {
        RequestQuery query = SdkTools.getRequestQuery(queryString, aesKey);
        System.out.println("query--->"+query);
        Assert.assertNotNull(query);
    }

    @Test
    public void test4UrlEncode() {
        String bizContent = "ZzW1iG6apnMSMyn2KXXMOA%25253D%25253D";
        String encodeStr = URLDecoder.decode(bizContent, StandardCharsets.UTF_8);
        System.out.println("encodeStr--->"+encodeStr);
        Assert.assertNotNull(encodeStr);
    }
}
