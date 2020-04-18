package com.mygdx.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rafael Fiedler on 23.06.2017.
 */
public class Mine extends PlayerBuilding {

    public Mine(int x,int y) {
        this.x = x;
        this.y = y;
        this.height = 32;
        this.width = 32;
        hitBox = new Rectangle(x,y,32,32);
    }

    @Override
    boolean process() {
        for (int i = 0; i < Main.enemys.size(); i++) {
            if (Intersector.overlaps(new Rectangle(x, y, 32,32), new Rectangle(Main.enemys.get(i).hitBox))) {
                for (int z = 0; z < Main.enemys.size(); z++) {
                    if (Intersector.overlaps(new Rectangle(x-32, y-32,96,96), new Rectangle(Main.enemys.get(z).hitBox))) {
                        Main.enemys.get(z).life = 0;
                    }
                }
                Main.explosions.add(new Explosion(x-32, y-32,96,96));
                return false;
            }
        }
        return true;
    }

    @Override
    void draw() {
        Main.batch.draw(Main.assets[9],x,y,width,height);
    }
}
