package tm.nsfantom.piechart.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import tm.nsfantom.piechart.R;

/**
 * Created by user on 2/20/18.
 */

public class HexagonView extends FrameLayout {
    private HexView hexView;
    private View layout;

    public HexagonView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HexagonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(@NonNull Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = layoutInflater.inflate(R.layout.ring_layout, this, true);
        hexView = new HexView(context);
        addView(hexView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public HexagonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setValue(int count) {
        if (count < 3) return;
        hexView.post(() -> hexView.setCountEdges(count));
    }

    private class HexView extends View {
        private Paint paint;
        private int countEdges = 6;
        private Point[] hexPoint = new Point[countEdges];
        private Path p = new Path();
        private int radius = 1;
        private double a, b;

        // CONSTRUCTOR
        public HexView(Context context) {
            super(context);
            setFocusable(true);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2f);
            setLayerType(LAYER_TYPE_HARDWARE, null);
            //for (int i = 0; i < countEdges; i++) hexPoint[i] = new Point(0, 0);
            setCountEdges(countEdges);
        }

        public void setCountEdges(int count) {
            this.countEdges = count;
            hexPoint = new Point[count];
            for (int i = 0; i < count; i++) hexPoint[i] = new Point(0, 0);
            a = ((Math.PI * 2) / countEdges);
            b = (Math.PI * 2) / (countEdges << 1);
            invalidate();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (widthMeasureSpec > heightMeasureSpec)
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            else super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            p.reset();
            paint.setColor(Color.RED);
            radius = Math.min(canvas.getWidth(), canvas.getHeight()) >> 3;
            //canvas.drawCircle(radius, radius, radius, paint);

            for (int i = 0; i < hexPoint.length; i++) {
                hexPoint[i].x = (int) (radius * Math.cos(a * i + b));
                hexPoint[i].y = (int) (radius * Math.sin(a * i + b));
            }

            int centerX = canvas.getWidth() >> 1;
            int centerY = canvas.getHeight() >> 1;
            p.moveTo(hexPoint[0].x + centerX, hexPoint[0].y + centerY);
            for (int i = 1; i < hexPoint.length; i++) {
                p.lineTo(hexPoint[i].x + centerX, hexPoint[i].y + centerY);
            }
            p.close();
            canvas.drawPath(p, paint);
            p.computeBounds(rf, true);
            int dx, dy;

            for (int i = 0; i < hexPoint.length; i++) {

                dx = (int) (radius*2 * Math.cos(a * i)) +centerX;
                dy = (int) (radius*2 * Math.sin(a * i)) +centerY;

                drawHex(canvas, dx, dy);
            }

//            p.computeBounds(rf, true);
//            Matrix scaleMatrix = new Matrix();
//            p.computeBounds(rectF, true);
//            scaleMatrix.setScale(canvas.getWidth()/rf.width(), canvas.getWidth()/rf.width(),rf.centerX(),rf.centerY());
//            p.transform(scaleMatrix);

        }

        private void drawHex(Canvas canvas, int x, int y) {
            p.reset();
            p.moveTo(hexPoint[0].x + x, hexPoint[0].y + y);
            for (int i = 1; i < hexPoint.length; i++) {
                p.lineTo(hexPoint[i].x + x, hexPoint[i].y + y);
            }
            p.close();
            canvas.drawPath(p, paint);
        }

        private Rect r = new Rect();
        private RectF rf = new RectF();

        private void drawCenter(Canvas canvas, Paint paint, String text) {
            canvas.getClipBounds(r);
            int cHeight = r.height();
            int cWidth = r.width();
            //            String text = "Some random text";
//
//            paint.getTextBounds(text, 0, text.length(), bounds);
//
//            text_height =  bounds.height();
//            text_width =  bounds.width();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds(text, 0, text.length(), r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            canvas.drawText(text, x, y, paint);
        }
    }
}
