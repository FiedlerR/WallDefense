package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rafael Fiedler on 23.06.2017.
 */
public abstract class PlayerBuilding {
    int x ;
    int y;
    int width ;
    int height;
    abstract boolean process();
    abstract void draw ();
    Rectangle hitBox;
}
