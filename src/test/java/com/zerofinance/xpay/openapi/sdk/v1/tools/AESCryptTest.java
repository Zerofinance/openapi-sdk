package com.zerofinance.xpay.openapi.sdk.v1.tools;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * JasyptCrypt Test
 *
 * <p>
 * <a href="JasyptCryptTest.java"><i>View Source</i></a>
 *
 * @author Dave.zhao
 * Date: 12/14/2022 2:59 PM
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AESCryptTest {

    private String encryptKey = "abtexfnuux7jn1l5";

    /**
     * SELECT  TO_base64(AES_ENCRYPT('12312312sssssssssssssssssssss3@qq.com','abtexfnuux7jn1l5'))
     */
    @Test
    public void test1Encode() {
        String encode = AESEncryptUtils.encrypt("12312312sssssssssssssssssssss3@qq.com", encryptKey);
        System.out.println("jasyptCrypt encode--->"+encode);
    }

    /**
     * SELECT AES_DECRYPT(from_base64("rLmSJNqadjR48MyHDf70jDq0pGQqnuvzaahI53UmKodKZoqXVYlNiTpKa3bDD96z"),'abtexfnuux7jn1l5');
     */
    @Test
    public void test2Decode() {
//        String encode = AESEncryptUtils.encrypt("12312312sssssssssssssssssssss3@qq.com", encryptKey);
        String encode = "rLmSJNqadjR48MyHDf70jDq0pGQqnuvzaahI53UmKodKZoqXVYlNiTpKa3bDD96z";
        System.out.println("jasyptCrypt decode--->"+ AESEncryptUtils.decrypt(encode, encryptKey));
    }
}
