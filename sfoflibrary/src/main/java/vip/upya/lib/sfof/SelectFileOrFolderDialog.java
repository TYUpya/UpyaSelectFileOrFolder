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
public class SelectFileOrFolderDialog extends UpyaBaseDialog implements View.OnClickListener, View.OnLongClickListener, CurrentPathAdapter.OnCurrentPathRVItemClickListener {

    private ImageView dialogSfofClose;
    private ImageView dialogSfofSelectAll;
    private ImageView dialogSfofFinish;
    private RecyclerView dialogSfofCurrentPathRView;
    private RecyclerView dialogSfofFilesRView;

    private CurrentPathAdapter mCurrentPathAdapter;

    private List<File> mCurrentFiles = new ArrayList<>();
    private List<File> mSubFiles = new ArrayList<>();
    private Map<String, Integer> mListPositions = new HashMap<>();

    public SelectFileOrFolderDialog(Activity activity) {
        super(activity, R.layout.dialog_select_file_or_folder, DIALOG_MODE_BOTTOM);
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

        dialogSfofCurrentPathRView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        dialogSfofCurrentPathRView.setAdapter(mCurrentPathAdapter = new CurrentPathAdapter().setOnRVItemClickListener(this));

        File sdFile = Environment.getExternalStorageDirectory();
        loadFiles(sdFile);

        setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_UP) {
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

        } else if (v.getId() == R.id.dialogSfofFinish) {

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

    /** 加载文件数据 */
    private void loadFiles(File currentFile) {
        mCurrentFiles.add(currentFile);
        File[] files = currentFile.listFiles();
        mSubFiles.clear();
        if (files != null && files.length > 0)
            mSubFiles.addAll(Arrays.asList(files));

        mCurrentPathAdapter.onRefreshData(mCurrentFiles);
    }

}
