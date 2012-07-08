package com.example;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainPanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = MainPanel.class.getSimpleName();

    private MainThread thread;
    ArrayList<Vector> vect;
    public static double rad, hs, ws, nby, xm, ym, py, nh, nx, ny, nw, nbx, cx, cy;
    public static float sw;

    public MainPanel(Context context) {
        super(context);

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        vect = new ArrayList<Vector>();


        // create the animation loop thread
        thread = new MainThread(getHolder(), this);

        // make the animationPanel focusable so it can handle events
        setFocusable(true);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        nbx = 14;
        nw = width;
        nx = 0;
        ny = 0;

        nh = height - 6;
        py = 20 + 2;
        sw = Math.round(nw / 20);
        ws = nw / nbx;
        nby = Math.round(nbx * nh / nw);
        hs = (nh - sw) / nby;
        rad = nw / 4;

        // ---- reset lines ----
        int k = 0;
        for (int j = 0; j <= nby; j++) {
            for (int i = 0; i <= nbx; i++) {
                vect.add(
                        new Vector(k++, i, j)
                );

            }
        }


    }

    public void surfaceCreated(SurfaceHolder holder) {
        xm = nx + nw / 2;
        ym = ny + nh / 2;
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");
    }

    boolean down = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                down = true;
                break;
            }
            case MotionEvent.ACTION_UP: {
                down = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (down) {
                    xm = -nx + event.getX();
                    ym = -ny + event.getY();
                }
                break;
            }

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        cx += Math.round((xm - cx) * 0.1);
        cy += Math.round((ym - cy) * 0.1);

        int s = vect.size();
        for (int i = 0; i < s; i++) {
            Vector v = vect.get(i);
            v.points();
        }
        // ---- zIndex sorting ----
        Collections.sort(vect, new sorter());
        // ---- painting ----
        for (int i = 0; i < s; i++) {
            Vector v = vect.get(i);
            v.animate(canvas);
        }
    }

    class sorter implements Comparator<Vector> {

        public int compare(Vector a, Vector b) {
            return a.zIndex - b.zIndex;
        }

    }

}

