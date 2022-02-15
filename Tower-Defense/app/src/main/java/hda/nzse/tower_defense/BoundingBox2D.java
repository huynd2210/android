package hda.nzse.tower_defense;


// TODO: Self-written classes, not extensively tested

import android.util.Pair;

/*
 Bounding Box for rectangle objects.
 Main functionality: checks whether this object collides with other objects (that also have a
 BoundingBox2d assigned) .
 Attributes: upperLeft and lowerRight save the coordinates of the edges of the bounding box.
 */

public class BoundingBox2D {

    private Pair<Integer, Integer> upperLeft;
    private Pair<Integer, Integer> lowerRight;

    BoundingBox2D(int up, int left, int bottom, int right){
        upperLeft = new Pair<>(up, left);
        lowerRight = new Pair<>(bottom, right);

    }

    BoundingBox2D(Pair<Integer, Integer> upperLeft, Pair<Integer, Integer> lowerRight ){
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
    }

    // Returns TRUE if THIS BoundingBox2D collides/overlaps with OTHER BoundingBox2D, FALSE otherwise
    public boolean collidesWith(BoundingBox2D other){
        return !((lowerRight.first < other.upperLeft.first) ||
                (upperLeft.first > other.lowerRight.first) ||
                (upperLeft.second > other.lowerRight.second) ||
                (lowerRight.second < other.upperLeft.second));
    }

    // ** Getter and Setter **

    public Pair<Integer, Integer> getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(Pair<Integer, Integer> upperLeft) {
        this.upperLeft = upperLeft;
    }

    public Pair<Integer, Integer> getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(Pair<Integer, Integer> lowerRight) {
        this.lowerRight = lowerRight;
    }

    public int getUp(){
        return upperLeft.first;
    }

    public int getLeft(){
        return upperLeft.second;
    }

    public int getBottom(){
        return lowerRight.first;
    }

    public int getRight(){
        return lowerRight.second;
    }

    public void setUp(int up){
        upperLeft = new Pair<>(up, upperLeft.second);
    }

    public void setLeft(int left){
        upperLeft = new Pair<>(upperLeft.first, left);
    }

    public void setRight(int right){
        lowerRight = new Pair<>(lowerRight.first, right);
    }

    public void setBottom(int bottom){
        lowerRight = new Pair<>(bottom, lowerRight.second);
    }
}
