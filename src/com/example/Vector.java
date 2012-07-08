package com.example;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Vector {

    double x;
    double y;
    double x1;
    double y1;
    double x2;
    double y2;

    int zIndex;
    boolean visible;
    boolean isVisible;

    private Paint mLinePaint;

    public Vector(int n, double x, double y) {
        this.x = x;
        this.y = y;
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
    }

    public void points() {
        this.x1 = this.x * MainPanel.ws;
        this.y1 = MainPanel.py + this.y * MainPanel.hs + MainPanel.sw * 0.5;
        double dx = MainPanel.cx - this.x1;
        double dy = MainPanel.cy - this.y1;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < MainPanel.rad) {
            double k = Math.PI * Math.abs(dist / MainPanel.rad);
            double M = Math.sin(k);
            this.zIndex = (int) (1 + 3 * (1 - Math.sin(k * .5)));
            this.visible = true;
            this.x2 = 1 + this.x1 - dx * M;
            this.y2 = 1 + this.y1 - dy * M;
        } else {
            this.zIndex = -1;
            this.visible = false;
        }
    }

    public void animate(Canvas canvas) {
        if (this.visible) {
            this.isVisible = true;
            mLinePaint.setColor(Color.BLACK);
            canvas.drawLine((float) Math.round(this.x1 + MainPanel.sw * 0.25), (float) Math.round(this.y1), (float) Math.round(this.x2 + MainPanel.sw * 0.25), (float) Math.round(this.y2), mLinePaint);
            // ---- color line ----
            double c = Math.round(-56 + this.zIndex * 255);

            mLinePaint.setColor(Color.rgb((int) Math.round(c * MainPanel.ym / MainPanel.nh), (int) Math.round(c * .5), (int) Math.round(c * MainPanel.xm / MainPanel.nw)));
            mLinePaint.setStrokeWidth(MainPanel.sw);
            canvas.drawLine((float) this.x1, (float) this.y1, (float) this.x2, (float) this.y2, mLinePaint);

        } else {
            if (this.isVisible) {
                // ---- hide line ----
                this.isVisible = false;
            }
        }
    }
}
