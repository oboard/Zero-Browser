package oboard.zero;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class W extends WebView {

    public WebChromeClient mClient;


    public W(Context context) {
        this(context, null);
    }

    public W(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public W(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //一堆WebView设置
        final WebSettings webSettings = getSettings();

        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > 16) {
			try {
				Class<?> c = webSettings.getClass();
				Method tt = c.getMethod("setDisplayZoomControls", new Class[] { boolean.class });
				tt.invoke(webSettings, false);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            webSettings.setMixedContentMode(webSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        //图片加载优化
        if (Build.VERSION.SDK_INT >= 19)
            webSettings.setLoadsImagesAutomatically(true);
        else
            webSettings.setLoadsImagesAutomatically(false);

    }
	int x = 0, y = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
			case event.ACTION_DOWN:
				x = (int)event.getRawX();
				y = (int)event.getY();
				break;
			case event.ACTION_MOVE:
				if (x < getWidth() / 10) {
					setX(event.getRawX() - x);
				}
				break;
			case event.ACTION_UP:
				if (x < getWidth() / 10) {
					float ax = getX();
					setX(0);//归位
					if (event.getRawX() - x > getWidth() / 8) {
						AnimationSet aniA = new AnimationSet(true);
						aniA.addAnimation(new TranslateAnimation(ax, 0, 0, 0));
						aniA.setInterpolator(new DecelerateInterpolator());
						aniA.setDuration(225);
						aniA.setAnimationListener(new Animation.AnimationListener() {
								public void onAnimationStart(Animation ani) {}
								public void onAnimationRepeat(Animation ani) {}
								public void onAnimationEnd(Animation ani) {
									W.this.goBack();
									Vibrator vibrator = (Vibrator)W.this.getContext().getSystemService(W.this.getContext().VIBRATOR_SERVICE);
									vibrator.vibrate(10);
								}
							});
						startAnimation(aniA);
					}
				}
				break;
		}
		return super.onTouchEvent(event);
	}



    public static String toWeb(String s) {
        String url = s;
        if (!isUri(url))  {
            if (url.indexOf(".") != -1) 
                url = "http://" + url;
            else
                url = M.d_s + url;
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
