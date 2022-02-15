package hda.nzse.tower_defense;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class who manages all objects within one Game, so the gameActivity doesn't need to.
 *
 */
public class GameManager {

    /** Game Field **/
    private ArrayList<Tower> towers;
    private ArrayList<Enemy> currentWave;
    private ArrayList<Enemy> spawnWave;
    private ArrayList<Tile> tiles;
    private ArrayList<Coins> coins;
    // path[0] = start Tile, path[path.length - 1] = finish Tile
    private Point[] path;
    private GrassTile selectedTile;
    private ArrayList<Tile> pathTiles;

    /** Graphical attributes **/
    // castle resembles the player
    private Bitmap castleImage;
    private int screenWidth;
    private int screenHeight;
    private BoundingBox2D castle;
    private boolean showCastleHitbox = false;

    /** Player stats **/
    // wave we are currently in
    private int wave = 0;
    // current gold
    private int gold = 0;
    //current player health
    private int playerHealth = 100;

    private int shop = 2;
    private int enemiesPerWave = 2;

    /** Else **/
    public static AppManager APP_MANAGER;
    private Context appContext;
    private Random random;
    private long timeofLastSpawn;
    private int waveCounter = 0;
    private boolean inWave = false;
    private final int NUM_OF_TILES_IN_WIDTH = 8;
    private final int NUM_OF_TILES_IN_HEIGHT = 5;
    private boolean lockGameField = true;

    private boolean hatGewonnen = false;

    /**
     * Constructor initializes important attributes
     *
     * @param path Enemies follow this path until they reach the castle
     * @param appContext Used to gibe the App_Manager an activity
     */
    public GameManager(Point[] path, Context appContext){
        // Random generator for creating enemies
        random = new Random();

        // Context allows cast to activity
        this.appContext = appContext;
        APP_MANAGER = new AppManager((Activity) appContext);

        // Gets created by GameManager
        currentWave = new ArrayList<>();
        spawnWave = new ArrayList<>();
        towers = new ArrayList<>();
        tiles = new ArrayList<>();
        coins = new ArrayList<>();

        // Path needed by enemies
        this.path = path;

        createCastle();

        generateTiles(appContext.getResources());
        //get 5 gold at start of game
        gainGold(5);
    }



    public void generateTiles(Resources resources){
        Random rand = new Random(APP_MANAGER.getPlayedLevel());
        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int tmpScreenWidth = (int) (screenWidth * 0.8);
        int tmpScreenHeight = (int) (screenHeight * 0.8);

        int tileSizeX = tmpScreenWidth / NUM_OF_TILES_IN_WIDTH;
        int tileSizeY = tmpScreenHeight / NUM_OF_TILES_IN_HEIGHT;

        // gernerating tile only for image, so position is unnecessary
        GrassTile tile = new GrassTile(this, 0, 0, tileSizeX, tileSizeY);
        int rowRange = tile.getImage().getWidth()/2;
        int columnRange = tile.getImage().getHeight()/2;

        int i = 0;
        int j = 0;

        List<Point> enemyPath = new ArrayList<>();

        for(int column = columnRange; column <= tmpScreenHeight; column += columnRange*2){
            for(int row = rowRange; row <= tmpScreenWidth; row += rowRange*2) {
                int posX = row;
                int posY = column;

                if((rand.nextInt() % 3) == 0){
                    tiles.add(new WaterTile(this, posX, posY, tileSizeX, tileSizeY));
                } else {
                    tiles.add(new GrassTile(this, posX, posY, tileSizeX, tileSizeY));
                }
            }
        }
        //path = enemyPath.toArray(new Point[0]);
        generatePathTiles();
    }

