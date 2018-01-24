package tm.nsfantom.piechart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tm.nsfantom.piechart.databinding.ActivityDemoBinding;
import tm.nsfantom.piechart.ui.CombiList;

/**
 * Created by user on 1/24/18.
 */

public class DemoActivity extends AppCompatActivity {
    ActivityDemoBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.combiList.setDescription("Test Title Text");
        binding.switchMulti.setChecked(binding.combiList.isMultiSelect());
        binding.switchMulti.setOnCheckedChangeListener((buttonView, isChecked) -> binding.combiList.setMultiSelect(isChecked));
        binding.switchSelect.setChecked(binding.combiList.getSelectAllPosition()==1);
        binding.switchSelect.setOnCheckedChangeListener((buttonView, isChecked) -> binding.combiList.setSelectAllPosition(isChecked?1:0));
        List<CombiList.CombiItem> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(new CombiList.CombiItem("test"+i,"desc" +i));
        }
        binding.combiList.setCombiItemList(items);

        binding.combiList.setAcceptListener(text ->
                Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_SHORT).show()
        );
        binding.combiList.setCancelListener(() -> Snackbar.make(binding.getRoot(), "It appear like working!", Snackbar.LENGTH_SHORT).show());
        binding.combiList.setCancelBtnText("fake cancel");
    }
}
