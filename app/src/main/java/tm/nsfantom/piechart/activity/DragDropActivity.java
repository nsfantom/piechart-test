package tm.nsfantom.piechart.activity;

import android.content.ClipData;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityDragdropBinding;

/**
 * Created by user on 2/10/18.
 */

public final class DragDropActivity extends AppCompatActivity {
    private ActivityDragdropBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = DataBindingUtil.setContentView(this, R.layout.activity_dragdrop);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GridTouchListener gridTouchListener = new GridTouchListener();
        GridDragListener gridDragListener = new GridDragListener();
        GridOnLongClickListener gridOnLongClickListener = new GridOnLongClickListener();
        layout.button4.setOnTouchListener(gridTouchListener);
        layout.button5.setOnLongClickListener(gridOnLongClickListener);
        layout.button6.setOnTouchListener(gridTouchListener);
        layout.button7.setOnLongClickListener(gridOnLongClickListener);
        layout.forList.getHolder().setOnDragListener(gridDragListener);
        layout.defaultHolder.setOnDragListener(gridDragListener);
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

    class GridDragListener implements View.OnDragListener {
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
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }
    }
}
