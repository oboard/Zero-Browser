package oboard.zero;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.view.animation.RotateAnimation;
import android.view.Gravity;
import java.util.ArrayList;

public class M extends Activity {

    static String d_s = "https://www.so.com/s?nav=0&q=";
    static String d_h = "https://www.so.com";
    static boolean d_c = true;

	static int bc = 0xFFFFFF, fc;//backcolor,forecolor

	static W v;//WebView
	static View tg;//Ground
	static CardView c;//EditText Border
	static EditText t;//URL text
	static TextView ti;//title text
	static ScrollView m;//Menu
	static LinearLayout l;//Menu
	static FrameLayout f, g, r;
	static ArrayList<TextView> mm = new ArrayList<TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

		//Find Views
		t = (EditText) findViewById(R.id.main_t);
		tg = findViewById(R.id.main_tg);
		f = (FrameLayout) findViewById(R.id.main_w);
		l = (LinearLayout) findViewById(R.id.main_l);
		g = (FrameLayout) findViewById(R.id.main_ground);
		r = (FrameLayout) f.getParent();
		m = (ScrollView) l.getParent();
		ti = (TextView) findViewById(R.id.main_ti);

		S.init(this, "zero");
        if (S.get("first", true)) {
            S.put("first", false)
                .put("s", d_s)
                .put("h", d_h)
                .put("c", d_c)
                .ok();
        } else {
            M.d_h = S.get("h", d_h);
            M.d_s = S.get("s", d_s);
            M.d_c = S.get("c", d_c);
        }


		//加载菜单
		addMenuItem("刷新", "主页", "前进", "后退", "设置", "+");
		
		//Create WebView
		v = new W(this);
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
		f.addView(v);

		//Load
		t.setText(S.get("ing", t.getText().toString()));
		v.loadUrl(t.getText().toString());

		//CardView Style
		c = ((CardView)t.getParent());

		c.setRound(32);
		c.setColor(Color.argb(20, 0, 0, 0));

