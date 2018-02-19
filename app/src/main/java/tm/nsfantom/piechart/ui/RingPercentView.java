package tm.nsfantom.piechart.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import tm.nsfantom.piechart.R;

/**
 * Created by user on 2/17/18.
 */

public class RingPercentView extends FrameLayout {
    private View layout;
    private RingView ringView;

    public RingPercentView(@NonNull Context context) {
        super(context);
    }

    public RingPercentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = layoutInflater.inflate(R.layout.ring_layout, this, true);
        ringView = new RingView(context);
        addView(ringView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public RingPercentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setValue(float percent){
        ringView.setValue(percent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec)
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private class RingView extends View {
        private Paint paint;
        private final Paint paintErase;
        private float percent = 33f;
        private RectF rectF;
        private int delta = 5;
        private int deltaIn = 10;
        private int deltaOut = 10;
        private int valueAngle = 0;
        private int startAngle = 270;
        private int fixAngle = 360 - startAngle;

        // CONSTRUCTOR
        public RingView(Context context) {
            super(context);
            setFocusable(true);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintErase = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintErase.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            rectF = new RectF();
            valueAngle = (int) (360 * (percent / 100));
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }

        public void setValue(float percent) {
            if (percent < 0 && percent > 100) return;
            this.percent = percent;
            valueAngle = (int) (360 * (percent / 100));
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            delta = canvas.getWidth() / 10;

            deltaOut = delta * 2;
            paint.setColor(Color.RED);
            rectF.set(deltaOut, deltaOut, canvas.getWidth() - deltaOut, canvas.getHeight() - deltaOut);
            canvas.drawArc(rectF, valueAngle - fixAngle, 360 - valueAngle, true, paint);

            deltaOut = delta;
            paint.setColor(Color.CYAN);
            rectF.set(deltaOut, deltaOut, canvas.getWidth() - deltaOut, canvas.getHeight() - deltaOut);
            canvas.drawArc(rectF, startAngle, valueAngle, true, paint);

            deltaIn = delta * 3;
            rectF.set(deltaIn, deltaIn, canvas.getWidth() - deltaIn, canvas.getHeight() - deltaIn);
            canvas.drawArc(rectF, valueAngle - fixAngle, 360 - valueAngle, true, paintErase);

            deltaIn = delta * 4;
            rectF.set(deltaIn, deltaIn, canvas.getWidth() - deltaIn, canvas.getHeight() - deltaIn);
            canvas.drawArc(rectF, 0, 360, true, paintErase);

        }
    }
}
