package tm.nsfantom.piechart.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityScheduleBinding;
import tm.nsfantom.piechart.ui.schedule.ScheduleModel;

public final class ScheduleActivity extends AppCompatActivity {

    private ActivityScheduleBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.scheduleContainer.setModel(
                new ScheduleModel().setTitle("Days")
                        .setPeriods(new String[]{"one", "two", "three","four"})
                        .setDays(new String[]{"S", "M", "T","W","T","F","S"}));
    }

}
