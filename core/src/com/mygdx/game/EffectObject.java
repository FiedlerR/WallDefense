package com.mygdx.game;

/**
 * Created by Rafael Fiedler on 23.06.2017.
 */
public class EffectObject {
    int lifeTime;
    int x;
    int y;
    int type;

    public EffectObject(int x,int y,int lifeTime,int type){
        this.x = x;
        this.y = y;
        this.lifeTime = lifeTime;
        this.type = type;
    }
}
