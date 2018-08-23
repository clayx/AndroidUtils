package chay.org.androidutils.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 屏蔽Emoji的输出限制
 * </p>
 **/
public class EmojiExcludeFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            int type = Character.getType(charSequence.charAt(i));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return "";
            }
        }
        return null;
    }
}
