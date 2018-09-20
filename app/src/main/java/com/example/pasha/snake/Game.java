package com.example.pasha.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    public Snake snake;
    public Point snakePos;
    private Point oldSnakePos;
    public FeedManager feedManager;
    private boolean intersect = false;
    private boolean GameOver = false;
    private int counter=0;
    private float x =0;
    private int score;
    private long gameOverTime=0;
    private Point point = new Point(100,100);
    private Paint paint;
    private Paint paintScore;
    private Rect r = new Rect();
    private GestureDetectorCompat detector;
    private GestureDetector.OnGestureListener glistener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            if(GameOver && System.currentTimeMillis() - gameOverTime >= 3000){
                reset();
                GameOver = false;
            }
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if(!GameOver) {
                if (snakePos.x - distanceX < Res.screenWidth && Res.feedSize < snakePos.x - distanceX) {
                    x = distanceX;
                    oldSnakePos = snakePos;
                    if (Math.abs(distanceX)<40)
                        snakePos.x = snakePos.x - (int) distanceX;
                    if (Math.abs(distanceX)>=40 && Math.abs(distanceX)<80)
                        snakePos.x = snakePos.x - (int)distanceX + (int)(distanceX/3);
                    if (Math.abs(distanceX)>=80 && Math.abs(distanceX)<140)
                        snakePos.x = snakePos.x - (int)distanceX + (int)(distanceX/2);
                    if (Math.abs(distanceX)>=140)
                        snakePos.x = snakePos.x - (int) distanceX + (int)(distanceX/1.6);
                    for (int i = 0; i<feedManager.getBlocks().size()-2; i++) {
                        Block b = feedManager.getBlocks().get(i);
                        if (snakePos.y < b.getRect().bottom && snakePos.y + Res.feedSize > b.getRect().top &&
                                snakePos.x + Res.feedSize > b.getRect().left &&
                                snakePos.x - Res.feedSize < b.getRect().right) {
                            if (oldSnakePos.x < b.getX())
                                snakePos.x = (int) b.getRect().left - Res.feedSize - 1;
                            else
                                snakePos.x = (int) b.getRect().right + Res.feedSize + 1;
                        }
                    }

                }

                if (0 > snakePos.x - distanceX) {
                    snakePos.x = Res.feedSize;
                }
                if (snakePos.x - distanceX > Res.screenWidth - Res.feedSize) {
                    snakePos.x = Res.screenWidth - Res.feedSize;
                }


            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
    public Game(Context context){
        super(context);
        setFocusable(true);
        GameOver = false;
        score = 0;
        detector = new GestureDetectorCompat(getContext(),glistener);
        snakePos = new Point(Res.screenWidth/2,Res.screenHight-Res.screenHight/3);
        snake = new Snake(snakePos,Color.RED);
        feedManager = new FeedManager();
        getHolder().addCallback(this);
        paint = new Paint();
        paintScore = new Paint();
        paintScore.setTextSize(48);
        paintScore.setColor(Color.WHITE);
        thread = new MainThread(getHolder(),this);
    }

    private void reset(){
        intersect = false;
        snakePos = new Point(Res.screenWidth/2,Res.screenHight-Res.screenHight/3);
        snake = new Snake(snakePos,Color.RED);
        feedManager = new FeedManager();
        score = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }catch (Exception e) { e.printStackTrace();}
            retry = false;
        }
    }

    public void update(){
        if (!GameOver) {
            intersect = false;
            feedManager.update();
            snake.update(snakePos);
            for (Feed f : feedManager.getFeeds()) {
                if (Math.abs(snakePos.x - f.getX()) < 2 * Res.feedSize
                        && Math.abs(snakePos.y - f.getY()) < 2 * Res.feedSize
                        && snakePos.y > f.getY()) {
                    snake.length += f.getWeight();
                    feedManager.toRemoveFeeds.add(f);
                }

            }
            feedManager.removeFeeds(feedManager.toRemoveFeeds);
            for (Block b : feedManager.getBlocks()) {
                if (b.getRect().contains(snakePos.x, snakePos.y - Res.feedSize)) {
                    feedManager.pause();
                    intersect = true;
                    if (counter % 5 == 0) {
                        b.decrementSize();
                        snake.decrementLength();
                        score++;
                    }
                    counter++;
                    if (b.getSize() <= 0) {
                        feedManager.toRemoveBlocks.add(b);
                        feedManager.unPause();
                        counter = 0;
                    }
                }
            }
            feedManager.removeBlocks(feedManager.toRemoveBlocks);
            feedManager.toRemoveBlocks.clear();

            if (!intersect)
                feedManager.unPause();
            if (snake.getLength() < 0) {
                GameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }

    }

    public void draw(Canvas canvas){
            super.draw(canvas);
            feedManager.draw(canvas);
            snake.draw(canvas);
            canvas.drawText(String.valueOf(score),Res.screenWidth - 120,paintScore.getTextSize()*2,paintScore);
            if (GameOver){
                paint.setTextSize(200);
                paint.setColor(Color.BLUE);
                paint.setFakeBoldText(true);
                drawCenter(canvas, paint, "Game Over");
            }
        Paint paint = new Paint();
        paint.setTextSize(45);
        paint.setColor(Color.WHITE);
        canvas.drawText(String.valueOf(x),100,100,paint);
        }

    private void drawCenter(Canvas canvas, Paint paint, String text) {
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
