package com.ujs.salarymanagementsystem.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.ujs.salarymanagementsystem.data.model.Wages;

public class DateSearchDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
    private Context mContext;
    private EditText dateStText, dateEdText;

    public DateSearchDialogBuilder(Context context) {
        super(context);
        mContext = context;
    }

    public EditText getdateStText() {
        return dateStText;
    }

    public EditText getdateEdText() {
        return dateEdText;
    }

    @Override
    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = QMUIDisplayHelper.dp2px(mContext, 10);
        layout.setPadding(padding, padding, padding, padding);
        dateStText = new EditText(mContext);
        dateEdText = new EditText(mContext);
        dateStText.setHint("起始日期");
        dateStText.setKeyListener(null);   // 禁止弹出输入法
        dateStText.setOnClickListener(view -> {
            CustomDateDialog dateDialog = new CustomDateDialog(view.getContext());
            dateDialog.setDate(2000, 0, (year, month) -> {
                dateStText.setText(dateDialog.getDate());
            });
            dateDialog.setTitle("选择日期");
            dateDialog.show();
        });
        dateEdText.setHint("结束日期");
        dateEdText.setKeyListener(null);   // 禁止弹出输入法
        dateEdText.setOnClickListener(view -> {
            CustomDateDialog dateDialog = new CustomDateDialog(view.getContext());
            dateDialog.setDate(2000, 0, (year, month) -> {
                dateEdText.setText(dateDialog.getDate());
            });
            dateDialog.setTitle("选择日期");
            dateDialog.show();
        });
        LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
        dateStText.setLayoutParams(editTextLP);
        dateEdText.setLayoutParams(editTextLP);
        layout.addView(dateStText);
        layout.addView(dateEdText);
        return layout;
    }
}
