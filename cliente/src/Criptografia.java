
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Criptografia {
        
    private static SecureRandom random = new SecureRandom();
    
    public static byte[] criptografa(byte[] msn, byte[] key) {
        try {
            Cipher c;
            c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            c.init(Cipher.ENCRYPT_MODE, k);
            byte[] encryptedData = c.doFinal(msn);
            return encryptedData;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println("Erro na criptografia");
        }
        return "DEUMERDA".getBytes();
        
    }
    
    public static byte[]  descriptografa(byte[] msn, byte[] key) {
        try {
            Cipher c;
            c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            c.init(Cipher.DECRYPT_MODE, k);
            byte[] encryptedData = c.doFinal(msn);
            return encryptedData;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println("rro na criptografia");
        }
        return null;
        
        
    }
    
    public static String nextSessionId() {
        return new BigInteger(130, random).toString(16);
    }
}
