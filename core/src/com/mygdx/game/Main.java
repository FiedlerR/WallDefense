package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.utils.Align;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
*written by Rafael Fiedler, Juli 2017
*/

public class Main extends ApplicationAdapter {
    static Texture[] assets;
    static Sound[] sound;
    static Texture gameFields[][];
    static BitmapFont[] fonts;
    static ArrayList<Enemy> enemys = new ArrayList<Enemy>();
    private static OrthographicCamera camera;
    static SpriteBatch batch;
    int stage = 0;
    long kills = 0;
    long score = 11000;
    long time = 0;
    static double health = 100;
    static long wait;
    int enemyCounter = 5;
    static GlyphLayout layout;
    private int playerArc = 0;
    static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    static ArrayList<PlayerBuilding> buildings = new ArrayList<PlayerBuilding>();
    static ArrayList<Explosion> explosions = new ArrayList<Explosion>();
    int fireRate = 300;
    int mouseMode = 0;
    private long fireTime;
    static boolean caseOfFire = false;
    static int enemyTick;
    int money = 0;
    int sin;
    static String playerName = "";
    String[] names = new String[10];
    long[] scores = new long[10];
    private long highscore = 100000;
    long stageTime = 0;
    private boolean isSpawnActiv = true;
    public static String name = "unnamend";
    private int [] yK = new int [200];
    private int [] xK = new int [200];
    private float [] alpha = new float [200];
    private double alphaText = 0;
    private double alphaTextDir = 1;
    boolean reload = false;


    public void create() {
        gameFields = new Texture[50][32];
        Loader data = new Loader();
        assets = data.loadTexture("texture");
        sound = data.loadSound("sound");
        fonts = data.loadFont("fonts", new String[]{"arial75.fnt", "arCena_40.fnt", "arial20.fnt","Arial_35.fnt"});//"arCena_40.fnt"
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);
        batch = new SpriteBatch();
        layout = new GlyphLayout();
        setGameFields();
        Pixmap pm = new Pixmap(Gdx.files.internal("cursor_1.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 16, 16));
        pm.dispose();
       //sendScore();
      //  Gdx.input.getTextInput(new InputObject(1), "change max. Objects", "", "new value");
      //  scores = getScore();
    }

    private void sendScore() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("game_id","266239");
        params.put("score",String.valueOf(score));
        params.put("guest",name);
        params.put("sort",String.valueOf(score));
        params.put("table_id","270727");
        /*
        Request
         */
    }

    protected long [] getScore() {
        final long playerScore = score;
        Map<String, String> params = new HashMap<String, String>();
        final long[] scores = new long[10];
        params.put("game_id","266239");
        params.put("table_id","270727");
        reload =  false;
        /*
        request
         */
        return scores;
    }

    private String encodeJson(String code,String key,int indexStart) {
        System.out.println("Debug Json:");
        String value = code.substring(indexStart+1);
        value = value.substring(value.indexOf("\""+key+"\"")+2+key.length());
        int newIndex = indexStart+value.indexOf("\""+key+"\"")+2+key.length();
        value = value.substring(2);
        value = value.substring(0,value.indexOf("\""));
        System.out.println(value+"#");
        return value+":"+newIndex;
    }

    private void setGameFields() {
        for (int x = 0; x < gameFields.length; x++) {
            for (int y = 1; y < gameFields[0].length; y++) {
                gameFields[x][y] = assets[MathUtils.random(2, 5)];
            }
        }
        for (int i = 0; i < gameFields.length; i++) {
            gameFields[i][0] = assets[6];
        }
    }

