package security;

import org.bson.types.ObjectId;
import play.Logger;
import play.mvc.Http;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;


public class Security {
    public static String generatePBKDF2EncryptedPassword(String password, String salt) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 2048, 192);
            SecretKey secretKey = keyFactory.generateSecret(spec);

            byte[] secretKeyBytes = secretKey.getEncoded();

            return toHexString(secretKeyBytes);

        } catch (Exception e) {
            Logger.error(e.getMessage());

            return "";
        }
    }

    public static String generateRandomPasswordSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] saltBytes = new byte[128];

        secureRandom.nextBytes(saltBytes);

        return toHexString(saltBytes);
    }

    public static String generateCapabilityIdFromRequest(Http.Request request) {
        String route = request.method() + " " + request.path();

        if (route.equals("/")) {
            return route;
        }

        String[] parts = route.split("/");

        StringBuilder builder = new StringBuilder();

        if (parts.length > 0) {
            builder.append(parts[0]);

            for (int i = 1; i < parts.length; ++i) {
                if (i == 1 && parts[0].equals("features")) {
                    builder.append("/:class");
                    continue;
                }

                if (ObjectId.isValid(parts[i])) {
                    builder.append("/:id");
                } else {
                    builder.append(String.format("/%s", parts[i]));
                }
            }
        }

        return builder.toString();
    }

    public static String generateTokenKey(int keyBytesLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[keyBytesLength];

        secureRandom.nextBytes(keyBytes);

        return toHexString(keyBytes);
    }

    public static String md5Hash(String clear) {
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] actionIdBytes = clear.getBytes();

            byte[] actionIdHashBytes = digester.digest(actionIdBytes);

            return toHexString(actionIdHashBytes);

        } catch (NoSuchAlgorithmException e) {
            Logger.debug(e.getMessage());

            return "";
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(Integer.toHexString(b & 0xff));
        }

        return builder.toString();
    }
}
