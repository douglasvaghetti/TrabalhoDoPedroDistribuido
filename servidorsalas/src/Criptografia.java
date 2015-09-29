
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Douglas
 */
public class Criptografia {
        
    private static SecureRandom random = new SecureRandom();
    
    public static byte[] criptografa(byte[] msn, byte[] key) {
        /*
        while(msn.length() < 16 || msn.length()%16 != 0){
            msn += "*";
        }
        */
        System.out.println("Criptografando + "+Arrays.toString( msn));
        try {
            Cipher c;
            c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            c.init(Cipher.ENCRYPT_MODE, k);
            byte[] encryptedData = c.doFinal(msn);
            return encryptedData;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println("Deu erro na criptografia");
        }
        return "DEUMERDA".getBytes();
        
    }
    
    public static byte[]  descriptografa(byte[] msn, byte[] key) {
        System.out.println("Descriptografando tamanho da menssagem = "+msn.length);
        System.out.println("msg = "+Arrays.toString( msn));
        try {
            Cipher c;
            c = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            c.init(Cipher.DECRYPT_MODE, k);
            byte[] encryptedData = c.doFinal(msn);
            return encryptedData;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println("Deu erro na criptografia");
        }
        return null;
        
        
    }
    
    
    public static String nextSessionId() {
        return new BigInteger(130, random).toString(16);
    }
    
    
}
