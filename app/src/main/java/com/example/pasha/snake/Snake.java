package com.example.pasha.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Snake implements GameObject {
    public int length;
    public int counter;
    private ArrayList<Integer> history;
    private int index;
    private int currentLength;
    private Point point;
    private Point point2;
    private int color;
    public Snake(Point point,int color){
        length = 0;
        currentLength = 0;
        index = 0;
        counter = 20;
        history = new ArrayList<>();
        for (int i = 0; i<130; i++){
            history.add(0);
        }
        this.point = point;
        point2 = point;
        this.color = color;
    }

    public int getLength() {
        return length;
    }

    public void decrementLength(){
        length--;
    }

   /* public boolean checkPos(float x, ArrayList<Block> blockList){
        Iterator iterator = blockList.iterator();
        while (iterator.hasNext()){
            Block b = (Block) iterator.next();
            if(point.y < b.getRect().bottom && point.y > b.getRect().top &&
                    point.x + Res.feedSize + x > b.getRect().left &&
                    point.x - Res.feedSize + x  < b.getRect().right){
                if (point.x < b.getRect().right + Res.feedSize && point.x > b.getRect().left) {
                    point.x = (int) b.getRect().right + Res.feedSize;
                }
                if (point.x + Res.feedSize > b.getRect().left && point.x < b.getRect().right){
                    point.x = (int)b.getRect().left - Res.feedSize;
                }
                return false;
            }
        }
        return true;
    }*/
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        point2 = point;
        currentLength = 0 ;
        index = 0;
        canvas.drawCircle(point.x,point.y,Res.feedSize, paint);
        if (length>0) {
            while (currentLength != length) {
                canvas.drawCircle(history.get(index), point.y + Res.feedSize*(index+1)*2,
                        Res.feedSize, paint);
                currentLength++;
                if (index < 25)
                    index++;
            }
        }

        paint.setColor(Color.WHITE);
        paint.setTextSize((int)(Res.feedSize*1.3));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(length),point.x,point.y-Res.feedSize-8,paint);
        paint.setTextSize(25);
        canvas.drawText(history.toString(),50,50, paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point){
        Collections.rotate(history,1);
        history.set(0,this.point.x);
        this.point.x = point.x;
    }

}
