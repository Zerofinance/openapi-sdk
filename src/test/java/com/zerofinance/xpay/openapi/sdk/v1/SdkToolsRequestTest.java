package com.zerofinance.xpay.openapi.sdk.v1;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.url.UrlQuery;
import com.zerofinance.xpay.openapi.sdk.v1.dto.RequestQuery;
import com.zerofinance.xpay.openapi.sdk.v1.entity.RSAKey;
import com.zerofinance.xpay.openapi.sdk.v1.tools.SdkTools;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.nio.charset.StandardCharsets;
import java.util.Date;

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
public class SdkToolsRequestTest {

    private static RSAKey rsaKey;

    private static String aesKey;

    private static String queryString;

    @BeforeClass
    public static void setUp() {
        rsaKey = SdkTools.genRSAKey();
        aesKey = "121212312312312312312312";
    }

    @Test
    public void t1SignRequest() {
        String privateKey = rsaKey.getPrivateKey();
        String bizContent = "{a:1,b:2,c:3}";
        RequestQuery query = RequestQuery.builder()
                .merchantId("111222")
                .timestamp(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
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
}
