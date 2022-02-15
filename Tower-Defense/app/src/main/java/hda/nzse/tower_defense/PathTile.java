package hda.nzse.tower_defense;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

enum pathDirection {
    NONE,
    TOP_BOT,
    LEFT_RIGHT,
    BOT_LEFT,
    BOT_RIGHT,
    TOP_LEFT,
    TOP_RIGHT
}

public class PathTile extends Tile{

    public PathTile(GameManager gameManager, int posX, int posY, int sizeX, int sizeY) {
        super(gameManager, posX, posY, sizeX, sizeY);
        // TODO: add background for pathTiles
        super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_leftright2));
    }

    public PathTile(GameManager gameManager, int posX, int posY, int sizeX, int sizeY, pathDirection direction){
        super(gameManager, posX, posY, sizeX, sizeY);

        switch(direction){
            case NONE: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_leftright2)); break;
            case TOP_BOT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_topbot2)); break;
            case LEFT_RIGHT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_leftright2)); break;
            case TOP_LEFT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_topleft2)); break;
            case TOP_RIGHT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_topright2)); break;
            case BOT_LEFT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_botleft2)); break;
            case BOT_RIGHT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_botright2)); break;
        }
    }

    public void setImage(pathDirection direction){
        switch(direction){
            case NONE: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_leftright2)); break;
            case TOP_BOT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_topbot2)); break;
            case LEFT_RIGHT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_leftright2)); break;
            case TOP_LEFT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_topleft2)); break;
            case TOP_RIGHT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_topright2)); break;
            case BOT_LEFT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_botleft2)); break;
            case BOT_RIGHT: super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.road_botright2)); break;
        }
    }
}
