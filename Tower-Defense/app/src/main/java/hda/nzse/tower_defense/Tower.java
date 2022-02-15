package hda.nzse.tower_defense;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class resembles the towers, which can be placed inside a TableLayout.
 *
 * Tower are locating and shooting an enemy automatically.
 * They are looking within in a radius to find an enemy and won't stop shooting it,
 * until it is destroyed.
 *
 */
public class Tower {

    private Bitmap image;
    private Bitmap imageUpgrade;
    private BoundingBox2D hitbox;

    private ArrayList<Bitmap> framesImage, framesImageUpgrade;
    private float frameCounterImage = 0, frameCounterImageUpgrade = 0;

    private int posX = 0, posY = 0;

    private int screenWidth;
    private int screenHeight;

    private int towerType;
    private int price;

    private int level;
    private int range;

    private Enemy target;
    private boolean hasTarget;

    private boolean isSold = false;

    private int damage;
    private double attackSpeed;

    private GameManager gameManager;
    private Resources res;

    private long timeOfLastAttack;

    private boolean showHitbox = false;

    private boolean isAttacking;

    public Tower(GameManager gameManager, int towerType, int posX, int posY, GrassTile targetTile){
        // set resources
        this.res = gameManager.getAppContext().getResources();
        // set image
        //setImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl1_a1));
        //setImageUpgrade(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl2_a1));

        image = scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl1_a1));
        imageUpgrade = scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl2_a1));

        setTowerType(towerType);

        Log.d("", "towerType"+" " + towerType + " " + this.towerType);
        setLevel(1);

        setGameManager(gameManager);

        targetTile.setTower(this);
        targetTile.setHasTower(true);

        this.posX = posX;
        this.posY = posY;

        this.timeOfLastAttack = System.nanoTime();
        createHitbox();
        fillFrames();

        //this.price = towerType * 2;
    }

    public void upgrade() {
        if(this.level >= 2){
            return;
        }
        this.level++;
        this.damage += 2;
        this.attackSpeed /= 2;
    }

    public void shootTarget() {
        if(!hasTarget){
            return;
        }
        else {
            double timeSinceLastAttack = (System.nanoTime() - timeOfLastAttack) / 100000000;
            if(timeSinceLastAttack >= attackSpeed) {
                target.takeDamage(damage, towerType);
                timeOfLastAttack = System.nanoTime();
            }
            isAttacking = true;
        }
    }

    public void checkForTarget(ArrayList<Enemy> possibleTargets) {
        // Abort
        if(possibleTargets == null || possibleTargets.isEmpty()){
            this.target = null;
            this.hasTarget = false;
            return;
        }

        // Only needs to check if it does not have a target
            for (Enemy e : possibleTargets) {
                if (hitbox.collidesWith(e.getHitbox())) {
                    this.target = e;
                    this.hasTarget = true;
                    return;
                } else {
                    this.target = null;
                    this.hasTarget = false;
                }
            }
    }

    public void sell(){
        this.isSold = true;
        this.hitbox = null;

        gameManager.gainGold(towerType*2*level);
        target = null;
        hasTarget = false;
        Log.d("Tower", "getTowers before" + " " + gameManager.getTowers().size());
        gameManager.getTowers().remove(this);
        Log.d("Tower", "getTowers after" + " " + gameManager.getTowers().size());
        gameManager.getSelectedTile().setTower(null);
        gameManager.getSelectedTile().setHasTower(false);
    }

    public void draw(Canvas canvas) {
        if(isSold) {
            return;
        }

        screenWidth = canvas.getWidth();
        screenHeight = canvas.getHeight();

        Log.d("Tower", "towerLevel" + " " + level);

        if(level == 1){
            // Translation in y-axis, because Tower images are longer than tile images
            float left = posX - (image.getWidth()/2);
            float top = posY - (image.getHeight()/2);
            float topOffset = image.getHeight()/4;

            //canvas.drawBitmap(image, left, top - topOffset, null);
            if(!isAttacking)
            {
                canvas.drawBitmap(framesImage.get(0), left, top - topOffset, null);
            }
            else{
                canvas.drawBitmap(framesImage.get((int)frameCounterImage), left, top - topOffset, null);

                frameCounterImage += 0.5;
                if(frameCounterImage >= framesImage.size()){
                    frameCounterImage = 0;
                    isAttacking = false;
                }
            }

        }
        else if( level == 2) {
            float left = posX - (image.getWidth()/2);
            float top = posY - (image.getHeight()/2);
            float topOffset = image.getHeight()/4;

            //canvas.drawBitmap(imageUpgrade, left, top - topOffset, null);
            if(!isAttacking){
                canvas.drawBitmap(framesImageUpgrade.get(0), left, top - topOffset, null);
            }
            else {
                canvas.drawBitmap(framesImageUpgrade.get((int)frameCounterImageUpgrade), left, top - topOffset, null);

                frameCounterImageUpgrade += 0.5;
                if (frameCounterImageUpgrade >= framesImageUpgrade.size()) {
                    frameCounterImageUpgrade = 0;
                    isAttacking = false;
                }
            }
        }
        if(showHitbox){
            Paint hitboxPaint = new Paint();
            hitboxPaint.setColor(Color.YELLOW);
            hitboxPaint.setStrokeWidth(5);
            hitboxPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(hitbox.getLeft(), hitbox.getUp(), hitbox.getRight(), hitbox.getBottom(), hitboxPaint);
        }
    }

    public void update(double lastFrameDur) {
        if(isSold){
            return;
        }
        checkForTarget(gameManager.getCurrentWave());
        shootTarget();
    }


    public void fillFrames(){
        framesImage = new ArrayList<Bitmap>();
        framesImageUpgrade = new ArrayList<Bitmap>();

        if(towerType == 1){
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl1_a1)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl1_a2)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl1_a3)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl1_a4)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl1_a5)));

            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl3_a1)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl3_a2)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl3_a3)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl3_a4)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.stonetower_lvl3_a5)));
        }
        else if(towerType == 2){
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl1_a1)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl1_a2)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl1_a3)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl1_a4)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl1_a5)));

            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl3_a1)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl3_a2)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl3_a3)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl3_a4)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.irontower_lvl3_a5)));
        }
        else if(towerType == 3){
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl1_a1)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl1_a2)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl1_a3)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl1_a4)));
            framesImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl1_a5)));

            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl3_a1)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl3_a2)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl3_a3)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl3_a4)));
            framesImageUpgrade.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.firetower_lvl3_a5)));
        }

    }

    public Bitmap scaleImage(Bitmap bmp) {

        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        this.screenHeight -= screenHeight*0.2;

        float aspectRatio = (float) bmp.getWidth() / (float) bmp.getHeight();

        int height = screenHeight / 5;
        int width = Math.round(height/aspectRatio)/2;

        /*
        int width = screenWidth / 10;
        int height = Math.round(width / aspectRatio);
         */
        bmp = Bitmap.createScaledBitmap(bmp, width, height, false);

        return bmp;
    }

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
//        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public Bitmap getImageUpgrade() {
        return imageUpgrade;
    }

    public void setImageUpgrade(Bitmap bmpUpgrade) {
        this.imageUpgrade = bmpUpgrade;

        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        float aspectRatio = (float) image.getWidth() / (float) image.getHeight();

        int width = screenWidth / 10;
        int height = Math.round(width / aspectRatio);

        imageUpgrade = Bitmap.createScaledBitmap(imageUpgrade, width, height, false);
//        imageUpgrade = Bitmap.createBitmap(imageUpgrade, 0, 0, image.getWidth(), image.getHeight());
    }

    public BoundingBox2D getHitbox() {
        return hitbox;
    }

    public void createHitbox() {
        // create hitbox for image
        //TODO end hitbox at left side of canvas
        int up = posY - image.getHeight()/2;
        up = posY - gameManager.getTiles().get(0).getSizeY()/2;

        int left = posX - image.getWidth()/2;
        left = posX - gameManager.getTiles().get(0).getSizeX()/2;

        int bottom = posY + image.getHeight()/2;
        bottom = posY + gameManager.getTiles().get(0).getSizeY()/2;

        int right = posX + image.getWidth()/2;
        right = posX + gameManager.getTiles().get(0).getSizeX()/2;


        hitbox = new BoundingBox2D(up, left, bottom, right);

        int distUpToBot = hitbox.getBottom() - hitbox.getUp();
        int distRightToLeft = hitbox.getRight() - hitbox.getLeft();

        // take distance and scale range of hitbox
        hitbox = new BoundingBox2D(up - distUpToBot * range, left - distRightToLeft * range,
                bottom + distUpToBot * range, right + distRightToLeft * range);
        Log.d("Tower", "hitbox.getLeft" + " " + hitbox.getLeft());
        if(hitbox.getLeft() <= 0){
            hitbox.setLeft(0);
        }
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

    public int getTowerType() {
        return towerType;
    }

    public void setTowerType(int towerType) {
        if(towerType > 3) Log.d("Tower", "setTowerType() - Exception: towerType > 3");
        if(towerType < 1) Log.d("Tower", "setTowerType() - Exception: towerType < 1");
        if(!(towerType > 3 || towerType < 1)){
            this.towerType = towerType;
        }
        else {
            this.towerType = 1;
        }
        if(towerType == 1){
            this.range = 1;
            this.damage = 2;
            this.attackSpeed = 2;
            this.price = towerType*2;
        }
        else if(towerType == 2){
            this.range = 2;
            this.damage = 2;
            this.attackSpeed = 3;
            this.price = towerType*2;
        }
        else if(towerType == 3){
            this.range = 1;
            this.damage = 4;
            this.attackSpeed = 0.5;
            this.price = towerType*2;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if(level < 1) {
            Log.d("Tower", "setLevel() - Exception: level < 1");
            this.level = 1;
        }
        else
            this.level = level;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public boolean isHasTarget() {
        return hasTarget;
    }

    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        if(gameManager == null) {
            Log.d("Tower", "setGameManager() - Exception: gameManager == null");
        }
        else
            this.gameManager = gameManager;
    }

    public long getTimeOfLastAttack() {
        return timeOfLastAttack;
    }

    public void setTimeOfLastAttack(long timeOfLastAttack) {
        this.timeOfLastAttack = timeOfLastAttack;
    }

    public void showHitbox(boolean flag){
        showHitbox = flag;
    }

    public int getPrice(){ return price; }
}
