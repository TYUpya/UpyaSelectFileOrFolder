package vip.upya.lib.sfof;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 子文件列表 适配器 */
@SuppressWarnings("all")
public class SubfileAdapter extends RecyclerView.Adapter<SubfileAdapter.ViewHolder> {

    private List<File> listDatas;
    private Map<String, File> selectedFileMap;
    private boolean isSingleChoice;
    private OnSubfileRVItemClickListener onSubfileRVItemClickListener;

    public SubfileAdapter(boolean isSingleChoice, OnSubfileRVItemClickListener onSubfileRVItemClickListener) {
        this.listDatas = new ArrayList<>();
        this.selectedFileMap = new HashMap<>();
        this.isSingleChoice = isSingleChoice;
        this.onSubfileRVItemClickListener = onSubfileRVItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subfile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = listDatas.get(position);

        holder.itemSfName.setText(file.getName());
        holder.itemSfUpdateTime.setText("更新时间：" + Utils.getTimestampToDate("yyyy-MM-dd HH:mm:ss", file.lastModified()));
        if (file.isFile()) {
            holder.itemSfImage.setImageResource(R.drawable.svg_file);
            holder.itemSfSize.setText("文件大小：" + Utils.formatFileSize(file.length()));
            holder.itemSfSize.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(null);
        } else if (file.isDirectory()) {
            holder.itemSfImage.setImageResource(R.drawable.svg_folder);
            holder.itemSfSize.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                if (onSubfileRVItemClickListener != null)
                    onSubfileRVItemClickListener.onSubfileRVItemClick(position, file);
            });
        } else {
            holder.itemSfImage.setImageResource(R.drawable.svg_unknown_file);
            holder.itemSfSize.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        }

        holder.itemSfCheck.setOnCheckedChangeListener(null);
        holder.itemSfCheck.setChecked(selectedFileMap.containsKey(file.getAbsolutePath()));
        holder.itemSfCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (isSingleChoice)
                    selectedFileMap.clear();

                selectedFileMap.put(file.getAbsolutePath(), file);

                if (isSingleChoice)
                    Utils.mHandler.postDelayed(this::notifyDataSetChanged, 100);
            } else {
                selectedFileMap.remove(file.getAbsolutePath());
            }
            if (onSubfileRVItemClickListener != null)
                onSubfileRVItemClickListener.onSubfileRVItemCBClick();
        });
    }

    @Override
    public int getItemCount() {
        return this.listDatas.size();
    }

    /** 刷新数据 */
    public void onRefreshData(List<File> list) {
        this.listDatas.clear();
        if (list != null && !list.isEmpty())
            this.listDatas.addAll(list);
        notifyDataSetChanged();
    }

    /** 获取选中的数据 */
    public Map<String, File> getSelectedFileMap() {
        return selectedFileMap;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemSfImage;
        TextView itemSfName;
        TextView itemSfSize;
        TextView itemSfUpdateTime;
        CheckBox itemSfCheck;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemSfImage = itemView.findViewById(R.id.itemSfImage);
            itemSfName = itemView.findViewById(R.id.itemSfName);
            itemSfSize = itemView.findViewById(R.id.itemSfSize);
            itemSfUpdateTime = itemView.findViewById(R.id.itemSfUpdateTime);
            itemSfCheck = itemView.findViewById(R.id.itemSfCheck);
        }
    }

    /** RecyclerView Item 监听事件回调 */
    public interface OnSubfileRVItemClickListener {
        /** RecyclerView Item 点击 */
        default void onSubfileRVItemClick(int position, File bean) {
        }

        /** RecyclerView Item CheckBox 点击 */
        default void onSubfileRVItemCBClick() {
        }
    }

}
