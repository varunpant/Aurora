package com.example;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    // Surface holder that can access the physical surface
    private SurfaceHolder surfaceHolder;
    // The actual view that handles inputs
    // and draws to the surface
    private MainPanel animationPanel;

    // flag to hold animation state
    private boolean running;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, MainPanel animationPanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.animationPanel = animationPanel;
    }

    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        while (running) {
            canvas = null;
            // try locking the canvas for exclusive pixel editing on the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // update game state
                    // draws the canvas on the panel
                    this.animationPanel.onDraw(canvas);
                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }    // end finally
        }
    }

}

