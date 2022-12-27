package com.zerofinance.xpay.openapi.sdk.v1.tools;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.zerofinance.xpay.openapi.sdk.v1.dto.RequestQuery;
import com.zerofinance.xpay.openapi.sdk.v1.dto.ResponseQuery;
import com.zerofinance.xpay.openapi.sdk.v1.entity.RSAKey;
import com.zerofinance.xpay.openapi.sdk.v1.utils.AESEncryptUtils;
import com.zerofinance.xpay.openapi.sdk.v1.utils.RSAUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A tools for openapi.
 *
 * <p>
 * <a href="SdkTools.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/27/2022 10:45 AM
 */
public final class SdkTools {

    private SdkTools() {}


    public static RSAKey genRSAKey() {
        try {
            Map<String, Object> kyePair = RSAUtils.genKeyPair();
            return RSAKey.builder().privateKey(RSAUtils.getPrivateKey(kyePair))
                    .publicKey(RSAUtils.getPublicKey(kyePair)).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String signRequest(RequestQuery query, String privateKey, String aesKey) {
        String bizContent = query.getBizContent();
        Assert.isTrue(StrUtil.isNotBlank(bizContent), "bizContent must not be emtpy!");
        String encryptBiZContent = AESEncryptUtils.encrypt(bizContent, aesKey);
        query.setBizContent(encryptBiZContent);
        String md5String = SdkHelper.md5Request(query);
        String sign = SdkHelper.sign(md5String, privateKey);
        query.setSign(sign);
        String queryString = SdkHelper.buildFullRequestUrl(query);
        return queryString;
    }

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

    public static RequestQuery getRequestQuery(String queryString, String aesKey) {
        UrlQuery parseQuery = new UrlQuery();
        parseQuery.parse(queryString, StandardCharsets.UTF_8);
        RequestQuery query = SdkHelper.buildRequestQuery(queryString);
        String aesEncrypt = query.getBizContent();
        String aesDecrypt = AESEncryptUtils.decrypt(aesEncrypt, aesKey);
        query.setBizContent(aesDecrypt);
        return query;
    }

    public static String signResponse(ResponseQuery query, String privateKey, String aesKey) {
        String data = query.getData();
        Assert.isTrue(StrUtil.isNotBlank(data), "data must not be emtpy!");
        String encryptData = AESEncryptUtils.encrypt(data, aesKey);
        query.setData(encryptData);
        String md5String = SdkHelper.md5Response(query);
        String sign = SdkHelper.sign(md5String, privateKey);
        query.setSign(sign);
        String queryString = SdkHelper.buildFullResponseUrl(query);
        return queryString;
    }

    public static boolean verifyResponse(String queryString, String publicKey) {
        try {
//            queryString = queryString.replaceAll("sign\\=.+?&", "");
            ResponseQuery query  = SdkHelper.buildResponseQuery(queryString);
            String md5String = SdkHelper.md5Response(query);
            String sign = query.getSign();
            boolean verified = RSAUtils.verify(md5String.getBytes(), publicKey, sign);
            return verified;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResponseQuery getResponseQuery(String queryString, String aesKey) {
        UrlQuery parseQuery = new UrlQuery();
        parseQuery.parse(queryString, StandardCharsets.UTF_8);
        ResponseQuery query = SdkHelper.buildResponseQuery(queryString);
        String aesEncrypt = query.getData();
        String aesDecrypt = AESEncryptUtils.decrypt(aesEncrypt, aesKey);
        query.setData(aesDecrypt);
        return query;
    }

    static final class SdkHelper {

        private SdkHelper() {}

        private static String md5Request(RequestQuery query) {
            String bizContent = query.getBizContent();
            Assert.isTrue(StrUtil.isNotBlank(bizContent), "bizContent must not be emtpy!");
            String queryString = buildNeedSignRequestUrl(query);
            return SecureUtil.md5(queryString);
        }

        private static String md5Response(ResponseQuery query) {
            String data = query.getData();
            Assert.isTrue(StrUtil.isNotBlank(data), "data must not be emtpy!");
            String queryString = buildNeedSignResponseUrl(query);
            return SecureUtil.md5(queryString);
        }

        private static String sign(String content, String privateKey) {
            try {
                return RSAUtils.sign(content.getBytes(), privateKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static String buildNeedSignRequestUrl(RequestQuery query) {
            UrlQuery urlQuery = new UrlQuery();
            // 升序排列
            urlQuery.add(RequestQuery.BIZ_CONTENT, query.getBizContent());
            urlQuery.add(RequestQuery.MERCHANT_ID, query.getMerchantId());
            urlQuery.add(RequestQuery.VERSION, query.getVersion());
            return urlQuery.build(StandardCharsets.UTF_8);
        }

        private static String buildFullRequestUrl(RequestQuery query) {
            UrlQuery urlQuery = new UrlQuery();
            // 升序排列
            urlQuery.add(RequestQuery.BIZ_CONTENT, query.getBizContent());
            urlQuery.add(RequestQuery.MERCHANT_ID, query.getMerchantId());
            urlQuery.add(RequestQuery.SIGN, query.getSign());
            urlQuery.add(RequestQuery.VERSION, query.getVersion());
            return urlQuery.build(StandardCharsets.UTF_8);
        }

        private static RequestQuery buildRequestQuery(String queryString) {
            UrlQuery parseQuery = new UrlQuery();
            parseQuery.parse(queryString, StandardCharsets.UTF_8);
            RequestQuery query  = new RequestQuery();
            query.setMerchantId(parseQuery.get(RequestQuery.MERCHANT_ID).toString());
            query.setVersion(parseQuery.get(RequestQuery.VERSION).toString());
            query.setBizContent(parseQuery.get(RequestQuery.BIZ_CONTENT).toString());
            String sign = parseQuery.get(RequestQuery.SIGN).toString();
            if(StrUtil.isNotBlank(sign)) {
                query.setSign(sign);
            }
            return query;
        }

        private static String buildNeedSignResponseUrl(ResponseQuery query) {
            UrlQuery urlQuery = new UrlQuery();
            // 升序排列
            urlQuery.add(ResponseQuery.CODE, query.getCode());
            urlQuery.add(ResponseQuery.DATA, query.getData());
            urlQuery.add(ResponseQuery.MSG, query.getMsg());
            return urlQuery.build(StandardCharsets.UTF_8);
        }

        private static String buildFullResponseUrl(ResponseQuery query) {
            UrlQuery urlQuery = new UrlQuery();
            // 升序排列
            urlQuery.add(ResponseQuery.CODE, query.getCode());
            urlQuery.add(ResponseQuery.DATA, query.getData());
            urlQuery.add(ResponseQuery.MSG, query.getMsg());
            urlQuery.add(ResponseQuery.SIGN, query.getSign());
            return urlQuery.build(StandardCharsets.UTF_8);
        }

        private static ResponseQuery buildResponseQuery(String queryString) {
            UrlQuery parseQuery = new UrlQuery();
            parseQuery.parse(queryString, StandardCharsets.UTF_8);
            ResponseQuery query  = new ResponseQuery();
            query.setCode(Integer.parseInt(parseQuery.get(ResponseQuery.CODE).toString()));
            query.setData(parseQuery.get(ResponseQuery.DATA).toString());
            query.setMsg(parseQuery.get(ResponseQuery.MSG).toString());
            String sign = parseQuery.get(ResponseQuery.SIGN).toString();
            if(StrUtil.isNotBlank(sign)) {
                query.setSign(sign);
            }
            return query;
        }
    }
}
