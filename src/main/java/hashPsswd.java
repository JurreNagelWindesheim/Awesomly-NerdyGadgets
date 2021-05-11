import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashPsswd {

    public static String makeSHA256Psswd (String password) {
        String securePassword = get_SHA_256_SecurePassword(password);
        System.out.println("Password hashed! Goodbye!");

        return securePassword;
    }

    private static String get_SHA_256_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}

