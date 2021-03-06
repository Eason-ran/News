package com.songhaoran.news.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.songhaoran.news.R;
import com.songhaoran.news.utils.ResourceStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2016/10/11 0011.
 */

public class NewsActivity extends AppCompatActivity {

    //设置CSS样式
    private static final String ARTICLE_STYLE = "<style>img{width:100%;" +
            "}p{text-align:justify}</style>";

    private ResourceStorage resourceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        init();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        }

        resourceStorage = new ResourceStorage(this);
        WebView webView = (WebView) findViewById(R.id.webView);
        WebViewClient webViewClient = new WebViewClient() {
            //这里如果返回的是response，webView就会默认加载返回的图片
            //如果返回的是空，webView就会再次联网获取网络图片
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse resourceResponse;
                String fileName = resourceStorage.getFileName(url);
                File file = new File(resourceStorage.getNewsImgDir(), fileName);
                if (file.exists()) {
                    try {
                        FileInputStream inputStream = new FileInputStream(file);
                        resourceResponse = new WebResourceResponse("image/" + resourceStorage.getFileFormat(fileName), "utf-8", inputStream);
                        return resourceResponse;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.loadDataWithBaseURL(null, ARTICLE_STYLE + getIntent().getStringExtra("html"),
                "text/html", "utf-8", null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
