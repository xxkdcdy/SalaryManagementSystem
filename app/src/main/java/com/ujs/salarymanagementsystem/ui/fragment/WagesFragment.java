package com.ujs.salarymanagementsystem.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.adapter.WagesAdapter;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.Wages;
import com.ujs.salarymanagementsystem.databinding.FragmentWagesBinding;
import com.ujs.salarymanagementsystem.ui.activity.ScreenshotActivity;
import com.ujs.salarymanagementsystem.util.ScreenshotUtil;
import com.ujs.salarymanagementsystem.util.WagesUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;

public class WagesFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TITLE = "工资信息";
    public static WagesAdapter adapter = null;
    private QMUITopBarLayout mTopBar;
    private FragmentWagesBinding binding;
    private List<Wages> wagesList = null;
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WagesFragment() {
        // Required empty public constructor
    }

    public static WagesFragment newInstance(String param1, String param2) {
        WagesFragment fragment = new WagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected View onCreateView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_wages, null,  false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        mTopBar = view.findViewById(R.id.topbar);
        initTopBar();
        initWages();
        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> popBackStack());
        mTopBar.setTitle("员工：" + Constant.staff.getSname());
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheet();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // 右侧菜单
    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(getContext())
                .addItem("增加工资信息")
                .addItem("范围查询")
                .addItem("保存报表")
                .addItem("生成工资条")
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    HashSet<String> sites;
                    switch (position) {
                        case 0:
                            if(null == Constant.enterprise || null == Constant.staffList) break;
                            new WagesUtil().addWages(getContext());
                            break;
                        case 1:
                            ScreenshotUtil screenshotUtil = new ScreenshotUtil();
                            showDialog();
                            screenshotUtil.SelectWagesStaffByDate(getContext());
                            break;
                        case 2:
                            Constant.screenshotList = ScreenshotUtil.wages2List(wagesList);
                            Intent intent = new Intent(getContext(), ScreenshotActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            ScreenshotUtil screenshotUtil1 = new ScreenshotUtil();
                            showDialog();
                            screenshotUtil1.SelectWagesStaffByDate_SingleDate(getContext());
                    }
                    dialog.dismiss();
                })
                .build().show();
    }

    // 工资信息加载
    private void initWages(){
        wagesList = Constant.wagesList;
        recyclerView = binding.getRoot().findViewById(R.id.wages_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());//设置布局模型，LinearLayout默认情况下为纵向布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置为横向布局
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WagesAdapter(wagesList, getContext());
        recyclerView.setAdapter(adapter);
        WagesUtil.SelectWages();
//
//        wagesList.add(new Wages(1, 1, 1, "2020-11", 2010.23, 10.19));
//        wagesList.add(new Wages(1, 1, 1, "2014-11", 3010.23, 30.19));
//        wagesList.add(new Wages(1, 1, 1, "2015-11", 4010.23, 50.19));
//        wagesList.add(new Wages(1, 1, 1, "2016-11", 5010.23, 30.19));
//        wagesList.add(new Wages(1, 1, 1, "2020-02", 4010.23, 20.19));
//        wagesList.add(new Wages(1, 1, 1, "2020-06", 3010.23, 10.19));
    }
}
