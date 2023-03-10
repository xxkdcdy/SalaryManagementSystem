package com.ujs.salarymanagementsystem.ui.activity;

import static com.ujs.salarymanagementsystem.App.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.adapter.ScreenshotAdapter;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.util.ChartUtil;
import com.ujs.salarymanagementsystem.util.ScreenshotUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ScreenshotActivity extends AppCompatActivity {

    private RecyclerView view;
    private Bitmap bitmap = null;
    private QMUITopBarLayout mTopBar;
    private String imgPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);
        mTopBar = findViewById(R.id.topbar);
        BarChart chart = findViewById(R.id.chart_wages);
        ChartUtil.initBarChart(chart, ChartUtil.generateData());
        initTopBar();
        initView();
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.setTitle("????????????");
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheet();
                    }
                });
    }

    // ????????????
    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem("????????????")
                .addItem("????????????")
                .addItem("????????????")
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    switch (position) {
                        case 0:
                            bitmap = ScreenshotUtil.shotRecyclerView(this.view);
                            QMUIDialog.CustomDialogBuilder dialogBuilder = new QMUIDialog.CustomDialogBuilder(this);
                            dialogBuilder.setSkinManager(QMUISkinManager.defaultInstance(this));
                            dialogBuilder.setLayout(R.layout.drawablehelper_createfromview);
                            final QMUIDialog mDialog = dialogBuilder.setTitle("????????????????????????????????????").create();
                            ImageView displayImageView = (ImageView) mDialog.findViewById(R.id.createFromViewDisplay);
                            displayImageView.setImageBitmap(bitmap);
                            displayImageView.setOnClickListener(v -> mDialog.dismiss());
                            mDialog.show();
                            break;
                        case 1:
                            bitmap = ScreenshotUtil.shotRecyclerView(this.view);
                            imgPath = ScreenshotUtil.saveScreenshot(bitmap);
                            break;
                        case 2:
                            bitmap = ScreenshotUtil.shotRecyclerView(this.view);
                            imgPath = ScreenshotUtil.saveScreenshot(bitmap);
                            // ???????????????????????????????????????
                            Intent imageIntent = new Intent(Intent.ACTION_SEND);
                            imageIntent.setType("image/jpeg");
                            imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgPath));
                            startActivity(Intent.createChooser(imageIntent, "??????"));
                            break;
                    }
                    dialog.dismiss();
                })
                .build().show();
    }

    private void initView(){
        view = findViewById(R.id.screenshotView);
        //??????RecyclerView?????????
        view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //??????????????????
        List<Map<String, String>> list = Constant.screenshotList;
        ScreenshotAdapter mAdapter = new ScreenshotAdapter(list);
        //???????????????
        view.setAdapter(mAdapter);
    }
}