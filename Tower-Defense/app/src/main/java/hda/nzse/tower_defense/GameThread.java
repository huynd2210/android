package hda.nzse.tower_defense;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private final int TARGET_FPS = 30;
    private final boolean DISPLAY_FPS = false;

    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;
    private double averageFPS= 0;
    private GameActivity activity;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.activity = (GameActivity) gameView.getContext();
    }


    @Override
    public void run() {

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / TARGET_FPS;
        double lastFrameDur = targetTime / 1000.0;  //Frame Duration in seconds

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update(lastFrameDur);
                    this.activity.update(lastFrameDur);
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) { Log.d("GameThread", e.toString());
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                Thread.sleep(waitTime);
            } catch (Exception e) {}

            if(DISPLAY_FPS){
                totalTime += System.nanoTime() - startTime;
                frameCount++;

                if (frameCount == TARGET_FPS)        {
                    averageFPS = Math.round(100000.0 / ((totalTime / frameCount) / 1000000.0))/100.0;
                    frameCount = 0;
                    totalTime = 0;
                    System.out.println(averageFPS);
                    Log.d("GameThread", "averageFPS: "+averageFPS);
                }
            }
            lastFrameDur = (System.nanoTime() - startTime) / 1000000000.0;
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    public boolean isRunning() {
        return running;
    }
}
