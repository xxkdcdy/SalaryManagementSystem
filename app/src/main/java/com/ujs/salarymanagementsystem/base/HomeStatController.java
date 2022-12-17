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

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.adapter.EnterpriseChartAdapter;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.ChartItem;
import com.ujs.salarymanagementsystem.databinding.FragmentStatBinding;
import com.ujs.salarymanagementsystem.util.ChartUtil;

import java.util.ArrayList;
import java.util.List;


public class HomeStatController extends HomeController {
    private static final String TAG = "HomeStController";
    private static HomeController.HomeControlListener homeControlListener;
    public static EnterpriseChartAdapter adapter = null;
    private FragmentStatBinding binding;
    private RecyclerView view;

    public HomeStatController(Context context) throws Exception {
        super(context, false);

        // 加载样式
        @NonNull
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_stat, null,  false);
        this.addView(binding.getRoot());

        // 加载layout
        view = binding.getRoot().findViewById(R.id.enterprise_chart_view);
        adapter = new EnterpriseChartAdapter(Constant.enterpriseChartList);
        view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        view.setAdapter(adapter);
    }

    @Override
    protected String getTitle() {
        return "统计";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        List<BaseItemDescription> list = new ArrayList<>();
        return new ItemAdapter(getContext(), list);
    }

    public static HomeControlListener getListener(){
        return mHomeControlListener;
    }
}
