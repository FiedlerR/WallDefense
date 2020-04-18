package com.mygdx.game;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rafael Fiedler on 17.06.2017.
 */
public class Bullet {
    float x;
    float y;
    float zielX;
    float zielY;
    int arc;
    int speed;
    boolean selfKill;
//25
    public Bullet(int x, int y, int zielX, int zielY, int arc, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.arc = arc;
        float a =zielX-x;
        float b =zielY-y;
        float rX = (float) (a/Math.sqrt(a*a+b*b));
        float rY = (float) (b/Math.sqrt(a*a+b*b));
        this.zielX = rX;
        this.zielY = rY;
    }

    void processBullet() {
        Main.batch.draw(Main.assets[17],x,y,32, 32, 64, 64, 1, 1,arc, 0, 0, 64, 64, false, false);
        if(!Main.caseOfFire) {
            for (int i = 0; i < Main.enemys.size(); i++) {
                if (Intersector.overlaps(new Circle(x + 32, y + 32, 15), new Rectangle(Main.enemys.get(i).x, Main.enemys.get(i).y, 35, 43))) {
                    Main.enemys.get(i).x += zielX * speed;
                    Main.enemys.get(i).y += zielY * speed;
                    Main.enemys.get(i).treffer = System.currentTimeMillis();
                    Main.enemys.get(i).life -= 50;
                    selfKill = true;
                }
            }
            x += zielX * speed;
            y += zielY * speed;
        }
    }

}

