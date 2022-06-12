package com.fingerchar.core.util;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DappCryptoUtil {

    public static final Logger logger = LoggerFactory.getLogger(DappCryptoUtil.class);

    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    /**
     * 验签 不带RSV，需要自己分割
     * @param signature
     * @param message
     * @param address
     * @return
     */
    public static boolean validate(String signature, String message, String address) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        SignatureData signatureData = new SignatureData(v, Arrays.copyOfRange(signatureBytes, 0, 32), Arrays.copyOfRange(signatureBytes, 32, 64));
        return validate(signatureData, message, address);
    }


    /**
     * 验签  带RSV
     * @param v
     * @param s
     * @param r
     * @param address
     * @param message
     * @return
     */
    public static boolean validate(Integer v, String s, String r, String address, String message) {
        SignatureData signatureData = new SignatureData(v.toString().getBytes(), Numeric.hexStringToByteArray(r), Numeric.hexStringToByteArray(s));
        return validate(signatureData, message, address);
    }

    public static boolean validate(SignatureData signatureData, String message, String address){
        List<String> addressList = recover(signatureData, message);
        if(addressList.isEmpty()){
            return false;
        }
        for(String _address: addressList){
            if(_address.equalsIgnoreCase(address)){
                return true;
            }
        }
        return false;
    }

    public static List<String> recover(SignatureData signatureData, String message){
        if(StringUtils.isEmpty(message)) {
            return new ArrayList<>();
        }
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());
        String address = null;
        List<String> addressList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature((byte) i, new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())), msgHash);
            if (publicKey != null) {
                address = "0x" + Keys.getAddress(publicKey);
                addressList.add(address);
            }
        }
        return addressList;
    }


    public static SignatureData sign(String message, String privateKey){
        ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(privateKey));
        String a = Hash.sha3(message);
        byte[] b = Numeric.hexStringToByteArray(a);
        return Sign.signPrefixedMessage(b, e);
    }

    /**
     * 获取签名盐  77位
     * @return
     */
    public static String getSalt() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<77; i++) {
            if(i == 0) {
                builder.append(random.nextInt(9) + 1);
            } else {
                builder.append(random.nextInt(10));
            }
        }
        return builder.toString();
    }

    /**
     * @param list
     * @return
     */
    private static List<Utf8String> convertToUtf8String(List<String> list) {
        List<Utf8String> result = new ArrayList<>();
        list.stream().forEach(data-> {
            result.add(new Utf8String(data));
        });
        return result;
    }

    private static List<Uint> convertToUint256(List<Long> list) {
        List<Uint> result = new ArrayList<>();
        list.stream().forEach(data-> {
            result.add(new Uint256(BigInteger.valueOf(data)));
        });
        return result;
    }
}
