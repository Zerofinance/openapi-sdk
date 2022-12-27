package com.zerofinance.xpay.openapi.sdk.v1.dto;

import lombok.*;

/**
 * A Query For Request.
 *
 * <p>
 * <a href="RequestQuery.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 */
@Builder
@Getter
@ToString
public class RequestQuery {

    public final static String MERCHANT_ID = "merchantId";

    public final static String VERSION = "version";

    public final static String BIZ_CONTENT = "bizContent";

    public final static String TIMESTAMP = "timestamp";

    public final static String SIGN = "sign";

    @NonNull
    private String merchantId;

    @NonNull
    private String version;

    @NonNull
    private String timestamp;

    @NonNull
    @Setter
    private String bizContent;

    @Setter
    private String sign;
}
