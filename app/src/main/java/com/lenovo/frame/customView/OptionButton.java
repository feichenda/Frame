package com.lenovo.frame.customView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.lenovo.frame.R;


/**
 * @author feizai
 * @date 2021/5/30 0030 PM 4:26:11
 */
public class OptionButton {
    private String name;
    private Color color;
    private ButtonOnClickListener listener;
    private Context mContext;
    private Button mButton;
    private View view;

    public OptionButton(Context context, String name, ButtonOnClickListener listener) {
        mContext = context;
        this.name = name;
        this.listener = listener;
        this.view = initButton();
    }

    private View initButton() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_button, null, false);
        mButton = view.findViewById(R.id.button);
        mButton.setText(name);
        mButton.setOnClickListener(v -> {
            listener.onClick(v);
        });
        return view;
    }

    public void setText(CharSequence text) {
        mButton.setText(text);
    }

    public void setTextSize(float textSize) {
        mButton.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        mButton.setTextColor(textColor);
    }

    public void setTextColor(ColorStateList textColor) {
        mButton.setTextColor(textColor);
    }

    public void setBackgroundResource(int resId) {
        mButton.setBackgroundResource(resId);
    }

    public void setBackground(Drawable background) {
        mButton.setBackground(background);
    }

    public View getView() {
        return view;
    }

    public interface ButtonOnClickListener{
        void onClick(View view);
    }
}
