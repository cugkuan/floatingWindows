package com.kuan.floatingwindowdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 如果显示不出小窗口，在软件管家中，打开相应的弹框权限，否则不会显示
 */
public class MainActivity extends AppCompatActivity {

    private FloatingWindow  mFloatingWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFloatingWindow = new FloatingWindow(this);
        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingWindow.show();
            }
        });
    }
}
