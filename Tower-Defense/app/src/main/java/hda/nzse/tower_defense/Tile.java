package hda.nzse.tower_defense;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Parent class of all tiles
 */
public abstract class Tile {
    // image of the Tile
    private Bitmap image;
    // positions
    private int posX;
    private int posY;
    // width of device
    private int screenWidth;
    // height of device
    private int screenHeight;

    private int sizeX, sizeY;

    // connection with gameManager
    private Resources resources;
    private GameManager gameManager;

    public Tile(GameManager gameManager, int posX, int posY, int sizeX, int sizeY){
        this.posX = posX;
        this.posY = posY;
        this.gameManager = gameManager;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        resources = gameManager.getAppContext().getResources();
    }

    /**
     * draws the Tile
     *
     * @param canvas Canvas on which the tile shall be drawn
     */
    public void draw(Canvas canvas){
        screenHeight = canvas.getHeight();
        screenWidth = canvas.getWidth();

        float left = posX - (image.getWidth()/2);
        float top = posY - (image.getHeight()/2);

        canvas.drawBitmap(image, left, top, null);
    }

    /**
     * update method which includes the logic of the tile.
     *
     * Currently this tile does nothing, so there is no logic.
     *
     * Should be overwritten by a tile with logic.
     */
    public void update(){
        // Tile lies on the ground, does nothing
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

    public void setImage(Bitmap bmp){
        // initialize image
        image = bmp;
        // scale image to aspect ratio
        int height = sizeY;
        int width = sizeX;
        // Set image as scaled Bitmap
        image = Bitmap.createScaledBitmap(image, width, height, false);
    }

    public Bitmap getImage(){return image;}

    public GameManager getGameManager(){ return gameManager; }

    public Resources getResources(){ return resources; }

    public int getScreenWidth(){ return screenWidth; }

    public int getScreenHeight(){ return  screenHeight; }

    public int getSizeX(){ return sizeX; }

    public int getSizeY(){ return sizeY; }
}
