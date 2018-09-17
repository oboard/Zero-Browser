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
				float ofx = 0;
				if (x < getWidth() / 10) {
					ofx = Math.abs(event.getRawX() - x);
					if (ofx > getWidth() / 6)
						ofx = getWidth() / 6;
					setX(ofx);
				} else if (x > getWidth() * 9 / 10) {
					ofx = -Math.abs(event.getRawX() - x);
					if (ofx < - getWidth() / 6)
						ofx = - getWidth() / 6;
					setX(ofx);
				}
				break;
			case event.ACTION_UP:
				Vibrator vibrator = (Vibrator)W.this.getContext().getSystemService(W.this.getContext().VIBRATOR_SERVICE);
				float ax = getX();
				setX(0);//归位
				if (x < getWidth() / 10) {
					if (ax >= getWidth() / 6) {
						goBack();
						vibrator.vibrate(10);
					}
				} else if (x > getWidth() * 9 / 10) {
					if (ax <= - getWidth() / 6) {
						goForward();
						vibrator.vibrate(10);
					}
				}
				
				AnimationSet aniA = new AnimationSet(true);
				aniA.addAnimation(new TranslateAnimation(ax, 0, 0, 0));
				aniA.setInterpolator(new DecelerateInterpolator());
				aniA.setDuration(225);
				startAnimation(aniA);
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
