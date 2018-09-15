package oboard.zero;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.widget.*;
import android.graphics.drawable.*;

public class CardView extends FrameLayout {

    private RectF mRoundRect = new RectF();
    private Paint mMaskPaint = new Paint();
    private Paint mZonePaint = new Paint();
	private boolean mSeen;
    private float mRadius;
    private int mColor;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
		mColor = Color.WHITE;
        mRadius = (2 * getResources().getDisplayMetrics().density + 0.5f);
		setBackgroundColor(mColor);
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mZonePaint.setAntiAlias(true);
        mZonePaint.setColor(mColor);
		mSeen = true;
    }
	
	public void setRound(int dip) {
		mRadius = (dip * getResources().getDisplayMetrics().density + 0.5f);
	}

	public void setSeen(boolean can) {
		mSeen = can;
	}
	
	public void setColor(int color) {
		mColor = color;
		setBackgroundColor(color);
	}
	
    @Override
    public void draw(Canvas canvas) {
		if (!mSeen) {
			setBackgroundColor(Color.TRANSPARENT);
			super.draw(canvas);
			setBackgroundColor(mColor);
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
