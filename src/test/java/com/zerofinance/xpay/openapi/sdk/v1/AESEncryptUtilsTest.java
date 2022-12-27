package com.zerofinance.xpay.openapi.sdk.v1;

import com.zerofinance.xpay.openapi.sdk.v1.utils.AESEncryptUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * <p>
 * <a href="AESEncryptUtilsTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:33 AM
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AESEncryptUtilsTest {

    private final String key = "121212312312312312312312";


    @Test
    public void encrypt() throws Exception {
        String str = "aaaaaa";
        String encrytpStr = AESEncryptUtils.encrypt(str, key);
        System.out.println("encrytpStr: "+encrytpStr);
        Assert.assertNotNull(encrytpStr);
    }

    @Test
    public void decode() throws Exception {
        String str = "aaaaaa";
        String decryptStr = AESEncryptUtils.decrypt(AESEncryptUtils.encrypt(str, key), key);
        System.out.println("decryptStr: " + decryptStr);
        Assert.assertEquals(str, decryptStr);
    }
}
