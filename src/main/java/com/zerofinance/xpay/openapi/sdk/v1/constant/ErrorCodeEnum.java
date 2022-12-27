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
package com.zerofinance.xpay.openapi.sdk.v1.constant;

import lombok.Getter;

/**
 * 公共错误枚举对象
 * 
 * <p>
 * <a href="ErrorCodeEnum.java"><i>View Source</i></a>
 * 
 * @author zhaoxunyong@qq.com
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum ErrorCodeEnum {
    
    /**
     * 成功
     */
    OK(200, "successful"),
    
    /**
     * 业务处理异常
     */
    BUSINESS_ERROR(900, "Business Exception"),
    
    /**
     * 参数传值异常
     */
    PARAMETER_ERROR(901, "Parameter Exception"),
    
    /**
     * 数据存储异常
     */
    DATA_ACCESS_ERROR(902, "Data Access Exception"),
    
    /**
     * 未知异常
     */
    UNKNOWN_ERROR(903, "Unknown Exception"),
    
    /**
     * 找不到数据
     */
    NOT_FOUND_ERROR(904, "NotFound Exception"),
    
    /**
     * 簽名校驗異常
     */
    SIGN_ERROR(905, "Signature Exception"),
    
    /**
     * session已失效
     */
    SESSION_ERROR(906, "Session Expired Exception"),
    
    /**
     * 无效的版本
     */
    VERSION_INVALID_ERROR(907, "Version InValid Exception"),
    
    /**
     * Remote Exception
     */
    REMOTE_ERROR(908, "Remote Invoke Exception"),
    
    /**
     * 无权限走流程
     */
    NO_ACCESS_PROCESS(909, "No Access Exception");
    
    private final int code;
    
    private final String msg;
    
    /**
     * @param code error code
     * @param msg error msg
     */
    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
