package com.zerofinance.xpay.openapi.sdk.v1;


import com.zerofinance.xpay.openapi.sdk.v1.utils.RSAUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Map;

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
