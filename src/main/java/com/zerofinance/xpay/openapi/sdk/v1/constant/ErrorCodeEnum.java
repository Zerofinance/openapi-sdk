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
 * A global error code list for openapi-sdk.
 * 
 * <p>
 * <a href="ErrorCodeEnum.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum ErrorCodeEnum {
    
    /**
     * Successful
     */
    OK(200, "successful"),
    
    /**
     * Business Exception
     */
    BUSINESS_ERROR(900, "Business Exception"),
    
    /**
     * Parameter Exception
     */
    PARAMETER_ERROR(901, "Parameter Exception"),
    
    /**
     * Data Access Exception
     */
    DATA_ACCESS_ERROR(902, "Data Access Exception"),
    
    /**
     * Unknown Exception
     */
    UNKNOWN_ERROR(903, "Unknown Exception"),
    
    /**
     * NotFound Exception
     */
    NOT_FOUND_ERROR(904, "NotFound Exception"),
    
    /**
     * Signature Exception
     */
    SIGN_ERROR(905, "Signature Exception"),
    
    /**
     * Session Expired Exception
     */
    SESSION_ERROR(906, "Session Expired Exception"),
    
    /**
     * Version InValid Exception
     */
    VERSION_INVALID_ERROR(907, "Version InValid Exception"),
    
    /**
     * Remote Invoke Exception
     */
    REMOTE_ERROR(908, "Remote Invoke Exception"),
    
    /**
     * No Access Exception
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
