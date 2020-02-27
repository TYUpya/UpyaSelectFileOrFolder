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
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 挑选文件或文件夹 弹窗 */
@SuppressWarnings("all")
public class SelectFileOrFolderDialog extends UpyaBaseDialog implements View.OnClickListener, View.OnLongClickListener, CurrentPathAdapter.OnCurrentPathRVItemClickListener, SubfileAdapter.OnSubfileRVItemClickListener {

    public static final int CHOICEMODE_ONLY_FILE = 1; // 只选文件
    public static final int CHOICEMODE_ONLY_FOLDER = 2; // 只选文件夹
    public static final int CHOICEMODE_UNLIMITED = 3; // 无限制选择

    private ImageView dialogSfofClose;
    private ImageView dialogSfofSelectAll;
    private ImageView dialogSfofFinish;
    private RecyclerView dialogSfofCurrentPathRView;
    private RecyclerView dialogSfofFilesRView;

    private CurrentPathAdapter mCurrentPathAdapter;
    private SubfileAdapter mSubfileAdapter;
    private List<File> mCurrentFiles = new ArrayList<>();
    private List<File> mSubFiles = new ArrayList<>();
    private Map<String, int[]> mListPositions = new HashMap<>();
    private boolean mIsSingleChoice;
    private int mChoiceMode;
    private FileFilter mFileFilter;
    private OnSelectFileOrFolderListener mOnSelectFileOrFolderListener;

