package com.fingerchar.api.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import com.fingerchar.api.vo.SignOrderInfo;
import com.fingerchar.api.vo.VSRSignInfo;

public class DappCryptoUtil {

	public static final Logger logger = LoggerFactory.getLogger(DappCryptoUtil.class);

	public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

	public static final int[] SALT_ARR = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private static String TRANSFER_KEY;

	private static String MINT_KEY;

	/**
	 * 验签 不带RSV，需要自己分割
	 * 
	 * @param signature
	 * @param message
	 * @param address
	 * @return
	 */
	public static boolean validate(String signature, String message, String address) {

		if (StringUtils.isEmpty(message)) {
			return false;
		}

		String prefix = PERSONAL_MESSAGE_PREFIX + message.length();

		byte[] msgHash = Hash.sha3((prefix + message).getBytes());

		byte[] signatureBytes = Numeric.hexStringToByteArray(signature);

		byte v = signatureBytes[64];
		if (v < 27) {
			v += 27;
		}
		SignatureData sd = new SignatureData(v, Arrays.copyOfRange(signatureBytes, 0, 32),
				Arrays.copyOfRange(signatureBytes, 32, 64));
		String addressRecovered = null;
		boolean match = false;
		for (int i = 0; i < 4; i++) {
			BigInteger publicKey = Sign.recoverFromSignature((byte) i,
					new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())), msgHash);
			if (publicKey != null) {
				addressRecovered = "0x" + Keys.getAddress(publicKey);
				if (addressRecovered.toLowerCase().equals(address.toLowerCase())) {
					match = true;
					break;
				}
			}
		}
		return match;
	}

	/**
	 * 验签 带RSV
	 * 
	 * @param v
	 * @param s
	 * @param r
	 * @param address
	 * @param message
	 * @return
	 */
	public static boolean validate(Integer v, String s, String r, String address, String message) {

		if (StringUtils.isEmpty(message)) {
			return false;
		}

		String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
		byte[] msgHash = Hash.sha3((prefix + message).getBytes());
		SignatureData sd = new SignatureData(v.toString().getBytes(), Numeric.hexStringToByteArray(r),
				Numeric.hexStringToByteArray(s));
		String addressRecovered = null;
		boolean match = false;
		for (int i = 0; i < 4; i++) {
			BigInteger publicKey = Sign.recoverFromSignature((byte) i,
					new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())), msgHash);
			if (publicKey != null) {
				addressRecovered = "0x" + Keys.getAddress(publicKey);
				if (addressRecovered.toLowerCase().equals(address.toLowerCase())) {
					match = true;
					break;
				}
			}
		}
		return match;
	}

	public static VSRSignInfo sign(String caddress, String tokenId) throws Exception {
		Type<BigInteger> tId = new Uint(new BigInteger(tokenId));
		String a = caddress + Numeric.cleanHexPrefix(TypeEncoder.encode(tId));
		a = Hash.sha3(a);
		byte[] b = Numeric.hexStringToByteArray(a);
		ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(MINT_KEY));
		SignatureData data = Sign.signPrefixedMessage(b, e);
		VSRSignInfo sign = new VSRSignInfo();
		sign.setV(new BigInteger(data.getV()).toString());
		sign.setS(Numeric.toHexString(data.getS()));
		sign.setR(Numeric.toHexString(data.getR()));
		logger.info(sign.getV());
		logger.info(sign.getS());
		logger.info(sign.getR());
		return sign;
	}

	/**
	 * order签名
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public static SignOrderInfo orderSign(SignOrderInfo order) {
		String salt = order.getSalt();
		if (StringUtils.isEmpty(order.getSalt()) || order.getSalt().equals("0")) {
			salt = getSalt();
		}
		String signStr = encode(order, salt);
		logger.info(signStr);
		signStr = Hash.sha3(signStr);
		logger.info(signStr);
		order.setSignature(signStr);
		order.setSalt(salt);
		return order;
	}

	/**
	 * order签名（带费用）
	 * 
	 * @param order
	 * @param fee
	 * @return
	 */
	public static SignOrderInfo orderSign(SignOrderInfo order, Integer fee) {
		String salt = order.getSalt();
		String signStr = encode(order, salt, fee);
		logger.info("message=>" + signStr);
		signStr = Hash.sha3(signStr);
		logger.info("第一次sha3=>" + signStr);
		signStr = Numeric.cleanHexPrefix(signStr);
		StringBuffer buffer1 = new StringBuffer(signStr);
		StringBuffer buffer2 = new StringBuffer(PERSONAL_MESSAGE_PREFIX);
		buffer2.append(buffer1.length()).append(buffer1);
		signStr = Numeric.toHexString(buffer2.toString().getBytes());
		logger.info("加头部=>" + signStr);
		signStr = Numeric.cleanHexPrefix(signStr);
		signStr = Hash.sha3(signStr);
		logger.info("第二次sha3=>" + signStr);

		ECKeyPair e = ECKeyPair.create(Numeric.hexStringToByteArray(TRANSFER_KEY));
		SignatureData data = Sign.signMessage(Numeric.hexStringToByteArray(signStr), e, false);
		logger.info("v=>" + new BigInteger(data.getV()).toString());
		logger.info("s=>" + Numeric.toHexString(data.getS()));
		logger.info("r=>" + Numeric.toHexString(data.getR()));
		order.setV(new BigInteger(data.getV()).toString());
		order.setS(Numeric.toHexString(data.getS()));
		order.setR(Numeric.toHexString(data.getR()));
		order.setSalt(salt);
		return order;
	}

	/**
	 * 仿abi.encode
	 * 
	 * @param order
	 * @param salt
	 * @return
	 */
	private static String encode(SignOrderInfo order, String salt) {
		return encode(order, salt, null);
	}

	/**
	 * 仿abi.encode
	 * 
	 * @param order
	 * @param salt
	 * @param fee
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String encode(SignOrderInfo order, String salt, Integer fee) {
		// owner(address)
		List<Type> typeList = new ArrayList<>();
		Type<String> addr = new Address(order.getOwner());
		typeList.add(addr);
		// salt(uint)
		Type<BigInteger> uint = new Uint256(new BigInteger(salt));
		typeList.add(uint);
		// seller token(address)
		addr = new Address(order.getSellerToken());
		typeList.add(addr);
		// seller tokenId(uint)
		uint = new Uint256(new BigInteger(order.getSellerTokenId()));
		typeList.add(uint);
		// seller assetType(uint8)
		Type<BigInteger> uint8 = new Uint8(new BigInteger(order.getSellerAssetType()));
		typeList.add(uint8);
		// buy token(address)
		addr = new Address(order.getBuyerToken());
		typeList.add(addr);
		// buy tokenId(uint)
		uint = new Uint256(new BigInteger(order.getBuyerTokenId()));
		typeList.add(uint);
		// buy assetType(uint8)
		uint8 = new Uint8(new BigInteger(order.getBuyerAssetType()));
		typeList.add(uint8);
		// selling(uint)
		uint = new Uint256(new BigDecimal(order.getSelling()).toBigInteger());
		typeList.add(uint);
		uint = new Uint256(new BigDecimal(order.getBuying()).toBigInteger());
		typeList.add(uint);
		// sellerFee(uint)
		uint = new Uint256(new BigInteger(order.getSellerFee()));
		typeList.add(uint);
		if (null != fee) {
			uint = new Uint256(new BigInteger(fee.toString()));
			typeList.add(uint);
		}
		DefaultFunctionEncoder encoder = new DefaultFunctionEncoder();
		return encoder.encodeParameters(typeList);
	}

	/**
	 * 获取签名盐 77位
	 * 
	 * @return
	 */
	public static String getSalt() {
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 77; i++) {
			if (i == 0) {
				builder.append(random.nextInt(9) + 1);
			} else {
				builder.append(random.nextInt(10));
			}
		}
		return builder.toString();
	}

	public static void setTransKey(String transKey) {
		TRANSFER_KEY = transKey;
	}

	public static void setMintKey(String mintKey) {
		MINT_KEY = mintKey;
	}
}