    public void generatePathTiles(){
        pathTiles = new ArrayList<>();

        for(Point posXY : path){
            restrictPathPoint(posXY);
            int index = (posXY.y * NUM_OF_TILES_IN_WIDTH) + posXY.x;
            Tile tile = tiles.get(index);
            tile = new PathTile(this, tile.getPosX(), tile.getPosY(), tile.getSizeX(), tile.getSizeY());
            pathTiles.add(tile);
            tiles.set(index, tile);
        }
        for(int i = 1; i <= pathTiles.size() - 2; i++){
            Tile startTile = pathTiles.get(i-1);
            Tile middleTile = pathTiles.get(i);
            Tile endTile = pathTiles.get(i+1);

            int indexMiddleTile = tiles.indexOf(middleTile);
            Log.d("GameManager", "indexOfMiddleTile = " + indexMiddleTile);
            tiles.set(indexMiddleTile, calcMiddleTile(startTile, middleTile, endTile));
        }
    }

    public void restrictPathPoint(Point pathPoint){
        int posXmax = NUM_OF_TILES_IN_WIDTH - 1;
        int posXmin = 0;
        int posYmax = NUM_OF_TILES_IN_HEIGHT - 1;
        int posYmin = 0;

        if(pathPoint.x > posXmax){
            pathPoint.x = posXmax;
        }
        if(pathPoint.x < posXmin){
            pathPoint.x = posXmin;
        }
        if(pathPoint.y > posYmax){
            pathPoint.y = posYmax;
        }
        if(pathPoint.y < posYmin){
            pathPoint.y = posYmin;
        }
    }

