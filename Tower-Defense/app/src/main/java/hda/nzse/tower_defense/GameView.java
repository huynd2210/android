package hda.nzse.tower_defense;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum directions{
    UP,
    DOWN,
    RIGHT,
    LEFT
}

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    // Enter your Game objects here

    // Thread that runs the game
    public GameThread thread;
    private GameManager gameManager;
    private float userX = -1, userY = -1;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);

        // Initialize your game objects here
        directions[] level1 = {directions.RIGHT, directions.RIGHT, directions.UP, directions.UP,
                directions.RIGHT, directions.RIGHT, directions.DOWN, directions.DOWN, directions.RIGHT, directions.RIGHT,
        directions.UP, directions.UP, directions.RIGHT, directions.RIGHT};
        directions[] level2 = {directions.RIGHT, directions.RIGHT, directions.UP, directions.UP,
                directions.RIGHT, directions.RIGHT, directions.RIGHT, directions.RIGHT,
                directions.DOWN, directions.DOWN, directions.RIGHT, directions.RIGHT};
        directions[] level3 = {directions.RIGHT, directions.RIGHT, directions.UP, directions.UP,
                directions.RIGHT, directions.RIGHT, directions.RIGHT, directions.RIGHT,
                directions.RIGHT, directions.RIGHT};

        switch((new AppManager((Activity) getContext())).getPlayedLevel()){
            case 0: gameManager = new GameManager(createPath(new ArrayList<directions>(Arrays.asList(level1))), getContext()); break;
            case 1: gameManager = new GameManager(createPath(new ArrayList<directions>(Arrays.asList(level2))), getContext()); break;
            case 2: gameManager = new GameManager(createPath(new ArrayList<directions>(Arrays.asList(level3))), getContext()); break;
            default: break;
        }
        gameManager.toggleCastleHitbox();
    }

    private Point[] createPath(List<directions> directions){
        List<Point> tiles = new ArrayList<>();
        int x = 0;
        int y = 2;
        for(directions direction : directions){
            switch(direction){
                case UP: tiles.add(new Point(x,y)); y--;
                    break;
                case DOWN: tiles.add(new Point(x,y)); y++;
                    break;
                case LEFT: tiles.add(new Point(x,y)); x--;
                    break;
                case RIGHT: tiles.add(new Point(x,y)); x++;
                    break;
            }
        }
        return tiles.toArray(new Point[0]);
    }



    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        userX = e.getX();
        userY = e.getY();
        Log.d("GameView", "userX = " + userX + ", userY = " + userY);
        Log.d("GameView", "userX rounded = " + Math.round(userX) + ", userY rounded = " + Math.round(userY));
        // TODO: tower shall not be placed with only touch
        // gameManager.placeTower(Math.round(userX), Math.round(userY), 1, towerList);
        gameManager.setShop(2);
        return true;
    }

    // update game logic!
    public void update(double lastFrameDur) {
        // Game objects can be updated here
//        rocket.update(lastFrameDur);

        gameManager.update(Math.round(userX), Math.round(userY), lastFrameDur);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            // Draw your game objects here
//            rocket.draw(canvas);
            gameManager.draw(canvas);
        }
    }

    public GameManager getGameManager() {
        return gameManager;
    }

//    public void changeSpeed(double speedChange){
//        rocket.changeSpeed(speedChange);
//    }

    public void restart() {
        thread = new GameThread(getHolder(), this);
    }

}
