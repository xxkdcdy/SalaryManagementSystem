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
import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.base.HomeStatController;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.databinding.ItemEnterpriseBinding;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.ui.fragment.DepartmentFragment;
import com.ujs.salarymanagementsystem.ui.fragment.StaffFragment;
import com.ujs.salarymanagementsystem.util.EnterpriseUtil;

import java.sql.SQLException;
import java.util.List;

public class EnterpriseAdapter extends RecyclerView.Adapter<EnterpriseAdapter.ViewHolder> {
    private List<Enterprise> menterpriseList;
    private Context context;

    public EnterpriseAdapter(List<Enterprise> menterpriseList, Context context) {
        this.menterpriseList = menterpriseList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item_enterprise, parent, false);
        final ViewHolder holder = new ViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Enterprise enterprise = menterpriseList.get( position );
        holder.bind( enterprise );
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
                                new EnterpriseUtil().AlterEnterprise(context, position);
                            }
                    ))
                    // 删除
                    .addAction(new QMUIQuickAction.Action().icon(R.drawable.enterprise_delete).onClick(
                            (quickAction, action, position12) -> {
                                quickAction.dismiss();
                                EnterpriseUtil.DeleteEnterpriseFromIDE(position);
                            }
                    ))
                    .show(holder.itemView);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return menterpriseList.size();
    }

//    @BindingAdapter({"android:src"})
//    public static void setImageResource(ImageView imageView, int resource ) {
//        imageView.setImageResource( resource );
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemEnterpriseBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            // 点击事件,去到对应企业的详细页面
            ImageView imageView = itemView.findViewById(R.id.item_icon_enterprise);
            imageView.setOnTouchListener((view1, motionEvent) -> {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    imageView.setBackground(getContext().getResources().getDrawable(R.drawable.item_home_selected));
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    imageView.setBackground(getContext().getResources().getDrawable(R.drawable.item_home_normal));
                    BaseFragment fragment = new DepartmentFragment();
                    HomePageController.getListener().startFragment(fragment);
                    Constant.enterprise = mBinding.getEnterprise();
                }
                return false;
            });
        }

        public void bind(@NonNull Enterprise enterprise ) {
            mBinding.setEnterprise(enterprise);
        }
    }
}
