package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rafael Fiedler on 23.06.2017.
 */
public class Wall extends PlayerBuilding{
    int health;

    public Wall(int x,int y) {
        health = 500;
        this.x = x;
        this.y = y;
        this.height = 22;
        this.width = 64;
        hitBox = new Rectangle(x,y,64,22);
    }

    boolean process(){
        if(health <= 0){
            return false;
        }
        return true;
    }

    @Override
    void draw() {
        Main.batch.draw(Main.assets[21],x,y);
    }
}
