package tm.nsfantom.piechart.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.ForListBinding;

public final class ForList extends LinearLayout {
    private ForListBinding layout;
    private String headerText;
    private String footerText;
    private int mainColor;
    private int secondColor;
    private int textColor;
    private Listener listener;
    public interface Listener {
        void onAdd();
    }

    public ForList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ForList, 0, 0);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = ForListBinding.inflate(layoutInflater, this, true);
        try {
            headerText = a.getString(R.styleable.ForList_header_text);
            mainColor = a.getColor(R.styleable.ForList_mainColor, Color.DKGRAY);
            footerText = a.getString(R.styleable.ForList_footer_text);
            secondColor =a.getColor(R.styleable.ForList_secondColor, Color.GRAY);
            textColor = a.getColor(R.styleable.ForList_text_color, Color.WHITE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        layout.tvHeader.setTextColor(textColor);
        layout.tvHeader.setText(headerText);
        layout.tvHeader.setBackgroundColor(mainColor);
        layout.btnAdd.setTextColor(textColor);
        layout.btnAdd.setBackgroundColor(secondColor);
        layout.tvFooter.setTextColor(textColor);
        layout.tvFooter.setText(footerText);
        layout.tvFooter.setBackgroundColor(mainColor);


        if (listener != null)
            layout.btnAdd.setOnClickListener(v -> listener.onAdd());
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        layout.btnAdd.setOnClickListener(v -> listener.onAdd());
    }

    public View getHolder(){
        return layout.listHolder;
    }
}
