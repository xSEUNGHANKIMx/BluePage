package com.example.bluepage.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.bluepage.R;

public class CustomTextView extends TextView {
    private static final String TAG = CustomTextView.class.getSimpleName();
    private Context mContext;

    public CustomTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public void setColor(final int startPos, final int endPos, final String text) {
        this.post(new Runnable() {

            @Override
            public void run() {
                setText("");

                final SpannableStringBuilder sp = new SpannableStringBuilder(text);
                sp.setSpan(
                    new ForegroundColorSpan(mContext.getResources().getColor(
                        R.color.contacts_list_item_contact_name_matched_text_color)),
                        startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                append(sp);
            }
        });
    }
}
