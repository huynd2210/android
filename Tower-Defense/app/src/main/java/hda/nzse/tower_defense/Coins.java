package hda.nzse.tower_defense;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Coins {


    private Bitmap coinImage;
    private ArrayList<Bitmap> framesCoinImage = new ArrayList<Bitmap>();
    private int posX, posY;
    private int  screenWidth, screenHeight;
    private boolean isDead;
    private boolean tookDamage = false;
    private boolean showHitbox = false;
    private int targetWaypoint = 0;
    private float frameCounterCoinImage = 0;
    private boolean isSpinning = false;

    private GameManager gameManager;
    private Resources res;

    public Coins(int posX, int posY, GameManager gameManager){
        this.gameManager = gameManager;
        this.res = gameManager.getAppContext().getResources();
        this.coinImage = scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a1));

        this.posX = posX;
        this.posY = posY;

        this.framesCoinImage = new ArrayList<Bitmap>();

        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a1)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a2)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a3)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a4)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a5)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a6)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a7)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a8)));
        framesCoinImage.add(scaleImage(BitmapFactory.decodeResource(res, R.drawable.goldcoin1_a9)));

        isSpinning = true;
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

    public void draw(Canvas canvas){

        if(isSpinning){
            float left = posX - (coinImage.getWidth()/2);
            float top = posY - (coinImage.getHeight()/2);

            canvas.drawBitmap(framesCoinImage.get((int)frameCounterCoinImage), left, top, null);
            frameCounterCoinImage += 0.5;
            if(frameCounterCoinImage >= framesCoinImage.size()){
                frameCounterCoinImage = 0;
                isSpinning = false;

            }
        }
    }
}
