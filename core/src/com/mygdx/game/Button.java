package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Rafael Fiedler on 18.02.2017.
 */
public class Button {
    int x;
    int y;
    int typeClicked;
    int type;
    int width;
    int height;
    int fontType;
    String text;


    public Button (int x ,int y,int width ,int height,String text,int typeClicked, int type, int fontType){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.typeClicked = typeClicked;
        this.type = type;
        this.fontType = fontType;
    }

    public void processButton() {
        if (rectangleCollision(Main.getMouseX(), Main.getMouseY(), 1, 1, x, y, width, height)) {
            Main.batch.draw(Main.assets[typeClicked], x, y, width, height);
        } else {
            Main.batch.draw(Main.assets[type], x, y, width, height);
        }
        Main.layout.setText(Main.fonts[fontType],text, Color.WHITE,width, Align.center,false);
        Main.fonts[fontType].draw(Main.batch,Main.layout,x,y+(height+Main.layout.height)/2);
    }

    public boolean clicked() {
        if (System.currentTimeMillis() - Main.wait >= 300 && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && rectangleCollision(Main.getMouseX(), Main.getMouseY(), 1, 1, x, y, width, height)) {
            Main.wait = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return x + "/" + y;
    }

    public static boolean rectangleCollision(double x1, double y1, double b1, double h1, double x2, double y2,
                                             double b2, double h2) {
        return !(x1 > x2 + b2 || x1 + b1 < x2 || y1 > y2 + h2 || y1 + h1 < y2);
    }

}
