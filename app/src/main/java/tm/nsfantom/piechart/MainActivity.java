package tm.nsfantom.piechart;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tm.nsfantom.piechart.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        i = new Intent(this, PieChartActivity.class);
//        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();
        binding.btnPieChart.setOnClickListener(v-> startActivity(new Intent(this, PieChartActivity.class)));
        binding.btnCustomCheckList.setOnClickListener(v->startActivity(new Intent(this, DemoActivity.class)));
    }
}
