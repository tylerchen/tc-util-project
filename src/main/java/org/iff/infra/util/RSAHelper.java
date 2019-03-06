/*******************************************************************************
 * Copyright (c) 2015-4-7 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * this code copy from:
 * <p>
 * Title: RSAHelper
 * DO NOT use the *ByDefaultKey method to encode or decode the data if you need the data safety and persistent.
 * the *ByDefaultKey method using to encrypt the data temporary.
 * </p>
 * <p>Description: Utility class that helps encrypt and decrypt strings using RSA algorithm</p>
 *
 * @author Aviran Mordo http://aviran.mordos.com
 * @version 1.0
 */
public class RSAHelper {
    protected static final String ALGORITHM = "RSA";
    protected static final Object[] PUB_KEY = new Object[1];
    protected static final Object[] PRI_KEY = new Object[1];

    /**
     * Init java security to add BouncyCastle as an RSA provider
     */
    static {
        //PUB-KEY Base64:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJHezVuDIGtL8RIcrU+7jCdMJIN2Amuzv0pU0iDCkTtuAuKrJidh35Tqa+4l5Z0uDCy6KhJdBvE2tL/L8NUcw7e4bgbsV8rm+LZoKWjUTRPfwskCVDN4iUk6KAMpUyW73AHJz3XRZnq2Z+LmJy+6mnguofGU37Mr6c5Jeh4PbhcwIDAQAB
        //PUB-KEY HEX:30819f300d06092a864886f70d010101050003818d0030818902818100891decd5b83206b4bf1121cad4fbb8c274c248376026bb3bf4a54d220c2913b6e02e2ab262761df94ea6bee25e59d2e0c2cba2a125d06f136b4bfcbf0d51cc3b7b86e06ec57cae6f8b6682968d44d13dfc2c90254337889493a2803295325bbdc01c9cf75d1667ab667e2e6272fba9a782ea1f194dfb32be9ce497a1e0f6e1730203010001
        //PRI-KEY Base64:MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIkd7NW4Mga0vxEhytT7uMJ0wkg3YCa7O/SlTSIMKRO24C4qsmJ2HflOpr7iXlnS4MLLoqEl0G8Ta0v8vw1RzDt7huBuxXyub4tmgpaNRNE9/CyQJUM3iJSTooAylTJbvcAcnPddFmerZn4uYnL7qaeC6h8ZTfsyvpzkl6Hg9uFzAgMBAAECgYAqVwhD8m3YLkX1t1aXr+ccfMtlW1wgeZ6I6+ZGhjTE9qqA9wRAbTLSQhwxQ1tZJ0nqPNFKK5ASTI/MVg50wXZrz+BWiAWK27TASJEYn04xG3+82DD6HiKfNI4LYf1Mz6786JgOIZdr+EGVmU+N0tHKtOhsakqO9iHrDVG2HubXIQJBAMSiV/b3RR2VGEbgrCfHrqG/46rwOvf4C9gSSoh9k7v6gFaqtuhaQpuq6Y1t57CdPUTpqWolmEjWqP/eXcqmQAMCQQCyg41iY1NEMtyB6z7ulIuY7FGzYXSYTu0o2CbJmFMAg18Mr6PNOl970h/4iubGIEtXgLPJghw91A1A/xoBVDXRAkB8QndCOzpzGlAQIlTIgriJwOCXml45a4fYkJ6HKIxeg0vs+M7DgJ6NDvGujWCXzJX2YY7M5Fsa1IRRxW0R7gOPAkARdcB8YQ6h2v+qTWIIX8sPl/2dt+h5hS65EfspOJbVtAO17+/rgMwaBkFOQ/eyZTI5SsNK8Ejm2zi4pLrcamohAkEAron4qBBB7ztosCrV5DSiGn84Opn+oBgnrMgfDlqqhM0/UKdfpLoKjuD30aLDzhiHyclW8LdFJ+Lnbr+A+Jfbuw==
        //PRI-KEY HEX:30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100891decd5b83206b4bf1121cad4fbb8c274c248376026bb3bf4a54d220c2913b6e02e2ab262761df94ea6bee25e59d2e0c2cba2a125d06f136b4bfcbf0d51cc3b7b86e06ec57cae6f8b6682968d44d13dfc2c90254337889493a2803295325bbdc01c9cf75d1667ab667e2e6272fba9a782ea1f194dfb32be9ce497a1e0f6e17302030100010281802a570843f26dd82e45f5b75697afe71c7ccb655b5c20799e88ebe6468634c4f6aa80f704406d32d2421c31435b592749ea3cd14a2b90124c8fcc560e74c1766bcfe05688058adbb4c04891189f4e311b7fbcd830fa1e229f348e0b61fd4ccfaefce8980e21976bf84195994f8dd2d1cab4e86c6a4a8ef621eb0d51b61ee6d721024100c4a257f6f7451d951846e0ac27c7aea1bfe3aaf03af7f80bd8124a887d93bbfa8056aab6e85a429baae98d6de7b09d3d44e9a96a259848d6a8ffde5dcaa64003024100b2838d6263534432dc81eb3eee948b98ec51b36174984eed28d826c9985300835f0cafa3cd3a5f7bd21ff88ae6c6204b5780b3c9821c3dd40d40ff1a015435d102407c4277423b3a731a50102254c882b889c0e0979a5e396b87d8909e87288c5e834becf8cec3809e8d0ef1ae8d6097cc95f6618ecce45b1ad48451c56d11ee038f02401175c07c610ea1daffaa4d62085fcb0f97fd9db7e879852eb911fb293896d5b403b5efefeb80cc1a06414e43f7b26532394ac34af048e6db38b8a4badc6a6a21024100ae89f8a81041ef3b68b02ad5e434a21a7f383a99fea01827acc81f0e5aaa84cd3f50a75fa4ba0a8ee0f7d1a2c3ce1887c9c956f0b74527e2e76ebf80f897dbbb
        {
            String defaultPubKeyHex = "30819f300d06092a864886f70d010101050003818d0030818902818100891decd5b83206b4bf1121cad4fbb8c274c248376026bb3bf4a54d220c2913b6e02e2ab262761df94ea6bee25e59d2e0c2cba2a125d06f136b4bfcbf0d51cc3b7b86e06ec57cae6f8b6682968d44d13dfc2c90254337889493a2803295325bbdc01c9cf75d1667ab667e2e6272fba9a782ea1f194dfb32be9ce497a1e0f6e1730203010001";
            String defaultPubKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJHezVuDIGtL8RIcrU+7jCdMJIN2Amuzv0pU0iDCkTtuAuKrJidh35Tqa+4l5Z0uDCy6KhJdBvE2tL/L8NUcw7e4bgbsV8rm+LZoKWjUTRPfwskCVDN4iUk6KAMpUyW73AHJz3XRZnq2Z+LmJy+6mnguofGU37Mr6c5Jeh4PbhcwIDAQAB";
            String pubKeyHex = System.getProperty("tc_rsa_pub_hex");
            String pubKeyB64 = System.getProperty("tc_rsa_pub_b64");
            try {
                if (!StringUtils.isBlank(pubKeyHex)) {
                    PUB_KEY[0] = Hex.decodeHex(pubKeyHex.toCharArray());
                } else if (!StringUtils.isBlank(pubKeyB64)) {
                    PUB_KEY[0] = Base64.decodeBase64(pubKeyB64);
                } else {
                    PUB_KEY[0] = Base64.decodeBase64(defaultPubKeyBase64);
                }
            } catch (Exception e) {
                PUB_KEY[0] = Base64.decodeBase64(defaultPubKeyBase64);
                Logger.warn(FCS.get(
                        //
                        "RSA Public Key init error, using default public key, keyHex:{pubKeyHex}, keyB64:{pubKeyB64}",
                        pubKeyHex, pubKeyB64), e);
            }
        }
        {
            String defaultPriKeyHex = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100891decd5b83206b4bf1121cad4fbb8c274c248376026bb3bf4a54d220c2913b6e02e2ab262761df94ea6bee25e59d2e0c2cba2a125d06f136b4bfcbf0d51cc3b7b86e06ec57cae6f8b6682968d44d13dfc2c90254337889493a2803295325bbdc01c9cf75d1667ab667e2e6272fba9a782ea1f194dfb32be9ce497a1e0f6e17302030100010281802a570843f26dd82e45f5b75697afe71c7ccb655b5c20799e88ebe6468634c4f6aa80f704406d32d2421c31435b592749ea3cd14a2b90124c8fcc560e74c1766bcfe05688058adbb4c04891189f4e311b7fbcd830fa1e229f348e0b61fd4ccfaefce8980e21976bf84195994f8dd2d1cab4e86c6a4a8ef621eb0d51b61ee6d721024100c4a257f6f7451d951846e0ac27c7aea1bfe3aaf03af7f80bd8124a887d93bbfa8056aab6e85a429baae98d6de7b09d3d44e9a96a259848d6a8ffde5dcaa64003024100b2838d6263534432dc81eb3eee948b98ec51b36174984eed28d826c9985300835f0cafa3cd3a5f7bd21ff88ae6c6204b5780b3c9821c3dd40d40ff1a015435d102407c4277423b3a731a50102254c882b889c0e0979a5e396b87d8909e87288c5e834becf8cec3809e8d0ef1ae8d6097cc95f6618ecce45b1ad48451c56d11ee038f02401175c07c610ea1daffaa4d62085fcb0f97fd9db7e879852eb911fb293896d5b403b5efefeb80cc1a06414e43f7b26532394ac34af048e6db38b8a4badc6a6a21024100ae89f8a81041ef3b68b02ad5e434a21a7f383a99fea01827acc81f0e5aaa84cd3f50a75fa4ba0a8ee0f7d1a2c3ce1887c9c956f0b74527e2e76ebf80f897dbbb";
            String defaultPriKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIkd7NW4Mga0vxEhytT7uMJ0wkg3YCa7O/SlTSIMKRO24C4qsmJ2HflOpr7iXlnS4MLLoqEl0G8Ta0v8vw1RzDt7huBuxXyub4tmgpaNRNE9/CyQJUM3iJSTooAylTJbvcAcnPddFmerZn4uYnL7qaeC6h8ZTfsyvpzkl6Hg9uFzAgMBAAECgYAqVwhD8m3YLkX1t1aXr+ccfMtlW1wgeZ6I6+ZGhjTE9qqA9wRAbTLSQhwxQ1tZJ0nqPNFKK5ASTI/MVg50wXZrz+BWiAWK27TASJEYn04xG3+82DD6HiKfNI4LYf1Mz6786JgOIZdr+EGVmU+N0tHKtOhsakqO9iHrDVG2HubXIQJBAMSiV/b3RR2VGEbgrCfHrqG/46rwOvf4C9gSSoh9k7v6gFaqtuhaQpuq6Y1t57CdPUTpqWolmEjWqP/eXcqmQAMCQQCyg41iY1NEMtyB6z7ulIuY7FGzYXSYTu0o2CbJmFMAg18Mr6PNOl970h/4iubGIEtXgLPJghw91A1A/xoBVDXRAkB8QndCOzpzGlAQIlTIgriJwOCXml45a4fYkJ6HKIxeg0vs+M7DgJ6NDvGujWCXzJX2YY7M5Fsa1IRRxW0R7gOPAkARdcB8YQ6h2v+qTWIIX8sPl/2dt+h5hS65EfspOJbVtAO17+/rgMwaBkFOQ/eyZTI5SsNK8Ejm2zi4pLrcamohAkEAron4qBBB7ztosCrV5DSiGn84Opn+oBgnrMgfDlqqhM0/UKdfpLoKjuD30aLDzhiHyclW8LdFJ+Lnbr+A+Jfbuw==";
            String priKeyHex = System.getProperty("tc_rsa_pri_hex");
            String priKeyB64 = System.getProperty("tc_rsa_pri_b64");
            try {
                if (!StringUtils.isBlank(priKeyHex)) {
                    PRI_KEY[0] = Hex.decodeHex(priKeyHex.toCharArray());
                } else if (!StringUtils.isBlank(priKeyB64)) {
                    PRI_KEY[0] = Base64.decodeBase64(priKeyB64);
                } else {
                    PRI_KEY[0] = Base64.decodeBase64(defaultPriKeyBase64);
                }
            } catch (Exception e) {
                PRI_KEY[0] = Base64.decodeBase64(defaultPriKeyBase64);
                Logger.warn(
                        FCS.get("RSA Private Key init error, using default private key, keyHex:{priKeyHex}, keyB64:{priKeyB64}",
                                priKeyHex, priKeyB64),
                        e);
            }
        }
        try {
            Security.addProvider(new BouncyCastleProvider());
            Logger.info("Using BouncyCastleProvider as RSA provider.");
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper: Security.addProvider(new BouncyCastleProvider()) Fail! ",
                    e);
        }
    }

    /**
     * Generate key which contains a pair of privae and public key using 1024 bytes
     *
     * @return key pair
     */
    public static KeyPair generateKey() {
        KeyPair key = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            keyGen.initialize(1024, new SecureRandom());
            key = keyGen.generateKeyPair();
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.generateKey", e);
        }
        return key;
    }

    /**
     * Generate key which contains a pair of privae and public key using 1024 bytes
     *
     * @return key pair
     */
    public static Map<String, byte[]> generateKeyBytes() {
        try {
            KeyPair key = null;
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            keyGen.initialize(1024, new SecureRandom());
            key = keyGen.generateKeyPair();
            return MapHelper.toMap("private", key.getPrivate().getEncoded(), "public", key.getPublic().getEncoded());
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.generateKeyBytes", e);
        }
        return null;
    }

    /**
     * Generates Public Key from bytes
     *
     * @param key hex encoded string which represents the key
     * @return The PublicKey
     */
    public static PublicKey getPublicKeyFromBytes(byte[] key) {
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(key);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.getPublicKeyFromBytes", e);
        }
        return publicKey;
    }

    /**
     * Generates Public Key from Hex encoded string
     *
     * @param key hex encoded string which represents the key
     * @return The PublicKey
     */
    public static PublicKey getPublicKeyFromHex(String key) {
        PublicKey publicKey = null;
        try {
            publicKey = getPublicKeyFromBytes(Hex.decodeHex(key.toCharArray()));
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.getPublicKeyFromHex", e);
        }
        return publicKey;
    }

    /**
     * Generates Public Key from BASE64 encoded string
     *
     * @param key BASE64 encoded string which represents the key
     * @return The PublicKey
     */
    public static PublicKey getPublicKeyFromBase62(String key) {
        PublicKey publicKey = null;
        try {
            publicKey = getPublicKeyFromBytes(BaseCryptHelper.decodeBase62(key.toCharArray()));
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.getPublicKeyFromBase62", e);
        }
        return publicKey;
    }

    /**
     * Generates Public Key from BASE64 encoded string
     *
     * @param key BASE64 encoded string which represents the key
     * @return The PublicKey
     */
    public static PublicKey getPublicKeyFromBase64(String key) {
        PublicKey publicKey = null;
        try {
            publicKey = getPublicKeyFromBytes(BaseCryptHelper.decodeBase64(key.toCharArray()));
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.getPublicKeyFromBase64", e);
        }
        return publicKey;
    }

    /**
     * Encrypt a text using public key.
     *
     * @param text The original unencrypted text
     * @param key  The public key
     * @return Encrypted text
     */
    public static byte[] encrypt(byte[] text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
            // encrypt the plaintext using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.encrypt", e);
        }
        return cipherText;
    }

    /**
     * Encrypt a text using public key.
     *
     * @param text The original unencrypted text
     * @param key  The public key
     * @return Encrypted text
     */
    public static byte[] encrypt(byte[] text, byte[] key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
            // encrypt the plaintext using the public key
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyFromBytes(key));
            cipherText = cipher.doFinal(text);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.encrypt", e);
        }
        return cipherText;
    }

    /**
     * Encrypt a text using public key.
     *
     * @param text      The original unencrypted text
     * @param base62Key The public key
     * @return Encrypted text
     */
    public static byte[] encryptByBase62Key(byte[] text, String base62Key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
            // encrypt the plaintext using the public key
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyFromBase62(base62Key));
            cipherText = cipher.doFinal(text);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.encrypt", e);
        }
        return cipherText;
    }

    /**
     * Encrypt a text using public key.
     *
     * @param text      The original unencrypted text
     * @param base64Key The public key
     * @return Encrypted text
     */
    public static byte[] encryptByBase64Key(byte[] text, String base64Key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
            // encrypt the plaintext using the public key
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyFromBase64(base64Key));
            cipherText = cipher.doFinal(text);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.encrypt", e);
        }
        return cipherText;
    }

    /**
     * Encrypt a text using default public key.
     *
     * @param text The original unencrypted text
     * @return Encrypted text
     */
    public static byte[] encryptByDefaultKey(byte[] text) {
        return encrypt(text, getPublicKeyFromBytes((byte[]) PUB_KEY[0]));
    }

    /**
     * Encrypt a text using public key. The result is enctypted BASE64 encoded text
     *
     * @param text The original unencrypted text
     * @param key  The public key
     * @return Encrypted text encoded as BASE64
     */
    public static String encrypt(String text, PublicKey key) {
        String encryptedText = null;
        try {
            byte[] cipherText = encrypt(text.getBytes("UTF8"), key);
            encryptedText = Base64.encodeBase64String(cipherText);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.encrypt", e);
        }
        return encryptedText;
    }

    /**
     * Encrypt a text using default public key. The result is enctypted BASE64 encoded text
     *
     * @param text The original unencrypted text
     * @return Encrypted text encoded as BASE64
     */
    public static String encryptByDefaultKey(String text) {
        return encrypt(text, getPublicKeyFromBytes((byte[]) PUB_KEY[0]));
    }

    /**
     * Decrypt text using private key
     *
     * @param text The encrypted text
     * @param key  The private key
     * @return The unencrypted text
     */
    public static byte[] decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            // decrypt the text using the private key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.decrypt", e);
        }
        return dectyptedText;
    }

    /**
     * Decrypt text using default private key
     *
     * @param text The encrypted text
     * @return The unencrypted text
     */
    public static byte[] decryptByDefaultKey(byte[] text) {
        return decrypt(text, getPrivateKeyFromBytes((byte[]) PRI_KEY[0]));
    }

    /**
     * Decrypt BASE64 encoded text using private key
     *
     * @param text The encrypted text, encoded as BASE64
     * @param key  The private key
     * @return The unencrypted text encoded as UTF8
     */
    public static String decrypt(String text, PrivateKey key) {
        String result = null;
        try {
            // decrypt the text using the private key
            byte[] dectyptedText = decrypt(Base64.decodeBase64(text), key);
            result = new String(dectyptedText, "UTF8");
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.decrypt", e);
        }
        return result;
    }

    /**
     * Decrypt BASE64 encoded text using default private key
     *
     * @param text The encrypted text, encoded as BASE64
     * @return The unencrypted text encoded as UTF8
     */
    public static String decryptByDefaultKey(String text) {
        return decrypt(text, getPrivateKeyFromBytes((byte[]) PRI_KEY[0]));
    }

    /**
     * Convert a Key to string encoded as BASE64
     *
     * @param key The key (private or public)
     * @return A string representation of the key
     */
    public static String getKeyAsBase64(Key key) {
        // Get the bytes of the key
        byte[] keyBytes = key.getEncoded();
        return Base64.encodeBase64String(keyBytes);
    }

    /**
     * Convert a Key to string encoded as BASE64
     *
     * @param key The key (private or public)
     * @return A string representation of the key
     */
    public static String getKeyAsHex(Key key) {
        // Get the bytes of the key
        byte[] keyBytes = key.getEncoded();
        return new String(Hex.encodeHex(keyBytes, true));
    }

    /**
     * Generates Private Key from BASE64 encoded string
     *
     * @param key BASE64 encoded string which represents the key
     * @return The PrivateKey
     */
    public static PrivateKey getPrivateKeyFromBase64(String key) {
        PrivateKey privateKey = null;
        try {
            privateKey = getPrivateKeyFromBytes(Base64.decodeBase64(key));
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.getPrivateKeyFromBase64", e);
        }
        return privateKey;
    }

    /**
     * Generates Private Key from Hex encoded string
     *
     * @param key Hex encoded string which represents the key
     * @return The PrivateKey
     */
    public static PrivateKey getPrivateKeyFromHex(String key) {
        PrivateKey privateKey = null;
        try {
            privateKey = getPrivateKeyFromBytes(Hex.decodeHex(key.toCharArray()));
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.getPrivateKeyFromHex", e);
        }
        return privateKey;
    }

    /**
     * Generates Private Key from bytes
     *
     * @param bytes Hex encoded string which represents the key
     * @return The PrivateKey
     */
    public static PrivateKey getPrivateKeyFromBytes(byte[] bytes) {
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytes);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.getPrivateKeyFromBytes", e);
        }
        return privateKey;
    }

    /**
     * Encrypt file using 1024 RSA encryption
     *
     * @param srcFileName  Source file name
     * @param destFileName Destination file name
     * @param key          The key. For encryption this is the Private Key and for decryption this is the public key
     */
    public static void encryptFile(String srcFileName, String destFileName, PublicKey key) {
        encryptDecryptFile(srcFileName, destFileName, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * Encrypt file using default 1024 RSA encryption
     *
     * @param srcFileName  Source file name
     * @param destFileName Destination file name
     */
    public static void encryptFileByDefaultKey(String srcFileName, String destFileName) {
        encryptDecryptFile(srcFileName, destFileName, getPublicKeyFromBytes((byte[]) PUB_KEY[0]), Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypt file using 1024 RSA encryption
     *
     * @param srcFileName  Source file name
     * @param destFileName Destination file name
     * @param key          The key. For encryption this is the Private Key and for decryption this is the public key
     */
    public static void decryptFile(String srcFileName, String destFileName, PrivateKey key) {
        encryptDecryptFile(srcFileName, destFileName, key, Cipher.DECRYPT_MODE);
    }

    /**
     * Decrypt file using default 1024 RSA encryption
     *
     * @param srcFileName  Source file name
     * @param destFileName Destination file name
     */
    public static void decryptFileyDefaultKey(String srcFileName, String destFileName) {
        encryptDecryptFile(srcFileName, destFileName, getPrivateKeyFromBytes((byte[]) PRI_KEY[0]), Cipher.DECRYPT_MODE);
    }

    /**
     * Encrypt and Decrypt files using 1024 RSA encryption
     *
     * @param srcFileName  Source file name
     * @param destFileName Destination file name
     * @param key          The key. For encryption this is the Private Key and for decryption this is the public key
     * @param cipherMode   Cipher Mode
     */
    public static void encryptDecryptFile(String srcFileName, String destFileName, Key key, int cipherMode) {
        OutputStream outputWriter = null;
        InputStream inputReader = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", BouncyCastleProvider.PROVIDER_NAME);
            //RSA encryption data size limitations are slightly less than the key modulus size,
            //depending on the actual padding scheme used (e.g. with 1024 bit (128 byte) RSA key,
            //the size limit is 117 bytes for PKCS#1 v 1.5 padding. (http://www.jensign.com/JavaScience/dotnet/RSAEncrypt/)
            byte[] buf = cipherMode == Cipher.ENCRYPT_MODE ? new byte[100] : new byte[128];
            int bufl;
            // init the Cipher object for Encryption...
            cipher.init(cipherMode, key);
            // start FileIO
            outputWriter = new FileOutputStream(destFileName);
            inputReader = new FileInputStream(srcFileName);
            while ((bufl = inputReader.read(buf)) != -1) {
                byte[] encText = null;
                if (cipherMode == Cipher.ENCRYPT_MODE) {
                    encText = encrypt(copyBytes(buf, bufl), (PublicKey) key);
                } else {
                    encText = decrypt(copyBytes(buf, bufl), (PrivateKey) key);
                }
                outputWriter.write(encText);
            }
            outputWriter.flush();
        } catch (Exception e) {
            Exceptions.runtime("org.iff.infra.util.RSAHelper.encryptDecryptFile", e);
        } finally {
            try {
                if (outputWriter != null) {
                    outputWriter.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (Exception e) {
                // do nothing...
            } // end of inner try, catch (Exception)...
        }
    }

    public static byte[] copyBytes(byte[] arr, int length) {
        byte[] newArr = null;
        if (arr == null || arr.length == length) {
            newArr = arr;
        } else {
            newArr = new byte[length];
            for (int i = 0; i < length; i++) {
                newArr[i] = (byte) arr[i];
            }
        }
        return newArr;
    }

    public static byte[] getDefaultPubKey() {
        return (byte[]) PUB_KEY[0];
    }

    public static String getDefaultPubKeyHex() {
        return Hex.encodeHexString((byte[]) PUB_KEY[0]);
    }

    public static String getDefaultPubKeyBase64() {
        return Base64.encodeBase64String((byte[]) PUB_KEY[0]);
    }

    public static void main2(String[] args) {
        String encrypt = encryptByDefaultKey("hello world!");
        System.out.println(encrypt);
        System.out.println(decryptByDefaultKey(encrypt));
    }

    public static void main1(String[] args) {
        KeyPair kp = generateKey();
        String pubKey = getKeyAsBase64(kp.getPublic());
        String priKey = getKeyAsBase64(kp.getPrivate());
        System.out.println(pubKey);
        System.out.println(priKey);
        String encrypt = encrypt("hello world!", getPublicKeyFromBase64(pubKey));
        System.out.println(encrypt);
        System.out.println(decrypt(encrypt, getPrivateKeyFromBase64(priKey)));
        System.out.println("PUB-KEY Base64:" + pubKey);
        System.out.println("PUB-KEY HEX:" + getKeyAsHex(kp.getPublic()));
        System.out.println("PRI-KEY Base64:" + priKey);
        System.out.println("PRI-KEY HEX:" + getKeyAsHex(kp.getPrivate()));
    }

    public static void main(String[] args) {
        String encrypt = encrypt("iff", getPublicKeyFromBase64(
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJHezVuDIGtL8RIcrU+7jCdMJIN2Amuzv0pU0iDCkTtuAuKrJidh35Tqa+4l5Z0uDCy6KhJdBvE2tL/L8NUcw7e4bgbsV8rm+LZoKWjUTRPfwskCVDN4iUk6KAMpUyW73AHJz3XRZnq2Z+LmJy+6mnguofGU37Mr6c5Jeh4PbhcwIDAQAB"));
        System.out.println(encrypt);
        String decrypt = decryptByDefaultKey("ISxz8KSdJ+/0ebzV5Ps9qQIY/BMsbFofn7KTw79zcUn9YDY7tz4lkTRDv/9+KGYoMsZCj5969LsP3Nj2o9vIIaueGspih1CnMHv72sB4bHhndqGj5WQeH3Xn4sD0e8tn7glfqt6uERMrfQMeFWzICLDvf8aM7/WuJCQyNHN96t8=");
        System.out.println(decrypt);
    }
}