package com.zerofinance.xpay.openapi.sdk.v1;

import cn.hutool.core.net.url.UrlQuery;
import com.zerofinance.xpay.openapi.sdk.v1.dto.ResponseQuery;
import com.zerofinance.xpay.openapi.sdk.v1.entity.RSAKey;
import com.zerofinance.xpay.openapi.sdk.v1.tools.SdkTools;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.nio.charset.StandardCharsets;

/**
 * A Testcase for SdkTools.
 *
 * <p>
 * <a href="SdkToolsTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 11:20 AM
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SdkToolsResponseTest {

    private static RSAKey rsaKey;

    private static String aesKey;

    private static String queryString;

    @BeforeClass
    public static void setUp() {
        rsaKey = SdkTools.genRSAKey();
        aesKey = "121212312312312312312312";
    }

    @Test
    public void t1SignResponse() {
        String privateKey = rsaKey.getPrivateKey();
        String data = "{a:1,b:2,c:3}";
        ResponseQuery query = ResponseQuery.buildSuccess(data);
        this.queryString = SdkTools.signResponse(query, privateKey, aesKey);
        System.out.println("queryString--->"+queryString);
        Assert.assertNotNull(queryString);
    }



    @Test
    public void t2VerifyResponse() {
        String publicKey = rsaKey.getPublicKey();
        UrlQuery parseQuery = new UrlQuery();
        parseQuery.parse(queryString, StandardCharsets.UTF_8);
        boolean verified = SdkTools.verifyResponse(queryString, publicKey);
        System.out.println("verified--->"+verified);
        Assert.assertTrue(verified);
    }

    @Test
    public void t3GetResponseQuery() {
        ResponseQuery query = SdkTools.getResponseQuery(queryString, aesKey);
        System.out.println("query--->"+query);
        Assert.assertNotNull(query);
    }
}
