package com.e1858.utils;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;
import android.util.Log;

import com.e1858.common.Constant;

public class Encrypt {
	private static String	TAG		= "Encrypt";
	private static byte[]	keyiv	= { 1, 2, 3, 4, 5, 6, 7, 8 };
	private static byte[]	Key		= Base64.decode("yhxtA3F32drD4e0w2r3F378fd3E2HNHX", Base64.NO_WRAP);	//des秘钥

	public static String byte2HexString(byte abyte0[]) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = abyte0.length;
		int j = 0;
		do {
			if (j >= i)
				return stringbuffer.toString();
			stringbuffer.append(Integer.toHexString(0x100 | 0xff & abyte0[j]).substring(1));
			j++;
		} while (true);
	}

	public static String MD5(String s) {
		try {
			return byte2HexString(MessageDigest.getInstance("MD5").digest(s.getBytes(Constant.ENCODING)));
		}
		catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
			return null;
		}
	}

	public static String MD5(byte[] s) {
		try {
			return byte2HexString(MessageDigest.getInstance("MD5").digest(s));
		}
		catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
			return null;
		}
	}

	public static void setDes3Key(String str) {
		try {
			byte[] abyte1 = str.getBytes(Constant.ENCODING);
			byte[] abyte0 = new byte[24];
			System.arraycopy(abyte1, 0, abyte0, 0, Math.min(abyte1.length, abyte0.length));
			new DESedeKeySpec(abyte0);
		}
		catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
	}

	/**
	 * CBC解密
	 * 
	 * @param key
	 *            密钥
	 * @param keyiv
	 *            IV
	 * @param data
	 *            Base64编码的密文
	 * @return 明文
	 * @throws Exception
	 */
	public static String des3DecodeCBC(String data) throws Exception {

		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(Key);
		byte[] decode_Data = Base64.decode(data, Base64.NO_WRAP);

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);

		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(decode_Data);

		return new String(bOut, "UTF-8");

	}

	public static String des3EncodeCBC(String data) throws Exception {

		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(Key);

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(data.getBytes("UTF-8"));

		//return new BASE64Encoder().encode(bOut);
		return Base64.encodeToString(bOut, Base64.NO_WRAP);
	}
}
