package com.ujs.salarymanagementsystem.util;

import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.base.DataPickDialogBuilder;
import com.ujs.salarymanagementsystem.base.DateSearchDialogBuilder;
import com.ujs.salarymanagementsystem.base.WagesDialogBuilder;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.data.model.Wages;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.ui.activity.ScreenshotActivity;
import com.ujs.salarymanagementsystem.ui.fragment.WagesFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenshotUtil {

    // 将工资记录转换成列表数据
    public static List<Map<String, String>> wages2List(List<Wages> wagesList){
        List<Map<String, String>> list = new ArrayList<>();
        Department d = null;
        Staff s = null;
        for(Wages w : wagesList){
            // 根据工资查企业
            try {
                s = MySQLoperation.selectSbyIDS(w.getId_S());
                d = MySQLoperation.selectDbyS(s);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Map<String, String> map = new HashMap<>();
            map.put("name", s.getSname());
            map.put("department", d.getDname());
            map.put("date", String.valueOf(w.getData()));
            map.put("p", w.getAmount_P());
            map.put("d", w.getAmount_D());
            map.put("s", w.getAmount_S());
            list.add(map);
        }
        return list;
    }

    // 根据日期查询某一公司工资
    public void SelectWagesEnterpriseByDate(Context context){
        final List<Wages> list = new ArrayList<>();
        if(null == Constant.enterprise) return;
        DateSearchDialogBuilder dateSearchDialogBuilder = new DateSearchDialogBuilder(context);
        dateSearchDialogBuilder.setTitle("范围查询工资信息")
                .addAction("取消", (dialog12, index) -> {
                    dialog12.dismiss();
                    BaseFragment.closeDialog();
                })
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String st = dateSearchDialogBuilder.getdateStText().getText().toString();
                    String ed = dateSearchDialogBuilder.getdateEdText().getText().toString();
                    int flag = 0;
                    if(null == st || st.length() == 0){
                        // 没有起始日期
                        if(null != ed && ed.length() > 0){
                            // 有终止日期
                            flag = 2;
                        }
                    }
                    else if(null == ed || ed.length() == 0){
                        // 没有终止日期
                        if(null != st && st.length() > 0){
                            // 有起始日期
                            flag = 3;
                        }
                    }
                    else{
                        // 二者都有
                        flag = 1;
                    }
                    switch (flag){
                        case 1:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyBetweendataaEid(Constant.enterprise, st, ed));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        case 2:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbySmalldataaEid(Constant.enterprise, ed));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        case 3:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyLargedataaEid(Constant.enterprise, st));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        default:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyE(Constant.enterprise));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                    }
                })
                .show();
    }

    // 根据日期查询某一公司某个月发下的工资
    public void SelectWagesEnterpriseByDate_SingleDate(Context context){
        final List<Wages> list = new ArrayList<>();
        if(null == Constant.enterprise) return;
        DataPickDialogBuilder dataPickDialogBuilder =new DataPickDialogBuilder(context);
        dataPickDialogBuilder.setTitle("选择月份")
                .addAction("取消", (dialog12, index) -> {
                    dialog12.dismiss();
                    BaseFragment.closeDialog();
                })
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String st = dataPickDialogBuilder.getdateStText().getText().toString();
                    //String ed = dateSearchDialogBuilder.getdateEdText().getText().toString();
                    int flag = 0;
                    switch (flag){

                        case 0:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbydataaEid(Constant.enterprise, st));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        default:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyE(Constant.enterprise));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                    }
                })
                .show();
    }

    // 根据日期查询某一部门某月发下的工资
    public void SelectWagesDepartmentByDate_SingleDate(Context context){
        final List<Wages> list = new ArrayList<>();
        if(null == Constant.enterprise) return;
        DataPickDialogBuilder dataPickDialogBuilder = new DataPickDialogBuilder(context);
        dataPickDialogBuilder.setTitle("选择月份")
                .addAction("取消", (dialog12, index) -> {
                    dialog12.dismiss();
                    BaseFragment.closeDialog();
                })
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String st = dataPickDialogBuilder.getdateStText().getText().toString();
                    //String ed = dateSearchDialogBuilder.getdateEdText().getText().toString();
                    int flag = 0;

                    switch (flag){
                        case 0:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbydataaDid(Constant.department, st));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        default:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyLargedataaDid(Constant.department, ""));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                    }
                })
                .show();
    }

    // 根据日期查询某一部门工资
    public void SelectWagesDepartmentByDate(Context context){
        final List<Wages> list = new ArrayList<>();
        if(null == Constant.enterprise) return;
        DateSearchDialogBuilder dateSearchDialogBuilder = new DateSearchDialogBuilder(context);
        dateSearchDialogBuilder.setTitle("范围查询工资信息")
                .addAction("取消", (dialog12, index) -> {
                    dialog12.dismiss();
                    BaseFragment.closeDialog();
                })
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String st = dateSearchDialogBuilder.getdateStText().getText().toString();
                    String ed = dateSearchDialogBuilder.getdateEdText().getText().toString();
                    int flag = 0;
                    if(null == st || st.length() == 0){
                        // 没有起始日期
                        if(null != ed && ed.length() > 0){
                            // 有终止日期
                            flag = 2;
                        }
                    }
                    else if(null == ed || ed.length() == 0){
                        // 没有终止日期
                        if(null != st && st.length() > 0){
                            // 有起始日期
                            flag = 3;
                        }
                    }
                    else{
                        // 二者都有
                        flag = 1;
                    }
                    switch (flag){
                        case 1:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyBetweendataaDid(Constant.department, st, ed));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        case 2:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbySmalldataaDid(Constant.department, ed));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        case 3:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyLargedataaDid(Constant.department, st));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        default:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyLargedataaDid(Constant.department, ""));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                    }
                })
                .show();
    }

    // 根据日期查询某一员工工资
    public void SelectWagesStaffByDate(Context context){
        final List<Wages> list = new ArrayList<>();
        if(null == Constant.enterprise) return;
        DateSearchDialogBuilder dateSearchDialogBuilder = new DateSearchDialogBuilder(context);
        dateSearchDialogBuilder.setTitle("范围查询工资信息")
                .addAction("取消", (dialog12, index) -> {
                    dialog12.dismiss();
                    BaseFragment.closeDialog();
                })
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String st = dateSearchDialogBuilder.getdateStText().getText().toString();
                    String ed = dateSearchDialogBuilder.getdateEdText().getText().toString();
                    int flag = 0;
                    if(null == st || st.length() == 0){
                        // 没有起始日期
                        if(null != ed && ed.length() > 0){
                            // 有终止日期
                            flag = 2;
                        }
                    }
                    else if(null == ed || ed.length() == 0){
                        // 没有终止日期
                        if(null != st && st.length() > 0){
                            // 有起始日期
                            flag = 3;
                        }
                    }
                    else{
                        // 二者都有
                        flag = 1;
                    }
                    switch (flag){
                        case 1:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyBetweendataaSid(Constant.staff, st, ed));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        case 2:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbySmalldataaSid(Constant.staff, ed));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        case 3:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyLargedataaSid(Constant.staff, st));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        default:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyLargedataaSid(Constant.staff, ""));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                    }
                })
                .show();
    }

    // 工资条
    public void SelectWagesStaffByDate_SingleDate(Context context){
        final List<Wages> list = new ArrayList<>();
        if(null == Constant.enterprise) return;
        DataPickDialogBuilder dataPickDialogBuilder=new DataPickDialogBuilder(context);
        dataPickDialogBuilder.setTitle("选择月份")
                .addAction("取消", (dialog12, index) -> {
                    dialog12.dismiss();
                    BaseFragment.closeDialog();
                })
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String st = dataPickDialogBuilder.getdateStText().getText().toString();
                    //String ed = dateSearchDialogBuilder.getdateEdText().getText().toString();
                    int flag = 0;
                    switch (flag){
                        case 0:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbydataaSid(Constant.staff, st));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        default:
                            new Thread(() ->
                            {
                                try {
                                    list.addAll(MySQLoperation.selectWbyLargedataaSid(Constant.staff, ""));
                                    Constant.screenshotList = wages2List(list);
                                    Intent intent = new Intent(context, ScreenshotActivity.class);
                                    context.startActivity(intent);
                                    BaseFragment.closeDialog();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                    }
                })
                .show();
    }

    // 生成图片
    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }

    // 保存图片
    public static String saveScreenshot(Bitmap bitmap) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        String filename;//声明文件名
        //以保存时间为文件名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        filename = sdf.format(date);
        File file = new File(extStorageDirectory, filename + ".png");//创建文件，第一个参数为路径，第二个参数为文件名
        try {
            outStream = new FileOutputStream(file);//创建输入流
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();
            /**       这三行可以实现相册更新
             Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
             Uri uri = Uri.fromFile(file);intent.setData(uri);
             sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！*/
            Toast.makeText(App.getContext(), "图片保存到：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (Exception e) {
            Toast.makeText(App.getContext(), "exception:" + e, Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
