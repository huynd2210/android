package hda.nzse.tower_defense;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GrassTile extends Tile{

    private Tower tower;
    private boolean hasTower = false;

    // edges of GrassTile
    private int top, bottom, left, right;

    // Show marked area that has been selected
    private Rect selection;
    private Paint selectionPaint;
    private boolean showSelection = false;

    public GrassTile(GameManager gameManager, int posX, int posY, int sizeX, int sizeY) {
        super(gameManager, posX, posY, sizeX, sizeY);
        //TODO: add tile image
        setImage(BitmapFactory.decodeResource(getResources(), R.drawable.grass5));
        calculateTopBottomLeftRight();
        initRect();
    }

    public void placeTower(Tower tower){
        this.tower = tower;
    }

    public int sellTower(){
        // Abort
        if(tower == null)
            return 0;
        // Sell
        int towerPrice = 0;
//     todo:   towerPrice = tower.getPrice();
        tower = null;
        return towerPrice;
    }

    // looks for TouchEvent and opens buyMenu
    public void update(int posX, int posY){
        // Abort
        if(posX > right || posX < left || posY > bottom || posY < top) {
            // Not in GrassTile
            showSelection = false;
            return;
        }

        if(!showSelection) {
            showSelection = true;
        }
        if (hasTower) {
            // open upgrade menu in GameManager
            getGameManager().openBuyOrUpgradeMenu(this, 1);
        } else {
            // open buy menu in GameManager
            getGameManager().openBuyOrUpgradeMenu(this, 0);
        }
    }

    public void calculateTopBottomLeftRight(){
        top = getPosY() - getImage().getHeight()/2;
        bottom = getPosY() + getImage().getHeight()/2;
        left = getPosX() - getImage().getWidth()/2;
        right = getPosX() + getImage().getWidth()/2;
    }

    public void initRect(){
        // create Rectangle for selected tile
        selection = new Rect(left, top, right, bottom);
        selectionPaint = new Paint();
        selectionPaint.setColor(Color.RED);
        selectionPaint.setStrokeWidth(5);
        selectionPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        if(showSelection)
            canvas.drawRect(selection, selectionPaint);
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public boolean isHasTower() {
        return hasTower;
    }

    public void setHasTower(boolean hasTower) {
        this.hasTower = hasTower;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}
