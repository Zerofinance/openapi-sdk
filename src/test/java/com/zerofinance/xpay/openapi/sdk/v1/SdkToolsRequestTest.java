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

import cn.hutool.core.net.URLDecoder;
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
        rsaKey = RSAKey.builder().privateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDF4daJrJojp2VqDjSfGQZ6nFkPGB/d6UTIa0/N///TrCTpr5oDwjB4Kb8ATuefYiFpcx1e7n9eZFH2pLoQA6cJYS8uZ2RkymaNmaSBrf8P9qWAQldzwLni1bz2AyWxRmQAshWGw5Kd7FzVlt/Dssw3oFwfvv2UC2VNbvqj0lxFfnZtu+ikIl8PxnIo2W4qhvYDtLyAq/oRKgnTh9t8+mkhoPKvBXFUA5V/xLwqwESCsyeqmH4+d0gN3V2bequAZE8sTLqGtVM++meO06aBiLIUImfkvD+pct97bxb3G/L8fM/xo09Zfz6HGxgafLjQWVvYZGCz6thbYVWsvLPt+webAgMBAAECggEALuscRipxpbCEIEBcA7kYSywVBbovnBs6Htcq5eokC1lOq0Xim6+IdIVZb0ZGLwlCaNFNjnu4IXZh5LwsDa4ABf5QBI2pRSikZkCeu8y4pF1T4nRYbe6tZHZPRnl6j2zkOzH+XqSyd9VaMZ6DUdBsEWhpT3GmiFqnW1PMA9nV3PfzZodsNuwJEkq098XqhH7xT6lghEoX9/8fyatmKRzyFgGDtsuOjvzZzsTtkT+SPNAMlOa8iJXVQKjQ+doFy9puXNTeEh6HVpGUxqoMeBd15SajBgxtLxzLi7dk1jKc0UA0EDhOrAHbInZsPI2No6R3Q33SYZFsekf6YishfWJE2QKBgQDp+JjMyn6bGijxU21YAbw8WDfoV0P7TBGBdqPukF8ea+l+np1zDuLDCQBjnSiLEggdg6lwEAz4jyTrHcXvM7RLZqEGuh6jCKVACYsrHUC+AIYqsbEr7knGTjBeccDiGco4sY0ASE9WtivqYcDsyfJYGcuUBqdrlv6jEt6XIYBMTQKBgQDYg2QBu2vI3Q4er6XPHR7IQajQLiyAOfc1PCsl97VzSapxaBR6Lv74VDlTuR3R+yZHvDja0w6hsg3wubkoFDVoGT4yX9RUO4YDOQ7E3dDf6wIIgIhhL6dAJbvJlDVfJHsMrMO05FWY5a7mPQdYv5vmXvGIwwNe3guq3zvokMd3hwKBgQDGOkYTM/jCJg7ML3ezmXzGz6NGaTECpvcp7b+ELrM4Dgt8qJwTUGhU/7phq1QfR36yqssSU7b5nCWWQdpiCDdXrrL9BRFR5dBEChHrzFP+5mjFoVjPNIsyxSn7ynq11U3cbKletOTrPVrFsF6I/6wgcnJljqGn6P0CYslN2Cdf/QKBgCLjCPo+eN+kvuGy68SzrIIYElt5FTKUJIEvcMRtZY9uD0i2vJXzfKIG8DXNXkC3dzOX4JFh8LmdNyHSy0Lp/rEHYD49srjf3ngmeXq/QyjRdUJpc5A1XX24lsmkV/U8q5cfP2mmYSSB3ey7aLOkLDqykbi3s5NVtAuUbNLMgVeBAoGBAMR3igEmw6UK0YXetPL4qizKIHWxJ9W/SvRVnL2p3uRnfirZiNKED3i7+HnbmRHYpJ58Ha7tbdwwEZ2WQHp5EXdl7kHEWXuAHvuZm9W7DQZnrZFOt+8iGXFy31g32gOEmQ58jI2ivthPySbjY6b891q+AORdjMLpUdyHSq9JF/Yc")
                       .publicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxeHWiayaI6dlag40nxkGepxZDxgf3elEyGtPzf//06wk6a+aA8IweCm/AE7nn2IhaXMdXu5/XmRR9qS6EAOnCWEvLmdkZMpmjZmkga3/D/algEJXc8C54tW89gMlsUZkALIVhsOSnexc1Zbfw7LMN6BcH779lAtlTW76o9JcRX52bbvopCJfD8ZyKNluKob2A7S8gKv6ESoJ04fbfPppIaDyrwVxVAOVf8S8KsBEgrMnqph+PndIDd1dm3qrgGRPLEy6hrVTPvpnjtOmgYiyFCJn5Lw/qXLfe28W9xvy/HzP8aNPWX8+hxsYGny40Flb2GRgs+rYW2FVrLyz7fsHmwIDAQAB").build();
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
