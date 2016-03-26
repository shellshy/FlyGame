package com.glacier.flygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shellshy on 16/3/25.
 */
public class GameView extends SurfaceView implements View.OnTouchListener, Runnable, SurfaceHolder.Callback {

    private Bitmap diren;
    private Bitmap plane;
    private Bitmap bullet;
    private Bitmap bg;
    private Bitmap bomb;
    private int score = 0;
    private SoundPool pool;
    private ArrayList<GameImage> gameImages = new ArrayList<>();
    private ArrayList<ZiDan> zidans = new ArrayList<>();
    int w, h;

    public GameView(Context context, int w, int h) {
        super(context);
        getHolder().addCallback(this);//注册回调处理类
        this.w = w;
        this.h = h;
        diren = BitmapFactory.decodeResource(getResources(), R.drawable.diren);
        plane = BitmapFactory.decodeResource(getResources(), R.drawable.plane);
        bullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        bomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        gameBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        gameImages.add(new BackgroundImage(this, bg));
        gameImages.add(new PlaneImage(plane, w, h));
        gameImages.add(new DijiImage(diren, bomb, w, h, iDismiss));
        setOnTouchListener(this);
        //加载声音池
        pool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        //pool.load(getContext(),);

    }

    private Thread thread = null;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;

        runState = true;
        stopState = false;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        runState = false;
    }

    private boolean runState = false;
    private boolean stopState = false;
    private SurfaceHolder holder = null;
    private Bitmap gameBitmap = null;//二级缓存图片

    private int guanqia = 1;

    private int chudishudu = 20;//出敌机速度
    public static int dijiyidong = 10;//敌机速度
    private int zidansudu = 5;

    private int[][] sj = {
            {1, 50, 20, 10,5},
            {2, 100, 20, 10,5},
            {3, 110, 18, 13,4},
            {4, 120, 18, 13,4},
            {5, 130, 16, 16,3},
            {6, 140, 16, 16,3},
            {7, 150, 14, 18,2},
            {8, 160, 14, 18,2},
            {9, 190, 12, 20,2},
            {10, 200, 12, 20,2},
    };

    public void stop(){
        stopState = true;
    }

    public void start(){
        stopState = false;
        thread.interrupt();//唤醒线程
    }

    @Override
    public void run() {
        Random ran = new Random();
        Paint p = new Paint();
        int diren_num = 0;
        int zidan_num = 0;
        Paint p2 = new Paint();
        p2.setColor(Color.YELLOW);
        p2.setTextSize(50);
        p2.setDither(true);
        p2.setAntiAlias(true);
        try {
            while (runState) {

                while(stopState){
                    try {
                        Thread.sleep(100000);
                    }catch (Exception e){

                    }
                }

                if (zidan_num == zidansudu) {
                    if (selectFeiji != null) {
                        zidans.add(new ZiDan(selectFeiji, bullet, zidanDismiss));
                    }
                    zidan_num = 0;
                }
                zidan_num++;
                Canvas c = new Canvas(gameBitmap);

                for (GameImage image : (List<GameImage>) gameImages.clone()) {
                    if (image instanceof DijiImage) {
                        //把子弹位置告诉敌机，由敌机来判断
                        ((DijiImage) image).shoudaogongji(zidans);
                    }
                    c.drawBitmap(image.getBitmap(), image.getX(), image.getY(), p);
                }

                for (ZiDan ziDan : (List<ZiDan>) zidans.clone()) {
                    c.drawBitmap(ziDan.getBitmap(), ziDan.getX(), ziDan.getY(), p);
                }

                c.drawText("分:" + score + "关:" + guanqia, 0, 50, p2);
                if (guanqia >= 10) {
                    guanqia = 1;
                    score = 0;
                    chudishudu = 20;
                    dijiyidong = 10;
                    zidansudu = sj[guanqia][4];
                }
                if (score >= sj[(guanqia - 1) % 10][1]) {
                    chudishudu = sj[guanqia][2];
                    dijiyidong = sj[guanqia][3];
                    guanqia = sj[guanqia][0];
                    zidansudu = sj[guanqia][4];
                }

                if (diren_num == chudishudu) {
                    gameImages.add(new DijiImage(diren, bomb, w, h, iDismiss));
                    diren_num = 0;
                }

                diren_num++;
                //获得绘画，在锁定的位置进行绘画
                Canvas canvas = holder.lockCanvas();

                canvas.drawBitmap(gameBitmap, 0, 0, p);

                //解锁,把绘画好的内容提交上去
                holder.unlockCanvasAndPost(canvas);

                Thread.sleep(10);
            }
        } catch (Exception e) {
            Log.e("App.tag", "异常", e);
        }
    }

    private PlaneImage selectFeiji;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//手接近屏幕
            for (GameImage game : gameImages) {
                if (game instanceof PlaneImage) {
                    if (((PlaneImage) game).select((int) event.getX(), (int) event.getY())) {
                        selectFeiji = (PlaneImage) game;
                        //Log.i("tag", "我被选中了");
                    } else {
                        selectFeiji = null;
                        //Log.i("tag", "我没有被选中");
                    }
                    break;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (selectFeiji != null) {
                selectFeiji.setX((int) event.getX() - selectFeiji.getWidth() / 2);
                selectFeiji.setY((int) event.getY() - selectFeiji.getHeight() / 2);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            selectFeiji = null;
        }
        return true;
    }

    private IDismiss iDismiss = new IDismiss() {
        @Override
        public void remove(GameImage gi, boolean fire) {
            if (fire) {
                score += 10;
            }
            gameImages.remove(gi);
        }
    };

    private IDismiss zidanDismiss = new IDismiss() {
        @Override
        public void remove(GameImage gi, boolean fire) {
            zidans.remove(gi);
        }
    };
}
