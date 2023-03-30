package com.zerofinance.xpay.openapi.sdk.v1;

import com.zerofinance.xpay.openapi.sdk.v1.entity.RSAKey;
import com.zerofinance.xpay.openapi.sdk.v1.tools.SdkTools;
import org.junit.Test;

/**
 * <p>
 * <a href="RSAKeyGenerator.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 3/30/2023 2:32 PM
 */
public class RSAKeyGenerator {

    private RSAKey rsaKey;

    @Test
    public void genRSAKey() {
        rsaKey = SdkTools.genRSAKey();
        System.out.println("Private key: "+rsaKey.getPrivateKey());
        System.out.println("Public key: "+rsaKey.getPublicKey());
    }
}
