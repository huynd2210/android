package hda.nzse.tower_defense;


// TODO: Self-written classes, not extensively tested

import android.util.Pair;

/*
 Bounding Sphere for round objects (in 2D, so rather a Bounding Circle)
 Main functionality: checks whether this object collides with other objects (that also have a
 BoundingSphere2D assigned) .
 Attributes: position saves the coordinates of the mid point / center of the object,
 radius the expansion of the sphere
*/

public class BoundingSphere2D {

    private double radius;
    private Pair<Integer, Integer> position;

    BoundingSphere2D(double radius, Pair<Integer, Integer> position){
        this.radius = radius;
        this.position = position;
    }

    BoundingSphere2D(double radius, int x, int y){
        this.radius = radius;
        position = new Pair<>(x, y);
    }

    // Returns TRUE if THIS BoundingSphere2D collides/overlaps with OTHER BoundingSphere2D, FALSE otherwise
    public boolean collidesWith(BoundingSphere2D other){
        // generate vector from THIS to OTHER
        hda.nzse.tower_defense.Vector2D dif = new hda.nzse.tower_defense.Vector2D(position.first - other.getPosition().first,
                position.second - other.getPosition().second );

        //Length should be higher than both radii together or they overlap
        return dif.length() <= radius + other.getRadius();
    }

    // ** Getter and Setter **

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public int getPosX(){
        return position.first;
    }

    public int getPosY(){
        return position.second;
    }

    public void setPosition(Pair<Integer, Integer> position) {
        this.position = position;
    }

    public void setPosition(int x, int y){
        position = new Pair<>(x, y);
    }

}
