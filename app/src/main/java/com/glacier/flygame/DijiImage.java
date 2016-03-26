package com.glacier.flygame;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shellshy on 16/3/26.
 */
public class DijiImage implements GameImage {

    private Bitmap diji,bomb;
    private List<Bitmap> bitmap = new ArrayList<>();
    private List<Bitmap> bombs = new ArrayList<>();
    int w;
    int h;
    private IDismiss iDismiss;

    public DijiImage(Bitmap diren,Bitmap bomb, int w, int h, IDismiss iDismiss) {
        this.diji = diren;
        this.bomb = bomb;
        this.iDismiss = iDismiss;
        bitmap.add(Bitmap.createBitmap(diji, 0, 0, diji.getWidth(), diji.getHeight()));
        bitmap.add(Bitmap.createBitmap(diji, 0, 0, diji.getWidth(), diji.getHeight()));
        bitmap.add(Bitmap.createBitmap(diji, 0, 0, diji.getWidth(), diji.getHeight()));
        bitmap.add(Bitmap.createBitmap(diji, 0, 0, diji.getWidth(), diji.getHeight()));

        bombs.add(Bitmap.createBitmap(bomb, 0, 0, bomb.getWidth(), bomb.getHeight()));
        bombs.add(Bitmap.createBitmap(bomb, 0, 0, bomb.getWidth(), bomb.getHeight()));
        bombs.add(Bitmap.createBitmap(bomb, 0, 0, bomb.getWidth(), bomb.getHeight()));
        bombs.add(Bitmap.createBitmap(bomb, 0, 0, bomb.getWidth(), bomb.getHeight()));

        this.w = w;
        this.h = h;
        Random ran = new Random();
        x = ran.nextInt(w - diji.getWidth());
        y = diji.getHeight();

    }

    private int index = 0;
    private int num = 0;
    private int x = 0;
    private int y = 0;

    @Override
    public Bitmap getBitmap() {
        Bitmap bp = bitmap.get(index);
        if (num == 2) {
            index++;
            if(index==4 && over){
                iDismiss.remove(this,true);
            }
            if (index == bitmap.size()) {
                index = 0;
            }
            num = 0;
        }
        y += GameView.dijiyidong;
        num++;
        if (y > h) {
            iDismiss.remove(this,false);
        }
        return bp;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getWidth() {
        if (diji != null) {
            return diji.getWidth();
        }
        return 0;
    }

    public int getHeight() {
        if (diji != null) {
            return diji.getHeight();
        }
        return 0;
    }

    private boolean over = false;
    /**
     * 受到攻击
     */
    public void shoudaogongji(ArrayList<ZiDan> zidans) {
        if(over){
            return;
        }
        for (ZiDan zidan : (List<ZiDan>) zidans.clone()) {
            if (zidan.getX() > x && zidan.getY() > y
                    && zidan.getX() < x + getWidth() &&
                    zidan.getY() < y + getHeight()) {
                zidans.remove(zidan);
                over = true;
                bitmap = bombs;
                break;
                //Log.i("tag","击中");
            }
        }
    }
}
