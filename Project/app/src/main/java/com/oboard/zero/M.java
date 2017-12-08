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

public class M extends Activity {

	static W v;
	static View tg;
	static CardView c;
	static EditText t;
	static ScrollView m;
	static FrameLayout f, g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

		//Find Views
		tg = findViewById(R.id.main_tg);
		t = (EditText) findViewById(R.id.main_t);
		f = (FrameLayout) findViewById(R.id.main_w);
		m = (ScrollView) findViewById(R.id.main_menu);
		g = (FrameLayout) findViewById(R.id.main_ground);

		//Create WebView
		v = new W(this);
		f.addView(v);

		//Load
		v.loadUrl(t.getText().toString());

		//CardView Style
		c = ((CardView)t.getParent());
		
		c.setRound(32);
		c.setColor(Color.argb(20, 0, 0, 0));
		
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
					M.t.setText(url);
                    WebView.HitTestResult hitTestResult = view.getHitTestResult();
                    if (v.isUri(url)) {
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
		AlphaAnimation aniA = new AlphaAnimation(1, 0);
		aniA.setInterpolator(new DecelerateInterpolator());
		aniA.setDuration(225);
		g.startAnimation(aniA);
		g.setVisibility(View.VISIBLE);
		
		ValueAnimation ani = ValueAnimation.ofInt(20, 0);
		ani.setFillAfter(true);
		ani.addUpdateListener(new ValueAnimation.OnAnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimation ani) {
					int value = (int) ani.getAnimatedValue();
					c.setColor(Color.argb(value, 0, 0, 0));
				}
			});
		c.startAnimation(aniA);
		c.setSeen(false);
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
		
		ValueAnimation ani = ValueAnimation.ofInt(0, 20);
		ani.setFillAfter(true);
		ani.addUpdateListener(new ValueAnimation.OnAnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimation ani) {
					int value = (int) ani.getAnimatedValue();
					c.setColor(Color.argb(value, 0, 0, 0));
				}
			});
		t.startAnimation(aniA);
		c.setSeen(true);
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {  
        super.onConfigurationChanged(newConfig);
    }


}
