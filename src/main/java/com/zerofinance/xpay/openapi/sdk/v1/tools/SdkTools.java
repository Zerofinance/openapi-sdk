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
package com.zerofinance.xpay.openapi.sdk.v1.tools;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.zerofinance.xpay.openapi.sdk.v1.constant.ErrorCodeEnum;
import com.zerofinance.xpay.openapi.sdk.v1.dto.CallBackExecutor;
import com.zerofinance.xpay.openapi.sdk.v1.dto.RequestExecutor;
import com.zerofinance.xpay.openapi.sdk.v1.dto.RequestQuery;
import com.zerofinance.xpay.openapi.sdk.v1.dto.ResponseQuery;
import com.zerofinance.xpay.openapi.sdk.v1.entity.RSAKey;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * A sdk tools for openapi.
 *
 * <p>
 * <a href="SdkTools.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:40 AM
 * @version 1.0
 * @since 1.0
 */
public final class SdkTools {

    private SdkTools() {}


    /**
     * Generates a pair of key for RSA2.
     *
     * @return RSAKey, including both privateKey and publicKey.
     */
    public static RSAKey genRSAKey() {
        try {
            Map<String, Object> kyePair = RSAUtils.genKeyPair();
            return RSAKey.builder().privateKey(RSAUtils.getPrivateKey(kyePair))
                    .publicKey(RSAUtils.getPublicKey(kyePair)).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Executes the request and get data from Response.
     *
     * @param requestExecutor RequestExecutor
     * @param <T> Optional
     */

    public static <T> void execute(RequestExecutor requestExecutor) {
        execute(requestExecutor, null);
    }

    /**
     * Executes the request and get data from Response.
     *
     * @param requestExecutor RequestExecutor
     * @param clazz Converts to this class.
     * @return data
     * @param <T> Result
     */
    public static <T> Optional<T> execute(RequestExecutor requestExecutor, Class<T> clazz) {
        String requestUrl = requestExecutor.getRequestUrl();
        int connectionTimeout = requestExecutor.getConnectionTimeout();
        int readTimeout = requestExecutor.getReadTimeout();
        String publicKey = requestExecutor.getPublicKey();
        String aesKey = requestExecutor.getAesKey();
        String responseBody = HttpRequest.of(requestUrl, CharsetUtil.CHARSET_UTF_8)
                .setConnectionTimeout(connectionTimeout)
                .setReadTimeout(readTimeout)
                .method(Method.POST).execute().body();
        ResponseQuery openApiResult = JSONUtil.toBean(responseBody, ResponseQuery.class);
        int code = openApiResult.getCode();
        Assert.isTrue(code == ErrorCodeEnum.OK.getCode(),"An error is occurred from calling remote service："+ responseBody);
        // 验签
        boolean verifySignResult = verifyResponse(openApiResult, publicKey);
        Assert.isTrue(verifySignResult,"Verifying signature encountered an error!");

        ResponseQuery responseQuery = SdkTools.getResponseQuery(openApiResult, aesKey);
        String data = responseQuery.getData();
        Optional<T> result = Optional.empty();
        if (StrUtil.isNotBlank(data) && !ResponseQuery.VOID_DATA.equals(data) && clazz != null){
            result = Optional.of(JSONUtil.toBean(data, clazz));
        }
        return result;
    }

    /**
     * Calling back the outlet's url.
     *
     * @param callBackExecutor CallBackExecutor
     * @return Result
     */
    public static String callback(CallBackExecutor callBackExecutor) {
        String callbackUrl = callBackExecutor.getCallbackUrl();
        int connectionTimeout = callBackExecutor.getConnectionTimeout();
        int readTimeout = callBackExecutor.getReadTimeout();
        String privateKey = callBackExecutor.getPrivateKey();
        String sign = signUrl(callbackUrl, privateKey);
        return HttpRequest.of(callbackUrl, CharsetUtil.CHARSET_UTF_8)
                .setConnectionTimeout(connectionTimeout)
                .setReadTimeout(readTimeout)
                .header("sign", sign)
                .method(Method.POST).execute().body();
    }

    /**
     * Generates a signature of a certain context.
     *
     * @param context context
     * @param privateKey privateKey
     * @return sign string.
     */
    public static String signUrl(String context, String privateKey) {
        return SdkHelper.sign(context, privateKey);
    }

    /**
     * Generates a signature of a certain context.
     *
     * @param context context
     * @param sign sign string
     * @param publicKey publicKey
     * @return if verified?
     */
    public static boolean verifyUrl(String context, String sign, String publicKey) {
        try {
            return RSAUtils.verify(context.getBytes(), publicKey, sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a signature of a certain request.
     *
     * @param query The object of RequestQuery.
     * @param privateKey Private key.
     * @param aesKey Aes key.
     * @return a signed string.
     */
    public static String signRequest(RequestQuery query, String privateKey, String aesKey) {
        String bizContent = query.getBizContent();
        Assert.isTrue(StrUtil.isNotBlank(bizContent), "bizContent must not be emtpy!");
        String encryptBiZContent = AESEncryptUtils.encrypt(bizContent, aesKey);
        query.setBizContent(encryptBiZContent);
        String md5String = SdkHelper.md5Request(query);
        String sign = SdkHelper.sign(md5String, privateKey);
        query.setSign(sign);
        String queryString = SdkHelper.buildRequestUrl(query);
        return queryString;
    }

    /**
     * Verifies if the request is a legal url.
     *
     * @param queryString The string of request.
     * @param publicKey Public key.
     * @return verified?
     */
    public static boolean verifyRequest(String queryString, String publicKey) {
        try {
            RequestQuery query  = SdkHelper.buildRequestQuery(queryString);
            String md5String = SdkHelper.md5Request(query);
            String sign = query.getSign();
            boolean verified = RSAUtils.verify(md5String.getBytes(), publicKey, sign);
            return verified;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the object of "RequestQuery" from a string request.
     *
     * @param queryString The string of request.
     * @param aesKey Aes key.
     * @return RequestQuery.
     */
    public static RequestQuery getRequestQuery(String queryString, String aesKey) {
        UrlQuery parseQuery = new UrlQuery();
        parseQuery.parse(queryString, StandardCharsets.UTF_8);
        RequestQuery query = SdkHelper.buildRequestQuery(queryString);
        String aesEncrypt = query.getBizContent();
        String aesDecrypt = AESEncryptUtils.decrypt(aesEncrypt, aesKey);
        query.setBizContent(aesDecrypt);
        return query;
    }

    /**
     * Generates a signature of a certain response.
     *
     * @param query The object of ResponseQuery.
     * @param privateKey Private key.
     * @param aesKey Aes key.
     */
    public static void signResponse(ResponseQuery query, String privateKey, String aesKey) {
        String data = query.getData();
        Assert.isTrue(StrUtil.isNotBlank(data), "data must not be emtpy!");
        String encryptData = AESEncryptUtils.encrypt(data, aesKey);
        query.setData(encryptData);
        String md5String = SecureUtil.md5(encryptData);
        String sign = SdkHelper.sign(md5String, privateKey);
        query.setSign(sign);
    }

    /**
     * Verifies if the response is a legal url.
     *
     * @param query ResponseQuery.
     * @param publicKey Public key.
     * @return Verified?
     */
    public static boolean verifyResponse(ResponseQuery query, String publicKey) {
        try {
            String encryptData = query.getData();
            String sign = query.getSign();
            String md5String = SecureUtil.md5(encryptData);
            boolean verified = RSAUtils.verify(md5String.getBytes(), publicKey, sign);
            return verified;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the object of "ResponseQuery" from the object of "ResponseQuery".
     *
     * @param query ResponseQuery.
     * @param aesKey Aes key.
     * @return ResponseQuery.
     */
    public static ResponseQuery getResponseQuery(ResponseQuery query, String aesKey) {
        String aesEncrypt = query.getData();
        String aesDecrypt = AESEncryptUtils.decrypt(aesEncrypt, aesKey);
        query.setData(aesDecrypt);
        return query;
    }

    /**
     * A helper of SDK.
     */
    static final class SdkHelper {

        private SdkHelper() {}

        /**
         * Md5 RequestQuery.
         *
         * @param query RequestQuery.
         * @return a md5 string.
         */
        private static String md5Request(RequestQuery query) {
            String bizContent = query.getBizContent();
            Assert.isTrue(StrUtil.isNotBlank(bizContent), "bizContent must not be emtpy!");

            UrlQuery urlQuery = new UrlQuery();
            // Ascending according to key:
            urlQuery.add(RequestQuery.BIZ_CONTENT, query.getBizContent());
            urlQuery.add(RequestQuery.OUTLET_ID, query.getOutletId());
            urlQuery.add(RequestQuery.VERSION, query.getVersion());
//            urlQuery.add(RequestQuery.TIMESTAMP, query.getTimestamp());
            String queryString = urlQuery.build(StandardCharsets.UTF_8);

            return SecureUtil.md5(queryString);
        }

        /**
         * Signs the content.
         *
         * @param content the business content.
         * @param privateKey Private key.
         * @return A signed string.
         */
        private static String sign(String content, String privateKey) {
            try {
                return RSAUtils.sign(content.getBytes(), privateKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Builds the request url.
         *
         * @param query RequestQuery.
         * @return a request url.
         */
        private static String buildRequestUrl(RequestQuery query) {
            UrlQuery urlQuery = new UrlQuery();
            // 升序排列
            urlQuery.add(RequestQuery.BIZ_CONTENT, URLEncoder.encode(query.getBizContent(), StandardCharsets.UTF_8));
            urlQuery.add(RequestQuery.OUTLET_ID, URLEncoder.encode(query.getOutletId(), StandardCharsets.UTF_8));
//            urlQuery.add(RequestQuery.TIMESTAMP, URLEncoder.encode(query.getTimestamp(), StandardCharsets.UTF_8));
            urlQuery.add(RequestQuery.VERSION, URLEncoder.encode(query.getVersion(), StandardCharsets.UTF_8));
            urlQuery.add(RequestQuery.SIGN, URLEncoder.encode(query.getSign(), StandardCharsets.UTF_8));
            return urlQuery.build(StandardCharsets.UTF_8, true);
        }

        /**
         * Builds the object of "RequestQuery".
         *
         * @param queryString The quest string.
         * @return RequestQuery.
         */
        private static RequestQuery buildRequestQuery(String queryString) {
            UrlQuery parseQuery = new UrlQuery();
            parseQuery.parse(queryString, StandardCharsets.UTF_8);
            RequestQuery query  = RequestQuery.builder()
                                              .bizContent(URLDecoder.decode(parseQuery.get(RequestQuery.BIZ_CONTENT).toString(), StandardCharsets.UTF_8))
                                              .outletId(URLDecoder.decode(parseQuery.get(RequestQuery.OUTLET_ID).toString(), StandardCharsets.UTF_8))
//                    .timestamp(URLDecoder.decode(parseQuery.get(RequestQuery.TIMESTAMP).toString(), StandardCharsets.UTF_8))
                                              .version(URLDecoder.decode(parseQuery.get(RequestQuery.VERSION).toString(), StandardCharsets.UTF_8))
                                              .sign(URLDecoder.decode(parseQuery.get(RequestQuery.SIGN).toString(), StandardCharsets.UTF_8))
                                              .build();
            return query;
        }

        /*private static String buildFullResponseUrl(ResponseQuery query) {
            UrlQuery urlQuery = new UrlQuery();
            // 升序排列
            urlQuery.add(ResponseQuery.CODE, query.getCode());
            urlQuery.add(ResponseQuery.DATA, query.getData());
            urlQuery.add(ResponseQuery.MSG, query.getMsg());
            urlQuery.add(ResponseQuery.SIGN, query.getSign());
            return urlQuery.build(StandardCharsets.UTF_8);
        }*/

        /*private static ResponseQuery buildResponseQuery(String queryString) {
            UrlQuery parseQuery = new UrlQuery();
            parseQuery.parse(queryString, StandardCharsets.UTF_8);
            ResponseQuery query  = ResponseQuery.builder()
                    .code(Integer.parseInt(parseQuery.get(ResponseQuery.CODE).toString()))
                    .data(parseQuery.get(ResponseQuery.DATA).toString())
                    .msg(parseQuery.get(ResponseQuery.MSG).toString())
                    .sign(parseQuery.get(ResponseQuery.SIGN).toString())
                    .build();
            return query;
        }*/
    }
}
