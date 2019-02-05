package com.example.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread dt;

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        dt = new DrawThread(getContext(),getHolder());
        dt.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        dt.request(false);
        boolean retry = true;
        while (retry) {
            try {
                dt.join();
                retry = false;
            } catch (InterruptedException ignored) {

            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if((((int)event.getX() >= dt.getSmileX()) && ((int)event.getX() <= (dt.getSmileX() + dt.getWidth())) &&
                (((int)event.getY() >= dt.getSmileY()) && (int)event.getY() <= (dt.getSmileY() + dt.getHeight())))) {
            Log.d("Cors", "succes!!");
            dt.request(false);
            dt.run();
            dt.request(true);
        } else Log.d("Cors", "false!!");
        return false;
    }
}