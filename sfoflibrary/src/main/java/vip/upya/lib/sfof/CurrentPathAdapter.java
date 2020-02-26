package vip.upya.lib.sfof;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** 当前文件路径 适配器 */
@SuppressWarnings("all")
public class CurrentPathAdapter extends RecyclerView.Adapter<CurrentPathAdapter.ViewHolder> {

    private List<File> listDatas;
    private OnCurrentPathRVItemClickListener onCurrentPathRVItemClickListener;

    public CurrentPathAdapter(OnCurrentPathRVItemClickListener onCurrentPathRVItemClickListener) {
        this.listDatas = new ArrayList<>();
        this.onCurrentPathRVItemClickListener = onCurrentPathRVItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_path, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = listDatas.get(position);
        holder.itemCpText.setText(position == 0 ? "SD卡" : file.getName());
        holder.itemCpText.setOnClickListener(v -> {
            if (onCurrentPathRVItemClickListener != null)
                onCurrentPathRVItemClickListener.onCurrentPathRVItemClick(position, file);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemCpText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCpText = itemView.findViewById(R.id.itemCpText);
        }
    }

    /** RecyclerView Item 监听事件回调 */
    public interface OnCurrentPathRVItemClickListener {
        /** RecyclerView Item 单击 */
        default void onCurrentPathRVItemClick(int position, File bean) {
        }
    }

}
