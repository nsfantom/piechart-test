package tm.nsfantom.piechart.ui.schedule;

import android.content.Context;
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

public class ScheduleFrameView extends FrameLayout {
    private View layout;
    private TableLayout tableLayout;

    private ScheduleModel model;
    private Map<String, SparseBooleanArray> days = new HashMap<>();
    private TableRow rowHeader;

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
        this.model = model;
        tableLayout.removeAllViews();

        rowHeader = new TableRow(getContext());
        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(model.getTitle());
        rowHeader.addView(tvTitle);

        int rowCount = model.getPeriods().length;
        int columnCount = model.getDays().length;
        if (rowCount == 0) rowCount = 1;

        TableRow tempRow;
        TextView tempTV;
        AppCompatCheckBox checkBox;

        for (int j = 0; j < columnCount; j++) {
            tempTV = new TextView(getContext());
            tempTV.setText(model.getDays()[j]);
            tempTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rowHeader.addView(tempTV);
        }
        tableLayout.addView(rowHeader);
        tableLayout.setStretchAllColumns(true);
        for (int i = 0; i < rowCount; i++) {
            tempRow = new TableRow(getContext());
            tempTV = new TextView(getContext());
            String periodName = model.getPeriods()[i];
            tempTV.setText(periodName);
            tempRow.addView(tempTV);
            days.put(periodName, new SparseBooleanArray());
            for (int j = 0; j < columnCount; j++) {
                checkBox = new AppCompatCheckBox(getContext());
                checkBox.setGravity(Gravity.CENTER);

                final int index = j;

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    days.get(periodName).put(index,isChecked);
                    Toast.makeText(getContext(), "clicked: "+periodName+" item: "+index, Toast.LENGTH_SHORT).show();
                });
                tempRow.addView(checkBox, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
            tableLayout.addView(tempRow);
        }
        layout.invalidate();
    }
}