    public void resize(int width, int height) {

    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        switch (stage) {
            case 0:
                drawMainmenu();
                break;
            case 1:
                gameProcess();
                break;
            case 2:
                drawGameOver();
                break;
        }
        if(reload) {
            reload = false;
        }
        batch.end();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void drawGameOver() {
        gameProcess();
        caseOfFire = true;
        drawCenterText(fonts[0], "Game Over", 0, 600, 1024);
        drawCenterText(fonts[1], "Score: " + score, 0, 500, 1024);
      //  drawCenterText(fonts[1], "Click here to continue", 0, 420, 1024);
        Main.layout.setText(fonts[1], "Click here to continue", new Color(1, 1, 1, (float) alphaText), 1024, Align.center, false);
        fonts[1].draw(batch, layout, 0, 420);
        if (alphaText <= 0) {
            alphaTextDir = 0.01;
        }
        if (alphaText >= 1) {
            alphaTextDir = -0.01;
        }
        alphaText += alphaTextDir;
        health = 0;
        if (mouseCollision(300,380,400,40) && System.currentTimeMillis() - stageTime > 1000 && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            stage = 0;
            stageTime = System.currentTimeMillis();
        }
    }

    private void gameProcess() {
        if(enemys.size() == 0 && !isSpawnActiv){
            isSpawnActiv = true;
            enemyCounter*=2;
        }
        if(enemys.size() == enemyCounter) {
            isSpawnActiv = false;
        }
        drawGameField();
        if(isSpawnActiv) {
            spawnEnemys();
        }
        enemyAni();
        for (int i = 0; i < buildings.size(); i++) {
            if (!buildings.get(i).process()) {
                buildings.remove(i);
            }
        }
        for (int i = 0; i < buildings.size(); i++) {
            if (!(buildings.get(i) instanceof HelpTower)) {
                buildings.get(i).draw();
            }
        }
        for (int i = 0; i < enemys.size(); i++) {
            enemys.get(i).processEnemy();
            if (enemys.get(i).life <= 0) {
                score += 5;
                money += 25;
                for (int z = 0; z < yK.length;z++) {
                    if (alpha[z] <= 0) {
                        yK[z] = enemys.get(i).y;
                        xK[z] = enemys.get(i).x;
                        alpha[z] = 1;
                        break;
                    }
                }
                enemys.remove(i);
            }
        }
        for (int i = 0; i < enemys.size(); i++) {
            enemys.get(i).draw();
        }

        batch.draw(assets[16], 480, 0);
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).processBullet();
            if (bullets.get(i).selfKill || bullets.get(i).x > 1024 || bullets.get(i).x < -64 || bullets.get(i).y > 768) {
                bullets.remove(i);
            }
        }
        for (int i = 0; i < buildings.size(); i++) {
            if ((buildings.get(i) instanceof HelpTower)) {
                buildings.get(i).draw();
            }
        }
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).process();
        }
        batch.draw(assets[11], 480, 0, 32, 32, 64, 64, 1, 1, playerArc, 0, 0, 64, 64, false, false);
        handlePlayer();
        handleInput();
        drawBuildPanel();
        drawPointLabel();
        drawCursor();
        if (health <= 0 && stage == 1) {
            stage = 2;
            stageTime = System.currentTimeMillis();
        }
    }

    private void enemyAni() {
        if (!caseOfFire) {
            sin += 5;
            if (sin >= 180) {
                sin = 0;
            }
            enemyTick = (int) (Math.sin(Math.toRadians(sin)) * 10);
        }
    }

    private void drawCursor() {
        switch (mouseMode) {
            case 1:
                for (int i = 0; i < Main.buildings.size(); i++) {
                    if (getMouseY() < 80 || Intersector.overlaps(new Rectangle(Main.buildings.get(i).x, Main.buildings.get(i).y, Main.buildings.get(i).width, Main.buildings.get(i).height), new Rectangle(getMouseX() - 32, getMouseY() - 11, 64, 22))) {
                        batch.draw(assets[22], getMouseX() - 32, getMouseY() - 32);
                        return;
                    }
                }
                batch.draw(assets[19], getMouseX() - 32, getMouseY() - 32);
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !(getMouseX() > 768 && getMouseY() > 668)) {
                    if (money >= 25) {
                        buildings.add(new Wall(getMouseX() - 32, getMouseY() - 11));
                        money -= 25;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < Main.buildings.size(); i++) {
                    if (getMouseY() < 80 || Intersector.overlaps(new Rectangle(Main.buildings.get(i).x, Main.buildings.get(i).y, Main.buildings.get(i).width, Main.buildings.get(i).height), new Rectangle(getMouseX() - 16, getMouseY() - 16, 32, 32))) {
                        batch.draw(assets[18], getMouseX() - 16, getMouseY() - 16, 32, 32);
                        return;
                    }
                }
                batch.draw(assets[25], getMouseX() - 16, getMouseY() - 16, 32, 32);
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !(getMouseX() > 768 && getMouseY() > 668)) {
                    if (money >= 50) {
                        buildings.add(new Mine(getMouseX() - 16, getMouseY() - 16));
                        money -= 50;
                    }
                }
                break;
            case 3:
                batch.draw(assets[27], 0, 0, 480, 64);
                batch.draw(assets[27], 544, 0, 480, 64);
                if (getMouseY() < 40 && (getMouseX() < 448 || getMouseX() > 586)) {
                    for (int i = 0; i < Main.buildings.size(); i++) {
                        if (Intersector.overlaps(new Rectangle(Main.buildings.get(i).x, Main.buildings.get(i).y, Main.buildings.get(i).width, Main.buildings.get(i).height), new Rectangle(getMouseX() - 32, getMouseY() - 32, 64, 64))) {
                            batch.draw(assets[24], getMouseX() - 32, getMouseY() - 32);
                            return;
                        }
                    }
                    batch.draw(assets[20], getMouseX() - 32, getMouseY() - 32);
                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !(getMouseX() > 768 && getMouseY() > 668)) {
                        if (money >= 200) {
                            buildings.add(new HelpTower(getMouseX() - 32, getMouseY() - 32));
                            money -= 200;
                        }
                    }
                } else {
                    batch.draw(assets[24], getMouseX() - 32, getMouseY() - 32);
                }
                break;
        }
    }

    private void handlePlayer() {
        float disty = getMouseY() - 32;
        float distx = getMouseX() - 512;
        if (getMouseY() < 32) {
            disty = 0;
        }
        playerArc = Math.abs((int) Math.toDegrees(Math.atan2(disty, distx)));
    }


    void drawPointLabel() {
        for (int i = 0; i < yK.length; i++) {
            if (alpha[i] >= 0) {
                Main.layout.setText(fonts[3], "+5 ps.", new Color(1, 1, 1, alpha[i]), 20, Align.center, false);
                fonts[3].draw(batch, layout, xK[i], yK[i]);
                yK[i]++;
                alpha[i] -= 0.01;
            }
        }
    }

    private void drawBuildPanel() {
        batch.draw(assets[7], 768, 668);
        if (mouseCollision(944, 668, 64, 64)) {
            batch.draw(assets[14], 944, 668);
        } else {
            batch.draw(assets[8], 944, 668);
        }
        if (mouseCollision(864, 668, 64, 64)) {
            batch.draw(assets[13], 864, 668);
        } else {
            batch.draw(assets[9], 864, 668);
        }
        if (mouseCollision(784, 668, 64, 64)) {
            batch.draw(assets[12], 784, 668);
        } else {
            batch.draw(assets[10], 784, 668);
        }
        fonts[2].draw(batch, "200$", 790, 758);
        fonts[2].draw(batch, "50$", 880, 758);
        fonts[2].draw(batch, "25$", 954, 758);

        fonts[1].draw(batch, "Score: " + score, 0, 768);
        fonts[1].draw(batch, "Health: " + (int) health, 0, 728);
        fonts[1].draw(batch, "Money: " + money + "$", 400, 768);
    }

    public static boolean mouseCollision(double x, double y, double b, double h) {
        return (getMouseX() > x && getMouseX() < x + b && getMouseY() > y && getMouseY() < y + h);
    }

    private void handleInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!(getMouseX() > 784 && getMouseY() > 668) && mouseMode == 0 && System.currentTimeMillis() - fireTime > fireRate) {
                fireTime = System.currentTimeMillis();
                if (getMouseY() < 32) {
                    bullets.add(new Bullet(480, 0, getMouseX() - 32, 0, playerArc, 6));
                } else {
                    bullets.add(new Bullet(480, 0, getMouseX() - 32, getMouseY() - 32, playerArc, 6));
                }
            } else {
                if (mouseCollision(944, 668, 64, 64)) {
                    mouseMode = 1;
                    caseOfFire = true;
                }
                if (mouseCollision(864, 668, 64, 64)) {
                    mouseMode = 2;
                    caseOfFire = true;
                }
                if (mouseCollision(784, 668, 64, 64)) {
                    mouseMode = 3;
                    caseOfFire = true;
                }
            }
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            mouseMode = 0;
            caseOfFire = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            health = 0;
        }
    }

    private void spawnEnemys() {
        if (!caseOfFire) {
            if (enemys.size() < enemyCounter) {
                int x = MathUtils.random(0, 960);
                int y = MathUtils.random(832, 1100);
                if (enemys.size() > 0) {
                    boolean test = true;
                    for (int i = 0; i < enemys.size(); i++) {
                        if (Intersector.overlaps(new Rectangle(x, y, 35, 43), new Rectangle(enemys.get(i).x - 2, enemys.get(i).y, 39, 43))) {
                            test = false;
                        }
                    }
                    if (test) {
                        enemys.add(new Enemy(x, y));
                    }
                } else {
                    enemys.add(new Enemy(x, y));
                }
            }
        }
    }

    private void drawGameField() {
        for (int x = 0; x < gameFields.length; x++) {
            for (int y = 0; y < gameFields[0].length; y++) {
                batch.draw(gameFields[x][y], x * 64, y * 64);
            }
        }
    }

    private void drawMainmenu() {
        drawGameField();
        //drawHighscoreTable(212, 600, 300);//window 1024
        drawCenterText(fonts[0], "Wall Defense", 0, 700, 1024);
        Main.layout.setText(fonts[1], "Click here to continue", new Color(1, 1, 1, (float) alphaText), 1024, Align.center, false);
        fonts[1].draw(batch, layout, 0, 100);
        if (alphaText <= 0) {
            alphaTextDir = 0.01;
        }
        if (alphaText >= 1) {
            alphaTextDir = -0.01;
        }
        alphaText += alphaTextDir;
        if (mouseCollision(340,60,350,50) && System.currentTimeMillis() - stageTime > 1000 && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            stage = 1;
            health = 100;
            score = 0;
            money = 0;
            enemyCounter = 5;
            caseOfFire = false;
            buildings.removeAll(buildings);
            enemys.removeAll(enemys);
            for (int i = 0; i < 16; i++) {
                buildings.add(new Wall(64*i,64));
            }
        }
    }

    public void dispose() {

    }

    static int getMouseX() {
        return (int) camera.unproject(new Vector3(Gdx.input.getX(), 0, 0)).x;
    }

    static int getMouseY() {
        return (int) camera.unproject(new Vector3(0, Gdx.input.getY(), 0)).y;
    }

    void drawCenterText(BitmapFont font, String text, int x, int y, int width) {
        Main.layout.setText(font, text, Color.WHITE, width, Align.center, false);
        font.draw(batch, layout, x, y);
    }

    private void drawHighscoreTable(int x, int y, int width) {
        fonts[1].draw(batch, "Highscore:", x, y);
        for (int i = 0; i < 10; i++) {
            //fonts[1].draw(batch, names[i] + ":", x, y - (40 * i) - 40);
           // fonts[1].draw(batch, String.valueOf(scores[i]), x + 400, y - (40 * i) - 40);
        }
    }
}
