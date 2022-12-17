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

import static android.content.Context.MODE_PRIVATE;

import static com.ujs.salarymanagementsystem.data.Constant.enterpriseList;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.adapter.EnterpriseAdapter;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.ujs.salarymanagementsystem.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;


public class HomePageController extends HomeController {
    private static final String TAG = "HomeAbController";
    private static HomeController.HomeControlListener homeControlListener;

    private Banner product_banner;
    private List<String> imageViewList = new ArrayList<>();
    private FragmentHomeBinding binding;
    public static EnterpriseAdapter adapter = null;

    public HomePageController(Context context) throws Exception {
        super(context, false);

        // 加载样式
        @NonNull
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, null,  false);
        this.addView(binding.getRoot());
        product_banner = binding.getRoot().findViewById(R.id.product_banner);

        initBanner();  // 轮播图

        // 公司列表
        initEnterprise();
    }

    @Override
    protected String getTitle() {
        return "主页";
    }

    @Override
    protected ItemAdapter getItemAdapter() {
        List<BaseItemDescription> list = new ArrayList<>();
        return new ItemAdapter(getContext(), list);
    }

    private void initBanner(){
        product_banner.setImageLoader(new GlideImageLoader());
        product_banner.isAutoPlay(false);
        // 下面设置图片的路径
        imageViewList.add("https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png");
        imageViewList.add("https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png");
        imageViewList.add("https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png");
        //设置轮播图 别问为啥这么写，文档要求
        product_banner.setImages(imageViewList);
        product_banner.start();
    }

    private void initEnterprise(){
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.bottom_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());//设置布局模型，LinearLayout默认情况下为纵向布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置为横向布局
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EnterpriseAdapter(Constant.enterpriseList, getContext());
        recyclerView.setAdapter(adapter);
    }

    public static HomeControlListener getListener(){
        return mHomeControlListener;
    }
}
