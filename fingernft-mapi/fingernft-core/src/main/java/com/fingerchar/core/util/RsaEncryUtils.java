package com.fingerchar.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class RsaEncryUtils {

    private static final String SIGN_TYPE = "RSA";

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final String SIGN_KEY = "SHA256withRSA";

    /** */
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    public static String getMessage(final Map<String, Object> data) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            if ("sign".equals(keyArray[i])) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (null != data.get(keyArray[i]) && String.valueOf(data.get(keyArray[i])).length() > 0) {
                if(sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(keyArray[i]).append("=").append(data.get(keyArray[i]));
            }
        }
        return sb.toString();
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGN_KEY);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    public static String sign(String content, String privateKey, Charset charset) throws Exception {
        if (content == null) {
            throw new Exception("content不能为空");
        }

        if (privateKey == null) {
            throw new Exception("私钥不能为空");
        }

        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }

        byte[] encodedKey = Base64.decodeBase64(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE);
        PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));

        Signature signature = Signature.getInstance(SIGN_KEY);
        signature.initSign(priKey);
        signature.update(content.getBytes(charset));
        byte[] signed = signature.sign();
        return Base64.encodeBase64String(signed);
    }

    public static String sign(Map<String, Object> map, String privateKey, Charset charset) throws Exception {
        String content = getMessage(map);
        return sign(content, privateKey, charset);
    }

}

