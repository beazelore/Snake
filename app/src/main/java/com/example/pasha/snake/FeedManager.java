package com.example.pasha.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

public class FeedManager {
    private ArrayList<Feed> feeds;
    private ArrayList<Block> blocks;
    public ArrayList<Feed> toRemoveFeeds;
    public ArrayList<Block> toRemoveBlocks;
    private Point point;
    private long distance;
    private boolean paused;
    public FeedManager(){
        paused = false;
        feeds = new ArrayList<>();
        blocks = new ArrayList<>();
        toRemoveBlocks = new ArrayList<>();
        toRemoveFeeds = new ArrayList<>();
        distance = 0;
        while (feeds.size()<6)
            createFeed();
        while (blocks.size()<6)
            createBlock();
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public ArrayList<Feed> getFeeds() {
        return feeds;
    }

    public void pause(){
        paused = true;
        for (Block b : blocks)
            b.setPaused(true);
        for (Feed f : feeds)
            f.setPaused(true);
    }

    public void unPause(){
        paused = false;
        for (Block b : blocks)
            b.setPaused(false);
        for (Feed f : feeds)
            f.setPaused(false);
    }

    public void createBlock(){
        if (blocks.size()<6){
            point = new Point();
            point.y = (int)(Math.random()*(-Res.screenHight*2)-Res.blockSize);
            point.x = (int)(Math.random()*(Res.screenWidth));
            point.x = (int)Math.floor(point.x/(Res.blockSize*2))*(Res.blockSize*2)+Res.blockSize;
            if (checkNewBlockPos(point))
                blocks.add(new Block(point));
            else {
                createBlock();
               // System.out.println("BAD BLOCK SPAWN");
            }
            for (Feed f : feeds){
                if (Math.abs(point.x - f.getX())<Res.blockSize + 2*Res.feedSize &&
                        Math.abs(point.y - f.getY())<Res.blockSize + 3*Res.feedSize)
                    toRemoveFeeds.add(f);
            }
        }
    }

    private boolean checkNewBlockPos(Point p){
        for (int i = 0; i<blocks.size();i++){
            int divY = p.y - blocks.get(i).getY();
            if (Math.abs(divY)<Res.blockSize*2 && p.x == blocks.get(i).getX()) {
                return false;
            }
        }
        return true;
    }

    public void createBlockRow(){
        int y = (int)(Math.random()*(-Res.screenHight)-3*Res.blockSize);
        for(int i =0;i<5;i++){
            point = new Point();
            point.y = y;
            point.x = (i+1)*Res.blockSize*2 - Res.blockSize;
            blocks.add(new Block(point));
            Block lastBlock = blocks.get(blocks.size()-1);
            for (int j = 0;j<blocks.size()-1;j++){
                int divY = lastBlock.getY() - blocks.get(j).getY();
                if (Math.abs(divY)<Res.blockSize*3 && lastBlock.getX() == blocks.get(j).getX()) {
                    removeBlock(blocks.get(j));
                }
            }
            for (Feed f : feeds){
                if (Math.abs(point.x - f.getX())<Res.blockSize + 2*Res.feedSize &&
                        Math.abs(point.y - f.getY())<Res.blockSize + 3*Res.feedSize)
                    toRemoveFeeds.add(f);
            }
        }

    }

    public void createFeed(){
        if(feeds.size()<6){
            point = new Point();
            point.y = (int)(Math.random()*(-Res.screenHight*2)-Res.feedSize);
            point.y = (int)Math.floor(point.y/(Res.feedSize*5))*(Res.feedSize*5);
            point.x = Res.feedSize*4 +  (int)(Math.random()*(Res.screenWidth - Res.feedSize*4)-Res.feedSize*4);
            point.x = (int)Math.floor(point.x/(Res.feedSize*6))*(Res.feedSize*6)+Res.feedSize*3;
            if (checkFeedPos(point))
                feeds.add(new Feed(point, Color.RED));
            else
                createFeed();
        }
    }

    private boolean checkFeedPos(Point p){
        for(Feed f  : feeds){
            int divY = Math.abs(p.y - f.getY());
            if (divY<Res.feedSize*3){
                return false;
            }
        }
        for(Block b : blocks){
            int divY = Math.abs(p.y - b.getY());
            if (divY<Res.blockSize*3){
                return false;
            }
        }
        return true;
    }
    public void removeFeed(Feed feed){
        feeds.remove(feed);
        createFeed();
    }

    public void removeFeeds(ArrayList<Feed> remFeeds){
        feeds.removeAll(remFeeds);
        createFeed();
    }

    public void removeBlocks(ArrayList<Block> remBlocks){
        blocks.removeAll(remBlocks);
        for (int i=0 ; i<remBlocks.size();i++)
            createBlock();
    }

    public void removeBlock(Block block){
        toRemoveBlocks.add(block);
        System.out.println("REMOVE BLOCK!!!");
        //createBlock();
    }

    public void draw(Canvas canvas){
        for(Feed f : feeds){
            f.draw(canvas);
        }
        for(Block b : blocks)
                b.draw(canvas);
    }

    public void update(){
        if(!paused)
            distance += Res.speed;
        if(distance>Res.screenHight*2.5){
            createBlockRow();
            distance = 0;
        }

        for(Feed f : feeds){
            f.update();
            if(f.getY()>Res.screenHight){
                toRemoveFeeds.add(f);
            }
        }
        removeFeeds(toRemoveFeeds);
        toRemoveFeeds.clear();
        for(Block b : blocks){
            b.update();
            if (b.getRect().top>Res.screenHight){
                toRemoveBlocks.add(b);
            }
        }
        if (toRemoveBlocks.size()>5)
            System.out.println(toRemoveBlocks.size());
    }

}
