package com.aulas.util.oldsessionmanager;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import sun.misc.*;

public class Criptografia {
    
      
    public Criptografia() throws Exception
    {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());

        final byte[] DESKeyBytes =
        {0x01, 0x02, 0x04, 0x08, 0x08, 0x04, 0x02, 0x01};
       
        // IV For CBC mode
        // Again, could be read from a file.
        final byte[] ivBytes = 
        {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        
        // Password encrypt agent that assumes the password is only ASCII characters
        String characterEncoding = "ASCII";
        
        SecretKey key = new SecretKeySpec(DESKeyBytes, "DES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);     
        this.characterEncoding = characterEncoding;
        this.encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding", "SunJCE");
        this.encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, iv); 
        this.decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding", "SunJCE");
        this.decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, iv);
    }
    
    synchronized public String encrypt(String password) throws Exception
    {
        byte[] passwordBytes = password.getBytes(characterEncoding);
        byte[] encryptedPasswordBytes = this.encryptCipher.doFinal(passwordBytes);
        String encodedEncryptedPassword = this.base64Encoder.encode(encryptedPasswordBytes);
        return encodedEncryptedPassword;
    }
    
    synchronized public String decrypt(String encodedEncryptedPassword) throws Exception
    {
        byte[] encryptedPasswordBytes = this.base64Decoder.decodeBuffer(encodedEncryptedPassword);
        byte[] passwordBytes = this.decryptCipher.doFinal(encryptedPasswordBytes);
        String recoveredPassword = new String(passwordBytes, characterEncoding);
        return recoveredPassword;
    }
    
    private String characterEncoding;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private BASE64Encoder base64Encoder = new BASE64Encoder();
    private BASE64Decoder base64Decoder = new BASE64Decoder();
}
