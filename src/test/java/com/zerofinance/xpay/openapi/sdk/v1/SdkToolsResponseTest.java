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
 * <a href="SdkToolsTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 11:20 AM
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
        boolean verified = SdkTools.verifyResponse(query.getData(), query.getSign(), publicKey, aesKey);
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
