package chay.org.androidutils.utils;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

/**
 * Author:Chay
 * Time:2018/8/29 0029
 * <p>
 * KeyStore工具类，存储一些需要加密的字段
 * </p>
 * <p>
 * 使用方式如下：
 * <p>
 * //alias相当于在keystore生成一个别名
 * String alias = "dongfang";
 * //pwd相当于，我们要生成什么的key
 * String pwd = "pwd";
 * try {
 * //判断在keystore中是否相关别名，防止重新创建，生成的相关的alias不一致
 * if (!KeyStoreUtil.isHaveKeyStore(alias)){
 * KeyStoreUtil.creatKeys(this, alias);
 * }
 * //将key签名获取密文（很长），将密文进行MD5加密，可生成短字符串，当成数据AES加密的秘钥
 * String miwen = KeyStoreUtil.sign(pwd, alias);
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * </p>
 **/
public class KeyStoreUtil {

    //TAG
    private static final String TAG = "@KeyStoreUtil@";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void creatKeys(Context context, String alias) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        //结束时间可根据自己项目中的.jks或者.keystore文件获取
        end.add(Calendar.YEAR, 1);
        AlgorithmParameterSpec spec;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=" + alias))
                    .setSerialNumber(BigInteger.valueOf(1337))
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
        } else {
            spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN)
                    .setCertificateSubject(new X500Principal("CN=" + alias))
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM, KeyProperties.BLOCK_MODE_CTR,
                            KeyProperties.BLOCK_MODE_CBC, KeyProperties.BLOCK_MODE_ECB)
                    .setCertificateSerialNumber(BigInteger.valueOf(1337))
                    .setCertificateNotBefore(start.getTime())
                    .setCertificateNotAfter(end.getTime())
                    .build();
        }

        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(KeyStoreEntity.TYPE_RSA, KeyStoreEntity.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        keyPairGenerator.initialize(spec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        L.cLog().e("public key is " + keyPair.getPublic().toString());
        L.cLog().e("private key is " + keyPair.getPrivate().toString());
    }

    public static String sign(String input, String alias) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException,
            UnrecoverableEntryException, InvalidKeyException, SignatureException {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        byte[] inputBytes = input.getBytes();
        KeyStore keyStore = KeyStore.getInstance(KeyStoreEntity.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        keyStore.load(null);
        if (TextUtils.isEmpty(alias)) {
            alias = KeyStoreEntity.SIMPLE_ALIAS;
        }
        KeyStore.Entry entry = keyStore.getEntry(alias, null);
        if (entry == null) {
            L.cLog().e("No key found under alias: " + alias);
            L.cLog().e( "Exiting signData()...");
            return "";
        }

        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            L.cLog().e( "Not an instance of a PrivateKeyEntry");
            L.cLog().e( "Exiting signData()...");
            return "";
        }

        Signature signature = Signature.getInstance(KeyStoreEntity.SIGNATURE_SHA256WITHRSA);
        signature.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
        signature.update(inputBytes);
        byte[] signatureBytes = signature.sign();
        String result = Base64.encodeToString(signatureBytes, Base64.DEFAULT);

        return result;
    }

    public static boolean verify(String input, String signatureStr, String alias)
            throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException,
            UnrecoverableEntryException, InvalidKeyException, SignatureException {
        if (TextUtils.isEmpty(input)) {
            return false;
        }

        if (TextUtils.isEmpty(signatureStr)) {
            L.cLog().e( "Invalid signature.");
            L.cLog().e( "Exiting verifyData()...");
            return false;
        }

        byte[] inputBytes = input.getBytes();

        byte[] signatureBytes;

        signatureBytes = Base64.decode(signatureStr, Base64.DEFAULT);

        KeyStore ks = KeyStore.getInstance(KeyStoreEntity.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        ks.load(null);

        if (TextUtils.isEmpty(alias)) {
            alias = KeyStoreEntity.SIMPLE_ALIAS;
        }

        KeyStore.Entry entry = ks.getEntry(alias, null);
        if (entry == null) {
            L.cLog().w( "No key found under alias: " + alias);
            L.cLog().w( "Exiting verifyData()...");
            return false;
        }

        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            L.cLog().w( "Not an instance of a PrivateKeyEntry");
            return false;
        }

        Signature signature = Signature.getInstance(KeyStoreEntity.SIGNATURE_SHA256WITHRSA);
        signature.initVerify(((KeyStore.PrivateKeyEntry) entry).getCertificate());
        signature.update(inputBytes);
        boolean valid = signature.verify(signatureBytes);
        return valid;
    }

    public static boolean isHaveKeyStore(@Nullable String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStoreEntity.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            keyStore.load(null);
            if (TextUtils.isEmpty(alias)) {
                alias = KeyStoreEntity.SIMPLE_ALIAS;
            }
            KeyStore.Entry entry = keyStore.getEntry(alias, null);
            if (entry == null) {
                return false;
            }
            return true;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }
        return false;
    }


}
