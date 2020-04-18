package com.mygdx.game;

import com.badlogic.gdx.Input;

/**
 * Created by Rafael Fiedler on 11.03.2017.
 */
public class InputObject implements Input.TextInputListener {
    int modul;

    public InputObject(int modul) {
        super();
        this.modul = modul;
    }

    @Override
    public void input(String text) {
       Main.name = text;
    }

    @Override
    public void canceled() {

    }
}
