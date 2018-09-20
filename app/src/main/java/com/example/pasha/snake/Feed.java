package com.example.pasha.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;

public class Feed implements GameObject {

    private int weight;
    private int color;
    private Point point;
    private boolean paused;
    public Feed(Point point,int color){
        paused = false;
        this.point = point;
        this.color = color;
        weight = 1 + (int)(Math.random() * ((5 - 1) + 1));
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getY(){
        return point.y;
    }
    public int getX(){
        return point.x;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(point.x, point.y,Res.feedSize,paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize((int)(Res.feedSize*1.3));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(weight),point.x,point.y-Res.feedSize-8,paint);
    }

    @Override
    public void update() {
        if (!paused)
            point.y += Res.speed;
    }
}
