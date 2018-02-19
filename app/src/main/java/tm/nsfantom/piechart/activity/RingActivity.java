package tm.nsfantom.piechart.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityRingBinding;

public final class RingActivity extends AppCompatActivity {

    private ActivityRingBinding binding;
    private Handler handler = new Handler();
    private boolean started = false;
    private Runnable rTimer = new Runnable() {
        @Override
        public void run() {
            int progress = (int) (System.currentTimeMillis() % 1000);
            binding.seekBar.setProgress(progress);
            binding.ringHolder.setValue(progress/10f);
            if (started)
                handler.postDelayed(this, 19);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ring);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    binding.cbTime.setChecked(false);
                    binding.ringHolder.setValue(seekBar.getProgress() / 10f);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.ringHolder.setValue(seekBar.getProgress() / 10f);
            }
        });
        binding.seekBar.setProgress(333);
        binding.cbTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                handler.post(() -> {started=true;
                    rTimer.run();
                });
            } else {
                started =false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}
