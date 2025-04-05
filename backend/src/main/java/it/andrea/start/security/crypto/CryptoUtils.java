package it.andrea.start.security.crypto;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import jakarta.validation.constraints.NotNull;

public class CryptoUtils {
    
    private static final String ALGORITHM_AES = "AES";
    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
    private static final int IV_SIZE = 16;

    private static final CryptoUtils INSTANCE = new CryptoUtils();

    private SecretKey secretKey;
    private byte[] initializationVector;

    public static CryptoUtils getInstance() {
	return INSTANCE;
    }

    private CryptoUtils() {
    }

    public void generateAES256Key(String pathKey) throws NoSuchAlgorithmException, IOException {
	secretKey = getOrGenerateKey(pathKey + "key0", AesKeySizes.AES_256);
	initializationVector = getOrGenerateIV(pathKey + "key1");
    }

    public SecretKey generateAESKey(@NotNull AesKeySizes aesKeySizes) throws NoSuchAlgorithmException {
	KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
	keyGenerator.init(aesKeySizes.getKeySize());
	return keyGenerator.generateKey();
    }

    public String encryptAES256String(@NotNull String input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
	byte[] encryptedBytes = encryptBytes(input.getBytes(StandardCharsets.UTF_8));
	return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public byte[] encryptBytesAES256String(@NotNull String input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
	return encryptBytes(input.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] encryptAES256Bytes(@NotNull byte[] bytes, @NotNull byte[] iv, @NotNull SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
	return encryptBytesWithKeyAndIV(bytes, key, iv);
    }

    public String decryptAES256String(@NotNull String input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
	byte[] decodedBytes = Base64.getDecoder().decode(input);
	byte[] decryptedBytes = decryptBytes(decodedBytes);
	return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public byte[] decryptAES256Bytes(@NotNull byte[] bytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
	byte[] decodedBytes = Base64.getDecoder().decode(bytes);
	return decryptBytes(decodedBytes);
    }

    private SecretKey getOrGenerateKey(String pathFile, AesKeySizes keySize) throws NoSuchAlgorithmException, IOException {
	SecretKey key = loadKey(pathFile);
	if (key == null) {
	    KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
	    keyGenerator.init(keySize.getKeySize());
	    key = keyGenerator.generateKey();
	    saveKey(key, pathFile);
	}
	return key;
    }

    private byte[] getOrGenerateIV(String pathFile) throws IOException {
	byte[] iv = loadBytes(pathFile);
	if (iv == null) {
	    iv = new byte[IV_SIZE];
	    new SecureRandom().nextBytes(iv);
	    saveBytes(iv, pathFile);
	}
	return iv;
    }

    private byte[] encryptBytes(@NotNull byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	return encryptBytesWithKeyAndIV(input, secretKey, initializationVector);
    }

    private byte[] encryptBytesWithKeyAndIV(@NotNull byte[] input, @NotNull SecretKey key, @NotNull byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
	cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
	return cipher.doFinal(input);
    }

    private byte[] decryptBytes(@NotNull byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
	cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(initializationVector));
	return cipher.doFinal(input);
    }

    private SecretKey loadKey(String pathFile) throws IOException {
	byte[] keyBytes = loadBytes(pathFile);
	return keyBytes == null ? null : new SecretKeySpec(keyBytes, ALGORITHM_AES);
    }

    private void saveKey(SecretKey key, String pathFile) throws IOException {
	saveBytes(key.getEncoded(), pathFile);
    }

    private void saveBytes(byte[] data, String pathFile) throws IOException {
	Files.write(Paths.get(pathFile), data);
    }

    private byte[] loadBytes(String pathFile) throws IOException {
	File file = new File(pathFile);
	return file.exists() && !file.isDirectory() ? Files.readAllBytes(Paths.get(pathFile)) : null;
    }
}
