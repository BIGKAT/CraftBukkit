package net.minecraft.server;

import cpw.mods.fml.common.asm.ReobfuscationMarker;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.org.bouncycastle.crypto.BufferedBlockCipher;
import net.minecraft.org.bouncycastle.crypto.engines.AESFastEngine;
import net.minecraft.org.bouncycastle.crypto.io.CipherInputStream;
import net.minecraft.org.bouncycastle.crypto.io.CipherOutputStream;
import net.minecraft.org.bouncycastle.crypto.modes.CFBBlockCipher;
import net.minecraft.org.bouncycastle.crypto.params.KeyParameter;
import net.minecraft.org.bouncycastle.crypto.params.ParametersWithIV;
import net.minecraft.org.bouncycastle.jce.provider.BouncyCastleProvider;

@ReobfuscationMarker // Forge
public class MinecraftEncryption {

   public static final Charset a = Charset.forName("ISO_8859_1");


   public static KeyPair b() {
      try {
         KeyPairGenerator var0 = KeyPairGenerator.getInstance("RSA");
         var0.initialize(1024);
         return var0.generateKeyPair();
      } catch (NoSuchAlgorithmException var1) {
         var1.printStackTrace();
         System.err.println("Key pair generation failed!");
         return null;
      }
   }

   public static byte[] a(String var0, PublicKey var1, SecretKey var2) {
      try {
         return a("SHA-1", new byte[][]{var0.getBytes("ISO_8859_1"), var2.getEncoded(), var1.getEncoded()});
      } catch (UnsupportedEncodingException var4) {
         var4.printStackTrace();
         return null;
      }
   }

   private static byte[] a(String var0, byte[] ... var1) {
      try {
         MessageDigest var2 = MessageDigest.getInstance(var0);
         byte[][] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte[] var6 = var3[var5];
            var2.update(var6);
         }

         return var2.digest();
      } catch (NoSuchAlgorithmException var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public static PublicKey a(byte[] var0) {
      try {
         X509EncodedKeySpec var1 = new X509EncodedKeySpec(var0);
         KeyFactory var2 = KeyFactory.getInstance("RSA");
         return var2.generatePublic(var1);
      } catch (NoSuchAlgorithmException var3) {
         var3.printStackTrace();
      } catch (InvalidKeySpecException var4) {
         var4.printStackTrace();
      }

      System.err.println("Public key reconstitute failed!");
      return null;
   }

   public static SecretKey a(PrivateKey var0, byte[] var1) {
      return new SecretKeySpec(b(var0, var1), "AES");
   }

   public static byte[] b(Key var0, byte[] var1) {
      return a(2, var0, var1);
   }

   private static byte[] a(int var0, Key var1, byte[] var2) {
      try {
         return a(var0, var1.getAlgorithm(), var1).doFinal(var2);
      } catch (IllegalBlockSizeException var4) {
         var4.printStackTrace();
      } catch (BadPaddingException var5) {
         var5.printStackTrace();
      }

      System.err.println("Cipher data failed!");
      return null;
   }

   private static Cipher a(int var0, String var1, Key var2) {
      try {
         Cipher var3 = Cipher.getInstance(var1);
         var3.init(var0, var2);
         return var3;
      } catch (InvalidKeyException var4) {
         var4.printStackTrace();
      } catch (NoSuchAlgorithmException var5) {
         var5.printStackTrace();
      } catch (NoSuchPaddingException var6) {
         var6.printStackTrace();
      }

      System.err.println("Cipher creation failed!");
      return null;
   }

   private static BufferedBlockCipher a(boolean var0, Key var1) {
      BufferedBlockCipher var2 = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
      var2.a(var0, new ParametersWithIV(new KeyParameter(var1.getEncoded()), var1.getEncoded(), 0, 16));
      return var2;
   }

   public static OutputStream a(SecretKey var0, OutputStream var1) {
      return new CipherOutputStream(var1, a(true, var0));
   }

   public static InputStream a(SecretKey var0, InputStream var1) {
      return new CipherInputStream(var1, a(false, var0));
   }

   static {
      Security.addProvider(new BouncyCastleProvider());
   }
}
