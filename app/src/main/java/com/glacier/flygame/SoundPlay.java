package com.glacier.flygame;

import android.media.SoundPool;

import com.glacier.flygame.view.GameView;

/**
 * Created by shy on 2016/3/26.
 */
public class SoundPlay extends Thread {
    private GameView gameView;
    private SoundPool pool;
    int i = 0;

    public SoundPlay(SoundPool pool, int i) {
        this.pool = pool;
        this.i = i;

    }

    public void run() {
        pool.play(i, 1, 1, 1, 0, 1);
    }
}
