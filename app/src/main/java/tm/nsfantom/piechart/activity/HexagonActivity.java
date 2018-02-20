package tm.nsfantom.piechart.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityHexagonBinding;

public final class HexagonActivity extends AppCompatActivity {

    private ActivityHexagonBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hexagon);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    binding.hexagonHolder.setValue(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.hexagonHolder.setValue(seekBar.getProgress());
            }
        });
        binding.seekBar.setProgress(6);
    }
}
