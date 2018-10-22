package tm.nsfantom.piechart.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityHexagonSeekBinding;
import tm.nsfantom.piechart.ui.seekbar.DiscreteSlider;
import tm.nsfantom.piechart.util.DisplayUtil;

public final class SeekBarActivity extends AppCompatActivity {

    private ActivityHexagonSeekBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hexagon_seek);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    binding.hexagonHolder.setChildCount(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.seekBar.setProgress(6);

        binding.seekBarEdges.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                int childCount = binding.tickMarkLabelsRl.getChildCount();
                for(int i= 0; i<childCount; i++){
                    TextView tv = (TextView) binding.tickMarkLabelsRl.getChildAt(i);
                    if(i == position)
                        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                    else
                        tv.setTextColor(getResources().getColor(R.color.grey_300));
                }
            }
        });

        binding.tickMarkLabelsRl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.tickMarkLabelsRl.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                addTickMarkTextLabels();
            }
        });

    }

    private void addTickMarkTextLabels(){
        int tickMarkCount = binding.seekBarEdges.getTickMarkCount();
        float tickMarkRadius = binding.seekBarEdges.getTickMarkRadius();
        int width = binding.tickMarkLabelsRl.getMeasuredWidth();

        int discreteSliderBackdropLeftMargin = DisplayUtil.dpToPx(getBaseContext(), 32);
        int discreteSliderBackdropRightMargin = DisplayUtil.dpToPx(getBaseContext(), 32);
        float firstTickMarkRadius = tickMarkRadius;
        float lastTickMarkRadius = tickMarkRadius;
        int interval = (width - (discreteSliderBackdropLeftMargin+discreteSliderBackdropRightMargin) - ((int)(firstTickMarkRadius+lastTickMarkRadius)) )
                / (tickMarkCount-1);

        String[] tickMarkLabels = {"$", "$$", "$$$", "$$$$", "$$$$$","$$$","@@","8","99","10","321","aa"};
        int tickMarkLabelWidth = DisplayUtil.dpToPx(getBaseContext(), 40);

        for(int i=0; i<tickMarkCount; i++) {
            TextView tv = new TextView(getBaseContext());

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    tickMarkLabelWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

            tv.setText(tickMarkLabels[i]);
            tv.setTextSize(7f);
            tv.setGravity(Gravity.CENTER);
            if(i==binding.seekBarEdges.getPosition())
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            else
                tv.setTextColor(getResources().getColor(R.color.grey_300));

//                    tv.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

            int left = discreteSliderBackdropLeftMargin + (int)firstTickMarkRadius + (i * interval) - (tickMarkLabelWidth/2);

            layoutParams.setMargins(left, 0, 0, 0);
            tv.setLayoutParams(layoutParams);

            binding.tickMarkLabelsRl.addView(tv);
        }
    }
}
