/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ujs.salarymanagementsystem.base;

import static com.ujs.salarymanagementsystem.data.Constant.enterpriseChartList;
import static com.ujs.salarymanagementsystem.data.Constant.enterpriseList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.data.LoginRepository;
import com.ujs.salarymanagementsystem.databinding.FragmentAboutBinding;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.ui.activity.LoginActivity;
import com.ujs.salarymanagementsystem.ui.activity.WebViewActivity;
import com.ujs.salarymanagementsystem.util.DataCleanManager;
import com.ujs.salarymanagementsystem.util.EnterpriseUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class HomeAboutController extends HomeController {
    private static final String TAG = "HomeAbController";
    int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height);

    public HomeAboutController(Context context) throws Exception {
        super(context, false);
        this.setBackground(getResources().getDrawable(R.drawable.bg_home));
        // 个人信息
        @NonNull
        LayoutInflater inflater = LayoutInflater.from(context);
        FragmentAboutBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_about, null,  false);
        binding.setRepository(LoginRepository.getInstance());
        this.addView(binding.getRoot());
        QMUIRadiusImageView2 image = binding.getRoot().findViewById(R.id.icon);
        image.setBorderColor(getResources().getColor(R.color.app_color_yellow));
        image.setBorderWidth(QMUIDisplayHelper.dp2px(getContext(), 6));

        // 第一栏
        QMUIGroupListView info = binding.getRoot().findViewById(R.id.grouplistview_info);
        QMUICommonListItemView infoItem1 = info.createItemView("修改密码");
        infoItem1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView infoItem2 = info.createItemView("添加企业");
        infoItem2.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView infoItem3 = info.createItemView("退出登录");
        infoItem3.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(context)
                .setTitle("用户")
                .addItemView(infoItem1, v -> {
                    if(!LoginRepository.getInstance().isLoggedIn()){
                        Toast.makeText(context, "尚未登录！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserDialogBuilder userDialogBuilder = new UserDialogBuilder(getContext());
                    userDialogBuilder.setTitle("修改密码")
                            .addAction("取消", (dialog12, index) -> dialog12.dismiss())
                            .addAction("确定", (dialog12, index) -> {
                                dialog12.dismiss();
                                String oldPwd = userDialogBuilder.getoldPwdText().getText().toString();
                                String newPwd = userDialogBuilder.getnewPwdText().getText().toString();
                                String newPwdRP = userDialogBuilder.getnewPwdRPText().getText().toString();
                                if(!newPwd.equals(newPwdRP)){
                                    Toast.makeText(context, "密码重复不正确！", Toast.LENGTH_SHORT).show();
                                }
                                else if(newPwd.length() < 6){
                                    Toast.makeText(context, "密码不少于6位！", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    try {
                                        if(!MySQLoperation.selectUnpbyUid(LoginRepository.getInstance().
                                                getUser().getId_U()).get("passwordd").equals(oldPwd)){
                                            Toast.makeText(context, "原密码不正确！", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            MySQLoperation.updateTableU(LoginRepository.getInstance().getUser(), newPwd);
                                            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();

                })
                .addItemView(infoItem2, v -> {
                    if(!LoginRepository.getInstance().isLoggedIn()){
                        Toast.makeText(context, "尚未登录！", Toast.LENGTH_SHORT).show();
                    }
                    else new EnterpriseUtil().AddEnterprise(context);
                })
                .addItemView(infoItem3, v -> {
                    if(!LoginRepository.getInstance().isLoggedIn()){
                        Toast.makeText(context, "尚未登录！", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        LoginRepository.getInstance().logout();
                        enterpriseList.clear();
                        enterpriseChartList.clear();
                        HomePageController.adapter.notifyDataSetChanged();
                        HomeStatController.adapter.notifyDataSetChanged();
                        Toast.makeText(context, "退出登录！", Toast.LENGTH_SHORT).show();
                    }
                })
                .addTo(info);

        // 第二栏
        QMUIGroupListView options = binding.getRoot().findViewById(R.id.grouplistview_options);
        QMUICommonListItemView opItem1 = options.createItemView(null,
                "清除缓存",
                DataCleanManager.getTotalCacheSize(context),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON,
                height);
        opItem1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView opItem2 = options.createItemView("Github");
        opItem2.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(context)
                .setTitle("操作")
                .addItemView(opItem1, v -> {
                    new QMUIDialog.MessageDialogBuilder(getContext())
                            .setTitle("清除缓存")
                            .setMessage("确定要清除缓存吗？")
                            .addAction("取消", (dialog, index) -> dialog.dismiss())
                            .addAction("确定", (dialog, index) -> {
                                dialog.dismiss();
                                DataCleanManager.clearAllCache(getContext());
                                try {
                                    opItem1.setDetailText(DataCleanManager.getTotalCacheSize(context));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            })
                            .show();
                })
                .addItemView(opItem2, v -> {
                    Intent intent = new Intent(v.getContext(), WebViewActivity.class);
                    intent.putExtra("searchItem", "GitHub");
                    v.getContext().startActivity(intent);
                })
                .addTo(options);

        // 点击图片登录
        QMUIRadiusImageView2 imageView = binding.getRoot().findViewById(R.id.icon);
        imageView.setOnClickListener(view -> {
            if(LoginRepository.getInstance().isLoggedIn()){
                Toast.makeText(App.getContext(), "已经登录！", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(App.getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            App.getContext().startActivity(intent);
        });
    }

    @Override
    protected String getTitle() {
        return "关于";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        List<BaseItemDescription> list = new ArrayList<>();
        return new ItemAdapter(getContext(), list);
    }
}
