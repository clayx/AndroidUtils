package chay.org.androidutils.utils.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Author:Chay
 * Time:2018/8/23 0023
 * <p>
 * 去除输入框输入的空格
 * </p>
 **/
public class TrimInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {
        if (charSequence.equals(" ")) {
            return "";
        } else {
            return null;
        }
    }
}
