/*

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

    implementation 'androidx.recyclerview:recyclerview:1.1.0'

*/
package vip.upya.lib.sfof;

import android.app.Activity;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 挑选文件或文件夹 弹窗 */
public class SelectFileOrFolderDialog extends UpyaBaseDialog implements View.OnClickListener, View.OnLongClickListener, CurrentPathAdapter.OnCurrentPathRVItemClickListener, SubfileAdapter.OnSubfileRVItemClickListener {

    private ImageView dialogSfofClose;
    private ImageView dialogSfofSelectAll;
    private ImageView dialogSfofFinish;
    private RecyclerView dialogSfofCurrentPathRView;
    private RecyclerView dialogSfofFilesRView;

    private CurrentPathAdapter mCurrentPathAdapter;
    private SubfileAdapter mSubfileAdapter;
    private List<File> mCurrentFiles = new ArrayList<>();
    private List<File> mSubFiles = new ArrayList<>();
    private Map<String, Integer> mListPositions = new HashMap<>();
    private OnSelectFileOrFolderListener mOnSelectFileOrFolderListener;

    public SelectFileOrFolderDialog(Activity activity, boolean isSingleChoice, OnSelectFileOrFolderListener onSelectFileOrFolderListener) {
        super(activity, R.layout.dialog_select_file_or_folder, DIALOG_MODE_BOTTOM);
        this.mOnSelectFileOrFolderListener = onSelectFileOrFolderListener;
        setCancelable(false);
        getWindow().setWindowAnimations(R.style.UpyaAnimSelectFileOrFolderDialog);
        dialogSfofClose = findViewById(R.id.dialogSfofClose);
        dialogSfofSelectAll = findViewById(R.id.dialogSfofSelectAll);
        dialogSfofFinish = findViewById(R.id.dialogSfofFinish);
        dialogSfofCurrentPathRView = findViewById(R.id.dialogSfofCurrentPathRView);
        dialogSfofFilesRView = findViewById(R.id.dialogSfofFilesRView);
        dialogSfofClose.setOnClickListener(this);
        dialogSfofSelectAll.setOnClickListener(this);
        dialogSfofFinish.setOnClickListener(this);
        dialogSfofClose.setOnLongClickListener(this);
        dialogSfofSelectAll.setOnLongClickListener(this);
        dialogSfofFinish.setOnLongClickListener(this);

        dialogSfofSelectAll.setVisibility(isSingleChoice ? View.GONE : View.VISIBLE);
        changeSfofSelectAllState(false);
        changeSfofFinishState(false);

        dialogSfofCurrentPathRView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        dialogSfofCurrentPathRView.setAdapter(mCurrentPathAdapter = new CurrentPathAdapter(this));
        dialogSfofFilesRView.setLayoutManager(new LinearLayoutManager(activity));
        dialogSfofFilesRView.setAdapter(mSubfileAdapter = new SubfileAdapter(isSingleChoice, this));

        Utils.mHandler.post(() -> {
            File sdFile = Environment.getExternalStorageDirectory();
            loadFiles(sdFile);
        });

        setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_UP) {
                // TODO 点击返回键
                return false;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialogSfofClose) {
            dismiss();
        } else if (v.getId() == R.id.dialogSfofSelectAll) {
            boolean isSelectAll = (boolean) dialogSfofSelectAll.getTag();
            Map<String, File> selectedFileMap = mSubfileAdapter.getSelectedFileMap();

            for (File subFile : mSubFiles) {
                if (isSelectAll) { // 需要全不选
                    selectedFileMap.remove(subFile.getAbsolutePath());
                } else { // 需要全选
                    selectedFileMap.put(subFile.getAbsolutePath(), subFile);
                }
            }

            changeSfofSelectAllState(!isSelectAll);
            changeSfofFinishState(!selectedFileMap.isEmpty());
            mSubfileAdapter.notifyDataSetChanged();
        } else if (v.getId() == R.id.dialogSfofFinish) {
            if (mOnSelectFileOrFolderListener != null) {
                Map<String, File> selectedFileMap = mSubfileAdapter.getSelectedFileMap();
                mOnSelectFileOrFolderListener.onSelectFileOrFolder(new ArrayList<>(selectedFileMap.values()));
            }
            dismiss();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.dialogSfofClose) {
            Toast.makeText(getContext(), "关闭按钮", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.dialogSfofSelectAll) {
            Toast.makeText(getContext(), "全选按钮", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.dialogSfofFinish) {
            Toast.makeText(getContext(), "完成按钮", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialogSfofClose = null;
        dialogSfofSelectAll = null;
        dialogSfofFinish = null;
        dialogSfofCurrentPathRView = null;
        dialogSfofFilesRView = null;
        mCurrentFiles = null;
        mSubFiles = null;
        mListPositions = null;
    }

    @Override
    public void onCurrentPathRVItemClick(int position, File bean) {
        // TODO 点击路径
    }

    @Override
    public void onSubfileRVItemClick(int position, File bean) {
        // TODO 点击子文件
    }

    @Override
    public void onSubfileRVItemCBClick() {
        Map<String, File> selectedFileMap = mSubfileAdapter.getSelectedFileMap();
        if (selectedFileMap.isEmpty()) {
            changeSfofSelectAllState(false);
            changeSfofFinishState(false);
        } else {
            if (dialogSfofSelectAll.getVisibility() == View.VISIBLE) {
                boolean isSelectAll = true;
                for (File subFile : mSubFiles) {
                    if (!selectedFileMap.containsValue(subFile)) {
                        isSelectAll = false;
                        break;
                    }
                }
                changeSfofSelectAllState(isSelectAll);
            }
            changeSfofFinishState(true);
        }
    }

    /** 加载文件数据 */
    private void loadFiles(File currentFile) {
        mCurrentFiles.add(currentFile);
        File[] files = currentFile.listFiles();
        mSubFiles.clear();
        if (files != null && files.length > 0) {
            mSubFiles.addAll(Arrays.asList(files));
            Utils.sortFileList(mSubFiles);
        }

        mCurrentPathAdapter.onRefreshData(mCurrentFiles);
        mSubfileAdapter.onRefreshData(mSubFiles);
    }

    /** 改变全选按钮状态 */
    private void changeSfofSelectAllState(boolean isSelectAll) {
        dialogSfofSelectAll.setTag(isSelectAll);
        dialogSfofSelectAll.setImageResource(isSelectAll ? R.drawable.svg_select_all : R.drawable.svg_select_none);
    }

    /** 改变完成按钮状态 */
    private void changeSfofFinishState(boolean isEnabled) {
        dialogSfofFinish.setEnabled(isEnabled);
        dialogSfofFinish.setImageResource(isEnabled ? R.drawable.svg_finish_enabled : R.drawable.svg_finish_disable);
    }

    /** 挑选文件或文件夹 监听事件回调 */
    public interface OnSelectFileOrFolderListener {
        /** 回调选中的文件列表 */
        default void onSelectFileOrFolder(List<File> selectedFileList) {
        }
    }

}
