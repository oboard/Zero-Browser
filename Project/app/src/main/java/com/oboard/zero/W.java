package com.oboard.zero;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.View.*;
import android.webkit.*;
import java.lang.reflect.*;

public class W extends WebView {

    public WebChromeClient mClient;

    public W(final Context context) {
        super(context);
        init();
    } public W(final Context context, AttributeSet attr) {
        super(context, attr);
        init();
    } void init() {

        //一堆WebView设置
        final WebSettings webSettings = getSettings();

        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > 16) {
			try {
				Class<?> c = Class.forName("android.webkit.WebSettingsClassic");
				Method tt = c.getMethod("setDisplayZoomControls", new Class[] { boolean.class });
				tt.invoke(webSettings, false);
			} catch (ClassNotFoundException e) {
				System.out.println("No such class: " + e);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        //AppCache
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getContext().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath());
        webSettings.setAppCacheMaxSize(5 * 1024 * 1024);

        //Database
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(getContext().getApplicationContext().getDir("db", Context.MODE_PRIVATE).getPath());

        //启用地理定位  
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());

        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(webSettings.LOAD_NO_CACHE);
        webSettings.setDefaultTextEncodingName("UTF-8");

        //图片加载优化
        if (Build.VERSION.SDK_INT >= 19)
            webSettings.setLoadsImagesAutomatically(true);
        else
            webSettings.setLoadsImagesAutomatically(false);

	}
	
    public static String toWeb(String s) {
        String url = s;
        if (!isUri(url))  {
            if (url.indexOf(".") != -1) 
                url = "http://" + url;
            else
                url = "https://so.com/s?q=" + url;
        }
        return url;
    }

    public static boolean isUri(String url) {
        return url.startsWith("http:") ||
            url.startsWith("javascript:") ||
            url.startsWith("content:") ||
            url.startsWith("https:") ||
            url.startsWith("about:") ||
			url.startsWith("file:");
    }
	
}