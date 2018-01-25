package tm.nsfantom.piechart.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import tm.nsfantom.piechart.R;
import tm.nsfantom.piechart.databinding.CombiItemBinding;
import tm.nsfantom.piechart.databinding.CombiListBinding;

public class CombiList extends LinearLayout {

    CombiListBinding layout;
    private String acceptBtnText;
    private String cancelBtnText;
    private String description;
    private int selectAllPosition;
    private boolean multiSelect;
    private ItemsAdapter adapter;

    private AcceptListener acceptListener;
    private CancelListener cancelListener;
    private ClickListener clickListener;

    private SparseBooleanArray selected = new SparseBooleanArray();
    private int selectedPosition = 0;

    public interface AcceptListener {
        void onAccept(String text);
    }

    public interface CancelListener {
        void cancel();
    }

    private interface ClickListener {
        void onClick(int position, boolean isChecked);
    }

    public CombiList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CombiList, 0, 0);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layout = CombiListBinding.inflate(layoutInflater, this, true);
        try {
            multiSelect = a.getBoolean(R.styleable.CombiList_multiSelect, false);
            selectAllPosition = a.getInteger(R.styleable.CombiList_selectAllPosition, 0);
            description = a.getString(R.styleable.CombiList_list_title);
            acceptBtnText = a.getString(R.styleable.CombiList_acceptBtnText);
            cancelBtnText = a.getString(R.styleable.CombiList_cancelBtnText);

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        layout.tvTitle.setText("cfdsvd");
        if (!TextUtils.isEmpty(description)) {
            layout.tvTitle.setVisibility(VISIBLE);
            layout.tvTitle.setText(description);
        } else layout.tvTitle.setVisibility(GONE);

        if (multiSelect) {
            Timber.d("switch multi");
            if (selectAllPosition == 0) {
                Timber.d("switch top");
                layout.footerBtnSelectAll.setVisibility(GONE);
                layout.headerBtnSelectAll.setVisibility(VISIBLE);
                layout.headerBtnSelectAll.setOnClickListener(v -> selectAll());
            } else {
                Timber.d("switch bottom");
                layout.headerBtnSelectAll.setVisibility(GONE);
                layout.footerBtnSelectAll.setVisibility(VISIBLE);
                layout.footerBtnSelectAll.setOnClickListener(v -> selectAll());
            }
        } else {
            layout.footerBtnSelectAll.setVisibility(GONE);
            layout.headerBtnSelectAll.setVisibility(GONE);
        }

        if (acceptListener != null)
            layout.btnAccept.setOnClickListener(v -> acceptListener.onAccept(parseSelection()));
        if (cancelListener != null)
            layout.btnCancel.setOnClickListener(v -> cancelListener.cancel());
        layout.listHolder.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public String getAcceptBtnText() {
        return acceptBtnText;
    }

    public void setAcceptBtnText(String acceptBtnText) {
        this.acceptBtnText = acceptBtnText;
        invalidate();
        requestLayout();
    }

    public String getCancelBtnText() {
        return cancelBtnText;
    }

    public void setCancelBtnText(String cancelBtnText) {
        this.cancelBtnText = cancelBtnText;
        invalidate();
        requestLayout();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        invalidate();
        requestLayout();
    }

    public int getSelectAllPosition() {
        return selectAllPosition;
    }

    public void setSelectAllPosition(int selectAllPosition) {
        this.selectAllPosition = selectAllPosition;
        invalidate();
        requestLayout();
        if (multiSelect)
            this.onFinishInflate();
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
        updateAdapterSelection();
        invalidate();
        requestLayout();
        this.onFinishInflate();
    }

    public void setAcceptListener(AcceptListener acceptListener) {
        this.acceptListener = acceptListener;
        layout.btnAccept.setOnClickListener(v -> acceptListener.onAccept(parseSelection()));
    }

    public void setCancelListener(CancelListener cancelListener) {
        this.cancelListener = cancelListener;
        layout.btnCancel.setOnClickListener(v -> cancelListener.cancel());
    }

    public void setCombiItemList(List<CombiItem> combiItemList) {
        this.adapter = new ItemsAdapter();
        combiItemList.get(0).setChecked(true);
        adapter.setCombiItemList(combiItemList);
        selected = new SparseBooleanArray(combiItemList.size());
        selected.append(0, true);
        clickListener = (position, isChecked) -> {
            //TODO toggle checked
            if (multiSelect) {
                if (isChecked) selected.append(position, isChecked);
                else if (selected.size() > 1) selected.delete(position);
                updateAdapterSelection();
            } else {
                selectedPosition = position;
                updateAdapterSelection();
                Toast.makeText(getContext(), "clicked: " + position, Toast.LENGTH_SHORT).show();
            }
        };
        layout.listHolder.setAdapter(adapter);
    }

    private void selectAll() {
        if (multiSelect) {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                selected.put(i, true);
            }
            updateAdapterSelection();
        }
    }

    private void updateAdapterSelection() {
        if (adapter == null) return;
        if (multiSelect)
            adapter.setSelected(selected);
        else adapter.setSelected(selectedPosition);
    }

    private String parseSelection() {
        if (multiSelect)
            return "selected: ".concat(selected.toString());
        else
            return "selected: ".concat(String.valueOf(selectedPosition));
    }

    public static class CombiItem {
        private boolean checked = false;
        private String text;
        private String description;

        public CombiItem() {
        }

        public CombiItem(String text) {
            this.text = text;
        }

        public CombiItem(String text, String description) {
            this.text = text;
            this.description = description;
        }

        public boolean isChecked() {
            return this.checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getText() {
            return text;
        }

        public String getDescription() {
            return description;
        }

        public CombiItem setText(String text) {
            this.text = text;
            return this;
        }

        public CombiItem setDescription(String description) {
            this.description = description;
            return this;
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemListVH> {

        private List<CombiItem> combiItemList = new ArrayList<>();

        @Override
        public ItemListVH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            CombiItemBinding binding = CombiItemBinding.inflate(inflater, parent, false);
            return new ItemListVH(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(ItemListVH holder, int position) {
            if (position != holder.getAdapterPosition()) return;
            CombiItem combiItem = combiItemList.get(position);
            holder.layout.setCombiItem(combiItem);
            holder.layout.checkBox.setClickable(false);
        }

        public void setCombiItemList(List<CombiItem> combiItemList) {
            this.combiItemList = combiItemList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return combiItemList.size();
        }

        protected void setSelected(SparseBooleanArray selected) {
            List<CombiItem> newList = new ArrayList<>();
            for (int i = 0; i < combiItemList.size(); i++) {
                CombiItem combiItem = combiItemList.get(i);
                combiItem.setChecked(selected.get(i));
                newList.add(i, combiItem);
                Timber.d("selected: %s is %s", i, selected.get(i));
            }
            this.combiItemList.clear();
            this.combiItemList.addAll(newList);
            notifyDataSetChanged();
        }

        protected void setSelected(int selected) {
            Timber.e("set selected");
            List<CombiItem> newList = new ArrayList<>();
            for (int i = 0; i < combiItemList.size(); i++) {
                CombiItem combiItem = combiItemList.get(i);
                combiItem.setChecked(selected == i);
                newList.add(i, combiItem);
                Timber.e("set selected: %s is %s", i, combiItem.isChecked());
            }
            this.combiItemList.clear();
            this.combiItemList.addAll(newList);
            notifyDataSetChanged();
        }

        class ItemListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

            CombiItemBinding layout;
            View view;

            public ItemListVH(View itemView) {
                super(itemView);
                layout = DataBindingUtil.bind(itemView);
                this.view = itemView;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(getAdapterPosition(), !layout.checkBox.isChecked());
            }
        }
    }
}
