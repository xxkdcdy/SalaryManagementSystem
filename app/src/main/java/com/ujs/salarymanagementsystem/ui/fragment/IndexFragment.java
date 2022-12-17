package com.ujs.salarymanagementsystem.ui.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentEffectHandler;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.base.CustomEffect;
import com.ujs.salarymanagementsystem.base.HomeAboutController;
import com.ujs.salarymanagementsystem.base.HomeController;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.base.HomeStatController;
import com.ujs.salarymanagementsystem.data.LoginRepository;
import com.ujs.salarymanagementsystem.util.EnterpriseUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexFragment extends BaseFragment {
    private final static String TAG = IndexFragment.class.getSimpleName();

    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabSegment;
    private HashMap<Pager, HomeController> mPages;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        private int mChildCount = 0;

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            HomeController page = mPages.get(Pager.getPagerFromPosition(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(page, params);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount == 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };

    private void initTabs() {
        QMUITabBuilder builder = mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT_BOLD);
        builder.setSelectedIconScale(1.2f)
                .setTextSize(QMUIDisplayHelper.sp2px(getActivity(), 13), QMUIDisplayHelper.sp2px(getActivity(), 15))
                .setDynamicChangeIconColor(false);
        QMUITab component = builder
                .setNormalDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.basic_gunsight))
                .setSelectedDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.basic_gunsight_selected))
                .setText("主页")
                .build(getActivity());
        QMUITab util = builder
                .setNormalDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.basic_server2))
                .setSelectedDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.basic_server2_selected))
                .setText("统计")
                .build(getActivity());
        QMUITab lab = builder
                .setNormalDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.basic_home))
                .setSelectedDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.basic_home_selected))
                .setText("关于")
                .build(getActivity());

        mTabSegment.addTab(component)
                .addTab(util)
                .addTab(lab);
    }

    private void initPagers() throws Exception {

        HomeController.HomeControlListener listener = new HomeController.HomeControlListener() {
            @Override
            public void startFragment(BaseFragment fragment) {
                IndexFragment.this.startFragment(fragment);
            }
        };

        mPages = new HashMap<>();

        // 各个页面
        HomeController homePageController = new HomePageController(getActivity());
        homePageController.setHomeControlListener(listener);
        mPages.put(Pager.HOME, homePageController);

        HomeController homeStatController = new HomeStatController(getActivity());
        homeStatController.setHomeControlListener(listener);
        mPages.put(Pager.STAT, homeStatController);

        HomeController homeAboutController = new HomeAboutController(getActivity());
        homeAboutController.setHomeControlListener(listener);
        mPages.put(Pager.ABOUT, homeAboutController);

        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(LoginRepository.getInstance().isLoggedIn()){
            showDialog();
            new Thread(() -> {
                EnterpriseUtil.SelectEnterprise(LoginRepository.getInstance().getUser(), false);
                //处理完成后给handler发送消息
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }).start();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if(HomePageController.adapter != null) {
                    HomePageController.adapter.notifyDataSetChanged();
                }
                if(HomeStatController.adapter != null) HomeStatController.adapter.notifyDataSetChanged();
                BaseFragment.closeDialog();
            }
        }
    };

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_index, null);
        ButterKnife.bind(this, layout);
        mTabSegment = layout.findViewById(R.id.tabs);
        mViewPager = layout.findViewById(R.id.pager);
        initTabs();
        try {
            initPagers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue("interested_type_key") != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {
                Object value = effect.getValue("interested_value_key");
                if(value instanceof String){
                    Toast.makeText(context, ((String)value), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerEffect(this, new QMUIFragmentEffectHandler<CustomEffect>() {
            @Override
            public boolean shouldHandleEffect(@NonNull CustomEffect effect) {
                return true;
            }

            @Override
            public void handleEffect(@NonNull CustomEffect effect) {
                Toast.makeText(context, effect.getContent(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleEffect(@NonNull List<CustomEffect> effects) {
                // we can only handle the last effect.
                handleEffect(effects.get(effects.size() - 1));
            }
        });
    }

    enum Pager {
        HOME, STAT, ABOUT;

        public static Pager getPagerFromPosition(int position) {
            switch (position) {
                case 0:
                    return HOME;
                case 1:
                    return STAT;
                case 2:
                    return ABOUT;
                default:
                    return HOME;
            }
        }
    }
}