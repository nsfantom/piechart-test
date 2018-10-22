package tm.nsfantom.piechart.ui.seekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import tm.nsfantom.piechart.util.DisplayUtil;

public class DiscreteSliderBackdrop extends FrameLayout {

    // region Member Variables
    private Paint strokePaint = new Paint();
    private int tickMarkCount = 0;
    private float tickMarkRadius = 0.0F;
    private int backdropStrokeColor = 0;
    private float backdropStrokeWidth = 0.0F;
    private int discreteSliderBackdropLeftMargin = DisplayUtil.dpToPx(getContext(), 32);
    private int discreteSliderBackdropRightMargin = DisplayUtil.dpToPx(getContext(), 32);
    // endregion

    // region Constructors
    public DiscreteSliderBackdrop(Context context) {
        super(context);
        init(context, null);
    }

    public DiscreteSliderBackdrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiscreteSliderBackdrop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    // endregion

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int interval = (width - (discreteSliderBackdropLeftMargin + discreteSliderBackdropRightMargin)) / (tickMarkCount - 1);

        setUpStrokePaint();

        for (int i = 0; i < tickMarkCount; i++) {

            canvas.drawLine(discreteSliderBackdropLeftMargin + (i * interval), height / 2,
                    discreteSliderBackdropLeftMargin + (i * interval), height / 2 -7f,
                    strokePaint);
        }

    }

    // region Helper Methods
    private void init(Context context, AttributeSet attrs) {
    }

    private void setUpStrokePaint(){
        strokePaint.setColor(backdropStrokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(backdropStrokeWidth);
    }

    public void setTickMarkCount(int tickMarkCount) {
        this.tickMarkCount = tickMarkCount < 2 ? 2 : tickMarkCount;
    }
    public void setTickMarkRadius(float tickMarkRadius) {
        this.tickMarkRadius = tickMarkRadius < 2.0F ? 2.0F : tickMarkRadius;
    }

    public void setBackdropStrokeColor(int backdropStrokeColor) {
        this.backdropStrokeColor = backdropStrokeColor;
    }

    public void setBackdropStrokeWidth(float backdropStrokeWidth) {
        this.backdropStrokeWidth = backdropStrokeWidth < 1.0F ? 1.0F : backdropStrokeWidth;
    }
    // endregion
}