    public Tile calcMiddleTile(Tile startTile, Tile middleTile, Tile endTile){
        int xMiddleStartDiff = middleTile.getPosX() - startTile.getPosX();
        int xMiddleEndDiff = endTile.getPosX() - middleTile.getPosX();
        int yMiddleStartDiff = middleTile.getPosY() - startTile.getPosY();
        int yMiddleEndDiff = endTile.getPosY() - middleTile.getPosY();

        if(xMiddleStartDiff == 0){
            // above or under start tile
            if(yMiddleStartDiff > 0){
                // under start tile
                if(xMiddleEndDiff > 0){
                    // right from end tile, under start tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.TOP_RIGHT);
                }
                if(xMiddleEndDiff < 0){
                    // left from end tile, under start tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.TOP_LEFT);
                }
            }
            if(yMiddleStartDiff < 0){
                // above start tile
                if(xMiddleEndDiff > 0){
                    // right from end tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.BOT_RIGHT);
                }
                if(xMiddleEndDiff < 0){
                    // left from end tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.BOT_LEFT);
                }
            }
            if(xMiddleEndDiff == 0){
                // above or under end tile
                return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.TOP_BOT);
            }
        }
        if(yMiddleStartDiff == 0){
            // left or right start tile
            if(xMiddleStartDiff > 0){
                // right start tile
                if(yMiddleEndDiff > 0){
                    // above from end tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.BOT_LEFT);
                }
                if(yMiddleEndDiff < 0){
                    // under from end tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.TOP_LEFT);
                }
            }
            if(xMiddleStartDiff < 0){
                // left start tile
                if(yMiddleEndDiff > 0){
                    // above from end tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.TOP_RIGHT);
                }
                if(yMiddleEndDiff < 0){
                    // under from end tile
                    return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.BOT_RIGHT);
                }
            }
            if(yMiddleEndDiff == 0){
                // left or right to end tile
                return new PathTile(this, middleTile.getPosX(), middleTile.getPosY(), middleTile.getSizeX(), middleTile.getSizeY(), pathDirection.LEFT_RIGHT);
            }
        }
        return null;
    }

    public ArrayList<Enemy> getCurrentWave() {
        return currentWave;
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }

    public void setCurrentWave(ArrayList<Enemy> currentWave) {
        if(currentWave == null){
            //Log.d("GameManager", "setCurrentWave() - Exception: currentWave == null");
            this.currentWave = new ArrayList<>();
        } else {
            this.currentWave = currentWave;
        }
    }

    public ArrayList<Enemy> getSpawnWave() {
        return spawnWave;
    }

    // spawnWave used holds all enemies, that won't be deleted
    public void setSpawnWave(ArrayList<Enemy> spawnWave) {
        if(spawnWave == null){
            //Log.d("GameManager", "setCurrentWave() - Exception: currentWave == null");
            this.spawnWave = new ArrayList<>();
        } else {
            this.spawnWave = spawnWave;
        }
    }

    public void createWave(){
        //wave++;
        Log.d("GameManager", "createWave() - creating wave number: " + wave);
        for(int i = 0; i < wave*enemiesPerWave ; i++){
            currentWave.add(createEnemy());
            //currentWave.get(currentWave.size()-1).showHitbox(true);
        }
        // used for iterating
        spawnWave = new ArrayList<Enemy>(currentWave);
        Log.d("GameManager", "createWave() - spawnWave = " + spawnWave.size());
    }

    // Gets called by User interface
    public void startWave(){
        wave++;
        Log.d("Test", "ist in start Wavexxx " + wave);
        if((Activity) appContext instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity) appContext;
            gameActivity.wave = this.wave;
        }
        if(currentWave.isEmpty()){
            createWave();
            inWave = true;
            // Enable game field
            lockGameField = false;
            // reset global iterator for spawn wave
            waveCounter = 0;
            Log.d("GameManager", "startWave() - wave has been started, inWave = true");
            //TODO:Kaufmenü ausblenden
        }
    }

    public void controlWave(){
        if(inWave){
            spawnEnemy();
            if(currentWave.isEmpty()) {
                if((Activity) appContext instanceof GameActivity) {
                    GameActivity gameActivity = (GameActivity) appContext;
                    gameActivity.inUpgradeFragment = false;
                    if((wave + 1) > gameActivity.maxWave){
                        Log.d("Test", "ist in start Wave " + wave + " " + gameActivity.maxWave);
                        gameOver();
                        hatGewonnen = true;
                        return; //sponsored by marc
                    }
                }
                inWave = false;
                lockGameField = true;
                this.shop = 1;
                openBuyOrUpgradeMenu(null, 2);
                Log.d("GameManager", "controlWave() - wave over. inWave = false");
            }
        }
    }

    public Enemy createEnemy(){
        int enemyType = (random.nextInt() % 2) + 1;
        Enemy enemy = new Enemy(this);
        if(enemyType == 1){
            enemy.initEnemy1(wave);
        } else {
            enemy.initEnemy2(wave);
        }
        return enemy;
    }

    public void spawnEnemy(){
        if(waveCounter >= spawnWave.size()){
            return;
        }

        double timeSinceLastSpawn = (System.nanoTime() - timeofLastSpawn) / 100000000;
        if(timeSinceLastSpawn >= 20 - wave ) {
            spawnWave.get(waveCounter).setVelocity(5);
            timeofLastSpawn = System.nanoTime();
            waveCounter++;
        }
    }

    public Point[] getPath() {
        return path;
    }

    public void setPath(Point[] path) {
        this.path = path;
    }

    public BoundingBox2D getCastle() {
        return castle;
    }

    public void createCastle(){
        this.castleImage = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.castle);

        this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        float aspectRatio = (float) castleImage.getWidth() / (float) castleImage.getHeight();

        int width = (int) (screenWidth * 0.2); // Width is equal to 20% of total width
        int height = (int)(screenHeight * 0.8);

        castleImage = Bitmap.createScaledBitmap(castleImage, width, height, false);

        int up = 0;
        int left = (int)(screenWidth * 0.8);
        int bottom = (int)(screenHeight * 0.8);
        int right = screenWidth;

        castle = new BoundingBox2D(up, left, bottom, right);
    }

    public void takeDamage(int damageTaken){
        if(damageTaken < 0){
            //Log.d("GameManager", "takeDamage() - Exception: damageTaken < ß");
            damageTaken = 0;
        }
        this.playerHealth -= damageTaken;
        if((Activity) appContext instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity) appContext;
            gameActivity.health = this.playerHealth;
        }
        if(playerHealth <= 0){
            gameOver();
            hatGewonnen = false;
        }
    }

    public void checkForEnemyCollision() {
        for (Enemy e : currentWave){
            if(castle.collidesWith(e.getHitbox())){
                takeDamage(e.getDamage());
                //currentWave.remove(e);
                takeDamage(e.getDamage());
                e.die();
                return;
            }
        }
    }

    private double calculateDistance(int firstPosX, int firstPosY, int secondPosX, int secondPosY){
        return Math.sqrt(Math.pow(secondPosX - firstPosX,2) + Math.pow(secondPosY - firstPosY, 2));
    }

    private Tile calculateTileClickedOn(int posX, int posY){
        double minDist = 9999999; //init max
        Tile minTile = null;
        for (Tile tile : tiles) {
            double dist = calculateDistance(tile.getPosX(), tile.getPosY(), posX, posY);
            if (dist < minDist){
                minDist = dist;
                minTile = tile;
            }
        }
        return minTile;
    }

    public void placeTower(int posX, int posY, int towerType, List<Tower> towerList){
        Tile tile = calculateTileClickedOn(posX, posY);
        //If tower already existed then dont place tower
        for (Tower tower : towerList) {
            if (tower.getPosX() == tile.getPosX() && tower.getPosY() == tile.getPosY()){
                return;
            }
        }
        //else then place a tower
        Tower test = new Tower(this, towerType, tile.getPosX(), tile.getPosY(), selectedTile);
        test.showHitbox(true);
        towerList.add(test);
        //TODO: tower aren't allowed on waterTiles or pathTiles
    }

    public void placeTower2(int towerType){
        if(selectedTile != null ){
            if(selectedTile.isHasTower()){
                //if selectedTile alrady has a tower STOP
                return;
            }
            else if(gold >= towerType*2){
                gold -= towerType*2;
                if((Activity) appContext instanceof GameActivity) {
                    GameActivity gameActivity = (GameActivity) appContext;
                    gameActivity.gold = this.gold;
                }
                towers.add(new Tower(this, towerType, selectedTile.getPosX(), selectedTile.getPosY(), selectedTile));
                towers.get(towers.size()-1).showHitbox(true);

            }
        }
    }
    public void upgradeTower(){
        if(selectedTile != null ) {
            if (!selectedTile.isHasTower()) {
                //if selectedTile does not have a tower STOP
                return;
            }
            //if you have enough gold
            else if(gold >= selectedTile.getTower().getTowerType()*2*2 && selectedTile.getTower().getLevel() < 2){
                gold -= selectedTile.getTower().getTowerType()*2*2;
                if((Activity) appContext instanceof GameActivity) {
                    GameActivity gameActivity = (GameActivity) appContext;
                    gameActivity.gold = this.gold;
                }
                selectedTile.getTower().upgrade();
            }
        }
    }

    public void gameOver(){
        //TODO STOP GAMETHREAD, DISPLAY GAME OVER MESSAGE, SEND BACK TO MAIN MENU
        if((Activity) appContext instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity) appContext;
            Log.d("Test", "gameOver Methode");
            closeBuyOrUpgradeMenu();
            gameActivity.gameView.thread.setRunning(false);

        }
    }


    public void upgradeTower(Tower tower){
        for(Tower t : towers)
            if(t == tower){
                t.upgrade();
                return;
            }
    }

    public int sellTower(Tower tower){
        int price = tower.getPrice();
        for(Tower t : towers)
            if(t == tower) {
                towers.remove(t);
                return price;
            }
        return price;
    }

    public void gainGold(int gold){
        this.gold += gold;
        if((Activity) appContext instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity) appContext;
            gameActivity.gold = this.gold;
        }
    }

    public void saveGame(){
        //TODO: save stats in APP_MANAGER
    }

    public void loadGame(){
        //TODO:take data from APP_MANAGER and rebuild earlier game
    }

    public void startGame(){
        //TODO: initialize all needed attributes and start the game (run the game thread)
    }

    public void pauseGame(GameView gameView){
//        gameView.thread.setRunning(false);
        //TODO: set game thread running to false, so the game pauses
    }

    public void openBuyOrUpgradeMenu(GrassTile customer, int shop){
        this.selectedTile = customer;

        if(this.shop >= 0){
            this.shop = shop % 3;
        }
        if((Activity) appContext instanceof GameActivity){
            GameActivity gameActivity = (GameActivity) appContext;
            Log.d("GameManager", "this.shop" + " " + this.shop);
            gameActivity.shopMenu = this.shop;
        }
    }

    public void closeBuyOrUpgradeMenu(){
        openBuyOrUpgradeMenu(null, -1);
        this.selectedTile = null;
    }

    public int getShop(){ return shop; }

    public void update(int posX, int posY, double lastFrameDur){
        // TODO: write update method

        // Check for game logic first
        // spawn enemies, if there are still enemies in wave left
        controlWave();
        // checks collision with castle and inflicts playerHealth damage
        checkForEnemyCollision();

        // then check logic for game objects
        if(!lockGameField){
            for(Tile t : tiles){
                if(t instanceof GrassTile)
                    ((GrassTile) t).update(posX, posY);
            }
            for(Tower t : towers){
                t.update(lastFrameDur);
            }
            for(Enemy e : currentWave){
                e.update(lastFrameDur);
            }
        }
        if((Activity) appContext instanceof GameActivity){
            GameActivity gameActivity = (GameActivity) appContext;
            Log.d("GameManager", "this.shop" + " " + this.shop);
            gameActivity.refresh();
        }
    }

    public void draw(Canvas canvas) {
        // TODO: write draw method
        if(!tiles.isEmpty()){
            for(Tile t : tiles){
                t.draw(canvas);
            }
        }
        if(!currentWave.isEmpty()){
            for(Enemy e : currentWave){
                e.draw(canvas);
            }
        }
        if(!towers.isEmpty()){
            for(Tower t : towers){
                t.draw(canvas);
            }
        }
        if(coins != null){
            for(Coins c : coins){
                c.draw(canvas);
            }
        }
        drawCastle(canvas);
    }

    public void drawCastle(Canvas canvas){
        float left = (float) (screenWidth * 0.8);
        float top = 0;

        canvas.drawBitmap(castleImage, left, top, null);

        if(showCastleHitbox){
            Paint hitboxPaint = new Paint();
            hitboxPaint.setColor(Color.CYAN);
            hitboxPaint.setStrokeWidth(5);
            hitboxPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(castle.getLeft(), castle.getUp(), castle.getRight(), castle.getBottom(), hitboxPaint);
        }
    }

    public void enemyDefeated(){
        if((Activity) appContext instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity) appContext;
            gameActivity.enemiesDefeated += 1;
        }
    }

    public void toggleCastleHitbox(){ showCastleHitbox = !showCastleHitbox; }

    public Context getAppContext(){ return appContext; }

    public boolean isInWave(){ return inWave; }

    public ArrayList<Coins> getCoins() {
        return coins;
    }

    public void setCoins(ArrayList<Coins> coins) {
        if(coins == null){
            //Log.d("GameManager", "setCurrentWave() - Exception: currentWave == null");
            this.coins = new ArrayList<>();
        } else {
            this.coins = coins;
        }
    }

    public ArrayList<Tile> getTiles(){ return tiles; }

    public ArrayList<Tile> getPathTiles(){ return pathTiles; }

    public GrassTile getSelectedTile(){ return selectedTile; }

    public void setShop(int shop) {
        this.shop = shop;
    }


    public void setHatGewonnen(boolean bool){
        this.hatGewonnen = bool;
    }

    public boolean getHatGewonnen(){
        return hatGewonnen;
    }


    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }
}
