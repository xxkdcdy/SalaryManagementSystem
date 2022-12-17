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
import com.ujs.salarymanagementsystem.data.model.Staff;

public class StaffDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
    private Context mContext;
    private EditText nameText, repoText;
    private Staff staff;

    public StaffDialogBuilder(Context context, Staff staff) {
        super(context);
        mContext = context;
        this.staff = staff;
    }

    public EditText getnameText() {
        return nameText;
    }

    public EditText getrepoText() {
        return repoText;
    }
    

    @Override
    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = QMUIDisplayHelper.dp2px(mContext, 10);
        layout.setPadding(padding, padding, padding, padding);
        nameText = new EditText(mContext);
        repoText = new EditText(mContext);
        nameText.setHint("姓名");
        repoText.setHint("职位");
        if(null != this.staff){
            nameText.setText(this.staff.getSname());
            repoText.setText(this.staff.getSposition());
        }
        LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
        nameText.setLayoutParams(editTextLP);
        repoText.setLayoutParams(editTextLP);
        layout.addView(nameText);
        layout.addView(repoText);
        return layout;
    }
}
