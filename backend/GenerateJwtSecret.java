import java.security.SecureRandom;
import java.util.Base64;

public class GenerateJwtSecret {
    public static void main(String[] args) {
        byte[] keyBytes = new byte[32]; // 256-bit key
        new SecureRandom().nextBytes(keyBytes);
        String secret = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("New JWT Secret Key: " + secret);
    }
}
