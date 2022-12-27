package com.zerofinance.xpay.openapi.sdk.v1.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A Key for RSA.
 *
 * <p>
 * <a href="RSAKey.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:46 AM
 */
@Getter
@Builder
public class RSAKey {

    @NonNull
    private String privateKey;

    @NonNull
    private String publicKey;
}
