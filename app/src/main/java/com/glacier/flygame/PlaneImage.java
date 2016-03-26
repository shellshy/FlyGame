package com.glacier.flygame;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shellshy on 16/3/26.
 */
public class PlaneImage implements GameImage {

    private Bitmap my;
    private List<Bitmap> bitmap = new ArrayList<>();
    int w;
    int h;

    public PlaneImage(Bitmap my, int w, int h) {
        this.my = my;
        bitmap.add(Bitmap.createBitmap(my, 0, 0, my.getWidth(), my.getHeight()));
        bitmap.add(Bitmap.createBitmap(my, 0, 0, my.getWidth(), my.getHeight()));
        bitmap.add(Bitmap.createBitmap(my, 0, 0, my.getWidth(), my.getHeight()));
        bitmap.add(Bitmap.createBitmap(my, 0, 0, my.getWidth(), my.getHeight()));
        this.w = w;
        this.h = h;
        x = (w - my.getWidth()) / 2;
        y = h - my.getHeight() - 100;

    }

    private int index = 0;
    private int num = 0;
    private int x = 0;
    private int y = 0;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Bitmap getBitmap() {
        Bitmap bp = bitmap.get(index);
        if (num == 7) {
            index++;
            if (index == bitmap.size()) {
                index = 0;
            }
            num = 0;
        }
        num++;
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

    /**
     * 判断是否选中飞机
     *
     * @param ex
     * @param ey
     * @return
     */
    public boolean select(int ex, int ey) {
        if (x < ex && y < ey &&
                x + my.getWidth() > ex &&
                y + my.getHeight() > ey){
            return true;
        }
        return false;
    }

    public int getWidth(){
        if(my!=null){
            return my.getWidth();
        }
        return 0;
    }

    public int getHeight(){
        if(my!=null){
            return my.getHeight();
        }
        return 0;
    }
}
