package hda.nzse.tower_defense;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Class which represents the enemy to be slain by the towers.
 * Enemies are moving within a grid, split up in rows and columns.
 *
 */
public class Enemy {

    private Bitmap image, hitImage, coinImage;
    private ArrayList<Bitmap> framesImage, framesHitImageStone, framesHitImageIron, framesHitImageFire, framesCoinImage;
    private int posX, posY, velocity, health, damage;
    private int  screenWidth, screenHeight;
    private boolean isDead;
    private boolean tookDamage = false;
    private boolean showHitbox = false;
    private int targetWaypoint = 0;
    private float frameCounterImage = 0, frameCounterHitImage = 0, frameCounterCoinImage = 0;
    private int attackingTowerType = 1;

    private int type = 1;


    private GameManager gameManager;
    private Resources res;

    private Point[] path;
    private BoundingBox2D hitbox;
    private ArrayList<Tile> pathTiles;

    public Enemy(GameManager gameManager){
        this.gameManager = gameManager;
        pathTiles = gameManager.getPathTiles();
        this.res = gameManager.getAppContext().getResources();
        //setImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a1));
        this.image = scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a1));
        //setHitImage(BitmapFactory.decodeResource(res, R.drawable.fireprojectile_a1));
        this.hitImage = scaleImage(BitmapFactory.decodeResource(res, R.drawable.fireprojectile_a1));
        this.coinImage = scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a1));

        this.path = gameManager.getPath();
        this.pathTiles.add(new PathTile(this.gameManager,pathTiles.get(pathTiles.size()-1).getPosX()*2, pathTiles.get(pathTiles.size()-1).getPosY(), 1, 1));
        //set starting position as first point of path
        this.posX = pathTiles.get(0).getPosX() * -2;
        this.posY = pathTiles.get(0).getPosY();
        //this.posX = posX;
        //this.posY = posY;
        targetWaypoint++;
        createHitbox();
        this.health = 400;
        this.velocity = 0;
        this.damage = 1;
        this.res = res;
        this.framesImage = new ArrayList<Bitmap>();
        this.framesHitImageStone = new ArrayList<Bitmap>();
        this.framesHitImageIron = new ArrayList<Bitmap>();
        this.framesHitImageFire = new ArrayList<Bitmap>();
        this.framesCoinImage = new ArrayList<Bitmap>();

        fillFrames();
    }

    public void initEnemy1(int hpMod) {
        this.type = 1;

        this.health = 40 + (hpMod*2);
        this.damage = 1;

        fillFrames();
    }

    public void initEnemy2(int hpMod) {
        this.type = 2;

        this.health = 80 + (hpMod*4);
        this.damage = 2;

        fillFrames();
    }

    public void inflictDamage(){
        //if enemy hitbox collides with castle hitbox
        if(hitbox.collidesWith(gameManager.getCastle())){
            //deal damage to player
            gameManager.takeDamage(damage);
        }
        //afterwards destroy this enemy
        die();
    }

    public void takeDamage(int damageTaken, int attackingTowerType){
        //take damage
        health -= damageTaken;
        this.attackingTowerType = attackingTowerType;


        //Log.d("Enemy", "attackingTowerType"+" " + this.attackingTowerType);
        tookDamage = true;

        //if dead
        if(health <= 0) {
            die();
        }
    }

    public void die(){
        this.isDead = true;
        this.hitbox = null;

        gameManager.getCoins().add(new Coins(this.posX, this.posY, this.gameManager));
        gameManager.getCurrentWave().remove(this);
        gameManager.gainGold(damage);
        gameManager.enemyDefeated();

    }

    public void move(){

        if(isDead){
            return;
        }
        // enemy going right
        if(posX < pathTiles.get(targetWaypoint).getPosX()){
            posX += velocity;
            if(posX >= pathTiles.get(targetWaypoint).getPosX()){
                posX = pathTiles.get(targetWaypoint).getPosX();
                if(targetWaypoint+1 < pathTiles.size()){
                    targetWaypoint++;
                }

            }
        }
        // enemy going left
        if(posX > pathTiles.get(targetWaypoint).getPosX()){
            posX -= velocity;
            if(posX <= pathTiles.get(targetWaypoint).getPosX()){
                posX = pathTiles.get(targetWaypoint).getPosX();
                if(targetWaypoint+1 < pathTiles.size()){
                    targetWaypoint++;
                }

            }
        }
        // Enemy going down
        if(posY < pathTiles.get(targetWaypoint).getPosY()){
            posY += velocity;
            if(posY >= pathTiles.get(targetWaypoint).getPosY()){
                posY = pathTiles.get(targetWaypoint).getPosY();
                if(targetWaypoint+1 < pathTiles.size()){
                    targetWaypoint++;
                }

            }
        }
        // Enemy going up
        if(posY > pathTiles.get(targetWaypoint).getPosY()){
            posY -= velocity;
            if(posY <= pathTiles.get(targetWaypoint).getPosY()){
                posY = pathTiles.get(targetWaypoint).getPosY();
                if(targetWaypoint+1 < pathTiles.size()){
                    targetWaypoint++;
                }
            }
        }
        createHitbox();


    }

    public void draw(Canvas canvas){
        if(isDead){
            return;
        }
        float left = posX - (image.getWidth()/2);
        float top = posY - (image.getHeight()/2);

        canvas.drawBitmap(framesImage.get((int)frameCounterImage), left, top, null);
        frameCounterImage += 0.5;
        if(frameCounterImage >= framesImage.size()){
            frameCounterImage = 0;
        }

        if(tookDamage) {
            left = posX - (hitImage.getWidth() / 2);
            top = posY - (hitImage.getHeight() / 2);

            if(attackingTowerType == 1){
                canvas.drawBitmap(framesHitImageStone.get((int)frameCounterHitImage), left, top, null);
            }
            else if(attackingTowerType == 2){
                canvas.drawBitmap(framesHitImageIron.get((int)frameCounterHitImage), left, top, null);
            }
            else if(attackingTowerType == 3){
                canvas.drawBitmap(framesHitImageFire.get((int)frameCounterHitImage), left, top, null);
            }
            frameCounterHitImage += 0.5;
            if(frameCounterHitImage >= framesHitImageIron.size()){
                frameCounterHitImage = 0;
                tookDamage = false;
            }
        }
        if(isDead) {
            left = posX - (coinImage.getWidth() / 2);
            top = posY - (coinImage.getHeight()/2);

            canvas.drawBitmap(framesCoinImage.get((int)frameCounterCoinImage), left, top, null);

            frameCounterCoinImage += 0.5;
            if(frameCounterCoinImage >= framesCoinImage.size()){
                frameCounterCoinImage = 0;
                isDead = false;
            }
        }

        if(showHitbox){
            Paint hitboxPaint = new Paint();
            hitboxPaint.setColor(Color.CYAN);
            hitboxPaint.setStrokeWidth(5);
            hitboxPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(hitbox.getLeft(), hitbox.getUp(), hitbox.getRight(), hitbox.getBottom(), hitboxPaint);
        }
    }

    public void update(double lastFrameDur){
        if(isDead){
            return;
        }
        move();
    }

    public void fillFrames(){
        framesImage = new ArrayList<Bitmap>();
        framesHitImageStone = new ArrayList<Bitmap>();
        framesHitImageIron = new ArrayList<Bitmap>();
        framesHitImageFire = new ArrayList<Bitmap>();

        framesHitImageStone.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stoneprojectile_a1)));
        framesHitImageStone.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stoneprojectile_a2)));
        framesHitImageStone.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stoneprojectile_a3)));
        framesHitImageStone.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stoneprojectile_a4)));
        framesHitImageStone.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stoneprojectile_a5)));


        framesHitImageIron.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.ironprojectile_a1)));
        framesHitImageIron.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.ironprojectile_a2)));
        framesHitImageIron.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.ironprojectile_a3)));
        framesHitImageIron.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.ironprojectile_a4)));
        framesHitImageIron.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.ironprojectile_a5)));


        framesHitImageFire.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.fireprojectile_a1)));
        framesHitImageFire.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.fireprojectile_a2)));
        framesHitImageFire.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.fireprojectile_a3)));
        framesHitImageFire.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.fireprojectile_a4)));
        framesHitImageFire.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.fireprojectile_a5)));

        if(type == 1){
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a1)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a2)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a3)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a4)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a5)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a6)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a7)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a8)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a9)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a10)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy1_a11)));
        }
        else if(type == 2){
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a1)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a2)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a3)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a4)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a5)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a6)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a7)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a8)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a9)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a10)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.enemy2_a11)));
        }
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a1)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a2)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a3)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a4)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a5)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a6)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a7)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a8)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a9)));
    }

    //---------------------- set und get Methoden --------------------------------------------------
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap bmp) {
        this.image = bmp;

        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        float aspectRatio = (float) image.getWidth() / (float) image.getHeight();

        int width = screenWidth / 10;
        int height = Math.round(width / aspectRatio);

        image = Bitmap.createScaledBitmap(image, width, height, false);
    }

    public void rotateImage(float degrees){
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        // rotate image by degrees
        // x and y are the start of the picture, which is upper left corner
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
    }

    public Bitmap getHitImage() {
        return hitImage;
    }

    public void setHitImage(Bitmap bmp) {
        this.hitImage = bmp;

        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        float aspectRatio = (float) image.getWidth() / (float) image.getHeight();

        int width = screenWidth / 10;
        int height = Math.round(width / aspectRatio);

        hitImage = Bitmap.createScaledBitmap(hitImage, width, height, false);
    }

    public Bitmap scaleImage(Bitmap bmp) {

        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        this.screenHeight -= screenHeight*0.2;

        float aspectRatio = (float) bmp.getWidth() / (float) bmp.getHeight();

        int height = screenHeight / 5;
        int width = Math.round(height/aspectRatio);

        /*
        int width = screenWidth / 10;
        int height = Math.round(width / aspectRatio);
         */
        bmp = Bitmap.createScaledBitmap(bmp, width, height, false);

        return bmp;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public Boolean getIsDead(){
        return isDead;
    }

    public void setIsDead(Boolean isDead){
        this.isDead = isDead;
    }


    public BoundingBox2D getHitbox() {
        return hitbox;
    }

    public void createHitbox() {
        // create hitbox for image
        int up = posY - image.getHeight()/2;
        up = posY - gameManager.getTiles().get(0).getSizeY()/2;

        int left = posX - image.getWidth()/2;
        left = posX - gameManager.getTiles().get(0).getSizeX()/2;

        int bottom = posY + image.getHeight()/2;
        bottom = posY + gameManager.getTiles().get(0).getSizeY()/2;

        int right = posX + image.getHeight()/2;
        right = posX + gameManager.getTiles().get(0).getSizeX()/2;


        hitbox = new BoundingBox2D(up+1, left+1, bottom-1, right-1);
    }

    public void showHitbox(boolean flag){
        showHitbox = flag;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        if(gameManager == null) {
            Log.d("Enemy", "setGameManager() - Exception: gameManager == null");
        }
        else
            this.gameManager = gameManager;
    }
}
