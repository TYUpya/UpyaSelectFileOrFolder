package vip.upya.demo.sfof;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

import vip.upya.lib.sfof.SelectFileOrFolderDialog;

public class MainActivity extends AppCompatActivity {

    private TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainText = findViewById(R.id.mainText);
    }

    /** 单选文件 */
    public void onClick01(View view) {
        new SelectFileOrFolderDialog(this, true, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FILE, new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
            @Override
            public void onSelectFileOrFolder(List<File> selectedFileList) {
                mainText.setText("选择的文件：" + selectedFileList.get(0).getAbsolutePath());
            }
        }).show();
    }

    /** 多选文件 */
    public void onClick02(View view) {
        new SelectFileOrFolderDialog(this, false, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FILE, new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
            @Override
            public void onSelectFileOrFolder(List<File> selectedFileList) {
                mainText.setText("选择的文件：\n");
                for (File file : selectedFileList) {
                    mainText.append(file.getAbsolutePath());
                    mainText.append("\n");
                }
            }
        }).show();
    }

    /** 单选文件夹 */
    public void onClick03(View view) {
        new SelectFileOrFolderDialog(this, true, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FOLDER, new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
            @Override
            public void onSelectFileOrFolder(List<File> selectedFileList) {
                mainText.setText("选择的文件：" + selectedFileList.get(0).getAbsolutePath());
            }
        }).show();
    }

    /** 多选文件夹 */
    public void onClick04(View view) {
        new SelectFileOrFolderDialog(this, false, SelectFileOrFolderDialog.CHOICEMODE_ONLY_FOLDER, new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
            @Override
            public void onSelectFileOrFolder(List<File> selectedFileList) {
                mainText.setText("选择的文件：\n");
                for (File file : selectedFileList) {
                    mainText.append(file.getAbsolutePath());
                    mainText.append("\n");
                }
            }
        }).show();
    }

    /** 单选文件和文件夹 */
    public void onClick05(View view) {
        new SelectFileOrFolderDialog(this, true, SelectFileOrFolderDialog.CHOICEMODE_UNLIMITED, new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
            @Override
            public void onSelectFileOrFolder(List<File> selectedFileList) {
                mainText.setText("选择的文件：" + selectedFileList.get(0).getAbsolutePath());
            }
        }).show();
    }

    /** 多选文件和文件夹 */
    public void onClick06(View view) {
        new SelectFileOrFolderDialog(this, false, SelectFileOrFolderDialog.CHOICEMODE_UNLIMITED, new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
            @Override
            public void onSelectFileOrFolder(List<File> selectedFileList) {
                mainText.setText("选择的文件：\n");
                for (File file : selectedFileList) {
                    mainText.append(file.getAbsolutePath());
                    mainText.append("\n");
                }
            }
        }).show();
    }

}
