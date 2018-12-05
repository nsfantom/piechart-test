package tm.nsfantom.piechart.ui.schedule;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.util.DisplayUtil;

public class ScheduleFrameView extends FrameLayout {
    private View layout;
    private TableLayout tableLayout;

    private Map<String, SparseBooleanArray> days = new HashMap<>();

    public ScheduleFrameView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScheduleFrameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScheduleFrameView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = layoutInflater.inflate(R.layout.schedule_table, this, true);
        tableLayout = layout.findViewById(R.id.tableContainer);
    }

    public void setModel(ScheduleModel model) {
        tableLayout.removeAllViews();

        Resources resources = getResources();
        TableRow rowHeader = new TableRow(getContext());
        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(model.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvTitle.setTextAppearance(R.style.ScheduleDaysHoliday);
        }
        // TODO: 12/5/18 set font style for old API
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvTitle.setHeight(DisplayUtil.dpToPx(resources, 35));
        rowHeader.addView(tvTitle);

        int rowCount = model.getPeriods().length;
        int columnCount = model.getDays().length;
        if (rowCount == 0) rowCount = 1;

        TableRow tempRow;
        TextView tempTV;
        AppCompatCheckBox checkBox;
        TableRow.LayoutParams cbLayoutParams;
        int margin = DisplayUtil.dpToPx(resources, 1);
        String text;
        for (int j = 0; j < columnCount; j++) {
            tempTV = new TextView(getContext());
            text = model.getDays()[j];
            tempTV.setText(text);
            tempTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tempTV.setGravity(Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (text.toLowerCase().equals("s"))
                    tempTV.setTextAppearance(R.style.ScheduleDaysHoliday);
                else tempTV.setTextAppearance(R.style.ScheduleDays);
            }
            // TODO: 12/5/18 set font style for old API
            rowHeader.addView(tempTV);
        }
        tableLayout.addView(rowHeader);
        tableLayout.setColumnStretchable(0, true);
        tableLayout.setColumnStretchable(1, false);
        for (int i = 0; i < rowCount; i++) {
            tempRow = new TableRow(getContext());
            tempTV = new TextView(getContext());
            String periodName = model.getPeriods()[i];
            tempTV.setText(periodName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tempTV.setTextAppearance(R.style.ScheduleDaysPeriod);
            }
            // TODO: 12/5/18 set font style for old API
            tempTV.setBackground(resources.getDrawable(R.drawable.schedule_background));
            tempTV.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            tempTV.setPadding(DisplayUtil.dpToPx(resources, 10), 0, 0, 0);

            tempRow.addView(tempTV, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
            days.put(periodName, new SparseBooleanArray());
            for (int j = 0; j < columnCount; j++) {
                checkBox = new AppCompatCheckBox(getContext());

                checkBox.setGravity(Gravity.CENTER);
                checkBox.setButtonDrawable(null);
                checkBox.setBackground(resources.getDrawable(R.drawable.checkbox_back));


                final int index = j;

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    days.get(periodName).put(index, isChecked);
                    Toast.makeText(getContext(), "clicked: " + periodName + " item: " + index, Toast.LENGTH_SHORT).show();
                });
                tempRow.addView(checkBox);
                cbLayoutParams = (TableRow.LayoutParams) checkBox.getLayoutParams();
                cbLayoutParams.setMargins(0, 0, margin, margin);
                checkBox.setLayoutParams(cbLayoutParams);

            }
            tableLayout.addView(tempRow);
        }
        layout.invalidate();
    }
}
