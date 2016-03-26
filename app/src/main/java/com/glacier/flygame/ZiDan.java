package com.glacier.flygame;

import android.graphics.Bitmap;
import android.util.Log;

import com.glacier.flygame.bean.GameImage;
import com.glacier.flygame.bean.IDismiss;
import com.glacier.flygame.bean.PlaneImage;

/**
 * Created by shellshy on 16/3/26.
 */
public class ZiDan implements GameImage {

    private Bitmap zidan;
    private PlaneImage feiji;
    int x;
    int y;
    IDismiss iDismiss;

    public ZiDan(PlaneImage feiji, Bitmap zidan, IDismiss iDismiss) {
        this.feiji = feiji;
        this.zidan = zidan;
        this.iDismiss = iDismiss;
        x = (feiji.getX() + feiji.getWidth() / 2) - 10;
        y = feiji.getY() - zidan.getHeight();

    }

    @Override
    public Bitmap getBitmap() {
        y -= 35;
        if (y < -10) {
            iDismiss.remove(this,false);
        }
        return zidan;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
