package com.ujs.salarymanagementsystem.base;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.adapter.BaseRecyclerAdapter;

import java.util.List;


public abstract class HomeController extends LinearLayout {

    protected QMUITopBarLayout mTopBar;
    protected RecyclerView mRecyclerView;
    protected Context mContext;

    protected static HomeControlListener mHomeControlListener;
    private ItemAdapter mItemAdapter;
    private int mDiffRecyclerViewSaveStateId = QMUIViewHelper.generateViewId();

    // todo：根据不同界面设计不同样式，传多一个参数表示当前的页面
    public HomeController(Context context, boolean flag) {
        super(context);
        mContext = context;
        setBackgroundColor(getResources().getColor(R.color.background_grey));
        setOrientation(LinearLayout.VERTICAL);
        mTopBar = new QMUITopBarLayout(context);
        mTopBar.setId(View.generateViewId());
        mTopBar.setPadding(0, QMUIStatusBarHelper.getStatusbarHeight(getContext()), 0, 0);
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setId(View.generateViewId());
        addView(mTopBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        initTopBar();
        initRecyclerView();
        if(flag){
            addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1f));
        }
    }

    protected void startFragment(BaseFragment fragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startFragment(fragment);
        }
    }

    public void setHomeControlListener(HomeControlListener homeControlListener) {
        mHomeControlListener = homeControlListener;
    }

    protected abstract String getTitle();

    private void initTopBar() {
        mTopBar.setTitle(getTitle());
    }

    private void initRecyclerView() {
        mItemAdapter = getItemAdapter();

        // 每个界面选项被选中后的跳转
        mItemAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                BaseItemDescription item = mItemAdapter.getItem(pos);
                String itemName = item.getName();
                try {
                    BaseFragment fragment = item.getDemoClass().newInstance();
                    startFragment(fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRecyclerView.setAdapter(mItemAdapter);
        // todo：样式修整
        int spanCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        // mRecyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));
    }

    protected abstract ItemAdapter getItemAdapter();

    public interface HomeControlListener {
        void startFragment(BaseFragment fragment);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        int id = mRecyclerView.getId();
        mRecyclerView.setId(mDiffRecyclerViewSaveStateId);
        super.dispatchSaveInstanceState(container);
        mRecyclerView.setId(id);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        int id = mRecyclerView.getId();
        mRecyclerView.setId(mDiffRecyclerViewSaveStateId);
        super.dispatchRestoreInstanceState(container);
        mRecyclerView.setId(id);
    }

    static class ItemAdapter extends BaseRecyclerAdapter<BaseItemDescription> {

        public ItemAdapter(Context ctx, List<BaseItemDescription> data) {
            super(ctx, data);
        }

        @Override
        public int getItemLayoutId(int viewType) {
            return R.layout.item_enterprise;
        }

        @Override
        public void bindData(RecyclerViewHolder holder, int position, BaseItemDescription item) {
            holder.getTextView(R.id.item_name).setText(item.getName());
            if (item.getBgRes() != 0) {
                holder.setBackground(R.id.card, item.getBgRes());
            }
        }
    }
}
