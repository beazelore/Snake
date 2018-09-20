package com.example.pasha.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;

public class Block implements GameObject {
    private int size;
    private boolean paused;
    private Point point = new Point(-100,-100);
    private RectF rect= new RectF(point.x - Res.blockSize,point.y - Res.blockSize,
            point.x + Res.blockSize,point.y + Res.blockSize);
    private int color;
    public Block(Point point) {
        paused = false;
        this.point = point;
        size = 1 + (int)(Math.random()*((50 + 1) - 1));
        setColor();
    }

    private void setColor(){
        if (size<18){
            color = Color.argb(255,0,255,255-15*size);
        }
        if (size>=18 && size<34){
            color = Color.argb(255,255-(34-size)*16 + 1 ,255,0);
        }
        if (size>33){
            color = Color.argb(255,255 ,15*(51-size),0);
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getY(){
        return point.y;
    }

    public int getX(){
        return point.x;
    }
    public void decrementSize(){
        size--;
    }
    public RectF getRect() {
        return rect;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRoundRect(rect,30,30,paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(Res.blockSize/2);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(size), point.x,point.y+paint.getTextSize()/4,paint);
    }

    @Override
    public void update() {
        if(!paused){
            point.y +=Res.speed;
            rect.set(point.x - Res.blockSize,point.y - Res.blockSize,
                    point.x + Res.blockSize,point.y + Res.blockSize);
        }
        setColor();
    }
}
