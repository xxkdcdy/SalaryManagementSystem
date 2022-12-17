package com.ujs.salarymanagementsystem.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.ujs.salarymanagementsystem.R;

public class WebViewActivity extends AppCompatActivity {
    WebView  webView;
    String searchItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否有网络连接
        if(isNetworkConnected(WebViewActivity.this)) {
            setContentView(R.layout.activity_web_view);
            //获取传过来的信息
            Intent intent = getIntent();
            searchItem = intent.getStringExtra("searchItem");
            initViews();
        }
        else{
            QMUIEmptyView mEmptyView = new QMUIEmptyView(WebViewActivity.this);
            mEmptyView.show(false, "详情界面需要网络连接", "请退出并连接网络再试", null, null);
            setContentView(mEmptyView);
        }
    }

    public void initViews(){
        webView = (WebView) findViewById(R.id.webView);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

                if (url == null) {
                    return  false;
                }

                if (url.toString().contains("baike.baidu.com")){
                    view.loadUrl(url);
                    return true;
                }

                try {
                    if (url.startsWith("weixin://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        view.getContext().startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("baike.baidu.com")){
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    }

                    try {
                        if (request.getUrl().toString().startsWith("weixin://")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
                            view.getContext().startActivity(intent);
                            return true;
                        }
                    } catch (Exception e) {
                        return false;
                    }
                }

                return false;
            }

        });
        //解决混合加载模式问题
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setBlockNetworkImage(false);
        if(searchItem.equals("GitHub")){
            webView.loadUrl("https://github.com/xxkdcdy");
        }
        else if(searchItem.equals("feedback")){
            webView.loadUrl("https://support.qq.com/product/381208?d-wx-push=1");
        }
        else {
            webView.loadUrl("https://baike.baidu.com/item/" + searchItem);
        }
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
