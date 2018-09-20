package com.example.pasha.snake;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static int MAX_FPS = 100;
    private SurfaceHolder surfaceHolder;
    private Game game;
    private boolean running;
    public static Canvas canvas;
    public  MainThread(SurfaceHolder surfaceHolder,Game game){
        super();
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }
    public  void setRunning(boolean running){
        this.running = running;
    }
    @Override
    public void run() {
        long startTime;
        long timeMills = 1000/MAX_FPS;
        long target = 1000/MAX_FPS;
        long waitTime;
        long totalTime=0;
        long averageFPS;
        int frameCount=0;
        while(running){
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    game.draw(canvas);
                    game.update();
                }
            }catch (Exception e) {e.printStackTrace();}
            finally {
                if (canvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e) {e.printStackTrace(); }
                }
            }
            timeMills = (System.nanoTime() - startTime)/1000000;
            waitTime = target - timeMills;
            try{
                if (waitTime>0)
                    this.sleep(waitTime);
            }catch (Exception e){ e.printStackTrace(); }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount==MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
        }
    }
}
