package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;

import java.util.Random;

public class DrawThread extends Thread {

    private final Random random = new Random();
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    private Bitmap bitmap;
    private final byte SPEED = 5;
    private int towardPointX, smileX;
    private int towardPointY, smileY;
    private SurfaceHolder surfaceHolder;

    {
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    DrawThread(Context context, SurfaceHolder surfaceHolder) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.earth);
        this.surfaceHolder = surfaceHolder;
    }

    void request(boolean flag) {
        running = flag;
    }

    private void setTowardPoint() {
        int x, y;
        x = random.nextInt(1072-bitmap.getWidth());
        y = random.nextInt(1672-bitmap.getHeight());
        towardPointX = x - (x % SPEED);
        towardPointY = y - (y % SPEED-1);
    }

    int getSmileX() {
        return smileX;
    }

    int getSmileY() {
        return smileY;
    }

    int getWidth() {
        return bitmap.getWidth();
    }

    int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public void run() {
        setTowardPoint();
        smileX = towardPointX;
        smileY = towardPointY;
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    canvas.drawBitmap(bitmap, smileX, smileY, backgroundPaint);
                    if (smileX < towardPointX) smileX += SPEED;
                    if (smileX > towardPointX) smileX -= SPEED;
                    if (smileY < towardPointY) smileY += SPEED-1;
                    if (smileY > towardPointY) smileY -= SPEED-1;
                    if (smileX == towardPointX || smileY == towardPointY) setTowardPoint();
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}