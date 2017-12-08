package com.oboard.zero;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.webkit.*;
import android.widget.*;
import java.lang.reflect.*;
import android.view.animation.*;
import android.graphics.*;
import android.view.View.*;
import java.net.*;

public class M extends Activity {

	static W v;//WebView
	static View tg;//Ground
	static CardView c;//EditText Border
	static EditText t;//URL text
	static ScrollView m;//Menu
	static LinearLayout l;//Menu
	static FrameLayout f, g, r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

		//Find Views
		tg = findViewById(R.id.main_tg);
		t = (EditText) findViewById(R.id.main_t);
		f = (FrameLayout) findViewById(R.id.main_w);
		l = (LinearLayout) findViewById(R.id.main_l);
		g = (FrameLayout) findViewById(R.id.main_ground);
		r = (FrameLayout) f.getParent();
		m = (ScrollView) l.getParent();

		S.init(this, "zero");
		
		//Create WebView
		v = new W(this);
		f.addView(v);

		//Load
		t.setText(S.get("ing", t.getText().toString()));
		v.loadUrl(t.getText().toString());

		//CardView Style
		c = ((CardView)t.getParent());
		
		c.setRound(32);
		c.setColor(Color.argb(20, 0, 0, 0));
		
		//Set Focus
		v.requestFocus();
		keyboardState(false);
		
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
    }

	@Override
    public void onBackPressed() {
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
		if (g.getVisibility() != View.GONE) return;
		tg.setVisibility(View.GONE);
		AlphaAnimation aniA = new AlphaAnimation(0, 1);
		aniA.setInterpolator(new DecelerateInterpolator());
		aniA.setDuration(225);
		g.startAnimation(aniA);
		g.setVisibility(View.VISIBLE);
		
		c.setSeen(false);
		c.invalidate();

		v.clearFocus();
		t.requestFocus();
		t.selectAll();
		
		keyboardState(true);
	}

	public void onBack(View view) {
		if (g.getVisibility() != View.VISIBLE) return;
		tg.setVisibility(View.VISIBLE);
		AlphaAnimation aniA = new AlphaAnimation(1, 0);
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
		t.clearFocus();
		v.requestFocus();
		
		keyboardState(false);
	}

	public void onMenu(View view) {
		//at Menu Button

		switch (view.getId()) {
			case R.id.main_menu0:
				v.reload();
				break;
			case R.id.main_menu1:
				v.loadUrl("https://www.so.com");
				break;
			case R.id.main_menu2:
				if (v.canGoBack())
					v.goBack();
				break;
			case R.id.main_menu3:
				if (v.canGoForward())
					v.goForward();
				break;
			default:
				if (m.getVisibility() == View.GONE) {
					AnimationSet aniA = new AnimationSet(true);
					aniA.addAnimation(new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
															 Animation.RELATIVE_TO_SELF, 0f,
															 Animation.RELATIVE_TO_SELF, -0.5f,
															 Animation.RELATIVE_TO_SELF, 0f));
					aniA.addAnimation(new AlphaAnimation(0.5f, 1));
					aniA.setInterpolator(new DecelerateInterpolator());
					aniA.setDuration(225);
					m.startAnimation(aniA);
					m.setVisibility(View.VISIBLE);
					return;
				}
		}

		//菜单关闭动画
		AnimationSet aniA = new AnimationSet(true);
		aniA.addAnimation(new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
												 Animation.RELATIVE_TO_SELF, 0f,
												 Animation.RELATIVE_TO_SELF, 0f,
												 Animation.RELATIVE_TO_SELF, -0.5f));
		aniA.addAnimation(new AlphaAnimation(1, 0));
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

    public int blendColor(int colorA, int colorB, float ratio) {  
        final float inverseRatio = 1f - ratio;
        float a = (Color.alpha(colorA) * ratio) + (Color.alpha(colorB) * inverseRatio);
        float r = (Color.red(colorA) * ratio) + (Color.red(colorB) * inverseRatio);
        float g = (Color.green(colorA) * ratio) + (Color.green(colorB) * inverseRatio);
        float b = (Color.blue(colorA) * ratio) + (Color.blue(colorB) * inverseRatio);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }
	
	public void changeColor(String url) {
		if (url.contains(":") && url.contains("//") && url.contains("http")) {
			Uri uri = Uri.parse(url);
			int color = Color.WHITE;
			Toast.makeText(this, uri.getHost(), Toast.LENGTH_SHORT).show();
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
			
			//Android 5.0 +
			if (Build.VERSION.SDK_INT >= 21) {
				try {
					Class<?> c = getWindow().getClass();
					Method tt = c.getMethod("setStatusBarColor", new Class[] { int.class });
					tt.invoke(getWindow(), color);
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
			
			color = blendColor(color, Color.WHITE, 0.8f);
			
			r.setBackgroundColor(color);
			l.setBackgroundColor(Color.argb(170, Color.red(color), Color.green(color), Color.blue(color)));
			
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
