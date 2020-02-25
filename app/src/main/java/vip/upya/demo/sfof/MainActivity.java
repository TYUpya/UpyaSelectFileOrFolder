package vip.upya.demo.sfof;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import vip.upya.lib.sfof.SelectFileOrFolderDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick01(View view) {
        new SelectFileOrFolderDialog(this).show();
    }

}
