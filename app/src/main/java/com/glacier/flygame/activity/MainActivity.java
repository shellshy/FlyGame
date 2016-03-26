package com.glacier.flygame.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.glacier.flygame.view.GameView;

public class MainActivity extends AppCompatActivity implements AlertDialog.OnClickListener{

    GameView gv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
        gv = new GameView(this, w, h);
        setContentView(gv);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            gv.stop();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("你要退出吗？");
            alert.setNegativeButton("退出",this);
            alert.setPositiveButton("继续",this);
            alert.create().show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE){
            gv.start();
        }else{
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
