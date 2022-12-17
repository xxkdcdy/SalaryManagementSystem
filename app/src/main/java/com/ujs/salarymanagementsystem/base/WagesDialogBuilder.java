package com.ujs.salarymanagementsystem.base;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.ujs.salarymanagementsystem.data.model.Wages;
import com.ujs.salarymanagementsystem.util.WagesUtil;

public class WagesDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
    private Context mContext;
    private EditText dateText, pText, dText;
    private Wages wages;

    public WagesDialogBuilder(Context context, Wages wages) {
        super(context);
        mContext = context;
        this.wages = wages;
    }

    public EditText getDateText() {
        return dateText;
    }

    public EditText getPText() {
        return pText;
    }

    public EditText getDText() {
        return dText;
    }
    

    @Override
    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = QMUIDisplayHelper.dp2px(mContext, 10);
        layout.setPadding(padding, padding, padding, padding);
        dateText = new EditText(mContext);
        pText = new EditText(mContext);
        dText = new EditText(mContext);
        dateText.setHint("发薪日期");
        dateText.setKeyListener(null);   // 禁止弹出输入法
        dateText.setOnClickListener(view -> {
            CustomDateDialog dateDialog = new CustomDateDialog(view.getContext());
            dateDialog.setDate(2000, 0, (year, month) -> {
                dateText.setText(dateDialog.getDate());
            });
            dateDialog.setTitle("选择日期");
            dateDialog.show();
        });
        pText.setHint("应发金额");
        dText.setHint("应扣金额");
        // 只允许输入数字和小数点
        pText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(null != this.wages){
            dateText.setText(wages.getData());
            pText.setText(wages.getAmount_P());
            dText.setText(wages.getAmount_D());
        }
        else{
            dateText.setText(WagesUtil.getNowDateStr());
        }
        LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
        dateText.setLayoutParams(editTextLP);
        pText.setLayoutParams(editTextLP);
        dText.setLayoutParams(editTextLP);
        layout.addView(dateText);
        layout.addView(pText);
        layout.addView(dText);
        return layout;
    }
}
