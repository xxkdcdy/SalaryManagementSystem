package com.ujs.salarymanagementsystem.adapter;

import static com.ujs.salarymanagementsystem.App.getContext;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.databinding.ItemDepartmentBinding;
import com.ujs.salarymanagementsystem.databinding.ItemStaffBinding;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.ui.fragment.DepartmentFragment;
import com.ujs.salarymanagementsystem.ui.fragment.StaffFragment;
import com.ujs.salarymanagementsystem.ui.fragment.WagesFragment;
import com.ujs.salarymanagementsystem.util.EnterpriseUtil;
import com.ujs.salarymanagementsystem.util.StaffUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {
    private List<Staff> mStaffList;
    private static BaseFragment fragment;
    private Context context;

    public StaffAdapter(List<Staff> mStaffList, BaseFragment fragment, Context context) {
        this.mStaffList = mStaffList;
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item_staff, parent, false);
        final ViewHolder holder = new ViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Staff staff = mStaffList.get( position );
        holder.bind( staff );
        // 长按操作
        holder.itemView.setOnLongClickListener(view -> {
            QMUIPopups.quickAction(getContext(),
                            QMUIDisplayHelper.dp2px(getContext(), 56),
                            QMUIDisplayHelper.dp2px(getContext(), 56))
                    .shadow(true)
                    .skinManager(QMUISkinManager.defaultInstance(getContext()))
                    .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20))
                    // 更名
                    .addAction(new QMUIQuickAction.Action().icon(R.drawable.staff_update).onClick(
                            (quickAction, action, position12) -> {
                                quickAction.dismiss();
                                AlterStaffDialog(holder.itemView, position, staff);
                            }
                    ))
                    // 删除
                    .addAction(new QMUIQuickAction.Action().icon(R.drawable.enterprise_delete).onClick(
                            (quickAction, action, position12) -> {
                                quickAction.dismiss();
                                StaffUtil.DeleteStaffFromIDS(position);
                            }
                    ))
                    .show(holder.itemView);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mStaffList.size();
    }

//    @BindingAdapter({"android:src"})
//    public static void setImageResource(ImageView imageView, int resource ) {
//        imageView.setImageResource( resource );
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemStaffBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

            // 点击事件,去到对应企业的详细页面
            ImageView imageView = itemView.findViewById(R.id.item_icon_staff);
            imageView.setOnTouchListener((view1, motionEvent) -> {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    imageView.setBackground(getContext().getResources().getDrawable(R.drawable.item_home_selected));
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    imageView.setBackground(getContext().getResources().getDrawable(R.drawable.item_home_normal));
                    Constant.staff = mBinding.getStaff();
                    WagesFragment fragment = new WagesFragment();
                    StaffAdapter.fragment.startFragment(fragment);
                }
                return false;
            });
        }

        public void bind(@NonNull Staff staff ) {
            mBinding.setStaff(staff);
        }
    }

    // 换部门的单选对话框
    public void CheckableDialogBuilder(Staff staff) {
        if(null == Constant.departmentList || null == Constant.department) return;
        String[] itemList = new String[Constant.departmentList.size()];
        for(int i = 0; i < Constant.departmentList.size(); i++){
            itemList[i] = Constant.departmentList.get(i).getDname();
        }
        final int checkIndex = Constant.departmentList.indexOf(Constant.department);
        new QMUIDialog.CheckableDialogBuilder(context)
                .addItems(itemList, (dialog, which) -> {
                    try {
                        MySQLoperation.updateTable_S_3(staff, Constant.departmentList.get(which));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    StaffUtil.SelectStaff();
                    Toast.makeText(context,staff.getSname() + "已被调整到" +
                            Constant.departmentList.get(which).getDname(),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setCheckedIndex(checkIndex)
                .show();
    }

    // 修改信息相关的对话框
    private void AlterStaffDialog(View view, int position, Staff staff){
        QMUIPopups.quickAction(getContext(),
                        QMUIDisplayHelper.dp2px(getContext(), 56),
                        QMUIDisplayHelper.dp2px(getContext(), 56))
                .shadow(true)
                .skinManager(QMUISkinManager.defaultInstance(getContext()))
                .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.staff_1).onClick(
                        // 修改信息
                        (quickAction3, action3, position123) -> {
                            quickAction3.dismiss();
                            new StaffUtil().AlterStaff(context, position);
                        }
                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.staff_2).onClick(
                        // 修改部门
                        (quickAction3, action3, position123) -> {
                            quickAction3.dismiss();
                            CheckableDialogBuilder(staff);
                        }
                ))
                .show(view);
    }
}
