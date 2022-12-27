/*
 * 描述： <描述>
 * 修改人： Dave.zhao
 * 修改时间： May 25, 2017
 * 项目： app-api
 */
package com.zerofinance.xpay.openapi.sdk.v1.dto;

import com.zerofinance.xpay.openapi.sdk.v1.constant.ErrorCodeEnum;
import lombok.Data;

/**
 * ReturnResponse
 * 
 * @author Dave.zhao
 * @version [版本号, May 25, 2017]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public final class ResponseQuery {

    public final static String CODE = "code";
    public final static String MSG = "msg";
    public final static String SIGN = "sign";
    public final static String DATA = "data";

    private int code;

    private String msg;

    private String sign;

    private String data;
    
    public static ResponseQuery buildSuccessVoid() {
        return buildSuccess(null);
    }
    
    public static ResponseQuery buildSuccess(String data) {
        ResponseQuery responseQuery = new ResponseQuery();
        responseQuery.setCode(ErrorCodeEnum.OK.getCode());
        responseQuery.setMsg(ErrorCodeEnum.OK.getMsg());
        responseQuery.setData(data);
        return responseQuery;
    }
    
	public static ResponseQuery buildFailed(int code, String msg) {
        ResponseQuery responseQuery = new ResponseQuery();
        responseQuery.setCode(code);
        responseQuery.setData(null);
        responseQuery.setMsg(msg);
        return responseQuery;
    }
    
    public static ResponseQuery buildFailed(int code, String msg, String data) {
        ResponseQuery responseQuery = new ResponseQuery();
        responseQuery.setCode(code);
        responseQuery.setMsg(msg);
        responseQuery.setData(data);
        return responseQuery;
    }
}
