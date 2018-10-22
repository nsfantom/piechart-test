package tm.nsfantom.piechart.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }


    @Override
    protected void onStart() {
        super.onStart();

        binding.btnPieChart.setOnClickListener(v-> startActivity(new Intent(this, PieChartActivity.class)));
        binding.btnCustomCheckList.setOnClickListener(v->startActivity(new Intent(this, DemoActivity.class)));
        binding.btnContryList.setOnClickListener(v-> startActivity(new Intent(this, CountryActivity.class)));
        binding.btnNotification.setOnClickListener(v-> startActivity(new Intent(this, NotificationActivity.class)));
        binding.btnDragDrop.setOnClickListener(v-> startActivity(new Intent(this, DragDropActivity.class)));
        binding.btnRing.setOnClickListener(v->startActivity(new Intent(this,RingActivity.class)));
        binding.btnHexagon.setOnClickListener(v->startActivity(new Intent(this,HexagonActivity.class)));
        binding.btnHexagonCustomSeek.setOnClickListener(v->startActivity(new Intent(this, SeekBarActivity.class)));
        binding.btnSchedule.setOnClickListener(v->startActivity(new Intent(this, ScheduleActivity.class)));
    }
}
