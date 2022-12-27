package com.zerofinance.xpay.openapi.sdk.v1.dto;

import lombok.Data;

/**
 * A Query For Request.
 *
 * <p>
 * <a href="RequestQuery.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 */
@Data
public class RequestQuery {

    public final static String MERCHANT_ID = "merchantId";
    public final static String VERSION = "version";
    public final static String BIZ_CONTENT = "bizContent";
    public final static String SIGN = "sign";

    private String merchantId;

    private String version;

    private String bizContent;

    private String sign;
}
