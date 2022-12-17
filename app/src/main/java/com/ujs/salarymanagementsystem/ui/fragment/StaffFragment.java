package com.ujs.salarymanagementsystem.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.adapter.DepartmentAdapter;
import com.ujs.salarymanagementsystem.adapter.StaffAdapter;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.LoginRepository;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.databinding.FragmentDepartmentBinding;
import com.ujs.salarymanagementsystem.databinding.FragmentStaffBinding;
import com.ujs.salarymanagementsystem.util.EnterpriseUtil;
import com.ujs.salarymanagementsystem.util.ScreenshotUtil;
import com.ujs.salarymanagementsystem.util.StaffUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;

public class StaffFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TITLE = "员工信息";
    public static StaffAdapter adapter = null;
    private QMUITopBarLayout mTopBar;
    private FragmentStaffBinding binding;
    private List<Staff> staffList = null;
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StaffFragment() {
        // Required empty public constructor
    }

    public static StaffFragment newInstance(String param1, String param2) {
        StaffFragment fragment = new StaffFragment();
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
                R.layout.fragment_staff, null,  false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        mTopBar = view.findViewById(R.id.topbar);
        initTopBar();
        initStaff();
        return view;
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> popBackStack());
        mTopBar.setTitle("部门：" + Constant.department.getDname());
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
                .addItem("添加员工")
                .addItem("范围查询")
                .addItem("生成工资表")
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    HashSet<String> sites;
                    switch (position) {
                        case 0:
                            if(null == Constant.department) break;
                            new StaffUtil().addStaff(getContext());
                            break;
                        case 1:
                            ScreenshotUtil screenshotUtil = new ScreenshotUtil();
                            showDialog();
                            screenshotUtil.SelectWagesDepartmentByDate(getContext());
                            break;
                        case 2:
                            ScreenshotUtil screenshotUtil1 = new ScreenshotUtil();
                            showDialog();
                            screenshotUtil1.SelectWagesDepartmentByDate_SingleDate(getContext());
                            break;
                    }
                    dialog.dismiss();
                })
                .build().show();
    }

    // 职工信息加载
    private void initStaff(){
        staffList = Constant.staffList;
        recyclerView = binding.getRoot().findViewById(R.id.staff_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());//设置布局模型，LinearLayout默认情况下为纵向布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置为横向布局
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StaffAdapter(staffList, this, getContext());
        recyclerView.setAdapter(adapter);
        StaffUtil.SelectStaff();
    }
}
