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
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.data.model.Wages;
import com.ujs.salarymanagementsystem.databinding.ItemStaffBinding;
import com.ujs.salarymanagementsystem.databinding.ItemWagesBinding;
import com.ujs.salarymanagementsystem.ui.fragment.StaffFragment;
import com.ujs.salarymanagementsystem.util.WagesUtil;

import java.util.List;

public class WagesAdapter extends RecyclerView.Adapter<WagesAdapter.ViewHolder> {
    private List<Wages> mWagesList;
    private Context context;

    public WagesAdapter(List<Wages> mWagesList, Context context) {
        this.mWagesList = mWagesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item_wages, parent, false);
        final ViewHolder holder = new ViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wages wages = mWagesList.get( position );
        holder.bind( wages );
        // 长按操作
        holder.itemView.setOnLongClickListener(view -> {
            QMUIPopups.quickAction(getContext(),
                            QMUIDisplayHelper.dp2px(getContext(), 56),
                            QMUIDisplayHelper.dp2px(getContext(), 56))
                    .shadow(true)
                    .skinManager(QMUISkinManager.defaultInstance(getContext()))
                    .edgeProtection(QMUIDisplayHelper.dp2px(getContext(), 20))
                    // 修改
                    .addAction(new QMUIQuickAction.Action().icon(R.drawable.staff_update).onClick(
                            (quickAction, action, position12) -> {
                                quickAction.dismiss();
                                new WagesUtil().AlterWages(context, position);
                            }
                    ))
                    // 删除
                    .addAction(new QMUIQuickAction.Action().icon(R.drawable.enterprise_delete).onClick(
                            (quickAction, action, position12) -> {
                                quickAction.dismiss();
                                WagesUtil.deleteWagesFromIDW(position);
                            }
                    ))
                    .show(holder.itemView);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mWagesList.size();
    }

//    @BindingAdapter({"android:src"})
//    public static void setImageResource(ImageView imageView, int resource ) {
//        imageView.setImageResource( resource );
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemWagesBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(@NonNull Wages wages ) {
            mBinding.setWages(wages);
        }
    }
}
