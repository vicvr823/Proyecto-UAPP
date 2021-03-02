package com.example.ejemplo1.webservices;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public String stringToHash(String sInputString) {
        // SHA1 -> 160 bits
        // SHA-256 -> 256 bits mas seguro
        return stringToHash(sInputString, "SHA-256");
    }

    public String stringToHash(String sInputString, String sHashType) {
        String sOutputString = "";
        try {
            // Create Hash Type
            MessageDigest digest = MessageDigest.getInstance(sHashType);
            digest.update(sInputString.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));

            sOutputString = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sOutputString;
    }
}
