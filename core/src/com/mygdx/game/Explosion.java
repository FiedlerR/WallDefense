package com.mygdx.game;

/**
 * Created by Rafael Fiedler on 01.07.2017.
 */
public class Explosion {
    int x;
    int y;
    int width;
    int height;
    int timeToLife = 1;
    int aniCounter;

    public Explosion(int x, int y , int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height =height;
    }

   boolean process() {
        if (timeToLife > 0) {
            Main.batch.draw(Main.assets[28+(int)(aniCounter/10)],x,y,width,height);//8
            aniCounter++;
            if(aniCounter == 15){
                timeToLife = 0;
            }
            //timeToLife--;
            return true;
        }
        return false;
    }
}
