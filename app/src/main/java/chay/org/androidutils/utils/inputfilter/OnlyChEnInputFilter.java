package chay.org.androidutils.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 只能输入英文和中文的输入框
 * </p>
 **/
public class OnlyChEnInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {
        String editable =
                charSequence.toString();
        if (PatternUtil.stringFilter(editable)) {
            return "";
        } else {
            return null;
        }
    }
}
