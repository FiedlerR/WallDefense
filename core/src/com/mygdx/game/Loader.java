package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Rafael Fiedler on 26.02.2017.
 */
public class Loader {

    public Sound[] loadSound(String path) {//load all sounds into a Array
        FileHandle dir = new FileHandle(path);
        FileHandle files [] = dir.list("wave");
        Sound sound [] = new Sound[files.length];
        for (int i = 0; i < sound.length; i++) {
            sound[i] =  Gdx.audio.newSound(Gdx.files.internal(path+"/"+i+".wav"));
        }
        return sound;
    }

    public Texture[] loadTexture(String path) {
        FileHandle dir = new FileHandle(path);
        FileHandle files [] = dir.list();
        Texture assets [] = new Texture [37];
        for (int i = 0; i < assets.length; i++) {
            assets[i] = new Texture(Gdx.files.internal(path+"/"+i+".png"));
        }
        return assets;
    }

    public BitmapFont [] loadFont(String path,String files[]) {
            BitmapFont fonts [] = new BitmapFont [files.length];
            for (int i = 0; i < files.length; i++) {
                fonts [i] = new BitmapFont(Gdx.files.internal(path+"/"+files[i]));
            }
            return fonts;
    }

}
