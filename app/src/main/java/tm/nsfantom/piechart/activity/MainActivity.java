package tm.nsfantom.piechart.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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
    }
}
