package com.oboard.zero;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.widget.*;
import android.graphics.drawable.*;

public class CardView extends FrameLayout {

    private RectF mRoundRect = new RectF();
    private Paint mMaskPaint = new Paint();
    private Paint mZonePaint = new Paint();
	private boolean mSeen = true;
    private float mRadius;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = (2 * getResources().getDisplayMetrics().density + 0.5f);
		setBackgroundColor(Color.WHITE);
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mZonePaint.setAntiAlias(true);
        mZonePaint.setColor(Color.WHITE);
    }
	
	public void setRound(int dip) {
		mRadius = (dip * getResources().getDisplayMetrics().density + 0.5f);
	}

	public void setSeen(boolean can) {
		mSeen = can;
	}
	
	public void setColor(int color) {
		setBackgroundColor(color);
	}
	
    @Override
    public void draw(Canvas canvas) {
		if (!mSeen) {
			super.draw(canvas);
			return;
		}
        canvas.saveLayer(mRoundRect, mZonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(mRoundRect, mRadius, mRadius, mZonePaint);
        canvas.saveLayer(mRoundRect, mMaskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRoundRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

}
