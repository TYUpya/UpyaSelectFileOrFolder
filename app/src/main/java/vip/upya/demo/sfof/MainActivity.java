package vip.upya.demo.sfof;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

import vip.upya.lib.sfof.SelectFileOrFolderDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick01(View view) {
        new SelectFileOrFolderDialog(this, false, new SelectFileOrFolderDialog.OnSelectFileOrFolderListener() {
            @Override
            public void onSelectFileOrFolder(List<File> selectedFileList) {
                for (File file : selectedFileList) {
                    System.out.println("选择的文件：" + file.getAbsoluteFile());
                }
            }
        }).show();
    }

}
