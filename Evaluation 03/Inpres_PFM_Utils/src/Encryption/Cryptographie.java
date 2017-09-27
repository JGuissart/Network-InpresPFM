/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Julien
 */
public class Cryptographie
{
    private static String KeystoreDirectory = System.getProperty("user.home") + System.getProperty("file.separator") + "Keystores" + System.getProperty("file.separator");
    private static String CodeProvider = "BC";
    
    /*------------------------------ CLE DE SESSION ------------------------------*/
    
    /**
     * Crée une clé de session qui sera échangée entre les 2 parties
     * @return la clé de session
     */
    public static SecretKey CreateSecretKey()
    {
        try
        {
            
            KeyGenerator cleGen = KeyGenerator.getInstance("DES", CodeProvider);
            cleGen.init(new SecureRandom());
            return cleGen.generateKey();
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException ex) 
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /*------------------------------ DIGEST ------------------------------*/
    
    /**
     * Crée un digest pour le contrôle d'intégrité
     * @param message l'objetChiffre à hashé
     * @return le digest
     */
    public static byte[] CreateDigest(String message)
    {
        try 
        {
            Security.addProvider(new BouncyCastleProvider());
            /* Confection du digest à envoyer au serveur */
            MessageDigest md = MessageDigest.getInstance("SHA-1", CodeProvider);
            md.update(message.getBytes());

            return md.digest();
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Crée un Digest salé d'un objetChiffre passé en paramètre
     * @param message le objetChiffre à hashé
     * @param date la date au format long pour le sel
     * @param random un nombre aléatoire pour le sel
     * @return le digest salé
     */
    public static byte[] CreateDigest(String message, long date, Double random)
    {
        try 
        {
            /* Envoi du sel sur un flux tableau de bytes */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            bdos.writeLong(date);
            bdos.writeDouble(random);

            Security.addProvider(new BouncyCastleProvider());
            /* Confection du digest à envoyer au serveur */
            MessageDigest md = MessageDigest.getInstance("SHA-1", CodeProvider);
            md.update(message.getBytes());
            md.update(baos.toByteArray());
            
            return md.digest();
        }
        catch (IOException | NoSuchAlgorithmException | NoSuchProviderException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Compare les digest passés en paramètre
     * @param digest1 Le digest 1
     * @param digest2 Le digest 2
     * @return vrai si les 2 digests sont identiques, faux sinon
     */
    public static boolean CompareDigest(byte[] digest1, byte[] digest2)
    {
        return MessageDigest.isEqual(digest1, digest2);
    }
    
    /*------------------------------ HANDSHAKE ------------------------------*/
    
    /**
     * Chiffre la clé de session en vue de l'échanger sur le réseau
     * @param cleSymetrique la clé de session à chiffrer
     * @param nameKeyStore le nom du keystore où se trouve la clé publique du destinataire
     * @param passwordKeyStore le mot de passe du keystore
     * @param aliasDestinataire l'alias du certificat contenant la clé publique du destinataire
     * @return la clé de session chiffrée
     */
    public static byte[] ChiffrementCleSession(SecretKey cleSymetrique, String nameKeyStore, String passwordKeyStore, String aliasDestinataire)
    {
        System.out.println("***** DEBUT ChiffrementCleSession *****\n");
        byte[] arrayByteCleChiffree = null;
        try 
        {
            /***** Algo Asymetrique *****/
            KeyStore ks = KeyStore.getInstance("PKCS12", CodeProvider);
            ks.load(new FileInputStream(KeystoreDirectory + nameKeyStore), passwordKeyStore.toCharArray());
            
            /* Récupérer le certificat contenant la clé publique du destinataire (le mec d'en face) */
            X509Certificate certif = (X509Certificate) ks.getCertificate(aliasDestinataire);
            PublicKey clePublique = certif.getPublicKey(); // Récupération de la clé publique du destinataire
            Cipher chiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding", CodeProvider); // Chiffrement ASYMETRIQUE !
            chiffrement.init(Cipher.ENCRYPT_MODE, clePublique);
            
            /* Transformer la clé symétrique à chiffer en bytes */
            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream oos = new DataOutputStream(baos);
            oos.write(cleSymetrique.getEncoded());*/
            
            arrayByteCleChiffree = chiffrement.doFinal(cleSymetrique.getEncoded());
            //baos.flush();
        }
        catch (InvalidKeyException | IOException| CertificateException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException | BadPaddingException | KeyStoreException ex) 
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("***** FIN ChiffrementCleSession *****");
        return arrayByteCleChiffree;
    }
    
    /**
     * Déchiffre la clé de session reçue par l'autre partie
     * @param cleSymetriqueChiffree la clé symétrique chiffrée (sous forme de tableau de bytes)
     * @param nameKeyStore le nom du keystore où se trouve la clé privée du receveur
     * @param passwordKeyStore le mot de passe du keystore
     * @param passwordKey le mot de passe de la clé privée
     * @param alias l'alias de la clé privée
     * @return la clé de session
     */
    public static SecretKey DechiffrementCleSession(byte[] cleSymetriqueChiffree, String nameKeyStore, String passwordKeyStore, String passwordKey, String alias)
    {
        System.out.println("***** DEBUT DechiffrementCleSession *****\n");
        byte[] arrayByteCleDechiffree = null;
        try 
        {
            /***** Algo Asymetrique *****/
            KeyStore ks = KeyStore.getInstance("PKCS12", CodeProvider);
            ks.load(new FileInputStream(KeystoreDirectory + nameKeyStore), passwordKeyStore.toCharArray());
            PrivateKey clePrivee = (PrivateKey)ks.getKey(alias, passwordKey.toCharArray());
            
            /* Déchiffrement de la clé de session */
            Cipher dechiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding", CodeProvider); // Chiffrement ASYMETRIQUE !
            dechiffrement.init(Cipher.DECRYPT_MODE, clePrivee);
            arrayByteCleDechiffree = dechiffrement.doFinal(cleSymetriqueChiffree);
        }
        catch (InvalidKeyException | IOException| CertificateException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException | BadPaddingException | KeyStoreException ex) 
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | UnrecoverableKeyException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("***** FIN DechiffrementCleSession *****");
        return new SecretKeySpec(arrayByteCleDechiffree, 0, arrayByteCleDechiffree.length, "DES");
    }
    
    /**
     * Compare 2 clés de sessions
     * @param cle1 la clé de session 1
     * @param cle2 la clé de session 2
     * @return la concordance des 2 clés
     */
    public static boolean CompareCleSession(SecretKey cle1, SecretKey cle2)
    {
        System.out.println("***** DEBUT CompareCleSession *****\n");
        byte[] arrayCle1 = null;
        byte[] arrayCle2 = null;
        try
        {
            /* Transformer la clé symétrique à chiffer en bytes */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(cle1);
            arrayCle1 = baos.toByteArray();
            baos.flush();
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
            oos2.writeObject(cle2);
            arrayCle2 = baos2.toByteArray();
            baos2.flush();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("***** FIN CompareCleSession *****");
        return Arrays.equals(arrayCle1, arrayCle2);
    }
    
    /*------------------------------ CHIFFREMENT/DECHIFFREMENT ------------------------------*/
    
    /**
     * Chiffre symétriquement l'objet passé en paramètre
     * @param cleChiffrement la clé de session permettant le chiffrement
     * @param messageClair l'objet à chiffrer
     * @return l'objet passé en paramètre chiffré sous forme de tableau de bytes
     */
    public static byte[] Chiffrement(SecretKey cleChiffrement, Object messageClair) 
    {
        byte[] messageChiffre = null;
        try 
        {
            /***** Algo symétrique *****/
            Cipher chiffrement = Cipher.getInstance("DES/ECB/PKCS5Padding", CodeProvider); // Cipher pour chiffrement SYMETRIQUE !
            chiffrement.init(Cipher.ENCRYPT_MODE, cleChiffrement); // ENCRYPT_MODE --> Chiffrement
            
            /* Transformer l'objet à chiffer en bytes */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(messageClair); // On écrit l'objet à chiffrer sur le flux
            
            messageChiffre = chiffrement.doFinal(baos.toByteArray()); // On chiffre à proprement parlé
            baos.flush();
        }
        catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException | BadPaddingException | NoSuchProviderException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return messageChiffre;
    }
    
    /**
     * Déchiffre symétriquement l'objet chiffré passé en paramètre (sous forme de tableau de bytes)
     * @param cleChiffrement la clé de session permettant le déchiffrement
     * @param messageChiffre l'objet chiffré à déchiffrer
     * @return l'objet déchiffré
     */
    public static Object Dechiffrement(SecretKey cleChiffrement, byte[] messageChiffre)
    {
        Object objMessageClair = null;
        try 
        {
            /***** Algo symétrique *****/
            Cipher dechiffrement = Cipher.getInstance("DES/ECB/PKCS5Padding", CodeProvider);  // Cipher pour déchiffrement SYMETRIQUE !
            dechiffrement.init(Cipher.DECRYPT_MODE, cleChiffrement); // DECRYPT_MODE -> Déchiffrement
            byte[] objetChiffre = dechiffrement.doFinal(messageChiffre); // On déchiffre l'objet !

            /* Transformer le tableau de bytes objetChiffre en objet Java */
            ByteArrayInputStream bais = new ByteArrayInputStream(objetChiffre);
            ObjectInputStream ois = new ObjectInputStream(bais);
            objMessageClair = ois.readObject();
        }
        catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException | BadPaddingException | ClassNotFoundException | NoSuchProviderException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Decryptage termine !");
        return objMessageClair;
    }
    
    /*------------------------------ SIGNATURE ------------------------------*/
    
    /**
     * 
     * @param nameKeyStore
     * @param alias
     * @param passwordKeyStore
     * @param passwordKey
     * @param messageClair
     * @return 
     */
    public static byte[] CreateSignature(String nameKeyStore, String alias, String passwordKeyStore, String passwordKey, Object messageClair)
    {
        byte[] arrayByteSignature = null;
        try
        {
            /***** Algo Asymetrique *****/
            KeyStore ks = KeyStore.getInstance("PKCS12", CodeProvider);
            ks.load(new FileInputStream(KeystoreDirectory + nameKeyStore), passwordKeyStore.toCharArray());
            PrivateKey clePrivee = (PrivateKey)ks.getKey(alias, passwordKey.toCharArray());
            
            /* Déclarer la signature */
            Signature s = Signature.getInstance("SHA1withRSA", CodeProvider);
            s.initSign(clePrivee); // Initialisation de la signature avec la clé privée
            
            /* Transformer l'objet à signer en bytes */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(messageClair);
            
            s.update(baos.toByteArray()); // Ajouter les "ingrédients" de la signature
            arrayByteSignature = s.sign(); // renvoi la signature sous forme d'un tableau de bytes
        } 
        catch (KeyStoreException | FileNotFoundException | NoSuchAlgorithmException | CertificateException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnrecoverableKeyException | InvalidKeyException | SignatureException | NoSuchProviderException | IOException ex) 
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return arrayByteSignature;
    }
    
    /**
     * 
     * @param nameKeyStore
     * @param aliasExpediteur
     * @param passwordKeyStore
     * @param messageClair
     * @param signature
     * @return 
     */
    public static Boolean CompareSignature(String nameKeyStore, String aliasExpediteur, String passwordKeyStore, Object messageClair, byte[] signature)
    {
        try
        {
            /***** Algo symétrique *****/
            KeyStore ks = KeyStore.getInstance("PKCS12", CodeProvider);
            ks.load(new FileInputStream(KeystoreDirectory + nameKeyStore), passwordKeyStore.toCharArray());
            /* Récupérer la clé publique de l'expéditeur dans le certificat */
            X509Certificate certif = (X509Certificate) ks.getCertificate(aliasExpediteur);
            PublicKey clePublique = certif.getPublicKey();
            
            Signature s = Signature.getInstance("SHA1withRSA", CodeProvider);
            s.initVerify(clePublique); // Initialisation de la signature avec la clé publique
            
            /* Transformer l'objet reçu en bytes */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(messageClair);
            
            s.update(baos.toByteArray()); // Ajouter les "ingrédients" de la signature
            
            return s.verify(signature); // Verifie vraiment la signature
        } 
        catch (KeyStoreException | FileNotFoundException | NoSuchAlgorithmException | CertificateException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidKeyException | SignatureException | NoSuchProviderException | IOException ex) 
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    /*------------------------------ HMAC ------------------------------*/
    
    /**
     * 
     * @param cleHMAC
     * @param message
     * @return 
     */
    public static byte[] CreateHMAC(SecretKey cleHMAC, Object message) 
    {
        byte[] messageHashe = null;
        try
        {
            /* Transformer l'objet à hasher en bytes */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message); // On écrit l'objet à hasher sur le flux
            
            /* Confection du HMAC */
            Mac hmac = Mac.getInstance("HmacSHA1", CodeProvider);
            hmac.init(cleHMAC); // On initialise le HMAC avec la clé secrète pour HMAC
            hmac.update(baos.toByteArray());
            messageHashe = hmac.doFinal();
            baos.flush();
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IOException ex) 
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return messageHashe;
    }
    
    /**
     * Compare le HMAC reçu avec un HMAC local créé sur base du message reçu
     * @param cleHMAC la clé de session du HMAC
     * @param message le message reçu
     * @param HMACRecu le HMAC reçu
     * @return la concordance des 2 HMAC
     */
    public static Boolean CompareHMAC(SecretKey cleHMAC, Object message, byte[] HMACRecu) 
    {
        byte[] HMACLocal = null;
        try 
        {
            /* Transformer l'objet à hasher en bytes */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message); // On écrit l'objet à hasher sur le flux
            
            /* Confection du HMAC local */
            Mac hmac = Mac.getInstance("HmacSHA1", CodeProvider);
            hmac.init(cleHMAC);
            hmac.update(baos.toByteArray());
            HMACLocal = hmac.doFinal();
            baos.flush();
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IOException ex)
        {
            Logger.getLogger(Cryptographie.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return MessageDigest.isEqual(HMACLocal, HMACRecu);
    }
}
