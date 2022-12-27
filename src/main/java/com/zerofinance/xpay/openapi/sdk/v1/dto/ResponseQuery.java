/*
 * 描述： <描述>
 * 修改人： Dave.zhao
 * 修改时间： May 25, 2017
 * 项目： app-api
 */
package com.zerofinance.xpay.openapi.sdk.v1.dto;

import com.zerofinance.xpay.openapi.sdk.v1.constant.ErrorCodeEnum;
import lombok.*;

/**
 * ReturnResponse
 * 
 * @author Dave.zhao
 * @version [版本号, May 25, 2017]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Builder
@Getter
@ToString
public final class ResponseQuery {

    public final static String CODE = "code";

    public final static String MSG = "msg";

    public final static String SIGN = "sign";

    public final static String DATA = "data";

    @NonNull
    private int code;

    @NonNull
    private String msg;

    @NonNull
    @Setter
    private String data;

    @Setter
    private String sign;
    
    public static ResponseQuery buildSuccessVoid() {
        return buildSuccess("{}");
    }
    
    public static ResponseQuery buildSuccess(String data) {
        ResponseQuery responseQuery = ResponseQuery.builder()
                .code(ErrorCodeEnum.OK.getCode())
                .msg(ErrorCodeEnum.OK.getMsg())
                .data(data)
                .build();
        return responseQuery;
    }
    
    public static ResponseQuery buildFailed(int code, String msg, String data) {
        ResponseQuery responseQuery = ResponseQuery.builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
        return responseQuery;
    }

    public static ResponseQuery buildFailed(int code, String msg) {
        return buildFailed(code, msg, "{}");
    }
}
