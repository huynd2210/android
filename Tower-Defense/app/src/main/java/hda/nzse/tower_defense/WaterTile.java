package hda.nzse.tower_defense;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WaterTile extends Tile{

    public WaterTile(GameManager gameManager, int posX, int posY, int sizeX, int sizeY) {
        super(gameManager, posX, posY, sizeX, sizeY);
        // TODO: add background for waterTiles
        super.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.water2));
    }
}
