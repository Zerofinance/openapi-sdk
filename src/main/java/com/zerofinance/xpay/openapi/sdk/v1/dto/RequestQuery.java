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

import lombok.*;

/**
 * A Query For Request.
 *
 * <p>
 * <a href="RequestQuery.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
@Builder
@Getter
@ToString
public class RequestQuery {

    /**
     * The constant of "outletId".
     */
    public final static String OUTLET_ID = "outletId";

    /**
     * The constant of "version".
     */
    public final static String VERSION = "version";

    /**
     * The constant of "bizContent".
     */
    public final static String BIZ_CONTENT = "bizContent";

//    public final static String TIMESTAMP = "timestamp";


    /**
     * The constant of "sign".
     */
    public final static String SIGN = "sign";

    /**
     * outletId.
     */
    @NonNull
    private String outletId;

    /**
     * Version.
     */
    @NonNull
    private String version;

//    @NonNull
//    private String timestamp;

    /**
     * Business content.
     */
    @NonNull
    @Setter
    private String bizContent;

    /**
     * Signature.
     */
    @Setter
    private String sign;
}
