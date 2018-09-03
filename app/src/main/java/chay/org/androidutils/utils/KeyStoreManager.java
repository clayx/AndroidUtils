package chay.org.androidutils.utils;

import android.content.Context;

/**
 * Author:Chay
 * Time:2018/9/3 0003
 * <p>
 * KeyStore管理类，便于管理keystoreUtil{@link KeyStoreUtil}的使用和操作
 * </p>
 **/
public class KeyStoreManager {

    private static KeyStoreManager instance;

    private KeyStoreManager() {
    }

    public static KeyStoreManager getInstance() {
        if (instance == null) {
            synchronized (KeyStoreManager.class) {
                if (instance == null) {
                    instance = new KeyStoreManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化Alias
     *
     * @param context
     * @param alias
     */
    public void init(Context context, String alias) {

    }


}
