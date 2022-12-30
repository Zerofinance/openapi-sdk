/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.zerofinance.xpay.openapi.sdk.v1.dto;

import com.zerofinance.xpay.openapi.sdk.v1.constant.ErrorCodeEnum;
import lombok.*;

/**
 * ReturnResponse
 *
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class ResponseQuery {


    /**
     * The constant of "code".
     */
    public final static String CODE = "code";

    /**
     * The constant of "msg".
     */
    public final static String MSG = "msg";

    /**
     * The constant of "sign".
     */
    public final static String SIGN = "sign";

    /**
     * The constant of "data".
     */
    public final static String DATA = "data";

    /**
     * Response code.
     */
    @NonNull
    private int code;

    /**
     * Response message.
     */
    @NonNull
    private String msg;

    /**
     * Response data.
     */
    @NonNull
    @Setter
    private String data;

    /**
     * Signature.
     */
    @Setter
    private String sign;

    /**
     * Builds a successful void object of ResponseQuery.
     *
     * @return ResponseQuery
     */
    public static ResponseQuery buildSuccessVoid() {
        return buildSuccess("{}");
    }

    /**
     * Builds an successful object of ResponseQuery.
     *
     * @param data response data.
     * @return ResponseQuery
     */
    public static ResponseQuery buildSuccess(String data) {
        ResponseQuery responseQuery = ResponseQuery.builder()
                .code(ErrorCodeEnum.OK.getCode())
                .msg(ErrorCodeEnum.OK.getMsg())
                .data(data)
                .build();
        return responseQuery;
    }

    /**
     * Builds a failed object of ResponseQuery.
     *
     * @param code response code.
     * @param msg response message.
     * @param data a returned business data.
     * @return ResponseQuery
     */
    public static ResponseQuery buildFailed(int code, String msg, String data) {
        ResponseQuery responseQuery = ResponseQuery.builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
        return responseQuery;
    }

    /**
     * Builds a failed object of ResponseQuery.
     *
     * @param code response code.
     * @param msg response message.
     * @return ResponseQuery
     */
    public static ResponseQuery buildFailed(int code, String msg) {
        return buildFailed(code, msg, "{}");
    }
}
