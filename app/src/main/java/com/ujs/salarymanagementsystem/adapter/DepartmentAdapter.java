package com.ujs.salarymanagementsystem.adapter;

import static com.ujs.salarymanagementsystem.App.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.databinding.ItemDepartmentBinding;
import com.ujs.salarymanagementsystem.ui.fragment.StaffFragment;
import com.ujs.salarymanagementsystem.util.DepartmentUtil;
import com.ujs.salarymanagementsystem.util.EnterpriseUtil;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {
    private List<Department> mDepartmentList;
    private static BaseFragment fragment;
    private Context context;

    public DepartmentAdapter(List<Department> mDepartmentList, BaseFragment fragment, Context context) {
        this.fragment = fragment;
        this.mDepartmentList = mDepartmentList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item_department, parent, false);
        final ViewHolder holder = new ViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Department department = mDepartmentList.get( position );
        holder.bind( department );
        // 长按操作
        holder.itemView.setOnLongClickListener(view -> {
            QMUIPopups.quickAction(getContext(),
                            QMUIDisplayHelper.dp2px(getContext(), 56),
                            QMUIDisplayHelper.dp2px(getContext(), 56))
                    .shadow(true)
                    .skinManager(QMUISkinManager.defaultInstance(getContext()))
                    .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20))
                    // 更名
                    .addAction(new QMUIQuickAction.Action().icon(R.drawable.enterprise_rename).onClick(
                            (quickAction, action, position12) -> {
                                quickAction.dismiss();
                                new DepartmentUtil().AlterDepartment(context, position);
                            }
                    ))
                    // 删除
                    .addAction(new QMUIQuickAction.Action().icon(R.drawable.enterprise_delete).onClick(
                            (quickAction, action, position12) -> {
                                quickAction.dismiss();
                                DepartmentUtil.DeleteDepartmentFromIDD(position);
                            }
                    ))
                    .show(holder.itemView);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mDepartmentList.size();
    }

//    @BindingAdapter({"android:src"})
//    public static void setImageResource(ImageView imageView, int resource ) {
//        imageView.setImageResource( resource );
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDepartmentBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            // 点击事件,去到对应企业的详细页面
            ImageView imageView = itemView.findViewById(R.id.item_icon_department);
            imageView.setOnTouchListener((view1, motionEvent) -> {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    imageView.setBackground(getContext().getResources().getDrawable(R.drawable.item_home_selected));
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    imageView.setBackground(getContext().getResources().getDrawable(R.drawable.item_home_normal));
                    Constant.department = mBinding.getDepartment();
                    BaseFragment fragment = new StaffFragment();
                    DepartmentAdapter.fragment.startFragment(fragment);
                }
                return false;
            });
        }

        public void bind(@NonNull Department department ) {
            mBinding.setDepartment(department);
        }
    }
}
