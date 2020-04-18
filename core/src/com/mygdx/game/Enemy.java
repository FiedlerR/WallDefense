package com.mygdx.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rafael Fiedler on 14.06.2017.
 */
public class Enemy {
    int x;
    int y;
    int life;
    int arc = 265;
    int texture;
    int end;
    long treffer;
    Rectangle hitBox= new Rectangle(x + 5, y, 25, 10);

    public Enemy(int x,int y){
        this.x = x;
        this.y = y;
        life = 100;
        end = MathUtils.random(0,10);
    }

    void processEnemy() {
        if(treffer != 0 &&System.currentTimeMillis()-treffer < 100) {
            texture = 26;
        }else if (!Main.caseOfFire) {
            treffer = 0;
            texture = 1;
            if(Intersector.overlaps(new Rectangle(x,y,35,43), new Rectangle(480, 0,64,64))){
                Main.health -=0.1;
            }
            if (!Intersector.overlaps(new Rectangle(x, y, 35, 43), new Rectangle(480, 0, 64, 50))) {
                if (y > end) {
                    hitBox.setPosition(x+5,y);
                    for (int i = 0; i < Main.buildings.size(); i++) {
                        if (Main.buildings.get(i) instanceof Wall && Intersector.overlaps(hitBox, Main.buildings.get(i).hitBox)) {
                            ((Wall) Main.buildings.get(i)).health--;
                            return;
                        }else if (Main.buildings.get(i) instanceof HelpTower && Intersector.overlaps(hitBox, Main.buildings.get(i).hitBox)) {
                            ((HelpTower) Main.buildings.get(i)).health--;
                            return;
                        }
                    }
                    y--;
                } else {
                    hitBox.setPosition(x+5,y);
                    for (int i = 0; i < Main.buildings.size(); i++) {
                        if (Main.buildings.get(i) instanceof HelpTower && Intersector.overlaps(hitBox, Main.buildings.get(i).hitBox)) {
                            ((HelpTower) Main.buildings.get(i)).health--;
                            return;
                        }
                    }
                    if (x > 512) {
                        arc = 175;
                        x--;
                    } else if(x < 480){
                        arc = -5;
                        x++;
                    }
                }
            }
        } else {
            texture = 1;
        }
    }


    void draw() {
        Main.batch.draw(Main.assets[texture], x, y, 17, 21, 35, 43, 1, 1, arc + + Main.enemyTick, 0, 0, 35, 43, false, false);
    }
}
