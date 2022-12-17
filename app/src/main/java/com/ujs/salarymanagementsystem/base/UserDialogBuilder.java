package com.ujs.salarymanagementsystem.base;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

public class UserDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
    private Context mContext;
    private EditText oldPwdText, newPwdText, newPwdRPText;

    public UserDialogBuilder(Context context) {
        super(context);
        mContext = context;
    }

    public EditText getoldPwdText() {
        return oldPwdText;
    }

    public EditText getnewPwdText() {
        return newPwdText;
    }

    public EditText getnewPwdRPText() {
        return newPwdRPText;
    }

    @Override
    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = QMUIDisplayHelper.dp2px(mContext, 10);
        layout.setPadding(padding, padding, padding, padding);
        oldPwdText = new EditText(mContext);
        newPwdText = new EditText(mContext);
        newPwdRPText = new EditText(mContext);
        oldPwdText.setHint("原密码");
        newPwdText.setHint("新密码");
        newPwdRPText.setHint("重复新密码");
        oldPwdText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPwdText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPwdRPText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
        oldPwdText.setLayoutParams(editTextLP);
        newPwdText.setLayoutParams(editTextLP);
        newPwdRPText.setLayoutParams(editTextLP);
        layout.addView(oldPwdText);
        layout.addView(newPwdText);
        layout.addView(newPwdRPText);
        return layout;
    }
}
