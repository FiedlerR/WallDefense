package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.math.MathContext;

/**
 * Created by Rafael Fiedler on 17.06.2017.
 */
public class HelpTower extends PlayerBuilding {
    int health;
    int arc = 0;
    int fireRate = 600;
    long timer;
    private int zielArc;

    public HelpTower(int x,int y) {
        health = 100;
        this.x = x;
        this.y = y;
        this.height = 64;
        this.width = 64;
        hitBox = new Rectangle(x,y,64,64);
    }

    @Override
    boolean process() {
        if(health <= 0){
            return false;
        }
        int zielX = 0;
        int zielY = 0;
        if (Main.enemys.size() > 0) {
            zielX = Main.enemys.get(0).x;
            zielY = Main.enemys.get(0).y;
        }
        float distx = -32 + zielX - x;
        float disty = -32 + zielY - y + 10;
        for (int i = 0; i < Main.enemys.size() ; i++) {
            if (distance( -32 + Main.enemys.get(i).x - x,-32 + Main.enemys.get(i).y - y + 10) < distance(distx,disty)) {
                zielY = Main.enemys.get(i).y;
                zielX = Main.enemys.get(i).x;
                distx = -32 + zielX - x;
                disty = -32 + zielY - y + 10;
            }
        }

        if(-32 + zielY - y < 32) {
            disty = 0;
        }
        zielArc = Math.abs((int) Math.toDegrees(Math.atan2(disty, distx)));

        if(!Main.caseOfFire && zielArc == arc && System.currentTimeMillis() - timer > fireRate){
            Main.bullets.add(new Bullet(x,y,zielX,zielY,arc,5));
            timer = System.currentTimeMillis();
        }

        if(zielArc > arc){
            arc++;
        } else if (zielArc < arc) {
            arc--;
        }
        return true;
    }

    @Override
    void draw() {
        Main.batch.draw(Main.assets[16],x,y);
        Main.batch.draw(Main.assets[11],x,y,32, 32, 64, 64, 1, 1,arc, 0, 0, 64, 64, false, false);
    }


    int distance (float x,float y){
        return (int) Math.sqrt(x*x+y*y);
    }
}
