package com.glacier.flygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by shellshy on 16/3/26.
 */
class BackgroundImage implements GameImage {

    private GameView gameView;
    private Bitmap bg;
    private Bitmap newBitmap = null;
    private int height = 0;

    BackgroundImage(GameView gameView, Bitmap bitmap) {
        this.gameView = gameView;
        this.bg = bitmap;
        newBitmap = Bitmap.createBitmap(
                gameView.w, gameView.h,
                Bitmap.Config.ARGB_8888);
    }

    public Bitmap getBitmap() {
        Paint p = new Paint();
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bg,
                new Rect(0, 0, bg.getWidth(), bg.getHeight()),
                new Rect(0, height, gameView.w, gameView.h + height),
                p);

        canvas.drawBitmap(bg,
                new Rect(0, 0, bg.getWidth(), bg.getHeight()),
                new Rect(0, -gameView.h + height, gameView.w, height),
                p);

        height += 3;
        if (height == gameView.h) {
            height = 0;
        }
        return newBitmap;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }
}
