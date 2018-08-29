package chay.org.androidutils.utils;

/**
 * Author:Chay
 * Time:2018/8/29 0029
 * <p>
 * KeyStore使用的一些常量
 * </p>
 **/
public interface KeyStoreEntity {

    String KEYSTORE_PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore";

    String TYPE_RSA = "RSA";
    String TYPE_DSA = "DSA";
    String TYPE_BKS = "BKS";

    String SIGNATURE_SHA256WITHRSA = "SHA256withRSA";
    String SIGNATURE_SHA512WITHRSA = "SHA512withRSA";

    String SIMPLE_ALIAS = "simple";

}
