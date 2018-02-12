package tm.nsfantom.piechart.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ClipData;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import timber.log.Timber;
import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityDragdropBinding;

/**
 * Created by user on 2/10/18.
 */

public final class DragDropActivity extends AppCompatActivity {
    private float mScale = 1f;
    private final float maxScale = 5f;
    private final float minScale = 0.5f;
    private ActivityDragdropBinding binding;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dragdrop);
        gestureDetector = new GestureDetector(this, new GestureListener());

    }

    @Override
    protected void onStart() {
        super.onStart();
        GridTouchListener gridTouchListener = new GridTouchListener();
        GridDragListener gridDragListener = new GridDragListener();
        GridOnLongClickListener gridOnLongClickListener = new GridOnLongClickListener();
        binding.button4.setOnTouchListener(gridTouchListener);
        binding.button5.setOnLongClickListener(gridOnLongClickListener);
        binding.button6.setOnTouchListener(gridTouchListener);
        binding.button7.setOnLongClickListener(gridOnLongClickListener);
        binding.forList.getHolder().setOnDragListener(gridDragListener);
        binding.defaultHolder.setOnDragListener(gridDragListener);
        initGestureDetector(binding.forList);
        initScaleGestureDetector(binding.forList);
    }

    private void initScaleGestureDetector(View view) {
        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scale = 1 - detector.getScaleFactor();

                float prevScale = mScale;
                mScale += scale;

                if (mScale < minScale) // Minimum scale condition:
                    mScale = minScale;

                if (mScale > maxScale) // Maximum scale condition:
                    mScale = maxScale;

                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f / prevScale, 1f / mScale);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f / prevScale, 1f / mScale);
                ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY);
                AnimatorSet setAnimation = new AnimatorSet();
                setAnimation.play(scaleAnimator);
                setAnimation.start();
                return true;
            }
        });
    }

    private void initGestureDetector(View view) {
        gestureDetector = new GestureDetector(this, new GestureListener()) {
            float dX, dY;

            @Override
            public boolean onTouchEvent(MotionEvent ev) {

                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - ev.getRawX();
                        dY = view.getY() - ev.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveX = ev.getRawX() + dX;
                        float moveY = ev.getRawY() + dY;
                        float width = view.getWidth();                      // binding Width, can be parent
                        float height = view.getHeight();                    // binding Height, can be parent
                        float scaledWidth = width * 1f / mScale;
                        float scaledHeight = height * 1f / mScale;
                        if (1f / mScale <= 1f) {
                            if (moveX + width / 2 - scaledWidth / 2 <= 0)       // left minimum
                                moveX = scaledWidth / 2 - width / 2;
                            if (moveX - (width / 2 - scaledWidth / 2) >= 0)     // right minimum
                                moveX = width / 2 - scaledWidth / 2;
                            if (moveY + height / 2 - scaledHeight / 2 <= 0)     // top minimum
                                moveY = scaledHeight / 2 - height / 2;
                            if (moveY - (height / 2 - scaledHeight / 2) >= 0)   // bottom minimum
                                moveY = height / 2 - scaledHeight / 2;
                        } else {
                            if (moveX + scaledWidth / 2 - width / 2 <= 0)         // left maximum
                                moveX = width / 2 - scaledWidth / 2;
                            if (moveX - (scaledWidth / 2 - width / 2) >= 0)       // right maximum
                                moveX = scaledWidth / 2 - width / 2;
                            if (moveY + scaledHeight / 2 - height / 2 <= 0)         // top maximum
                                moveY = height / 2 - scaledHeight / 2;
                            if (moveY - (scaledHeight / 2 - height / 2) >= 0)       // bottom maximum
                                moveY = scaledHeight / 2 - height / 2;
                        }
                        ObjectAnimator moveOAX = ObjectAnimator.ofFloat(view, "x", moveX);
                        ObjectAnimator moveOAY = ObjectAnimator.ofFloat(view, "y", moveY);
                        moveOAX.setDuration(0);
                        moveOAY.setDuration(0);

                        AnimatorSet scaleDown = new AnimatorSet();
                        scaleDown.play(moveOAX).with(moveOAY);
                        scaleDown.start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        if (mScaleDetector != null)
            mScaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    private final class GridTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class GridOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private final class GridDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(
                R.drawable.shape_droptarget);
        //        Drawable normalShape = getResources().getDrawable(R.drawable.shape_normal); //if was in use
        Drawable normalShape = null;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    //TODO if the same parent
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    LinearLayout container = (LinearLayout) v;
                    owner.removeView(view);
                    container.addView(view,swap((ViewGroup) v,(int)event.getX(),(int)event.getY()));
                    if(v == owner) {
                        Timber.e("the same");
                    }
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }

        private int swap(ViewGroup owner, int x, int y){
            for(int numChildren = owner.getChildCount()-1;numChildren>=0 ; numChildren--) {
                Timber.e("num: %s",numChildren);
                View child = owner.getChildAt(numChildren);
                Rect bounds = new Rect();
                child.getHitRect(bounds);
                if (bounds.contains(x, y)){
                    return numChildren;
                }
            }
            return owner.getChildCount();
        }
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // double tap fired.
            return true;
        }
    }
}