		t.setEnabled(false);
        t.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
				@Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event)  {
                    if (actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        String url = view.getText().toString();
                        if (!url.equals("")) {
                            url = W.toWeb(url);
							view.setText(url);
                            //转到页面
                            v.loadUrl(url);
							onBack(null);
                            return true;
                        }
                    }
                    return false;
                }    
            });


        //设置WebClient
        v.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    WebView.HitTestResult hitTestResult = view.getHitTestResult();
                    if (v.isUri(url)) {
						//Colorful
						changeColor(url);
						//Change URL Text
						M.t.setText(url);
						//Save Now URL
						S.put("ing", url).ok();

                        if (hitTestResult == null) {
                            view.loadUrl(url);
                            return true;
                        }
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        } catch (Exception e) {

						}
                        return true;
                    } return super.shouldOverrideUrlLoading(view, url);
                }
				
                public void onPageFinished(WebView v, String url) {
                    super.onPageFinished(v, url);
					//Colorful
					changeColor(url);
					//Change URL Text
					M.t.setText(url);
                    if (!v.getSettings().getLoadsImagesAutomatically())
						v.getSettings().setLoadsImagesAutomatically(true);
                }
            });
        //设置WebChromeClient
		v.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
					ti.setText(title);
                    setTitle(title);
                }
            });

    }

	@Override
    public void onBackPressed() {
        //返回
		if (g.getVisibility() == View.VISIBLE) {
			onBack(null);
			return;
		}
		if (v.canGoBack())
			v.goBack();
		else
			finish();
	}

	public void onEdit(View view) {
        //编辑中
		if (g.getVisibility() != View.GONE) return;
		tg.setVisibility(View.GONE);
		AlphaAnimation aniA = new AlphaAnimation(0, 1);
		aniA.setInterpolator(new DecelerateInterpolator());
		aniA.setDuration(225);
		g.startAnimation(aniA);
		g.setVisibility(View.VISIBLE);

		c.setSeen(false);
		c.invalidate();

		t.setText(v.getUrl());
		t.setEnabled(true);
        t.requestFocus();
        t.selectAll();

		keyboardState(true);
	}

	public void onBack(View view) {
		if (g.getVisibility() != View.VISIBLE) return;
		AlphaAnimation aniA = new AlphaAnimation(1.0f, 0.0f);
		aniA.setInterpolator(new DecelerateInterpolator());
		aniA.setDuration(225);
		aniA.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation ani) {}
				public void onAnimationRepeat(Animation ani) {}
				public void onAnimationEnd(Animation ani) {
					g.setVisibility(View.GONE);
				}
			});
		g.startAnimation(aniA);

		c.setSeen(true);
		c.invalidate();

        t.setSelection(0);
		t.setEnabled(false);
		tg.setVisibility(View.VISIBLE);

		keyboardState(false);
	}

	public void addMenuItem(String...s) {
		for (String ss : s) {
			TextView mi = new TextView(this);
			mi.setTextColor(fc);
			mi.setText(ss);
			mi.setMinHeight(dip2px(48));
			mi.setClickable(true);
			mi.setFocusable(true);
			mi.setGravity(Gravity.CENTER);
			mi.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						onMenu(view);
					}
				});
			l.addView(mi, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			mm.add(mi);
		}
	}

	public void onMenu(View view) {
		//at Menu Button

		if (view instanceof MenuButton) {
			//open menu
			if (m.getVisibility() == View.GONE) {
				AnimationSet aniA = new AnimationSet(true);
				aniA.addAnimation(new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
														 Animation.RELATIVE_TO_SELF, 0.0f,
														 Animation.RELATIVE_TO_SELF, 0.5f,
														 Animation.RELATIVE_TO_SELF, 0.0f));
				aniA.addAnimation(new RotateAnimation(30.0f, 0.0f,
													  Animation.RELATIVE_TO_SELF, 0.5f,
													  Animation.RELATIVE_TO_SELF, 1.0f));
				aniA.addAnimation(new AlphaAnimation(0.5f, 1.0f));
				aniA.setInterpolator(new DecelerateInterpolator());
				aniA.setDuration(225);
				m.startAnimation(aniA);
				m.setVisibility(View.VISIBLE);
				return;
			}
		} else if (view instanceof TextView) {
			switch (((TextView)view).getText().toString()) {
				case "刷新":
					v.reload();
					break;
				case "主页":
					v.loadUrl(d_h);
					break;
				case "后退":
					if (v.canGoBack())
						v.goBack();
					break;
				case "前进":
					if (v.canGoForward())
						v.goForward();
					break;
				case "设置":
					startActivity(new Intent(this, T.class));
					break;
				case "+" :
					addMenuItem("666");
					break;
			}
		}
		//菜单关闭动画
		AnimationSet aniA = new AnimationSet(true);
		aniA.addAnimation(new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
												 Animation.RELATIVE_TO_SELF, 0.0f,
												 Animation.RELATIVE_TO_SELF, 0.0f,
												 Animation.RELATIVE_TO_SELF, 0.5f));
		aniA.addAnimation(new AlphaAnimation(1.0f, 0.0f));
		aniA.setInterpolator(new DecelerateInterpolator());
		aniA.setDuration(225);
		aniA.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation ani) {}
				public void onAnimationRepeat(Animation ani) {}
				public void onAnimationEnd(Animation ani) {
					m.setVisibility(View.GONE);
				}
			});
		m.startAnimation(aniA);
	}

	public void keyboardState(boolean open) {
        InputMethodManager i = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (open)
            i.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        else {
            View view = getWindow().peekDecorView();
            if (view != null) i.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
	
	public int dip2px(float dipValue) {
        return (int)(dipValue * getResources().getDisplayMetrics().density + 0.5f) ;
    }
	
	public static boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return (darkness < 0.5);
    }

    public int blendColor(int colorA, int colorB, float ratio) {  
        final float inverseRatio = 1f - ratio;
        float a = (Color.alpha(colorA) * ratio) + (Color.alpha(colorB) * inverseRatio),
            r = (Color.red(colorA) * ratio) + (Color.red(colorB) * inverseRatio),
            g = (Color.green(colorA) * ratio) + (Color.green(colorB) * inverseRatio),
            b = (Color.blue(colorA) * ratio) + (Color.blue(colorB) * inverseRatio);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

	public void changeColor(String url) {
		if (url.contains(":") && url.contains("//") && url.contains("http")) {
			Uri uri = Uri.parse(url);
			int color = Color.WHITE;
			//Toast.makeText(this, uri.getHost(), Toast.LENGTH_SHORT).show();
            if (d_c) {
                //d_c是变色器的开关
                switch (uri.getHost().replace("www.", "m.")) {
                    case "m.bilibili.com":
                        color = 0xffF06292;
                        break;
                    case "m.coolapk.com":
                        color = 0xff4CAF50;
                        break;
                    case "m.zhihu.com":
                        color = 0xff2196F3;
                        break;
                    case "m.baidu.com":
                        color = 0xffEEEEEE;
                        break;
                    case "news.baidu.com":
                        color = 0xff2196F3;
                        break;
                    case "m.taobao.com":
                        color = 0xffFF5722;
                        break;
                    case "m.tmall.com":
                        color = 0xffE53935;
                        break;
                    case "music.163.com":
                        color = 0xffE53935;
                        break;
                    case "baike.sogou.com":
                        color = 0xff607D8B;
                        break;
                    case "github.com":
                        color = 0xff9E9E9E;
                        break;
                    case "dushu.m.baidu.com":
                        color = 0xffFF5722;
                        break;
                    case "zhihu.sogou.com":
                        color = 0xff2196F3;
                        break;
                    case "outlook.live.com":
                        color = 0xff039BE5;
                        break;
                }
			}
			//Android 5.0 +
			if (Build.VERSION.SDK_INT >= 21) {
				try {
                    getWindow().setStatusBarColor(blendColor(color, Color.BLACK, 0.8f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ti.setBackgroundColor(blendColor(color, Color.BLACK, 0.8f));
			bc = color;

			int light = getResources().getColor(R.color.backcolor), dark = getResources().getColor(R.color.forecolor);
			if (isLightColor(bc)) {
				fc = (dark);
			} else {
				fc = (light);
			}

			t.setTextColor(fc);
			for (int i = 0;i < mm.size(); i++) {
				mm.get(i).setTextColor(fc);
			}

			color = blendColor(color, Color.WHITE, 0.8f);

			r.setBackgroundColor(color);
			l.setBackgroundColor(Color.argb(170, Color.red(color), Color.green(color), Color.blue(color)));

		}
	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction() == null) return;
        switch (intent.getAction()) {
            case Intent.ACTION_SEND:
                v.loadUrl(M.d_s + intent.getStringExtra(Intent.EXTRA_TEXT));
                break;
            case Intent.ACTION_VIEW:
                v.loadUrl(v.toWeb(intent.getData().toString()));
                break;
            case Intent.ACTION_WEB_SEARCH:
                v.loadUrl(M.d_s + intent.getStringExtra("query"));
                break;
        }
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
