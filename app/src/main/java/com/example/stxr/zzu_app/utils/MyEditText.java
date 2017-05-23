package com.example.stxr.zzu_app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   MyEditText
 *  创建者:   Stxr
 *  创建时间:  2017/5/5 16:58
 *  描述：    
 */
public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void insertImage(int id) {
        final SpannableString ss = new SpannableString("ss");
        Drawable d = getResources().getDrawable(id);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, "ss".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }
}
