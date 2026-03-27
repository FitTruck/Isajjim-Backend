package kr.co.isajjim.global.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@Converter
public class DatabaseEncryptionConverter implements AttributeConverter<String, String> {

    @Value("${app.security.encryption-key}")
    private String secretKey;

    private static final String ALGORITHM     = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTE = 12;
    private static final int TAG_LENGTH_BIT = 128;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (!StringUtils.hasText(attribute)) {
            return null;
        }
        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            SECURE_RANDOM.nextBytes(iv);

            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            SecretKeySpec keySpec = new SecretKeySpec(decodedKey, ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            byte[] cipherTextWithTag = cipher.doFinal(
                    attribute.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[IV_LENGTH_BYTE + cipherTextWithTag.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH_BYTE);
            System.arraycopy(cipherTextWithTag, 0, combined, IV_LENGTH_BYTE, cipherTextWithTag.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasText(dbData)) {
            return null;
        }
        try {
            byte[] combined = Base64.getDecoder().decode(dbData);
            byte[] iv = new byte[IV_LENGTH_BYTE];
            byte[] cipherTextWithTag = new byte[combined.length - IV_LENGTH_BYTE];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH_BYTE);
            System.arraycopy(combined, IV_LENGTH_BYTE, cipherTextWithTag, 0, cipherTextWithTag.length);

            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            SecretKeySpec keySpec = new SecretKeySpec(decodedKey, ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            byte[] decrypted = cipher.doFinal(cipherTextWithTag);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}