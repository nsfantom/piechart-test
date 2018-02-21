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

import java.util.ArrayList;
import java.util.HashSet;

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

    public void setChildCount(int count) {
        if (count < 1) return;
        hexView.post(() -> hexView.setCountChild(count));
    }

    private class HexView extends View {
        private Paint paint;
        private int countEdges = 6;
        private int countChild = 4;
        private Point[] hexPoint = new Point[countEdges];
        private Path p = new Path();
        private int radius = 1;
        private double a, b;
        private HashSet<Hexagon> childs = new HashSet<>();
        HashSet<Hexagon> hexsBuff = new HashSet<>();
        ArrayList<Hexagon> hexagons = new ArrayList<>();

        // CONSTRUCTOR
        public HexView(Context context) {
            super(context);
            setFocusable(true);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            //paint.setStrokeWidth(2f);
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

        private void setCountChild(int count) {
            this.countChild = count;
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
            radius = Math.min(canvas.getWidth(), canvas.getHeight()) >> 5;
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

            hexagon.setCenterXY(centerX, centerY)
                    .setHexPoint(hexPoint)
                    .setRadius(radius)
                    .setInitAngle(b);
            drawCenterText(canvas,paint,String.valueOf(hexagon.index),centerX,centerY);

            long dx, dy;
//            childs.clear();
//            childs.add(hexagon);
            p.computeBounds(rf, true);
            hexagons.clear();
            hexagons.add(hexagon);
            while (hexagons.size() < countChild) {
                for (int h = 0; h < hexagons.size(); h++) {
                    Hexagon hex = hexagons.get(h);
                    for (int i = 0; i < hex.getHexPoint().length; i++) {
                        dx = Math.round(((hex.radius * 2) * Math.cos(a * i)) + hex.center.x);
                        dy = Math.round(((hex.radius * 2) * Math.sin(a * i)) + hex.center.y);
                        Hexagon hexC = (Hexagon) hex.clone();
                        hexC.setCenterXY((int)dx, (int)dy).setIndex(hexagons.size());
                        if (!hexagons.contains(hexC)) {
                            hexagons.add(hexC);
                            drawHexagon(canvas, hexC);
                        }
                        if (hexagons.size() == countChild) break;
                    }

                    if (hexagons.size() == countChild) break;
                }
            }


//            p.computeBounds(rf, true);
//            int dx, dy;
//            for (int j = 2; j <= countEdges; j++) {
//                for (int i = 0; i < hexPoint.length; i++) {
//
//                    dx = (int) (radius*j * Math.cos(a * i)) +centerX;
//                    dy = (int) (radius*j * Math.sin(a * i)) +centerY;
//
//                    drawHex(canvas, dx, dy);
//                }
//            }


//            p.computeBounds(rf, true);
//            Matrix scaleMatrix = new Matrix();
//            p.computeBounds(rectF, true);
//            scaleMatrix.setScale(canvas.getWidth()/rf.width(), canvas.getWidth()/rf.width(),rf.centerX(),rf.centerY());
//            p.transform(scaleMatrix);

        }

        private void drawHexagon(Canvas canvas, Hexagon hexagon) {
            p.reset();
            p.moveTo(hexagon.hexPoint[0].x + hexagon.center.x, hexagon.hexPoint[0].y + hexagon.center.y);
            for (int i = 1; i < hexPoint.length; i++) {
                p.lineTo(hexagon.hexPoint[i].x + hexagon.center.x, hexagon.hexPoint[i].y + hexagon.center.y);
            }
            p.close();
            canvas.drawPath(p, paint);
            p.computeBounds(rf, true);
            drawCenterText(canvas,paint,String.valueOf(hexagon.getIndex()),hexagon.center.x,hexagon.center.y);
        }

        Rect rText = new Rect();
        private void drawCenterText(Canvas canvas, Paint paint, String text, int cx, int cy) {
            paint.setTextAlign(Paint.Align.LEFT);
            paint.getTextBounds(text, 0, text.length(), rText);
            float x = cx - rText.width() / 2f - rText.left;
            float y = cy + rText.height() / 2f - rText.bottom;
            canvas.drawText(text, x, y, paint);
        }

        Hexagon hexagon = new Hexagon();

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

    private class Hexagon {

        private Point[] hexPoint = new Point[6];
        private int radius = 1;
        private double initAngle;
        private Point center;
        private int index = 0;

        public Hexagon() {
        }

        public Point[] getHexPoint() {
            return hexPoint;
        }

        public Hexagon addPoint(int num, Point point) {
            hexPoint[num] = point;
            return this;
        }

        public Hexagon setHexPoint(Point[] hexPoint) {
            this.hexPoint = hexPoint;
            return this;
        }

        public Hexagon setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Hexagon setInitAngle(double initAngle) {
            this.initAngle = initAngle;
            return this;
        }

        public Hexagon setCenter(Point center) {
            this.center = center;
            return this;
        }

        public Hexagon setCenterXY(int x, int y) {
            this.center = new Point(x, y);
            return this;
        }

        public Point getCenter() {
            return center;
        }

        public int getIndex() {
            return index;
        }

        public Hexagon setIndex(int index) {
            this.index = index;
            return this;
        }

        public Hexagon incrementIndex(int inc) {
            this.index += inc;
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Hexagon) {
                Point pObg = ((Hexagon) obj).center;
                if (pObg.x % this.center.x < 5 && pObg.y % this.center.y < 5) return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.center.x + this.center.y;
        }

        @Override
        protected Object clone() {
            return new Hexagon().setInitAngle(this.initAngle)
                    .setHexPoint(this.hexPoint)
                    .setIndex(this.index)
                    .setRadius(this.radius);
        }
    }
}