    public SelectFileOrFolderDialog(Activity activity, boolean isSingleChoice, int choiceMode, OnSelectFileOrFolderListener onSelectFileOrFolderListener) {
        super(activity, R.layout.dialog_select_file_or_folder, DIALOG_MODE_BOTTOM);
        this.mIsSingleChoice = isSingleChoice;
        this.mChoiceMode = choiceMode;
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

        TextView dialogSfofTitle = findViewById(R.id.dialogSfofTitle);
        switch (mChoiceMode) {
            case CHOICEMODE_ONLY_FILE:
                dialogSfofTitle.setText(R.string.dialogSfofTitle1);
                break;
            case CHOICEMODE_ONLY_FOLDER:
                dialogSfofTitle.setText(R.string.dialogSfofTitle2);
                break;
            case CHOICEMODE_UNLIMITED:
                dialogSfofTitle.setText(R.string.dialogSfofTitle3);
                break;
        }

        dialogSfofSelectAll.setVisibility(mIsSingleChoice ? View.GONE : View.VISIBLE);
        changeSfofSelectAllState(false);
        changeSfofFinishState(false);

        dialogSfofCurrentPathRView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        dialogSfofCurrentPathRView.setAdapter(mCurrentPathAdapter = new CurrentPathAdapter(this));
        dialogSfofFilesRView.setLayoutManager(new LinearLayoutManager(activity));
        dialogSfofFilesRView.setAdapter(mSubfileAdapter = new SubfileAdapter(isSingleChoice, mChoiceMode, this));

        mFileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (mChoiceMode == CHOICEMODE_ONLY_FOLDER) {
                    return pathname.isDirectory();
                } else {
                    return true;
                }
            }
        };

        setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_UP) {
                rollbackFiles();
                return false;
            }
            return false;
        });

        Utils.mHandler.post(() -> {
            File sdFile = Environment.getExternalStorageDirectory();
            loadFiles(sdFile);
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
                if (mChoiceMode == CHOICEMODE_ONLY_FILE && subFile.isDirectory())
                    continue;

                String path = subFile.getAbsolutePath();
                if (isSelectAll) { // 需要全不选
                    selectedFileMap.remove(path);
                } else { // 需要全选
                    selectedFileMap.put(path, subFile);
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
        gotoFiles(bean);
    }

    @Override
    public void onSubfileRVItemClick(int position, File bean) {
        loadFiles(bean);
    }

    /** 选中文件回调/查询更新全选状态 */
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
                    if ((mChoiceMode == CHOICEMODE_ONLY_FILE && subFile.isFile()) || (mChoiceMode == CHOICEMODE_ONLY_FOLDER && subFile.isDirectory()) || (mChoiceMode == CHOICEMODE_UNLIMITED)) {
                        if (!selectedFileMap.containsValue(subFile)) {
                            isSelectAll = false;
                            break;
                        }
                    }
                }
                changeSfofSelectAllState(isSelectAll);
            }
            changeSfofFinishState(true);
        }
    }

    /** 加载文件数据 */
    private void loadFiles(File currentFile) {
        if (!mCurrentFiles.isEmpty()) {
            int[] positionRecord = Utils.getPositionAndOffset((LinearLayoutManager) dialogSfofFilesRView.getLayoutManager());
            mListPositions.put(mCurrentFiles.get(mCurrentFiles.size() - 1).getAbsolutePath(), positionRecord);
        }

        mCurrentFiles.add(currentFile);
        File[] files = currentFile.listFiles(mFileFilter);
        mSubFiles.clear();
        if (files != null && files.length > 0) {
            mSubFiles.addAll(Arrays.asList(files));
            Utils.sortFileList(mSubFiles);
        }

        if (!mIsSingleChoice)
            dialogSfofSelectAll.setVisibility(mSubFiles.isEmpty() ? View.GONE : View.VISIBLE);
        onSubfileRVItemCBClick();

        mCurrentPathAdapter.onRefreshData(mCurrentFiles);
        mSubfileAdapter.onRefreshData(mSubFiles);
        Utils.mHandler.post(() -> dialogSfofCurrentPathRView.scrollToPosition(mCurrentFiles.size() - 1));
    }

    /** 回退文件数据 */
    private void rollbackFiles() {
        if (mCurrentFiles.size() <= 1) {
            dismiss();
            return;
        }

        mCurrentFiles.remove(mCurrentFiles.size() - 1);
        File currentFile = mCurrentFiles.get(mCurrentFiles.size() - 1);
        File[] files = currentFile.listFiles(mFileFilter);
        mSubFiles.clear();
        if (files != null && files.length > 0) {
            mSubFiles.addAll(Arrays.asList(files));
            Utils.sortFileList(mSubFiles);
        }

        if (!mIsSingleChoice)
            dialogSfofSelectAll.setVisibility(mSubFiles.isEmpty() ? View.GONE : View.VISIBLE);
        onSubfileRVItemCBClick();

        mCurrentPathAdapter.onRefreshData(mCurrentFiles);
        mSubfileAdapter.onRefreshData(mSubFiles);
        int[] positionRecord = mListPositions.get(currentFile.getAbsolutePath());
        ((LinearLayoutManager) dialogSfofFilesRView.getLayoutManager()).scrollToPositionWithOffset(positionRecord[0], positionRecord[1]);
    }

    /** 跳转到指定文件数据 */
    private void gotoFiles(File currentFile) {
        int index = mCurrentFiles.indexOf(currentFile);
        mCurrentFiles.subList(index + 1, mCurrentFiles.size()).clear();
        File[] files = currentFile.listFiles(mFileFilter);
        mSubFiles.clear();
        if (files != null && files.length > 0) {
            mSubFiles.addAll(Arrays.asList(files));
            Utils.sortFileList(mSubFiles);
        }

        if (!mIsSingleChoice)
            dialogSfofSelectAll.setVisibility(mSubFiles.isEmpty() ? View.GONE : View.VISIBLE);
        onSubfileRVItemCBClick();

        mCurrentPathAdapter.onRefreshData(mCurrentFiles);
        mSubfileAdapter.onRefreshData(mSubFiles);
        int[] positionRecord = mListPositions.get(currentFile.getAbsolutePath());
        ((LinearLayoutManager) dialogSfofFilesRView.getLayoutManager()).scrollToPositionWithOffset(positionRecord[0], positionRecord[1]);
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
