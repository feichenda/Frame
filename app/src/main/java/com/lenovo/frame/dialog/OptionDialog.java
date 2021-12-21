package com.lenovo.frame.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.frame.R;
import com.lenovo.frame.customView.OptionButton;

/**
 * @author feizai
 * @date 12/22/2020 022 3:57:18 PM
 */
public class OptionDialog {
    private Context mContext;
    private Dialog dialog;
    private LinearLayout layout;
    private LinearLayout root;
    private OptionButton[] buttons;
    private TextView title;
    private View title_line;
    private Button button;

    private Boolean mShowTitleValue;
    private Integer mPaddingValue;

    public OptionDialog(Context context) {
        this(context, true, 10);
    }

    public OptionDialog(Context context, Boolean showTitleValue) {
        this(context, showTitleValue,10);
    }

    public OptionDialog(Context context, Boolean showTitleValue, Integer paddingValue) {
        mContext = context;
        mShowTitleValue = showTitleValue;
        mPaddingValue = paddingValue;
        initDialog();
        setCancelButtonLinter(v -> {
            cancel();
        });

    }

    private void initDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog, null, false);
        root = view.findViewById(R.id.root);
        layout = view.findViewById(R.id.dialog_layout);
        title = view.findViewById(R.id.title);
        title_line = view.findViewById(R.id.title_line);
        button = view.findViewById(R.id.cancel_button);
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.optiondialogstyle);
        }
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.option_dialog_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnDismissListener((DialogInterface dialog) -> {
            layout.removeAllViews();
        });

        dialog.setOnCancelListener((DialogInterface dialog) -> {
            layout.removeAllViews();
        });

        setPadding(this.mPaddingValue);
    }

    public void addButton(OptionButton... button) {
        buttons = button;
        for (int i = button.length - 1; i >= 0; i--) {
            if (i == 0) {
                button[i].setBackground(mContext.getDrawable(R.drawable.white_bom_radius));
            } else if (i == button.length -1){
                button[i].setBackground(mContext.getDrawable(R.drawable.whitebg));
                if (!mShowTitleValue){
                    button[i].setBackground(mContext.getDrawable(R.drawable.white_top_radius));
                }
            } else {
                button[i].setBackground(mContext.getDrawable(R.drawable.whitebg));
            }
            View view = button[i].getView();
            layout.addView(view);
            if (i != 0) {
                View line = LayoutInflater.from(mContext).inflate(R.layout.layout_cross_line, null, false);
                layout.addView(line);
            }
        }
    }

    public void setTitle(CharSequence text) {
        title.setText(text);
    }

    public void setTitleTxtSize(float size) {
        title.setTextSize(size);
    }

    public void setTitleTxtColor(int textColor) {
        title.setTextColor(textColor);
    }

    public void setTitleTxtColor(ColorStateList textColor) {
        title.setTextColor(textColor);
    }

    public void setTextSize(float textSize) {
        for (OptionButton button : buttons) {
            button.setTextSize(textSize);
        }
    }

    public void setTextColor(int textColor) {
        for (OptionButton button : buttons) {
            button.setTextColor(textColor);
        }
    }

    public void setTextColor(ColorStateList textColor) {
        for (OptionButton button : buttons) {
            button.setTextColor(textColor);
        }
    }

    public void show() {
        if (dialog != null) {
            setShowTitle(this.mShowTitleValue);
            dialog.show();
        }
    }

    public void dismisss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void cancel() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    public void setShowTitle(Boolean value) {
        mShowTitleValue = value;
        title_line.setVisibility(View.GONE);
        title.setVisibility(value ? View.VISIBLE : View.GONE);
        if (buttons != null) {
            if (!value) {
                buttons[buttons.length - 1].setBackground(mContext.getDrawable(R.drawable.white_top_radius));
            } else {
                buttons[buttons.length - 1].setBackground(mContext.getDrawable(R.drawable.whitebg));
            }
            title_line.setVisibility(value ? View.VISIBLE : View.GONE);
        }else {
            title.setBackground(mContext.getDrawable(R.drawable.button_background));
        }
    }

    public Boolean isShowTitle() {
        return this.mShowTitleValue;
    }

    public void setPadding(int value) {
        root.setPadding(value, value, value, value);
    }

    public void setCancelButtonLinter(OptionButton.ButtonOnClickListener listener) {
        button.setOnClickListener(v -> {
            listener.onClick(v);
        });
    }
}
