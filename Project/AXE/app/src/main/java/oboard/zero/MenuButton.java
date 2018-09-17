package oboard.zero;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class MenuButton extends View {

	int c;
	
    public MenuButton(Context context) {
        this(context, null);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
		c = getContext().getResources().getColor(R.color.forecolor);
    }
	
	public void setColor(int color) {
		c = color;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int centreX = getWidth() / 2;
		int height = getHeight() / 10 * 5;
		int circleRadius = height / 10;
		int tranY = (getHeight() - height) / 3;
		Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2f);
		p.setAntiAlias(true);
		p.setColor(c);
		canvas.drawCircle(centreX, height * 1 / 3 + tranY, circleRadius, p);
		canvas.drawCircle(centreX, height * 2 / 3 + tranY, circleRadius, p);
		canvas.drawCircle(centreX, height * 3 / 3 + tranY, circleRadius, p);
	}
	
}
